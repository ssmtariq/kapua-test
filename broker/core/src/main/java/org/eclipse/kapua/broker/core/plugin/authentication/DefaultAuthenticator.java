/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.broker.core.plugin.authentication;

import com.codahale.metrics.Timer.Context;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.core.plugin.KapuaSecurityContext;
import org.eclipse.kapua.broker.core.plugin.metric.ClientMetric;
import org.eclipse.kapua.broker.core.plugin.metric.LoginMetric;
import org.eclipse.kapua.broker.client.message.MessageConstants;
import org.eclipse.kapua.broker.client.pool.JmsAssistantProducerPool;
import org.eclipse.kapua.broker.client.pool.JmsAssistantProducerPool.DESTINATIONS;
import org.eclipse.kapua.broker.client.pool.JmsAssistantProducerWrapper;
import org.eclipse.kapua.broker.core.setting.BrokerSetting;
import org.eclipse.kapua.broker.core.setting.BrokerSettingKey;
import org.eclipse.kapua.commons.setting.system.SystemSetting;
import org.eclipse.kapua.commons.setting.system.SystemSettingKey;
import org.eclipse.kapua.commons.util.ClassUtil;
import org.eclipse.kapua.consumer.commons.message.system.DefaultSystemMessageCreator;
import org.eclipse.kapua.consumer.commons.message.system.SystemMessageCreator;
import org.eclipse.kapua.consumer.commons.message.system.SystemMessageCreator.Fields;
import org.eclipse.kapua.consumer.commons.message.system.SystemMessageCreator.SystemMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default authenticator implementation
 *
 * @since 1.0
 */
public class DefaultAuthenticator implements Authenticator {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultAuthenticator.class);

    private static final String SYSTEM_MESSAGE_CREATOR_CLASS_NAME;
    private static final String ADMIN_USERNAME;
    private static final String ADMIN_AUTHENTICATION_LOGIC_CLASS_NAME;
    private static final String USER_AUTHENTICATION_LOGIC_CLASS_NAME;

    static {
        SYSTEM_MESSAGE_CREATOR_CLASS_NAME = BrokerSetting.getInstance().getString(BrokerSettingKey.SYSTEM_MESSAGE_CREATOR_CLASS_NAME);
        ADMIN_USERNAME = SystemSetting.getInstance().getString(SystemSettingKey.SYS_ADMIN_USERNAME);
        ADMIN_AUTHENTICATION_LOGIC_CLASS_NAME = BrokerSetting.getInstance().getString(BrokerSettingKey.ADMIN_AUTHENTICATION_LOGIC_CLASS_NAME);
        USER_AUTHENTICATION_LOGIC_CLASS_NAME = BrokerSetting.getInstance().getString(BrokerSettingKey.USER_AUTHENTICATION_LOGIC_CLASS_NAME);
    }

    private Map<String, Object> options;
    private ClientMetric clientMetric = ClientMetric.getInstance();
    private LoginMetric loginMetric = LoginMetric.getInstance();

    protected AdminAuthenticationLogic adminAuthenticationLogic;
    protected UserAuthenticationLogic userAuthenticationLogic;

    protected SystemMessageCreator systemMessageCreator;

    /**
     * Default constructor
     *
     * @param options thread safe options used to customize the authenticator behavior (please don't change the signature since the class is instantiated by reflection)
     * @throws KapuaException
     */
    public DefaultAuthenticator(Map<String, Object> options) throws KapuaException {
        this.options = options;
        logger.info(">>> Security broker filter: calling start... Instantiating admin authentication logic {} (fallback: {}", ADMIN_AUTHENTICATION_LOGIC_CLASS_NAME, AdminAuthenticationLogic.class);
        adminAuthenticationLogic = ClassUtil.newInstance(
                ADMIN_AUTHENTICATION_LOGIC_CLASS_NAME,
                AdminAuthenticationLogic.class,
                new Class[] {Map.class},
                new Object[] {options});
        logger.info(">>> Security broker filter: calling start... Instantiating user authentication logic {} (fallback: {}", USER_AUTHENTICATION_LOGIC_CLASS_NAME, UserAuthenticationLogic.class);
        userAuthenticationLogic = ClassUtil.newInstance(
                USER_AUTHENTICATION_LOGIC_CLASS_NAME,
                UserAuthenticationLogic.class,
                new Class[] {Map.class},
                new Object[] {options});
        logger.info(">>> Security broker filter: calling start... Initialize system message creator");
        systemMessageCreator = ClassUtil.newInstance(SYSTEM_MESSAGE_CREATOR_CLASS_NAME, DefaultSystemMessageCreator.class);
    }

    @Override
    public List<AuthorizationEntry> connect(KapuaSecurityContext kapuaSecurityContext)
            throws KapuaException {
        List<AuthorizationEntry> authorizationEntries = null;
        if (isAdminUser(kapuaSecurityContext)) {
            loginMetric.getKapuasysTokenAttempt().inc();
            authorizationEntries = adminAuthenticationLogic.connect(kapuaSecurityContext);
            clientMetric.getConnectedKapuasys().inc();
        }
        else {
            loginMetric.getNormalUserAttempt().inc();
            authorizationEntries = userAuthenticationLogic.connect(kapuaSecurityContext);
            clientMetric.getConnectedClient().inc();
            sendConnectMessage(kapuaSecurityContext);
        }
        return authorizationEntries;
    }

    @Override
    public void disconnect(KapuaSecurityContext kapuaSecurityContext, Throwable error) {
        if (isAdminUser(kapuaSecurityContext)) {
            clientMetric.getDisconnectedKapuasys().inc();
            adminAuthenticationLogic.disconnect(kapuaSecurityContext, error);
        }
        else {
            clientMetric.getDisconnectedClient().inc();
            if (userAuthenticationLogic.disconnect(kapuaSecurityContext, error)) {
                sendDisconnectMessage(kapuaSecurityContext);
            }
        }
    }

    @Override
    public void sendConnectMessage(KapuaSecurityContext kapuaSecurityContext) {
        sendMessage(kapuaSecurityContext, Authenticator.ADDRESS_CONNECT_PATTERN_KEY, SystemMessageType.CONNECT);
    }

    @Override
    public void sendDisconnectMessage(KapuaSecurityContext kapuaSecurityContext) {
        sendMessage(kapuaSecurityContext, Authenticator.ADDRESS_DISCONNECT_PATTERN_KEY, SystemMessageType.DISCONNECT);
    }

    private void sendMessage(KapuaSecurityContext kapuaSecurityContext, String messageAddressPattern, SystemMessageType systemMessageType) {
        if (systemMessageType != null) {
            Context loginSendLogingUpdateMsgTimeContex = loginMetric.getSendLoginUpdateMsgTime().time();
            String message = systemMessageCreator.createMessage(systemMessageType, buildMessageParametersFromSecurityContext(kapuaSecurityContext));
            JmsAssistantProducerWrapper producerWrapper = null;
            try {
                producerWrapper = JmsAssistantProducerPool.getIOnstance(DESTINATIONS.NO_DESTINATION).borrowObject();
                producerWrapper.send(String.format((String) options.get(messageAddressPattern),
                        SystemSetting.getInstance().getMessageClassifier(), kapuaSecurityContext.getAccountName(), kapuaSecurityContext.getClientId()),
                        message,
                        buildContextParametersFromSecurityContext(kapuaSecurityContext));
            } catch (Exception e) {
                logger.error("Exception sending the {} message: {}", systemMessageType.name().toLowerCase(), e.getMessage(), e);
            } finally {
                if (producerWrapper != null) {
                    JmsAssistantProducerPool.getIOnstance(DESTINATIONS.NO_DESTINATION).returnObject(producerWrapper);
                }
            }
            loginSendLogingUpdateMsgTimeContex.stop();
        }
        else {
            logger.warn("Cannot send system message for address pattern {} since the system message type is null!", messageAddressPattern);
        }
    }

    protected boolean isAdminUser(KapuaSecurityContext kapuaSecurityContext) {
        return kapuaSecurityContext.getUserName().equals(ADMIN_USERNAME);
    }

    protected Map<Fields, String> buildMessageParametersFromSecurityContext(KapuaSecurityContext kapuaSecurityContext) {
        Map<Fields, String> parameters = new HashMap<>();
        parameters.put(Fields.clientId, kapuaSecurityContext.getClientId());
        parameters.put(Fields.username, kapuaSecurityContext.getUserName());
        return parameters;
    }

    protected Map<String, String> buildContextParametersFromSecurityContext(KapuaSecurityContext kapuaSecurityContext) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(MessageConstants.PROPERTY_BROKER_ID, kapuaSecurityContext.getBrokerId());
        parameters.put(MessageConstants.PROPERTY_CLIENT_ID, kapuaSecurityContext.getClientId());
        parameters.put(MessageConstants.PROPERTY_SCOPE_ID, String.valueOf(kapuaSecurityContext.getScopeIdAsLong()));
        return parameters;
    }
}
