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
package org.eclipse.kapua.service.device.registry.event.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;

/**
 * Device event DAO
 *
 * @since 1.0.0
 */
public class DeviceEventDAO extends ServiceDAO {

    /**
     * Create a new {@link DeviceEvent}
     *
     * @param em
     * @param deviceEventCreator
     * @return
     */
    public static DeviceEvent create(EntityManager em, DeviceEventCreator deviceEventCreator) {
        DeviceEvent deviceEvent = new DeviceEventImpl(deviceEventCreator.getScopeId());
        deviceEvent.setDeviceId(deviceEventCreator.getDeviceId());
        deviceEvent.setReceivedOn(deviceEventCreator.getReceivedOn());
        deviceEvent.setSentOn(deviceEventCreator.getSentOn());
        deviceEvent.setResource(deviceEventCreator.getResource());
        deviceEvent.setAction(deviceEventCreator.getAction());
        deviceEvent.setResponseCode(deviceEventCreator.getResponseCode());
        deviceEvent.setEventMessage(deviceEventCreator.getEventMessage());
        deviceEvent.setPosition(deviceEventCreator.getPosition());

        return ServiceDAO.create(em, deviceEvent);
    }

    /**
     * Find the device event by device event identifier
     *
     * @param em
     * @param scopeId
     * @param deviceEventId
     * @return
     */
    public static DeviceEvent find(EntityManager em, KapuaId scopeId, KapuaId deviceEventId) {
        return ServiceDAO.find(em, DeviceEventImpl.class, scopeId, deviceEventId);
    }

    /**
     * Return the device event list matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static DeviceEventListResult query(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.query(em, DeviceEvent.class, DeviceEventImpl.class, new DeviceEventListResultImpl(), query);
    }

    /**
     * Return the device event count matching the provided query
     *
     * @param em
     * @param query
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery query)
            throws KapuaException {
        return ServiceDAO.count(em, DeviceEvent.class, DeviceEventImpl.class, query);
    }

    /**
     * Delete the device event by device event identifier
     *
     * @param em
     * @param scopeId
     * @param deviceEventId
     * @return deleted entity
     * @throws KapuaEntityNotFoundException If the {@link DeviceEvent} is not found.
     */
    public static DeviceEvent delete(EntityManager em, KapuaId scopeId, KapuaId deviceEventId)
            throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, DeviceEventImpl.class, scopeId, deviceEventId);
    }

}
