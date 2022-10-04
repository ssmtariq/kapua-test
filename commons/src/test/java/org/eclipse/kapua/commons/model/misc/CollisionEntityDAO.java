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
 *      Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.commons.model.misc;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.jpa.EntityManager;
import org.eclipse.kapua.commons.service.internal.ServiceDAO;
import org.eclipse.kapua.model.id.KapuaId;

/**
 * {@link CollisionEntity} {@link ServiceDAO}
 */
public class CollisionEntityDAO {

    private CollisionEntityDAO() {
    }

    public static CollisionEntity create(EntityManager em, CollisionEntityCreator collisionEntityCreator)
            throws KapuaException {
        //
        // Create User
        CollisionEntity collisionEntity = new CollisionEntity(collisionEntityCreator.getTestField());

        return ServiceDAO.create(em, collisionEntity);
    }

    public static CollisionEntity find(EntityManager em, KapuaId scopeId, KapuaId userId) {
        return ServiceDAO.find(em, CollisionEntity.class, scopeId, userId);
    }

}
