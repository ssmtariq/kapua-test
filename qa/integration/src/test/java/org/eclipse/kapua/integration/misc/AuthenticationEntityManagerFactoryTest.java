/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.integration.misc;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.shiro.AuthenticationEntityManagerFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class AuthenticationEntityManagerFactoryTest extends Assert {

    @Test
    public void authenticationEntityManagerFactoryTest() throws Exception {
        Constructor<AuthenticationEntityManagerFactory> authenticationEntityManagerFactory = AuthenticationEntityManagerFactory.class.getDeclaredConstructor();
        authenticationEntityManagerFactory.setAccessible(true);
        authenticationEntityManagerFactory.newInstance();
        assertTrue("True expected.", Modifier.isPrivate(authenticationEntityManagerFactory.getModifiers()));
    }

    @Test
    public void getEntityManagerTest() throws KapuaException {
        assertTrue("True expected.", AuthenticationEntityManagerFactory.getEntityManager() instanceof EntityManager);
    }

    @Test
    public void getInstanceTest() {
        assertTrue("True expected.", AuthenticationEntityManagerFactory.getInstance() instanceof AuthenticationEntityManagerFactory);
    }
} 