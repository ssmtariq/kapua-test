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
package org.eclipse.kapua.service.user.internal;

import org.eclipse.kapua.commons.event.ServiceEventClientConfiguration;
import org.eclipse.kapua.commons.event.ServiceEventModule;
import org.eclipse.kapua.commons.event.ServiceEventModuleConfiguration;
import org.eclipse.kapua.commons.event.ServiceInspector;
import org.eclipse.kapua.service.user.UserService;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSetting;
import org.eclipse.kapua.service.user.internal.setting.KapuaUserSettingKeys;

import javax.inject.Inject;
import java.util.List;

public class UserServiceModule extends ServiceEventModule {

    @Inject
    private UserService userService;

    @Override
    protected ServiceEventModuleConfiguration initializeConfiguration() {
        KapuaUserSetting kas = KapuaUserSetting.getInstance();
        List<ServiceEventClientConfiguration> selc = ServiceInspector.getEventBusClients(userService, UserService.class);
        return new ServiceEventModuleConfiguration(
                kas.getString(KapuaUserSettingKeys.USER_EVENT_ADDRESS),
                UserEntityManagerFactory.getInstance(),
                selc.toArray(new ServiceEventClientConfiguration[0]));
    }
}
