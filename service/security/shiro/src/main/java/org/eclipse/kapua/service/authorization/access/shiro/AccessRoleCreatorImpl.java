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

import org.eclipse.kapua.commons.model.AbstractKapuaEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authorization.access.AccessRole;
import org.eclipse.kapua.service.authorization.access.AccessRoleCreator;

/**
 * {@link AccessRole} creator implementation.
 *
 * @since 1.0.0
 *
 */
public class AccessRoleCreatorImpl extends AbstractKapuaEntityCreator<AccessRole> implements AccessRoleCreator {

    private static final long serialVersionUID = 972154225756734130L;

    private KapuaId accessInfo;
    private KapuaId roleId;

    /**
     * Constructor
     *
     * @param scopeId
     */
    public AccessRoleCreatorImpl(KapuaId scopeId) {
        super(scopeId);
    }

    @Override
    public KapuaId getAccessInfoId() {
        return accessInfo;
    }

    @Override
    public void setAccessInfoId(KapuaId accessInfo) {
        this.accessInfo = accessInfo;
    }

    @Override
    public KapuaId getRoleId() {
        return roleId;
    }

    @Override
    public void setRoleId(KapuaId roleId) {
        this.roleId = roleId;
    }

}
