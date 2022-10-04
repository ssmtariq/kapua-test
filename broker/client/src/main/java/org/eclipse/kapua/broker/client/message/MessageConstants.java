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
package org.eclipse.kapua.broker.client.message;

/**
 * Kapua message constants (metrics name and header keys)
 *
 * @since 1.0
 */
public class MessageConstants {

    private MessageConstants() {
    }

    //constants depending on messaging system (broker) layer
    public static final String VT_TOPIC_PREFIX = "VirtualTopic.";

    // used by login update info asynch
    public static final String METRIC_USERNAME = "username";
    public static final String METRIC_ACCOUNT = "account";
    public static final String METRIC_CLIENT_ID = "clientId";
    public static final String METRIC_IP = "ip";
    public static final String PROPERTY_ORIGINAL_TOPIC = "originalTopic";
    public static final String PROPERTY_ENQUEUED_TIMESTAMP = "enqueuedTimestamp";
    public static final String PROPERTY_BROKER_ID = "brokerId";
    public static final String PROPERTY_CLIENT_ID = "clientId";
    public static final String PROPERTY_SCOPE_ID = "scopeId";

    // used by login update info asynch
    public static final String METRIC_USER_ID = "userId";
    public static final String METRIC_NODE_ID = "nodeId";

    public static final String HEADER_KAPUA_CONNECTION_ID = "KAPUA_CONNECTION_ID";
    public static final String HEADER_KAPUA_RECEIVED_TIMESTAMP = "KAPUA_RECEIVED_TIMESTAMP";
    public static final String HEADER_KAPUA_CLIENT_ID = "KAPUA_CLIENT_ID";
    public static final String HEADER_KAPUA_CONNECTOR_DEVICE_PROTOCOL = "KAPUA_DEVICE_PROTOCOL";
    public static final String HEADER_KAPUA_SESSION = "KAPUA_SESSION";
    public static final String HEADER_KAPUA_BROKER_CONTEXT = "KAPUA_BROKER_CONTEXT";
    public static final String HEADER_KAPUA_PROCESSING_EXCEPTION = "KAPUA_PROCESSING_EXCEPTION";

}
