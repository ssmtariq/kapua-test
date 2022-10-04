/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.service.event.store.api.EventStoreRecord;
import org.eclipse.kapua.model.id.KapuaId;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.math.BigInteger;

@Category(JUnitTests.class)
public class EventStoreFactoryImplTest extends Assert {

    @Test
    public void newEntityTest() {
        EventStoreFactoryImpl eventStoreFactoryImpl = new EventStoreFactoryImpl();
        KapuaId[] scopeIdList = {null, new KapuaEid(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            assertNotNull("Null not expected.", eventStoreFactoryImpl.newEntity(scopeId));
            assertThat("EventStoreRecordImpl object expected.", eventStoreFactoryImpl.newEntity(scopeId), IsInstanceOf.instanceOf(EventStoreRecordImpl.class));
        }
    }

    @Test
    public void newCreatorTest() {
        EventStoreFactoryImpl eventStoreFactoryImpl = new EventStoreFactoryImpl();
        KapuaId[] scopeIdList = {null, new KapuaEid(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            assertNotNull("Null not expected.", eventStoreFactoryImpl.newCreator(scopeId));
            assertThat("EventStoreRecordImpl object expected.", eventStoreFactoryImpl.newCreator(scopeId), IsInstanceOf.instanceOf(EventStoreRecordCreatorImpl.class));
        }
    }

    @Test
    public void newQueryTest() {
        EventStoreFactoryImpl eventStoreFactoryImpl = new EventStoreFactoryImpl();
        KapuaId[] scopeIdList = {null, new KapuaEid(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            assertNotNull("Null not expected.", eventStoreFactoryImpl.newQuery(scopeId));
            assertThat("EventStoreQueryImpl object expected.", eventStoreFactoryImpl.newQuery(scopeId), IsInstanceOf.instanceOf(EventStoreQueryImpl.class));
        }
    }

    @Test
    public void newListResultTest() {
        EventStoreFactoryImpl eventStoreFactoryImpl = new EventStoreFactoryImpl();

        assertNotNull("Null not expected.", eventStoreFactoryImpl.newListResult());
        assertThat("EventStoreRecordListResultImpl object expected.", eventStoreFactoryImpl.newListResult(), IsInstanceOf.instanceOf(EventStoreRecordListResultImpl.class));
    }

    @Test
    public void cloneTest() {
        EventStoreFactoryImpl eventStoreFactoryImpl = new EventStoreFactoryImpl();
        EventStoreRecord eventStoreRecord = Mockito.mock(EventStoreRecord.class);
        EventStoreRecord nullEventStoreRecord = null;
        KapuaEntityCloneException kapuaEntityCloneException = new KapuaEntityCloneException(new Exception(), EventStoreRecord.TYPE, nullEventStoreRecord);

        assertNotNull("Null not expected.", eventStoreFactoryImpl.clone(eventStoreRecord));
        assertThat("EventStoreRecordImpl object expected.", eventStoreFactoryImpl.clone(eventStoreRecord), IsInstanceOf.instanceOf(EventStoreRecordImpl.class));

        try {
            eventStoreFactoryImpl.clone(nullEventStoreRecord);
        } catch (Exception e) {
            assertEquals("KapuaEntityCloneException expected.", kapuaEntityCloneException.toString(), e.toString());
        }
    }
} 
