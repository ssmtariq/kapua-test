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
package org.eclipse.kapua.qa.common.utils;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.ConnectionFactoryConfiguration;
import org.apache.activemq.artemis.jms.server.config.JMSConfiguration;
import org.apache.activemq.artemis.jms.server.config.impl.ConnectionFactoryConfigurationImpl;
import org.apache.activemq.artemis.jms.server.config.impl.JMSConfigurationImpl;
import org.apache.activemq.artemis.jms.server.embedded.EmbeddedJMS;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.qa.common.DBHelper;
import org.eclipse.kapua.qa.common.Suppressed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Singleton;

import io.cucumber.java.en.Given;

@Singleton
public class EmbeddedEventBroker {

    private static final Logger logger = LoggerFactory.getLogger(EmbeddedEventBroker.class);

    private static final String DEFAULT_DATA_DIRECTORY_PREFIX = "target/artemis" + UUID.randomUUID();
    private static final String DEFAULT_DATA_DIRECTORY = DEFAULT_DATA_DIRECTORY_PREFIX + "/data/journal";

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.broker.extraStartupDelay", 0);

    private static Map<String, List<AutoCloseable>> closables = new HashMap<>();

    private DBHelper database;

    private static EmbeddedJMS jmsServer;

    @Given("^Start Event Broker$")
    public void start() {
        //set a default value if not set
        if (StringUtils.isEmpty(System.getProperty(SystemSettingKey.EVENT_BUS_URL.key()))) {
            System.setProperty(SystemSettingKey.EVENT_BUS_URL.key(), "amqp://127.0.0.1:5672");
        }

        logger.info("Starting new instance of Event Broker");
        try {
            //start Artemis embedded
            Configuration configuration = new ConfigurationImpl();
            configuration.setPersistenceEnabled(false);
            configuration.setJournalDirectory(DEFAULT_DATA_DIRECTORY);
            configuration.setSecurityEnabled(false);
            configuration.addAcceptorConfiguration("amqp",
                    "tcp://127.0.0.1:5672?protocols=AMQP");
            configuration.addConnectorConfiguration("connector", "tcp://127.0.0.1:5672");
            JMSConfiguration jmsConfig = new JMSConfigurationImpl();
            ConnectionFactoryConfiguration cfConfig = new ConnectionFactoryConfigurationImpl().setName("cf").setConnectorNames(Arrays.asList("connector")).setBindings("cf");
            jmsConfig.getConnectionFactoryConfigurations().add(cfConfig);

            jmsServer = new EmbeddedJMS().setConfiguration(configuration).setJmsConfiguration(jmsConfig).start();

            if (EXTRA_STARTUP_DELAY > 0) {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to start Event Broker", e);
        }
    }

    @Given("^Stop Event Broker$")
    public void stop() {
        logger.info("Stopping Event Broker instance ...");
        try (final Suppressed<RuntimeException> s = Suppressed.withRuntimeException()) {
            // close all resources
            closables.values().stream().flatMap(values -> values.stream()).forEach(s::closeSuppressed);
            // shut down broker
            if (jmsServer != null) {
                jmsServer.stop();
                jmsServer = null;
            }
        } catch (Exception e) {
            logger.error("Failed to stop Event Broker", e);
        }
        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Wait interrupted!", e);
                }
                else {
                    logger.warn("Wait interrupted!");
                }
            }
        }
        logger.info("Stopping Event Broker instance ... done!");
    }

}
