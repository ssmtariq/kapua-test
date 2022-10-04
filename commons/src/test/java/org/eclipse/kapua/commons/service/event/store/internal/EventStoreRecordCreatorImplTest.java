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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdImpl;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigInteger;

@Category(JUnitTests.class)
public class EventStoreRecordCreatorImplTest extends Assert {
    @Test
    public void eventStoreRecordCreatorImplTest() {
        KapuaId[] scopeIdList = {null, new KapuaIdImpl(BigInteger.ONE)};

        for (KapuaId scopeId : scopeIdList) {
            EventStoreRecordCreatorImpl eventStoreRecordCreatorImpl = new EventStoreRecordCreatorImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, eventStoreRecordCreatorImpl.getScopeId());
        }
    }
}  
