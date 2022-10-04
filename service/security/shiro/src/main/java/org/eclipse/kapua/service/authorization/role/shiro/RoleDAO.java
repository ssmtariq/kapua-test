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
import org.eclipse.kapua.service.authorization.role.Role;
import org.eclipse.kapua.service.authorization.role.RoleCreator;
import org.eclipse.kapua.service.authorization.role.RoleListResult;

/**
 * Role DAO
 *
 * @since 1.0
 *
 */
public class RoleDAO extends ServiceDAO {

    /**
     * Creates and return new role
     *
     * @param em
     * @param creator
     * @return
     * @throws KapuaException
     */
    public static Role create(EntityManager em, RoleCreator creator)
            throws KapuaException {
        Role role = new RoleImpl(creator.getScopeId());

        role.setName(creator.getName());
        role.setDescription(creator.getDescription());

        return ServiceDAO.create(em, role);
    }

    /**
     * Updates and returns the updated {@link Role}
     *
     * @param em
     * @param role
     * @return
     * @throws KapuaEntityNotFoundException
     *             If {@link Role} is not found.
     */
    public static Role update(EntityManager em, Role role) throws KapuaEntityNotFoundException {
        RoleImpl roleImpl = (RoleImpl) role;
        return ServiceDAO.update(em, RoleImpl.class, roleImpl);
    }

    /**
     * Find the role by role identifier
     *
     * @param em
     * @param scopeId
     * @param roleId
     * @return
     */
    public static Role find(EntityManager em, KapuaId scopeId, KapuaId roleId) {
        return ServiceDAO.find(em, RoleImpl.class, scopeId, roleId);
    }

    /**
     * Return the role list matching the provided query
     *
     * @param em
     * @param roleQuery
     * @return
     * @throws KapuaException
     */
    public static RoleListResult query(EntityManager em, KapuaQuery roleQuery)
            throws KapuaException {
        return ServiceDAO.query(em, Role.class, RoleImpl.class, new RoleListResultImpl(), roleQuery);
    }

    /**
     * Return the role count matching the provided query
     *
     * @param em
     * @param roleQuery
     * @return
     * @throws KapuaException
     */
    public static long count(EntityManager em, KapuaQuery roleQuery)
            throws KapuaException {
        return ServiceDAO.count(em, Role.class, RoleImpl.class, roleQuery);
    }

    /**
     * Delete the role by role identifier
     *
     * @param em
     * @param scopeId
     * @param roleId
     * @return the deleted {@link Role}
     * @throws KapuaEntityNotFoundException
     *             If {@link Role} is not found.
     */
    public static Role delete(EntityManager em, KapuaId scopeId, KapuaId roleId)
            throws KapuaEntityNotFoundException {
        return ServiceDAO.delete(em, RoleImpl.class, scopeId, roleId);
    }
}
