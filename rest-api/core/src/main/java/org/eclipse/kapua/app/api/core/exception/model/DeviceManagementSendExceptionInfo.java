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

import org.eclipse.kapua.service.device.management.exception.DeviceManagementSendException;
import org.eclipse.kapua.service.device.management.message.request.KapuaRequestMessage;

import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "deviceManagementSendExceptionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceManagementSendExceptionInfo extends ExceptionInfo {

    private KapuaRequestMessage<?, ?> kapuaRequestMessage;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    protected DeviceManagementSendExceptionInfo() {
        super();
    }

    /**
     * Constructor.
     *
     * @param deviceManagementSendException The root exception.
     * @since 1.0.0
     */
    public DeviceManagementSendExceptionInfo(DeviceManagementSendException deviceManagementSendException) {
        super(Response.Status.INTERNAL_SERVER_ERROR, deviceManagementSendException);

        this.kapuaRequestMessage = deviceManagementSendException.getRequestMessage();
    }

    /**
     * Gets the {@link DeviceManagementSendExceptionInfo#getRequestMessage()}.
     *
     * @return The {@link DeviceManagementSendExceptionInfo#getRequestMessage()}.
     * @since 1.0.0
     */
    public KapuaRequestMessage<?, ?> getRequestMessage() {
        return kapuaRequestMessage;
    }
}
