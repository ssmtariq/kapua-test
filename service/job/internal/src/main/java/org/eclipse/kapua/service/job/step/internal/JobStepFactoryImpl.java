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
package org.eclipse.kapua.service.job.step.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepFactory;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepPropertyImpl;

import javax.inject.Singleton;

/**
 * {@link JobStepFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class JobStepFactoryImpl implements JobStepFactory {

    @Override
    public JobStep newEntity(KapuaId scopeId) {
        return new JobStepImpl(scopeId);
    }

    @Override
    public JobStepCreator newCreator(KapuaId scopeId) {
        return new JobStepCreatorImpl(scopeId);
    }

    @Override
    public JobStepQuery newQuery(KapuaId scopeId) {
        return new JobStepQueryImpl(scopeId);
    }

    @Override
    public JobStepListResult newListResult() {
        return new JobStepListResultImpl();
    }

    @Override
    public JobStepProperty newStepProperty(String name, String propertyType, String propertyValue) {
        return new JobStepPropertyImpl(name, propertyType, propertyValue, null);
    }

    @Override
    public JobStep clone(JobStep jobStep) {
        try {
            return new JobStepImpl(jobStep);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, JobStep.TYPE, jobStep);
        }
    }
}
