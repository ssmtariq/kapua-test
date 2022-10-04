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
package org.eclipse.kapua.app.console.module.job.server;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.eclipse.kapua.KapuaErrorCodes;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.api.server.util.KapuaExceptionHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.util.GwtKapuaCommonsModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTargetCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTargetQuery;
import org.eclipse.kapua.app.console.module.job.shared.service.GwtJobTargetService;
import org.eclipse.kapua.app.console.module.job.shared.util.GwtKapuaJobModelConverter;
import org.eclipse.kapua.app.console.module.job.shared.util.KapuaGwtJobModelConverter;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.job.targets.JobTarget;
import org.eclipse.kapua.service.job.targets.JobTargetCreator;
import org.eclipse.kapua.service.job.targets.JobTargetFactory;
import org.eclipse.kapua.service.job.targets.JobTargetListResult;
import org.eclipse.kapua.service.job.targets.JobTargetQuery;
import org.eclipse.kapua.service.job.targets.JobTargetService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GwtJobTargetServiceImpl extends KapuaRemoteServiceServlet implements GwtJobTargetService {

    private static final long serialVersionUID = -4365251346832037608L;

    private static final String NOT_AVAILABLE = "Not available";

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final JobTargetService JOB_TARGET_SERVICE = LOCATOR.getService(JobTargetService.class);
    private static final JobTargetFactory JOB_TARGET_FACTORY = LOCATOR.getFactory(JobTargetFactory.class);

    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);
    private static final DeviceFactory DEVICE_FACTORY = LOCATOR.getFactory(DeviceFactory.class);

    @Override
    public PagingLoadResult<GwtJobTarget> query(PagingLoadConfig loadConfig, GwtJobTargetQuery gwtJobTargetQuery) throws GwtKapuaException {
        //
        // Do query
        int totalLength = 0;
        List<GwtJobTarget> gwtJobTargetList = new ArrayList<GwtJobTarget>();
        try {
            // Convert from GWT entity
            JobTargetQuery jobTargetQuery = GwtKapuaJobModelConverter.convertJobTargetQuery(gwtJobTargetQuery, loadConfig);

            // query
            JobTargetListResult jobTargetList = JOB_TARGET_SERVICE.query(jobTargetQuery);
            totalLength = jobTargetList.getTotalCount().intValue();

            List<KapuaId> deviceIds = new ArrayList<KapuaId>();
            // Convert to GWT entity
            for (JobTarget jt : jobTargetList.getItems()) {
                deviceIds.add(jt.getJobTargetId());
            }

            DeviceQuery query = DEVICE_FACTORY.newQuery(jobTargetQuery.getScopeId());
            query.setPredicate(query.attributePredicate(DeviceAttributes.ENTITY_ID, deviceIds));
            DeviceListResult deviceListResult = DEVICE_REGISTRY_SERVICE.query(query);

            Map<KapuaId, Device> deviceMap = new HashMap<KapuaId, Device>();
            for (Device device : deviceListResult.getItems()) {
                deviceMap.put(device.getId(), device);
            }

            for (JobTarget jt : jobTargetList.getItems()) {
                GwtJobTarget gwtJobTarget = KapuaGwtJobModelConverter.convertJobTarget(jt);
                Device device = DEVICE_REGISTRY_SERVICE.find(KapuaEid.parseCompactId(gwtJobTarget.getScopeId()), KapuaEid.parseCompactId(gwtJobTarget.getJobTargetId()));
                if (device != null) {
                    insertClientId(gwtJobTarget, deviceMap.get(jt.getJobTargetId()));
                    gwtJobTargetList.add(gwtJobTarget);
                }
            }

        } catch (Exception e) {
            KapuaExceptionHandler.handle(e);
        }

        return new BasePagingLoadResult<GwtJobTarget>(gwtJobTargetList, loadConfig != null ? loadConfig.getOffset() : 0, totalLength);
    }

    @Override
    public PagingLoadResult<GwtJobTarget> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId) throws GwtKapuaException {
        if (jobId != null) {
            GwtJobTargetQuery gwtJobTargetQuery = new GwtJobTargetQuery();
            gwtJobTargetQuery.setScopeId(scopeId);
            gwtJobTargetQuery.setJobId(jobId);
            return query(loadConfig, gwtJobTargetQuery);
        } else {
            return new BasePagingLoadResult<GwtJobTarget>(new ArrayList<GwtJobTarget>(), 0, 0);
        }
    }

    @Override
    public List<GwtJobTarget> findByJobId(String scopeId, String jobId, boolean fetchDetails) throws GwtKapuaException {
        GwtJobTargetQuery gwtJobTargetQuery = new GwtJobTargetQuery();
        gwtJobTargetQuery.setScopeId(scopeId);
        gwtJobTargetQuery.setJobId(jobId);
        if (fetchDetails) {
            // TODO fetch details
        }
        return query(null, gwtJobTargetQuery).getData();
    }

    @Override
    public List<GwtJobTarget> create(GwtXSRFToken xsrfToken, String scopeId, String jobId, List<GwtJobTargetCreator> gwtJobTargetCreatorList) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);
        List<GwtJobTarget> existingTargets = findByJobId(scopeId, jobId, false);
        List<GwtJobTarget> gwtJobTargetList = new ArrayList<GwtJobTarget>();
        List<Device> devices = new ArrayList<Device>();
        try {
            for (GwtJobTargetCreator gwtJobTargetCreator : gwtJobTargetCreatorList) {
                if (findExtistingTarget(gwtJobTargetCreator.getJobTargetId(), existingTargets)) {
                    continue;
                }
                Device device = DEVICE_REGISTRY_SERVICE.find(KapuaEid.parseCompactId(gwtJobTargetCreator.getScopeId()), KapuaEid.parseCompactId(gwtJobTargetCreator.getJobTargetId()));
                if (device != null) {
                    devices.add(device);
                }
                KapuaId creatorScopeId = KapuaEid.parseCompactId(gwtJobTargetCreator.getScopeId());
                JobTargetCreator jobTargetCreator = JOB_TARGET_FACTORY.newCreator(creatorScopeId);
                jobTargetCreator.setJobId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetCreator.getJobId()));
                jobTargetCreator.setJobTargetId(GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetCreator.getJobTargetId()));

                // Create the Job Target
                JobTarget jobTarget = JOB_TARGET_SERVICE.create(jobTargetCreator);

                // convert to GwtJobTarget and return
                gwtJobTargetList.add(KapuaGwtJobModelConverter.convertJobTarget(jobTarget));
            }

            if (devices.isEmpty()) {
                throw new KapuaException(KapuaErrorCodes.DEVICE_NOT_FOUND);
            }
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
        return gwtJobTargetList;
    }

    private boolean findExtistingTarget(String jobTargetId, List<GwtJobTarget> existingTargets) {
        for (GwtJobTarget existingTarget : existingTargets) {
            if (existingTarget.getJobTargetId().equals(jobTargetId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(GwtXSRFToken xsrfToken, String gwtScopeId, String gwtJobTargetId) throws GwtKapuaException {
        checkXSRFToken(xsrfToken);

        try {
            KapuaId scopeId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtScopeId);
            KapuaId jobTargetId = GwtKapuaCommonsModelConverter.convertKapuaId(gwtJobTargetId);

            JOB_TARGET_SERVICE.delete(scopeId, jobTargetId);
        } catch (Throwable t) {
            KapuaExceptionHandler.handle(t);
        }
    }

    /**
     * For each item query clientId by its foreign key and insert it into existing list.
     *
     * @param gwtJobTarget existing target that is updated
     * @param device       existing device
     * @throws KapuaException
     */
    private void insertClientId(GwtJobTarget gwtJobTarget, Device device) throws KapuaException {
        String clientId = null;
        String displayName = null;
        if (device != null) {
            clientId = device.getClientId();
            displayName = device.getDisplayName();
        }

        if (clientId != null) {
            gwtJobTarget.setClientId(clientId);
            gwtJobTarget.setDisplayName(displayName);
        } else {
            gwtJobTarget.setClientId(NOT_AVAILABLE);
        }
    }
}
