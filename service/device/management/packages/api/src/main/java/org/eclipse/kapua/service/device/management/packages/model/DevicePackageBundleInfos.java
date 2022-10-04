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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.management.packages.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * {@link DevicePackageBundleInfos} implementation.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "bundleInfos")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = DevicePackageXmlRegistry.class, factoryMethod = "newDevicePackageBundleInfos")
public interface DevicePackageBundleInfos {

    /**
     * Gets the {@link DevicePackageBundleInfo} {@link List}.
     *
     * @return The {@link DevicePackageBundleInfo} {@link List}.
     * @since 1.0.0
     */
    @XmlElement(name = "bundleInfo")
    List<DevicePackageBundleInfo> getBundlesInfos();
}
