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
package org.eclipse.kapua.app.console.module.authentication.shared.service;

import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialCreator;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredentialQuery;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("credential")
public interface GwtCredentialService extends RemoteService {

    /**
     * Returns the list of all Credentials matching the query.
     *
     * @param gwtCredentialQuery
     * @return
     * @throws GwtKapuaException
     */
    public PagingLoadResult<GwtCredential> query(PagingLoadConfig loadConfig, GwtCredentialQuery gwtCredentialQuery)
            throws GwtKapuaException;

    /**
     * Delete the supplied Credential.
     *
     * @param gwtCredentialId
     * @throws GwtKapuaException
     */
    public void delete(GwtXSRFToken xsfrToken, String stringScopeId, String gwtCredentialId)
            throws GwtKapuaException;

    public GwtCredential create(GwtXSRFToken gwtXsrfToken, GwtCredentialCreator gwtRoleCreator)
            throws GwtKapuaException;

    public GwtCredential update(GwtXSRFToken gwtXsrfToken, GwtCredential gwtCredential)
            throws GwtKapuaException;

    public void changePassword(GwtXSRFToken gwtXsrfToken, String oldPassword, String newPassword, String mfaCode, String stringUserId, String stringScopeId)
            throws GwtKapuaException;

    public void unlock(GwtXSRFToken xsfrToken, String stringScopeId, String gwtCredentialId)
            throws GwtKapuaException;

    Integer getMinPasswordLength(String scopeId)
            throws GwtKapuaException;

}
