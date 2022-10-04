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

import org.apache.commons.lang.NotImplementedException;
import org.eclipse.kapua.KapuaDuplicateNameException;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableService;
import org.eclipse.kapua.commons.jpa.EntityManagerContainer;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.ServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.device.registry.common.DeviceValidationRegex;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionAttributes;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.internal.DeviceEntityManagerFactory;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCache;
import org.eclipse.kapua.service.device.registry.internal.DeviceRegistryCacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

/**
 * {@link DeviceConnectionService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class DeviceConnectionServiceImpl extends AbstractKapuaConfigurableService implements DeviceConnectionService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceConnectionServiceImpl.class);

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public DeviceConnectionServiceImpl() {
        this(DeviceEntityManagerFactory.getInstance());
    }

    /**
     * Constructor.
     *
     * @param deviceEntityManagerFactory The {@link DeviceEntityManagerFactory#getInstance()}.
     * @since 1.0.0
     */
    public DeviceConnectionServiceImpl(DeviceEntityManagerFactory deviceEntityManagerFactory) {
        super(DeviceConnectionService.class.getName(),
                DeviceDomains.DEVICE_CONNECTION_DOMAIN,
                deviceEntityManagerFactory,
                DeviceRegistryCacheFactory.getInstance());
    }

    @Override
    public DeviceConnection create(DeviceConnectionCreator deviceConnectionCreator)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionCreator, "deviceConnectionCreator");
        ArgumentValidator.notNull(deviceConnectionCreator.getScopeId(), "deviceConnectionCreator.scopeId");
        ArgumentValidator.notNull(deviceConnectionCreator.getUserId(), "deviceConnectionCreator.userId");
        ArgumentValidator.notEmptyOrNull(deviceConnectionCreator.getClientId(), "deviceConnectionCreator.clientId");
        ArgumentValidator.lengthRange(deviceConnectionCreator.getClientId(), 1, 255, "deviceCreator.clientId");
        ArgumentValidator.match(deviceConnectionCreator.getClientId(), DeviceValidationRegex.CLIENT_ID, "deviceCreator.clientId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, null));

        //
        // Check duplicate ClientId
        DeviceConnectionQuery query = new DeviceConnectionQueryImpl(deviceConnectionCreator.getScopeId());
        query.setPredicate(query.attributePredicate(DeviceConnectionAttributes.CLIENT_ID, deviceConnectionCreator.getClientId()));

        if (count(query) > 0) {
            throw new KapuaDuplicateNameException(deviceConnectionCreator.getClientId());
        }

        //
        // Do create
        return entityManagerSession.doTransactedAction(
                EntityManagerContainer
                        .<DeviceConnection>create()
                        .onResultHandler(entityManager -> DeviceConnectionDAO.create(entityManager, deviceConnectionCreator)));
    }

    @Override
    public DeviceConnection update(DeviceConnection deviceConnection)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnection, "deviceConnection");
        ArgumentValidator.notNull(deviceConnection.getId(), "deviceConnection.id");
        ArgumentValidator.notNull(deviceConnection.getScopeId(), "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, null));

        //
        // Do Update
        return entityManagerSession.doTransactedAction(
                EntityManagerContainer
                        .<DeviceConnection>create()
                        .onResultHandler(entityManager -> {
                            if (DeviceConnectionDAO.find(entityManager, deviceConnection.getScopeId(), deviceConnection.getId()) == null) {
                                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnection.getId());
                            }
                            return DeviceConnectionDAO.update(entityManager, deviceConnection);
                        }).onBeforeHandler(() -> {
                            ((DeviceRegistryCache) entityCache).removeByDeviceConnectionId(deviceConnection.getScopeId(), deviceConnection.getId());
                            return null;
                        }));
    }

    @Override
    public DeviceConnection find(KapuaId scopeId, KapuaId entityId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(entityId, "entityId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(
                EntityManagerContainer
                        .<DeviceConnection>create()
                        .onResultHandler(entityManager -> DeviceConnectionDAO.find(entityManager, scopeId, entityId)));
    }

    @Override
    public DeviceConnection findByClientId(KapuaId scopeId, String clientId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notEmptyOrNull(clientId, "clientId");

        //
        // Build query
        DeviceConnectionQueryImpl query = new DeviceConnectionQueryImpl(scopeId);
        query.setPredicate(query.attributePredicate(DeviceConnectionAttributes.CLIENT_ID, clientId));

        //
        // Do find
        return query(query).getFirstItem();
    }

    @Override
    public DeviceConnectionListResult query(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(
                EntityManagerContainer
                        .<DeviceConnectionListResult>create()
                        .onResultHandler(entityManager -> DeviceConnectionDAO.query(entityManager, query)));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(
                EntityManagerContainer
                        .<Long>create()
                        .onResultHandler(entityManager -> DeviceConnectionDAO.count(entityManager, query)));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId deviceConnectionId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(deviceConnectionId, "deviceConnection.id");
        ArgumentValidator.notNull(scopeId, "deviceConnection.scopeId");

        //
        // Check Access
        KapuaLocator locator = KapuaLocator.getInstance();
        AuthorizationService authorizationService = locator.getService(AuthorizationService.class);
        PermissionFactory permissionFactory = locator.getFactory(PermissionFactory.class);
        authorizationService.checkPermission(permissionFactory.newPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, null));

        entityManagerSession.doTransactedAction(
                EntityManagerContainer
                        .create()
                        .onResultHandler(entityManager -> {
                            // TODO: check if it is correct to remove this statement (already thrown by the delete method, but
                            //  without TYPE)
                            if (DeviceConnectionDAO.find(entityManager, scopeId, deviceConnectionId) == null) {
                                throw new KapuaEntityNotFoundException(DeviceConnection.TYPE, deviceConnectionId);
                            }
                            return DeviceConnectionDAO.delete(entityManager, scopeId, deviceConnectionId);
                        })
                        .onAfterHandler((emptyParam) -> ((DeviceRegistryCache) entityCache).removeByDeviceConnectionId(scopeId, deviceConnectionId)));
    }

    @Override
    public void connect(DeviceConnectionCreator creator)
            throws KapuaException {
        throw new NotImplementedException();
    }

    @Override
    public void disconnect(KapuaId scopeId, String clientId)
            throws KapuaException {
        throw new NotImplementedException();
    }

    // @ListenServiceEvent(fromAddress="account")
    public void onKapuaEvent(ServiceEvent kapuaEvent) throws KapuaException {
        if (kapuaEvent == null) {
            //service bus error. Throw some exception?
        }
        LOG.info("DeviceConnectionService: received kapua event from {}, operation {}", kapuaEvent.getService(), kapuaEvent.getOperation());
        if ("account".equals(kapuaEvent.getService()) && "delete".equals(kapuaEvent.getOperation())) {
            deleteConnectionByAccountId(kapuaEvent.getScopeId(), kapuaEvent.getEntityId());
        }
    }

    //
    // Private methods
    //

    private void deleteConnectionByAccountId(KapuaId scopeId, KapuaId accountId) throws KapuaException {
        KapuaLocator locator = KapuaLocator.getInstance();
        DeviceConnectionFactory deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(accountId);

        DeviceConnectionListResult deviceConnectionsToDelete = query(query);

        for (DeviceConnection dc : deviceConnectionsToDelete.getItems()) {
            delete(dc.getScopeId(), dc.getId());
        }
    }
}
