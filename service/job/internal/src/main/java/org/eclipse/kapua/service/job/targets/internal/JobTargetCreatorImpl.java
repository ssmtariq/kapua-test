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

import org.eclipse.kapua.commons.model.AbstractKapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;

public class JobTargetCreatorImpl extends AbstractKapuaUpdatableEntityCreator<JobTarget> implements JobTargetCreator {

    private static final long serialVersionUID = 3119071638220738358L;

    private KapuaId jobId;
    private KapuaId jobTargetId;

    public JobTargetCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = jobId;
    }

    @Override
    public KapuaId getJobTargetId() {
        return jobTargetId;
    }

    @Override
    public void setJobTargetId(KapuaId jobTargetId) {
        this.jobTargetId = jobTargetId;
    }

}
