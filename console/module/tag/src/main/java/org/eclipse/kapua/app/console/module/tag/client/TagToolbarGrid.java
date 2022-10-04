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
package org.eclipse.kapua.app.console.module.tag.client;

import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.permission.TagSessionPermission;

import com.google.gwt.user.client.Element;

public class TagToolbarGrid extends EntityCRUDToolbar<GwtTag> {

    public TagToolbarGrid(GwtSession currentSession) {
        super(currentSession);
    }

    public TagToolbarGrid(GwtSession currentSession, boolean slaveEntity) {
        super(currentSession, slaveEntity);
    }

    @Override
    protected KapuaDialog getAddDialog() {
        return new TagAddDialog(currentSession);
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
        getAddEntityButton().setEnabled(currentSession.hasPermission(TagSessionPermission.write()));
    }

    @Override
    protected KapuaDialog getEditDialog() {
        GwtTag selectedTag = gridSelectionModel.getSelectedItem();
        TagEditDialog dialog = null;
        if (selectedTag != null) {
            dialog = new TagEditDialog(currentSession, selectedTag);
        }
        return dialog;
    }

    @Override
    protected KapuaDialog getDeleteDialog() {
        GwtTag selectedTag = gridSelectionModel.getSelectedItem();
        TagDeleteDialog dialog = null;
        if (selectedTag != null) {
            dialog = new TagDeleteDialog(selectedTag);
        }
        return dialog;
    }

}
