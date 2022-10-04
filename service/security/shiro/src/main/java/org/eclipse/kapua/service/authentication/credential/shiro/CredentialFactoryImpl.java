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
package org.eclipse.kapua.service.authentication.credential.shiro;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;

import javax.inject.Singleton;
import java.util.Date;

/**
 * {@link CredentialFactory} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class CredentialFactoryImpl implements CredentialFactory {

    @Override
    public CredentialCreatorImpl newCreator(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        return new CredentialCreatorImpl(scopeId, userId, credentialType, credentialKey, credentialStatus, expirationDate);
    }

    @Override
    public CredentialListResult newListResult() {
        return new CredentialListResultImpl();
    }

    @Override
    public Credential newEntity(KapuaId scopeId) {
        return new CredentialImpl(scopeId);
    }

    @Override
    public Credential newCredential(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate) {
        return new CredentialImpl(scopeId, userId, credentialType, credentialKey, credentialStatus, expirationDate);
    }

    @Override
    public CredentialQuery newQuery(KapuaId scopeId) {
        return new CredentialQueryImpl(scopeId);
    }

    @Override
    public CredentialCreator newCreator(KapuaId scopeId) {
        return new CredentialCreatorImpl(scopeId);
    }

    @Override
    public Credential clone(Credential credential) {
        try {
            return new CredentialImpl(credential);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, Credential.TYPE, credential);
        }
    }
}
