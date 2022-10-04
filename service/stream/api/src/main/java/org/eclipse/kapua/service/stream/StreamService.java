/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.stream;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.message.device.data.KapuaDataMessage;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseMessage;

/**
 * @since 1.0.0
 */
public interface StreamService extends KapuaService {

    /**
     * @param message
     * @param timeout
     * @return
     * @throws KapuaException
     * @since 1.0.0
     */
    KapuaResponseMessage<?, ?> publish(KapuaDataMessage message, Long timeout) throws KapuaException;
}
