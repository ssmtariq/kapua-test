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
package org.eclipse.kapua.qa.common;

import com.google.common.collect.Lists;
import org.eclipse.kapua.commons.model.AbstractKapuaEntity;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.service.authorization.domain.Domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Test domain.<br>
 *
 * @since 1.0
 */
public class TestDomain extends AbstractKapuaEntity implements Domain, org.eclipse.kapua.model.domain.Domain {

    private static final long serialVersionUID = 3782336558657796495L;

    private String name = "test";
    private Set<Actions> actions = new HashSet<>(Lists.newArrayList(Actions.read, Actions.delete, Actions.write));
    private boolean groupable;

    public TestDomain() {}

    public TestDomain(String name) {
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setActions(Set<Actions> actions) {
        this.actions = actions;
    }

    @Override
    public Set<Actions> getActions() {
        return actions;
    }

    @Override
    public boolean getGroupable() {
        return groupable;
    }

    @Override
    public void setGroupable(boolean groupable) {
        this.groupable = groupable;
    }

}
