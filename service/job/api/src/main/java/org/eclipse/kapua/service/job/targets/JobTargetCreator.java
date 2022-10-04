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
package org.eclipse.kapua.service.job.targets;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * {@link JobTargetCreator} encapsulates all the information needed to create a new JobTarget in the system.<br>
 * The data provided will be used to seed the new JobTarget.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "jobTargetCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = JobTargetXmlRegistry.class, factoryMethod = "newJobTargetCreator")
public interface JobTargetCreator extends KapuaUpdatableEntityCreator<JobTarget> {

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobId();

    void setJobId(KapuaId jobId);

    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getJobTargetId();

    void setJobTargetId(KapuaId jobTargetId);
}
