/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.Credential;
import org.eclipse.kapua.service.authentication.credential.CredentialQuery;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialListResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class CredentialFactoryImplTest extends Assert {

    CredentialFactoryImpl credentialFactoryImpl;
    KapuaId[] scopeIds;
    KapuaEid[] userIds;
    String[] credentialKeys;
    CredentialType[] credentialTypes;
    CredentialStatus[] credentialStatuses;
    Date[] dates;
    Credential credential;
    Date expirationDate, modifiedOn;

    @Before
    public void initialize() {
        credentialFactoryImpl = new CredentialFactoryImpl();
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaEid[]{null, new KapuaEid()};
        credentialKeys = new String[]{null, "", "!!credentialKey-1", "#1(credentialKey.,/Key)9--99", "!$$ 1-2 key//", "credential_K_ey(....)<00>"};
        credentialTypes = new CredentialType[]{null, CredentialType.PASSWORD, CredentialType.API_KEY, CredentialType.JWT};
        credentialStatuses = new CredentialStatus[]{null, CredentialStatus.ENABLED, CredentialStatus.DISABLED};
        dates = new Date[]{null, new Date()};
        credential = Mockito.mock(Credential.class);
        expirationDate = new Date();
        modifiedOn = new Date();
    }

    @Test
    public void newCreatorScopeIdUserIdMfaSecretKeyParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaEid userId : userIds) {
                for (CredentialType credentialType : credentialTypes) {
                    for (String credentialKey : credentialKeys) {
                        for (CredentialStatus credentialStatus : credentialStatuses) {
                            for (Date date : dates) {
                                CredentialCreatorImpl credentialCreatorImpl = credentialFactoryImpl.newCreator(scopeId, userId, credentialType, credentialKey, credentialStatus, date);
                                assertEquals("Expected and actual values should be the same.", scopeId, credentialCreatorImpl.getScopeId());
                                assertEquals("Expected and actual values should be the same.", userId, credentialCreatorImpl.getUserId());
                                assertEquals("Expected and actual values should be the same.", credentialType, credentialCreatorImpl.getCredentialType());
                                assertEquals("Expected and actual values should be the same.", credentialKey, credentialCreatorImpl.getCredentialPlainKey());
                                assertEquals("Expected and actual values should be the same.", credentialStatus, credentialCreatorImpl.getCredentialStatus());
                                assertEquals("Expected and actual values should be the same.", date, credentialCreatorImpl.getExpirationDate());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void newListResultTest() {
        assertTrue("True expected.", credentialFactoryImpl.newListResult() instanceof CredentialListResult);
    }

    @Test
    public void newEntityTest() {
        for (KapuaId scopeId : scopeIds) {
            Credential credential = credentialFactoryImpl.newEntity(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, credential.getScopeId());
        }
    }

    @Test
    public void newMfaOptionTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaEid userId : userIds) {
                for (CredentialType credentialType : credentialTypes) {
                    for (String credentialKey : credentialKeys) {
                        for (CredentialStatus credentialStatus : credentialStatuses) {
                            for (Date date : dates) {
                                Credential credential = credentialFactoryImpl.newCredential(scopeId, userId, credentialType, credentialKey, credentialStatus, date);
                                assertEquals("Expected and actual values should be the same.", scopeId, credential.getScopeId());
                                assertEquals("Expected and actual values should be the same.", userId, credential.getUserId());
                                assertEquals("Expected and actual values should be the same.", credentialType, credential.getCredentialType());
                                assertEquals("Expected and actual values should be the same.", credentialKey, credential.getCredentialKey());
                                assertEquals("Expected and actual values should be the same.", credentialStatus, credential.getStatus());
                                assertEquals("Expected and actual values should be the same.", date, credential.getExpirationDate());

                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void newQueryTest() {
        for (KapuaId scopeId : scopeIds) {
            CredentialQuery credentialQuery = credentialFactoryImpl.newQuery(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, credentialQuery.getScopeId());
        }
    }

    @Test
    public void newCreatorScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            CredentialCreator credentialCreator = credentialFactoryImpl.newCreator(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, credentialCreator.getScopeId());
        }
    }

    @Test
    public void cloneTest() {
        Mockito.when(credential.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(credential.getUserId()).thenReturn(KapuaId.ONE);
        Mockito.when(credential.getCredentialType()).thenReturn(CredentialType.JWT);
        Mockito.when(credential.getCredentialKey()).thenReturn("key");
        Mockito.when(credential.getExpirationDate()).thenReturn(expirationDate);
        Mockito.when(credential.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(credential.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(credential.getOptlock()).thenReturn(10);

        Credential credentialResult = credentialFactoryImpl.clone(credential);

        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, credentialResult.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, credentialResult.getUserId());
        assertEquals("Expected and actual values should be the same.", CredentialType.JWT, credentialResult.getCredentialType());
        assertEquals("Expected and actual values should be the same.", "key", credentialResult.getCredentialKey());
        assertEquals("Expected and actual values should be the same.", expirationDate, credentialResult.getExpirationDate());
        assertEquals("Expected and actual values should be the same.", modifiedOn, credentialResult.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, credentialResult.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", 10, credentialResult.getOptlock());
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void cloneNullTest() {
        credentialFactoryImpl.clone(null);
    }
}