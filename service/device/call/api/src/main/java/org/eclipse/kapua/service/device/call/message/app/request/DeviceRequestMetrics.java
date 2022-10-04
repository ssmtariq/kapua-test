/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.call.message.app.request;

import org.eclipse.kapua.service.device.call.message.DeviceMetrics;

/**
 * {@link DeviceRequestMetrics} definition.
 *
 * @since 1.0.0
 */
public interface DeviceRequestMetrics extends DeviceMetrics {

    /**
     * Gets the value of this {@link DeviceRequestMetrics}.
     *
     * @return The value of this {@link DeviceRequestMetrics}.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Renamed to {@link #getName()}.
     */
    @Deprecated
    String getValue();
}
