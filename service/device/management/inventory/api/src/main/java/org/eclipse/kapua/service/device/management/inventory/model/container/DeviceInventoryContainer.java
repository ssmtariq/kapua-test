/*******************************************************************************
 * Copyright (c) 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.model.container;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link DeviceInventoryContainer} definition.
 * <p>
 * It represents a container present on a device.
 *
 * @since 2.0.0
 */
@XmlRootElement(name = "deviceInventoryContainer")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DeviceInventoryContainersXmlRegistry.class, factoryMethod = "newDeviceInventoryContainer")
public interface DeviceInventoryContainer {

    /**
     * Gets the name.
     *
     * @return The name.
     * @since 2.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Sets the name.
     *
     * @param name The name.
     * @since 2.0.0
     */
    void setName(String name);

    /**
     * Gets the container version.
     *
     * @return The version.
     * @since 2.0.0
     */
    @XmlElement(name = "version")
    String getVersion();

    /**
     * Sets the version.
     *
     * @param version The version.
     * @since 2.0.0
     */
    void setVersion(String version);

    /**
     * Gets the type.
     *
     * @return The type.
     * @since 2.0.0
     */
    @XmlElement(name = "containerType")
    String getContainerType();

    /**
     * Sets the container type.
     *
     * @param containerType The container type.
     * @since 2.0.0
     */
    void setContainerType(String containerType);
}
