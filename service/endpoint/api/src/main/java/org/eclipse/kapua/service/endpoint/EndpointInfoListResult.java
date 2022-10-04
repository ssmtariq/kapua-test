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
package org.eclipse.kapua.service.endpoint;

import org.eclipse.kapua.model.query.KapuaListResult;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * {@link EndpointInfo} list result definition.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "endpointInfos")
@XmlType(factoryClass = EndpointInfoXmlRegistry.class, factoryMethod = "newListResult")
public interface EndpointInfoListResult extends KapuaListResult<EndpointInfo> {

}
