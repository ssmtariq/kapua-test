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
package org.eclipse.kapua.service.authorization.domain.shiro;

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.domain.DomainCreator;
import org.eclipse.kapua.service.authorization.domain.DomainListResult;
import org.eclipse.kapua.service.authorization.domain.DomainQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Category(JUnitTests.class)
public class DomainFactoryImplTest extends Assert {

    DomainFactoryImpl domainFactoryImpl;

    @Before
    public void initialize() {
        domainFactoryImpl = new DomainFactoryImpl();
    }

    @Test
    public void newCreatorNameParameterTest() {
        String[] names = {"", "  na123)(&*^&NAME  <>", "Na-,,..,,Me name ---", "-&^454536 na___,,12 NAME name    ", "! 2#@ na     meNEMA 2323", "12&^%4   ,,,. '|<>*(", "       ,,123name;;'", "12#name--765   ,.aaa!!#$%^<> "};

        for (String name : names) {
            DomainCreator domainCreator = domainFactoryImpl.newCreator(name);
            assertEquals("Expected and actual values should be the same.", name, domainCreator.getName());
            assertNull("Null expected.", domainCreator.getScopeId());
        }
    }

    @Test
    public void newCreatorNullNameParameterTest() {
        DomainCreator domainCreator = domainFactoryImpl.newCreator((String) null);
        assertNull("Null expected.", domainCreator.getName());
        assertNull("Null expected.", domainCreator.getScopeId());
    }

    @Test
    public void newListResultTest() {
        DomainListResult domainListResult = domainFactoryImpl.newListResult();
        assertTrue("True expected.", domainListResult.isEmpty());
    }

    @Test
    public void newQueryTest() {
        DomainQuery domainQuery = domainFactoryImpl.newQuery(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domainQuery.getScopeId());
        assertNull("domainQuery.sortCriteria", domainQuery.getSortCriteria());
        assertNotNull("domainQuery.defaultSortCriteria", domainQuery.getDefaultSortCriteria());
    }

    @Test
    public void newQueryNullTest() {
        DomainQuery domainQuery = domainFactoryImpl.newQuery(null);
        assertNull("Null expected.", domainQuery.getScopeId());
        assertNull("domainQuery.sortCriteria", domainQuery.getSortCriteria());
        assertNotNull("domainQuery.defaultSortCriteria", domainQuery.getDefaultSortCriteria());
    }

    @Test
    public void newEntityTest() {
        Domain domain = domainFactoryImpl.newEntity(KapuaId.ONE);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, domain.getScopeId());
    }

    @Test
    public void newEntityNullTest() {
        Domain domain = domainFactoryImpl.newEntity(null);
        assertNull("Null expected.", domain.getScopeId());
    }

    @Test(expected = NotImplementedException.class)
    public void newCreatorScopeIdParameterTest() {
        domainFactoryImpl.newCreator(KapuaId.ONE);
    }

    @Test(expected = NotImplementedException.class)
    public void newCreatorNullScopeIdParameterTest() {
        domainFactoryImpl.newCreator((KapuaId) null);
    }

    @Test
    public void cloneTest() {
        Domain domain = Mockito.mock(Domain.class);
        Set<Actions> actions = new HashSet<>();
        Date createdOn = new Date();

        Mockito.when(domain.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(domain.getScopeId()).thenReturn(KapuaId.ANY);
        Mockito.when(domain.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(domain.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(domain.getName()).thenReturn("name");
        Mockito.when(domain.getActions()).thenReturn(actions);
        Mockito.when(domain.getGroupable()).thenReturn(true);

        Domain resultDomain = domainFactoryImpl.clone(domain);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultDomain.getId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, resultDomain.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, resultDomain.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", createdOn, resultDomain.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", "name", resultDomain.getName());
        assertEquals("Expected and actual values should be the same.", actions, resultDomain.getActions());
        assertEquals("Expected and actual values should be the same.", true, resultDomain.getGroupable());
    }

    @Test(expected = NullPointerException.class)
    public void cloneNullTest() {
        domainFactoryImpl.clone(null);
    }
}