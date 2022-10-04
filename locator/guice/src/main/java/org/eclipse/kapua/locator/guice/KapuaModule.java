/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.locator.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.AbstractKapuaModule;
import org.eclipse.kapua.commons.core.InterceptorBind;
import org.eclipse.kapua.commons.core.ServiceModule;
import org.eclipse.kapua.commons.core.ServiceModuleProvider;
import org.eclipse.kapua.commons.core.ServiceModuleProviderImpl;
import org.eclipse.kapua.commons.util.ResourceUtils;
import org.eclipse.kapua.locator.KapuaProvider;
import org.eclipse.kapua.model.KapuaObjectFactory;
import org.eclipse.kapua.service.KapuaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;

public class KapuaModule extends AbstractKapuaModule {

    private static final Logger logger = LoggerFactory.getLogger(KapuaModule.class);

    /**
     * Service resource file from which the managed services are read
     */
    private static final String SERVICE_RESOURCE = "locator.xml";

    private final String resourceName;

    private Multibinder<ServiceModule> serviceModulesBindings;

    public KapuaModule(final String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    protected void configureModule() {
        try {
            // Find locator configuration file
            List<URL> locatorConfigurations = Arrays.asList(ResourceUtils.getResource(resourceName));
            if (locatorConfigurations.isEmpty()) {
                return;
            }

            // Read configurations from resource files
            URL locatorConfigURL = locatorConfigurations.get(0);
            LocatorConfig locatorConfig = LocatorConfig.fromURL(locatorConfigURL);

            // Packages are supposed to contain service implementations
            Collection<String> packageNames = locatorConfig.getIncludedPackageNames();

            // Packages that are excluded
            Collection<String> excludedPkgNames = locatorConfig.getExcludedPackageNames();

            ClassLoader classLoader = this.getClass().getClassLoader();
            ClassPath classPath = ClassPath.from(classLoader);
            boolean initialize = true;

            // Among all the classes in the configured packages, retain only the ones
            // annotated with @KapuaProvider annotation
            Set<Class<?>> providers = new HashSet<>();
            for (String packageName : packageNames) {
                // Use the class loader of this (module) class
                ImmutableSet<ClassInfo> classInfos = classPath.getTopLevelClassesRecursive(packageName);
                for (ClassInfo classInfo : classInfos) {
                    if (isExcluded(classInfo.getName(), excludedPkgNames)) {
                         logger.trace("CLASS: {} ... excluded by configuration, skip", classInfo.getName());
                         continue;
                    }
                    logger.trace("CLASS: {}", classInfo.getName());
                    Class<?> theClass = Class.forName(classInfo.getName(), !initialize, classLoader);
                    KapuaProvider serviceProvider = theClass.getAnnotation(KapuaProvider.class);
                    if (serviceProvider != null) {
                        providers.add(theClass);
                    }
                }
            }

            // Provided names are the objects provided by the module (services or factories
            Collection<String> providedInterfaceNames = locatorConfig.getProvidedInterfaceNames();

            for (String providedName : providedInterfaceNames) {

                boolean isClassBound = false;

                final String trimmedServiceLine = providedName.trim();
                Class<?> kapuaObject = Class.forName(trimmedServiceLine, !initialize, classLoader);

                // When the provided object is a service ...
                // ... add binding with a matching implementation
                if (KapuaService.class.isAssignableFrom(kapuaObject)) {
                    for (Class<?> clazz : providers) {
                        if (kapuaObject.isAssignableFrom(clazz)) {
                            ServiceResolver<KapuaService, ?> resolver = ServiceResolver.newInstance(kapuaObject, clazz);
                            bind(resolver.getServiceClass()).to(resolver.getImplementationClass()).in(Singleton.class);
                            logger.info("Bind Kapua service {} to {}", kapuaObject, clazz);
                            isClassBound = true;
                            break;
                        }
                    }

                    if (isClassBound) {
                        continue;
                    }
                }

                // When the provided object is a factory ...
                // ... add binding with a matching implementation
                if (KapuaObjectFactory.class.isAssignableFrom(kapuaObject)) {
                    for (Class<?> clazz : providers) {
                        if (kapuaObject.isAssignableFrom(clazz)) {
                            FactoryResolver<KapuaObjectFactory, ?> resolver = FactoryResolver.newInstance(kapuaObject, clazz);
                            bind(resolver.getFactoryClass()).to(resolver.getImplementationClass()).in(Singleton.class);
                            logger.info("Bind Kapua factory {} to {}", kapuaObject, clazz);
                            isClassBound = true;
                            break;
                        }
                    }

                    if (isClassBound) {
                        continue;
                    }
                }

                logger.warn("No provider found for {}", kapuaObject);
            }

            // Bind interceptors
            logger.info("Binding interceptors ...");
            for (Class<?> clazz : providers) {
                if (MethodInterceptor.class.isAssignableFrom(clazz)) {
                    InterceptorBind annotation = clazz.getAnnotation(InterceptorBind.class);
                    Class<?> parentClazz = annotation.matchSubclassOf();
                    Class<? extends Annotation> methodAnnotation = annotation.matchAnnotatedWith();

                    // Need to request injection explicitely otherwise the interceptor would not
                    // be injected.
                    MethodInterceptor interceptor = (MethodInterceptor) clazz.newInstance();
                    requestInjection(interceptor);

                    bindInterceptor(Matchers.subclassesOf(parentClazz), Matchers.annotatedWith(methodAnnotation), interceptor);
                    logger.info("Bind service interceptor {} to subclasses of {} annotated with {}", clazz, parentClazz, methodAnnotation);
                }
            }

            bind(ServiceModuleProvider.class).to(ServiceModuleProviderImpl.class).in(Singleton.class);
            serviceModulesBindings = Multibinder.newSetBinder(binder(), ServiceModule.class);

            logger.info("Binding service modules ...");
            //cast is safe by design
            for (Class<?> clazz : providers) {
                if (ServiceModule.class.isAssignableFrom(clazz)) {
                    ComponentResolver resolver = ComponentResolver.newInstance(ServiceModule.class, clazz);
                    //cast is safe by design
                    if (resolver.getProvidedClass().isAssignableFrom(clazz)) {
                        logger.info("Assignable from {}", resolver.getProvidedClass(), clazz);
                        serviceModulesBindings.addBinding().to(resolver.getImplementationClass());
                    }
                    continue;
                }
            }

            logger.trace("Binding completed");

        } catch (Exception e) {
            logger.error("Exeption configuring module", e);
            throw new KapuaRuntimeException(KapuaErrorCodes.INTERNAL_ERROR, e, "Cannot load " + SERVICE_RESOURCE);
        }
    }

    @Override
    protected void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
        super.bindInterceptor(classMatcher, Matchers.not(SyntheticMethodMatcher.getInstance()).and(methodMatcher), interceptors);
    }

    private boolean isExcluded(String className, Collection<String> excludedPkgs) {
        if (className == null || className.isEmpty()) {
            return true;
        }
        if (excludedPkgs == null || excludedPkgs.isEmpty()) {
            return false;
        }
        for(String pkg:excludedPkgs) {
            if (className.startsWith(pkg)) {
                return true;
            }
        }
        return false;
    }

}
