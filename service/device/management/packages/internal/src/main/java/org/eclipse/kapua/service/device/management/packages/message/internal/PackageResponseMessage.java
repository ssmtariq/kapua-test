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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.message.internal;

import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponseMessageImpl;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;
import org.eclipse.kapua.service.device.management.packages.model.DevicePackage;

/**
 * {@link DevicePackage} {@link KapuaResponseMessage} implementation.
 *
 * @since 1.0.0
 */
public class PackageResponseMessage extends KapuaResponseMessageImpl<PackageResponseChannel, PackageResponsePayload>
        implements KapuaResponseMessage<PackageResponseChannel, PackageResponsePayload> {

    private static final long serialVersionUID = 1728387783187306831L;
}
