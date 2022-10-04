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
package org.eclipse.kapua.app.console.module.device.shared.model.event;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

import java.io.Serializable;
import java.util.Date;

public class GwtDeviceEvent extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = 8453754536105261520L;

    private static final String EVENT_MESSAGE = "eventMessage";

    public enum GwtActionType implements IsSerializable {
        READ, //
        CREATE, //
        WRITE, //
        DELETE, //
        OPTIONS, //
        EXECUTE
    }

    public enum GwtResponseCode implements IsSerializable {
        ACCEPTED, // 200
        // 204
        BAD_REQUEST, // 400
        NOT_FOUND, // 404
        INTERNAL_ERROR // 500
    }

    public GwtDeviceEvent() {
    }

    @Override
    public <X> X get(String property) {
        if ("sentOnFormatted".equals(property)) {
            if (getSentOn() != null) {
                return (X) (getSentOn());
            } else {
                return (X) "N/A";
            }
        } else if ("receivedOnFormatted".equals(property)) {
            if (getReceivedOn() != null) {
                return (X) (DateUtils.formatDateTime(getReceivedOn()));
            } else {
                return (X) "N/A";
            }
        } else if ("actionTypeEnum".equals(property)) {
            return (X) (GwtActionType.valueOf(getGwtActionType()));
        } else if ("respondeCodeEnum".equals(property)) {
            return (X) (GwtResponseCode.valueOf(getGwtResponseCode()));
        } else {
            return super.get(property);
        }
    }

    public String getAccountName() {
        return (String) get("accountName");
    }

    public void setAccountName(String accountName) {
        set("accountName", accountName);
    }

    public String getDeviceId() {
        return (String) get("deviceId");
    }

    public void setDeviceId(String deviceId) {
        set("deviceId", deviceId);
    }

    public Date getSentOn() {
        return (Date) get("sentOn");
    }

    public String getSentOnFormatted() {
        return (String) get("sentOnFormatted");
    }

    public void setSentOn(Date sentOn) {
        set("sentOn", sentOn);
    }

    public Date getReceivedOn() {
        return (Date) get("receivedOn");
    }

    public String getReceivedOnFormatted() {
        return (String) get("receivedOnFormatted");
    }

    public void setReceivedOn(Date receivedOn) {
        set("receivedOn", receivedOn);
    }

    public String getEventType() {
        return (String) get("eventType");
    }

    public void setEventType(String eventType) {
        set("eventType", eventType);
    }

    public String getGwtActionType() {
        return get("actionType");
    }

    public GwtActionType getGwtActionTypeEnum() {
        return get("actionTypeEnum");
    }

    public void setGwtActionType(String actionType) {
        set("actionType", actionType);
    }

    public String getGwtResponseCode() {
        return get("responseCode");
    }

    public GwtResponseCode getGwtResponseCodeEnum() {
        return get("responseCodeEnum");
    }

    public void setGwtResponseCode(String gwtResponseCode) {
        set("responseCode", gwtResponseCode);
    }

    public String getEventMessage() {
        return (String) get(EVENT_MESSAGE);
    }

    public String getUnescapedEventMessage() {
        return getUnescaped(EVENT_MESSAGE);
    }

    public void setEventMessage(String eventMessage) {
        set(EVENT_MESSAGE, eventMessage);
    }
}
