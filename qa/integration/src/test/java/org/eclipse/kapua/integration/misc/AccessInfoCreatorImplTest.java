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

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.shiro.AccessInfoCreatorImpl;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

@Category(JUnitTests.class)
public class AccessInfoCreatorImplTest extends Assert {

    AccessInfoFactory accessInfoFactory;
    AccessInfoCreator accessInfoCreator;
    Set<KapuaId> roleId;
    Set<Permission> setPermission;
    AccessInfoCreatorImpl accessInfoCreatorImpl1, accessInfoCreatorImpl2;

    @Before
    public void initialize() {
        accessInfoFactory = KapuaLocator.getInstance().getFactory(AccessInfoFactory.class);
        accessInfoCreator = accessInfoFactory.newCreator(KapuaId.ONE);
        roleId = new HashSet<>();
        setPermission = new HashSet<>();
        roleId.add(KapuaId.ANY);
        setPermission.add(Mockito.mock(Permission.class));
        accessInfoCreator.setScopeId(KapuaId.ONE);
        accessInfoCreator.setUserId(KapuaId.ONE);
        accessInfoCreator.setRoleIds(roleId);
        accessInfoCreator.setPermissions(setPermission);

        accessInfoCreatorImpl1 = new AccessInfoCreatorImpl(accessInfoCreator);
        accessInfoCreatorImpl2 = new AccessInfoCreatorImpl(KapuaId.ONE);
    }

    @Test
    public void accessInfoCreatorImplAccessInfoCreatorParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoCreatorImpl1.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoCreatorImpl1.getUserId());
        assertEquals("Expected and actual values should be the same.", roleId, accessInfoCreatorImpl1.getRoleIds());
        assertEquals("Expected and actual values should be the same.", setPermission, accessInfoCreatorImpl1.getPermissions());
    }

    @Test(expected = NullPointerException.class)
    public void accessInfoCreatorImplNullAccessInfoCreatorParameterTest() {
        accessInfoCreatorImpl1 = new AccessInfoCreatorImpl((AccessInfoCreator) null);
    }

    @Test
    public void accessInfoCreatorScopeIdParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessInfoCreatorImpl2.getScopeId());
        assertNull("Null expected.", accessInfoCreatorImpl2.getUserId());
        assertTrue("True expected.", accessInfoCreatorImpl2.getRoleIds().isEmpty());
        assertTrue("True expected.", accessInfoCreatorImpl2.getPermissions().isEmpty());
    }

    @Test
    public void accessInfoCreatorNullScopeIdParameterTest() {
        accessInfoCreatorImpl2 = new AccessInfoCreatorImpl((KapuaId) null);

        assertNull("Null expected.", accessInfoCreatorImpl2.getScopeId());
        assertNull("Null expected.", accessInfoCreatorImpl2.getUserId());
        assertTrue("True expected.", accessInfoCreatorImpl2.getRoleIds().isEmpty());
        assertTrue("True expected.", accessInfoCreatorImpl2.getPermissions().isEmpty());
    }

    @Test
    public void setAndGetUserIdTest() {
        accessInfoCreatorImpl1.setUserId(KapuaId.ANY);
        accessInfoCreatorImpl2.setUserId(KapuaId.ANY);

        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessInfoCreatorImpl1.getUserId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessInfoCreatorImpl2.getUserId());

        accessInfoCreatorImpl1.setUserId(null);
        accessInfoCreatorImpl2.setUserId(null);
        assertNull("Null expected.", accessInfoCreatorImpl1.getUserId());
        assertNull("Null expected.", accessInfoCreatorImpl2.getUserId());
    }

    @Test
    public void setAndGetRoleIdsTest() {
        Set<KapuaId> newRoleIds = new HashSet<>();
        newRoleIds.add(KapuaId.ANY);
        newRoleIds.add(KapuaId.ONE);

        accessInfoCreatorImpl1.setRoleIds(newRoleIds);
        accessInfoCreatorImpl2.setRoleIds(newRoleIds);

        assertEquals("Expected and actual values should be the same.", newRoleIds, accessInfoCreatorImpl1.getRoleIds());
        assertEquals("Expected and actual values should be the same.", newRoleIds, accessInfoCreatorImpl2.getRoleIds());

        accessInfoCreatorImpl1.setRoleIds(null);
        accessInfoCreatorImpl2.setRoleIds(null);
        assertTrue("True expected.", accessInfoCreatorImpl1.getRoleIds().isEmpty());
        assertTrue("True expected.", accessInfoCreatorImpl2.getRoleIds().isEmpty());
    }

    @Test
    public void setAndGetPermissionsTest() {
        Set<Permission> newPermissions = new HashSet<>();
        newPermissions.add(Mockito.mock(Permission.class));
        newPermissions.add(Mockito.mock(Permission.class));

        accessInfoCreatorImpl1.setPermissions(newPermissions);
        accessInfoCreatorImpl2.setPermissions(newPermissions);

        assertEquals("Expected and actual values should be the same.", newPermissions, accessInfoCreatorImpl1.getPermissions());
        assertEquals("Expected and actual values should be the same.", newPermissions, accessInfoCreatorImpl2.getPermissions());

        accessInfoCreatorImpl1.setPermissions(null);
        accessInfoCreatorImpl2.setPermissions(null);
        assertNotNull("NotNull expected.", accessInfoCreatorImpl1.getPermissions());
        assertNotNull("NotNull expected.", accessInfoCreatorImpl2.getPermissions());
        assertTrue("True expected.", accessInfoCreatorImpl1.getPermissions().isEmpty());
        assertTrue("True expected.", accessInfoCreatorImpl2.getPermissions().isEmpty());
    }
}