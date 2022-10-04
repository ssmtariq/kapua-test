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
package org.eclipse.kapua.app.console.module.user.client.tabs.credentials;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials.CredentialGrid;
import org.eclipse.kapua.app.console.module.authentication.shared.model.permission.CredentialSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserTabItemCredentials extends KapuaTabItem<GwtUser> {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    private CredentialGrid credentialsGrid;

    public UserTabItemCredentials(GwtSession currentSession) {
        super(currentSession, MSGS.gridTabCredentialsLabel(), new KapuaIcon(IconSet.KEY));

        credentialsGrid = new CredentialGrid(null, currentSession);
        credentialsGrid.setRefreshOnRender(false);
        setEnabled(false);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setBorders(false);
        add(credentialsGrid);
    }

    public CredentialGrid getCredentialsGrid() {
        return credentialsGrid;
    }

    @Override
    public void setEntity(GwtUser gwtUser) {
        super.setEntity(gwtUser);
        if (gwtUser != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
        credentialsGrid.setSelectedUserId(gwtUser != null ? gwtUser.getId() : null);
        credentialsGrid.setSelectedUserName(gwtUser != null ? gwtUser.getUsername() : null);
    }

    @Override
    protected void doRefresh() {
        credentialsGrid.refresh();
        credentialsGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null && currentSession.hasPermission(CredentialSessionPermission.write()));
        credentialsGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
