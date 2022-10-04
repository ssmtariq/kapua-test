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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

@Category(JUnitTests.class)
public class CredentialCreatorImplTest extends Assert {

    CredentialCreatorImpl credentialCreatorImpl1, credentialCreatorImpl2;

    @Before
    public void initialize() {
        credentialCreatorImpl1 = new CredentialCreatorImpl(KapuaId.ONE, KapuaId.ONE, CredentialType.API_KEY, "credential key", CredentialStatus.ENABLED, new Date());
        credentialCreatorImpl2 = new CredentialCreatorImpl(KapuaId.ONE);
    }

    @Test
    public void credentialCreatorImplMultipleParametersTest() {
        KapuaId[] scopeIds = {null, KapuaId.ONE};
        KapuaId[] userIds = {null, KapuaId.ONE};
        String[] credentialKeys = {null, "", "!!credentialKey-1", "#1(credentialKey.,/Key)9--99", "!$$ 1-2 key//", "credential_K_ey(....)<00>"};
        CredentialType[] credentialTypes = {null, CredentialType.PASSWORD, CredentialType.API_KEY, CredentialType.JWT};
        CredentialStatus[] credentialStatuses = {null, CredentialStatus.ENABLED, CredentialStatus.DISABLED};
        Date[] dates = {null, new Date()};

        for (KapuaId scopeId : scopeIds) {
            for (KapuaId userId : userIds) {
                for (CredentialType credentialType : credentialTypes) {
                    for (String credentialKey : credentialKeys) {
                        for (CredentialStatus credentialStatus : credentialStatuses) {
                            for (Date date : dates) {
                                CredentialCreatorImpl credentialCreatorImpl = new CredentialCreatorImpl(scopeId, userId, credentialType, credentialKey, credentialStatus, date);
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
    public void credentialCreatorImplScopeIdParameterTest() {
        KapuaId[] scopeIds = {null, KapuaId.ONE};
        for (KapuaId scopeId : scopeIds) {
            CredentialCreatorImpl credentialCreatorImpl = new CredentialCreatorImpl(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, credentialCreatorImpl.getScopeId());
            assertNull("Null expected.", credentialCreatorImpl.getUserId());
            assertNull("Null expected.", credentialCreatorImpl.getCredentialType());
            assertNull("Null expected.", credentialCreatorImpl.getCredentialPlainKey());
            assertNull("Null expected.", credentialCreatorImpl.getCredentialStatus());
            assertNull("Null expected.", credentialCreatorImpl.getExpirationDate());
        }
    }

    @Test
    public void setAndGetUserIdTest() {
        KapuaId[] newUserIds = {null, KapuaId.ONE};
        for (KapuaId newUserId : newUserIds) {
            credentialCreatorImpl1.setUserId(newUserId);
            credentialCreatorImpl2.setUserId(newUserId);
            assertEquals("Expected and actual values should be the same.", newUserId, credentialCreatorImpl1.getUserId());
            assertEquals("Expected and actual values should be the same.", newUserId, credentialCreatorImpl2.getUserId());
        }
    }

    @Test
    public void setAndGetCredentialTypeTest() {
        CredentialType[] newCredentialTypes = {null, CredentialType.PASSWORD, CredentialType.API_KEY, CredentialType.JWT};
        for (CredentialType newCredentialType : newCredentialTypes) {
            credentialCreatorImpl1.setCredentialType(newCredentialType);
            credentialCreatorImpl2.setCredentialType(newCredentialType);
            assertEquals("Expected and actual values should be the same.", newCredentialType, credentialCreatorImpl1.getCredentialType());
            assertEquals("Expected and actual values should be the same.", newCredentialType, credentialCreatorImpl2.getCredentialType());
        }
    }

    @Test
    public void setAndGetCredentialPlainKeyTest() {
        String[] newCredentialKeys = {null, "", "!!NEWcredentialKey-1", "#1(new credentialKey.,/Key)9--99", "!$$ 1-2 key//", "credential_K_ey NEW KEY(....)<00>"};
        for (String newCredentialKey : newCredentialKeys) {
            credentialCreatorImpl1.setCredentialPlainKey(newCredentialKey);
            credentialCreatorImpl2.setCredentialPlainKey(newCredentialKey);
            assertEquals("Expected and actual values should be the same.", newCredentialKey, credentialCreatorImpl1.getCredentialPlainKey());
            assertEquals("Expected and actual values should be the same.", newCredentialKey, credentialCreatorImpl2.getCredentialPlainKey());
        }
    }

    @Test
    public void setAndGetExpirationDateTest() {
        Date[] newDates = {null, new Date()};
        for (Date newDate : newDates) {
            credentialCreatorImpl1.setExpirationDate(newDate);
            credentialCreatorImpl2.setExpirationDate(newDate);
            assertEquals("Expected and actual values should be the same.", newDate, credentialCreatorImpl1.getExpirationDate());
            assertEquals("Expected and actual values should be the same.", newDate, credentialCreatorImpl2.getExpirationDate());
        }
    }

    @Test
    public void setAndGetCredentialStatusTest() {
        CredentialStatus[] newCredentialStatuses = {null, CredentialStatus.ENABLED, CredentialStatus.DISABLED};
        for (CredentialStatus newCredentialStatus : newCredentialStatuses) {
            credentialCreatorImpl1.setCredentialStatus(newCredentialStatus);
            credentialCreatorImpl2.setCredentialStatus(newCredentialStatus);
            assertEquals("Expected and actual values should be the same.", newCredentialStatus, credentialCreatorImpl1.getCredentialStatus());
            assertEquals("Expected and actual values should be the same.", newCredentialStatus, credentialCreatorImpl2.getCredentialStatus());
        }
    }
}