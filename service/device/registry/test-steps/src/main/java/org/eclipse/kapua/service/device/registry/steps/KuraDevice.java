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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import com.google.protobuf.InvalidProtocolBufferException;
import org.eclipse.kapua.qa.common.Suppressed;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.request.KuraRequestPayload;
import org.eclipse.kura.core.message.protobuf.KuraPayloadProto;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class KuraDevice implements MqttCallback {

    private static final Logger LOG = LoggerFactory.getLogger(KuraDevice.class);

    private static final String JOB_ID = "job.id";
    private static final String CLIENT_ID = "client.id";

    private static final String $EDC = "$EDC/";
    private static final String $EDC_KAPUA_SYS = "$EDC/kapua-sys/";
    private static final String DEPLOY_V2_REPLY = "/DEPLOY-V2/REPLY/";
    private static final String DEPLOY_V2_NOTIFY = "/DEPLOY-V2/NOTIFY/";
    private static final String INVENTORY_V1_REPLY = "/INVENTORY-V1/REPLY/";
    private static final String KEYS_V1_REPLY = "/KEYS-V1/REPLY/";

    private static final String COMPLETED = "COMPLETED";

    //
    // Topics that Kura device is listening to.
    //
    private String assetExecRead;
    private String assetExecWrite;
    private String cmdExecCommand;
    private String confGetConfigurations;
    private String confPutConfigurations;
    private String deployGetPackages;
    private String deployExecUninstall;
    private String deployExecDownload;
    private String deployGetBundles;
    private String deployExecStart34;
    private String deployExecStart95;
    private String deployExecStop77;

    // INVENTORY-V1
    private String inventoryStartWithTopic;
    private String inventoryGetInventoryEntries;
    private String inventoryGetInventoryBundleEntries;
    private String inventoryExecInventoryBundleStart;
    private String inventoryExecInventoryBundleStop;
    private String inventoryGetInventoryContainerEntries;
    private String inventoryExecInventoryContainerStart;
    private String inventoryExecInventoryContainerStop;
    private String inventoryGetInventorySystemPackagesEntries;
    private String inventoryGetInventoryPackagesEntries;

    // KEYS-V1
    private String keystoreStartWithTopic;
    private String keystoreGetKeystores;
    private String keystoreGetKeystoresEntries;
    private String keystoreGetKeystoresEntry;
    private String keystorePostKeystoresEntriesCertificate;
    private String keystorePostKeystoresEntriesKeypair;
    private String keystorePostKeystoresEntriesCsr;
    private String keystoreDelKeystoresEntries;

    /**
     * URI of mqtt broker.
     */
    private static final String BROKER_URI = "tcp://localhost:1883";

    /**
     * Mocked Kura device client id.
     */
    private static final String CLIENT_ID_RPIONE3 = "rpione3";

    /**
     * User with which Mocked Kura device is connecting to Cloud service.
     */
    private static final String CLIENT_USER = "kapua-broker";

    /**
     * Mocked Kura device password while connecting to Cloud service.
     */
    private static final String CLIENT_PASSWORD = "kapua-password";

    /**
     * User under which Kura device is listening for messages.
     */
    private static final String SERVER_USER = "kapua-sys";

    /**
     * Password for system user under which Kura is listening form messages.
     */
    private static final String SERVER_PASSWORD = "kapua-password";

    /**
     * Account under which Kura device is registered.
     */
    private static final String CLIENT_ACCOUNT = "kapua-sys";

    /**
     * Kapua system topic and everything under it as mqtt filter.
     */
    private static final String TOPIC_FILTER = "$EDC/#";

    /**
     * Default quality of service - mqtt.
     */
    private static final int QOS = 0;

    /**
     * Mqtt client for sending messages form Mocked Kura device.
     */
    private MqttClient mqttClient;

    /**
     * Mqtt client form listening from messages on Mocked Kura device.
     */
    private MqttClient subscribedClient;
    private String clientId;

    public boolean bundleStateChanged;
    public boolean configurationChanged;
    public boolean packageListChanged;
    public boolean packageListChangedAfterUninstall;
    public boolean assetStateChanged;

    public boolean inventoryBundleStarted;
    public boolean inventoryBundleStopped;
    public boolean inventoryContainerStarted;
    public boolean inventoryContainerStopped;

    public int keystoreInstalledCertificate;
    public int keystoreInstalledKeypair;

    public KuraDevice() {
        // ASSET-V1
        assetExecRead = "$EDC/kapua-sys/rpione3/ASSET-V1/EXEC/read";
        assetExecWrite = "$EDC/kapua-sys/rpione3/ASSET-V1/EXEC/write";

        // CMD-V1
        cmdExecCommand = "$EDC/kapua-sys/rpione3/CMD-V1/EXEC/command";

        // CONF-V1
        confGetConfigurations = "$EDC/kapua-sys/rpione3/CONF-V1/GET/configurations";
        confPutConfigurations = "$EDC/kapua-sys/rpione3/CONF-V1/PUT/configurations";

        // DEPLOY-V2
        deployGetPackages = "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/packages";
        deployExecDownload = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/download";
        deployExecUninstall = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/uninstall";
        deployGetBundles = "$EDC/kapua-sys/rpione3/DEPLOY-V2/GET/bundles";
        deployExecStart34 = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/start/34";
        deployExecStart95 = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/start/95";
        deployExecStop77 = "$EDC/kapua-sys/rpione3/DEPLOY-V2/EXEC/stop/77";

        // INVENTORY-V1
        inventoryStartWithTopic = "$EDC/kapua-sys/rpione3/INVENTORY-V1/";
        inventoryGetInventoryEntries = "$EDC/kapua-sys/rpione3/INVENTORY-V1/GET/inventory";
        inventoryGetInventoryBundleEntries = "$EDC/kapua-sys/rpione3/INVENTORY-V1/GET/bundles";
        inventoryExecInventoryBundleStart = "$EDC/kapua-sys/rpione3/INVENTORY-V1/EXEC/bundles/_start";
        inventoryExecInventoryBundleStop = "$EDC/kapua-sys/rpione3/INVENTORY-V1/EXEC/bundles/_stop";
        inventoryGetInventoryContainerEntries = "$EDC/kapua-sys/rpione3/INVENTORY-V1/GET/containers";
        inventoryExecInventoryContainerStart = "$EDC/kapua-sys/rpione3/INVENTORY-V1/EXEC/containers/_start";
        inventoryExecInventoryContainerStop = "$EDC/kapua-sys/rpione3/INVENTORY-V1/EXEC/containers/_stop";
        inventoryGetInventorySystemPackagesEntries = "$EDC/kapua-sys/rpione3/INVENTORY-V1/GET/systemPackages";
        inventoryGetInventoryPackagesEntries = "$EDC/kapua-sys/rpione3/INVENTORY-V1/GET/deploymentPackages";

        // KEYS-V1
        keystoreStartWithTopic = "$EDC/kapua-sys/rpione3/KEYS-V1/";
        keystoreGetKeystores = "$EDC/kapua-sys/rpione3/KEYS-V1/GET/keystores";
        keystoreGetKeystoresEntries = "$EDC/kapua-sys/rpione3/KEYS-V1/GET/keystores/entries";
        keystoreGetKeystoresEntry = "$EDC/kapua-sys/rpione3/KEYS-V1/GET/keystores/entries/entry";
        keystorePostKeystoresEntriesCertificate = "$EDC/kapua-sys/rpione3/KEYS-V1/POST/keystores/entries/certificate";
        keystorePostKeystoresEntriesKeypair = "$EDC/kapua-sys/rpione3/KEYS-V1/POST/keystores/entries/keypair";
        keystorePostKeystoresEntriesCsr = "$EDC/kapua-sys/rpione3/KEYS-V1/POST/keystores/entries/csr";
        keystoreDelKeystoresEntries = "$EDC/kapua-sys/rpione3/KEYS-V1/DEL/keystores/entries";

        clientId = CLIENT_ID_RPIONE3;

        mqttClientSetup();
    }

    public String getClientId() {
        return this.clientId;
    }

    public void addMoreThanOneDeviceToKuraMock(String name) {
        clientId = name;
        mqttClientSetupForMoreDevices();
    }

    /**
     * Disconnect Mocked Kura device mqtt clients that listen and send messages
     * to mqtt broker.
     */
    public void mqttClientDisconnect() {
        try {
            try (Suppressed<Exception> s = Suppressed.withException()) {
                s.run(mqttClient::disconnect);
                s.run(subscribedClient::disconnect);
                s.run(mqttClient::close);
                s.run(subscribedClient::close);
            }
        } catch (Exception e) {
            //suppress stacktrace
            LOG.warn("Failed during cleanup of Paho resources. Error: {}", e.getMessage());
        }
    }

    /**
     * Connect both listening and sending mqtt client of Mocked Kura device.
     */
    public void mqttClientConnect() {
        MqttConnectOptions clientOpts = new MqttConnectOptions();
        clientOpts.setUserName(CLIENT_USER);
        clientOpts.setPassword(CLIENT_PASSWORD.toCharArray());

        MqttConnectOptions serverOpts = new MqttConnectOptions();
        serverOpts.setUserName(SERVER_USER);
        serverOpts.setPassword(SERVER_PASSWORD.toCharArray());

        try {
            mqttClient.connect(clientOpts);
            subscribedClient.connect(serverOpts);
            subscribedClient.subscribe(TOPIC_FILTER, QOS);
        } catch (MqttException me) {
            LOG.error("Error while connecting KuraDevice!", me);
        }
    }

    /**
     * Prepare client and server part of mocked mqtt.
     * <p>
     * MqttClient is meant to simulate Kura device for sending messages,
     * while subscribedClient is meant to receive messages from Kura device side.
     */
    public void mqttClientSetup() {
        try {
            mqttClient = new MqttClient(BROKER_URI, CLIENT_ID_RPIONE3, new MemoryPersistence());
            subscribedClient = new MqttClient(BROKER_URI, MqttClient.generateClientId(), new MemoryPersistence());
        } catch (MqttException me) {
            LOG.error("Error while setting up KuraDevice!", me);
        }

        subscribedClient.setCallback(this);
    }

    /**
     * MqttClient is meant to simulate Kura device for sending messages,
     * while subscribedClient is meant to receive messages from Kura device side.
     */
    private void mqttClientSetupForMoreDevices() {
        try {
            deployGetPackages = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/GET/packages";
            deployExecDownload = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/EXEC/download";
            deployExecUninstall = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/EXEC/uninstall";
            deployGetBundles = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/GET/bundles";
            confGetConfigurations = $EDC_KAPUA_SYS + clientId + "/CONF-V1/GET/configurations";
            confPutConfigurations = $EDC_KAPUA_SYS + clientId + "/CONF-V1/PUT/configurations";
            cmdExecCommand = $EDC_KAPUA_SYS + clientId + "/CMD-V1/EXEC/command";
            deployExecStart34 = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/EXEC/start/34";
            deployExecStart95 = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/EXEC/start/95";
            deployExecStop77 = $EDC_KAPUA_SYS + clientId + "/DEPLOY-V2/EXEC/stop/77";

            assetExecRead = $EDC_KAPUA_SYS + clientId + "/ASSET-V1/EXEC/read";

            assetExecWrite = $EDC_KAPUA_SYS + clientId + "/ASSET-V1/EXEC/write";

            mqttClient = new MqttClient(BROKER_URI, clientId, new MemoryPersistence());
            subscribedClient = new MqttClient(BROKER_URI, MqttClient.generateClientId(), new MemoryPersistence());
        } catch (MqttException me) {
            LOG.error("Error while setting up KuraDevice!", me);
        }
        subscribedClient.setCallback(this);
    }

    /**
     * Sending data to mqtt broker. Data is read form file containing pre-recorded response.
     *
     * @param topic    mqtt broker topic
     * @param qos      mqtt QOS
     * @param retained is message retained (mqtt specific)
     * @param fileName name of file and path with pre-recorded response
     * @throws MqttException
     * @throws IOException
     */
    public void sendMessageFromFile(String topic, int qos, boolean retained, String fileName) throws MqttException, IOException, URISyntaxException {
        byte[] payload = Files.readAllBytes(Paths.get(getClass().getResource(fileName).toURI()));

        mqttClient.publish(topic, payload, qos, retained);
    }

    /**
     * Extraction of metrics form Kapua message payload.
     *
     * @param payload   payload received from Kapua
     * @param metricKey string representing key of metric
     * @return string representation of metric value
     */
    private String getMetric(byte[] payload, String metricKey) {

        String value = null;
        KuraPayloadProto.KuraPayload kuraPayload = null;
        try {
            kuraPayload = KuraPayloadProto.KuraPayload.parseFrom(payload);
        } catch (InvalidProtocolBufferException e) {
            value = null;
        }

        if (kuraPayload == null) {
            return value;
        }

        List<KuraPayloadProto.KuraPayload.KuraMetric> metrics = kuraPayload.getMetricList();
        for (KuraPayloadProto.KuraPayload.KuraMetric metric : metrics) {
            String name = metric.getName();
            if (name.equals(metricKey)) {
                value = metric.getStringValue();
            }
        }

        return value;
    }

    /**
     * Ectraction of callback parameters form Kapua generated message stored as Metrics.
     *
     * @param payload Kapua message
     * @return tuple with client and request id
     */
    private CallbackParam extractCallback(byte[] payload) {

        CallbackParam callbackParam = new CallbackParam();
        String clientId = getMetric(payload, "requester.client.id");
        String requestId = getMetric(payload, "request.id");
        callbackParam.setClientId(clientId);
        callbackParam.setRequestId(requestId);

        return callbackParam;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        LOG.info("Kapua Mock Device connection to broker lost.");
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        LOG.info("Message arrived in Kapua Mock Device with topic: " + topic);

        CallbackParam callbackParam;
        String responseTopic;
        byte[] responsePayload;
        byte[] requestPayload = mqttMessage.getPayload();

        try {
            // ASSET-V1
            if (topic.equals(assetExecRead)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/ASSET-V1/REPLY/" + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(assetStateChanged ? "/mqtt/KapuaPool-client-id_ASSET-V1_READ_req-id_assets_updated_assets.mqtt" : "/mqtt/KapuaPool-client-id_ASSET-V1_READ_req-id_assets.mqtt").toURI()));

                mqttClient.publish(responseTopic, responsePayload, 0, false);
            } else if (topic.equals(assetExecWrite)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/ASSET-V1/REPLY/" + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_ASSET-V1_WRITE_req-id_assets.mqtt").toURI()));

                assetStateChanged = true;
                mqttClient.publish(responseTopic, responsePayload, 0, false);
            }
            // CMD-V1
            else if (topic.equals(cmdExecCommand)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CMD-V1/REPLY/" + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_CMD-V1_REPLY_req-id_command.mqtt").toURI()));

                mqttClient.publish(responseTopic, responsePayload, 0, false);
            }
            // CONF-V1
            else if (topic.equals(confGetConfigurations)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CONF-V1/REPLY/" + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(configurationChanged ? "/mqtt/KapuaPool-client-id_CONF-V1_REPLY_req-id_updated_configurations.mqtt" : "/mqtt/KapuaPool-client-id_CONF-V1_REPLY_req-id_inital_configurations.mqtt").toURI()));

                mqttClient.publish(responseTopic, responsePayload, 0, false);
            } else if (topic.equals(confPutConfigurations)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + "/CONF-V1/REPLY/" + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_CONF-V1_PUT_configurations.mqtt").toURI()));

                configurationChanged = true;
                mqttClient.publish(responseTopic, responsePayload, 0, false);
            }
            // DEPLOY-V2
            else if (topic.equals(deployGetPackages)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_REPLY + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(packageListChanged ? "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_packages_updated_list.mqtt" : (packageListChangedAfterUninstall == true ? "/mqtt/KapuaPoolClient-id_DEPLOY_V2_REPLY_package_list_after_uninstall.mqtt" : "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_packages_initial_list.mqtt")).toURI()));

                mqttClient.publish(responseTopic, responsePayload, 0, false);
            } else if (topic.equals(deployExecDownload)) {
                callbackParam = extractCallback(requestPayload);
                KuraPayload kuraPayloadInitial = new KuraPayload();
                kuraPayloadInitial.readFromByteArray(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_REPLY + callbackParam.getRequestId();
                KuraPayload customKuraPayload1 = new KuraPayload();
                customKuraPayload1.setTimestamp(new Date());
                customKuraPayload1.getMetrics().put("response.code", 200);
                responsePayload = customKuraPayload1.toByteArray();
                mqttClient.publish(responseTopic, responsePayload, 0, false);
                Thread.sleep(100);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_NOTIFY + clientId + "/download";
                KuraPayload customKuraPayload2 = new KuraPayload();
                customKuraPayload2.setTimestamp(new Date());
                customKuraPayload2.getMetrics().put(JOB_ID, kuraPayloadInitial.getMetrics().get(JOB_ID));
                customKuraPayload2.getMetrics().put(CLIENT_ID, clientId);
                customKuraPayload2.getMetrics().put("dp.download.progress", 50);
                customKuraPayload2.getMetrics().put("dp.download.size", 20409);
                customKuraPayload2.getMetrics().put("dp.download.status", "IN_PROGRESS");
                customKuraPayload2.getMetrics().put("dp.download.index", 0);
                responsePayload = customKuraPayload2.toByteArray();
                mqttClient.publish(responseTopic, responsePayload, 0, false);
                Thread.sleep(100);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_NOTIFY + clientId + "/download";
                KuraPayload customKuraPayload3 = new KuraPayload();
                customKuraPayload3.setTimestamp(new Date());
                customKuraPayload3.getMetrics().put(JOB_ID, kuraPayloadInitial.getMetrics().get(JOB_ID));
                customKuraPayload3.getMetrics().put(CLIENT_ID, clientId);
                customKuraPayload3.getMetrics().put("dp.download.progress", 100);
                customKuraPayload3.getMetrics().put("dp.download.size", 20409);
                customKuraPayload3.getMetrics().put("dp.download.status", COMPLETED);
                customKuraPayload3.getMetrics().put("dp.download.index", 0);
                responsePayload = customKuraPayload3.toByteArray();
                mqttClient.publish(responseTopic, responsePayload, 0, false);
                Thread.sleep(100);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_NOTIFY + clientId + "/install";
                KuraPayload customKuraPayload4 = new KuraPayload();
                customKuraPayload4.setTimestamp(new Date());
                customKuraPayload4.getMetrics().put("dp.name", "Example Publisher-1.0.300.dp");
                customKuraPayload4.getMetrics().put(JOB_ID, kuraPayloadInitial.getMetrics().get(JOB_ID));
                customKuraPayload4.getMetrics().put("dp.install.progress", 100);
                customKuraPayload4.getMetrics().put("dp.install.status", COMPLETED);
                customKuraPayload4.getMetrics().put(CLIENT_ID, clientId);
                responsePayload = customKuraPayload4.toByteArray();
                mqttClient.publish(responseTopic, responsePayload, 0, false);

                packageListChanged = true;
            } else if (topic.equals(deployExecUninstall)) {
                callbackParam = extractCallback(requestPayload);
                KuraPayload kuraPayloadInitial = new KuraPayload();
                kuraPayloadInitial.readFromByteArray(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_REPLY + callbackParam.getRequestId();
                KuraPayload customKuraPayload = new KuraPayload();

                customKuraPayload.setTimestamp(new Date());
                customKuraPayload.getMetrics().put("response.code", 200);
                responsePayload = customKuraPayload.toByteArray();
                mqttClient.publish(responseTopic, responsePayload, 0, false);
                Thread.sleep(5000);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_NOTIFY + clientId + "/uninstall";
                KuraPayload customKuraPayload2 = new KuraPayload();

                customKuraPayload2.setTimestamp(new Date());
                customKuraPayload2.getMetrics().put(JOB_ID, kuraPayloadInitial.getMetrics().get(JOB_ID));
                customKuraPayload2.getMetrics().put("dp.name", "org.eclipse.kura.example.beacon");
                customKuraPayload2.getMetrics().put("dp.uninstall.progress", 100);
                customKuraPayload2.getMetrics().put("dp.uninstall.status", COMPLETED);
                customKuraPayload2.getMetrics().put(CLIENT_ID, clientId);
                responsePayload = customKuraPayload2.toByteArray();
                mqttClient.publish(responseTopic, responsePayload, 0, false);

                packageListChangedAfterUninstall = true;
            } else if (topic.equals(deployGetBundles)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_REPLY + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource(bundleStateChanged ? "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_bundles_updated_state.mqtt" : "/mqtt/KapuaPool-client-id_DEPLOY-V2_REPLY_req-id_bundles_inital_state.mqtt").toURI()));

                mqttClient.publish(responseTopic, responsePayload, 0, false);
            } else if (topic.equals(deployExecStart34) || topic.equals(deployExecStart95)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_REPLY + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_DEPLOY-V2_EXEC_START_bundle_id.mqtt").toURI()));

                bundleStateChanged = true;
                mqttClient.publish(responseTopic, responsePayload, 0, false);
            } else if (topic.equals(deployExecStop77)) {
                callbackParam = extractCallback(requestPayload);

                responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + DEPLOY_V2_REPLY + callbackParam.getRequestId();
                responsePayload = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KapuaPool-client-id_DEPLOY-V2_EXEC_STOP_bundle_id.mqtt").toURI()));

                bundleStateChanged = true;
                mqttClient.publish(responseTopic, responsePayload, 0, false);
            }
            // INVENTORY-V1
            else if (topic.startsWith(inventoryStartWithTopic)) {
                if (topic.equals(inventoryGetInventoryEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                    byte[] responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/INVENTORY-V1_GET_inventory_reply.json").toURI()));

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(inventoryGetInventoryBundleEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    byte[] responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/INVENTORY-V1_GET_inventory_bundles_reply.json").toURI()));

                    String responseBodyString = new String(responseBody);
                    if (inventoryBundleStarted) {
                        responseBodyString =
                                responseBodyString.replace("\"name\": \"io.netty.transport-native-unix-common\",\n" +
                                                "      \"version\": \"4.1.68.Final\",\n" +
                                                "      \"id\": 140,\n" +
                                                "      \"state\": \"RESOLVED\",\n" +
                                                "      \"signed\": true",
                                        "\"name\": \"io.netty.transport-native-unix-common\",\n" +
                                                "      \"version\": \"4.1.68.Final\",\n" +
                                                "      \"id\": 140,\n" +
                                                "      \"state\": \"ACTIVE\",\n" +
                                                "      \"signed\": true");
                    }

                    if (inventoryBundleStopped) {
                        responseBodyString =
                                responseBodyString.replace("\"name\": \"org.apache.felix.fileinstall\",\n" +
                                                "      \"version\": \"3.6.4\",\n" +
                                                "      \"id\": 178,\n" +
                                                "      \"state\": \"ACTIVE\",\n" +
                                                "      \"signed\": false",
                                        "\"name\": \"org.apache.felix.fileinstall\",\n" +
                                                "      \"version\": \"3.6.4\",\n" +
                                                "      \"id\": 178,\n" +
                                                "      \"state\": \"RESOLVED\",\n" +
                                                "      \"signed\": false");
                    }

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBodyString.getBytes(StandardCharsets.UTF_8));
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(inventoryExecInventoryBundleStart)) {
                    callbackParam = extractCallback(requestPayload);

                    KuraPayload kuraRequestPayload = new KuraPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    String jsonRequestBody = new String(kuraRequestPayload.getBody());

                    if (jsonRequestBody.contains("io.netty.transport-native-unix-common")) {
                        inventoryBundleStarted = true;

                        responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                        KuraPayload kuraResponsePayload = new KuraPayload();
                        kuraResponsePayload.getMetrics().put("response.code", 200);

                        responsePayload = kuraResponsePayload.toByteArray();

                        mqttClient.publish(responseTopic, responsePayload, 0, false);
                    }
                } else if (topic.equals(inventoryExecInventoryBundleStop)) {
                    callbackParam = extractCallback(requestPayload);

                    KuraPayload kuraRequestPayload = new KuraPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    String jsonRequestBody = new String(kuraRequestPayload.getBody());

                    if (jsonRequestBody.contains("org.apache.felix.fileinstall")) {
                        inventoryBundleStopped = true;

                        responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                        KuraPayload kuraResponsePayload = new KuraPayload();
                        kuraResponsePayload.getMetrics().put("response.code", 200);

                        responsePayload = kuraResponsePayload.toByteArray();

                        mqttClient.publish(responseTopic, responsePayload, 0, false);
                    }
                } else if (topic.equals(inventoryGetInventoryContainerEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                    byte[] responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/INVENTORY-V1_GET_inventory_containers_reply.json").toURI()));

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(inventoryExecInventoryContainerStart)) {
                    callbackParam = extractCallback(requestPayload);

                    KuraPayload kuraRequestPayload = new KuraPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    String jsonRequestBody = new String(kuraRequestPayload.getBody());

                    if (jsonRequestBody.contains("db")) {
                        inventoryContainerStarted = true;

                        responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                        KuraPayload kuraResponsePayload = new KuraPayload();
                        kuraResponsePayload.getMetrics().put("response.code", 200);

                        responsePayload = kuraResponsePayload.toByteArray();

                        mqttClient.publish(responseTopic, responsePayload, 0, false);
                    }
                } else if (topic.equals(inventoryExecInventoryContainerStop)) {
                    callbackParam = extractCallback(requestPayload);

                    KuraPayload kuraRequestPayload = new KuraPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    String jsonRequestBody = new String(kuraRequestPayload.getBody());

                    if (jsonRequestBody.contains("db")) {
                        inventoryContainerStopped = true;

                        responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                        KuraPayload kuraResponsePayload = new KuraPayload();
                        kuraResponsePayload.getMetrics().put("response.code", 200);

                        responsePayload = kuraResponsePayload.toByteArray();

                        mqttClient.publish(responseTopic, responsePayload, 0, false);
                    }
                } else if (topic.equals(inventoryGetInventorySystemPackagesEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                    byte[] responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/INVENTORY-V1_GET_inventory_system_reply.json").toURI()));

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(inventoryGetInventoryPackagesEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + INVENTORY_V1_REPLY + callbackParam.getRequestId();

                    byte[] responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/INVENTORY-V1_GET_inventory_packages_reply.json").toURI()));

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else {
                    LOG.error("Kapua Mock Device unhandled INVENTORY-V1 topic: {}", topic);
                }
            }
            // KEYS-V1
            else if (topic.startsWith(keystoreStartWithTopic)) {
                if (topic.equals(keystoreGetKeystores)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    byte[] responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_reply.json").toURI()));

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(keystoreGetKeystoresEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    byte[] responseBody = null;
                    if (kuraRequestPayload.hasBody()) {
                        String queryJsonString = new String(kuraRequestPayload.getBody());

                        if (queryJsonString.contains("alias")) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entries_alias_reply.json").toURI()));
                        } else if (queryJsonString.contains("keystoreServicePid")) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entries_keystore_reply.json").toURI()));
                        }
                    } else {

                        if (keystoreInstalledCertificate == 0 && keystoreInstalledKeypair == 0) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entries_default_reply.json").toURI()));
                        } else if (keystoreInstalledCertificate == 1 && keystoreInstalledKeypair == 0) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entries_cert_installed_reply.json").toURI()));
                        } else if (keystoreInstalledCertificate == 1 && keystoreInstalledKeypair == 1) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entries_cert_key_installed_reply.json").toURI()));
                        } else if (keystoreInstalledCertificate == 0 && keystoreInstalledKeypair == 1) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entries_key_installed_reply.json").toURI()));
                        }
                    }

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(keystoreGetKeystoresEntry)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    byte[] responseBody = null;
                    if (kuraRequestPayload.hasBody()) {
                        String queryJsonString = new String(kuraRequestPayload.getBody());

                        if (queryJsonString.contains("\"alias\":\"localhost\"") &&
                                queryJsonString.contains("\"keystoreServicePid\":\"HttpsKeystore\"")) {
                            responseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_GET_keystores_entry_reply.json").toURI()));
                        }
                    }

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.setBody(responseBody);
                    kuraResponsePayload.getMetrics().put("response.code", 200);

                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(keystorePostKeystoresEntriesCertificate)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    int responseCode;
                    if (kuraRequestPayload.hasBody()) {
                        String queryJsonString = new String(kuraRequestPayload.getBody());

                        if (queryJsonString.contains("-----BEGIN CERTIFICATE-----") &&
                                queryJsonString.contains("-----END CERTIFICATE-----")) {
                            keystoreInstalledCertificate++;
                            responseCode = 200;
                        } else {
                            responseCode = 500;
                        }
                    } else {
                        responseCode = 400;
                    }

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.getMetrics().put("response.code", responseCode);
                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(keystorePostKeystoresEntriesKeypair)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    int responseCode;
                    if (kuraRequestPayload.hasBody()) {
                        String queryJsonString = new String(kuraRequestPayload.getBody());

                        if (queryJsonString.contains("SHA256withRSA") &&
                                queryJsonString.contains("CN=Let's Encrypt Authority X3,O=Let's Encrypt,C=US")) {
                            keystoreInstalledKeypair++;
                            responseCode = 200;
                        } else {
                            responseCode = 500;
                        }
                    } else {
                        responseCode = 400;
                    }

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.getMetrics().put("response.code", responseCode);
                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(keystorePostKeystoresEntriesCsr)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    byte[] resposnseBody = null;
                    int responseCode;
                    if (kuraRequestPayload.hasBody()) {
                        String queryJsonString = new String(kuraRequestPayload.getBody());

                        if (queryJsonString.contains("\"signatureAlgorithm\":\"SHA256withRSA\"") &&
                                queryJsonString.contains("\"attributes\":\"CN=Kura, OU=IoT, O=Eclipse, C=US\"")) {

                            if (queryJsonString.contains("\"alias\":\"localhostFixed\"")) {
                                resposnseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_POST_keystores_entries_csr_fixed_reply.json").toURI()));
                                responseCode = 200;
                            } else if (queryJsonString.contains("\"alias\":\"localhostKuraBugged\"")) {
                                resposnseBody = Files.readAllBytes(Paths.get(getClass().getResource("/mqtt/KEYS-V1_POST_keystores_entries_csr_kurabugged_reply.txt").toURI()));
                                responseCode = 200;
                            } else {
                                responseCode = 500;
                            }
                        } else {
                            responseCode = 500;
                        }
                    } else {
                        responseCode = 400;
                    }

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.getMetrics().put("response.code", responseCode);
                    kuraResponsePayload.setBody(resposnseBody);
                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else if (topic.equals(keystoreDelKeystoresEntries)) {
                    callbackParam = extractCallback(requestPayload);

                    responseTopic = $EDC + CLIENT_ACCOUNT + "/" + callbackParam.getClientId() + KEYS_V1_REPLY + callbackParam.getRequestId();

                    KuraRequestPayload kuraRequestPayload = new KuraRequestPayload();
                    kuraRequestPayload.readFromByteArray(requestPayload);

                    int responseCode;
                    if (kuraRequestPayload.hasBody()) {
                        String queryJsonString = new String(kuraRequestPayload.getBody());

                        if (queryJsonString.contains("SSLKeystore") && queryJsonString.contains("qaCertificate")) {
                            keystoreInstalledCertificate--;
                            responseCode = 200;
                        } else if (queryJsonString.contains("SSLKeystore") && queryJsonString.contains("qaKeypair")) {
                            keystoreInstalledKeypair--;
                            responseCode = 200;
                        } else {
                            responseCode = 500;
                        }
                    } else {
                        responseCode = 400;
                    }

                    KuraPayload kuraResponsePayload = new KuraPayload();
                    kuraResponsePayload.getMetrics().put("response.code", responseCode);
                    responsePayload = kuraResponsePayload.toByteArray();

                    mqttClient.publish(responseTopic, responsePayload, 0, false);
                } else {
                    LOG.error("Kapua Mock Device unhandled KEYS-V1 topic: {}", topic);
                }
            }
            // Fail
            else {
                LOG.error("Kapua Mock Device unhandled topic: {}", topic);
            }
        } catch (Exception e) {
            LOG.error("Error while handling the request on topic: {}", topic, e);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        LOG.info("Kapua Mock Device message delivery complete.");
    }

    /**
     * Simple tuple for callback parameters.
     */
    private class CallbackParam {

        private String clientId;

        private String requestId;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }
    }
}
