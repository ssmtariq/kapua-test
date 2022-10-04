/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.message.internal.device.lifecycle;

import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaLifecycleMessageFactory;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaAppsMessageTest extends Assert {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final KapuaLifecycleMessageFactory LIFECYCLE_MESSAGE_FACTORY = LOCATOR.getFactory(KapuaLifecycleMessageFactory.class);

    private static final String PAYLOAD_DISPLAY_STR = "" +
            "acceptEncoding=UTF-8" +
            "~~applicationFramework=applicationFramework" +
            "~~applicationFrameworkVersion=applicationFrameworkV-1" +
            "~~applicationIdentifiers=applicationIdentifiers" +
            "~~availableProcessors=1" +
            "~~bios=bios" +
            "~~biosVersion=biosV-1" +
            "~~connectionInterface=connectionInterface" +
            "~~connectionIp=192.168.1.1" +
            "~~containerFramework=containerFramework" +
            "~~containerFrameworkVersion=containerFrameworkV-1" +
            "~~displayName=Display Name" +
            "~~extendedProperties=" + "{\n  \"version\": \"1.0\",\n  \"properties\": {\n    \"GroupName\": {\n      \"propertyName\": \"propertyValue\"\n    }\n  }\n}" +
            "~~firmware=firmware-1" +
            "~~firmwareVersion=firmwareV-1" +
            "~~jvm=Oracle HotSpot" +
            "~~jvmProfile=desktop" +
            "~~jvmVersion=8" +
            "~~modelId=Model Id-1" +
            "~~modelName=Model Name" +
            "~~modemIccid=8991101200003204510" +
            "~~modemImei=49-015420-323751" +
            "~~modemImsi=359881234567890" +
            "~~os=Linux~~osArch=Linux x86" +
            "~~osVersion=osV-1" +
            "~~partNumber=part-1" +
            "~~serialNumber=SN-123" +
            "~~totalMemory=4" +
            "~~uptime=12";

    @Test
    public void kapuaAppsPayloadInitConstructor() {
        KapuaAppsPayload kapuaAppsPayload = populateKapuaAppsPayload();

        assertEquals("12", kapuaAppsPayload.getUptime());
        assertEquals("Display Name", kapuaAppsPayload.getDisplayName());
        assertEquals("Model Name", kapuaAppsPayload.getModelName());
        assertEquals("Model Id-1", kapuaAppsPayload.getModelId());
        assertEquals("part-1", kapuaAppsPayload.getPartNumber());
        assertEquals("SN-123", kapuaAppsPayload.getSerialNumber());
        assertEquals("firmware-1", kapuaAppsPayload.getFirmware());
        assertEquals("firmwareV-1", kapuaAppsPayload.getFirmwareVersion());
        assertEquals("bios", kapuaAppsPayload.getBios());
        assertEquals("biosV-1", kapuaAppsPayload.getBiosVersion());
        assertEquals("Linux", kapuaAppsPayload.getOs());
        assertEquals("osV-1", kapuaAppsPayload.getOsVersion());
        assertEquals("Oracle HotSpot", kapuaAppsPayload.getJvm());
        assertEquals("8", kapuaAppsPayload.getJvmVersion());
        assertEquals("desktop", kapuaAppsPayload.getJvmProfile());
        assertEquals("containerFramework", kapuaAppsPayload.getContainerFramework());
        assertEquals("containerFrameworkV-1", kapuaAppsPayload.getContainerFrameworkVersion());
        assertEquals("applicationFramework", kapuaAppsPayload.getApplicationFramework());
        assertEquals("applicationFrameworkV-1", kapuaAppsPayload.getApplicationFrameworkVersion());
        assertEquals("connectionInterface", kapuaAppsPayload.getConnectionInterface());
        assertEquals("192.168.1.1", kapuaAppsPayload.getConnectionIp());
        assertEquals("UTF-8", kapuaAppsPayload.getAcceptEncoding());
        assertEquals("applicationIdentifiers", kapuaAppsPayload.getApplicationIdentifiers());
        assertEquals("1", kapuaAppsPayload.getAvailableProcessors());
        assertEquals("4", kapuaAppsPayload.getTotalMemory());
        assertEquals("Linux x86", kapuaAppsPayload.getOsArch());
        assertEquals("49-015420-323751", kapuaAppsPayload.getModemImei());
        assertEquals("359881234567890", kapuaAppsPayload.getModemImsi());
        assertEquals("8991101200003204510", kapuaAppsPayload.getModemIccid());
        assertEquals("{\n  \"version\": \"1.0\",\n  \"properties\": {\n    \"GroupName\": {\n      \"propertyName\": \"propertyValue\"\n    }\n  }\n}", kapuaAppsPayload.getExtendedProperties());
    }

    @Test
    public void toDisplayString() throws Exception {
        KapuaAppsPayload kapuaAppsPayload = populateKapuaAppsPayload();

        String displayStr = kapuaAppsPayload.toDisplayString();
        assertEquals(PAYLOAD_DISPLAY_STR, displayStr);
    }

    @Test
    public void kapuaAppsMessageConstructor() throws Exception {
        KapuaAppsMessage kapuaAppsMessage = LIFECYCLE_MESSAGE_FACTORY.newKapuaAppsMessage();

        assertNotNull(kapuaAppsMessage);
    }

    @Test
    public void kapuaAppsChannelGetterSetters() throws Exception {
        KapuaAppsChannel kapuaAppsChannel = LIFECYCLE_MESSAGE_FACTORY.newKapuaAppsChannel();

        kapuaAppsChannel.setClientId("clientId-1");
        assertEquals("clientId-1", kapuaAppsChannel.getClientId());
    }

    /**
     * Prepare Kapua Apps payload data, data is not necessary semantically correct.
     *
     * @return all KapuaAppsPayload fields populated with data.
     */
    private static KapuaAppsPayload populateKapuaAppsPayload() {
        return new KapuaAppsPayloadImpl(
                "12",
                "Display Name",
                "Model Name",
                "Model Id-1",
                "part-1",
                "SN-123",
                "firmware-1",
                "firmwareV-1",
                "bios",
                "biosV-1",
                "Linux",
                "osV-1",
                "Oracle HotSpot",
                "8",
                "desktop",
                "containerFramework",
                "containerFrameworkV-1",
                "applicationFramework",
                "applicationFrameworkV-1",
                "connectionInterface",
                "192.168.1.1",
                "UTF-8",
                "applicationIdentifiers",
                "1",
                "4",
                "Linux x86",
                "49-015420-323751",
                "359881234567890",
                "8991101200003204510",
                "{\n" +
                        "  \"version\": \"1.0\",\n" +
                        "  \"properties\": {\n" +
                        "    \"GroupName\": {\n" +
                        "      \"propertyName\": \"propertyValue\"\n" +
                        "    }\n" +
                        "  }\n" +
                        "}"
        );
    }
}
