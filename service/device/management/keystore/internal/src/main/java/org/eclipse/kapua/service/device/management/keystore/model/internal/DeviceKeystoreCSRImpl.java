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
package org.eclipse.kapua.service.device.management.keystore.model.internal;

import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;

/**
 * {@link DeviceKeystoreCSR} implementation.
 *
 * @since 1.5.0
 */
public class DeviceKeystoreCSRImpl implements DeviceKeystoreCSR {

    private String signingRequest;

    /**
     * Constructor.
     *
     * @since 1.5.0
     */
    public DeviceKeystoreCSRImpl() {
    }

    @Override
    public String getSigningRequest() {
        return signingRequest;
    }

    @Override
    public void setSigningRequest(String signingRequest) {
        this.signingRequest = signingRequest;
    }
}