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
package org.eclipse.kapua.service.authentication.token.shiro;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.eclipse.kapua.service.authentication.token.AccessToken;
import org.eclipse.kapua.service.authentication.token.AccessTokenQuery;
import org.eclipse.kapua.service.authentication.token.LoginInfo;
import org.eclipse.kapua.service.authentication.token.AccessTokenListResult;
import org.eclipse.kapua.service.authentication.token.AccessTokenCreator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Date;

@Category(JUnitTests.class)
public class AccessTokenFactoryImplTest extends Assert {

    AccessTokenFactoryImpl accessTokenFactoryImpl;
    KapuaId[] scopeIds;
    KapuaEid[] userIds;
    String[] tokenIds, refreshTokens;
    Date[] expiresOnDates, refreshExpiresOnDates;
    AccessToken accessToken;
    Date modifiedOn, createdOn, invalidatedOn;

    @Before
    public void initialize() {
        accessTokenFactoryImpl = new AccessTokenFactoryImpl();
        scopeIds = new KapuaId[]{null, KapuaId.ONE};
        userIds = new KapuaEid[]{null, new KapuaEid()};
        tokenIds = new String[]{null, "", "!!token id-1", "#1(token.,/tokenID)9--99", "!$$ 1-2 id//", "to_ken_id(....)<00>"};
        refreshTokens = new String[]{null, "", "!!token refresh token-1", "#1(REfreSF.,/token_refresh)9--99", "!$$ 1-2 id//", "to_ken_refRESH token(....)<00>"};
        expiresOnDates = new Date[]{null, new Date()};
        refreshExpiresOnDates = new Date[]{null, new Date()};
        accessToken = Mockito.mock(AccessToken.class);
        modifiedOn = new Date();
        createdOn = new Date();
        invalidatedOn = new Date();
    }

    @Test
    public void newCreatorMultipleParametersTest() {
        for (KapuaId scopeId : scopeIds) {
            for (KapuaEid userId : userIds) {
                for (String tokenId : tokenIds) {
                    for (Date expiresOnDate : expiresOnDates) {
                        for (String refreshToken : refreshTokens) {
                            for (Date refreshExpiresOnDate : refreshExpiresOnDates) {
                                AccessTokenCreatorImpl accessTokenCreatorImpl = accessTokenFactoryImpl.newCreator(scopeId, userId, tokenId, expiresOnDate, refreshToken, refreshExpiresOnDate);
                                assertEquals("Expected and actual values should be the same.", scopeId, accessTokenCreatorImpl.getScopeId());
                                assertEquals("Expected and actual values should be the same.", userId, accessTokenCreatorImpl.getUserId());
                                assertEquals("Expected and actual values should be the same.", tokenId, accessTokenCreatorImpl.getTokenId());
                                assertEquals("Expected and actual values should be the same.", expiresOnDate, accessTokenCreatorImpl.getExpiresOn());
                                assertEquals("Expected and actual values should be the same.", refreshToken, accessTokenCreatorImpl.getRefreshToken());
                                assertEquals("Expected and actual values should be the same.", refreshExpiresOnDate, accessTokenCreatorImpl.getRefreshExpiresOn());
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void newEntityTest() {
        for (KapuaId scopeId : scopeIds) {
            AccessToken accessToken = accessTokenFactoryImpl.newEntity(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, accessToken.getScopeId());
        }
    }

    @Test
    public void newCreatorScopeIdParameterTest() {
        for (KapuaId scopeId : scopeIds) {
            AccessTokenCreator accessTokenCreator = accessTokenFactoryImpl.newCreator(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, accessTokenCreator.getScopeId());
        }
    }

    @Test
    public void newQueryTest() {
        for (KapuaId scopeId : scopeIds) {
            AccessTokenQuery accessTokenQuery = accessTokenFactoryImpl.newQuery(scopeId);
            assertEquals("Expected and actual values should be the same.", scopeId, accessTokenQuery.getScopeId());
        }
    }

    @Test
    public void newListResultTest() {
        assertTrue("True expected.", accessTokenFactoryImpl.newListResult() instanceof AccessTokenListResult);
    }

    @Test
    public void cloneTest() {
        Mockito.when(accessToken.getId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getCreatedOn()).thenReturn(createdOn);
        Mockito.when(accessToken.getCreatedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getScopeId()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getUserId()).thenReturn(KapuaId.ANY);
        Mockito.when(accessToken.getTokenId()).thenReturn("token id");
        Mockito.when(accessToken.getExpiresOn()).thenReturn(expiresOnDates[1]);
        Mockito.when(accessToken.getRefreshToken()).thenReturn("refresh token");
        Mockito.when(accessToken.getRefreshExpiresOn()).thenReturn(refreshExpiresOnDates[1]);
        Mockito.when(accessToken.getInvalidatedOn()).thenReturn(invalidatedOn);
        Mockito.when(accessToken.getModifiedOn()).thenReturn(modifiedOn);
        Mockito.when(accessToken.getModifiedBy()).thenReturn(KapuaId.ONE);
        Mockito.when(accessToken.getOptlock()).thenReturn(10);

        AccessToken accessTokenResult = accessTokenFactoryImpl.clone(accessToken);
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenResult.getId());
        assertEquals("Expected and actual values should be the same.", createdOn, accessTokenResult.getCreatedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenResult.getCreatedBy());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenResult.getScopeId());
        assertEquals("Expected and actual values should be the same.", KapuaId.ANY, accessTokenResult.getUserId());
        assertEquals("Expected and actual values should be the same.", "token id", accessTokenResult.getTokenId());
        assertEquals("Expected and actual values should be the same.", expiresOnDates[1], accessTokenResult.getExpiresOn());
        assertEquals("Expected and actual values should be the same.", "refresh token", accessTokenResult.getRefreshToken());
        assertEquals("Expected and actual values should be the same.", refreshExpiresOnDates[1], accessTokenResult.getRefreshExpiresOn());
        assertEquals("Expected and actual values should be the same.", invalidatedOn, accessTokenResult.getInvalidatedOn());
        assertEquals("Expected and actual values should be the same.", modifiedOn, accessTokenResult.getModifiedOn());
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenResult.getModifiedBy());
        assertEquals("Expected and actual values should be the same.", 10, accessTokenResult.getOptlock());
    }

    @Test(expected = KapuaEntityCloneException.class)
    public void cloneNullTest() {
        accessTokenFactoryImpl.clone(null);
    }

    @Test
    public void newLoginInfoTest() {
        assertTrue("True expected.", accessTokenFactoryImpl.newLoginInfo() instanceof LoginInfo);
    }
}