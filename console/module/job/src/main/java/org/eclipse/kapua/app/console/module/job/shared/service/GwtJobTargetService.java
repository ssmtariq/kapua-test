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
package org.eclipse.kapua.app.console.module.job.shared.service;

import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTarget;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTargetCreator;
import org.eclipse.kapua.app.console.module.job.shared.model.GwtJobTargetQuery;

import java.util.List;

@RemoteServiceRelativePath("jobTarget")
public interface GwtJobTargetService extends RemoteService {

    PagingLoadResult<GwtJobTarget> query(PagingLoadConfig loadConfig, GwtJobTargetQuery gwtJobQuery)
            throws GwtKapuaException;

    PagingLoadResult<GwtJobTarget> findByJobId(PagingLoadConfig loadConfig, String scopeId, String jobId)
            throws GwtKapuaException;

    List<GwtJobTarget> findByJobId(String scopeId, String jobId, boolean fetchDetails)
            throws GwtKapuaException;

    /**
     * Creates a new job target under the account specified in the JobTargetCreator.
     *
     * @param scopeId
     * @param jobId
     * @param gwtJobTargetCreatorList
     * @return
     * @throws GwtKapuaException
     */
    List<GwtJobTarget> create(GwtXSRFToken xsrfToken, String scopeId, String jobId, List<GwtJobTargetCreator> gwtJobTargetCreatorList)
            throws GwtKapuaException;

    /**
     * Delete the supplied job target.
     *
     * @param gwtJobTargetId
     * @param accountId
     * @throws GwtKapuaException
     */
    void delete(GwtXSRFToken xsrfToken, String accountId, String gwtJobTargetId)
            throws GwtKapuaException;
}
