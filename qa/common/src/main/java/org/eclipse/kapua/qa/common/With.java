/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import org.eclipse.kapua.commons.util.ThrowingRunnable;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.authentication.AuthenticationService;
import org.eclipse.kapua.service.authentication.LoginCredentials;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.junit.Assert;

public final class With {

    private With() {
    }

    public static void withLogin(final LoginCredentials credentials, final ThrowingRunnable runnable) throws Exception {
        final AuthenticationService service = KapuaLocator.getInstance().getService(AuthenticationService.class);

        try {
            service.login(credentials);
            runnable.run();
        } finally {
            service.logout();
        }
    }

    public static void withUserAccount(final String accountName, final ThrowingConsumer<User> consumer) throws Exception {
        final UserService userService = KapuaLocator.getInstance().getService(UserService.class);
        final User account = userService.findByName(accountName);

        Assert.assertNotNull(String.format("Account %s should be found", accountName), account);

        consumer.accept(account);
    }

    public static void withDevice(final User account, final String clientId, final ThrowingConsumer<Device> consumer) throws Exception {
        DeviceRegistryService service = KapuaLocator.getInstance().getService(DeviceRegistryService.class);
        Device device = service.findByClientId(account.getId(), clientId);

        Assert.assertNotNull("Device should not be null", device);

        consumer.accept(device);
    }
}
