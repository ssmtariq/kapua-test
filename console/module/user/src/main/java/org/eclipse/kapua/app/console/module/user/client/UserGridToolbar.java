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

import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserAddDialog;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserDeleteDialog;
import org.eclipse.kapua.app.console.module.user.client.dialog.UserEditDialog;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;

public class UserGridToolbar extends EntityCRUDToolbar<GwtUser> {

    public UserGridToolbar(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new UserAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserEditDialog dialog = null;
        if (selectedUser != null) {
            dialog = new UserEditDialog(currentSession, selectedUser);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtUser selectedUser = gridSelectionModel.getSelectedItem();
        UserDeleteDialog dialog = null;
        if (selectedUser != null) {
            dialog = new UserDeleteDialog(selectedUser);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getEditEntityButton().disable();
        getDeleteEntityButton().disable();
        getAddEntityButton().setEnabled(currentSession.hasPermission(UserSessionPermission.write()));
    }

}
