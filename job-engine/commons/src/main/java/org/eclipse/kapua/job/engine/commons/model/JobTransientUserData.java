/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.job.engine.commons.model;

import org.eclipse.kapua.job.engine.commons.logger.JobLogger;

import javax.batch.runtime.context.JobContext;

/**
 * {@link JobTransientUserData} offers utilities over the {@link JobContext#getTransientUserData()}.
 * <p>
 * Using this class in the {@link JobContext#getTransientUserData()} ease the access to the stored data.
 *
 * @since 1.1.0
 */
public class JobTransientUserData {

    private JobLogger jobLogger;

    /**
     * Gets the {@link JobLogger}.
     *
     * @return The {@link JobLogger}.
     * @since 1.1.0
     */
    public JobLogger getJobLogger() {
        return jobLogger;
    }

    /**
     * Sets the {@link JobLogger}.
     *
     * @param jobLogger The {@link JobLogger}.
     * @since 1.1.0
     */
    public void setJobLogger(JobLogger jobLogger) {
        this.jobLogger = jobLogger;
    }
}
