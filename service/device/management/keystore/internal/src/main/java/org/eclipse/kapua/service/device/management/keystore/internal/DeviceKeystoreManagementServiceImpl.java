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
package org.eclipse.kapua.service.device.management.keystore.internal;

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.KapuaIllegalArgumentException;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.info.CertificateInfoFactory;
import org.eclipse.kapua.service.certificate.info.CertificateInfoService;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.management.commons.AbstractDeviceManagementServiceImpl;
import org.eclipse.kapua.service.device.management.commons.call.DeviceCallExecutor;
import org.eclipse.kapua.service.device.management.exception.DeviceManagementRequestContentException;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementService;
import org.eclipse.kapua.service.device.management.keystore.internal.message.request.KeystoreCertificateRequestMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.request.KeystoreCsrRequestMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.request.KeystoreKeypairRequestMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.request.KeystoreQueryRequestMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.request.KeystoreRequestChannel;
import org.eclipse.kapua.service.device.management.keystore.internal.message.request.KeystoreRequestPayload;
import org.eclipse.kapua.service.device.management.keystore.internal.message.response.KeystoreCsrResponseMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.response.KeystoreItemResponseMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.response.KeystoreItemsResponseMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.response.KeystoreNoContentResponseMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.response.KeystoreResponseMessage;
import org.eclipse.kapua.service.device.management.keystore.internal.message.response.KeystoresResponseMessage;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSR;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCSRInfo;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItem;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItemQuery;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreItems;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreKeypair;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystores;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreCertificateImpl;
import org.eclipse.kapua.service.device.management.keystore.model.internal.DeviceKeystoreItemQueryImpl;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Date;

/**
 * {@link DeviceKeystoreManagementService} implementation.
 *
 * @since 1.5.0
 */
@Singleton
public class DeviceKeystoreManagementServiceImpl extends AbstractDeviceManagementServiceImpl implements DeviceKeystoreManagementService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceKeystoreManagementServiceImpl.class);

    private static final String SCOPE_ID = "scopeId";
    private static final String DEVICE_ID = "deviceId";

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();

    private static final CertificateInfoService CERTIFICATE_INFO_SERVICE = LOCATOR.getService(CertificateInfoService.class);
    private static final CertificateInfoFactory CERTIFICATE_INFO_FACTORY = LOCATOR.getFactory(CertificateInfoFactory.class);

    @Override
    public DeviceKeystores getKeystores(KapuaId scopeId, KapuaId deviceId, Long timeout)
            throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.READ);

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();

        KeystoreQueryRequestMessage keystoreRequestMessage = new KeystoreQueryRequestMessage() {
            @Override
            public Class<KeystoresResponseMessage> getResponseClass() {
                return KeystoresResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getKeystores());
    }

    @Override
    public DeviceKeystoreItems getKeystoreItems(KapuaId scopeId, KapuaId deviceId, Long timeout) throws KapuaException {
        return getKeystoreItems(scopeId, deviceId, new DeviceKeystoreItemQueryImpl(), timeout);
    }

    @Override
    public DeviceKeystoreItems getKeystoreItems(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreItemQuery itemQuery, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(itemQuery, "itemQuery");

        // Only one of the filter can be specified.
        if (itemQuery.hasFilters() &&
                !Strings.isNullOrEmpty(itemQuery.getKeystoreId()) &&
                !Strings.isNullOrEmpty(itemQuery.getAlias())
        ) {
            throw new KapuaIllegalArgumentException("itemQuery.alias", itemQuery.getAlias());
        }

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.READ);
        keystoreRequestChannel.setResource("items");

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();
        try {
            keystoreRequestPayload.setItemQuery(itemQuery);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, itemQuery);
        }

        KeystoreQueryRequestMessage keystoreRequestMessage = new KeystoreQueryRequestMessage() {
            @Override
            public Class<KeystoreItemsResponseMessage> getResponseClass() {
                return KeystoreItemsResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getKeystoreItems());
    }

    @Override
    public DeviceKeystoreItem getKeystoreItem(KapuaId scopeId, KapuaId deviceId, String keystoreId, String alias, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notEmptyOrNull(keystoreId, "keystoreId");
        ArgumentValidator.notEmptyOrNull(alias, "alias");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.READ);
        keystoreRequestChannel.setResource("item");

        // Build query
        DeviceKeystoreItemQuery itemQuery = new DeviceKeystoreItemQueryImpl();
        itemQuery.setKeystoreId(keystoreId);
        itemQuery.setAlias(alias);

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();
        try {
            keystoreRequestPayload.setItemQuery(itemQuery);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, itemQuery);
        }

        KeystoreQueryRequestMessage keystoreRequestMessage = new KeystoreQueryRequestMessage() {
            @Override
            public Class<KeystoreItemResponseMessage> getResponseClass() {
                return KeystoreItemResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getKeystoreItem());
    }

    @Override
    public void createKeystoreCertificate(KapuaId scopeId, KapuaId deviceId, String keystoreId, String alias, KapuaId certificateId, Long timeout) throws KapuaException {
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notEmptyOrNull(keystoreId, "keystoreId");
        ArgumentValidator.notEmptyOrNull(alias, "alias");
        ArgumentValidator.notNull(certificateId, "certificateId");

        //
        // Check Certificate Info existence
        CertificateInfo certificateInfo;
        try {
            certificateInfo = CERTIFICATE_INFO_SERVICE.find(scopeId, certificateId);
        } catch (UnsupportedOperationException e) {
            LOG.warn("Unable to get the certificate {} since the implementation does not support CertificateInfoService.find(scopeId, certificateId)... Returning KapuaEntityNotFoundException!", certificateId);
            throw new KapuaEntityNotFoundException(CertificateInfo.TYPE, certificateId);
        }

        if (certificateInfo == null) {
            throw new KapuaEntityNotFoundException(CertificateInfo.TYPE, certificateId);
        }

        //
        // Build DeviceKeystoreCertificate create
        DeviceKeystoreCertificate deviceKeystoreCertificate = new DeviceKeystoreCertificateImpl();
        deviceKeystoreCertificate.setKeystoreId(keystoreId);
        deviceKeystoreCertificate.setAlias(alias);
        deviceKeystoreCertificate.setCertificate(certificateInfo.getCertificate());

        createKeystoreCertificate(scopeId, deviceId, deviceKeystoreCertificate, timeout);
    }

    @Override
    public void createKeystoreCertificate(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreCertificate keystoreCertificate, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(keystoreCertificate, "keystoreCertificate");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.CREATE);
        keystoreRequestChannel.setResource("certificate");

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();
        try {
            keystoreRequestPayload.setCertificate(keystoreCertificate);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, keystoreCertificate);
        }

        KeystoreCertificateRequestMessage keystoreRequestMessage = new KeystoreCertificateRequestMessage() {
            @Override
            public Class<KeystoreNoContentResponseMessage> getResponseClass() {
                return KeystoreNoContentResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }

    @Override
    public void createKeystoreKeypair(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreKeypair keystoreKeypair, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(keystoreKeypair, "keystoreKeypair");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.CREATE);
        keystoreRequestChannel.setResource("keypair");

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();
        try {
            keystoreRequestPayload.setKeypair(keystoreKeypair);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, keystoreKeypair);
        }

        KeystoreKeypairRequestMessage keystoreRequestMessage = new KeystoreKeypairRequestMessage() {
            @Override
            public Class<KeystoreNoContentResponseMessage> getResponseClass() {
                return KeystoreNoContentResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }

    @Override
    public DeviceKeystoreCSR createKeystoreCSR(KapuaId scopeId, KapuaId deviceId, DeviceKeystoreCSRInfo keystoreCSRInfo, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notNull(keystoreCSRInfo, "keystoreCSRInfo");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.read, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.CREATE);
        keystoreRequestChannel.setResource("csr");

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();
        try {
            keystoreRequestPayload.setCsrInfo(keystoreCSRInfo);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, keystoreCSRInfo);
        }

        KeystoreCsrRequestMessage keystoreRequestMessage = new KeystoreCsrRequestMessage() {
            @Override
            public Class<KeystoreCsrResponseMessage> getResponseClass() {
                return KeystoreCsrResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        return checkResponseAcceptedOrThrowError(responseMessage, () -> responseMessage.getPayload().getCSR());
    }

    @Override
    public void deleteKeystoreItem(KapuaId scopeId, KapuaId deviceId, String keystoreId, String alias, Long timeout) throws KapuaException {
        //
        // Argument Validation
        ArgumentValidator.notNull(scopeId, SCOPE_ID);
        ArgumentValidator.notNull(deviceId, DEVICE_ID);
        ArgumentValidator.notEmptyOrNull(keystoreId, "keystoreId");
        ArgumentValidator.notEmptyOrNull(alias, "alias");

        //
        // Check Access
        AUTHORIZATION_SERVICE.checkPermission(PERMISSION_FACTORY.newPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.delete, scopeId));

        //
        // Prepare the request
        KeystoreRequestChannel keystoreRequestChannel = new KeystoreRequestChannel();
        keystoreRequestChannel.setAppName(DeviceKeystoreAppProperties.APP_NAME);
        keystoreRequestChannel.setVersion(DeviceKeystoreAppProperties.APP_VERSION);
        keystoreRequestChannel.setMethod(KapuaMethod.DELETE);
        keystoreRequestChannel.setResource("items");

        // Build query
        DeviceKeystoreItemQuery itemQuery = new DeviceKeystoreItemQueryImpl();
        itemQuery.setKeystoreId(keystoreId);
        itemQuery.setAlias(alias);

        KeystoreRequestPayload keystoreRequestPayload = new KeystoreRequestPayload();
        try {
            keystoreRequestPayload.setItemQuery(itemQuery);
        } catch (Exception e) {
            throw new DeviceManagementRequestContentException(e, itemQuery);
        }

        KeystoreQueryRequestMessage keystoreRequestMessage = new KeystoreQueryRequestMessage() {
            @Override
            public Class<KeystoreNoContentResponseMessage> getResponseClass() {
                return KeystoreNoContentResponseMessage.class;
            }
        };

        keystoreRequestMessage.setScopeId(scopeId);
        keystoreRequestMessage.setDeviceId(deviceId);
        keystoreRequestMessage.setCapturedOn(new Date());
        keystoreRequestMessage.setPayload(keystoreRequestPayload);
        keystoreRequestMessage.setChannel(keystoreRequestChannel);

        //
        // Do get
        DeviceCallExecutor<?, ?, ?, KeystoreResponseMessage> deviceApplicationCall = new DeviceCallExecutor<>(keystoreRequestMessage, timeout);
        KeystoreResponseMessage responseMessage = deviceApplicationCall.send();

        //
        // Create event
        createDeviceEvent(scopeId, deviceId, keystoreRequestMessage, responseMessage);

        //
        // Check response
        checkResponseAcceptedOrThrowError(responseMessage);
    }
}
