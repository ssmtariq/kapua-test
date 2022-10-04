/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.commons.configuration.metatype;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

@Category(JUnitTests.class)
public class EmptyTocdTest extends Assert {

    @Before
    public void createInstanceOfClass() {

        assertNotNull(emptyTocd = new EmptyTocd());
    }

    EmptyTocd emptyTocd;

    //helper list of objects
    List<Object> objList = Arrays.asList(
            new EmptyTocd("1", "name"),
            new EmptyTocd("", ""),
            new EmptyTocd("2", ""),
            new EmptyTocd("", "name2"));

    @Test
    public void emptyTocdTest() {
        EmptyTocd emptyTocd = new EmptyTocd("49", "name");
        assertEquals("tocd.id", "49", emptyTocd.getId());
        assertEquals("tocd.name", "name", emptyTocd.getName());
    }

    @Test
    public void setNameToNullTest() {
        emptyTocd.setName(null);
        assertNull(emptyTocd.getName());
    }

    @Test
    public void setAndGetNameTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularName", "regular Name", "49", "regularName49", "REGULAR", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            emptyTocd.setName(value);
            assertTrue(emptyTocd.getName().contains(value));
        }
    }

    @Test
    public void setAndGetIdToNullValueTest() {
        emptyTocd.setId(null);
        assertNull(emptyTocd.getId());
    }

    @Test
    public void setAndGetIdTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regular Id", "49", "regularId49", "ID", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            emptyTocd.setId(value);
            assertTrue(emptyTocd.getId().contains(value));
        }
    }

    @Test //no implementation for this functions
    public void setAndGetOtherAttributesToNullTest() {
        emptyTocd.setOtherAttributes(null);
        assertTrue(emptyTocd.getOtherAttributes().isEmpty());
    }

    @Test //no implementation for this functions
    public void setAndGetIconToNullTest() {
        emptyTocd.setIcon(null);
        assertTrue(emptyTocd.getIcon().isEmpty());
    }

    @Test //no implementation for this functions
    public void setDescriptionToNullTest() {
        emptyTocd.setDescription(null);
        assertNull(emptyTocd.getDescription());
    }

    @Test
    public void setAndGetDescriptionTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regular Description", "49", "regularDescription49", "DESCRIPTION", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            emptyTocd.setDescription(value);
            assertNull(emptyTocd.getDescription());
        }
    }

    @Test //no implementation for this functions
    public void setAndGetAnyToNullTest() {
        emptyTocd.setAny(null);
        assertTrue(emptyTocd.getAny().isEmpty());
    }

    @Test //no implementation for this functions
    public void setAndGetAnyToRegularTest() {
        emptyTocd.setAny(objList);
        assertTrue(emptyTocd.getAny().isEmpty());
    }

    @Test //no implementation for this functions
    public void setAndGetADToNullTest() {
        emptyTocd.setAD(null);
        assertTrue(emptyTocd.getAD().isEmpty());
    }

    @Test //this function always returns a null value
    public void getADToRegularTest() {
        assertTrue(emptyTocd.getAD().isEmpty());
    }
}
