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
package org.eclipse.kapua.service.job.targets.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;

import javax.inject.Singleton;

/**
 * {@link JobTargetFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class JobTargetFactoryImpl implements JobTargetFactory {

    @Override
    public JobTarget newEntity(KapuaId scopeId) {
        return new JobTargetImpl(scopeId);
    }

    @Override
    public JobTargetCreator newCreator(KapuaId scopeId) {
        return new JobTargetCreatorImpl(scopeId);
    }

    @Override
    public JobTargetQuery newQuery(KapuaId scopeId) {
        return new JobTargetQueryImpl(scopeId);
    }

    @Override
    public JobTargetListResult newListResult() {
        return new JobTargetListResultImpl();
    }

    @Override
    public JobTarget clone(JobTarget jobTarget) {
        try {
            return new JobTargetImpl(jobTarget);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, JobTarget.TYPE, jobTarget);
        }
    }
}
