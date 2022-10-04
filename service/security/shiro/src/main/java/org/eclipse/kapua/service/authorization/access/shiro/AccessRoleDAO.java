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
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;
import org.eclipse.kapua.service.authorization.access.AccessRoleListResult;

/**
 * {@link AccessRole} {@link ServiceDAO}
 *
 * @since 1.0
 */
public class AccessRoleDAO extends ServiceDAO {

    /**
     * Creates and return new access permission
     *
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static AccessRole create(EntityManager em, AccessRoleCreator creator)
            throws KapuaException {
        AccessRole accessRole = new AccessRoleImpl(creator.getScopeId());

        accessRole.setAccessInfoId(creator.getAccessInfoId());
        accessRole.setRoleId(creator.getRoleId());

        return ServiceDAO.create(em, accessRole);
    }

    /**
     * Find the access info by access permission identifier
     *
     * @param em
     * @param scopeId
     * @param accessRoleId
     * @return
     */
    public static AccessRole find(EntityManager em, KapuaId scopeId, KapuaId accessRoleId) {
        return ServiceDAO.find(em , AccessRoleImpl.class, scopeId, accessRoleId);
    }

    /**
     * Return the {@link AccessRole}s list matching the provided query
     *
     * @param em
     * @param accessInfoPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static AccessRoleListResult query(EntityManager em, KapuaQuery accessInfoPermissionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, AccessRole.class, AccessRoleImpl.class, new AccessRoleListResultImpl(), accessInfoPermissionQuery);
    }

    /**
     * Return the count of {@link AccessRole} matching the provided query
     *
     * @param em
     * @param accessPermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery accessPermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, AccessRole.class, AccessRoleImpl.class, accessPermissionQuery);
    }

    /**
     * Delete the access permission by access permission id.
     *
     * @param em
     * @param scopeId
     * @param accessRoleId
     * @return the deleted {@link AccessRole}
     * @throws KapuaEntityNotFoundException
     *             If {@link AccessRole} is not found.
     */
    public static AccessRole delete(EntityManager em, KapuaId scopeId, KapuaId accessRoleId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, AccessRoleImpl.class, scopeId, accessRoleId);
    }
}
