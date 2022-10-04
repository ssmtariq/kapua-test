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
package org.eclipse.kapua.app.console.module.device.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.model.query.KapuaListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

public abstract class DeviceEventExporter {

    protected static final String BLANK = "";

    protected static final String[] DEVICE_PROPERTIES = {
            "Account ID",
            "Account Name",
            "Event ID",
            "Created On (UTC)",
            "Created By",
            "Client ID",
            "Device ID",
            "Received On (UTC)",
            "Sent On (UTC)",
            "Position Longitude",
            "Position Latitude",
            "Position Altitude",
            "Position Precision",
            "Position Heading",
            "Position Speed",
            "Position Timestamp",
            "Position Satellites",
            "Position Status",
            "Resource",
            "Action",
            "Response Code",
            "Event Message"
    };

    protected HttpServletResponse response;

    protected DeviceEventExporter(HttpServletResponse response) {
        this.response = response;
    }

    public abstract void init(String account, String deviceName)
            throws ServletException, IOException, KapuaException;

    public abstract void append(KapuaListResult<DeviceEvent> messages)
            throws ServletException, IOException, KapuaException;

    public abstract void close()
            throws ServletException, IOException;
}
