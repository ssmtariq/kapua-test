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
package org.eclipse.kapua.service.authentication.credential;

import org.eclipse.kapua.model.KapuaEntityFactory;
import org.eclipse.kapua.model.id.KapuaId;

import java.util.Date;

/**
 * {@link CredentialFactory} definition.
 *
 * @see org.eclipse.kapua.model.KapuaEntityFactory
 * @since 1.0.0
 */
public interface CredentialFactory extends KapuaEntityFactory<Credential, CredentialCreator, CredentialQuery, CredentialListResult> {

    /**
     * Instantiates a new {@link Credential}.
     *
     * @param scopeId          The scope {@link KapuaId} to set into the {@link Credential}.
     * @param userId           The {@link org.eclipse.kapua.service.user.User} {@link KapuaId} to set into the {@link Credential}.
     * @param credentialType   The {@link CredentialType} to set into the {@link Credential}.
     * @param credentialKey    The key to set into the {@link Credential}.
     * @param credentialStatus The {@link CredentialStatus} to set into the {@link Credential}.
     * @param expirationDate   The expiration date to set into the {@link Credential}.
     * @return The newly instantiated {@link Credential}
     * @since 1.0.0
     */
    Credential newCredential(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate);

    /**
     * Instantiates a new {@link CredentialCreator}.
     *
     * @param scopeId          The scope {@link KapuaId} to set into the {@link CredentialCreator}.
     * @param userId           The {@link org.eclipse.kapua.service.user.User} {@link KapuaId} to set into the {@link CredentialCreator}.
     * @param credentialType   The {@link CredentialType} to set into the {@link CredentialCreator}.
     * @param credentialKey    The key to set into the {@link CredentialCreator}.
     * @param credentialStatus The {@link CredentialStatus} to set into the {@link CredentialCreator}.
     * @param expirationDate   The expiration date to set into the {@link CredentialCreator}.
     * @return The newly instantiated {@link CredentialCreator}
     * @since 1.0.0
     */
    CredentialCreator newCreator(KapuaId scopeId, KapuaId userId, CredentialType credentialType, String credentialKey, CredentialStatus credentialStatus, Date expirationDate);

}
