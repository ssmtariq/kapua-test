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

import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

@Category(JUnitTests.class)
public class AccessTokenCreatorImplTest extends Assert {

    AccessTokenCreatorImpl accessTokenCreatorImpl;

    @Before
    public void initialize() {
        accessTokenCreatorImpl = new AccessTokenCreatorImpl(KapuaId.ONE);
    }

    @Test
    public void accessTokenCreatorImplTest() {
        assertEquals("Expected and actual values should be the same.", KapuaId.ONE, accessTokenCreatorImpl.getScopeId());
    }

    @Test
    public void accessTokenCreatorImplNullTest() {
        AccessTokenCreatorImpl accessTokenCreatorImpl = new AccessTokenCreatorImpl(null);
        assertNull("Null expected.", accessTokenCreatorImpl.getScopeId());
    }

    @Test
    public void setAndGetTokenIdTest() {
        String[] tokenIds = {null, "", "!!tokenID-1", "#1(TOKEN.,/token id)9--99", "!$$ 1-2 ID//", "id_tokeN(....)<00>"};

        assertNull("Null expected.", accessTokenCreatorImpl.getTokenId());
        for (String tokenId : tokenIds) {
            accessTokenCreatorImpl.setTokenId(tokenId);
            assertEquals("Expected and actual values should be the same.", tokenId, accessTokenCreatorImpl.getTokenId());
        }
    }

    @Test
    public void setAndGetUserIdTest() {
        KapuaId[] userIds = {null, KapuaId.ONE};

        assertNull("Null expected.", accessTokenCreatorImpl.getUserId());
        for (KapuaId userId : userIds) {
            accessTokenCreatorImpl.setUserId(userId);
            assertEquals("Expected and actual values should be the same.", userId, accessTokenCreatorImpl.getUserId());
        }
    }

    @Test
    public void setAndGetExpiresOnTest() {
        Date[] expiresOnDates = {null, new Date(), new Date(1L), new Date(9999999999999L)};
        assertNull("Null expected.", accessTokenCreatorImpl.getExpiresOn());
        for (Date expiresOnDate : expiresOnDates) {
            accessTokenCreatorImpl.setExpiresOn(expiresOnDate);
            assertEquals("Expected and actual values should be the same.", expiresOnDate, accessTokenCreatorImpl.getExpiresOn());
        }
    }

    @Test
    public void setAndGetRefreshTokenTest() {
        String[] refreshTokens = {null, "", "!!refreshToken-1", "#1(TOKEN.,/refresh token id)9--99", "!$$ 1-2 REFREsh//", "refresh_tokeN(....)<00>"};

        assertNull("Null expected.", accessTokenCreatorImpl.getRefreshToken());
        for (String refreshToken : refreshTokens) {
            accessTokenCreatorImpl.setRefreshToken(refreshToken);
            assertEquals("Expected and actual values should be the same.", refreshToken, accessTokenCreatorImpl.getRefreshToken());
        }
    }

    @Test
    public void setAndGetRefreshExpiresOnTest() {
        Date[] refreshExpiresOnDates = {null, new Date(), new Date(1L), new Date(9999999999999L)};
        assertNull("Null expected.", accessTokenCreatorImpl.getRefreshExpiresOn());
        for (Date refreshExpiresOnDate : refreshExpiresOnDates) {
            accessTokenCreatorImpl.setRefreshExpiresOn(refreshExpiresOnDate);
            assertEquals("Expected and actual values should be the same.", refreshExpiresOnDate, accessTokenCreatorImpl.getRefreshExpiresOn());
        }
    }
}