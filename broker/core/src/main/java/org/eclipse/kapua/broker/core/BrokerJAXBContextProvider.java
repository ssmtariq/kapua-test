/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobAlreadyRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobInvalidTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingStepExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobMissingTargetExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobNotRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobResumingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobRunningExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobScopedEngineExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStartingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.JobStoppingExceptionInfo;
import org.eclipse.kapua.app.api.core.exception.model.ThrowableInfo;
import org.eclipse.kapua.commons.configuration.metatype.TscalarImpl;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreXmlRegistry;
import org.eclipse.kapua.commons.util.xml.JAXBContextProvider;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.job.engine.JobStartOptions;
import org.eclipse.kapua.job.engine.commons.model.JobTargetSublist;
import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaTdesignate;
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTmetadata;
import org.eclipse.kapua.model.config.metatype.KapuaTobject;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.MetatypeXmlRegistry;
import org.eclipse.kapua.service.device.management.command.DeviceCommandInput;
import org.eclipse.kapua.service.device.management.command.DeviceCommandOutput;
import org.eclipse.kapua.service.job.Job;
import org.eclipse.kapua.service.job.JobListResult;
import org.eclipse.kapua.service.job.JobXmlRegistry;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.HashMap;
import java.util.Map;

public class BrokerJAXBContextProvider implements JAXBContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(BrokerJAXBContextProvider.class);

    private JAXBContext context;

    @Override
    public JAXBContext getJAXBContext() throws KapuaException {
        if (context == null) {
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(MarshallerProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);

            Class<?>[] classes = new Class<?>[]{
                    KapuaTmetadata.class,
                    KapuaTocd.class,
                    KapuaTad.class,
                    KapuaTicon.class,
                    TscalarImpl.class,
                    KapuaToption.class,
                    KapuaTdesignate.class,
                    KapuaTobject.class,
                    MetatypeXmlRegistry.class,

                    // KapuaEvent
                    ServiceEvent.class,
                    EventStoreRecordCreator.class,
                    EventStoreRecordListResult.class,
                    EventStoreRecordQuery.class,
                    EventStoreXmlRegistry.class,

                    //TODO EXT-CAMEL only for test remove when jobs will be defined in their own container
                    // Jobs
                    Job.class,
                    JobListResult.class,
                    JobXmlRegistry.class,
                    JobTargetSublist.class,
                    DeviceCommandInput.class,
                    DeviceCommandOutput.class,

                    // Job Engine
                    JobStartOptions.class,

                    // REST API exception models
                    ThrowableInfo.class,
                    ExceptionInfo.class,

                    // Jobs Exception Info
                    CleanJobDataExceptionInfo.class,
                    JobAlreadyRunningExceptionInfo.class,
                    JobEngineExceptionInfo.class,
                    JobScopedEngineExceptionInfo.class,
                    JobInvalidTargetExceptionInfo.class,
                    JobMissingStepExceptionInfo.class,
                    JobMissingTargetExceptionInfo.class,
                    JobNotRunningExceptionInfo.class,
                    JobResumingExceptionInfo.class,
                    JobRunningExceptionInfo.class,
                    JobStartingExceptionInfo.class,
                    JobStoppingExceptionInfo.class
            };
            try {
                context = JAXBContextFactory.createContext(classes, properties);
                LOG.debug("Default JAXB context initialized!");
            } catch (JAXBException e) {
                throw KapuaException.internalError(e, "Error creating JAXBContext!");
            }
        }
        return context;
    }
}
