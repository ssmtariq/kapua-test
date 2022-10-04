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
package org.eclipse.kapua;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(JUnitTests.class)
public class KapuaExceptionTest extends Assert {

    String expectedErrorMessage;
    Object argument1, argument2, argument3;
    Throwable[] throwables;
    KapuaErrorCode kapuaErrorCode;

    @Before
    public void initialize() {
        expectedErrorMessage = "kapua-service-error-messages";
        argument1 = "user";
        argument2 = 2;
        argument3 = 'c';
        throwables = new Throwable[]{new Throwable(), null};
        kapuaErrorCode = KapuaErrorCodes.ENTITY_NOT_FOUND;
    }

    @Test
    public void kapuaExceptionKapuaErrorCodeParameterTest() {
        KapuaException kapuaException = new KapuaException(kapuaErrorCode);

        assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaException.getCode());
        assertEquals("Expected and actual values should be the same.", "The entity of type {0} with id/name {1} was not found.", kapuaException.getMessage());
        assertEquals("Expected and actual values should be the same.", "The entity of type {0} with id/name {1} was not found.", kapuaException.getLocalizedMessage());
        assertNull("Null expected.", kapuaException.getCause());
        assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
    }

    @Test
    public void kapuaExceptionNullKapuaErrorCodeParameterTest() {
        KapuaException kapuaException = new KapuaException(null);
        assertNull("Null expected.", kapuaException.getCode());
        assertNull("Null expected.", kapuaException.getCause());
        assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
        try {
            kapuaException.getMessage();
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        try {
            kapuaException.getLocalizedMessage();
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void kapuaExceptionKapuaErrorCodeObjectParametersTest() {
        KapuaException kapuaException = new KapuaException(kapuaErrorCode, argument1, argument2, argument3);
        assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaException.getCode());
        assertEquals("Expected and actual values should be the same.", "The entity of type " + argument1 + " with id/name " + argument2 + " was not found.", kapuaException.getMessage());
        assertEquals("Expected and actual values should be the same.", "The entity of type " + argument1 + " with id/name " + argument2 + " was not found.", kapuaException.getLocalizedMessage());
        assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
        assertNull("Null expected.", kapuaException.getCause());
    }

    @Test
    public void kapuaExceptionNullKapuaErrorCodeObjectParametersTest() {
        KapuaException kapuaException = new KapuaException(null, argument1, argument2, argument3);
        assertNull("Null expected.", kapuaException.getCode());
        assertNull("Null expected.", kapuaException.getCause());
        assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
        try {
            kapuaException.getMessage();
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        try {
            kapuaException.getLocalizedMessage();
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void kapuaExceptionKapuaErrorCodeNullObjectParametersTest() {
        KapuaException kapuaException = new KapuaException(kapuaErrorCode, null);
        assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaException.getCode());
        assertEquals("Expected and actual values should be the same.", "The entity of type {0} with id/name {1} was not found.", kapuaException.getMessage());
        assertEquals("Expected and actual values should be the same.", "The entity of type {0} with id/name {1} was not found.", kapuaException.getLocalizedMessage());
        assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
        assertNull("Null expected.", kapuaException.getCause());
    }

    @Test
    public void kapuaExceptionNullKapuaErrorCodeNullObjectParametersTest() {
        KapuaException kapuaException = new KapuaException(null, null);
        assertNull("Null expected.", kapuaException.getCode());
        assertNull("Null expected.", kapuaException.getCause());
        assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
        try {
            kapuaException.getMessage();
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
        try {
            kapuaException.getLocalizedMessage();
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void kapuaExceptionKapuaErrorCodeThrowableObjectParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaException kapuaException = new KapuaException(kapuaErrorCode, throwable, argument1, argument2, argument3);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaException.getCode());
            assertEquals("Expected and actual values should be the same.", "The entity of type " + argument1 + " with id/name " + argument2 + " was not found.", kapuaException.getMessage());
            assertEquals("Expected and actual values should be the same.", "The entity of type " + argument1 + " with id/name " + argument2 + " was not found.", kapuaException.getLocalizedMessage());
            assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
            assertEquals("Expected and actual values should be the same.", throwable, kapuaException.getCause());
        }
    }

    @Test
    public void kapuaExceptionNullKapuaErrorCodeThrowableObjectParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaException kapuaException = new KapuaException(null, throwable, argument1, argument2, argument3);
            assertNull("Null expected.", kapuaException.getCode());
            assertEquals("Expected and actual values should be the same.", throwable, kapuaException.getCause());
            assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
            try {
                kapuaException.getMessage();
                fail("NullPointerException expected.");
            } catch (Exception e) {
                assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
            }
            try {
                kapuaException.getLocalizedMessage();
                fail("NullPointerException expected.");
            } catch (Exception e) {
                assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
            }
        }
    }

    @Test
    public void kapuaExceptionKapuaErrorCodeThrowableNullObjectParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaException kapuaException = new KapuaException(kapuaErrorCode, throwable, null);
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.ENTITY_NOT_FOUND, kapuaException.getCode());
            assertEquals("Expected and actual values should be the same.", "The entity of type {0} with id/name {1} was not found.", kapuaException.getMessage());
            assertEquals("Expected and actual values should be the same.", "The entity of type {0} with id/name {1} was not found.", kapuaException.getLocalizedMessage());
            assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
            assertEquals("Expected and actual values should be the same.", throwable, kapuaException.getCause());
        }
    }

    @Test
    public void kapuaExceptionNullKapuaErrorCodeThrowableNullObjectParametersTest() {
        for (Throwable throwable : throwables) {
            KapuaException kapuaException = new KapuaException(null, throwable, null);
            assertNull("Null expected.", kapuaException.getCode());
            assertEquals("Expected and actual values should be the same.", expectedErrorMessage, kapuaException.getKapuaErrorMessagesBundle());
            assertEquals("Expected and actual values should be the same.", throwable, kapuaException.getCause());
            try {
                kapuaException.getMessage();
                fail("NullPointerException expected.");
            } catch (Exception e) {
                assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
            }
            try {
                kapuaException.getLocalizedMessage();
                fail("NullPointerException expected.");
            } catch (Exception e) {
                assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
            }
        }
    }

    @Test
    public void internalErrorCauseMessageTest() {
        Throwable[] cause = {new Throwable(), null};
        String[] messages = {"Message", null};

        for (Throwable throwable : cause) {
            for (String msg : messages) {
                assertThat("Instance of KapuaException expected.", KapuaException.internalError(throwable, msg), IsInstanceOf.instanceOf(KapuaException.class));
                assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, throwable, msg).toString(), KapuaException.internalError(throwable, msg).toString());
                assertEquals("Expected and actual values should be the same.", "An internal error occurred: " + msg + ".", KapuaException.internalError(throwable, msg).getMessage());
                assertEquals("Expected and actual values should be the same.", "An internal error occurred: " + msg + ".", KapuaException.internalError(throwable, msg).getLocalizedMessage());
                assertEquals("Expected and actual values should be the same.", throwable, KapuaException.internalError(throwable, msg).getCause());
                assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.INTERNAL_ERROR, KapuaException.internalError(throwable, msg).getCode());
                assertEquals("Expected and actual values should be the same.", expectedErrorMessage, KapuaException.internalError(throwable, msg).getKapuaErrorMessagesBundle());
            }
        }
    }

    @Test
    public void internalErrorCauseTest() {
        String message = "Message";
        Throwable nullThrowable = null;
        Throwable[] throwables = {new Throwable(message), new Throwable()};
        String[] expectedMessage = {"An internal error occurred: " + message + ".", "An internal error occurred: " + throwables[1] + "."};
        String[] arguments = {"Message", "java.lang.Throwable"};

        for (int i = 0; i < throwables.length; i++) {
            assertThat("Instance of KapuaException expected.", KapuaException.internalError(throwables[i]), IsInstanceOf.instanceOf(KapuaException.class));
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, throwables[i], arguments[i]).toString(), KapuaException.internalError(throwables[i]).toString());
            assertEquals("Expected and actual values should be the same.", expectedMessage[i], KapuaException.internalError(throwables[i]).getMessage());
            assertEquals("Expected and actual values should be the same.", throwables[i], KapuaException.internalError(throwables[i]).getCause());
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.INTERNAL_ERROR, KapuaException.internalError(throwables[i]).getCode());
            assertEquals("Expected and actual values should be the same.", expectedErrorMessage, KapuaException.internalError(throwables[i]).getKapuaErrorMessagesBundle());
        }
        try {
            KapuaException.internalError(nullThrowable);
            fail("NullPointerException expected.");
        } catch (Exception e) {
            assertEquals("NullPointerException expected.", new NullPointerException().toString(), e.toString());
        }
    }

    @Test
    public void internalErrorMessageTest() {
        String[] messages = {"Message", null};

        for (String msg : messages) {
            assertThat("Instance of KapuaRuntimeException expected.", KapuaException.internalError(msg), IsInstanceOf.instanceOf(KapuaException.class));
            assertEquals("Expected and actual values should be the same.", new KapuaException(KapuaErrorCodes.INTERNAL_ERROR, null, msg).toString(), KapuaException.internalError(msg).toString());
            assertEquals("Expected and actual values should be the same.", "An internal error occurred: " + msg + ".", KapuaException.internalError(msg).getMessage());
            assertNull("Null expected.", KapuaException.internalError(msg).getCause());
            assertEquals("Expected and actual values should be the same.", KapuaErrorCodes.INTERNAL_ERROR, KapuaException.internalError(msg).getCode());
            assertEquals("Expected and actual values should be the same.", expectedErrorMessage, KapuaException.internalError(msg).getKapuaErrorMessagesBundle());
        }
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionKapuaErrorCodeParameterTest() throws KapuaException {
        throw new KapuaException(kapuaErrorCode);
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionNullKapuaErrorCodeParameterTest() throws KapuaException {
        throw new KapuaException(null);
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionKapuaErrorCodeObjectParameterTest() throws KapuaException {
        throw new KapuaException(kapuaErrorCode, argument1, argument2, argument3);
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionNullKapuaErrorCodeObjectParameterTest() throws KapuaException {
        throw new KapuaException(null, argument1, argument2, argument3);
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionKapuaErrorCodeNullObjectParameterTest() throws KapuaException {
        throw new KapuaException(kapuaErrorCode, null);
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionNullKapuaErrorCodeNullObjectParameterTest() throws KapuaException {
        throw new KapuaException(null, null);
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionKapuaErrorCodeObjectThrowableParameterTest() throws KapuaException {
        for (Throwable throwable : throwables) {
            throw new KapuaException(kapuaErrorCode, throwable, argument1, argument2, argument3);
        }
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionNullKapuaErrorCodeObjectThrowableParameterTest() throws KapuaException {
        for (Throwable throwable : throwables) {
            throw new KapuaException(null, throwable, argument1, argument2, argument3);
        }
    }

    @Test(expected = KapuaException.class)
    public void throwingExceptionKapuaErrorCodeNullObjectThrowableParameterTest() throws KapuaException {
        for (Throwable throwable : throwables) {
            throw new KapuaException(kapuaErrorCode, throwable, null);
        }
    }
}
