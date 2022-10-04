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
package org.eclipse.kapua.app.api.core.model.device.management;

import org.eclipse.kapua.app.api.core.model.message.JsonKapuaPayload;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestChannel;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.storable.model.id.StorableId;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

@Category(JUnitTests.class)
public class JsonGenericRequestMessageTest extends Assert {

    GenericRequestMessage genericRequestMessage;
    UUID id;
    StorableId storableId;
    Date timestamp, receivedOn, sentOn, capturedOn;
    Date[] dates;
    String clientId;
    KapuaId scopeId, deviceId;
    KapuaPosition kapuaPosition;
    GenericRequestChannel genericRequestChannel;
    GenericRequestPayload genericRequestPayload;
    JsonGenericRequestMessage jsonGenericRequestMessage1, jsonGenericRequestMessage2;
    KapuaPayload newKapuaPayload;

    @Before
    public void initialize() {
        genericRequestMessage = Mockito.mock(GenericRequestMessage.class);
        id = new UUID(10L, 100L);
        storableId = Mockito.mock(StorableId.class);
        timestamp = new Date();
        scopeId = KapuaId.ONE;
        clientId = "clientID";
        deviceId = KapuaId.ONE;
        receivedOn = new Date();
        sentOn = new Date();
        capturedOn = new Date();
        dates = new Date[]{new Date(), new Date(99L), new Date(99999999999999L)};
        kapuaPosition = Mockito.mock(KapuaPosition.class);
        genericRequestChannel = Mockito.mock(GenericRequestChannel.class);
        genericRequestPayload = Mockito.mock(GenericRequestPayload.class);
        newKapuaPayload = Mockito.mock(KapuaPayload.class);

        Mockito.when(genericRequestMessage.getId()).thenReturn(id);
        Mockito.when(genericRequestMessage.getScopeId()).thenReturn(scopeId);
        Mockito.when(genericRequestMessage.getDeviceId()).thenReturn(deviceId);
        Mockito.when(genericRequestMessage.getClientId()).thenReturn(clientId);
        Mockito.when(genericRequestMessage.getReceivedOn()).thenReturn(receivedOn);
        Mockito.when(genericRequestMessage.getSentOn()).thenReturn(sentOn);
        Mockito.when(genericRequestMessage.getCapturedOn()).thenReturn(capturedOn);
        Mockito.when(genericRequestMessage.getPosition()).thenReturn(kapuaPosition);
        Mockito.when(genericRequestMessage.getChannel()).thenReturn(genericRequestChannel);
        Mockito.when(genericRequestMessage.getPayload()).thenReturn(genericRequestPayload);

        jsonGenericRequestMessage1 = new JsonGenericRequestMessage();
        jsonGenericRequestMessage2 = new JsonGenericRequestMessage(genericRequestMessage);
    }

    @Test
    public void jsonGenericRequestMessageWithoutParameterTest() {
        JsonGenericRequestMessage jsonGenericRequestMessage = new JsonGenericRequestMessage();

        assertNull("Null expected.", jsonGenericRequestMessage.getId());
        assertNull("Null expected.", jsonGenericRequestMessage.getScopeId());
        assertNull("Null expected.", jsonGenericRequestMessage.getDeviceId());
        assertNull("Null expected.", jsonGenericRequestMessage.getClientId());
        assertNull("Null expected.", jsonGenericRequestMessage.getReceivedOn());
        assertNull("Null expected.", jsonGenericRequestMessage.getSentOn());
        assertNull("Null expected.", jsonGenericRequestMessage.getCapturedOn());
        assertNull("Null expected.", jsonGenericRequestMessage.getPosition());
        assertNull("Null expected.", jsonGenericRequestMessage.getChannel());
        assertNull("Null expected.", jsonGenericRequestMessage.getPayload());
    }

    @Test
    public void jsonGenericRequestMessageWithParameterTest() {
        JsonGenericRequestMessage jsonGenericRequestMessage = new JsonGenericRequestMessage(genericRequestMessage);

        assertEquals("Expected and actual values should be the same.", id, jsonGenericRequestMessage.getId());
        assertEquals("Expected and actual values should be the same.", scopeId, jsonGenericRequestMessage.getScopeId());
        assertEquals("Expected and actual values should be the same.", deviceId, jsonGenericRequestMessage.getDeviceId());
        assertEquals("Expected and actual values should be the same.", clientId, jsonGenericRequestMessage.getClientId());
        assertEquals("Expected and actual values should be the same.", receivedOn, jsonGenericRequestMessage.getReceivedOn());
        assertEquals("Expected and actual values should be the same.", sentOn, jsonGenericRequestMessage.getSentOn());
        assertEquals("Expected and actual values should be the same.", capturedOn, jsonGenericRequestMessage.getCapturedOn());
        assertEquals("Expected and actual values should be the same.", kapuaPosition, jsonGenericRequestMessage.getPosition());
        assertEquals("Expected and actual values should be the same.", genericRequestChannel, jsonGenericRequestMessage.getChannel());
        assertNotNull("NotNull expected.", jsonGenericRequestMessage.getPayload());
    }

    @Test(expected = NullPointerException.class)
    public void jsonGenericRequestMessageWithNullParameterTest() {
        new JsonGenericRequestMessage(null);
    }

    @Test
    public void setAndGetIdTest() {
        UUID newId = new UUID(1L, 99L);

        jsonGenericRequestMessage1.setId(newId);
        jsonGenericRequestMessage2.setId(newId);

        assertEquals("Expected and actual values should be the same.", newId, jsonGenericRequestMessage1.getId());
        assertEquals("Expected and actual values should be the same.", newId, jsonGenericRequestMessage2.getId());

        jsonGenericRequestMessage1.setId(null);
        jsonGenericRequestMessage2.setId(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getId());
        assertNull("Null expected.", jsonGenericRequestMessage2.getId());
    }

    @Test
    public void setAndGetScopeIdTest() {
        KapuaId newScopeId = KapuaId.ANY;

        jsonGenericRequestMessage1.setScopeId(newScopeId);
        jsonGenericRequestMessage2.setScopeId(newScopeId);

        assertEquals("Expected and actual values should be the same.", newScopeId, jsonGenericRequestMessage1.getScopeId());
        assertEquals("Expected and actual values should be the same.", newScopeId, jsonGenericRequestMessage2.getScopeId());

        jsonGenericRequestMessage1.setScopeId(null);
        jsonGenericRequestMessage2.setScopeId(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getScopeId());
        assertNull("Null expected.", jsonGenericRequestMessage2.getScopeId());
    }

    @Test
    public void setAndGetDeviceIdTest() {
        KapuaId newDeviceId = KapuaId.ANY;

        jsonGenericRequestMessage1.setDeviceId(newDeviceId);
        jsonGenericRequestMessage2.setDeviceId(newDeviceId);

        assertEquals("Expected and actual values should be the same.", newDeviceId, jsonGenericRequestMessage1.getDeviceId());
        assertEquals("Expected and actual values should be the same.", newDeviceId, jsonGenericRequestMessage2.getDeviceId());

        jsonGenericRequestMessage1.setDeviceId(null);
        jsonGenericRequestMessage2.setDeviceId(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getDeviceId());
        assertNull("Null expected.", jsonGenericRequestMessage2.getDeviceId());
    }

    @Test
    public void setAndGetClientIdTest() {
        String[] newClientIDs = {"ID", ",.  id *&64930 new ID ,,,", "  NEW12 ,./)(*&%^% IDnew", "newID  98*90__=88id ", ",,,.id new ID 847&^3#@!  "};

        for (String newClientID : newClientIDs) {
            jsonGenericRequestMessage1.setClientId(newClientID);
            jsonGenericRequestMessage2.setClientId(newClientID);

            assertEquals("Expected and actual values should be the same.", newClientID, jsonGenericRequestMessage1.getClientId());
            assertEquals("Expected and actual values should be the same.", newClientID, jsonGenericRequestMessage2.getClientId());
        }

        jsonGenericRequestMessage1.setClientId(null);
        jsonGenericRequestMessage2.setClientId(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getClientId());
        assertNull("Null expected.", jsonGenericRequestMessage2.getClientId());
    }

    @Test
    public void setAndGetReceivedOnTest() {
        for (Date newReceivedOn : dates) {
            jsonGenericRequestMessage1.setReceivedOn(newReceivedOn);
            jsonGenericRequestMessage2.setReceivedOn(newReceivedOn);

            assertEquals("Expected and actual values should be the same.", newReceivedOn, jsonGenericRequestMessage1.getReceivedOn());
            assertEquals("Expected and actual values should be the same.", newReceivedOn, jsonGenericRequestMessage2.getReceivedOn());
        }

        jsonGenericRequestMessage1.setReceivedOn(null);
        jsonGenericRequestMessage2.setReceivedOn(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getReceivedOn());
        assertNull("Null expected.", jsonGenericRequestMessage2.getReceivedOn());
    }

    @Test
    public void setAndGetSentOnTest() {
        for (Date newSentOn : dates) {
            jsonGenericRequestMessage1.setSentOn(newSentOn);
            jsonGenericRequestMessage2.setSentOn(newSentOn);

            assertEquals("Expected and actual values should be the same.", newSentOn, jsonGenericRequestMessage1.getSentOn());
            assertEquals("Expected and actual values should be the same.", newSentOn, jsonGenericRequestMessage2.getSentOn());
        }

        jsonGenericRequestMessage1.setSentOn(null);
        jsonGenericRequestMessage2.setSentOn(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getSentOn());
        assertNull("Null expected.", jsonGenericRequestMessage2.getSentOn());
    }

    @Test
    public void setAndGetCapturedOnTest() {
        for (Date newCapturedOn : dates) {
            jsonGenericRequestMessage1.setCapturedOn(newCapturedOn);
            jsonGenericRequestMessage2.setCapturedOn(newCapturedOn);

            assertEquals("Expected and actual values should be the same.", newCapturedOn, jsonGenericRequestMessage1.getCapturedOn());
            assertEquals("Expected and actual values should be the same.", newCapturedOn, jsonGenericRequestMessage2.getCapturedOn());
        }

        jsonGenericRequestMessage1.setCapturedOn(null);
        jsonGenericRequestMessage2.setCapturedOn(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getCapturedOn());
        assertNull("Null expected.", jsonGenericRequestMessage2.getCapturedOn());
    }

    @Test
    public void setAndGetPositionTest() {
        KapuaPosition newPosition = Mockito.mock(KapuaPosition.class);

        jsonGenericRequestMessage1.setPosition(newPosition);
        jsonGenericRequestMessage2.setPosition(newPosition);

        assertEquals("Expected and actual values should be the same.", newPosition, jsonGenericRequestMessage1.getPosition());
        assertEquals("Expected and actual values should be the same.", newPosition, jsonGenericRequestMessage2.getPosition());

        jsonGenericRequestMessage1.setPosition(null);
        jsonGenericRequestMessage2.setPosition(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getPosition());
        assertNull("Null expected.", jsonGenericRequestMessage2.getPosition());
    }

    @Test
    public void setAndGetChannelTest() {
        GenericRequestChannel newGenericRequestChannel = Mockito.mock(GenericRequestChannel.class);

        jsonGenericRequestMessage1.setChannel(newGenericRequestChannel);
        jsonGenericRequestMessage2.setChannel(newGenericRequestChannel);

        assertEquals("Expected and actual values should be the same.", newGenericRequestChannel, jsonGenericRequestMessage1.getChannel());
        assertEquals("Expected and actual values should be the same.", newGenericRequestChannel, jsonGenericRequestMessage2.getChannel());

        jsonGenericRequestMessage1.setChannel(null);
        jsonGenericRequestMessage2.setChannel(null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getChannel());
        assertNull("Null expected.", jsonGenericRequestMessage2.getChannel());
    }

    @Test
    public void setAndGetJsonKapuaPayloadTest() {
        JsonKapuaPayload newJsonKapuaPayload = Mockito.mock(JsonKapuaPayload.class);

        jsonGenericRequestMessage1.setPayload(newJsonKapuaPayload);
        jsonGenericRequestMessage2.setPayload(newJsonKapuaPayload);

        assertEquals("Expected and actual values should be the same.", newJsonKapuaPayload, jsonGenericRequestMessage1.getPayload());
        assertEquals("Expected and actual values should be the same.", newJsonKapuaPayload, jsonGenericRequestMessage2.getPayload());

        jsonGenericRequestMessage1.setPayload((JsonKapuaPayload) null);
        jsonGenericRequestMessage2.setPayload((JsonKapuaPayload) null);

        assertNull("Null expected.", jsonGenericRequestMessage1.getPayload());
        assertNull("Null expected.", jsonGenericRequestMessage2.getPayload());
    }

    @Test(expected = NullPointerException.class)
    public void setAndGetKapuaPayloadMessageWithoutParametersTest() {
        jsonGenericRequestMessage1.setPayload(newKapuaPayload);

        assertNotNull("NotNull expected.", jsonGenericRequestMessage1.getPayload());

        jsonGenericRequestMessage1.setPayload((KapuaPayload) null);
    }

    @Test(expected = NullPointerException.class)
    public void setAndGetKapuaPayloadMessageWithParameterTest() {
        jsonGenericRequestMessage2.setPayload(newKapuaPayload);

        assertNotNull("NotNull expected.", jsonGenericRequestMessage2.getPayload());

        jsonGenericRequestMessage2.setPayload((KapuaPayload) null);
    }
}