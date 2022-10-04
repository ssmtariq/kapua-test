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
import java.util.HashSet;
import java.util.Set;

public class CucRole {

    private String name;
    private Integer scopeId;
    private String actions;
    private KapuaId id;
    private Set<Actions> actionSet;

    public CucRole(String name, Integer scopeId, String actions, KapuaId id, Set<Actions> actionSet) {
        this.name = name;
        this.scopeId = scopeId;
        this.actions = actions;
        this.id = id;
        this.actionSet = actionSet;
    }

    public void doParse() {
        if (scopeId != null) {
            id = new KapuaEid(BigInteger.valueOf(scopeId));
        }
        if (actions != null) {
            String tmpAct = actions.trim().toLowerCase();
            if (tmpAct.length() != 0) {
                actionSet = new HashSet<>();
                String[] tmpList = actions.split(",");

                for (String tmpS : tmpList) {
                    switch (tmpS.trim().toLowerCase()) {
                    case "read":
                        actionSet.add(Actions.read);
                        break;
                    case "write":
                        actionSet.add(Actions.write);
                        break;
                    case "delete":
                        actionSet.add(Actions.delete);
                        break;
                    case "connect":
                        actionSet.add(Actions.connect);
                        break;
                    case "execute":
                        actionSet.add(Actions.execute);
                        break;
                    }
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScopeId(KapuaId id) {
        this.id = id;
    }

    public KapuaId getScopeId() {
        return id;
    }

    public Set<Actions> getActions() {
        return actionSet;
    }

    public void setActions(Set<Actions> actions) {
        actionSet = actions;
    }
}
