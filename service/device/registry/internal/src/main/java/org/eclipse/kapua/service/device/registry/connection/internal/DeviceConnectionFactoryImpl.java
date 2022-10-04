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
package org.eclipse.kapua.service.device.registry.connection.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;

import javax.inject.Singleton;

/**
 * {@link DeviceConnectionFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class DeviceConnectionFactoryImpl implements DeviceConnectionFactory {

    @Override
    public DeviceConnectionCreator newCreator(KapuaId scopeId) {
        return new DeviceConnectionCreatorImpl(scopeId);
    }

    @Override
    public DeviceConnectionQuery newQuery(KapuaId scopeId) {
        return new DeviceConnectionQueryImpl(scopeId);
    }

    @Override
    public DeviceConnection newEntity(KapuaId scopeId) {
        return new DeviceConnectionImpl(scopeId);
    }

    @Override
    public DeviceConnectionListResult newListResult() {
        return new DeviceConnectionListResultImpl();
    }

    @Override
    public DeviceConnection clone(DeviceConnection deviceConnection) {
        try {
            return new DeviceConnectionImpl(deviceConnection);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, DeviceConnection.TYPE, deviceConnection);
        }
    }
}
