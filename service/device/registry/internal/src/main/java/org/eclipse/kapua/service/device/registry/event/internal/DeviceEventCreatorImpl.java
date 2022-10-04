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
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;

import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

/**
 * Device event creator service implementation.
 *
 * @since 1.0
 */
public class DeviceEventCreatorImpl extends AbstractKapuaEntityCreator<DeviceEvent> implements DeviceEventCreator {

    private static final long serialVersionUID = -3982569213440658172L;

    @XmlElement(name = "deviceId")
    private KapuaId deviceId;

    @XmlElement(name = "receivedOn")
    private Date receivedOn;

    @XmlElement(name = "sentOn")
    private Date sentOn;

    @XmlElement(name = "position")
    private KapuaPosition position;

    @XmlElement(name = "resource")
    private String resource;

    @XmlElement(name = "action")
    private KapuaMethod action;

    @XmlElement(name = "responseCode")
    private KapuaResponseCode responseCode;

    @XmlElement(name = "eventMessage")
    private String eventMessage;

    /**
     * Constructor
     *
     * @param scopeId
     */
    protected DeviceEventCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(KapuaId deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public Date getSentOn() {
        return sentOn != null ? new Date(sentOn.getTime()) : null;
    }

    @Override
    public void setSentOn(Date sentOn) {
        this.sentOn = sentOn;
    }

    @Override
    public Date getReceivedOn() {
        return receivedOn != null ? new Date(receivedOn.getTime()) : null;
    }

    @Override
    public void setReceivedOn(Date receivedOn) {
        this.receivedOn = receivedOn;
    }

    @Override
    public KapuaPosition getPosition() {
        return position;
    }

    @Override
    public void setPosition(KapuaPosition position) {
        this.position = position;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public KapuaMethod getAction() {
        return action.normalizeAction();
    }

    @Override
    public void setAction(KapuaMethod action) {
        this.action = action.normalizeAction();
    }

    @Override
    public KapuaResponseCode getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(KapuaResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public String getEventMessage() {
        return eventMessage;
    }

    @Override
    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

}
