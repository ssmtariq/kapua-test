/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Wait {

    private static final Logger logger = LoggerFactory.getLogger(Wait.class);

    private Wait() {
    }

    @FunctionalInterface
    public interface Condition {

        public boolean test() throws Exception;
    }

    @FunctionalInterface
    public interface AssertCondition {

        public void test() throws Exception;
    }

    /**
     * Test for a specific amount of time for a state of become {@code true}
     * <p>
     * In case the state provider or the {@link Thread#sleep(long)} throws an exception the method
     * will wrap this into a {@link RuntimeException} rethrow it.
     * </p>
     *
     * @param action
     *            short description what this waits for. This will be used in the exception message.
     * @param duration
     *            the duration to wait
     * @param testPeriod
     *            the period between tests
     * @param condition
     *            The condition to test for
     * @return {@code true} if the state was reached in that period of time, {@code false} otherwise
     */
    public static boolean testFor(String action, final Duration duration, final Duration testPeriod, final Condition condition) {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(testPeriod);
        Objects.requireNonNull(condition);

        if (action != null) {
            action = action + ": ";
        } else {
            action = "";
        }

        final Instant start = Instant.now();
        final Instant end = start.plus(duration.abs());
        while (Instant.now().isBefore(end)) {
            try {
                final boolean result = condition.test();
                if (result) {
                    logger.info("{}completed successfully after {} ms", action, Duration.between(start, Instant.now()).toMillis());
                    return true;
                }
                Thread.sleep(testPeriod.toMillis());
            } catch (final Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean testFor(final Duration duration, final Duration testPeriod, final Condition condition) {
        return testFor(null, duration, testPeriod, condition);
    }

    public static boolean testFor(final Duration duration, final Condition condition) {
        return testFor(duration, Duration.ofMillis(100), condition);
    }

    /**
     * Wait for a condition or fail
     *
     * @param action
     *            short description what this waits for. This will be used in the exception message.
     * @param duration
     *            the duration to wait for
     * @param testPeriod
     *            the period between testing for the condition
     * @param condition
     *            the condition to test for, returning {@code false} means that the condition is not met
     * @throws WaitTimeoutException
     *             in case the wait timed out
     */
    public static void waitFor(final String action, final Duration duration, final Duration testPeriod, final Condition condition) throws WaitTimeoutException {
        if (testFor(action, duration, testPeriod, condition)) {
            return;
        }
        throw new WaitTimeoutException(action, duration);
    }

    /**
     * Allow to wait using assertions
     *
     * @param action
     *            short description what this waits for. This will be used in the exception message.
     * @param duration
     *            the duration to wait for
     * @param testPeriod
     *            the period between testing for the condition
     * @param condition
     *            the condition to test for, throwing an {@link AssertionError} means that the condition is not met
     * @throws WaitTimeoutException
     *             in case the wait timed out
     */
    public static void assertFor(final String action, final Duration duration, final Duration testPeriod, final AssertCondition condition) throws WaitTimeoutException {

        final AtomicReference<AssertionError> cause = new AtomicReference<>();

        if (testFor(action, duration, testPeriod, () -> {

            try {
                condition.test();
                cause.set(null);
            } catch (final AssertionError e) {
                cause.set(e);
                // assertion error means: condition not met
                return false;
            }
            return true;
        })) {
            return;
        }

        throw new WaitTimeoutException(action, duration, cause.get());
    }
}
