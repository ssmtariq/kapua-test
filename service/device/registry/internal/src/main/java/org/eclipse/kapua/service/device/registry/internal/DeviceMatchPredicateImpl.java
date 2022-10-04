/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.registry.internal;

import java.util.Arrays;

import org.eclipse.kapua.commons.model.query.predicate.AbstractMatchPredicate;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceMatchPredicate;

public class DeviceMatchPredicateImpl<T> extends AbstractMatchPredicate<T> implements DeviceMatchPredicate<T> {

    /**
     * Constructor.
     *
     * @param matchTerm
     * @since 1.3.0
     */
    public DeviceMatchPredicateImpl(T matchTerm) {
        this.attributeNames = Arrays.asList(
                DeviceAttributes.CLIENT_ID,
                DeviceAttributes.DISPLAY_NAME,
                DeviceAttributes.SERIAL_NUMBER,
                DeviceAttributes.MODEL_ID,
                DeviceAttributes.MODEL_NAME,
                DeviceAttributes.BIOS_VERSION,
                DeviceAttributes.FIRMWARE_VERSION,
                DeviceAttributes.OS_VERSION,
                DeviceAttributes.JVM_VERSION,
                DeviceAttributes.OSGI_FRAMEWORK_VERSION,
                DeviceAttributes.APPLICATION_FRAMEWORK_VERSION,
                DeviceAttributes.CONNECTION_INTERFACE,
                DeviceAttributes.CONNECTION_IP
        );
        this.matchTerm = matchTerm;
    }

}
