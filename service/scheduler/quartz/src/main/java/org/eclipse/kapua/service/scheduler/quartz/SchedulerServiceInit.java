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
package org.eclipse.kapua.service.scheduler.quartz;

import org.eclipse.kapua.KapuaException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerServiceInit {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerServiceInit.class);

    private SchedulerServiceInit() {
    }

    public static void initialize() throws KapuaException {
        logger.info("Starting scheduler service...");
        SchedulerFactory sf = new StdSchedulerFactory();
        try {
            sf.getScheduler().start();
        } catch (SchedulerException e) {
            throw KapuaException.internalError(e, "Cannot start scheduler service");
        }
        logger.info("Starting scheduler service... DONE");
    }

    public static void close() {
        logger.info("Stopping scheduler service...");
        // do nothing
        logger.info("Stopping scheduler service... DONE");
    }
}
