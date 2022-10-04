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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common.utils;

import org.eclipse.jetty.jmx.MBeanContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AllowSymLinkAliasChecker;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.java.en.Given;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;

public class EmbeddedJetty {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedJetty.class);

    private static Server jetty;

    @Given("^Start Jetty Server on host \"(.*)\" at port \"(.+)\"$")
    public void start(String host, int port) throws Exception {
        // Switch back to default DB connection resolver
        // as Jetty has its own class loader and has to access in memory DB over TCP
        System.setProperty(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), "DEFAULT");

        InetSocketAddress address = new InetSocketAddress(host, port);
        jetty = new Server(address);
        logger.info("Starting Jetty " + jetty);

        // Setup JMX
        MBeanContainer mbContainer = new MBeanContainer(
                ManagementFactory.getPlatformMBeanServer());
        jetty.addBean(mbContainer);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        //File warFile = new File("../rest-api/web/target/api.war");
        String warFileName = System.getProperty("jetty.war.file");
        File warFile = new File(warFileName);
        webapp.setWar(warFile.getAbsolutePath());
        webapp.addAliasCheck(new AllowSymLinkAliasChecker());

        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(jetty);
        classlist.addBefore(
                "org.eclipse.jetty.webapp.JettyWebXmlConfiguration",
                "org.eclipse.jetty.annotations.AnnotationConfiguration" );

        webapp.setAttribute(
                "org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/[^/]*taglibs.*\\.jar$" );

        jetty.setHandler(webapp);

        jetty.start();
        jetty.dumpStdErr();
        // Blocks
        //jetty.join();
    }

    @Given("^Stop Jetty Server$")
    public void stop() throws Exception {
        logger.info("Stopping Jetty " + jetty);

        jetty.stop();
        System.setProperty(SystemSettingKey.DB_JDBC_CONNECTION_URL_RESOLVER.key(), "H2");
    }

}
