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
package org.eclipse.kapua.app.console.module.device.shared.model.connection;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.io.Serializable;

public class GwtDeviceConnection extends GwtUpdatableEntityModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5059430797167078209L;

    @Override
    public <X> X get(String property) {
        if ("connectionStatusEnum".equals(property)) {
            return (X) GwtDeviceConnectionStatus.valueOf(getConnectionStatus());
        } else if ("connectionUserCouplingModeEnum".equals(property)) {
            return (X) GwtConnectionUserCouplingMode.getEnumFromLabel(getConnectionUserCouplingMode());
        } else {
            return super.get(property);
        }
    }

    public enum GwtConnectionUserCouplingMode implements IsSerializable {
        LOOSE("Unbound"), //
        STRICT("Device-bound"), //
        INHERITED("Account Default");

        private final String label;

        GwtConnectionUserCouplingMode(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

        public static GwtConnectionUserCouplingMode getEnumFromLabel(String label) {
            GwtConnectionUserCouplingMode gdct = null;

            for (GwtConnectionUserCouplingMode e : GwtConnectionUserCouplingMode.values()) {
                if (e.getLabel().equals(label)) {
                    gdct = e;
                }
            }

            return gdct;
        }
    }

    public String getConnectionStatus() {
        return get("connectionStatus");
    }

    public GwtDeviceConnectionStatus getConnectionStatusEnum() {
        return get("connectionStatusEnum");
    }

    public void setConnectionStatus(String connectionStatus) {
        set("connectionStatus", connectionStatus);
    }

    public String getClientId() {
        return get("clientId");
    }

    public void setClientId(String clientId) {
        set("clientId", clientId);
    }

    public String getUserId() {
        return get("userId");
    }

    public void setUserId(String userId) {
        set("userId", userId);
    }

    public String getProtocol() {
        return get("protocol");
    }

    public void setProtocol(String protocol) {
        set("protocol", protocol);
    }

    public String getClientIp() {
        return get("clientIp");
    }

    public void setClientIp(String clientIp) {
        set("clientIp", clientIp);
    }

    public String getServerIp() {
        return get("serverIp");
    }

    public void setServerIp(String serverIp) {
        set("serverIp", serverIp);
    }

    public String getUserName() {
        return get("userName");
    }

    public void setUserName(String userName) {
        set("userName", userName);
    }

    public String getConnectionUserCouplingMode() {
        return get("connectionUserCouplingMode");
    }

    public GwtConnectionUserCouplingMode getConnectionUserCouplingModeEnum() {
        return get("connectionUserCouplingModeEnum");
    }

    public void setConnectionUserCouplingMode(String connectionUserCouplingMode) {
        set("connectionUserCouplingMode", connectionUserCouplingMode);
    }

    public String getReservedUserId() {
        return get("reservedUserId");
    }

    public void setReservedUserId(String userId) {
        set("reservedUserId", userId);
    }

    public String getReservedUserName() {
        return get("reservedUserName");
    }

    public void setReservedUserName(String userName) {
        set("reservedUserName", userName);
    }

    public Boolean getAllowUserChange() {
        return get("allowUserChange");
    }

    public void setAllowUserChange(Boolean allowUserChange) {
        set("allowUserChange", allowUserChange);
    }

}
