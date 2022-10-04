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
package org.eclipse.kapua.service.authorization.access.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.access.AccessInfo;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoListResult;

/**
 * {@link AccessInfo} DAO
 *
 * @since 1.0.0
 */
public class AccessInfoDAO extends ServiceDAO {

    /**
     * Creates and return new {@link AccessInfo}
     *
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static AccessInfo create(EntityManager em, AccessInfoCreator creator)
            throws KapuaException {
        AccessInfo accessInfo = new AccessInfoImpl(creator.getScopeId());
        accessInfo.setUserId(creator.getUserId());
        return ServiceDAO.create(em, accessInfo);
    }

    /**
     * Find the {@link AccessInfo} by user {@link AccessInfo} identifier
     *
     * @param em
     * @param scopeId
     * @param accessInfoId
     * @return
     * @since 1.0.0
     */
    public static AccessInfo find(EntityManager em, KapuaId scopeId, KapuaId accessInfoId) {
        return ServiceDAO.find(em, AccessInfoImpl.class, scopeId, accessInfoId);
    }

    /**
     * Return the {@link AccessInfo} list matching the provided query
     *
     * @param em
     * @param accessInfoQuery
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static AccessInfoListResult query(EntityManager em, KapuaQuery accessInfoQuery)
            throws KapuaException {
        return ServiceDAO.query(em, AccessInfo.class, AccessInfoImpl.class, new AccessInfoListResultImpl(), accessInfoQuery);
    }

    /**
     * Return the {@link AccessInfo} count matching the provided query
     *
     * @param em
     * @param accessInfoQuery
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    public static long count(EntityManager em, KapuaQuery accessInfoQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessInfo.class, AccessInfoImpl.class, accessInfoQuery);
    }

    /**
     * Delete the {@link AccessInfo} by {@link AccessInfo} identifier
     *
     * @param em
     * @param scopeId
     * @param accessInfoId
     * @return the deleted {@link AccessInfo}
     * @throws KapuaEntityNotFoundException If {@link AccessInfo} is nott found.
     * @since 1.0.0
     */
    public static AccessInfo delete(EntityManager em, KapuaId scopeId, KapuaId accessInfoId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, AccessInfoImpl.class, scopeId, accessInfoId);
    }

}
