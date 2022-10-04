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
package org.eclipse.kapua.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link KapuaNamedEntity} definition.
 * <p>
 * The {@link KapuaNamedEntity} adds on top of the {@link KapuaUpdatableEntity} the following properties:
 *
 * <ul>
 * <li>name</li>
 * <li>description</li>
 * </ul>
 *
 * <div>
 *
 * <p>
 * <b>Name</b>
 * </p>
 * <p>
 * The <i>Name</i> property is the unique name of the {@link KapuaEntity} in the scope.
 * </p>
 *
 * <p>
 * <b>Description</b>
 * </p>
 * <p>
 * The <i>Description</i> property is the optional description of the {@link KapuaEntity}.
 * </p>
 * </div>
 *
 * @since 1.0.0
 */
@XmlType(propOrder = {"name", "description"})
public interface KapuaNamedEntity extends KapuaUpdatableEntity {

    /**
     * Gets the name of the {@link KapuaEntity}
     *
     * @return the name of the {@link KapuaEntity}
     * @since 1.0.0
     */
    @XmlElement(name = "name")
    String getName();

    /**
     * Sets the name of the {@link KapuaEntity}
     *
     * @param name the name of the {@link KapuaEntity}
     * @since 1.0.0
     */
    void setName(String name);

    /**
     * Gets the description for the {@link KapuaEntity}
     *
     * @return the description of this {@link KapuaEntity}
     * @since 1.1.0
     */
    String getDescription();

    /**
     * Sets the description for the {@link KapuaEntity}
     *
     * @param description the description for the {@link KapuaEntity}
     * @since 1.1.0
     */
    void setDescription(String description);
}
