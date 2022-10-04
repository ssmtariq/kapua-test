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
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.service.internal.KapuaNamedEntityServiceUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.job.engine.JobEngineService;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobCreator;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.JobFactory;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobQuery;
import org.eclipse.kapua.service.job.JobService;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerAttributes;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.TriggerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link JobService} implementation
 *
 * @since 1.0.0
 */
@Singleton
public class JobServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Job, JobCreator, JobService, JobListResult, JobQuery, JobFactory> implements JobService {

    private static final Logger LOG = LoggerFactory.getLogger(JobServiceImpl.class);

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private final JobEngineService jobEngineService = LOCATOR.getService(JobEngineService.class);

    @Inject
    private AuthorizationService authorizationService;
    @Inject
    private PermissionFactory permissionFactory;

    @Inject
    private TriggerService triggerService;
    @Inject
    private TriggerFactory triggerFactory;

    public JobServiceImpl() {
        super(JobService.class.getName(),
                JobDomains.JOB_DOMAIN,
                JobEntityManagerFactory.getInstance(),
                JobService.class,
                JobFactory.class);
    }

    @Override
    public Job create(JobCreator creator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(creator, "jobCreator");
        ArgumentValidator.notNull(creator.getScopeId(), "jobCreator.scopeId");
        ArgumentValidator.validateJobName(creator.getName(), "jobCreator.name");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, creator.getScopeId()));

        //
        // Check entity limit
        checkAllowedEntities(creator.getScopeId(), "Jobs");

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, creator);

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> JobDAO.create(em, creator));
    }

    @Override
    public Job update(Job job) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(job, "job");
        ArgumentValidator.notNull(job.getScopeId(), "job.scopeId");
        ArgumentValidator.validateEntityName(job.getName(), "job.name");

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, job.getScopeId()));

        //
        // Check existence
        if (find(job.getScopeId(), job.getId()) == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, job.getId());
        }

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, job);

        //
        // Do update
        return entityManagerSession.doTransactedAction(em -> JobDAO.update(em, job));
    }

    @Override
    public Job find(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> JobDAO.find(em, scopeId, jobId));
    }

    @Override
    public JobListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        deleteInternal(scopeId, jobId, false);
    }

    @Override
    public void deleteForced(KapuaId scopeId, KapuaId jobId) throws KapuaException {
        deleteInternal(scopeId, jobId, true);
    }

    //
    // Private methods
    //

    /**
     * Deletes the {@link Job} like {@link #delete(KapuaId, KapuaId)}.
     * <p>
     * If {@code forced} is {@code true} {@link org.eclipse.kapua.service.authorization.permission.Permission} checked will be {@code job:delete:null},
     * and when invoking {@link JobEngineService#cleanJobData(KapuaId, KapuaId)} any exception is logged and ignored.
     *
     * @param scopeId The {@link KapuaId} scopeId of the {@link Job}.
     * @param jobId   The {@link KapuaId} of the {@link Job}.
     * @param forced  Whether or not the {@link Job} must be forcibly deleted.
     * @throws KapuaException In case something bad happens.
     * @since 1.1.0
     */
    private void deleteInternal(KapuaId scopeId, KapuaId jobId, boolean forced) throws KapuaException {

        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, forced ? null : scopeId));

        //
        // Check existence
        if (find(scopeId, jobId) == null) {
            throw new KapuaEntityNotFoundException(Job.TYPE, jobId);
        }

        //
        // Find all the triggers that are associated with this job
        TriggerQuery query = triggerFactory.newQuery(scopeId);
        AndPredicate andPredicate = query.andPredicate(
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_NAME, "jobId"),
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_VALUE, jobId.toCompactId()),
                query.attributePredicate(TriggerAttributes.TRIGGER_PROPERTIES_TYPE, KapuaId.class.getName())
        );

        query.setPredicate(andPredicate);

        //
        // Query for and delete all the triggers that are associated with this job
        KapuaSecurityUtils.doPrivileged(() -> {
            TriggerListResult triggers = triggerService.query(query);
            for (Trigger trig : triggers.getItems()) {
                triggerService.delete(trig.getScopeId(), trig.getId());
            }
        });

        //
        // Do delete
        try {
            KapuaSecurityUtils.doPrivileged(() -> jobEngineService.cleanJobData(scopeId, jobId));
        } catch (Exception e) {
            if (forced) {
                LOG.warn("Error while cleaning Job data. Ignoring exception since delete is forced! Error: {}", e.getMessage());
                LOG.debug("Error while cleaning Job data. Ignoring exception since delete is forced!", e);
            } else {
                throw e;
            }
        }

        entityManagerSession.doTransactedAction(em -> JobDAO.delete(em, scopeId, jobId));
    }
}
