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
 *     Red Hat - improved tests coverage
 *******************************************************************************/
package org.eclipse.kapua.locator.internal;

import org.eclipse.kapua.KapuaRuntimeException;
import org.eclipse.kapua.commons.core.ServiceModuleConfiguration;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.locator.KapuaLocatorErrorCodes;
import org.eclipse.kapua.locator.guice.GuiceLocatorImpl;
import org.eclipse.kapua.locator.guice.TestService;
import org.eclipse.kapua.locator.internal.guice.FactoryA;
import org.eclipse.kapua.locator.internal.guice.FactoryB;
import org.eclipse.kapua.locator.internal.guice.FactoryC;
import org.eclipse.kapua.locator.internal.guice.FactoryD;
import org.eclipse.kapua.locator.internal.guice.ServiceA;
import org.eclipse.kapua.locator.internal.guice.ServiceB;
import org.eclipse.kapua.locator.internal.guice.ServiceC;
import org.eclipse.kapua.locator.internal.guice.extra.ServiceE;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.KapuaService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.List;

@Category(JUnitTests.class)
public class GuiceLocatorImplTest {

    private KapuaLocator locator = GuiceLocatorImpl.getInstance();

    @Ignore
    @Test
    public void shouldThrowKapuaExceptionWhenServiceIsNotAvailable() {
        try {
            locator.getService(MyService.class);
        } catch (KapuaRuntimeException e) {
            Assert.assertEquals(KapuaLocatorErrorCodes.SERVICE_UNAVAILABLE.name(), e.getCode().name());
            return;
        }
        Assert.fail();
    }

    @Ignore
    @Test
    public void shouldLoadTestService() {
        MyTestableService service = locator.getService(MyTestableService.class);
        Assert.assertTrue(service instanceof TestMyTestableService);
    }

    @Test
    public void shouldProvideServiceA() {
        Assert.assertNotNull(locator.getService(ServiceA.class));
    }

    @Test
    public void shouldProvideFactoryA() {
        Assert.assertNotNull(locator.getFactory(FactoryA.class));
    }

    @Test
    public void shouldProvideServiceB() {
        Assert.assertNotNull(locator.getService(ServiceB.class));
    }

    @Test
    public void shouldProvideFactoryB() {
        Assert.assertNotNull(locator.getFactory(FactoryB.class));
    }

    @Test
    @Ignore
    public void shouldProvideOneServiceModule() {
        Assert.assertEquals(1, ServiceModuleConfiguration.getServiceModules().size());
    }

    @Test(expected = KapuaRuntimeException.class)
    public void shouldNotProvideServiceC() {
        Assert.assertNotNull(locator.getService(ServiceC.class));
    }

    @Test(expected = KapuaRuntimeException.class)
    public void shouldNotProvideFactoryC() {
        Assert.assertNotNull(locator.getFactory(FactoryC.class));
    }

    @Test(expected = KapuaRuntimeException.class)
    public void shouldNotProvideServiceD() {
        Assert.assertNotNull(locator.getService(ServiceE.class));
    }

    @Test
    public void shouldNotFindFactoryD() {
        try {
            locator.getFactory(FactoryD.class);
            Assert.fail("getFactory must throw an exception for un-bound factories");
        } catch (KapuaRuntimeException e) {
            Assert.assertEquals(KapuaLocatorErrorCodes.FACTORY_UNAVAILABLE, e.getCode());
        } catch (Throwable e) {
            Assert.fail("Exception must be of type: " + KapuaRuntimeException.class.getName());
        }
    }

    @Test
    public void shouldProvideAll() {
        List<KapuaService> result = locator.getServices();
        Assert.assertEquals(2, result.size());
    }

    @Test(expected = KapuaRuntimeException.class)
    public void shouldNotLoad() {
        KapuaLocator locator = new GuiceLocatorImpl("locator-1.xml");
        locator.getService(ServiceC.class);
    }

    static interface MyService extends KapuaService {
    }

    interface MyTestableService extends KapuaService {

    }

    public static class MyTestableServiceImpl implements MyTestableService {

    }

    @TestService
    public static class TestMyTestableService implements MyTestableService {

    }

}
