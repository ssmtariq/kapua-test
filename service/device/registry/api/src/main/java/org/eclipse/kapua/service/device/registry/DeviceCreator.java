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
package org.eclipse.kapua.service.device.registry;

import org.eclipse.kapua.model.KapuaUpdatableEntityCreator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * {@link DeviceCreator} encapsulates all the information needed to create a new {@link Device} in the system.<br>
 * The data provided will be used to seed the new {@link Device} and its related information.<br>
 * The fields of the {@link DeviceCreator} presents the attributes that are searchable for a given device.<br>
 * The DeviceCreator Properties field can be used to provide additional properties associated to the Device;
 * those properties will not be searchable through Device queries.<br>
 * The clientId field of the Device is used to store the MAC address of the primary network interface of the device.
 *
 * @since 1.0.0
 */
@XmlRootElement(name = "deviceCreator")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = {
        "groupId",
        "clientId",
        "status",
        "connectionId",
        "lastEventId",
        "displayName",
        "serialNumber",
        "modelId",
        "modelName",
        "imei",
        "imsi",
        "iccid",
        "biosVersion",
        "firmwareVersion",
        "osVersion",
        "jvmVersion",
        "osgiFrameworkVersion",
        "applicationFrameworkVersion",
        "connectionInterface",
        "connectionIp",
        "applicationIdentifiers",
        "acceptEncoding",
        "customAttribute1",
        "customAttribute2",
        "customAttribute3",
        "customAttribute4",
        "customAttribute5",
        "extendedProperties"
}, factoryClass = DeviceXmlRegistry.class, factoryMethod = "newDeviceCreator")
public interface DeviceCreator extends KapuaUpdatableEntityCreator<Device> {

    /**
     * Gets the {@link Group#getId()}.
     *
     * @return The {@link Group#getId()}.
     * @since 1.0.0
     */
    @XmlElement(name = "groupId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getGroupId();

    /**
     * Sets the {@link Group#getId()}.
     *
     * @param groupId The {@link Group#getId()}.
     * @since 1.0.0
     */
    void setGroupId(KapuaId groupId);

    /**
     * Gets the client identifier.
     *
     * @return The client identifier.
     * @since 1.0.0
     */
    @XmlElement(name = "clientId")
    String getClientId();

    /**
     * Sets the client identifier.
     *
     * @param clientId The client identifier.
     * @since 1.0.0
     */
    void setClientId(String clientId);

    /**
     * Gets the {@link DeviceConnection#getId()}.
     *
     * @return The {@link DeviceConnection#getId()}.
     * @since 1.0.0
     */
    @XmlElement(name = "connectionId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getConnectionId();

    /**
     * Sets the {@link DeviceConnection#getId()}.
     *
     * @param connectionId The {@link DeviceConnection#getId()}.
     * @since 1.0.0
     */
    void setConnectionId(KapuaId connectionId);

    /**
     * Gets the {@link DeviceStatus}.
     *
     * @return The {@link DeviceStatus}.
     * @since 1.0.0
     */
    @XmlElement(name = "status")
    DeviceStatus getStatus();

    /**
     * Sets the {@link DeviceStatus}.
     *
     * @param status The {@link DeviceStatus}.
     * @since 1.0.0
     */
    void setStatus(DeviceStatus status);

    /**
     * Gets the display name.
     *
     * @return The display name.
     * @since 1.0.0
     */
    @XmlElement(name = "displayName")
    String getDisplayName();

    /**
     * Sets the display name.
     *
     * @param diplayName The display name.
     * @since 1.0.0
     */
    void setDisplayName(String diplayName);

    /**
     * Gets the last {@link DeviceEvent#getId()}.
     *
     * @return The last {@link DeviceEvent#getId()}.
     * @since 1.0.0
     */
    @XmlElement(name = "lastEventId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getLastEventId();

    /**
     * Sets the last {@link DeviceEvent#getId()}.
     *
     * @param lastEventId The last {@link DeviceEvent#getId()}.
     * @since 1.0.0
     */
    void setLastEventId(KapuaId lastEventId);

    /**
     * Gets the serial number.
     *
     * @return The serial number.
     * @since 1.0.0
     */
    @XmlElement(name = "serialNumber")
    String getSerialNumber();

    /**
     * Sets the serial number.
     *
     * @param serialNumber The serial number.
     * @since 1.0.0
     */
    void setSerialNumber(String serialNumber);

    /**
     * Gets the model identifier.
     *
     * @return The model identifier.
     * @since 1.0.0
     */
    @XmlElement(name = "modelId")
    String getModelId();

    /**
     * Sets the model identifier.
     *
     * @param modelId The model identifier.
     * @since 1.0.0
     */
    void setModelId(String modelId);

    /**
     * Gets the model name.
     *
     * @return The model name.
     * @since 1.0.0
     */
    @XmlElement(name = "modelName")
    String getModelName();

    /**
     * Sets the model name.
     *
     * @param modelName The model name.
     * @since 1.0.0
     */
    void setModelName(String modelName);

    /**
     * Gets the modem imei.
     *
     * @return The modem imei.
     * @since 1.0.0
     */
    @XmlElement(name = "imei")
    String getImei();

    /**
     * Sets the modem imei.
     *
     * @param imei The modem imei.
     * @since 1.0.0
     */
    void setImei(String imei);

    /**
     * Gets the modem imsi.
     *
     * @return The modem imsi.
     * @since 1.0.0
     */
    @XmlElement(name = "imsi")
    String getImsi();

    /**
     * Sets the modem imsi.
     *
     * @param imsi The modem imsi.
     * @since 1.0.0
     */
    void setImsi(String imsi);

    /**
     * Gets the modem iccid.
     *
     * @return The modem iccid.
     * @since 1.0.0
     */
    @XmlElement(name = "iccid")
    String getIccid();

    /**
     * Sets the modem iccid.
     *
     * @param iccid The modem iccid.
     * @since 1.0.0
     */
    void setIccid(String iccid);

    /**
     * Gets the bios version.
     *
     * @return The bios version.
     * @since 1.0.0
     */
    @XmlElement(name = "biosVersion")
    String getBiosVersion();

    /**
     * Sets the bios version.
     *
     * @param biosVersion The bios version.
     * @since 1.0.0
     */
    void setBiosVersion(String biosVersion);

    /**
     * Gets the firmware version.
     *
     * @return The firmware version.
     * @since 1.0.0
     */
    @XmlElement(name = "firmwareVersion")
    String getFirmwareVersion();

    /**
     * Sets the firmware version.
     *
     * @param firmwareVersion The firmware version.
     * @since 1.0.0
     */
    void setFirmwareVersion(String firmwareVersion);

    /**
     * Gets the OS version.
     *
     * @return The OS version.
     * @since 1.0.0
     */
    @XmlElement(name = "osVersion")
    String getOsVersion();

    /**
     * Sets the OS version.
     *
     * @param osVersion The OS version.
     * @since 1.0.0
     */
    void setOsVersion(String osVersion);

    /**
     * Gets the JVM version.
     *
     * @return The JVM version.
     * @since 1.0.0
     */
    @XmlElement(name = "jvmVersion")
    String getJvmVersion();

    /**
     * Sets the JVM version.
     *
     * @param jvmVersion The JVM version.
     * @since 1.0.0
     */
    void setJvmVersion(String jvmVersion);

    /**
     * Gets the OSGi framework version.
     *
     * @return The OSGi framework version.
     * @since 1.0.0
     */
    @XmlElement(name = "osgiFrameworkVersion")
    String getOsgiFrameworkVersion();

    /**
     * Sets the OSGi framework version.
     *
     * @param osgiFrameworkVersion The OSGi framework version.
     * @since 1.0.0
     */
    void setOsgiFrameworkVersion(String osgiFrameworkVersion);

    /**
     * Gets the application framework version.
     *
     * @return The application framework version.
     * @since 1.0.0
     */
    @XmlElement(name = "applicationFrameworkVersion")
    String getApplicationFrameworkVersion();

    /**
     * Sets the application framework version.
     *
     * @param appFrameworkVersion The application framework version.
     * @since 1.0.0
     */
    void setApplicationFrameworkVersion(String appFrameworkVersion);

    /**
     * Gets the device network interfaces name.
     *
     * @return The device network interfaces name.
     * @since 1.0.0
     */
    @XmlElement(name = "connectionInterface")
    String getConnectionInterface();

    /**
     * Sets the device network interfaces name.
     *
     * @param connectionInterface The device network interfaces name.
     * @since 1.0.0
     */
    void setConnectionInterface(String connectionInterface);

    /**
     * Gets the device network interfaces IP.
     *
     * @return The device network interfaces IP.
     * @since 1.0.0
     */
    @XmlElement(name = "connectionIp")
    String getConnectionIp();

    /**
     * Sets the device network interfaces IP.
     *
     * @param connectionIp The device network interfaces IP.
     * @since 1.0.0
     */
    void setConnectionIp(String connectionIp);

    /**
     * Gets the application identifiers supported.
     *
     * @return The application identifiers supported.
     * @since 1.0.0
     */
    @XmlElement(name = "applicationIdentifiers")
    String getApplicationIdentifiers();

    /**
     * Sets the application identifiers supported.
     *
     * @param applicationIdentifiers The application identifiers supported.
     * @since 1.0.0
     */
    void setApplicationIdentifiers(String applicationIdentifiers);

    /**
     * Gets the accept encodings.
     *
     * @return The accept encodings.
     * @since 1.0.0
     */
    @XmlElement(name = "acceptEncoding")
    String getAcceptEncoding();

    /**
     * Sets the accept encodings.
     *
     * @param acceptEncoding The accept encodings.
     * @since 1.0.0
     */
    void setAcceptEncoding(String acceptEncoding);

    /**
     * Gets the custom attribute 1.
     *
     * @return The custom attribute 1.
     * @since 1.0.0
     */
    @XmlElement(name = "customAttribute1")
    String getCustomAttribute1();

    /**
     * Sets the custom attribute 1.
     *
     * @param customAttribute1 The custom attribute 1.
     * @since 1.0.0
     */
    void setCustomAttribute1(String customAttribute1);

    /**
     * Gets the custom attribute 2.
     *
     * @return The custom attribute 2.
     * @since 1.0.0
     */
    @XmlElement(name = "customAttribute2")
    String getCustomAttribute2();

    /**
     * Sets the custom attribute 2.
     *
     * @param customAttribute2 The custom attribute 2.
     * @since 1.0.0
     */
    void setCustomAttribute2(String customAttribute2);

    /**
     * Gets the custom attribute 3.
     *
     * @return The custom attribute 3.
     * @since 1.0.0
     */
    @XmlElement(name = "customAttribute3")
    String getCustomAttribute3();

    /**
     * Sets the custom attribute 3.
     *
     * @param customAttribute3 The custom attribute 3.
     * @since 1.0.0
     */
    void setCustomAttribute3(String customAttribute3);

    /**
     * Gets the custom attribute 4.
     *
     * @return The custom attribute 4.
     * @since 1.0.0
     */
    @XmlElement(name = "customAttribute4")
    String getCustomAttribute4();

    /**
     * Sets the custom attribute 4.
     *
     * @param customAttribute4 The custom attribute 4.
     * @since 1.0.0
     */
    void setCustomAttribute4(String customAttribute4);

    /**
     * Gets the custom attribute 5.
     *
     * @return The custom attribute 5.
     * @since 1.0.0
     */
    @XmlElement(name = "customAttribute5")
    String getCustomAttribute5();

    /**
     * Sets the custom attribute 5.
     *
     * @param customAttribute5 The custom attribute 5.
     * @since 1.0.0
     */
    void setCustomAttribute5(String customAttribute5);

    /**
     * Gets the {@link DeviceExtendedProperty} {@link List}.
     *
     * @return The {@link DeviceExtendedProperty} {@link List}.
     * @since 1.5.0
     */
    @XmlElement(name = "extendedProperties")
    List<DeviceExtendedProperty> getExtendedProperties();

    /**
     * Add a {@link DeviceExtendedProperty} to the {@link List}.
     *
     * @param extendedProperty The {@link DeviceExtendedProperty} to add.
     * @since 1.5.0
     */
    void addExtendedProperty(DeviceExtendedProperty extendedProperty);

    /**
     * Sets the {@link DeviceExtendedProperty} {@link List}.
     *
     * @param extendedProperties The {@link DeviceExtendedProperty} {@link List}.
     * @since 1.5.0
     */
    void setExtendedProperties(List<DeviceExtendedProperty> extendedProperties);
}
