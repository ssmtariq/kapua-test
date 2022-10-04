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
package org.eclipse.kapua.app.console.module.device.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection.GwtConnectionUserCouplingMode;

import java.io.Serializable;

public class GwtDeviceCreator extends KapuaBaseModel implements Serializable {

    private static final long serialVersionUID = -6479267588650553114L;

    // General info
    private String accountId;
    private String groupId;
    private String clientId;
    private String displayName;
    private String deviceStatus;

    // Security options
    private GwtConnectionUserCouplingMode gwtCredentialsTight;
    private String gwtPreferredUserId;

    // Custom Attributes
    private String customAttribute1;
    private String customAttribute2;
    private String customAttribute3;
    private String customAttribute4;
    private String customAttribute5;

    public GwtDeviceCreator() {
    }

    public String getScopeId() {
        return accountId;
    }

    public void setScopeId(String scopeId) {
        this.accountId = scopeId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public GwtConnectionUserCouplingMode getGwtCredentialsTight() {
        return gwtCredentialsTight;
    }

    public void setGwtCredentialsTight(String gwtCredentialsTight) {
        setGwtCredentialsTight(GwtConnectionUserCouplingMode.getEnumFromLabel(gwtCredentialsTight));
    }

    public void setGwtCredentialsTight(GwtConnectionUserCouplingMode gwtCredentialsTight) {
        this.gwtCredentialsTight = gwtCredentialsTight;
    }

    public String getGwtPreferredUserId() {
        return gwtPreferredUserId;
    }

    public void setGwtPreferredUserId(String gwtPreferredUserId) {
        this.gwtPreferredUserId = gwtPreferredUserId;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getCustomAttribute1() {
        return customAttribute1;
    }

    public void setCustomAttribute1(String customAttribute1) {
        this.customAttribute1 = customAttribute1;
    }

    public String getCustomAttribute2() {
        return customAttribute2;
    }

    public void setCustomAttribute2(String customAttribute2) {
        this.customAttribute2 = customAttribute2;
    }

    public String getCustomAttribute3() {
        return customAttribute3;
    }

    public void setCustomAttribute3(String customAttribute3) {
        this.customAttribute3 = customAttribute3;
    }

    public String getCustomAttribute4() {
        return customAttribute4;
    }

    public void setCustomAttribute4(String customAttribute4) {
        this.customAttribute4 = customAttribute4;
    }

    public String getCustomAttribute5() {
        return customAttribute5;
    }

    public void setCustomAttribute5(String customAttribute5) {
        this.customAttribute5 = customAttribute5;
    }
}
