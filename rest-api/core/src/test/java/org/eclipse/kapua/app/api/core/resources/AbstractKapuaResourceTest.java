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
package org.eclipse.kapua.app.api.core.resources;

import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Category(JUnitTests.class)
public class AbstractKapuaResourceTest extends Assert {

    private class AbstractKapuaResourceImpl extends AbstractKapuaResource {

    }

    AbstractKapuaResource abstractKapuaResource;
    Object[] objects;

    @Before
    public void initialize() {
        abstractKapuaResource = new AbstractKapuaResourceImpl();
        objects = new Object[]{new Object(), "", "string", 10, 'c', KapuaId.ONE, new Throwable(), new ScopeId("111")};
    }

    @Test
    public void returnNotNullEntityTest() {
        for (Object object : objects) {
            assertEquals("Expected and actual values should be the same.", object, abstractKapuaResource.returnNotNullEntity(object));
        }
    }

    @Test
    public void returnNotNullEntityNullTest() {
        try {
            abstractKapuaResource.returnNotNullEntity(null);
            fail("WebApplicationException expected.");
        } catch (Exception e) {
            assertEquals("WebApplicationException expected.", new WebApplicationException(Response.Status.NOT_FOUND).toString(), e.toString());
        }
    }

    @Test
    public void returnOkTest() {
        assertEquals("Expected and actual values should be the same.", "OutboundJaxrsResponse{status=200, reason=OK, hasEntity=false, closed=false, buffered=false}", abstractKapuaResource.returnOk().toString());
    }

    @Test
    public void returnNoContentTest() {
        assertEquals("Expected and actual values should be the same.", "OutboundJaxrsResponse{status=204, reason=No Content, hasEntity=false, closed=false, buffered=false}", abstractKapuaResource.returnNoContent().toString());
    }

    @Test
    public void returnCreatedTest() {
        for (Object object : objects) {
            assertEquals("Expected and actual values should be the same.", "OutboundJaxrsResponse{status=201, reason=Created, hasEntity=true, closed=false, buffered=false}", abstractKapuaResource.returnCreated(object).toString());
            assertEquals("Expected and actual values should be the same.", object, abstractKapuaResource.returnCreated(object).getEntity());
        }
    }
}
