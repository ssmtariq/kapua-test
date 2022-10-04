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
package org.eclipse.kapua.broker.client.message;

import org.eclipse.kapua.broker.client.protocol.ProtocolDescriptor;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

@Category(JUnitTests.class)
public class CamelKapuaMessageTest {

    CamelKapuaMessage camelKapuaMessage;

    @Before
    public void initialize() {
        camelKapuaMessage = new CamelKapuaMessage(null, null, null);
    }

    @Test
    public void setAndGetMessageTest() {
        KapuaMessage kapuaMessage = Mockito.mock(KapuaMessage.class);

        Assert.assertNull("Null expected.", camelKapuaMessage.getMessage());
        camelKapuaMessage.setMessage(kapuaMessage);
        Assert.assertEquals("Expected and actual values should be the same.", kapuaMessage, camelKapuaMessage.getMessage());
        camelKapuaMessage.setMessage(null);
        Assert.assertNull("Null expected.", camelKapuaMessage.getMessage());
    }

    @Test
    public void setAndGetConnectionIdTest() {
        KapuaId connectionId = KapuaId.ONE;

        Assert.assertNull("Null expected.", camelKapuaMessage.getConnectionId());
        camelKapuaMessage.setConnectionId(connectionId);
        Assert.assertEquals("Expected and actual values should be the same.", connectionId, camelKapuaMessage.getConnectionId());
        camelKapuaMessage.setConnectionId(null);
        Assert.assertNull("Null expected.", camelKapuaMessage.getConnectionId());
    }

    @Test
    public void setAndGetConnectorDescriptorTest() {
        ProtocolDescriptor descriptor = Mockito.mock(ProtocolDescriptor.class);

        Assert.assertNull("Null expected.", camelKapuaMessage.getConnectorDescriptor());
        camelKapuaMessage.setConnectorDescriptor(descriptor);
        Assert.assertEquals("Expected and actual values should be the same.", descriptor, camelKapuaMessage.getConnectorDescriptor());
        camelKapuaMessage.setConnectorDescriptor(null);
        Assert.assertNull("Null expected.", camelKapuaMessage.getConnectorDescriptor());
    }

    @Test
    public void setAndGetDatastoreIdTest() {
        String[] dataStoreIds = {null, "", "1", "awsd", "123123123", "-123", "ASDa12$%~", "~ˇ#$%&/()=?*\\", "DSUrCucsvb2qUwbdD5yyiCC5v1wwF5FmBIcWa2oXafNw5bqV2RAcyyN0gCHQTdL2JedME5A4PXsXt7iHekhys52GZVmiCNcA065RxFEsDasCcaH1dzeRioRvA14NoJPGmScHdHf8Mfzv03vWrs7n2W59f1In9NdUtnaxY1Wp5TjknB6X8U5ZRczLntQZNX3MSu5f4OzF29oTtuDcuPIUal6OBTKnm8qLVhsiu3oMK3YnFhoHuiATBk3Pl0q9LaL"};

        Assert.assertNull("Null expected.", camelKapuaMessage.getDatastoreId());
        for (String id : dataStoreIds) {
            camelKapuaMessage.setDatastoreId(id);
            Assert.assertEquals("Expected and actual values should be the same.", id, camelKapuaMessage.getDatastoreId());
        }
    }
}