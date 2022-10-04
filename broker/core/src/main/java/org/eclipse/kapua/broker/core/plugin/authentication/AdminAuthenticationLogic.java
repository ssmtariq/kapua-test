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
package org.eclipse.kapua.broker.core.plugin.authentication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.Acl;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;

/**
 * Admin profile authentication logic implementation
 *
 * @since 1.0
 */
public class AdminAuthenticationLogic extends AuthenticationLogic {

    /**
     * Default constuctor
     *
     * @param options
     */
    public AdminAuthenticationLogic(Map<String, Object> options) {
        super((String) options.get(Authenticator.ADDRESS_PREFIX_KEY), (String) options.get(Authenticator.ADDRESS_CLASSIFIER_KEY), (String) options.get(Authenticator.ADDRESS_ADVISORY_PREFIX_KEY));
    }

    @Override
    public List<AuthorizationEntry> connect(KapuaSecurityContext kapuaSecurityContext) throws KapuaException {
        kapuaSecurityContext.setAdmin(true);
        return buildAuthorizationMap(kapuaSecurityContext);
    }

    @Override
    public boolean disconnect(KapuaSecurityContext kapuaSecurityContext, Throwable error) {
        boolean stealingLinkDetected = isIllegalState(kapuaSecurityContext, error);
        logger.debug("Old connection id: {} - new connection id: {} - error: {} - error cause: {}", kapuaSecurityContext.getOldConnectionId(), kapuaSecurityContext.getConnectionId(), error, (error!=null ? error.getCause() : "null"), error);
        if (stealingLinkDetected) {
            loginMetric.getAdminStealingLinkDisconnect().inc();
        }
        return !stealingLinkDetected && !kapuaSecurityContext.isMissing();
    }

    protected List<AuthorizationEntry> buildAuthorizationMap(KapuaSecurityContext kapuaSecurityContext) {
        ArrayList<AuthorizationEntry> ael = new ArrayList<AuthorizationEntry>();
        ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.ALL, aclHash));
        ael.add(createAuthorizationEntry(kapuaSecurityContext, Acl.WRITE_ADMIN, aclAdvisory));
        kapuaSecurityContext.logAuthDestinationToLog();
        return ael;
    }

}
