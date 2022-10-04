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
package org.eclipse.kapua.service.scheduler.trigger.quartz;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.scheduler.trigger.Trigger;
import org.eclipse.kapua.service.scheduler.trigger.TriggerCreator;
import org.eclipse.kapua.service.scheduler.trigger.TriggerFactory;
import org.eclipse.kapua.service.scheduler.trigger.TriggerListResult;
import org.eclipse.kapua.service.scheduler.trigger.TriggerQuery;
import org.eclipse.kapua.service.scheduler.trigger.definition.TriggerProperty;
import org.eclipse.kapua.service.scheduler.trigger.definition.quartz.TriggerPropertyImpl;

import javax.inject.Singleton;

/**
 * {@link TriggerFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class TriggerFactoryImpl implements TriggerFactory {

    @Override
    public TriggerCreator newCreator(KapuaId scopeId) {
        return new TriggerCreatorImpl(scopeId, null);
    }

    @Override
    public Trigger newEntity(KapuaId scopeId) {
        return new TriggerImpl(scopeId);
    }

    @Override
    public TriggerQuery newQuery(KapuaId scopeId) {
        return new TriggerQueryImpl(scopeId);
    }

    @Override
    public TriggerListResult newListResult() {
        return new TriggerListResultImpl();
    }

    @Override
    public TriggerProperty newTriggerProperty(String name, String type, String value) {
        return new TriggerPropertyImpl(name, type, value);
    }

    @Override
    public Trigger clone(Trigger trigger) {
        try {
            return new TriggerImpl(trigger);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, Trigger.TYPE, trigger);
        }
    }
}
