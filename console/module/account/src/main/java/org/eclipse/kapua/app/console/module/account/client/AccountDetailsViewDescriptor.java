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
package org.eclipse.kapua.app.console.module.account.client;

import org.eclipse.kapua.app.console.module.account.shared.model.permission.AccountSessionPermission;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.ui.view.View;
import org.eclipse.kapua.app.console.module.api.client.ui.view.descriptor.AbstractMainViewDescriptor;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public class AccountDetailsViewDescriptor extends AbstractMainViewDescriptor {

    @Override
    public String getViewId() {
        return "mysettings";
    }

    @Override
    public IconSet getIcon() {
        return IconSet.COG;
    }

    @Override
    public Integer getOrder() {
        return 1100;
    }

    @Override
    public String getName() {
        return AccountDetailsView.getName();
    }

    @Override
    public View getViewInstance(GwtSession currentSession) {
        return new AccountDetailsView(currentSession);
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return currentSession.hasPermission(AccountSessionPermission.read());
    }
}
