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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cucumber;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.user.UserService;

/**
 * Data object used in Gherkin to input Device Connection parameters.
 * The data setters intentionally use only cucumber-friendly data types and
 * generate the resulting Kapua types internally.
 */
public class CucConnection {

    String scope;
    KapuaId scopeId;
    String status;
    String clientId;
    String user;
    KapuaId userId;
    String allowUserChange;
    String userCouplingMode;
    String reservedUser;
    KapuaId reservedUserId;
    String protocol;
    String clientIp;
    String serverIp;

    private static UserService userService = KapuaLocator.getInstance().getService(UserService.class);
    private static AccountService accountService = KapuaLocator.getInstance().getService(AccountService.class);

    public CucConnection(String scope, KapuaId scopeId, String status, String clientId, String user, KapuaId userId, String allowUserChange, String userCouplingMode,
            String reservedUser, KapuaId reservedUserId, String protocol, String clientIp, String serverIp) {
        this.scope = scope;
        this.scopeId = scopeId;
        this.status = status;
        this.clientId = clientId;
        this.user = user;
        this.userId = userId;
        this.allowUserChange = allowUserChange;
        this.userCouplingMode = userCouplingMode;
        this.reservedUser = reservedUser;
        this.reservedUserId = reservedUserId;
        this.protocol = protocol;
        this.clientIp = clientIp;
        this.serverIp = serverIp;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public KapuaId getScopeId() throws KapuaException {
        return KapuaSecurityUtils.doPrivileged(() -> accountService.findByName(scope).getId());
    }

    public DeviceConnectionStatus getStatus() {
        DeviceConnectionStatus tmpStatus = null;
        if (status == null) {
            return null;
        }
        switch (status.trim().toUpperCase()) {
        case "CONNECTED":
            tmpStatus = DeviceConnectionStatus.CONNECTED;
            break;
        case "DISCONNECTED":
            tmpStatus = DeviceConnectionStatus.DISCONNECTED;
            break;
        case "MISSING":
            tmpStatus = DeviceConnectionStatus.MISSING;
            break;
        }
        return tmpStatus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public KapuaId getUserId() throws KapuaException {
        return KapuaSecurityUtils.doPrivileged(() -> userService.findByName(user).getId());
    }

    public boolean getAllowUserChange() {
        return Boolean.valueOf(allowUserChange);
    }

    public void setAllowUserChange(String allowUserChange) {
        this.allowUserChange = allowUserChange;
    }

    public ConnectionUserCouplingMode getUserCouplingMode() {
        ConnectionUserCouplingMode mode;

        if (userCouplingMode == null) {
            return ConnectionUserCouplingMode.INHERITED;
        }
        switch (userCouplingMode.toUpperCase().trim()) {
        case "INHERITED":
            mode = ConnectionUserCouplingMode.INHERITED;
            break;
        case "LOOSE":
            mode = ConnectionUserCouplingMode.LOOSE;
            break;
        case "STRICT":
            mode = ConnectionUserCouplingMode.STRICT;
            break;
        default:
            mode = ConnectionUserCouplingMode.INHERITED;
        }
        return mode;
    }

    public void setUserCouplingMode(String userCouplingMode) {
        this.userCouplingMode = userCouplingMode;
    }

    public String getReservedUser() {
        return reservedUser;
    }

    public void setReservedUser(String reservedUser) {
        this.reservedUser = reservedUser;
    }

    public KapuaId getReservedUserId() throws KapuaException {
        if ((reservedUser == null) || reservedUser.isEmpty()) {
            return null;
        } else {
            return KapuaSecurityUtils.doPrivileged(() -> userService.findByName(reservedUser).getId());
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
