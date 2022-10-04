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
package org.eclipse.kapua.service.device.management.keystore.job.definition;

import org.eclipse.kapua.service.device.management.keystore.job.DeviceKeystoreCertificateCreateTargetProcessor;
import org.eclipse.kapua.service.job.step.definition.JobPropertyKey;

/**
 * {@link DeviceKeystoreCertificateCreateTargetProcessor} {@link JobPropertyKey} implementation.
 *
 * @since 1.5.0
 */
public class DeviceCertificateCreatePropertyKeys implements JobPropertyKey {

    /**
     * @since 1.5.0
     */
    public static final String KEYSTORE_ID = "keystoreId";

    /**
     * @since 1.5.0
     */
    public static final String ALIAS = "alias";

    /**
     * @since 1.5.0
     */
    public static final String CERTIFICATE = "certificate";

    /**
     * @since 1.5.0
     */
    public static final String TIMEOUT = "timeout";

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    private DeviceCertificateCreatePropertyKeys() {
    }
}
