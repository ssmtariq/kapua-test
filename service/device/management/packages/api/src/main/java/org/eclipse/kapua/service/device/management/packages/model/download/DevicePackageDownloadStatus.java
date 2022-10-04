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
package org.eclipse.kapua.service.device.management.packages.model.download;

/**
 * Device package download status.
 *
 * @since 1.0
 */
public enum DevicePackageDownloadStatus {
    /**
     * In progress
     */
    IN_PROGRESS,
    /**
     * Completed
     */
    COMPLETED,
    /**
     * Failed
     */
    FAILED,

    /**
     * No operation going on
     */
    NONE
}
