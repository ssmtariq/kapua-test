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
package org.eclipse.kapua.service.authentication.shiro.exceptions;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.time.format.DateTimeFormatter;
import java.util.Date;

@Category(JUnitTests.class)
public class TemporaryLockedAccountExceptionTest extends Assert {

    @Test
    public void temporaryLockedAccountExceptionTest() {
        Date[] dates = {new Date(), new Date(1L), new Date(9999999999999L)};
        String[] expectedMessages = {"This credential has been locked out until " + DateTimeFormatter.ISO_INSTANT.format(dates[0].toInstant()), "This credential has been locked out until 1970-01-01T00:00:00.001Z", "This credential has been locked out until 2286-11-20T17:46:39.999Z"};

        for (int i = 0; i < dates.length; i++) {
            TemporaryLockedAccountException temporaryLockedAccountException = new TemporaryLockedAccountException(dates[i]);
            assertEquals("Expected and actual values should be the same.", expectedMessages[i], temporaryLockedAccountException.getMessage());
        }
    }

    @Test
    public void temporaryLockedAccountExceptionNullTest() {
        TemporaryLockedAccountException temporaryLockedAccountException = new TemporaryLockedAccountException(null);
        assertEquals("Expected and actual values should be the same.", "This credential has been locked out until <null>", temporaryLockedAccountException.getMessage());
    }
}