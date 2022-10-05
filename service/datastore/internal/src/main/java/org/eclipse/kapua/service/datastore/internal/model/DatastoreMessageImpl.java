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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.datastore.internal.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.kapua.message.KapuaPayload;
import org.eclipse.kapua.message.device.data.KapuaDataChannel;
import org.eclipse.kapua.message.internal.KapuaMessageImpl;
import org.eclipse.kapua.service.datastore.model.DatastoreMessage;
import org.eclipse.kapua.service.storable.model.id.StorableId;

import java.util.Date;

/**
 * {@link DatastoreMessage} implementation.
 *
 * @since 1.0.0
 */
public class DatastoreMessageImpl extends KapuaMessageImpl<KapuaDataChannel, KapuaPayload> implements DatastoreMessage {

    private static final long serialVersionUID = 1L;

    private StorableId datastoreId;
    private Date timestamp;

    @Override
    public StorableId getDatastoreId() {
        return datastoreId;
    }

    @Override
    public void setDatastoreId(StorableId id) {
        this.datastoreId = id;
    }

    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", getId())
                .append("datastoreId", getDatastoreId())
                .append("timestamp", getTimestamp())
                .append("deviceId", getDeviceId().toStringId())
                .append("clientId", getClientId())
                .append("receivedOn", getReceivedOn())
                .append("sentOn", getSentOn())
                .append("capturedOn", getCapturedOn())
                .append("position", getPosition().toDisplayString())
                .append("channel", getChannel().toPathString())
                .append("payload", getPayload().toDisplayString())
                .toString();
    }
}
