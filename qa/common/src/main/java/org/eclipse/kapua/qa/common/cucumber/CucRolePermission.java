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
package org.eclipse.kapua.qa.common.cucumber;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;

import java.math.BigInteger;

public class CucRolePermission {

    private KapuaId scope;
    private Integer scopeId;

    private KapuaId role;
    private Integer roleId;

    private Actions action;
    private String actionName;

    private KapuaId targetScope;
    private Integer targetScopeId;

    public CucRolePermission(KapuaId scope, Integer scopeId, KapuaId role, Integer roleId, Actions action, String actionName, KapuaId targetScope, Integer targetScopeId) {
        this.scope = scope;
        this.scopeId = scopeId;
        this.role = role;
        this.roleId = roleId;
        this.action = action;
        this.actionName = actionName;
        this.targetScope = targetScope;
        this.targetScopeId = targetScopeId;
    }

    public void doParse() {
        if (this.scopeId != null) {
            this.scope = new KapuaEid(BigInteger.valueOf(scopeId));
        }
        if (this.roleId != null) {
            this.role = new KapuaEid(BigInteger.valueOf(roleId));
        }
        if (this.actionName != null) {
            switch (actionName.trim().toLowerCase()) {
            case "read":
                this.action = Actions.read;
                break;
            case "write":
                this.action = Actions.write;
                break;
            case "delete":
                this.action = Actions.delete;
                break;
            case "connect":
                this.action = Actions.connect;
                break;
            case "execute":
                this.action = Actions.execute;
                break;
            }
        }
        if (this.targetScopeId != null) {
            this.targetScope = new KapuaEid(BigInteger.valueOf(targetScopeId));
        }
    }

    public void setScopeId(KapuaId id) {
        this.scope = id;
    }

    public KapuaId getScopeId() {
        return this.scope;
    }

    public void setRoleId(KapuaId id) {
        this.role = id;
    }

    public KapuaId getRoleId() {
        return this.role;
    }

    public Actions getAction() {
        return action;
    }

    public void setAction(Actions action) {
        this.action = action;
    }

    public KapuaId getTargetScopeId() {
        return targetScope;
    }

    public void setTargetScopeId(KapuaId targetScope) {
        this.targetScope = targetScope;
    }
}
