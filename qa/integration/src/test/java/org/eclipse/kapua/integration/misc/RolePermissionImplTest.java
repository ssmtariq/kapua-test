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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.shiro.PermissionImpl;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.shiro.RolePermissionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Date;

@Category(JUnitTests.class)
public class RolePermissionImplTest extends Assert {

    KapuaId[] scopeIds;
    Permission permission1, permission2;
    RolePermissionImpl rolePermissionImpl1, rolePermissionImpl2;
    RolePermission rolePermission;
    Date createdOn;

    @Before
    public void initialize() {
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        permission1 = Mockito.mock(Permission.class);
        permission2 = Mockito.mock(PermissionImpl.class);
        rolePermissionImpl1 = new RolePermissionImpl(KapuaId.ONE);
        rolePermissionImpl2 = new RolePermissionImpl(KapuaId.ANY);
        rolePermission = Mockito.mock(RolePermission.class);
        createdOn = new Date();

        Mockito.when(rolePermission.getId()).thenReturn(KapuaId.ANY);
        Mockito.when(rolePermission.getRoleId()).thenReturn(KapuaId.ONE);
        Mockito.when(rolePermission.getPermission()).thenReturn(permission2);
        Mockito.when(rolePermission.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(rolePermission.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(rolePermission.getCreatedOn()).thenReturn(createdOn);
    }

    @Test
    public void rolePermissionImplWithoutParametersTest() throws Exception {
        Constructor<RolePermissionImpl> rolePermissionImpl = RolePermissionImpl.class.getDeclaredConstructor();
        rolePermissionImpl.setAccessible(true);
        rolePermissionImpl.newInstance();
        assertTrue("True expected.", Modifier.isProtected(rolePermissionImpl.getModifiers()));
    }

    @Test
    public void rolePermissionImpScopeIdTest() {
        for (KapuaId scopeId : scopeIds) {
            RolePermissionImpl rolePermissionImpl = new RolePermissionImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, rolePermissionImpl.getScopeId());
            assertEquals("Expected and actual values should be the same.", new PermissionImpl(null, null, null, null), rolePermissionImpl.getPermission());
            assertNull("Null expected.", rolePermissionImpl.getRoleId());
        }
    }

    @Test
    public void rolePermissionImplScopeIdPermissionTest() {
        for (KapuaId scopeId : scopeIds) {
            RolePermissionImpl rolePermissionImpl = new RolePermissionImpl(scopeId, permission2);
            assertEquals("Expected and actual values should be the same.", scopeId, rolePermissionImpl.getScopeId());
            assertEquals("Expected and actual values should be the same.", permission2, rolePermissionImpl.getPermission());
            assertNull("Null expected.", rolePermissionImpl.getRoleId());
        }
    }

    @Test
    public void rolePermissionImplScopeIdNullPermissionTest() {
        for (KapuaId scopeId : scopeIds) {
            RolePermissionImpl rolePermissionImpl = new RolePermissionImpl(scopeId, null);
            assertEquals("Expected and actual values should be the same.", scopeId, rolePermissionImpl.getScopeId());
            assertEquals("Expected and actual values should be the same.", new PermissionImpl(null, null, null, null), rolePermissionImpl.getPermission());
        }
    }

    @Test
    public void rolePermissionImplRolePermissionTest() {
        RolePermissionImpl rolePermissionImpl = new RolePermissionImpl(rolePermission);

        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, rolePermissionImpl.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, rolePermissionImpl.getRoleId());
        assertEquals("Expected and actual values should be the same.", permission2, rolePermissionImpl.getPermission());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, rolePermissionImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, rolePermissionImpl.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, rolePermissionImpl.getCreatedOn());
    }

    @Test(expected = NullPointerException.class)
    public void rolePermissionImplNullRolePermissionTest() {
        new RolePermissionImpl((RolePermission) null);
    }

    @Test
    public void setAndGetRoleIdTest() {
        KapuaId[] roleIds = {null, KapuaId.ONE};

        RolePermissionImpl rolePermissionImpl1 = new RolePermissionImpl(KapuaId.ONE);
        RolePermissionImpl rolePermissionImpl2 = new RolePermissionImpl(KapuaId.ANY, permission2);
        RolePermissionImpl rolePermissionImpl3 = new RolePermissionImpl(rolePermission);

        for (KapuaId roleId : roleIds) {
            rolePermissionImpl1.setRoleId(roleId);
            assertEquals("Expected and actual values should be the same.", roleId, rolePermissionImpl1.getRoleId());

            rolePermissionImpl2.setRoleId(roleId);
            assertEquals("Expected and actual values should be the same.", roleId, rolePermissionImpl2.getRoleId());

            rolePermissionImpl3.setRoleId(roleId);
            assertEquals("Expected and actual values should be the same.", roleId, rolePermissionImpl3.getRoleId());
        }
    }

    @Test
    public void setAndGetPermissionToStringTest() {
        RolePermissionImpl rolePermissionImpl1 = new RolePermissionImpl(KapuaId.ONE);
        RolePermissionImpl rolePermissionImpl2 = new RolePermissionImpl(KapuaId.ANY, permission2);
        RolePermissionImpl rolePermissionImpl3 = new RolePermissionImpl(rolePermission);
        Permission[] permissions = {null, permission1, permission2};
        Permission[] expectedPermissions = {new PermissionImpl(null, null, null, null), new PermissionImpl(null, null, null, null), permission2};

        for (int i = 0; i < permissions.length; i++) {
            rolePermissionImpl1.setPermission(permissions[i]);
            assertEquals("Expected and actual values should be the same.", expectedPermissions[i], rolePermissionImpl1.getPermission());
            assertEquals("Expected and actual values should be the same.", expectedPermissions[i].toString(), rolePermissionImpl1.toString());

            rolePermissionImpl2.setPermission(permissions[i]);
            assertEquals("Expected and actual values should be the same.", expectedPermissions[i].toString(), rolePermissionImpl2.toString());

            rolePermissionImpl3.setPermission(permissions[i]);
            assertEquals("Expected and actual values should be the same.", expectedPermissions[i].toString(), rolePermissionImpl3.toString());
        }

    }

    @Test
    public void hashCodeNullRoleIdNullPermissionTest() {
        assertEquals("Expected and actual values should be the same.", 961, rolePermissionImpl1.hashCode());
    }

    @Test
    public void hashCodeNullPermissionTest() {
        rolePermissionImpl1.setRoleId(KapuaId.ONE);

        assertEquals("Expected and actual values should be the same.", 993, rolePermissionImpl1.hashCode());
    }

    @Test
    public void hashCodeNullRoleIdTest() {
        rolePermissionImpl1.setPermission(permission1);

        assertEquals("Expected and actual values should be the same.", 28630112, rolePermissionImpl1.hashCode());
    }

    @Test
    public void hashCodeTest() {
        rolePermissionImpl1.setRoleId(KapuaId.ONE);
        rolePermissionImpl1.setPermission(Mockito.mock(Permission.class));

        assertEquals("Expected and actual values should be the same.", 28630144, rolePermissionImpl1.hashCode());
    }

    @Test
    public void equalsSameObjectTest() {
        assertTrue("True expected.", rolePermissionImpl1.equals(rolePermissionImpl1));
    }

    @Test
    public void equalsNullObjectTest() {
        assertFalse("False expected.", rolePermissionImpl1.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        assertFalse("False expected.", rolePermissionImpl1.equals(new Object()));
    }

    @Test
    public void equalsNullBothRoleIdsTest() {
        assertTrue("True expected.", rolePermissionImpl1.equals(rolePermissionImpl2));
    }

    @Test
    public void equalsNullBothRoleIdsDifferentPermissionsTest() {
        rolePermissionImpl1.setPermission(permission1);
        rolePermissionImpl2.setPermission(permission2);

        assertFalse("False expected.", rolePermissionImpl1.equals(rolePermissionImpl2));
    }

    @Test
    public void equalsNullRoleIdTest() {
        rolePermissionImpl2.setRoleId(KapuaId.ONE);

        assertFalse("False expected.", rolePermissionImpl1.equals(rolePermissionImpl2));
    }

    @Test
    public void equalsDifferentRoleIdsTest() {
        rolePermissionImpl1.setRoleId(KapuaId.ONE);
        rolePermissionImpl2.setRoleId(KapuaId.ANY);

        assertFalse("False expected.", rolePermissionImpl1.equals(rolePermissionImpl2));
    }

    @Test
    public void equalsSameRoleIdsTest() {
        rolePermissionImpl1.setRoleId(KapuaId.ONE);
        rolePermissionImpl2.setRoleId(KapuaId.ONE);

        assertTrue("True expected.", rolePermissionImpl1.equals(rolePermissionImpl2));
    }

    @Test
    public void equalsSameRoleIdsDifferentPermissionsTest() {
        rolePermissionImpl1.setRoleId(KapuaId.ONE);
        rolePermissionImpl1.setPermission(permission1);
        rolePermissionImpl2.setRoleId(KapuaId.ONE);
        rolePermissionImpl2.setPermission(permission2);

        assertFalse("False expected.", rolePermissionImpl1.equals(rolePermissionImpl2));
    }
}