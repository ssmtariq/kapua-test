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
package org.eclipse.kapua.app.api.core.model;

import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

@Category(JUnitTests.class)
public class MetricTypeTest extends Assert {

    @Test
    public void metricTypeTest() throws KapuaIllegalArgumentException {
        String[] types = {"string", "integer", "int", "long", "float", "double", "boolean", "date", "binary"};
        Class[] expectedClasses = {String.class, Integer.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, Date.class, byte[].class};

        for (int i = 0; i < types.length; i++) {
            MetricType metricType = new MetricType(types[i]);
            assertEquals("Expected and actual values should be the same.", expectedClasses[i], metricType.getType());
        }
    }

    @Test
    public void metricEmptyTypeTest() throws KapuaIllegalArgumentException {
        MetricType metricType = new MetricType("");
        Assert.assertNull("Null expected.", metricType.getType());
    }

    @Test
    public void metricNullTypeTest() throws KapuaIllegalArgumentException {
        MetricType metricType = new MetricType(null);
        Assert.assertNull("Null expected.", metricType.getType());
    }

    @Test(expected = KapuaIllegalArgumentException.class)
    public void metricInvalidTypeTest() throws KapuaIllegalArgumentException {
        new MetricType("invalid type");
    }
}