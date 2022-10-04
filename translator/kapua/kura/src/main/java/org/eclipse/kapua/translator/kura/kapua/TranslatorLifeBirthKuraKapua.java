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
package org.eclipse.kapua.translator.kura.kapua;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthChannelImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthMessageImpl;
import org.eclipse.kapua.message.internal.device.lifecycle.KapuaBirthPayloadImpl;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthChannel;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthMessage;
import org.eclipse.kapua.service.device.call.message.kura.lifecycle.KuraBirthPayload;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.exception.InvalidChannelException;
import org.eclipse.kapua.translator.exception.InvalidMessageException;
import org.eclipse.kapua.translator.exception.InvalidPayloadException;
import org.eclipse.kapua.translator.exception.TranslateException;

/**
 * {@link Translator} implementation from {@link KuraBirthMessage} to {@link KapuaBirthMessage}
 *
 * @since 1.0.0
 */
public class TranslatorLifeBirthKuraKapua extends Translator<KuraBirthMessage, KapuaBirthMessage> {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final AccountService ACCOUNT_SERVICE = LOCATOR.getService(AccountService.class);
    private static final DeviceRegistryService DEVICE_REGISTRY_SERVICE = LOCATOR.getService(DeviceRegistryService.class);

    @Override
    public KapuaBirthMessage translate(KuraBirthMessage kuraBirthMessage) throws TranslateException {
        try {
            KapuaBirthMessage kapuaBirthMessage = new KapuaBirthMessageImpl();
            kapuaBirthMessage.setChannel(translate(kuraBirthMessage.getChannel()));
            kapuaBirthMessage.setPayload(translate(kuraBirthMessage.getPayload()));

            Account account = ACCOUNT_SERVICE.findByName(kuraBirthMessage.getChannel().getScope());
            if (account == null) {
                throw new KapuaEntityNotFoundException(Account.TYPE, kuraBirthMessage.getChannel().getScope());
            }
            kapuaBirthMessage.setScopeId(account.getId());

            Device device = DEVICE_REGISTRY_SERVICE.findByClientId(account.getId(), kuraBirthMessage.getChannel().getClientId());
            if (device != null) {
                kapuaBirthMessage.setDeviceId(device.getId());
            } else {
                kapuaBirthMessage.setClientId(kuraBirthMessage.getChannel().getClientId());
            }

            kapuaBirthMessage.setCapturedOn(kuraBirthMessage.getPayload().getTimestamp());
            kapuaBirthMessage.setSentOn(kuraBirthMessage.getPayload().getTimestamp());
            kapuaBirthMessage.setReceivedOn(kuraBirthMessage.getTimestamp());
            kapuaBirthMessage.setPosition(TranslatorKuraKapuaUtils.translate(kuraBirthMessage.getPayload().getPosition()));

            return kapuaBirthMessage;
        } catch (InvalidChannelException | InvalidPayloadException te) {
            throw te;
        } catch (Exception e) {
            throw new InvalidMessageException(e, kuraBirthMessage);
        }
    }

    private KapuaBirthChannel translate(KuraBirthChannel kuraBirthChannel) {
        KapuaBirthChannel kapuaBirthChannel = new KapuaBirthChannelImpl();
        kapuaBirthChannel.setClientId(kuraBirthChannel.getClientId());
        return kapuaBirthChannel;
    }

    private KapuaBirthPayload translate(KuraBirthPayload kuraBirthPayload) {
        return new KapuaBirthPayloadImpl(
                kuraBirthPayload.getUptime(),
                kuraBirthPayload.getDisplayName(),
                kuraBirthPayload.getModelName(),
                kuraBirthPayload.getModelId(),
                kuraBirthPayload.getPartNumber(),
                kuraBirthPayload.getSerialNumber(),
                kuraBirthPayload.getFirmware(),
                kuraBirthPayload.getFirmwareVersion(),
                kuraBirthPayload.getBios(),
                kuraBirthPayload.getBiosVersion(),
                kuraBirthPayload.getOs(),
                kuraBirthPayload.getOsVersion(),
                kuraBirthPayload.getJvm(),
                kuraBirthPayload.getJvmVersion(),
                kuraBirthPayload.getJvmProfile(),
                kuraBirthPayload.getContainerFramework(),
                kuraBirthPayload.getContainerFrameworkVersion(),
                kuraBirthPayload.getApplicationFramework(),
                kuraBirthPayload.getApplicationFrameworkVersion(),
                kuraBirthPayload.getConnectionInterface(),
                kuraBirthPayload.getConnectionIp(),
                kuraBirthPayload.getAcceptEncoding(),
                kuraBirthPayload.getApplicationIdentifiers(),
                kuraBirthPayload.getAvailableProcessors(),
                kuraBirthPayload.getTotalMemory(),
                kuraBirthPayload.getOsArch(),
                kuraBirthPayload.getModemImei(),
                kuraBirthPayload.getModemImsi(),
                kuraBirthPayload.getModemIccid(),
                kuraBirthPayload.getExtendedProperties());
    }

    @Override
    public Class<KuraBirthMessage> getClassFrom() {
        return KuraBirthMessage.class;
    }

    @Override
    public Class<KapuaBirthMessage> getClassTo() {
        return KapuaBirthMessage.class;
    }

}
