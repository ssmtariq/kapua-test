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

import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.EntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.tag.shared.model.GwtTag;
import org.eclipse.kapua.app.console.module.tag.shared.model.permission.TagSessionPermission;

public class TagViewDescriptor extends AbstractEntityViewDescriptor<GwtTag> {

    @Override
    public String getViewId() {
        return "tags";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.TAGS;
    }

    @Override
    public Integer getOrder() {
        return 600;
    }

    @Override
    public String getName() {
        return TagView.getName();
    }

    @Override
    public EntityView<GwtTag> getViewInstance(GwtSession currentSession) {
        return new TagView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(TagSessionPermission.read());
    }
}
