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
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeviceSnapshotsServlet extends HttpServlet {

    private static final long serialVersionUID = -2533869595709953567L;

    private static final Logger logger = LoggerFactory.getLogger(DeviceSnapshotsServlet.class);

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        PrintWriter writer = response.getWriter();
        try {

            // parameter extraction
            String account = request.getParameter("scopeId");
            String clientId = request.getParameter("deviceId");
            String snapshotId = request.getParameter("snapshotId");

            //
            // get the devices and append them to the exporter
            KapuaLocator locator = KapuaLocator.getInstance();
            DeviceConfigurationManagementService deviceConfigurationManagementService = locator.getService(DeviceConfigurationManagementService.class);
            DeviceConfiguration conf = deviceConfigurationManagementService.get(KapuaEid.parseCompactId(account),
                    KapuaEid.parseCompactId(clientId),
                    snapshotId,
                    null,
                    null);

            String contentDispositionFormat = "attachment; filename*=UTF-8''snapshot_%s_%s_%s.xml; ";

            response.setContentType("application/xml; charset=UTF-8");
            response.setHeader("Cache-Control", "no-transform, max-age=0");
            response.setHeader("Content-Disposition", String.format(contentDispositionFormat,
                    URLEncoder.encode(account, CharEncoding.UTF_8),
                    URLEncoder.encode(clientId, CharEncoding.UTF_8),
                    snapshotId));

            XmlUtil.marshal(conf, writer);
        } catch (Exception e) {
            logger.error("Error creating Excel export", e);
            throw new ServletException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}
