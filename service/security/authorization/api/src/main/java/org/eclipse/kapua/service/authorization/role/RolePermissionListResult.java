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
package org.eclipse.kapua.service.authorization.role;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.kapua.model.query.KapuaListResult;

/**
 * {@link RolePermission} list result definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "rolePermissions")
@XmlType(factoryClass = RolePermissionXmlRegistry.class, factoryMethod = "newRolePermissionListResult")
public interface RolePermissionListResult extends KapuaListResult<RolePermission> {

}
