/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.broker.client.protocol;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.kapua.broker.client.message.MessageType;
import org.eclipse.kapua.broker.client.setting.BrokerClientSetting;
import org.eclipse.kapua.broker.client.setting.BrokerClientSettingKey;
import org.eclipse.kapua.message.KapuaMessage;
import org.eclipse.kapua.service.device.call.message.DeviceMessage;
import org.eclipse.kapua.service.device.call.message.kura.app.notification.KuraNotifyMessage;
import org.eclipse.kapua.service.device.management.message.notification.KapuaNotifyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A default implementation of the {@link ProtocolDescriptorProvider} interface
 * <p>
 * This implementation tries to provide some ready-to-use defaults for the {@link ProtocolDescriptorProvider} system.
 * If nothing else is configured, then the provider will return settings which are targeting Eclipse Kura for
 * all transport names requested.
 * </p>
 * <h2>Extra configuration</h2>
 * <p>
 * It is possible to provide a URI to the system using the configuration key {@link BrokerClientSettingKey#CONFIGURATION_URI}
 * which has to point to a standard Java properties file of the following format:
 * </p>
 * <code>
 * transports=foo,bar,baz
 * foo.device.APP=full.qualified.ClassName
 * foo.kapua.APP=full.qualified.ClassName
 * bar.device.APP=full.qualified.ClassName
 * bar.kapua.APP=full.qualified.ClassName
 * </code>
 * <p>
 * The property {@code transports} holds a comma separated list of all transports by name. For each transport it will
 * look up all keys of {@code<transport>.[device|kapua].<MessageType>}, where {@code MessageType} are all values of
 * the {@link MessageType} enum. The value of each of this key must be a full qualified class name implementing
 * {@link DeviceMessage} for the "device" sub-key and {@link KapuaMessage} for the "kapua" sub-key.
 *
 * <h2>Disabling the default fallback</h2>
 * <p>
 * By default this implementation will return first from the configured transport name and then always
 * return a hard coded default provider. If this default provided should not be returned it is possible
 * to disable this by using the settings key {@link BrokerClientSettingKey#DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR}.
 * </p>
 */
public class DefaultProtocolDescriptionProvider implements ProtocolDescriptorProvider {

    private static final Logger logger = LoggerFactory.getLogger(DefaultProtocolDescriptionProvider.class);

    public static final String DEFAULT_TRANSPORT_PROTOCOL = "MQTT";

    private final Map<String, ProtocolDescriptor> configuration = new HashMap<>();
    private final ProtocolDescriptor defaultDescriptor;

    public DefaultProtocolDescriptionProvider() {
        if (!BrokerClientSetting.getInstance().getBoolean(BrokerClientSettingKey.DISABLE_DEFAULT_PROTOCOL_DESCRIPTOR, false)) {
            defaultDescriptor = createDefaultDescriptor();
        } else {
            defaultDescriptor = null;
        }

        try {
            loadConfigurations(configuration);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProtocolDescriptor getDescriptor(String protocolName) {
        return configuration.getOrDefault(protocolName, defaultDescriptor);
    }

    private static void loadConfigurations(Map<String, ProtocolDescriptor> configuration) throws Exception {
        String uri = BrokerClientSetting.getInstance().getString(BrokerClientSettingKey.CONFIGURATION_URI, null);

        if (StringUtils.isEmpty(uri)) {
            return;
        }

        Properties p = new Properties();

        URL url = new URL(uri);
        try (InputStream in = url.openStream()) {
            p.load(in);
        }

        String transports = p.getProperty("transports", "");
        for (String transport : transports.split("\\s*,\\s*")) {
            if (!transport.isEmpty()) {
                ProtocolDescriptor desc = loadFromProperties(p, transport);
                configuration.put(transport, desc);
            }
        }
    }

    private static ProtocolDescriptor loadFromProperties(Properties p, String transport) throws ClassNotFoundException {

        Map<MessageType, Class<? extends DeviceMessage<?, ?>>> deviceClasses = new EnumMap<>(MessageType.class);
        Map<MessageType, Class<? extends KapuaMessage<?, ?>>> kapuaClasses = new EnumMap<>(MessageType.class);

        String transportProtocol = p.getProperty(String.format("%s.transport_protocol", transport));

        for (MessageType mt : MessageType.values()) {
                String key = String.format("%s.device.%s", transport, mt.name());
                String clazzName = p.getProperty(key);
                if (clazzName != null && !clazzName.isEmpty()) {
                    Class<? extends DeviceMessage<?, ?>> clazz = (Class<? extends DeviceMessage<?, ?>>) Class.forName(clazzName).asSubclass(DeviceMessage.class);
                    deviceClasses.put(mt, clazz);
                } else {
                    logger.info("No class mapping for key {}", key);
                }

                key = String.format("%s.kapua.%s", transport, mt.name());
                clazzName = p.getProperty(key);
                if (clazzName != null && !clazzName.isEmpty()) {
                    Class<? extends KapuaMessage<?, ?>> clazz = (Class<? extends KapuaMessage<?, ?>>) Class.forName(clazzName).asSubclass(KapuaMessage.class);
                    kapuaClasses.put(mt, clazz);
                } else {
                    logger.info("No class mapping for key {}", key);
                }
        }

        return new ProtocolDescriptor(transportProtocol, deviceClasses, kapuaClasses);
    }

    /**
     * Create the default {@link org.eclipse.kapua.service.device.call.kura.Kura} protocol description
     *
     * @return the default instance, never returns {@code null}
     */
    private static ProtocolDescriptor createDefaultDescriptor() {
        Map<MessageType, Class<? extends DeviceMessage<?, ?>>> deviceClass = new EnumMap<>(MessageType.class);
        deviceClass.put(MessageType.APP, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraAppsMessage.class);
        deviceClass.put(MessageType.BIRTH, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage.class);
        deviceClass.put(MessageType.DISCONNECT, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraDisconnectMessage.class);
        deviceClass.put(MessageType.MISSING, org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraMissingMessage.class);
        deviceClass.put(MessageType.NOTIFY, KuraNotifyMessage.class);
        deviceClass.put(MessageType.DATA, org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage.class);

        Map<MessageType, Class<? extends KapuaMessage<?, ?>>> kapuaClass = new EnumMap<>(MessageType.class);
        kapuaClass.put(MessageType.APP, org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage.class);
        kapuaClass.put(MessageType.BIRTH, org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage.class);
        kapuaClass.put(MessageType.DISCONNECT, org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage.class);
        kapuaClass.put(MessageType.MISSING, org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage.class);
        kapuaClass.put(MessageType.NOTIFY, KapuaNotifyMessage.class);
        kapuaClass.put(MessageType.DATA, org.eclipse.kapua.message.device.data.KapuaDataMessage.class);

        return new ProtocolDescriptor(DEFAULT_TRANSPORT_PROTOCOL, deviceClass, kapuaClass);
    }

}
