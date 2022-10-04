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
 *     Eurotech
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.qa.common.utils;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

/**
 * Singleton for managing datastore creation and deletion inside Gherkin scenarios.
 */
@ScenarioScoped
public class EmbeddedDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(EmbeddedDatastore.class);

    private static final int EXTRA_STARTUP_DELAY = Integer.getInteger("org.eclipse.kapua.qa.datastore.extraStartupDelay", 0);


    @Given("^Start Datastore$")
    public void setup() {

        LOG.info("Starting embedded datastore...");

        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Wait interrupted!", e);
                }
                else {
                    LOG.warn("Wait interrupted!");
                }
            }
        }

        LOG.info("Starting embedded datastore... DONE!");
    }

    @Given("^Stop Datastore$")
    public void closeNode() throws IOException {

        LOG.info("Stopping embedded datastore...");
        if (EXTRA_STARTUP_DELAY > 0) {
            try {
                Thread.sleep(Duration.ofSeconds(EXTRA_STARTUP_DELAY).toMillis());
            } catch (InterruptedException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Wait interrupted!", e);
                }
                else {
                    LOG.warn("Wait interrupted!");
                }
            }
        }

        LOG.info("Stopping embedded datastore... DONE!");
    }
}
