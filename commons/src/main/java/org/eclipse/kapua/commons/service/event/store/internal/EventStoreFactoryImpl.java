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
package org.eclipse.kapua.commons.service.event.store.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreFactory;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordCreator;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordListResult;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecordQuery;
import org.eclipse.kapua.model.id.KapuaId;

import javax.inject.Singleton;

/**
 * {@link EventStoreFactory} implementation
 *
 * @since 1.0.0
 */
@Singleton
public class EventStoreFactoryImpl implements EventStoreFactory {

    @Override
    public EventStoreRecord newEntity(KapuaId scopeId) {
        return new EventStoreRecordImpl(scopeId);
    }

    @Override
    public EventStoreRecordCreator newCreator(KapuaId scopeId) {
        return new EventStoreRecordCreatorImpl(scopeId);
    }

    @Override
    public EventStoreRecordQuery newQuery(KapuaId scopeId) {
        return new EventStoreQueryImpl(scopeId);
    }

    @Override
    public EventStoreRecordListResult newListResult() {
        return new EventStoreRecordListResultImpl();
    }

    @Override
    public EventStoreRecord clone(EventStoreRecord eventStoreRecord) {
        try {
            return new EventStoreRecordImpl(eventStoreRecord);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, EventStoreRecord.TYPE, eventStoreRecord);
        }
    }
}
