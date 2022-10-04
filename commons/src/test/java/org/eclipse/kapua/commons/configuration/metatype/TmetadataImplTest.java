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

import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Category(JUnitTests.class)
public class TmetadataImplTest extends Assert {

    @Before
    public void createInstanceOfClasses() {
        emptyTocd = new EmptyTocd();
        tocd = new TocdImpl();
        tmetadata = new TmetadataImpl();
        tdesignate = new TdesignateImpl();
    }

    EmptyTocd emptyTocd;
    TocdImpl tocd;
    TmetadataImpl tmetadata;
    TdesignateImpl tdesignate;

    @Test
    public void getOCDTest() {
        assertTrue(tmetadata.getOCD().isEmpty());
    }

    @Test
    public void setAndGetOCDTest() {
        List<KapuaTocd> ocd = new ArrayList<>();
        ocd.add(tocd);
        ocd.add(emptyTocd);
        tmetadata.setOCD(ocd);
        assertEquals("tmetadata.ocd", ocd, tmetadata.getOCD());
    }

    @Test
    public void getDesignateTest() {
        assertTrue(tmetadata.getDesignate().isEmpty());
    }

    @Test
    public void setAndGetDesignateTest() {
        List<KapuaTdesignate> designate = new ArrayList<>();
        designate.add(tdesignate);
        tmetadata.setDesignate(designate);
        assertEquals("tmetadata.designate", designate, tmetadata.getDesignate());
    }

    @Test
    public void getAnyTest() {
        assertTrue(tmetadata.getAny().isEmpty());
    }

    @Test
    public void setAndGetAnyTest() {
        List<Object> options = new ArrayList<>();
        options.add(tocd);
        options.add(emptyTocd);
        tmetadata.setAny(options);
        assertEquals("tmetadata.ocd", options, tmetadata.getAny());
    }

    @Test
    public void setAndGetLocalizationToNullTest() {
        tmetadata.setLocalization(null);
        assertNull(tmetadata.getLocalization());
    }

    @Test
    public void setAndGetLocalizationTest() {
        String[] permittedValues = {"", "!@#$%^^&**(-()_)+/|", "regularLocalization", "regular Localization", "49", "regularLocalization49", "LOCALIZATION", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            tmetadata.setLocalization(value);
            assertTrue(tmetadata.getLocalization().contains(value));
        }
    }

    @Test
    public void testGetOtherAttribute() {
        assertTrue(tmetadata.getOtherAttributes().isEmpty());
    }

    @Test
    public void testSetAndGetOtherAttribute() {
        Map<QName, String> values = new HashMap<>();

        values.put(QName.valueOf("1"), "a");
        values.put(QName.valueOf("2"), "b");
        values.put(QName.valueOf("3"), "c");

        tmetadata.setOtherAttributes(values);
        assertEquals("tmetadata.otherAttributes", values, tmetadata.getOtherAttributes());
    }
}
