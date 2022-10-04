/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.qa.common;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.util.RandomUtils;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.junit.Assert;

import io.cucumber.java.Scenario;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;

public class TestBase {

    private static final String LAST_ACCOUNT = "LastAccount";

    /**
     * Inter step data scratchpad.
     */
    protected StepData stepData;

    /**
     * Current scenario scope
     */
    protected Scenario scenario;

    /**
     * Random number generator
     */
    public Random random = RandomUtils.getInstance();

    /**
     * Commonly used constants
     */
    protected static final String KURA_DEVICES = "kuraDevices";
    protected static final KapuaId SYS_SCOPE_ID = KapuaId.ONE;
    protected static final KapuaId SYS_USER_ID = new KapuaEid(BigInteger.ONE);
    protected static final int DEFAULT_SCOPE_ID = 42;
    protected static final KapuaId DEFAULT_ID = new KapuaEid(BigInteger.valueOf(DEFAULT_SCOPE_ID));

    protected TestBase(StepData stepData) {
        this.stepData = stepData;
    }

    protected void updateScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public KapuaId getKapuaId() {
        return new KapuaEid(BigInteger.valueOf(random.nextLong()).abs());
    }

    public KapuaId getKapuaId(int id) {
        return new KapuaEid(BigInteger.valueOf(id));
    }

    public KapuaId getKapuaId(String id) {
        return new KapuaEid(new BigInteger(id));
    }

    public KapuaId getCurrentScopeId() {
        if (stepData.contains("LastAccountId")) {
            return (KapuaId) stepData.get("LastAccountId");
        } else if (stepData.get(LAST_ACCOUNT) != null) {
            return ((Account) stepData.get(LAST_ACCOUNT)).getId();
        } else {
            return SYS_SCOPE_ID;
        }
    }

    public KapuaId getCurrentParentId() {
        if (stepData.get(LAST_ACCOUNT) != null) {
            return ((Account) stepData.get(LAST_ACCOUNT)).getScopeId();
        } else {
            return SYS_SCOPE_ID;
        }
    }

    public KapuaId getCurrentUserId() {
        if (stepData.contains("LastUserId")) {
            return (KapuaId) stepData.get("LastUserId");
        } else if (stepData.get("LastUser") != null) {
            return ((Account) stepData.get("LastUser")).getId();
        } else {
            return SYS_USER_ID;
        }
    }

    public void primeException() {
        stepData.put("ExceptionCaught", false);
        stepData.remove("Exception");
    }

    /**
     * Check the exception that was caught. In case the exception was expected the type and message is shown in the cucumber logs.
     * Otherwise the exception is rethrown failing the test and dumping the stack trace to help resolving problems.
     *
     * @param ex
     * @throws Exception
     */
    public void verifyException(Exception ex) throws Exception {
        boolean exceptionExpected = stepData.contains("ExceptionExpected") && (boolean) stepData.get("ExceptionExpected");
        String exceptionName = stepData.contains("ExceptionName") ? ((String) stepData.get("ExceptionName")).trim() : "";
        String exceptionMessage = stepData.contains("ExceptionMessage") ? ((String) stepData.get("ExceptionMessage")).trim() : "";

        if (!exceptionExpected ||
                (!exceptionName.isEmpty() && !ex.getClass().toGenericString().contains(exceptionName)) ||
                (!exceptionMessage.isEmpty() && !exceptionMessage.trim().contentEquals("*") && !ex.getMessage().contains(exceptionMessage))) {
            scenario.log("An unexpected exception was raised!");
            throw (ex);
        }

        scenario.log("Exception raised as expected: " + ex.getClass().getCanonicalName() + ", " + ex.getMessage());
        stepData.put("ExceptionCaught", true);
        stepData.put("Exception", ex);
    }

    public void verifyAssertionError(AssertionError assetError) throws AssertionError {
        boolean assertErrorExpected = stepData.contains("AssertErrorExpected") ? (boolean) stepData.get("AssertErrorExpected") : false;
        String assertErrorName = stepData.contains("AssertErrorName") ? ((String) stepData.get("AssertErrorName")).trim() : "";
        String assertErrorMessage = stepData.contains("AssertErrorMessage") ? ((String) stepData.get("AssertErrorMessage")).trim() : "";

        if (!assertErrorExpected ||
                (!assertErrorName.isEmpty() && !assetError.getClass().toGenericString().contains(assertErrorName)) ||
                (!assertErrorMessage.isEmpty() && !assertErrorMessage.trim().contentEquals("*") && !assetError.getMessage().contains(assertErrorMessage))) {
            scenario.log("An unexpected assert error was raised!");
            throw (assetError);
        }

        scenario.log("Assert error raised as expected: " + assetError.getClass().getCanonicalName() + ", " + assetError.getMessage());
        stepData.put("AssertErrorCaught", true);
        stepData.put("AssertError", assetError);
    }


    public Date parseDateString(String date) {
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date expDate = null;
        Instant now = Instant.now();

        if (date == null) {
            return null;
        }
        // Special keywords for date
        switch (date.trim().toLowerCase()) {
            case "yesterday":
                expDate = Date.from(now.minus(Duration.ofDays(1)));
                break;
            case "today":
                expDate = Date.from(now);
                break;
            case "tomorrow":
                expDate = Date.from(now.plus(Duration.ofDays(1)));
                break;
            case "null":
                break;
        }

        // Not one of the special cases. Just parse the date.
        try {
            expDate = df.parse(date.trim().toLowerCase());
        } catch (ParseException | NullPointerException e) {
            // skip, leave date null
        }

        return expDate;
    }

    protected void updateCount(Callable<Integer> countEvaluator) throws Exception {
        primeException();
        try {
            stepData.updateCount(countEvaluator.call());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    protected void updateCountAndCheck(Callable<Integer> countEvaluator, int valueToCompare) throws Exception {
        primeException();
        try {
            int count = countEvaluator.call();
            stepData.updateCount(count);
            //why this check???
            Assert.assertEquals(valueToCompare, count);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    public String getRandomString() {
        return String.valueOf(random.nextInt());
    }
}
