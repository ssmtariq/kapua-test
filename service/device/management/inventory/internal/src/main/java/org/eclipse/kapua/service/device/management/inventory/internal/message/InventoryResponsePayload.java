/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.inventory.internal.message;

import org.eclipse.kapua.commons.util.xml.XmlUtil;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.device.management.commons.message.response.KapuaResponsePayloadImpl;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSetting;
import org.eclipse.kapua.service.device.management.commons.setting.DeviceManagementSettingKey;
import org.eclipse.kapua.service.device.management.inventory.DeviceInventoryManagementFactory;
import org.eclipse.kapua.service.device.management.inventory.model.bundle.DeviceInventoryBundles;
import org.eclipse.kapua.service.device.management.inventory.model.container.DeviceInventoryContainers;
import org.eclipse.kapua.service.device.management.inventory.model.inventory.DeviceInventory;
import org.eclipse.kapua.service.device.management.inventory.model.packages.DeviceInventoryPackages;
import org.eclipse.kapua.service.device.management.inventory.model.system.DeviceInventorySystemPackages;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponsePayload;

import javax.validation.constraints.NotNull;

/**
 * {@link DeviceInventory} {@link KapuaResponsePayload} implementation.
 *
 * @since 1.5.0
 */
public class InventoryResponsePayload extends KapuaResponsePayloadImpl implements KapuaResponsePayload {

    private static final long serialVersionUID = 4380715272822080425L;

    private static final String CHAR_ENCODING = DeviceManagementSetting.getInstance().getString(DeviceManagementSettingKey.CHAR_ENCODING);

    private static final DeviceInventoryManagementFactory DEVICE_INVENTORY_MANAGEMENT_FACTORY = KapuaLocator.getInstance().getFactory(DeviceInventoryManagementFactory.class);

    /**
     * Gets the {@link DeviceInventory} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventory} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventory getDeviceInventory() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventory();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventory.class);
    }

    /**
     * Sets the {@link DeviceInventory} in the {@link #getBody()}.
     *
     * @param deviceInventory The {@link DeviceInventory} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventory(@NotNull DeviceInventory deviceInventory) throws Exception {
        String bodyString = XmlUtil.marshal(deviceInventory);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceInventoryBundles} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventoryBundles} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventoryBundles getDeviceInventoryBundles() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventoryBundles();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventoryBundles.class);
    }

    /**
     * Sets the {@link DeviceInventoryBundles} in the {@link #getBody()}.
     *
     * @param inventoryBundles The {@link DeviceInventoryBundles} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventoryBundles(@NotNull DeviceInventoryBundles inventoryBundles) throws Exception {
        String bodyString = XmlUtil.marshal(inventoryBundles);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceInventoryContainers} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventoryContainers} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 2.0.0
     */
    public DeviceInventoryContainers getDeviceInventoryContainers() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventoryContainers();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventoryContainers.class);
    }

    /**
     * Sets the {@link DeviceInventoryContainers} in the {@link #getBody()}.
     *
     * @param inventoryContainers The {@link DeviceInventoryContainers} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 2.0.0
     */
    public void setDeviceInventoryContainers(@NotNull DeviceInventoryContainers inventoryContainers) throws Exception {
        String bodyString = XmlUtil.marshal(inventoryContainers);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceInventorySystemPackages} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventorySystemPackages} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventorySystemPackages getDeviceInventorySystemPackages() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventorySystemPackages();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventorySystemPackages.class);
    }

    /**
     * Sets the {@link DeviceInventorySystemPackages} in the {@link #getBody()}.
     *
     * @param systemPackages The {@link DeviceInventorySystemPackages} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventorySystemPackages(@NotNull DeviceInventorySystemPackages systemPackages) throws Exception {
        String bodyString = XmlUtil.marshal(systemPackages);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }

    /**
     * Gets the {@link DeviceInventoryPackages} from the {@link #getBody()}.
     *
     * @return The {@link DeviceInventoryPackages} from the {@link #getBody()}.
     * @throws Exception if reading {@link #getBody()} errors.
     * @since 1.5.0
     */
    public DeviceInventoryPackages getDeviceInventoryPackages() throws Exception {
        if (!hasBody()) {
            return DEVICE_INVENTORY_MANAGEMENT_FACTORY.newDeviceInventoryPackages();
        }

        String bodyString = new String(getBody(), CHAR_ENCODING);
        return XmlUtil.unmarshal(bodyString, DeviceInventoryPackages.class);
    }

    /**
     * Sets the {@link DeviceInventoryPackages} in the {@link #getBody()}.
     *
     * @param packages The {@link DeviceInventoryPackages} in the {@link #getBody()}.
     * @throws Exception if writing errors.
     * @since 1.5.0
     */
    public void setDeviceInventoryPackages(@NotNull DeviceInventoryPackages packages) throws Exception {
        String bodyString = XmlUtil.marshal(packages);
        setBody(bodyString.getBytes(CHAR_ENCODING));
    }
}
