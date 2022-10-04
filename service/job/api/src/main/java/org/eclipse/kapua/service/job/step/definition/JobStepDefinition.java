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
package org.eclipse.kapua.service.job.step.definition;

import org.eclipse.kapua.model.KapuaNamedEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link JobStepDefinition} {@link org.eclipse.kapua.model.KapuaEntity} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobStepDefinition")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobStepDefinitionXmlRegistry.class, factoryMethod = "newJobStepDefinition")
public interface JobStepDefinition extends KapuaNamedEntity {

    String TYPE = "stepDefinition";

    @Override
    default String getType() {
        return TYPE;
    }

    JobStepType getStepType();

    void setStepType(JobStepType jobStepType);

    String getReaderName();

    void setReaderName(String readerName);

    String getProcessorName();

    void setProcessorName(String processorName);

    String getWriterName();

    void setWriterName(String writesName);

    <P extends JobStepProperty> List<P> getStepProperties();

    void setStepProperties(List<JobStepProperty> jobStepProperties);

}
