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

import org.apache.shiro.authc.AuthenticationToken;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.shiro.JwtCredentialsImpl;
import org.eclipse.kapua.service.authentication.shiro.realm.JwtAuthenticatingRealm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class JwtAuthenticatingRealmTest extends Assert {

    JwtAuthenticatingRealm jwtAuthenticatingRealm;

    @Before
    public void initialize() throws KapuaException {
        jwtAuthenticatingRealm = new JwtAuthenticatingRealm();
    }

    @Test
    public void jwtAuthenticatingRealmTest() {
        assertEquals("Expected and actual values should be the same.", "jwtAuthenticatingRealm", jwtAuthenticatingRealm.getName());
    }

    @Test
    public void destroyNullJwtProcessorTest() throws Exception {
        try {
            jwtAuthenticatingRealm.destroy();
        } catch (Exception e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void supportsTrueTest() {
        JwtCredentialsImpl authenticationToken = new JwtCredentialsImpl("jwt", "token id");
        assertTrue("True expected.", jwtAuthenticatingRealm.supports(authenticationToken));
    }

    @Test
    public void supportsFalseTest() {
        AuthenticationToken authenticationToken = Mockito.mock(AuthenticationToken.class);
        assertFalse("False expected.", jwtAuthenticatingRealm.supports(authenticationToken));
    }
}