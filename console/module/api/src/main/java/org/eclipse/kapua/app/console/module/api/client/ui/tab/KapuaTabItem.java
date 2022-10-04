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
package org.eclipse.kapua.app.console.module.api.client.ui.tab;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public abstract class KapuaTabItem<M extends GwtEntityModel> extends TabItem {

    protected GwtSession currentSession;

    protected M selectedEntity;

    private boolean dirty;

    public KapuaTabItem(GwtSession currentSession, String title, KapuaIcon tabIcon) {
        super(title, tabIcon);

        this.currentSession = currentSession;

        setBorders(true);
        setLayout(new FitLayout());

        addListener(Events.Select, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                refresh();
            }
        });
    }

    public void setEntity(M t) {
        selectedEntity = t;
        dirty = true;
    }

    public M getSelectedEntity() {
        return selectedEntity;
    }

    public void refresh() {
        if (dirty) {
            doRefresh();
            dirty = false;
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    protected abstract void doRefresh();
}
