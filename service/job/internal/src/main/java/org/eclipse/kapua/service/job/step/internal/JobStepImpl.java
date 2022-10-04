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

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.AbstractKapuaNamedEntity;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.job.step.JobStep;
import org.eclipse.kapua.service.job.step.definition.JobStepProperty;
import org.eclipse.kapua.service.job.step.definition.internal.JobStepPropertyImpl;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link JobStep} implementation.
 *
 * @since 1.0.0
 */
@Entity(name = "JobStep")
@Table(name = "job_job_step")
public class JobStepImpl extends AbstractKapuaNamedEntity implements JobStep {

    private static final long serialVersionUID = -5686107367635300337L;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_id", nullable = false, updatable = false))
    })
    private KapuaEid jobId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "eid", column = @Column(name = "job_step_definition_id", nullable = false, updatable = false))
    })
    private KapuaEid jobStepDefinitionId;

    @Basic
    @Column(name = "step_index", nullable = false, updatable = true)
    private Integer stepIndex;

    @ElementCollection
    @CollectionTable(name = "job_job_step_properties", joinColumns = @JoinColumn(name = "step_id", referencedColumnName = "id"))
    private List<JobStepPropertyImpl> stepProperties;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public JobStepImpl() {
    }

    /**
     * Constructor.
     *
     * @param scopeId The scope {@link KapuaId} to set into the {@link JobStep}
     * @since 1.0.0
     */
    public JobStepImpl(KapuaId scopeId) {
        super(scopeId);
    }

    /**
     * Clone constructor.
     *
     * @param jobStep
     * @throws KapuaException
     * @since 1.1.0
     */
    public JobStepImpl(JobStep jobStep) throws KapuaException {
        super(jobStep);

        setJobId(jobStep.getJobId());
        setJobStepDefinitionId(jobStep.getJobStepDefinitionId());
        setStepIndex(jobStep.getStepIndex());
        setStepProperties(jobStep.getStepProperties());
    }

    @Override
    public KapuaId getJobId() {
        return jobId;
    }

    @Override
    public void setJobId(KapuaId jobId) {
        this.jobId = KapuaEid.parseKapuaId(jobId);
    }

    @Override
    public KapuaId getJobStepDefinitionId() {
        return jobStepDefinitionId;
    }

    @Override
    public void setJobStepDefinitionId(KapuaId jobStepDefinitionId) {
        this.jobStepDefinitionId = KapuaEid.parseKapuaId(jobStepDefinitionId);
    }

    @Override
    public int getStepIndex() {
        return stepIndex;
    }

    @Override
    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    @Override
    public List<JobStepPropertyImpl> getStepProperties() {
        if (stepProperties == null) {
            stepProperties = new ArrayList<>();
        }

        return stepProperties;
    }

    @Override
    public void setStepProperties(List<JobStepProperty> jobStepProperties) {

        this.stepProperties = new ArrayList<>();

        for (JobStepProperty sp : jobStepProperties) {
            this.stepProperties.add(JobStepPropertyImpl.parse(sp));
        }
    }
}
