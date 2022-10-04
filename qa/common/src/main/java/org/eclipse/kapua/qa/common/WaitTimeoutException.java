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

public class WaitTimeoutException extends AssertionError {

    private static final String DEFAULT_MESSAGE = "Waiting for condition";
    private static final long serialVersionUID = 1L;

    public WaitTimeoutException(final Duration period) {
        super(makeMessage(DEFAULT_MESSAGE, period));
    }

    public WaitTimeoutException(final String action, final Duration period) {
        super(makeMessage(action, period));
    }

    public WaitTimeoutException(final Duration period, final Throwable cause) {
        super(makeMessage(DEFAULT_MESSAGE, period), cause);
    }

    public WaitTimeoutException(final String action, final Duration period, final Throwable cause) {
        super(makeMessage(action, period), cause);
    }

    private static String makeMessage(final String action, final Duration period) {
        String unit = "ms";
        long amount = period.abs().toMillis();

        if (amount % 1000 == 0) {
            amount /= 1000;
            unit = amount == 1 ? "second" : "seconds";
        }

        return String.format("%s expired after %s %s", action != null ? action : DEFAULT_MESSAGE, amount, unit);
    }

}
