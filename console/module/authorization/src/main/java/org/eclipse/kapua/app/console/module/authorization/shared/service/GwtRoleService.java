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
package org.eclipse.kapua.app.console.module.authorization.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import java.util.List;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtGroupedNVPair;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtPermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermission;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRolePermissionCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRoleQuery;

@RemoteServiceRelativePath("role")
public interface GwtRoleService extends RemoteService {

    public GwtRole create(GwtXSRFToken gwtXsrfToken, GwtRoleCreator gwtRoleCreator)
            throws GwtKapuaException;

    public GwtRole update(GwtXSRFToken gwtXsrfToken, GwtRole gwtRole)
            throws GwtKapuaException;

    public GwtRole find(String scopeShortId, String roleShortId)
            throws GwtKapuaException;

    /**
     * Returns the list of all Roles which belong to an account.
     *
     * @param scopeIdStirng
     * @return
     * @throws GwtKapuaException
     *
     */
    public List<GwtRole> findAll(String scopeIdStirng)
            throws GwtKapuaException;

    public PagingLoadResult<GwtRole> query(PagingLoadConfig loadConfig, GwtRoleQuery gwtRoleQuery)
            throws GwtKapuaException;

    public ListLoadResult<GwtGroupedNVPair> getRoleDescription(String scopeShortId, String roleShortId)
            throws GwtKapuaException;

    public PagingLoadResult<GwtRolePermission> getRolePermissions(PagingLoadConfig loadConfig, String scopeShortId, String roleShortId)
            throws GwtKapuaException;

    public void delete(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId)
            throws GwtKapuaException;

    public GwtRolePermission addRolePermission(GwtXSRFToken gwtXsrfToken, GwtRolePermissionCreator gwtRolePermissionCreator, GwtPermission gwtPermission)
            throws GwtKapuaException;

    public void deleteRolePermission(GwtXSRFToken gwtXsrfToken, String scopeShortId, String roleShortId)
            throws GwtKapuaException;
}
