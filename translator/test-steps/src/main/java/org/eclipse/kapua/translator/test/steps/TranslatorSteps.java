/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.translator.test.steps;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.service.device.call.message.kura.KuraPayload;
import org.eclipse.kapua.service.device.call.message.kura.app.response.KuraResponseMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataChannel;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataMessage;
import org.eclipse.kapua.service.device.call.message.kura.data.KuraDataPayload;
import org.eclipse.kapua.translator.Translator;
import org.eclipse.kapua.translator.jms.kura.TranslatorDataJmsKura;
import org.eclipse.kapua.translator.kura.jms.TranslatorDataKuraJms;
import org.eclipse.kapua.translator.kura.mqtt.TranslatorDataKuraMqtt;
import org.eclipse.kapua.translator.mqtt.kura.TranslatorDataMqttKura;
import org.eclipse.kapua.translator.mqtt.kura.TranslatorResponseMqttKura;
import org.eclipse.kapua.transport.message.jms.JmsMessage;
import org.eclipse.kapua.transport.message.jms.JmsPayload;
import org.eclipse.kapua.transport.message.jms.JmsTopic;
import org.eclipse.kapua.transport.message.mqtt.MqttMessage;
import org.eclipse.kapua.transport.message.mqtt.MqttPayload;
import org.eclipse.kapua.transport.message.mqtt.MqttTopic;
import org.junit.Assert;

import com.google.inject.Singleton;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

/**
 * Implementation of Gherkin steps used in TranslatorUnitTests.feature scenarios.
 */
@Singleton
public class TranslatorSteps extends TestBase {

    private ExampleTranslator exampleTranslator;
    private TranslatorDataMqttKura translatorDataMqttKura;
    private TranslatorResponseMqttKura translatorResponseMqttKura;
    private TranslatorDataKuraMqtt translatorDataKuraMqtt;
    private TranslatorDataJmsKura translatorDataJmsKura;
    private TranslatorDataKuraJms translatorDataKuraJms;

    @Inject
    public TranslatorSteps(StepData stepData) {
        super(stepData);
        exampleTranslator = new ExampleTranslator();
        translatorDataMqttKura = new TranslatorDataMqttKura();
        translatorResponseMqttKura = new TranslatorResponseMqttKura();
        translatorDataKuraMqtt = new TranslatorDataKuraMqtt();
        translatorDataJmsKura = new TranslatorDataJmsKura();
        translatorDataKuraJms = new TranslatorDataKuraJms();
    }

    // *************************************
    // Definition of Cucumber scenario steps
    // *************************************

    @Before
    public void beforeScenarioDockerFull(Scenario scenario) {
        updateScenario(scenario);
    }

    @Given("I try to translate from {string} to {string}")
    public void iFindTranslator(String from, String to) throws Exception {
        Class fromClass;
        Class toClass;
        try {
            if (!from.equals("") && !to.equals("")) {
                fromClass = Class.forName(from);
                toClass = Class.forName(to);
            } else {
                fromClass = null;
                toClass = null;
            }
            Translator translator = Translator.getTranslatorFor(exampleTranslator.getClass(fromClass), exampleTranslator.getClass(toClass));
            stepData.put("Translator", translator);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("Translator {string} is found")
    public void translatorIsFound(String translatorName) {
        Translator translator = (Translator) stepData.get("Translator");
        Assert.assertEquals(translatorName, translator.getClass().getSimpleName());
    }

    @Given("I create mqtt message with (valid/invalid/empty) payload {string} and (valid/invalid) topic {string}")
    public void creatingMqttMessage(String payload, String topic) throws Exception{
        try {
            Date date = new Date();
            MqttTopic mqttTopic = new MqttTopic(topic);
            KuraPayload kuraPayload = new KuraPayload();
            if (payload.equals("invalidPayload") || payload.equals("")) {
                kuraPayload.setBody(payload.getBytes());
            } else {
                kuraPayload.getMetrics().put(payload, 200);
            }
            MqttPayload mqttPayload = new MqttPayload(kuraPayload.toByteArray());
            MqttMessage mqttMessage = new MqttMessage(mqttTopic, date, mqttPayload);
            stepData.put("MqttMessage", mqttMessage);
        } catch (Exception ex){
            verifyException(ex);
        }
    }

    @When("I try to translate mqtt response")
    public void iTryToTranslateMqttResponse() throws Exception {
        MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");
        try {
            KuraResponseMessage kuraResponseMessage = translatorResponseMqttKura.translate(mqttMessage);
            stepData.put("KuraResponseMessage", kuraResponseMessage);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("I got kura response message with {string} payload body")
    public void kuraResponseMessageWithPayloadBody(String payloadType) {
        KuraResponseMessage kuraResponseMessage = (KuraResponseMessage) stepData.get("KuraResponseMessage");
        Assert.assertTrue(kuraResponseMessage.getPayload().getBody().getClass().getSimpleName().equals(payloadType));
    }

    @Then("I got kura response message with proper payload metrics")
    public void kuraResponseMessageWithPayloadAndChannelAndData() {
        KuraResponseMessage kuraResponseMessage = (KuraResponseMessage) stepData.get("KuraResponseMessage");
        Assert.assertTrue(kuraResponseMessage.getPayload().getMetrics() != null);
    }

    @Given("I create kura data message with channel with scope {string}, client id {string} and payload without body and metrics")
    public void iCreateKuraDataMessage(String scope, String clientId) throws Exception {
        try {
            KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
            Date date = new Date();
            KuraDataPayload kuraDataPayload = new KuraDataPayload();
            KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, kuraDataPayload);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("I try to translate kura data message to mqtt message")
    public void iTryToTranslateKuraDataMessageToMqttMessage() throws Exception {
        try {
            KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
            MqttMessage mqttMessage = translatorDataKuraMqtt.translate(kuraDataMessage);
            stepData.put("MqttMessage", mqttMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }

    }

    @Then("I get mqtt message with channel with scope {string}, client id {string} and (empty body|non empty body)")
    public void mqttMessageWithChannelScopeClientIDAndBody(String scope, String clientId) {
        MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");
        String requestTopic = scope.concat("/" + clientId);
        Assert.assertEquals(requestTopic, mqttMessage.getRequestTopic().getTopic());
        if (mqttMessage.getPayload().getBody().length == 0) {
            Assert.assertTrue(mqttMessage.getPayload().getBody().length == 0);
        } else {
            Assert.assertTrue(mqttMessage.getPayload().getBody().length != 0);
        }
    }

    @And("I got kura response message channel with {string}, {string}, {string}, {string}, {string} and {string} data")
    public void kuraResponseMessageWithChannelAndData(String replyPart, String requestId, String appId, String messageClassification, String scope, String clientId) {
        KuraResponseMessage kuraResponseMessage = (KuraResponseMessage) stepData.get("KuraResponseMessage");
        Assert.assertTrue(kuraResponseMessage.getChannel().getReplyPart().equals(replyPart));
        Assert.assertTrue(kuraResponseMessage.getChannel().getRequestId().equals(requestId));
        Assert.assertTrue(kuraResponseMessage.getChannel().getAppId().equals(appId));
        Assert.assertTrue(kuraResponseMessage.getChannel().getMessageClassification().equals(messageClassification));
        Assert.assertTrue(kuraResponseMessage.getChannel().getScope().equals(scope));
        Assert.assertTrue(kuraResponseMessage.getChannel().getClientId().equals(clientId));
    }

    @Given("I create kura data message with channel with scope {string}, client id {string}, valid payload and metrics but without body")
    public void kuraDataMessageWithoutBodyAndMetrics(String scope, String clientId) throws Exception {
        try {
            Date date = new Date();
            KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
            KuraDataPayload kuraDataPayload = new KuraDataPayload();
            kuraDataPayload.getMetrics().put("response.code", 200);
            KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, kuraDataPayload);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("I create kura data message with channel with scope {string}, client id {string} and payload with body and metrics")
    public void fullKuraDataMessage(String scope, String clientId) throws Exception {
        try {
            Date date = new Date();
            KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
            KuraDataPayload kuraDataPayload = new KuraDataPayload();
            kuraDataPayload.setBody("Payload".getBytes());
            kuraDataPayload.getMetrics().put("response.code", 200);
            KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, kuraDataPayload);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("I try to translate mqtt message to kura data message")
    public void iTryToTranslateMqttMessageToKuraMessage() throws Exception {
        try {
            MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");
            KuraDataMessage kuraDataMessage = translatorDataMqttKura.translate(mqttMessage);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("I got kura data message with {string} payload body")
    public void iGotKuraDataMessageWithPayloadBody(String payloadType) throws Throwable {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
        Assert.assertTrue(kuraDataMessage.getPayload().getBody().getClass().getSimpleName().equals(payloadType));
    }

    @And("I got kura data message channel with {string} and {string} data")
    public void iGotKuraDataMessageChannelWithAndData(String scope, String clientId) {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
        Assert.assertTrue(kuraDataMessage.getChannel().getScope().equals(scope));
        Assert.assertTrue(kuraDataMessage.getChannel().getClientId().equals(clientId));
    }

    @Then("I got kura data message with proper payload metrics response code {int}")
    public void iGotKuraDataMessageWithProperPayloadMetrics(int responseCode) {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
        Assert.assertEquals(kuraDataMessage.getPayload().getMetrics().get("response.code"), responseCode);
    }

    @Then("I got kura data message with empty payload")
    public void iGotKuraDataMessageWithEmptyPayload() {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
        Assert.assertEquals(null, kuraDataMessage.getPayload().getBody());
    }

    @Given("I create jms message with (valid|invalid|empty) payload {string} and (valid|invalid) topic {string}")
    public void iCreateJmsMessageWithInvalidPayloadAndInvalidTopic(String payload, String topic) throws Exception {
        try {
            Date date = new Date();
            JmsTopic jmsTopic = new JmsTopic(topic);
            KuraPayload kuraPayload = new KuraPayload();
            if (payload.equals("invalidPayload") || payload.equals("")) {
                kuraPayload.setBody(payload.getBytes());
            } else {
                kuraPayload.getMetrics().put(payload, 200);
            }
            JmsPayload jmsPayload = new JmsPayload(kuraPayload.toByteArray());
            JmsMessage jmsMessage = new JmsMessage(jmsTopic, date, jmsPayload);
            stepData.put("JmsMessage", jmsMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @And("I try to translate jms message to kura data message")
    public void iTryToTranslateJmsMessageToKuraMessage() throws Exception {
        JmsMessage jmsMessage = (JmsMessage) stepData.get("JmsMessage");
        try {
            KuraDataMessage kuraDataMessage = translatorDataJmsKura.translate(jmsMessage);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I try to translate kura data message to jms message")
    public void iTryToTranslateKuraDataMessageToJmsMessage() throws Exception {
        try {
            KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
            JmsMessage jmsMessage = translatorDataKuraJms.translate(kuraDataMessage);
            stepData.put("JmsMessage", jmsMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("I got kura data message channel with {string} scope, {string} client id and proper semanticPart")
    public void iCreateJmsMessageWithInvalidPayloadAndTopic(String scope, String clientId, List<String> semanticParts) {
        KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
        Assert.assertEquals(scope, kuraDataMessage.getChannel().getScope());
        Assert.assertEquals(clientId, kuraDataMessage.getChannel().getClientId());
        for (String semanticPart : semanticParts) {
            Assert.assertTrue(kuraDataMessage.getChannel().getSemanticParts().contains(semanticPart));
        }
    }

    @Then("I got jms message with topic {string} and (empty body|non empty body)")
    public void iGotJmsMessageWithTopicAndEmptyPayload(String topic) {
        JmsMessage jmsMessage = (JmsMessage) stepData.get("JmsMessage");
        Assert.assertEquals(new JmsTopic(topic).getTopic(), jmsMessage.getTopic().getTopic());
        if (jmsMessage.getPayload().getBody().length == 0) {
            Assert.assertTrue(jmsMessage.getPayload().getBody().length == 0);
        } else {
            Assert.assertTrue(jmsMessage.getPayload().getBody().length != 0);
        }
    }

    @When("I try to translate mqtt null message to kura data message")
    public void iTryToTranslateMqttNullMessageToKuraDataMessage() throws Exception {
        try {
            MqttMessage mqttMessage = (MqttMessage) stepData.get("MqttMessage");
            KuraDataMessage kuraDataMessage = translatorDataMqttKura.translate((MqttMessage) null);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (Exception ex){
            verifyException(ex);
        }
    }

    @Given("I create kura data message with channel with scope {string}, client id {string} and null payload")
    public void iCreateKuraDataMessageWithChannelWithScopeClientIdAndNullPayload(String scope, String clientId) {
        KuraDataChannel kuraDataChannel = new KuraDataChannel(scope, clientId);
        Date date = new Date();
        KuraDataMessage kuraDataMessage = new KuraDataMessage(kuraDataChannel, date, null);
        stepData.put("KuraDataMessage", kuraDataMessage);
    }

    @Given("I create kura data message with null channel and payload without body and with metrics")
    public void iCreateKuraDataMessageWithNullChannelAndPayloadWithoutBodyAndWithMetrics() {
        Date date = new Date();
        KuraDataPayload kuraDataPayload = new KuraDataPayload();
        kuraDataPayload.getMetrics().put("response.code", 200);
        KuraDataMessage kuraDataMessage = new KuraDataMessage(null, date, kuraDataPayload);

        stepData.put("KuraDataMessage", kuraDataMessage);
    }

    @And("I try to translate invalid kura data message to mqtt message")
    public void iTryToTranslateInvalidKuraDataMessageToMqttMessage() throws Exception {
        try {
            KuraDataMessage kuraDataMessage = (KuraDataMessage) stepData.get("KuraDataMessage");
            MqttMessage mqttMessage = translatorDataKuraMqtt.translate((KuraDataMessage) null);
            stepData.put("MqttMessage", mqttMessage);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @When("I try to translate invalid jms message to kura data message")
    public void iTryToTranslateInvalidJmsMessageToKuraDataMessage() throws Exception{
        try {
            KuraDataMessage kuraDataMessage = translatorDataJmsKura.translate((JmsMessage) null);
            stepData.put("KuraDataMessage", kuraDataMessage);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I try to translate invalid kura data message to jms message")
    public void iTryToTranslateInvalidKuraDataMessageToJmsMessage() throws Exception {
        try {
            JmsMessage jmsMessage = translatorDataKuraJms.translate((KuraDataMessage) null);
            stepData.put("JmsMessage", jmsMessage);
        } catch (Exception ex){
            verifyException(ex);
        }
    }
}
