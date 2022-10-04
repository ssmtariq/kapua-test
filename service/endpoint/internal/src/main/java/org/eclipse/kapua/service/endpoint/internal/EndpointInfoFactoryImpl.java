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
package org.eclipse.kapua.service.endpoint.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.endpoint.EndpointInfo;
import org.eclipse.kapua.service.endpoint.EndpointInfoCreator;
import org.eclipse.kapua.service.endpoint.EndpointInfoFactory;
import org.eclipse.kapua.service.endpoint.EndpointInfoListResult;
import org.eclipse.kapua.service.endpoint.EndpointInfoQuery;
import org.eclipse.kapua.service.endpoint.EndpointUsage;

import javax.inject.Singleton;

/**
 * {@link EndpointInfoFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class EndpointInfoFactoryImpl implements EndpointInfoFactory {

    @Override
    public EndpointInfo newEntity(KapuaId scopeId) {
        return new EndpointInfoImpl(scopeId);
    }

    @Override
    public EndpointInfoListResult newListResult() {
        return new EndpointInfoListResultImpl();
    }

    @Override
    public EndpointInfoQuery newQuery(KapuaId scopeId) {
        return new EndpointInfoQueryImpl(scopeId);
    }

    @Override
    public EndpointInfoCreator newCreator(KapuaId scopeId) {
        return new EndpointInfoCreatorImpl(scopeId);
    }

    @Override
    public EndpointUsage newEndpointUsage(String name) {
        return new EndpointUsageImpl(name);
    }

    @Override
    public EndpointInfo clone(EndpointInfo endpointInfo) {
        try {
            return new EndpointInfoImpl(endpointInfo);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, EndpointInfo.TYPE, endpointInfo);
        }
    }
}
