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
package org.eclipse.kapua.service.device.management.registry.operation.notification.internal;

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotification;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationCreator;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationFactory;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationListResult;
import org.eclipse.kapua.service.device.management.registry.operation.notification.ManagementOperationNotificationQuery;

import javax.inject.Singleton;

/**
 * {@link ManagementOperationNotificationFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class ManagementOperationNotificationFactoryImpl implements ManagementOperationNotificationFactory {

    @Override
    public ManagementOperationNotification newEntity(KapuaId scopeId) {
        return new ManagementOperationNotificationImpl(scopeId);
    }

    @Override
    public ManagementOperationNotificationCreator newCreator(KapuaId scopeId) {
        return new ManagementOperationNotificationCreatorImpl(scopeId);
    }

    @Override
    public ManagementOperationNotificationQuery newQuery(KapuaId scopeId) {
        return new ManagementOperationNotificationQueryImpl(scopeId);
    }

    @Override
    public ManagementOperationNotificationListResult newListResult() {
        return new ManagementOperationNotificationListResultImpl();
    }

    @Override
    public ManagementOperationNotification clone(ManagementOperationNotification managementOperationNotification) {
        return new ManagementOperationNotificationImpl(managementOperationNotification);
    }
}
