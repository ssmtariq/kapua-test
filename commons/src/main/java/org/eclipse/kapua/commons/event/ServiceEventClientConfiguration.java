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
 *******************************************************************************/
package org.eclipse.kapua.commons.event;

import org.eclipse.kapua.event.ServiceEventBusListener;

public class ServiceEventClientConfiguration {

    private String address;
    private String clientName;
    private ServiceEventBusListener eventListener;

    public ServiceEventClientConfiguration(String address, String subscriberName, ServiceEventBusListener eventListener) {
        this.address = address;
        this.clientName = subscriberName;
        this.eventListener = eventListener;
    }

    public String getAddress() {
        return address;
    }

    public String getClientName() {
        return clientName;
    }

    public ServiceEventBusListener getEventListener() {
        return eventListener;
    }
}
