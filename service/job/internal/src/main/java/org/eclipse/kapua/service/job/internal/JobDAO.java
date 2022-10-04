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
package org.eclipse.kapua.service.job.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobListResult;

/**
 * Job DAO
 *
 * @since 1.0
 */
public class JobDAO {

    private JobDAO() {
    }

    /**
     * Creates and return new Job
     *
     * @param em
     * @param jobCreator
     * @return
     * @throws KapuaException
     */
    public static Job create(EntityManager em, JobCreator jobCreator)
            throws KapuaException {
        //
        // Create Job

        JobImpl jobImpl = new JobImpl(jobCreator.getScopeId());
        jobImpl.setName(jobCreator.getName());
        jobImpl.setDescription(jobCreator.getDescription());

        return ServiceDAO.create(em, jobImpl);
    }

    /**
     * Updates the provided job
     *
     * @param em
     * @param job
     * @return
     * @throws KapuaException
     */
    public static Job update(EntityManager em, Job job)
            throws KapuaException {
        //
        // Update job
        JobImpl jobImpl = (JobImpl) job;

        return ServiceDAO.update(em, JobImpl.class, jobImpl);
    }

    /**
     * Finds the job by job identifier
     *
     * @param em
     * @param scopeId
     * @param jobId
     * @return
     */
    public static Job find(EntityManager em, KapuaId scopeId, KapuaId jobId) {
        return ServiceDAO.find(em, JobImpl.class, scopeId, jobId);
    }

    /**
     * Finds the job by name
     *
     * @param em
     * @param name
     * @return
     */
    public static Job findByName(EntityManager em, String name) {
        return ServiceDAO.findByName(em, JobImpl.class, name);
    }

    /**
     * Returns the job list matching the provided query
     *
     * @param em
     * @param jobQuery
     * @return
     * @throws KapuaException
     */
    public static JobListResult query(EntityManager em, KapuaQuery jobQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Job.class, JobImpl.class, new JobListResultImpl(), jobQuery);
    }

    /**
     * Returns the job count matching the provided query
     *
     * @param em
     * @param jobQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery jobQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Job.class, JobImpl.class, jobQuery);
    }

    /**
     * Deletes the job by job identifier
     *
     * @param em
     * @param scopeId
     * @param jobId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link Job} is not found
     */
    public static Job delete(EntityManager em, KapuaId scopeId, KapuaId jobId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, JobImpl.class, scopeId, jobId);
    }

}
