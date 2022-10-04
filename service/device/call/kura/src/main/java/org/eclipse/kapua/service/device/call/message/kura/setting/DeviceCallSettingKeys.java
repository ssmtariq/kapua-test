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
package org.eclipse.kapua.service.device.call.message.kura.setting;

import org.eclipse.kapua.commons.setting.SettingKey;

/**
 * {@link SettingKey}s for {@code kapua-device-call-kura} module.
 *
 * @since 1.0.0
 */
public enum DeviceCallSettingKeys implements SettingKey {

    /**
     * Destination reply part.
     *
     * @since 1.0.0
     */
    DESTINATION_REPLY_PART("destination.reply.part");

    /**
     * The key value of the {@link SettingKey}.
     *
     * @since 1.0.0
     */
    private final String key;

    /**
     * Constructor.
     *
     * @param key The key value of the {@link SettingKey}.
     * @since 1.0.0
     */
    private DeviceCallSettingKeys(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}