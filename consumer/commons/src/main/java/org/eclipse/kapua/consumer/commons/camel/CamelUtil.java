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
package org.eclipse.kapua.consumer.commons.camel;

import javax.jms.JMSException;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.camel.Message;
import org.eclipse.kapua.broker.client.message.MessageConstants;
import org.eclipse.kapua.consumer.commons.listener.CamelConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Camel utility methods
 *
 * @since 1.0
 */
public class CamelUtil {

    public static final Logger logger = LoggerFactory.getLogger(CamelUtil.class);

    private CamelUtil() {
    }

    /**
     * Extract the topic from the {@link Message} looking for Kapua header property that handles this value and inspecting Camel header properties)
     *
     * @param message
     * @return
     * @throws JMSException
     */
    public static String getTopic(org.apache.camel.Message message) throws JMSException {
        String topicOrig = message.getHeader(MessageConstants.PROPERTY_ORIGINAL_TOPIC, String.class);
        if (topicOrig != null) {
            return topicOrig;
        } else {
            ActiveMQDestination destination = message.getHeader(CamelConstants.JMS_HEADER_DESTINATION, ActiveMQDestination.class);
            if (destination instanceof ActiveMQTopic) {
                ActiveMQTopic destinationTopic = (ActiveMQTopic) destination;
                return destinationTopic.getTopicName().substring(MessageConstants.VT_TOPIC_PREFIX.length());
            } else {
                logger.warn("jmsMessage destination is not a Topic or Queue: {}", destination);
                throw new JMSException(String.format("Unable to extract the destination. Wrong destination %s", destination));
            }
        }
    }

}
