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

@Category(JUnitTests.class)
public class TobjectImplTest extends Assert {

    @Before
    public void createInstanceOfClasses() {
        tobjectImpl = new TobjectImpl();
    }

    TobjectImpl tobjectImpl;

    @Test
    public void getAttributeTest() {
        assertTrue(tobjectImpl.getAttribute().isEmpty());
    }

    @Test
    public void getAnyTest() {
        assertTrue(tobjectImpl.getAny().isEmpty());
    }

    @Test
    public void setAndGetOcdrefToNullTest() {
        tobjectImpl.setOcdref(null);
        assertNull(tobjectImpl.getOcdref());
    }

    @Test
    public void setAndGetOcdrefTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularOcdref", "regular Ocdref", "49", "regularOcdref49", "OCDREF", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tobjectImpl.setOcdref(value);
            assertTrue(tobjectImpl.getOcdref().contains(value));
        }
    }

    @Test
    public void testGetOtherAttributes() {
        assertTrue(tobjectImpl.getOtherAttributes().isEmpty());
    }
}
