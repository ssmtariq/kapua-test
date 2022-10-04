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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManagerFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreDomains;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreService;
import org.eclipse.kapua.commons.service.internal.AbstractKapuaService;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.event.RaiseServiceEvent;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntityAttributes;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;

import javax.inject.Inject;

/**
 * {@link EventStoreService} implementation.
 *
 * @since 1.0.0
 */
public class EventStoreServiceImpl extends AbstractKapuaService implements EventStoreService {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final AuthorizationService AUTHORIZATION_SERVICE = LOCATOR.getService(AuthorizationService.class);
    private static final PermissionFactory PERMISSION_FACTORY = LOCATOR.getFactory(PermissionFactory.class);

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    @Inject
    public EventStoreServiceImpl(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    @Override
    @RaiseServiceEvent
    public EventStoreRecord create(EventStoreRecordCreator kapuaEventCreator)
            throws KapuaException {

        throw new UnsupportedOperationException();
    }

    @Override
    @RaiseServiceEvent
    public EventStoreRecord update(EventStoreRecord kapuaEvent)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(kapuaEvent.getId(), "kapuaEvent.id");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EventStoreDomains.EVENT_STORE_DOMAIN, Actions.write, kapuaEvent.getScopeId()));

        //
        // Do update
        return entityManagerSession.doTransactedAction(em -> {
            EventStoreRecord oldKapuaEvent = EventStoreDAO.find(em, kapuaEvent.getScopeId(), kapuaEvent.getId());
            if (oldKapuaEvent == null) {
                throw new KapuaEntityNotFoundException(EventStoreRecord.TYPE, kapuaEvent.getId());
            }

            // Update
            return EventStoreDAO.update(em, kapuaEvent);
        });
    }

    @Override
    @RaiseServiceEvent
    public void delete(KapuaId scopeId, KapuaId kapuaEventId)
            throws KapuaException {

        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(kapuaEventId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        Actions action = Actions.write;
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EventStoreDomains.EVENT_STORE_DOMAIN, action, scopeId));

        //
        // Do delete
        entityManagerSession.doTransactedAction(em -> EventStoreDAO.delete(em, scopeId, kapuaEventId));
    }

    @Override
    public EventStoreRecord find(KapuaId scopeId, KapuaId kapuaEventId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(scopeId, KapuaEntityAttributes.SCOPE_ID);
        ArgumentValidator.notNull(kapuaEventId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EventStoreDomains.EVENT_STORE_DOMAIN, Actions.read, scopeId));

        //
        // Make sure kapuaEvent exists
        return findById(kapuaEventId);
    }

    @Override
    public EventStoreRecord find(KapuaId kapuaEventId)
            throws KapuaException {
        //
        // Validation of the fields
        ArgumentValidator.notNull(kapuaEventId, KapuaEntityAttributes.ENTITY_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EventStoreDomains.EVENT_STORE_DOMAIN, Actions.read, kapuaEventId));

        //
        // Make sure kapuaEvent exists
        return findById(kapuaEventId);
    }

    @Override
    public EventStoreRecordListResult query(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EventStoreDomains.EVENT_STORE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> EventStoreDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query)
            throws KapuaException {
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(EventStoreDomains.EVENT_STORE_DOMAIN, Actions.read, query.getScopeId()));

        return entityManagerSession.doAction(em -> EventStoreDAO.count(em, query));
    }

    /**
     * Find an {@link EventStoreRecord} without authorization checks.
     *
     * @param kapuaEventId
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    private EventStoreRecord findById(KapuaId kapuaEventId)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(kapuaEventId, KapuaEntityAttributes.ENTITY_ID);

        return entityManagerSession.doAction(em -> EventStoreDAO.find(em, null, kapuaEventId));
    }

}
