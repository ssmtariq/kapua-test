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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.role.dialog.RoleAddDialog;
import org.eclipse.kapua.app.console.module.authorization.client.role.dialog.RoleDeleteDialog;
import org.eclipse.kapua.app.console.module.authorization.client.role.dialog.RoleEditDialog;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;

import com.google.gwt.user.client.Element;

public class RoleToolbarGrid extends EntityCRUDToolbar<GwtRole> {

    public RoleToolbarGrid(GwtSession currentSession) {
        super(currentSession);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new RoleAddDialog(currentSession);
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtRole selectedRole = gridSelectionModel.getSelectedItem();
        RoleEditDialog dialog = null;
        if (selectedRole != null) {
            dialog = new RoleEditDialog(currentSession, selectedRole);
        }
        return dialog;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getAddEntityButton().setEnabled(currentSession.hasPermission(RoleSessionPermission.write()));
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtRole selectedRole = gridSelectionModel.getSelectedItem();
        RoleDeleteDialog dialog = null;
        if (selectedRole != null) {
            dialog = new RoleDeleteDialog(selectedRole);
        }
        return dialog;
    }
}
