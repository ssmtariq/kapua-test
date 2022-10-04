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
package org.eclipse.kapua.app.console.module.user.client;

import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;

public class UserView extends AbstractEntityView<GwtUser> {

    private UserGrid userGrid;

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);

    public UserView(GwtSession gwtSession) {
        super(gwtSession);
    }

    public static String getName() {
        return MSGS.mainMenuUsers();
    }

    @Override
    public EntityGrid<GwtUser> getEntityGrid(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        if (userGrid == null) {
            userGrid = new UserGrid(entityView, currentSession);
        }
        return userGrid;
    }

    @Override
    public EntityFilterPanel<GwtUser> getEntityFilterPanel(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        return new UserFilterPanel(this, currentSession);
    }

}
