/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and others.
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
package org.eclipse.kapua.consumer.commons.converter;

import org.apache.camel.Exchange;
import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.shiro.util.ThreadContext;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.client.message.MessageConstants;
import org.eclipse.kapua.consumer.commons.listener.AbstractListener;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.commons.security.KapuaSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * Kapua Camel session filter used to bind/unbind Kapua session to the thread context
 */
public class KapuaCamelFilter extends AbstractListener {

    private static final Logger logger = LoggerFactory.getLogger(KapuaCamelFilter.class);

    public KapuaCamelFilter() {
        super("filter");
    }

    /**
     * Bind the Kapua session retrieved from the message header (with key {@link MessageConstants#HEADER_KAPUA_SESSION}) to the current thread context.
     *
     * @param exchange
     * @param value
     * @throws KapuaException
     */
    public void bindSession(Exchange exchange, Object value) throws KapuaException {
        ThreadContext.unbindSubject();
        if (Boolean.FALSE.equals(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_BROKER_CONTEXT, boolean.class))) {
            try {
                // FIX #164
                KapuaSecurityUtils
                        .setSession((KapuaSession) SerializationUtils.deserialize(Base64.getDecoder().decode(exchange.getIn().getHeader(MessageConstants.HEADER_KAPUA_SESSION, String.class))));
            } catch (IllegalArgumentException | SerializationException e) {
                // continue without session
                logger.debug("Cannot restore Kapua session: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Unbind the Kapua session from the current thread context.
     *
     * @param exchange
     * @param value
     * @throws KapuaException
     */
    public void unbindSession(Exchange exchange, Object value) throws KapuaException {
        KapuaSecurityUtils.clearSession();
    }

    /**
     * Bridge the error condition putting the in the JmsMessage header (At the present only the Exception raised is handled by this method)
     *
     * @param exchange
     * @param value
     */
    public void bridgeError(Exchange exchange, Object value) {
        // TODO is the in message null check needed?
        if (exchange.getIn() != null && exchange.getIn().getExchange().getException() != null) {
            exchange.getIn().setHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION,
                    Base64.getEncoder().encodeToString(SerializationUtils.serialize(exchange.getIn().getExchange().getException())));
        } else if (exchange.getException() != null) {
            exchange.getIn().setHeader(MessageConstants.HEADER_KAPUA_PROCESSING_EXCEPTION,
                    Base64.getEncoder().encodeToString(SerializationUtils.serialize(exchange.getException())));
        } else {
            logger.debug("Cannot serialize exception since it is null!");
        }
    }
}
