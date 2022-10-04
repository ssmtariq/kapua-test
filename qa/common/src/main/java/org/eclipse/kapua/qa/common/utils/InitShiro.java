/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.common.utils;

import java.io.IOException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.mgt.SecurityManager;
import org.eclipse.kapua.service.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.cucumber.guice.ScenarioScoped;
import io.cucumber.java.en.Given;

@ScenarioScoped
public class InitShiro {

    private static final Logger logger = LoggerFactory.getLogger(InitShiro.class);

    @Given("^Init Security Context$")
    public void start() throws IOException {
        logger.info("Init shiro security manager...");
        try {
            SecurityManager securityManager = SecurityUtils.getSecurityManager();
            logger.info("Found Shiro security manager {}", securityManager);
        }
        catch (UnavailableSecurityManagerException e) {
            logger.info("Init shiro security manager...");
            SecurityUtil.initSecurityManager();
        }
        logger.info("Init shiro security manager... DONE");
    }

    @Given("^Reset Security Context$")
    public void stop() {
        logger.info("Reset shiro security manager...");
        SecurityUtils.setSecurityManager(null);
        logger.info("Reset shiro security manager... DONE");
    }

}
