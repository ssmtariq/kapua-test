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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class AccessRoleImplTest extends Assert {

    AccessRoleImpl accessRoleImpl1, accessRoleImpl2, accessRoleImpl;
    AccessRole accessRole;
    Date createdOn;

    @Before
    public void initialize() throws KapuaException {
        accessRoleImpl1 = new AccessRoleImpl();
        accessRoleImpl2 = new AccessRoleImpl(KapuaId.ONE);
        accessRole = Mockito.mock(AccessRole.class);
        createdOn = new Date();

        Mockito.when(accessRole.getId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessRole.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRole.getCreatedBy()).thenReturn(KapuaId.ANY);
        Mockito.when(accessRole.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(accessRole.getAccessInfoId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessRole.getRoleId()).thenReturn(KapuaId.ANY);

        accessRoleImpl = new AccessRoleImpl(accessRole);
    }

    @Test
    public void accessRoleImplWithoutParametersTest() {
        assertNull("Null expected.", accessRoleImpl1.getScopeId());
        assertNull("Null expected.", accessRoleImpl1.getAccessInfoId());
        assertNull("Null expected.", accessRoleImpl1.getRoleId());
    }

    @Test
    public void accessRoleImplScopeIdParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleImpl2.getScopeId());
        assertNull("Null expected.", accessRoleImpl2.getAccessInfoId());
        assertNull("Null expected.", accessRoleImpl2.getRoleId());
    }

    @Test
    public void accessRoleImplNullScopeIdParameterTest() {
        AccessRoleImpl accessRoleImplNullScopeId = new AccessRoleImpl((KapuaId) null);
        assertNull("Null expected.", accessRoleImplNullScopeId.getScopeId());
        assertNull("Null expected.", accessRoleImplNullScopeId.getAccessInfoId());
        assertNull("Null expected.", accessRoleImplNullScopeId.getRoleId());
    }

    @Test
    public void accessRoleImplAccessRoleParameterTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleImpl.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, accessRoleImpl.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessRoleImpl.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl.getRoleId());
    }

    @Test(expected = NullPointerException.class)
    public void accessRoleImplNullAccessRoleParameterTest() throws KapuaException {
        new AccessRoleImpl((AccessRole) null);
    }

    @Test
    public void setAndGetAccessInfoIdTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ANY);
        accessRoleImpl2.setAccessInfoId(KapuaId.ANY);
        accessRoleImpl.setAccessInfoId(KapuaId.ANY);
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl1.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl2.getAccessInfoId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl.getAccessInfoId());

        accessRoleImpl1.setAccessInfoId(null);
        accessRoleImpl2.setAccessInfoId(null);
        accessRoleImpl.setAccessInfoId(null);
        assertNull("Null expected.", accessRoleImpl1.getAccessInfoId());
        assertNull("Null expected.", accessRoleImpl2.getAccessInfoId());
        assertNull("Null expected.", accessRoleImpl.getAccessInfoId());
    }

    @Test
    public void setAndGetRoleIdTest() {
        accessRoleImpl1.setRoleId(KapuaId.ANY);
        accessRoleImpl2.setRoleId(KapuaId.ANY);
        accessRoleImpl.setRoleId(KapuaId.ANY);
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl1.getRoleId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl2.getRoleId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessRoleImpl.getRoleId());

        accessRoleImpl1.setRoleId(null);
        accessRoleImpl2.setRoleId(null);
        accessRoleImpl.setRoleId(null);
        assertNull("Null expected.", accessRoleImpl1.getRoleId());
        assertNull("Null expected.", accessRoleImpl2.getRoleId());
        assertNull("Null expected.", accessRoleImpl.getRoleId());
    }

    @Test
    public void hashCodeNullAccessInfoIdNullRoleIdTest() {
        assertEquals("Expected and actual values should be the same.", 961, accessRoleImpl1.hashCode());
    }

    @Test
    public void hashCodeNullRoleIdTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", 1953, accessRoleImpl1.hashCode());
    }

    @Test
    public void hashCodeNullAccessInfoIdTest() {
        accessRoleImpl1.setRoleId(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", 993, accessRoleImpl1.hashCode());
    }

    @Test
    public void hashCodeTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl1.setRoleId(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", 1985, accessRoleImpl1.hashCode());
    }

    @Test
    public void equalsSameObjectTest() {
        assertTrue("True expected.", accessRoleImpl1.equals(accessRoleImpl1));
    }

    @Test
    public void equalsNullObjectTest() {
        assertFalse("False expected.", accessRoleImpl1.equals(null));
    }

    @Test
    public void equalsObjectTest() {
        assertFalse("False expected.", accessRoleImpl1.equals(new Object()));
    }

    @Test
    public void equalsNullAccessInfoIdsNullRoleIdsTest() {
        assertTrue("True expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }

    @Test
    public void equalsNullThisAccessInfoIdNullRoleIdsTest() {
        accessRoleImpl2.setAccessInfoId(KapuaId.ONE);
        assertFalse("False expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }

    @Test
    public void equalsNullAccessInfoIdsNullThisRoleIdTest() {
        accessRoleImpl2.setRoleId(KapuaId.ANY);
        assertFalse("False expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }

    @Test
    public void equalsDifferentAccessInfoIdsTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl1.setAccessInfoId(KapuaId.ANY);
        assertFalse("False expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }

    @Test
    public void equalsEqualAccessInfoIdsNullRoleIdsTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl2.setAccessInfoId(KapuaId.ONE);
        assertTrue("True expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }

    @Test
    public void equalsEqualAccessInfoIdsDifferentRoleIdsTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl2.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl1.setRoleId(KapuaId.ANY);
        accessRoleImpl2.setRoleId(KapuaId.ONE);

        assertFalse("False expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }

    @Test
    public void equalsEqualAccessInfoIdsEqualRoleIdsTest() {
        accessRoleImpl1.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl2.setAccessInfoId(KapuaId.ONE);
        accessRoleImpl1.setRoleId(KapuaId.ONE);
        accessRoleImpl2.setRoleId(KapuaId.ONE);

        assertTrue("True expected.", accessRoleImpl1.equals(accessRoleImpl2));
    }
}