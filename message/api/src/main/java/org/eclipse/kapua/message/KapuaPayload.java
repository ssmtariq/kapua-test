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
package org.eclipse.kapua.message;

import org.eclipse.kapua.commons.util.Payloads;
import org.eclipse.kapua.message.xml.MessageXmlRegistry;
import org.eclipse.kapua.message.xml.MetricsXmlAdapter;
import org.eclipse.kapua.model.xml.BinaryXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

/**
 * {@link KapuaPayload} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "payload")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {"metrics", "body"}, factoryClass = MessageXmlRegistry.class, factoryMethod = "newPayload")
public interface KapuaPayload extends Payload {

    /**
     * Get the metrics map
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "metrics")
    @XmlJavaTypeAdapter(MetricsXmlAdapter.class)
    Map<String, Object> getMetrics();

    /**
     * Set the metrics map
     *
     * @param metrics
     * @since 1.0.0
     */
    void setMetrics(Map<String, Object> metrics);

    /**
     * Get the message body
     *
     * @return
     * @since 1.0.0
     */
    @XmlElement(name = "body")
    @XmlJavaTypeAdapter(BinaryXmlAdapter.class)
    byte[] getBody();

    /**
     * Set the message body
     *
     * @param body
     * @since 1.0.0
     */
    void setBody(byte[] body);

    /**
     * Says whether or not the {@link #getBody()} has value.
     * <p>
     * Checks for {@code null} and size equals to 0
     *
     * @return {@code true} if {@link #getBody()} is not {@code null} and {@link #getBody()}{@code length > 0}, {@code false} otherwise.
     * @since 1.2.0
     */
    default boolean hasBody() {
        return getBody() != null && getBody().length > 0;
    }

    /**
     * Returns a string for displaying the {@link KapuaPayload} content.
     *
     * @return A {@link String} used for displaying the content of the {@link KapuaPayload}, never returns {@code null}
     * @since 1.0.0
     */
    @XmlTransient
    default String toDisplayString() {
        return Payloads.toDisplayString(getMetrics());
    }
}
