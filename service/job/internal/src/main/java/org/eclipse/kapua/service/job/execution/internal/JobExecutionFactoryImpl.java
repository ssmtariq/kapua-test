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
package org.eclipse.kapua.service.job.execution.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.execution.JobExecution;
import org.eclipse.kapua.service.job.execution.JobExecutionCreator;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionListResult;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;

import javax.inject.Singleton;

/**
 * {@link JobExecutionFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class JobExecutionFactoryImpl implements JobExecutionFactory {

    @Override
    public JobExecution newEntity(KapuaId scopeId) {
        return new JobExecutionImpl(scopeId);
    }

    @Override
    public JobExecutionCreator newCreator(KapuaId scopeId) {
        return new JobExecutionCreatorImpl(scopeId);
    }

    @Override
    public JobExecutionQuery newQuery(KapuaId scopeId) {
        return new JobExecutionQueryImpl(scopeId);
    }

    @Override
    public JobExecutionListResult newListResult() {
        return new JobExecutionListResultImpl();
    }

    @Override
    public JobExecution clone(JobExecution jobExecution) {
        try {
            return new JobExecutionImpl(jobExecution);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, JobExecution.TYPE, jobExecution);
        }
    }
}
