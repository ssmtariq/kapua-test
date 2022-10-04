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
package org.eclipse.kapua.service.job.execution;

import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;

/**
 * {@link JobExecution} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobExecution")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobExecutionXmlRegistry.class, factoryMethod = "newJobExecution")
public interface JobExecution extends KapuaUpdatableEntity {

    String TYPE = "jobExecution";

    @Override
    default String getType() {
        return TYPE;
    }

    /**
     * @return
     * @since 1.0.0
     */
    KapuaId getJobId();

    /**
     * @param jobId
     * @since 1.0.0
     */
    void setJobId(KapuaId jobId);

    /**
     * @return
     * @since 1.0.0
     */
    Date getStartedOn();

    /**
     * @param startedOn
     * @since 1.0.0
     */
    void setStartedOn(Date startedOn);

    /**
     * @return
     * @since 1.0.0
     */
    Date getEndedOn();

    /**
     * @param endedOn
     * @since 1.0.0
     */
    void setEndedOn(Date endedOn);

    /**
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "targetIds")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    <I extends KapuaId> Set<I> getTargetIds();

    /**
     * @param tagTargetIds
     * @since 1.1.0
     */
    void setTargetIds(Set<KapuaId> tagTargetIds);

    /**
     * @return
     * @since 1.1.0
     */
    String getLog();

    /**
     * @param log
     * @since 1.1.0
     */
    void setLog(String log);
}
