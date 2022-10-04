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
package org.eclipse.kapua.service.authorization.role.shiro;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.role.RolePermission;
import org.eclipse.kapua.service.authorization.role.RolePermissionCreator;
import org.eclipse.kapua.service.authorization.role.RolePermissionListResult;

/**
 * {@link RolePermission} DAO
 *
 * @since 1.0
 *
 */
public class RolePermissionDAO extends ServiceDAO {

    /**
     * Creates and return new role permission
     *
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static RolePermission create(EntityManager em, RolePermissionCreator creator)
            throws KapuaException {
        RolePermission rolePermission = new RolePermissionImpl(creator.getScopeId());

        rolePermission.setRoleId(creator.getRoleId());
        rolePermission.setPermission(creator.getPermission());

        return ServiceDAO.create(em, rolePermission);
    }

    /**
     * Find the role by role identifier
     *
     * @param em
     * @param scopeId
     * @param roleId
     * @return
     */
    public static RolePermission find(EntityManager em, KapuaId scopeId, KapuaId roleId) {
        return ServiceDAO.find(em, RolePermissionImpl.class, scopeId, roleId);
    }

    /**
     * Return the {@link RolePermission} list matching the provided query
     *
     * @param em
     * @param rolePermissionQuery
     * @return
     * @throws KapuaException
     */
    public static RolePermissionListResult query(EntityManager em, KapuaQuery rolePermissionQuery)
            throws KapuaException {
        return ServiceDAO.query(em, RolePermission.class, RolePermissionImpl.class, new RolePermissionListResultImpl(), rolePermissionQuery);
    }

    /**
     * Return the count of {@link RolePermission} matching the provided query
     *
     * @param em
     * @param rolePermissionQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery rolePermissionQuery)
            throws KapuaException {
        return ServiceDAO.count(em, RolePermission.class, RolePermissionImpl.class, rolePermissionQuery);
    }

    /**
     * Delete the role by role identifier
     *
     * @param em
     * @param scopeId
     * @param rolePermissionId
     * @return the deleted {@link RolePermission}
     * @throws KapuaEntityNotFoundException
     *             If {@link RolePermission} is not found.
     */
    public static RolePermission delete(EntityManager em, KapuaId scopeId, KapuaId rolePermissionId) throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, RolePermissionImpl.class, scopeId, rolePermissionId);
    }
}
