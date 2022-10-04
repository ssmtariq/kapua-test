/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.internal;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.StringWriter;
import java.util.Map;

@Category(JUnitTests.class)
public class KapuaPayloadTest extends Assert {

    private static final String PAYLOAD_DISPLAY_STR = "Boolean=true~~Double=42.42~~Float=42.42~~Integer=42~~Long=43~~String=Big brown fox~~byte=Ym9keQA~~unknown=";

    private static final String KAPUA_PAYLOAD_XML_STR = "missing";

    @Before
    public void before() throws Exception {
        XmlUtil.setContextProvider(new MessageJAXBContextProvider());
    }

    @Test
    public void neverGetNull() {
        assertNotNull(new KapuaPayloadImpl().getMetrics());
    }

    @Test
    public void payloadGetterSetters() throws Exception {
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();
        KapuaMessageUtil.populatePayload(kapuaPayload);

        Map<String, Object> properties = kapuaPayload.getMetrics();
        byte[] body = kapuaPayload.getBody();
        assertEquals("value1", properties.get("key1"));
        assertEquals("value2", properties.get("key2"));
        assertArrayEquals(new byte[]{'b', 'o', 'd', 'y'}, body);
    }

    @Test
    public void displayStringEmpty() throws Exception {
        final KapuaPayload kapuaPayload = new KapuaPayloadImpl();
        assertEquals("", kapuaPayload.toDisplayString());
    }

    @Test
    public void displayString() throws Exception {
        final KapuaPayload kapuaPayload = new KapuaPayloadImpl();

        KapuaMessageUtil.populatePayloadWithAllTypesOfMetrics(kapuaPayload);
        kapuaPayload.getMetrics().put("null", null);

        String displayStr = kapuaPayload.toDisplayString();
        assertEquals(PAYLOAD_DISPLAY_STR, displayStr);
    }

    @Test
    @Ignore("KapuaPayload marshaling not working")
    public void marshallPayload() throws Exception {
        KapuaPayload kapuaPayload = new KapuaPayloadImpl();
        KapuaMessageUtil.populatePayload(kapuaPayload);

        StringWriter strWriter = new StringWriter();
        XmlUtil.marshal(kapuaPayload, strWriter);
        assertEquals(KAPUA_PAYLOAD_XML_STR, strWriter.toString());
    }
}
