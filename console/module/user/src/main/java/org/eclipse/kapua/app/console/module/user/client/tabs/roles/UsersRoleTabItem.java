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
package org.eclipse.kapua.app.console.module.user.client.tabs.roles;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;

public class UsersRoleTabItem extends KapuaTabItem<GwtRole> {

    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    RoleSubjectGrid rolePermissionGrid;

    public UsersRoleTabItem(GwtSession currentSession) {
        super(currentSession, MSGS.roleTabSubjectGridTitle(), new KapuaIcon(IconSet.SUPPORT));
        rolePermissionGrid = new RoleSubjectGrid(null, currentSession);
        rolePermissionGrid.setRefreshOnRender(false);
        setEnabled(false);
    }

    @Override
    protected void doRefresh() {
        if (selectedEntity != null) {
            rolePermissionGrid.refresh();
        }

    }

    @Override
    public void setEntity(GwtRole t) {
        super.setEntity(t);
        if (t != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
        rolePermissionGrid.setEntity(t);

    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
        add(rolePermissionGrid);
    }

}
