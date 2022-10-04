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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

/**
 * {@link DeviceEvent} types.
 *
 * @since 1.0.0
 *
 */
public enum DeviceEventType {
    /**
     * Connected (network layer)
     */
    CONNECTED,
    /**
     * Birth
     */
    BIRTH,
    /**
     * Disconnected (network layer)
     */
    DC,
    /**
     * Disconnected
     */
    DISCONNECTED,
    /**
     * Missing (gthe device doesn't reply to the ping request from the broker)
     */
    MISSING,
    /**
     * Configuration component updated
     */
    CONF_COMP_UPDATED,
    /**
     * Configuration updated
     */
    CONF_UPDATED,
    /**
     * Configuration rollbacked
     */
    CONF_ROLLEDBACK,
    /**
     * Deploy package downloaded
     */
    DEPLOY_DOWNLOADED,
    /**
     * Deploy package installed
     */
    DEPLOY_INSTALLED,
    /**
     * Deploy package uninstalled
     */
    DEPLOY_UNINSTALLED,
    /**
     * Command executed
     */
    CMD_EXECUTED,
    /**
     * Applications updated
     */
    APPS_UPDATED,
    /**
     * Bundle started
     */
    BUNDLE_STARTED,
    /**
     * Bundle stopped
     */
    BUNDLE_STOPPED,
    /**
     * Device provisioned
     */
    PROVISIONED,
    /**
     * Certificate updated
     */
    CERTIFICATE_UPDATED,
    /**
     * Certificate revoked
     */
    CERTIFICATE_REVOKED,
    /**
     * Certificate update error
     */
    CERTIFICATE_UPDATE_ERROR
}
