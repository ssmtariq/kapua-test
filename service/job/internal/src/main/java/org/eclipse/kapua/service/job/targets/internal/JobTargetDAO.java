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

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetStatus;

/**
 * JobTarget DAO
 *
 * @since 1.0
 */
public class JobTargetDAO {

    private JobTargetDAO() {
    }

    /**
     * Creates and return new JobTarget
     *
     * @param em
     * @param jobTargetCreator
     * @return
     * @throws KapuaException
     */
    public static JobTarget create(EntityManager em, JobTargetCreator jobTargetCreator)
            throws KapuaException {
        //
        // Create JobTarget

        JobTargetImpl jobTargetImpl = new JobTargetImpl(jobTargetCreator.getScopeId());
        jobTargetImpl.setJobId(jobTargetCreator.getJobId());
        jobTargetImpl.setJobTargetId(jobTargetCreator.getJobTargetId());
        jobTargetImpl.setStepIndex(0);
        jobTargetImpl.setStatus(JobTargetStatus.PROCESS_AWAITING);

        return ServiceDAO.create(em, jobTargetImpl);
    }

    /**
     * Updates the provided jobTarget
     *
     * @param em
     * @param jobTarget
     * @return
     * @throws KapuaException
     */
    public static JobTarget update(EntityManager em, JobTarget jobTarget)
            throws KapuaException {
        //
        // Update jobTarget
        JobTargetImpl jobTargetImpl = (JobTargetImpl) jobTarget;

        return ServiceDAO.update(em, JobTargetImpl.class, jobTargetImpl);
    }

    /**
     * Finds the jobTarget by jobTarget identifier
     *
     * @param em
     * @param scopeId
     * @param jobTargetId
     * @return
     */
    public static JobTarget find(EntityManager em, KapuaId scopeId, KapuaId jobTargetId) {
        return ServiceDAO.find(em, JobTargetImpl.class, scopeId, jobTargetId);
    }

    /**
     * Returns the jobTarget list matching the provided query
     *
     * @param em
     * @param jobTargetQuery
     * @return
     * @throws KapuaException
     */
    public static JobTargetListResult query(EntityManager em, KapuaQuery jobTargetQuery)
            throws KapuaException {
        return ServiceDAO.query(em, JobTarget.class, JobTargetImpl.class, new JobTargetListResultImpl(), jobTargetQuery);
    }

    /**
     * Returns the jobTarget count matching the provided query
     *
     * @param em
     * @param jobTargetQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery jobTargetQuery)
            throws KapuaException {
        return ServiceDAO.count(em, JobTarget.class, JobTargetImpl.class, jobTargetQuery);
    }

    /**
     * Deletes the jobTarget by jobTarget identifier
     *
     * @param em
     * @param scopeId
     * @param jobTargetId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link JobTarget} is not found
     */
    public static JobTarget delete(EntityManager em, KapuaId scopeId, KapuaId jobTargetId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, JobTargetImpl.class, scopeId, jobTargetId);
    }

}
