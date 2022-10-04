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
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.event.ServiceEventBusListener;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class ServiceEventClientConfigurationTest extends Assert {

    ServiceEventBusListener serviceEventBusListener;

    @Test
    public void constructorRegularTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration("address", "subscriberName", serviceEventBusListener);
        assertEquals("Expected and actual values are not equals!", "address", serviceEventClientConfiguration.getAddress());
        assertEquals("Expected and actual values are not equals!", "subscriberName", serviceEventClientConfiguration.getClientName());
        assertEquals("Expected and actual values are not equals!", serviceEventBusListener, serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorNullTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration(null , null, null);
        assertNull("Null expected!", serviceEventClientConfiguration.getAddress());
        assertNull("Null expected!", serviceEventClientConfiguration.getClientName());
        assertNull("Null expected!", serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorAddressNullTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration(null , "subscriberName", serviceEventBusListener);
        assertNull("Null expected!", serviceEventClientConfiguration.getAddress());
        assertEquals("Expected and actual values are not equals!", "subscriberName", serviceEventClientConfiguration.getClientName());
        assertEquals("Expected and actual values are not equals!", serviceEventBusListener, serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorClientNameNullTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration("address", null, serviceEventBusListener);
        assertEquals("Expected and actual values are not equals!", "address", serviceEventClientConfiguration.getAddress());
        assertNull("Null expected!", serviceEventClientConfiguration.getClientName());
        assertEquals("Expected and actual values are not equals!", serviceEventBusListener, serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorServiceEventNullTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration("address", "subscriberName", null);
        assertEquals("Expected and actual values are not equals!", "address", serviceEventClientConfiguration.getAddress());
        assertEquals("Expected and actual values are not equals!", "subscriberName", serviceEventClientConfiguration.getClientName());
        assertNull("Null expected!", serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorAddressAndNameNullTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration(null, null, serviceEventBusListener);
        assertNull("Null expected!", serviceEventClientConfiguration.getAddress());
        assertNull("Null expected!", serviceEventClientConfiguration.getClientName());
        assertEquals("Expected and actual values are not equals!", serviceEventBusListener, serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorNameAndServiceEventNullTest() {
        ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration("address", null, null);
        assertEquals("Expected and actual values are not equals!", "address", serviceEventClientConfiguration.getAddress());
        assertNull("Null expected!", serviceEventClientConfiguration.getClientName());
        assertNull("Null expected!", serviceEventClientConfiguration.getEventListener());
    }

    @Test
    public void constructorAddressAndNameCharCheckTest() {
        String[] permittedValues = {"", "!#$%&'()=?⁄@‹›€°·‚,.-;:_Èˇ¿<>«‘”’ÉØ∏{}|ÆæÒuF8FFÔÓÌÏÎÅ«»Ç◊Ñˆ¯Èˇ", "regularNaming", "regular Naming", "49", "regularNaming49", "NAMING", "246465494135646120009090049684646496468456468496846464968496844"};
        for (String value : permittedValues) {
            ServiceEventClientConfiguration serviceEventClientConfiguration = new ServiceEventClientConfiguration(value, value, serviceEventBusListener);
            assertEquals("Expected and actual values are not equals!", value, serviceEventClientConfiguration.getAddress());
            assertEquals("Expected and actual values are not equals!", value, serviceEventClientConfiguration.getClientName());
            assertEquals("Expected and actual values are not equals!", serviceEventBusListener, serviceEventClientConfiguration.getEventListener());
        }
    }
}
