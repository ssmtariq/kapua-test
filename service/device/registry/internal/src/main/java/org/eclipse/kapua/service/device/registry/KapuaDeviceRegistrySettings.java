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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.commons.setting.AbstractKapuaSetting;

public class KapuaDeviceRegistrySettings extends AbstractKapuaSetting<KapuaDeviceRegistrySettingKeys> {

    private static final String DEVICE_REGISTRY_SETTING_RESOURCE = "kapua-device-registry-setting.properties";

    private static final KapuaDeviceRegistrySettings INSTANCE = new KapuaDeviceRegistrySettings();

    /**
     * Construct a new device registry setting reading settings from {@link KapuaDeviceRegistrySettings#DEVICE_REGISTRY_SETTING_RESOURCE}
     */
    private KapuaDeviceRegistrySettings() {
        super(DEVICE_REGISTRY_SETTING_RESOURCE);
    }

    /**
     * Return the device registry setting instance (singleton)
     *
     * @return
     */
    public static KapuaDeviceRegistrySettings getInstance() {
        return INSTANCE;
    }

}
