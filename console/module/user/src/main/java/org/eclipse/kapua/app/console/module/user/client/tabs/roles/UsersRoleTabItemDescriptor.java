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

import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractEntityTabDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.role.RoleView;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.AccessInfoSessionPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.permission.RoleSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;

public class UsersRoleTabItemDescriptor extends AbstractEntityTabDescriptor<GwtRole, UsersRoleTabItem, RoleView> {

    @Override
    public String getViewId() {
        return "role.users";
    }

    @Override
    public Integer getOrder() {
        return 300;
    }

    @Override
    public UsersRoleTabItem getTabViewInstance(RoleView view, GwtSession currentSession) {
        return new UsersRoleTabItem(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(UserSessionPermission.read())
                && currentSession.hasPermission(RoleSessionPermission.read())
                && currentSession.hasPermission(AccessInfoSessionPermission.read());
    }
}
