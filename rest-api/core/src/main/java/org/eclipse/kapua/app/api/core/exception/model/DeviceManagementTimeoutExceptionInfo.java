/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.exception.model;

import org.eclipse.kapua.service.device.management.exception.DeviceManagementTimeoutException;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "deviceManagementTimeoutExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceManagementTimeoutExceptionInfo extends ExceptionInfo {

    private Long timeout;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected DeviceManagementTimeoutExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param deviceManagementTimeoutException The root exception.
     * @since 1.0.0
     */
    public DeviceManagementTimeoutExceptionInfo(DeviceManagementTimeoutException deviceManagementTimeoutException) {
        super(Response.Status.INTERNAL_SERVER_ERROR, deviceManagementTimeoutException);

        this.timeout = deviceManagementTimeoutException.getTimeout();
    }

    /**
     * Gets the {@link DeviceManagementTimeoutException#getTimeout()}.
     *
     * @return The {@link DeviceManagementTimeoutException#getTimeout()}.
     * @since 1.0.0
     */
    public Long getTimeout() {
        return timeout;
    }
}
