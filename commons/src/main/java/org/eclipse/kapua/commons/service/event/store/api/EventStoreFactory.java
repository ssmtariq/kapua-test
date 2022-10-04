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
package org.eclipse.kapua.commons.service.event.store.api;

import org.eclipse.kapua.model.KapuaEntityFactory;

/**
 * {@link EventStoreFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface EventStoreFactory extends KapuaEntityFactory<EventStoreRecord, EventStoreRecordCreator, EventStoreRecordQuery, EventStoreRecordListResult> {

}
