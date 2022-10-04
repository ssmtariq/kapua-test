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
package org.eclipse.kapua.service.scheduler.trigger.definition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.service.scheduler.trigger.TriggerXmlRegistry;

/**
 * {@link TriggerProperty} definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "triggerProperty")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = TriggerXmlRegistry.class, factoryMethod = "newTriggerProperty")
public interface TriggerProperty {

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 1.0.0
     */
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the property type.
     *
     * @return The property type.
     * @since 1.0.0
     */
    String getPropertyType();

    /**
     * Sets the property type.
     *
     * @param propertyType The property type.
     * @since 1.0.0
     */
    void setPropertyType(String propertyType);

    /**
     * Sets the property value.
     *
     * @return The property value.
     * @since 1.0.0
     */
    String getPropertyValue();

    /**
     * Sets the property value.
     *
     * @param propertyValue The property value.
     * @since 1.0.0
     */
    void setPropertyValue(String propertyValue);

}
