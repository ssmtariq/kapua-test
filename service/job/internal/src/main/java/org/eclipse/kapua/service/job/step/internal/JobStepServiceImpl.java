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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.service.internal.KapuaNamedEntityServiceUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.model.query.QueryFactory;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.query.predicate.AttributePredicate.Operator;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.job.JobDomains;
import org.eclipse.kapua.service.job.exception.CannotModifyJobStepsException;
import org.eclipse.kapua.service.job.execution.JobExecutionAttributes;
import org.eclipse.kapua.service.job.execution.JobExecutionFactory;
import org.eclipse.kapua.service.job.execution.JobExecutionQuery;
import org.eclipse.kapua.service.job.execution.JobExecutionService;
import org.eclipse.kapua.service.job.internal.JobEntityManagerFactory;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.JobStepAttributes;
import org.eclipse.kapua.service.job.step.JobStepCreator;
import org.eclipse.kapua.service.job.step.JobStepIndex;
import org.eclipse.kapua.service.job.step.JobStepListResult;
import org.eclipse.kapua.service.job.step.JobStepQuery;
import org.eclipse.kapua.service.job.step.JobStepService;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinition;
import org.eclipse.kapua.service.job.step.definition.JobStepDefinitionService;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.bind.DatatypeConverter;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link JobStepService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class JobStepServiceImpl extends AbstractKapuaService implements JobStepService {

    @Inject
    private AuthorizationService authorizationService;
    @Inject
    private PermissionFactory permissionFactory;

    @Inject
    private JobExecutionService jobExecutionService;
    @Inject
    private JobExecutionFactory jobExecutionFactory;

    @Inject
    private JobStepDefinitionService jobStepDefinitionService;

    @Inject
    QueryFactory queryFactory;

    public JobStepServiceImpl() {
        super(JobEntityManagerFactory.getInstance(), null);
    }

    @Override
    public JobStep create(JobStepCreator jobStepCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(jobStepCreator, "jobStepCreator");
        ArgumentValidator.notNull(jobStepCreator.getScopeId(), "jobStepCreator.scopeId");
        ArgumentValidator.validateEntityName(jobStepCreator.getName(), "jobStepCreator.name");
        ArgumentValidator.notNull(jobStepCreator.getJobStepDefinitionId(), "jobStepCreator.stepDefinitionId");

        if (jobStepCreator.getDescription() != null) {
            ArgumentValidator.numRange(jobStepCreator.getDescription().length(), 0, 8192, "jobStepCreator.description");
        }

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobStepCreator.getScopeId()));

        //
        // Check job step definition
        validateJobStepProperties(jobStepCreator);

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(
                this,
                jobStepCreator,
                Collections.singletonList(queryFactory.newQuery().attributePredicate(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()))
        );

        //
        // Check Job Executions
        JobExecutionQuery jobExecutionQuery = jobExecutionFactory.newQuery(jobStepCreator.getScopeId());
        jobExecutionQuery.setPredicate(
                jobExecutionQuery.attributePredicate(JobExecutionAttributes.JOB_ID, jobStepCreator.getJobId())
        );

        if (jobExecutionService.count(jobExecutionQuery) > 0) {
            throw new CannotModifyJobStepsException(jobStepCreator.getJobId());
        }

        //
        // Populate JobStepCreator.stepIndex if not specified
        JobStepQuery query = new JobStepQueryImpl(jobStepCreator.getScopeId());
        if (jobStepCreator.getStepIndex() == null) {
            query.setPredicate(query.attributePredicate(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()));
            query.setSortCriteria(query.fieldSortCriteria(JobStepAttributes.STEP_INDEX, SortOrder.DESCENDING));
            query.setLimit(1);

            JobStepListResult jobStepListResult = query(query);
            JobStep lastJobStep = jobStepListResult.getFirstItem();

            jobStepCreator.setStepIndex(lastJobStep != null ? lastJobStep.getStepIndex() + 1 : JobStepIndex.FIRST);
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction((em) -> {
            // Check if JobStep.stepIndex is duplicate.
            JobStepQuery jobStepQuery = new JobStepQueryImpl(jobStepCreator.getScopeId());
            jobStepQuery.setPredicate(
                    jobStepQuery.andPredicate(
                            jobStepQuery.attributePredicate(JobStepAttributes.JOB_ID, jobStepCreator.getJobId()),
                            jobStepQuery.attributePredicate(JobStepAttributes.STEP_INDEX, jobStepCreator.getStepIndex())
                    )
            );

            JobStep jobStepAtIndex = JobStepDAO.query(em, jobStepQuery).getFirstItem();

            if (jobStepAtIndex != null) {
                // JobStepCreator inserted between existing JobSteps.
                // Moving existing JobSteps + 1
                JobStepQuery selectorJobStepQuery = new JobStepQueryImpl(jobStepAtIndex.getScopeId());
                selectorJobStepQuery.setPredicate(
                        selectorJobStepQuery.andPredicate(
                                selectorJobStepQuery.attributePredicate(JobStepAttributes.JOB_ID, jobStepAtIndex.getJobId()),
                                selectorJobStepQuery.attributePredicate(JobStepAttributes.STEP_INDEX, jobStepAtIndex.getStepIndex(), Operator.GREATER_THAN_OR_EQUAL)
                        )
                );

                shiftJobStepPosition(em, selectorJobStepQuery, +1);
            }

            return JobStepDAO.create(em, jobStepCreator);
        });
    }


    @Override
    public JobStep update(JobStep jobStep) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(jobStep, "jobStep");
        ArgumentValidator.notNull(jobStep.getScopeId(), "jobStep.scopeId");
        ArgumentValidator.validateEntityName(jobStep.getName(), "jobStep.name");
        ArgumentValidator.notNull(jobStep.getJobStepDefinitionId(), "jobStep.stepDefinitionId");
        if (jobStep.getDescription() != null) {
            ArgumentValidator.numRange(jobStep.getDescription().length(), 0, 8192, "jobStep.description");
        }

        //
        // Check access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, jobStep.getScopeId()));

        //
        // Check existence
        if (find(jobStep.getScopeId(), jobStep.getId()) == null) {
            throw new KapuaEntityNotFoundException(jobStep.getType(), jobStep.getId());
        }

        //
        // Check job step definition
        validateJobStepProperties(jobStep);

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(
                this,
                jobStep,
                Collections.singletonList(queryFactory.newQuery().attributePredicate(JobStepAttributes.JOB_ID, jobStep.getJobId()))
        );

        //
        // Check Job Executions
        JobExecutionQuery jobExecutionQuery = jobExecutionFactory.newQuery(jobStep.getScopeId());
        jobExecutionQuery.setPredicate(
                jobExecutionQuery.attributePredicate(JobExecutionAttributes.JOB_ID, jobStep.getJobId())
        );

        if (jobExecutionService.count(jobExecutionQuery) > 0) {
            throw new CannotModifyJobStepsException(jobStep.getJobId());
        }

        //
        // Do Update
        return entityManagerSession.doTransactedAction((em) -> {
            // Check if JobStep.stepIndex has changed
            JobStep currentJobStep = JobStepDAO.find(em, jobStep.getScopeId(), jobStep.getId());

            if (jobStep.getStepIndex() != currentJobStep.getStepIndex()) {

                if (jobStep.getStepIndex() < currentJobStep.getStepIndex()) {
                    // Moved before current position.
                    // Shift JobSteps +1
                    JobStepQuery selectorJobStepQuery = new JobStepQueryImpl(jobStep.getScopeId());
                    selectorJobStepQuery.setPredicate(
                            selectorJobStepQuery.andPredicate(
                                    selectorJobStepQuery.attributePredicate(JobStepAttributes.JOB_ID, jobStep.getJobId()),
                                    selectorJobStepQuery.attributePredicate(JobStepAttributes.STEP_INDEX, jobStep.getStepIndex(), Operator.GREATER_THAN_OR_EQUAL),
                                    selectorJobStepQuery.attributePredicate(JobStepAttributes.STEP_INDEX, currentJobStep.getStepIndex(), Operator.LESS_THAN)
                            )
                    );

                    shiftJobStepPosition(em, selectorJobStepQuery, +1);
                } else {
                    // Moved after current position.
                    // Shift JobSteps -1
                    JobStepQuery selectorJobStepQuery = new JobStepQueryImpl(jobStep.getScopeId());
                    selectorJobStepQuery.setPredicate(
                            selectorJobStepQuery.andPredicate(
                                    selectorJobStepQuery.attributePredicate(JobStepAttributes.JOB_ID, jobStep.getJobId()),
                                    selectorJobStepQuery.attributePredicate(JobStepAttributes.STEP_INDEX, currentJobStep.getStepIndex(), Operator.GREATER_THAN),
                                    selectorJobStepQuery.attributePredicate(JobStepAttributes.STEP_INDEX, jobStep.getStepIndex(), Operator.LESS_THAN_OR_EQUAL)
                            )
                    );

                    shiftJobStepPosition(em, selectorJobStepQuery, -1);
                }
            }

            return JobStepDAO.update(em, jobStep);
        });
    }

    @Override
    public JobStep find(KapuaId scopeId, KapuaId jobStepId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobStepId, "jobStepId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.write, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> JobStepDAO.find(em, scopeId, jobStepId));
    }

    @Override
    public JobStepListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> JobStepDAO.query(em, query));
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
        return entityManagerSession.doAction(em -> JobStepDAO.count(em, query));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId jobStepId) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(jobStepId, "jobStepId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(JobDomains.JOB_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        JobStep jobStep = find(scopeId, jobStepId);
        if (jobStep == null) {
            throw new KapuaEntityNotFoundException(JobStep.TYPE, jobStepId);
        }

        //
        // Check Job Executions
        JobExecutionQuery jobExecutionQuery = jobExecutionFactory.newQuery(scopeId);
        jobExecutionQuery.setPredicate(
                jobExecutionQuery.attributePredicate(JobExecutionAttributes.JOB_ID, jobStep.getJobId())
        );

        if (jobExecutionService.count(jobExecutionQuery) > 0) {
            throw new CannotModifyJobStepsException(jobStep.getJobId());
        }

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> {
            JobStep deletedJobStep = JobStepDAO.find(em, scopeId, jobStepId);

            JobStepDAO.delete(em, scopeId, jobStepId);

            //
            // Shift following steps of one position in the step index
            JobStepQuery query = new JobStepQueryImpl(scopeId);

            query.setPredicate(
                    query.andPredicate(
                            query.attributePredicate(JobStepAttributes.JOB_ID, deletedJobStep.getJobId()),
                            query.attributePredicate(JobStepAttributes.STEP_INDEX, deletedJobStep.getStepIndex(), Operator.GREATER_THAN)
                    )
            );

            JobStepListResult followingJobStep = JobStepDAO.query(em, query);

            for (JobStep js : followingJobStep.getItems()) {
                js.setStepIndex(js.getStepIndex() - 1);

                JobStepDAO.update(em, js);
            }
            return deletedJobStep;
        });
    }

    //
    // Private methods
    //

    /**
     * Shifts {@link JobStep} matched by the given {@link JobStepQuery} according to the given increment.
     *
     * @param em            The {@link EntityManager} which is owning the transaction.
     * @param selectorQuery The selector {@link JobStepQuery}.
     * @param increment     The increment o apply to the matched {@link JobStep}s
     * @throws KapuaException
     * @since 2.0.0
     */
    private void shiftJobStepPosition(EntityManager em, JobStepQuery selectorQuery, int increment) throws KapuaException {
        selectorQuery.setSortCriteria(selectorQuery.fieldSortCriteria(JobStepAttributes.STEP_INDEX, SortOrder.ASCENDING));

        JobStepListResult followingJobStepListResult = JobStepDAO.query(em, selectorQuery);

        LoggerFactory.getLogger(JobStepServiceImpl.class).warn("Got {} steps to move", followingJobStepListResult.getSize());

        // Move them +1/-1 position
        for (JobStep followingJobStep : followingJobStepListResult.getItems()) {
            LoggerFactory.getLogger(JobStepServiceImpl.class).warn("Moving step named {} to index {}", followingJobStep.getName(), followingJobStep.getStepIndex() + increment);
            followingJobStep.setStepIndex(followingJobStep.getStepIndex() + increment);
            JobStepDAO.update(em, followingJobStep);
        }
    }

    private void validateJobStepProperties(JobStepCreator jobStepCreator) throws KapuaException {
        JobStepDefinition jobStepDefinition = jobStepDefinitionService.find(jobStepCreator.getScopeId(), jobStepCreator.getJobStepDefinitionId());
        ArgumentValidator.notNull(jobStepDefinition, "jobStepCreator.jobStepDefinitionId");

        try {
            validateJobStepProperties(jobStepCreator.getStepProperties(), jobStepDefinition);
        } catch (KapuaIllegalArgumentException kiae) {
            throw new KapuaIllegalArgumentException("jobStepCreator." + kiae.getArgumentName(), kiae.getArgumentValue());
        }
    }

    private void validateJobStepProperties(JobStep jobStep) throws KapuaException {
        JobStepDefinition jobStepDefinition = jobStepDefinitionService.find(jobStep.getScopeId(), jobStep.getJobStepDefinitionId());
        ArgumentValidator.notNull(jobStepDefinition, "jobStep.jobStepDefinitionId");

        try {
            validateJobStepProperties(jobStep.getStepProperties(), jobStepDefinition);
        } catch (KapuaIllegalArgumentException kiae) {
            throw new KapuaIllegalArgumentException("jobStep." + kiae.getArgumentName(), kiae.getArgumentValue());
        }
    }

    private void validateJobStepProperties(List<JobStepProperty> jobStepProperties, JobStepDefinition jobStepDefinition) throws KapuaIllegalArgumentException {

        for (JobStepProperty jobStepProperty : jobStepProperties) {
            for (JobStepProperty jobStepDefinitionProperty : jobStepDefinition.getStepProperties()) {
                if (jobStepProperty.getName().equals(jobStepDefinitionProperty.getName())) {

                    if (jobStepDefinitionProperty.getRequired()) {
                        ArgumentValidator.notNull(jobStepProperty.getPropertyValue(), "stepProperties[]." + jobStepProperty.getName());
                    }

                    ArgumentValidator.areEqual(jobStepProperty.getPropertyType(), jobStepDefinitionProperty.getPropertyType(), "stepProperties[]." + jobStepProperty.getName());
                    ArgumentValidator.lengthRange(jobStepProperty.getPropertyValue(), jobStepDefinitionProperty.getMinLength(), jobStepDefinitionProperty.getMaxLength(), "stepProperties[]." + jobStepProperty.getName());

                    validateJobStepPropertyValue(jobStepProperty, jobStepDefinitionProperty);
                }
            }
        }
    }

    private <C extends Comparable<C>, E extends Enum<E>> void validateJobStepPropertyValue(JobStepProperty jobStepProperty, JobStepProperty jobStepDefinitionProperty) throws KapuaIllegalArgumentException {
        try {
            Class<?> jobStepDefinitionPropertyClass = Class.forName(jobStepDefinitionProperty.getPropertyType());

            if (Comparable.class.isAssignableFrom(jobStepDefinitionPropertyClass)) {
                Class<C> jobStepDefinitionPropertyClassComparable = (Class<C>) jobStepDefinitionPropertyClass;

                C propertyValue = fromString(jobStepProperty.getPropertyValue(), jobStepDefinitionPropertyClassComparable);
                C propertyMinValue = fromString(jobStepDefinitionProperty.getMinValue(), jobStepDefinitionPropertyClassComparable);
                C propertyMaxValue = fromString(jobStepDefinitionProperty.getMaxValue(), jobStepDefinitionPropertyClassComparable);

                ArgumentValidator.valueRange(propertyValue, propertyMinValue, propertyMaxValue, "stepProperties[]." + jobStepProperty.getName());

                if (String.class.equals(jobStepDefinitionPropertyClass) && !Strings.isNullOrEmpty(jobStepDefinitionProperty.getValidationRegex())) {
                    ArgumentValidator.match((String) propertyValue, () -> Pattern.compile(jobStepDefinitionProperty.getValidationRegex()), "stepProperties[]." + jobStepProperty.getName());
                }
            } else if (KapuaId.class.isAssignableFrom(jobStepDefinitionPropertyClass) ||
                    (jobStepDefinitionPropertyClass == byte[].class || jobStepDefinitionPropertyClass == Byte[].class)) {
                fromString(jobStepProperty.getPropertyValue(), jobStepDefinitionPropertyClass);
            } else if (jobStepDefinitionPropertyClass.isEnum()) {
                Class<E> jobStepDefinitionPropertyClassEnum = (Class<E>) jobStepDefinitionPropertyClass;
                Enum.valueOf(jobStepDefinitionPropertyClassEnum, jobStepProperty.getPropertyValue());
            } else {
                XmlUtil.unmarshal(jobStepProperty.getPropertyValue(), jobStepDefinitionPropertyClass);
            }

        } catch (KapuaIllegalArgumentException kiae) {
            throw kiae;
        } catch (Exception e) {
            throw new KapuaIllegalArgumentException("stepProperties[]." + jobStepProperty.getName(), jobStepProperty.getPropertyValue());
        }
    }

    public <T, E extends Enum<E>> T fromString(String jobStepPropertyString, Class<T> type) throws Exception {
        T stepProperty = null;
        if (jobStepPropertyString != null) {
            if (type == String.class) {
                stepProperty = (T) jobStepPropertyString;
            } else if (type == Integer.class) {
                stepProperty = (T) Integer.valueOf(jobStepPropertyString);
            } else if (type == Long.class) {
                stepProperty = (T) Long.valueOf(jobStepPropertyString);
            } else if (type == Float.class) {
                stepProperty = (T) Float.valueOf(jobStepPropertyString);
            } else if (type == Double.class) {
                stepProperty = (T) Double.valueOf(jobStepPropertyString);
            } else if (type == Boolean.class) {
                stepProperty = (T) Boolean.valueOf(jobStepPropertyString);
            } else if (type == byte[].class || type == Byte[].class) {
                stepProperty = (T) DatatypeConverter.parseBase64Binary(jobStepPropertyString);
            } else if (type == KapuaId.class) {
                stepProperty = (T) KapuaEid.parseCompactId(jobStepPropertyString);
            }
        }

        return stepProperty;
    }
}
