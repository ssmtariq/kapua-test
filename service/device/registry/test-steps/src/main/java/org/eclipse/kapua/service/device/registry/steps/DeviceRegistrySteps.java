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
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.message.KapuaMessageFactory;
import org.eclipse.kapua.message.KapuaPosition;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaAppsPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaBirthPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaDisconnectPayload;
import org.eclipse.kapua.message.device.lifecycle.KapuaLifecycleMessageFactory;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingChannel;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingMessage;
import org.eclipse.kapua.message.device.lifecycle.KapuaMissingPayload;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.qa.common.StepData;
import org.eclipse.kapua.qa.common.TestBase;
import org.eclipse.kapua.qa.common.TestDomain;
import org.eclipse.kapua.qa.common.cucumber.CucConfig;
import org.eclipse.kapua.qa.common.cucumber.CucConnection;
import org.eclipse.kapua.qa.common.cucumber.CucDevice;
import org.eclipse.kapua.qa.common.cucumber.CucUser;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authorization.domain.Domain;
import org.eclipse.kapua.service.authorization.group.Group;
import org.eclipse.kapua.service.authorization.group.GroupAttributes;
import org.eclipse.kapua.service.authorization.group.GroupFactory;
import org.eclipse.kapua.service.authorization.group.GroupQuery;
import org.eclipse.kapua.service.authorization.group.GroupService;
import org.eclipse.kapua.service.device.management.message.KapuaMethod;
import org.eclipse.kapua.service.device.management.message.response.KapuaResponseCode;
import org.eclipse.kapua.service.device.registry.ConnectionUserCouplingMode;
import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceAttributes;
import org.eclipse.kapua.service.device.registry.DeviceCreator;
import org.eclipse.kapua.service.device.registry.DeviceFactory;
import org.eclipse.kapua.service.device.registry.DeviceListResult;
import org.eclipse.kapua.service.device.registry.DeviceQuery;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.device.registry.DeviceStatus;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnection;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionCreator;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionDomain;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionFactory;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionListResult;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionQuery;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionService;
import org.eclipse.kapua.service.device.registry.connection.DeviceConnectionStatus;
import org.eclipse.kapua.service.device.registry.event.DeviceEvent;
import org.eclipse.kapua.service.device.registry.event.DeviceEventAttributes;
import org.eclipse.kapua.service.device.registry.event.DeviceEventCreator;
import org.eclipse.kapua.service.device.registry.event.DeviceEventFactory;
import org.eclipse.kapua.service.device.registry.event.DeviceEventListResult;
import org.eclipse.kapua.service.device.registry.event.DeviceEventQuery;
import org.eclipse.kapua.service.device.registry.event.DeviceEventService;
import org.eclipse.kapua.service.device.registry.lifecycle.DeviceLifeCycleService;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagAttributes;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

/**
 * Implementation of Gherkin steps used in DeviceRegistry.feature scenarios.
 * <p>
 * MockedLocator is used for Location Service. Mockito is used to mock other
 * services that the Device Registry services dependent on. Dependent services are: -
 * Authorization Service -
 */
@Singleton
public class DeviceRegistrySteps extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(DeviceRegistrySteps.class);

    private static final String TEST_DEVICE_NAME = "test_name";
    private static final String TEST_BIOS_VERSION_1 = "bios_version_1";
    private static final String TEST_BIOS_VERSION_2 = "bios_version_2";
    private static final String TEST_BIOS_VERSION_3 = "bios_version_3";

    // Strings for client ID character set and length checks
    private static String simpleClientId = "simpleClientIdWith64Chars_12345678901234567890123456789012345678";
    private static String fullClientId = "fullClientIdWith64Chars_✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓";
    private static String simpleClientIdTooLong = "simpleClientIdWith65Chars_123456789012345678901234567890123456789";
    private static String fullClientIdTooLong = "fullClientIdWith65Chars_✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔✕✁✂✃✄✅✆✇✈✉✊✋✌✍✎✏✐✑✒✓✔";

    // Various device connection details
    private static final String CLIENT_NAME = "test_client";
    private static final String CLIENT_IP = "127.1.1.10";
    private static final String SERVER_IP = "127.1.1.100";

    private static final String DEVICE_CREATOR = "DeviceCreator";
    private static final String DEVICE_ID = "DeviceId";
    private static final String DEVICE = "Device";
    private static final String LAST_DEVICE = "LastDevice";
    private static final String LAST_ACCOUNT = "LastAccount";
    private static final String DEVICE_QUERY = "DeviceQuery";
    private static final String DEVICE_LIST = "DeviceList";
    private static final String DEVICE_CONNECTION_CREATOR = "DeviceConnectionCreator";
    private static final String DEVICE_CONNECTION = "DeviceConnection";
    private static final String DEVICE_CONNECTION_ID = "DeviceConnectionId";
    private static final String DEVICE_CONNECTION_LIST = "DeviceConnectionList";
    private static final String DEVICE_EVENT_CREATOR = "DeviceEventCreator";
    private static final String DEVICE_EVENT = "DeviceEvent";
    private static final String DEVICE_EVENT_ID = "DeviceEventId";
    private static final String DEVICE_EVENT_LIST = "DeviceEventList";
    private static final String PART1 = "part1";
    private static final String PART2 = "part2";
    private static final String RELIAGATE_10_20 = "ReliaGate 10-20";
    private static final String VERSION_NUMBER = "1.2.3";
    private static final String LINUX = "linux";

    // Various device registry service related references
    private DeviceRegistryService deviceRegistryService;
    private DeviceFactory deviceFactory;

    // Various device connection service related references
    private DeviceConnectionService deviceConnectionService;
    private DeviceConnectionFactory deviceConnectionFactory;

    // Various device event service related references
    private DeviceEventService eventService;
    private DeviceEventFactory eventFactory;


    // Additional service references for integration testing
    private DeviceLifeCycleService deviceLifeCycleService;
    private AccountService accountService;
    private UserService userService;
    private TagService tagService;
    private TagFactory tagFactory;
    private KapuaMessageFactory messageFactory;
    private KapuaLifecycleMessageFactory lifecycleMessageFactory;
    private GroupService groupService;
    private GroupFactory groupFactory;

    private AclCreator aclCreator;

    // Default constructor
    @Inject
    public DeviceRegistrySteps(StepData stepData) {
        super(stepData);
    }

    // ************************************************************************************
    // ************************************************************************************
    // * Definition of Cucumber scenario steps                                            *
    // ************************************************************************************
    // ************************************************************************************

    // ************************************************************************************
    // * Setup and tear-down steps                                                        *
    // ************************************************************************************

    @Before(value="@env_docker or @env_docker_base or @env_none", order=10)
    public void beforeScenarioNone(Scenario scenario) {
        updateScenario(scenario);
    }

    @After(value="@setup")
    public void setServices() {
        KapuaLocator locator = KapuaLocator.getInstance();
        deviceRegistryService = locator.getService(DeviceRegistryService.class);
        deviceFactory = locator.getFactory(DeviceFactory.class);

        deviceConnectionService = locator.getService(DeviceConnectionService.class);
        deviceConnectionFactory = locator.getFactory(DeviceConnectionFactory.class);

        eventService = locator.getService(DeviceEventService.class);
        eventFactory = locator.getFactory(DeviceEventFactory.class);

        messageFactory = locator.getFactory(KapuaMessageFactory.class);
        lifecycleMessageFactory = locator.getFactory(KapuaLifecycleMessageFactory.class);

        deviceLifeCycleService = locator.getService(DeviceLifeCycleService.class);
        accountService = locator.getService(AccountService.class);
        userService = locator.getService(UserService.class);
        tagService = locator.getService(TagService.class);
        tagFactory = locator.getFactory(TagFactory.class);
        groupService = locator.getService(GroupService.class);
        groupFactory = locator.getFactory(GroupFactory.class);

        aclCreator = new AclCreator();
    }

    // ************************************************************************************
    // * Cucumber Test steps                                                              *
    // ************************************************************************************

    // ************************************************************************************
    // * Device Registry steps                                                            *
    // ************************************************************************************

    @When("I configure the device registry service")
    public void setDeviceRegistryConfigurationValue(List<CucConfig> cucConfigs) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getCurrentScopeId();
        KapuaId scopeId = getCurrentParentId();
        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getParentId() != null) {
                scopeId = getKapuaId(config.getParentId());
            }
            if (config.getScopeId() != null) {
                accId = getKapuaId(config.getScopeId());
            }
        }
        primeException();
        try {
            deviceRegistryService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("A regular device creator")
    public void prepareDefaultDeviceCreator() {
        DeviceCreator deviceCreator = prepareRegularDeviceCreator(getCurrentScopeId(), "device_1");
        stepData.put(DEVICE_CREATOR, deviceCreator);
    }

    @Given("A null device creator")
    public void createANullDeviceCreator() {
        stepData.put(DEVICE_CREATOR, null);
    }

    @When("I set the creator scope ID to null")
    public void setDeviceCreatorScopeToNull() {
        DeviceCreator deviceCreator = (DeviceCreator) stepData.get(DEVICE_CREATOR);
        deviceCreator.setScopeId(null);
        stepData.put(DEVICE_CREATOR, deviceCreator);
    }

    @When("I set the creator client ID to null")
    public void setDeviceCreatorClientToNull() {
        DeviceCreator deviceCreator = (DeviceCreator) stepData.get(DEVICE_CREATOR);
        deviceCreator.setClientId(null);
        stepData.put(DEVICE_CREATOR, deviceCreator);
    }

    @Given("The device ID {string}")
    public void setDeviceId(String deviceId) {
        KapuaId dev;
        if (deviceId.trim().toLowerCase().equals("null")) {
            dev = null;
        } else {
            dev = getKapuaId(deviceId);
        }
        stepData.put(DEVICE_ID, dev);
    }

    @Given("The device client ID {string}")
    public void setDeviceClientId(String clientId) {
        String id;
        if (clientId.trim().toLowerCase().equals("null")) {
            id = null;
        } else {
            id = clientId;
        }
        stepData.put("ClientId", id);
    }

    @Given("A regular device")
    public void createRegularDevice() {
        Device device = prepareRegularDevice(getCurrentParentId(), getKapuaId());
        stepData.put(DEVICE, device);
    }

    @Given("(A device)/(Devices) such as")
    public void createDevicesSuchAs(List<CucDevice> devLst) throws Exception {
        createDevicesAsSpecifiedInternal(devLst);
    }

    @Given("I create (a device)/(devices) with parameters")
    public void createDevicesWithParameters(List<CucDevice> devLst) throws Exception {
        createDevicesAsSpecifiedInternal(devLst);
    }

    private void createDevicesAsSpecifiedInternal(List<CucDevice> devLst) throws Exception {
        primeException();
        try {
            stepData.remove(LAST_DEVICE);
            Device tmpDevice = null;
            for (CucDevice tmpCDev : devLst) {
                tmpCDev.parse();
                DeviceCreator devCr = prepareDeviceCreatorFromCucDevice(tmpCDev);
                tmpDevice = deviceRegistryService.create(devCr);
            }
            stepData.put(LAST_DEVICE, tmpDevice);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("The device {string}")
    public void createDeviceWithName(String clientId) throws KapuaException {
        Account tmpAcc = (Account) stepData.get(LAST_ACCOUNT);
        DeviceCreator tmpDevCr = deviceFactory.newCreator(tmpAcc.getId(), clientId);
        Device tmpDev = deviceRegistryService.create(tmpDevCr);
        stepData.put(LAST_DEVICE, tmpDev);
    }

    @Given("I try to create devices with invalid symbols in name")
    public void createDeviceWithInvalidSymbolsInName() throws Exception {
        String invalidSymbols = "!\"#$%&'()=»Ç" +
                ">:;<-.,⁄@‹›€" +
                "*ı–°·‚_±Œ„‰" +
                "?“‘”’ÉØ∏{}|Æ" +
                "æÒ\uF8FFÔÓÌÏÎÍÅ«" +
                "◊Ñˆ¯Èˇ¿";
        Account tmpAcc = (Account) stepData.get("LastAccount");
        for (int i = 0; i < invalidSymbols.length(); i++) {
            String clientId = "Device" + invalidSymbols.charAt(i);
            try {
                DeviceCreator tmpDevCr = deviceFactory.newCreator(tmpAcc.getId(), clientId);
                Device tmpDev = deviceRegistryService.create(tmpDevCr);
            } catch (KapuaException e) {
                verifyException(e);
            }
        }
    }

    @Given("A null device")
    public void createANullDevice() {
        stepData.put(DEVICE, null);
    }

    @When("I set the device scope ID to null")
    public void setDeviceScopeToNull() {
        Device device = (Device) stepData.get(DEVICE);
        device.setScopeId(null);
        stepData.put(DEVICE, device);
    }

    @When("I set the device ID to null")
    public void setDeviceIdToNull() {
        Device device = (Device) stepData.get(DEVICE);
        device.setId(null);
        stepData.put(DEVICE, device);
    }

    @Given("A regular query")
    public void createRegularQuery() {
        DeviceQuery query = deviceFactory.newQuery(getCurrentScopeId());
        stepData.put(DEVICE_QUERY, query);
    }

    @Given("A query with a null Scope ID")
    public void createQueryWithNullScopeId() {
        DeviceQuery query = deviceFactory.newQuery(null);
        stepData.put(DEVICE_QUERY, query);
    }

    @Given("A null query")
    public void createNullQuery() {
        stepData.put(DEVICE_QUERY, null);
    }

    @Given("A device named {string}")
    public void createNamedDevice(String name) throws Exception {
        DeviceCreator deviceCreator = prepareRegularDeviceCreator(getCurrentScopeId(), name);
        stepData.put(DEVICE_CREATOR, deviceCreator);
        primeException();
        try {
            stepData.remove(DEVICE);
            stepData.remove(DEVICE_ID);
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put(DEVICE, device);
            stepData.put(DEVICE_ID, device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("A device with BIOS version {string} named {string}")
    public void createNamedDeviceWithBiosVersion(String version, String name) throws Exception {
        DeviceCreator deviceCreator = prepareRegularDeviceCreator(getCurrentScopeId(), name);
        deviceCreator.setBiosVersion(version);
        stepData.put(DEVICE_CREATOR, deviceCreator);
        primeException();
        try {
            stepData.remove(DEVICE);
            stepData.remove(DEVICE_ID);
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put(DEVICE, device);
            stepData.put(DEVICE_ID, device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I create {int} randomly named devices with BIOS version {string}")
    public void generateABunchOfTestDevices(int number, String version) throws Exception {
        DeviceCreator tmpDevCr;
        primeException();
        try {
            for (int i = 0; i < number; i++) {
                tmpDevCr = deviceFactory.newCreator(getCurrentScopeId(), "test_" + String.valueOf(random.nextInt()));
                tmpDevCr.setBiosVersion(version);
                deviceRegistryService.create(tmpDevCr);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I create {int} randomly named devices in scope {int}")
    public void generateABunchOfTestDevicesInScope(int number, int scope) throws Exception {
        DeviceCreator tmpDevCr;
        KapuaId tmpId;
        String tmpClient;
        primeException();
        try {
            for (int i = 0; i < number; i++) {
                tmpId = new KapuaEid(BigInteger.valueOf(scope));
                tmpClient = "test_" + String.valueOf(random.nextInt());
                tmpDevCr = deviceFactory.newCreator(tmpId, tmpClient);
                deviceRegistryService.create(tmpDevCr);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I create a device from the existing creator")
    public void createDeviceFromExistingCreator() throws Exception {
        DeviceCreator deviceCreator = (DeviceCreator) stepData.get(DEVICE_CREATOR);
        primeException();
        try {
            stepData.remove(DEVICE);
            stepData.remove(DEVICE_ID);
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put(DEVICE, device);
            stepData.put(DEVICE_ID, device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for a device with the remembered ID")
    public void findDeviceWithRememberedId() throws Exception {
        KapuaId deviceId = (KapuaId) stepData.get(DEVICE_ID);
        primeException();
        try {
            stepData.remove(DEVICE);
            Device device = deviceRegistryService.find(getCurrentScopeId(), deviceId);
            stepData.put(DEVICE, device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for a device with the client ID {string}")
    public void findDeviceWithClientId(String clientId) throws Exception {
        String client;
        if (clientId.trim().toLowerCase().equals("null")) {
            client = null;
        } else {
            client = clientId;
        }
        primeException();
        try {
            stepData.remove(DEVICE);
            Device device = deviceRegistryService.findByClientId(getCurrentScopeId(), client);
            stepData.put(DEVICE, device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I find searched device")
    public void findDevice() throws Exception {
         Assert.assertNotNull(stepData.get("Device"));
    }

    @When("I search for a device with a nonexisting registry ID")
    public void findDeviceWithRandomId() throws Exception {
        primeException();
        try {
            stepData.remove(DEVICE);
            Device device = deviceRegistryService.find(getCurrentScopeId(), getKapuaId());
            stepData.put(DEVICE, device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for a device with a random client ID")
    public void findDeviceWithRandomClientId() throws Exception {
        primeException();
        try {
            stepData.remove(DEVICE);
            Device device = deviceRegistryService.findByClientId(getCurrentScopeId(), String.valueOf(random.nextLong()));
            stepData.put(DEVICE, device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I perform the remembered query")
    public void queryForDevices() throws Exception {
        DeviceQuery tmpQuery = (DeviceQuery) stepData.get(DEVICE_QUERY);
        primeException();
        try {
            stepData.remove(DEVICE_LIST);
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put(DEVICE_LIST, deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I query for devices with BIOS version {string}")
    public void queryForDevicesBasedOnBiosVersion(String version) throws Exception {
        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.BIOS_VERSION, version, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(DEVICE_LIST);
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put(DEVICE_LIST, deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I query for devices with BIOS different from {string}")
    public void queryForDevicesWithDifferentBiosVersion(String version) throws Exception {
        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.BIOS_VERSION, version, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(DEVICE_LIST);
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put(DEVICE_LIST, deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I query for devices with Client Id {string}")
    public void queryForDevicesBasedOnClientId(String id) throws Exception {
        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, id, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(DEVICE_LIST);
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            stepData.put(DEVICE_LIST, deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I extract the device with correct BIOS version")
    public void getFirstDeviceFromList() {
        DeviceListResult deviceList = (DeviceListResult) stepData.get(DEVICE_LIST);
        // A device should have been found
        Assert.assertNotEquals(0, deviceList.getSize());
        stepData.put(DEVICE, deviceList.getFirstItem());
    }

    @When("I count the devices based on the remembered query")
    public void countForDevices() throws Exception {
        updateCount(() -> (int)deviceRegistryService.count((DeviceQuery) stepData.get(DEVICE_QUERY)));
    }

    @When("I count the devices in scope {int}")
    public void countDevicesInScope(int scope) throws Exception {
        updateCount(() -> (int)deviceRegistryService.count(deviceFactory.newQuery(getKapuaId(scope))));
    }

    @When("I count devices with BIOS version {string}")
    public void countDevicesWithBIOSVersion(String version) throws Exception {
        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.BIOS_VERSION, version, AttributePredicate.Operator.EQUAL));
        updateCount(() -> (int)deviceRegistryService.count(tmpQuery));
    }

    @When("I update some device parameters")
    public void updateDeviceParameters() throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        if (device != null) {
            device.setBiosVersion(device.getBiosVersion() + "_upd");
            device.setCustomAttribute1(device.getCustomAttribute1() + "_upd");
        }
        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I update the device cleint ID to {string}")
    public void updateDeviceClientId(String newId) throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        stepData.put("Text", device.getClientId());
        device.setClientId(newId);
        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I update a device with an invalid ID")
    public void updateDeviceWithInvalidId() throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        device.setId(getKapuaId());
        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete the device with the remembered ID")
    public void deleteDeviceWithRememberedId() throws Exception {
        KapuaId deviceId = (KapuaId) stepData.get(DEVICE_ID);
        primeException();
        try {
            deviceRegistryService.delete(getCurrentScopeId(), deviceId);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete the device with the clientId {string}")
    public void deleteDeviceWithClientId(String clientId) throws Exception {
        try {
            primeException();
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, clientId, AttributePredicate.Operator.EQUAL));
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            Device device = deviceList.getFirstItem();
            deviceRegistryService.delete(getCurrentScopeId(), device.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("When I delete a device with nonexisting ID")
    public void deleteDeviceWithRandomIds() throws Exception {
        primeException();
        try {
            deviceRegistryService.delete(getKapuaId(), getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("The device has a non-null ID")
    public void checkCreatedDeviceId() {
        DeviceCreator deviceCreator = (DeviceCreator) stepData.get(DEVICE_CREATOR);
        Device device = (Device) stepData.get(DEVICE);
        Assert.assertNotNull(device.getId());
        Assert.assertEquals(deviceCreator.getScopeId(), device.getScopeId());
        Assert.assertEquals(deviceCreator.getClientId(), device.getClientId());
    }

    @Then("It is possible to find the device based on its registry ID")
    public void findDeviceByRememberedId() throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        primeException();
        try {
            Device tmpDev = deviceRegistryService.find(getCurrentScopeId(), device.getId());
            Assert.assertEquals(device.getClientId(), tmpDev.getClientId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("I find the device based on clientID {string}")
    public void findDeviceByRememberedClientId(String clientID) throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        primeException();
        try {
            Device tmpDev = deviceRegistryService.findByClientId(getCurrentScopeId(), clientID);
            Assert.assertEquals(device.getId(), tmpDev.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("Named device registry searches are case sensitive")
    public void checkCaseSensitivnessOfRegistrySearches() throws Exception {
        DeviceCreator deviceCreator = (DeviceCreator) stepData.get(DEVICE_CREATOR);
        primeException();
        try {
            Assert.assertNull(deviceRegistryService.findByClientId(getCurrentScopeId(), deviceCreator.getClientId().toLowerCase()));
            Assert.assertNull(deviceRegistryService.findByClientId(getCurrentScopeId(), deviceCreator.getClientId().toUpperCase()));
            Assert.assertNotNull(deviceRegistryService.findByClientId(getCurrentScopeId(), deviceCreator.getClientId()));
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("The device matches the creator parameters")
    public void checkCreatedDeviceAgainstCreatorParameters() {
        DeviceCreator deviceCreator = (DeviceCreator) stepData.get(DEVICE_CREATOR);
        Device device = (Device) stepData.get(DEVICE);
        Assert.assertNotNull(device.getId());
        Assert.assertEquals(deviceCreator.getScopeId(), device.getScopeId());
        Assert.assertEquals(deviceCreator.getClientId().length(), device.getClientId().length());
        Assert.assertEquals(deviceCreator.getClientId(), device.getClientId());
        Assert.assertEquals(deviceCreator.getConnectionId(), device.getConnectionId());
        Assert.assertEquals(deviceCreator.getDisplayName(), device.getDisplayName());
        Assert.assertEquals(deviceCreator.getSerialNumber(), device.getSerialNumber());
        Assert.assertEquals(deviceCreator.getModelId(), device.getModelId());
        Assert.assertEquals(deviceCreator.getModelName(), device.getModelName());
        Assert.assertEquals(deviceCreator.getImei(), device.getImei());
        Assert.assertEquals(deviceCreator.getImsi(), device.getImsi());
        Assert.assertEquals(deviceCreator.getIccid(), device.getIccid());
        Assert.assertEquals(deviceCreator.getBiosVersion(), device.getBiosVersion());
        Assert.assertEquals(deviceCreator.getFirmwareVersion(), device.getFirmwareVersion());
        Assert.assertEquals(deviceCreator.getOsVersion(), device.getOsVersion());
        Assert.assertEquals(deviceCreator.getJvmVersion(), device.getJvmVersion());
        Assert.assertEquals(deviceCreator.getOsgiFrameworkVersion(), device.getOsgiFrameworkVersion());
        Assert.assertEquals(deviceCreator.getApplicationFrameworkVersion(), device.getApplicationFrameworkVersion());
        Assert.assertEquals(deviceCreator.getApplicationIdentifiers(), device.getApplicationIdentifiers());
        Assert.assertEquals(deviceCreator.getAcceptEncoding(), device.getAcceptEncoding());
        Assert.assertEquals(deviceCreator.getCustomAttribute1(), device.getCustomAttribute1());
        Assert.assertEquals(deviceCreator.getCustomAttribute2(), device.getCustomAttribute2());
        Assert.assertEquals(deviceCreator.getCustomAttribute3(), device.getCustomAttribute3());
        Assert.assertEquals(deviceCreator.getCustomAttribute4(), device.getCustomAttribute4());
        Assert.assertEquals(deviceCreator.getCustomAttribute5(), device.getCustomAttribute5());
        Assert.assertEquals(deviceCreator.getStatus(), device.getStatus());
    }

    @Then("The device was correctly updated")
    public void checkUpdatedDeviceAgainstOriginal() throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        Device tmpDevice;
        primeException();
        try {
            tmpDevice = deviceRegistryService.find(device.getScopeId(), device.getId());
            Assert.assertEquals(tmpDevice.getScopeId(), device.getScopeId());
            Assert.assertEquals(tmpDevice.getClientId().length(), device.getClientId().length());
            Assert.assertEquals(tmpDevice.getClientId(), device.getClientId());
            Assert.assertEquals(tmpDevice.getConnectionId(), device.getConnectionId());
            Assert.assertEquals(tmpDevice.getDisplayName(), device.getDisplayName());
            Assert.assertEquals(tmpDevice.getSerialNumber(), device.getSerialNumber());
            Assert.assertEquals(tmpDevice.getModelId(), device.getModelId());
            Assert.assertEquals(tmpDevice.getModelName(), device.getModelName());
            Assert.assertEquals(tmpDevice.getImei(), device.getImei());
            Assert.assertEquals(tmpDevice.getImsi(), device.getImsi());
            Assert.assertEquals(tmpDevice.getIccid(), device.getIccid());
            Assert.assertEquals(tmpDevice.getBiosVersion(), device.getBiosVersion());
            Assert.assertEquals(tmpDevice.getFirmwareVersion(), device.getFirmwareVersion());
            Assert.assertEquals(tmpDevice.getOsVersion(), device.getOsVersion());
            Assert.assertEquals(tmpDevice.getJvmVersion(), device.getJvmVersion());
            Assert.assertEquals(tmpDevice.getOsgiFrameworkVersion(), device.getOsgiFrameworkVersion());
            Assert.assertEquals(tmpDevice.getApplicationFrameworkVersion(), device.getApplicationFrameworkVersion());
            Assert.assertEquals(tmpDevice.getApplicationIdentifiers(), device.getApplicationIdentifiers());
            Assert.assertEquals(tmpDevice.getAcceptEncoding(), device.getAcceptEncoding());
            Assert.assertEquals(tmpDevice.getCustomAttribute1(), device.getCustomAttribute1());
            Assert.assertEquals(tmpDevice.getCustomAttribute2(), device.getCustomAttribute2());
            Assert.assertEquals(tmpDevice.getCustomAttribute3(), device.getCustomAttribute3());
            Assert.assertEquals(tmpDevice.getCustomAttribute4(), device.getCustomAttribute4());
            Assert.assertEquals(tmpDevice.getCustomAttribute5(), device.getCustomAttribute5());
            Assert.assertEquals(tmpDevice.getStatus(), device.getStatus());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("The device client id is {string}")
    public void checkDeviceClientName(String name) {
        Device device = (Device) stepData.get(DEVICE);
        Assert.assertEquals(name, device.getClientId());
    }

    @Then("I find {int} device(s)")
    public void checkListForNumberOfItems(int number) {
        DeviceListResult deviceList = (DeviceListResult) stepData.get(DEVICE_LIST);
        Assert.assertEquals(number, deviceList.getSize());
    }

    @Then("I find device {string}")
    public void iFindDeviceWithTag(String deviceName) {
        DeviceListResult deviceList = (DeviceListResult) stepData.get(DEVICE_LIST);
        Device device = deviceList.getFirstItem();
        Assert.assertNotNull(device);
        Assert.assertEquals(deviceName, device.getClientId());
    }

    @Then("The client ID was not changed")
    public void checkDeviceClientIdForChanges() throws Exception {
        Device device = (Device) stepData.get(DEVICE);
        String stringValue = (String) stepData.get("Text");
        primeException();
        try {
            Device tmpDevice = deviceRegistryService.find(getCurrentScopeId(), device.getId());
            Assert.assertNotEquals(device.getClientId(), tmpDevice.getClientId());
            Assert.assertEquals(stringValue, tmpDevice.getClientId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("There is no device with the client ID {string}")
    public void checkWhetherNamedDeviceStillExists(String clientId) throws KapuaException {
        Device tmpDevice = deviceRegistryService.findByClientId(getCurrentScopeId(), clientId);
        Assert.assertNull(tmpDevice);
    }

    @Then("There is no such device")
    public void noSuchDevice() {
        Assert.assertNull(stepData.get(DEVICE));
    }

    @Then("I find the device")
    public void deviceIsNotNull() {
        Assert.assertNotNull(stepData.get(DEVICE));
    }

    @Then("All device factory functions must return non null values")
    public void exerciseAllDeviceFactoryFunctions() {
        Device tmpDevice;
        DeviceCreator tmpCreator;
        DeviceQuery tmpQuery;
        DeviceListResult tmpListRes;
        tmpDevice = deviceFactory.newEntity(SYS_SCOPE_ID);
        tmpCreator = deviceFactory.newCreator(SYS_SCOPE_ID, "TestDevice");
        tmpQuery = deviceFactory.newQuery(SYS_SCOPE_ID);
        tmpListRes = deviceFactory.newListResult();
        Assert.assertNotNull(tmpDevice);
        Assert.assertNotNull(tmpCreator);
        Assert.assertNotNull(tmpQuery);
        Assert.assertNotNull(tmpListRes);
    }

    // ************************************************************************************
    // * Device Connection steps                                                          *
    // ************************************************************************************

    @When("I configure the device connection service")
    public void setDeviceConnectionConfigurationValue(List<CucConfig> cucConfigs) throws Exception {
        Map<String, Object> valueMap = new HashMap<>();
        KapuaId accId = getCurrentScopeId();
        KapuaId scopeId = getCurrentParentId();
        for (CucConfig config : cucConfigs) {
            config.addConfigToMap(valueMap);
            if (config.getParentId() != null) {
                scopeId = getKapuaId(config.getParentId());
            }
            if (config.getScopeId() != null) {
                accId = getKapuaId(config.getScopeId());
            }
        }
        primeException();
        try {
            deviceConnectionService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("A regular connection creator")
    public void createRegularCreator() {
        DeviceConnectionCreator connectionCreator = prepareRegularConnectionCreator(SYS_SCOPE_ID, getKapuaId());
        stepData.put(DEVICE_CONNECTION_CREATOR, connectionCreator);
    }

    @Given("A connection for scope (d+)")
    public void createConnectionInScope(int scope) throws Exception {
        DeviceConnectionCreator tmpCreator = prepareRegularConnectionCreator(getKapuaId(scope), getKapuaId());
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION);
            DeviceConnection connection = deviceConnectionService.create(tmpCreator);
            stepData.put(DEVICE_CONNECTION, connection);
        } catch (KapuaException ke) {
            verifyException(ke);
        }
    }

    @Given("I have the following connection(s)")
    public void createConnections(List<CucConnection> connections) throws Exception {
        KapuaId scopeId = getCurrentScopeId();
        KapuaId userId = getCurrentUserId();
        primeException();
        try {
            DeviceConnectionCreator connectionCreator = null;
            DeviceConnection connection = null;
            stepData.remove(DEVICE_CONNECTION);
            stepData.remove(DEVICE_CONNECTION_ID);
            stepData.remove(DEVICE_CONNECTION_LIST);
            for (CucConnection connItem : connections) {
                connectionCreator = deviceConnectionFactory.newCreator(scopeId);
                connectionCreator.setStatus(DeviceConnectionStatus.CONNECTED);
                connectionCreator.setUserId(userId);
                connectionCreator.setUserCouplingMode(ConnectionUserCouplingMode.LOOSE);
                connectionCreator.setClientId(connItem.getClientId());
                connectionCreator.setClientIp(connItem.getClientIp());
                connectionCreator.setServerIp(connItem.getServerIp());
                connectionCreator.setProtocol(connItem.getProtocol());
                connectionCreator.setAllowUserChange(false);
                connection = deviceConnectionService.create(connectionCreator);
            }
            stepData.put(DEVICE_CONNECTION_CREATOR, connectionCreator);
            stepData.put(DEVICE_CONNECTION, connection);
            stepData.put(DEVICE_CONNECTION_ID, connection.getId());
            DeviceConnectionListResult connList = deviceConnectionFactory.newListResult();
            Vector<DeviceConnection> vec = new Vector<>();
            vec.add(connection);
            connList.addItems(vec);
            stepData.put(DEVICE_CONNECTION_LIST, connList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I modify the connection details to")
    public void updateConnectionDetails(List<CucConnection> connections) throws Exception {
        // Only a single connection must be specified for this test!
        Assert.assertNotNull(connections);
        Assert.assertEquals(1, connections.size());
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        DeviceConnectionCreator connectionCreator = (DeviceConnectionCreator) stepData.get(DEVICE_CONNECTION_CREATOR);
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION);
            stepData.remove(DEVICE_CONNECTION_CREATOR);
            // try to modify the existing connection
            // Slight workaround for cucumber limitations: Remember the desired
            // connection settings via the global connectionCreator variable
            if (connections.get(0).getClientId() != null) {
                connection.setClientId(connections.get(0).getClientId());
                connectionCreator.setClientId(connections.get(0).getClientId());
            }
            if (connections.get(0).getClientIp() != null) {
                connection.setClientIp(connections.get(0).getClientIp());
                connectionCreator.setClientIp(connections.get(0).getClientIp());
            }
            if (connections.get(0).getServerIp() != null) {
                connection.setServerIp(connections.get(0).getServerIp());
                connectionCreator.setServerIp(connections.get(0).getServerIp());
            }
            if (connections.get(0).getProtocol() != null) {
                connection.setProtocol(connections.get(0).getProtocol());
                connectionCreator.setProtocol(connections.get(0).getProtocol());
            }
            DeviceConnection newConnection = deviceConnectionService.update(connection);
            stepData.put(DEVICE_CONNECTION, newConnection);
            stepData.put(DEVICE_CONNECTION_CREATOR, connectionCreator);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I try to modify the connection client Id to {string}")
    public void changeConnectionClientId(String client) throws Exception {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        // Remember the old client ID for later checking
        stepData.put("Text", connection.getClientId());
        // Update the connection client ID
        connection.setClientId(client);
        primeException();
        try {
            DeviceConnection newConnection = deviceConnectionService.update(connection);
            stepData.put(DEVICE_CONNECTION, newConnection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I try to modify the connection Id")
    public void changeConnectionIdRandomly() throws Exception {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        // Try to update the connection ID
        connection.setId(getKapuaId());
        primeException();
        try {
            deviceConnectionService.update(connection);
        } catch (KapuaException ex) {
            // Since the ID is not updatable there should be an exception
            verifyException(ex);
        }
    }

    @When("I create a new connection from the existing creator")
    public void createConnectionFromExistingCreator() throws Exception {
        DeviceConnectionCreator connectionCreator = (DeviceConnectionCreator) stepData.get(DEVICE_CONNECTION_CREATOR);
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION);
            DeviceConnection connection = deviceConnectionService.create(connectionCreator);
            stepData.put(DEVICE_CONNECTION, connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("The connection object is regular")
    public void checkConnectionObject() {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(connection.getId());
    }

    @Then("The connection object matches the creator")
    public void checkConnectionObjectAgainstCreator() {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        DeviceConnectionCreator connectionCreator = (DeviceConnectionCreator) stepData.get(DEVICE_CONNECTION_CREATOR);
        Assert.assertNotNull(connection);
        Assert.assertNotNull(connectionCreator);
        Assert.assertEquals(connectionCreator.getScopeId(), connection.getScopeId());
        Assert.assertEquals(connectionCreator.getClientId(), connection.getClientId());
        Assert.assertEquals(connectionCreator.getUserId(), connection.getUserId());
        Assert.assertEquals(connectionCreator.getUserCouplingMode(), connection.getUserCouplingMode());
        Assert.assertEquals(connectionCreator.getReservedUserId(), connection.getReservedUserId());
        Assert.assertEquals(connectionCreator.getAllowUserChange(), connection.getAllowUserChange());
        Assert.assertEquals(connectionCreator.getClientIp(), connection.getClientIp());
        Assert.assertEquals(connectionCreator.getServerIp(), connection.getServerIp());
        Assert.assertEquals(connectionCreator.getProtocol(), connection.getProtocol());
    }

    @Then("The connection status is {string}")
    public void checkDeviceConnectionStatus(String status) {
        DeviceConnectionStatus tmpStat = parseConnectionStatusString(status);
        DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get(DEVICE_CONNECTION_LIST);
        Assert.assertNotNull(tmpConnLst);
        Assert.assertNotEquals(0, tmpConnLst.getSize());
        DeviceConnection tmpConnection = tmpConnLst.getFirstItem();
        Assert.assertEquals(tmpStat, tmpConnection.getStatus());
    }

    @Then("I count {int} connections in scope {int}")
    public void countConnectioncInScope(int target, int scope) throws Exception {
        updateCountAndCheck(() -> (int)deviceConnectionService.count(deviceConnectionFactory.newQuery(getKapuaId(scope))), target);
    }

    @When("I search for a connection by scope and connection IDs")
    public void findConnectionByScopeAndConnectionId() throws Exception {
        KapuaId scopeId = getCurrentScopeId();
        KapuaId connectionId = (KapuaId) stepData.get(DEVICE_CONNECTION_ID);
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION);
            DeviceConnection connection = deviceConnectionService.find(scopeId, connectionId);
            stepData.put(DEVICE_CONNECTION, connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for a random connection ID")
    public void searchForARandomConnectionId() throws Exception {
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION);
            DeviceConnection connection = deviceConnectionService.find(getCurrentScopeId(), getKapuaId());
            stepData.put(DEVICE_CONNECTION, connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for a connection with the client ID {string}")
    public void findConnectionByClientId(String client) throws Exception {
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION);
            DeviceConnection connection = deviceConnectionService.findByClientId(getCurrentScopeId(), client);
            stepData.put(DEVICE_CONNECTION, connection);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete the existing connection")
    public void deleteExistingConnection() throws Exception {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        primeException();
        try {
            deviceConnectionService.delete(connection.getScopeId(), connection.getId());
            stepData.remove(DEVICE_CONNECTION);
        } catch (KapuaException ex) {
            verifyException(ex);
        }

    }

    @When("I try to delete a random connection ID")
    public void deleteRandomConnection() throws Exception {
        primeException();
        try {
            deviceConnectionService.delete(getCurrentScopeId(), getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I query for all connections with the parameter {string} set to {string}")
    public void cueryForConnections(String parameter, String value) throws Exception {
        DeviceConnectionQuery query = deviceConnectionFactory.newQuery(getCurrentScopeId());
        query.setPredicate(query.attributePredicate(parameter, value, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(DEVICE_CONNECTION_LIST);
            DeviceConnectionListResult connectionList = deviceConnectionService.query(query);
            Assert.assertNotNull(connectionList);
            stepData.put(DEVICE_CONNECTION_LIST, connectionList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("I find {int} connection(s)")
    public void checkResultListLength(int num) {
        DeviceConnectionListResult connectionList = (DeviceConnectionListResult) stepData.get(DEVICE_CONNECTION_LIST);
        Assert.assertNotNull(connectionList);
        Assert.assertEquals(num, connectionList.getSize());
    }

    @Then("The connection details match")
    public void checkConnectionDetails(List<CucConnection> connections) {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        // Only a single connection must be specified for this test!
        Assert.assertNotNull(connections);
        Assert.assertEquals(1, connections.size());
        // Slight workaround for cucumber limitations: The connection settings are
        // remembered via the global connection variable
        if (connections.get(0).getClientId() != null) {
            Assert.assertEquals(connections.get(0).getClientId(), connection.getClientId());
        }
        if (connections.get(0).getClientIp() != null) {
            Assert.assertEquals(connections.get(0).getClientIp(), connection.getClientIp());
        }
        if (connections.get(0).getServerIp() != null) {
            Assert.assertEquals(connections.get(0).getServerIp(), connection.getServerIp());
        }
        if (connections.get(0).getProtocol() != null) {
            Assert.assertEquals(connections.get(0).getProtocol(), connection.getProtocol());
        }
    }

    @Then("The connection client ID remains unchanged")
    public void checkThatClientIdHasNotChanged() {
        DeviceConnection connection = (DeviceConnection) stepData.get(DEVICE_CONNECTION);
        String text = (String) stepData.get("Text");
        Assert.assertEquals(text, connection.getClientId());
    }

    @Then("No connection was found")
    public void checkThatConnectionIsNull() {
        Assert.assertNull(stepData.get(DEVICE_CONNECTION));
    }

    @Then("All connection factory functions must return non null values")
    public void exerciseAllConnectionFactoryFunctions() {
        DeviceConnectionCreator tmpCreator = null;
        DeviceConnectionQuery tmpQuery = null;
        tmpCreator = deviceConnectionFactory.newCreator(SYS_SCOPE_ID);
        tmpQuery = deviceConnectionFactory.newQuery(SYS_SCOPE_ID);
        Assert.assertNotNull(tmpCreator);
        Assert.assertNotNull(tmpQuery);
    }

    @Then("The device connection domain defaults are correctly initialized")
    public void checkConnectionDomainInitialization() {
        DeviceConnectionDomain tmpDomain = new DeviceConnectionDomain();
        Assert.assertEquals("device_connection", tmpDomain.getName());
        Assert.assertEquals(3, tmpDomain.getActions().size());
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.read));
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.write));
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.delete));
    }

    @Then("The device connection domain data can be updated")
    public void checkDeviceConnectionDomainUpdate() {
        Domain tmpDomain = new TestDomain();
        tmpDomain.setName(TEST_DEVICE_NAME);
        tmpDomain.setActions(new HashSet<>(Lists.newArrayList(Actions.connect, Actions.execute)));
        Assert.assertEquals(TEST_DEVICE_NAME, tmpDomain.getName());
        Assert.assertEquals(2, tmpDomain.getActions().size());
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.connect));
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.execute));
    }

    // ************************************************************************************
    // * Device Event steps                                                               *
    // ************************************************************************************

    @Given("An event creator with null action")
    public void prepareCreatorWithNullAction() {
        DeviceEventCreator eventCreator = prepareRegularDeviceEventCreator(getCurrentScopeId(), getKapuaId());
        eventCreator.setAction(null);
        stepData.put(DEVICE_EVENT_CREATOR, eventCreator);
    }

    @Given("A {string} event from device {string}")
    public void createRegularEvent(String eventType, String clientId) throws Exception {
        primeException();
        try {
            Device tmpDev = getDeviceWithClientId(clientId);
            DeviceEventCreator eventCreator = prepareRegularDeviceEventCreator(getCurrentScopeId(), tmpDev.getId());
            KapuaMethod tmpMeth = getMethodFromString(eventType);
            eventCreator.setAction(tmpMeth);
            stepData.put(DEVICE_EVENT_CREATOR, eventCreator);
            stepData.remove(DEVICE_EVENT);
            stepData.remove(DEVICE_EVENT_ID);
            DeviceEvent event = eventService.create(eventCreator);
            stepData.put(DEVICE_EVENT, event);
            stepData.put(DEVICE_EVENT_ID, event.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I have {int} {string} events from device {string}")
    public void createANumberOfEvents(int num, String eventType, String clientId) throws Exception {
        primeException();
        try {
            KapuaId currScopeId = getCurrentScopeId();
            KapuaId tmpDevId = getDeviceWithClientId(clientId).getId();
            KapuaMethod tmpMeth = getMethodFromString(eventType);
            DeviceEventCreator tmpCreator;
            DeviceEvent tmpEvent;
            for (int i = 0; i < num; i++) {
                tmpCreator = prepareRegularDeviceEventCreator(currScopeId, tmpDevId);
                Assert.assertNotNull(tmpCreator);
                tmpCreator.setAction(tmpMeth);
                tmpEvent = eventService.create(tmpCreator);
                Assert.assertNotNull(tmpEvent);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I create an event from the existing creator")
    public void createEventFromCreator() throws Exception {
        DeviceEventCreator eventCreator = (DeviceEventCreator) stepData.get(DEVICE_EVENT_CREATOR);
        primeException();
        try {
            stepData.remove(DEVICE_EVENT);
            stepData.remove(DEVICE_EVENT_ID);
            DeviceEvent event = eventService.create(eventCreator);
            stepData.put(DEVICE_EVENT, event);
            stepData.put(DEVICE_EVENT_ID, event.getId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for an event with the remembered ID")
    public void findEventById() throws Exception {
        KapuaId eventId = (KapuaId) stepData.get(DEVICE_EVENT_ID);
        primeException();
        try {
            stepData.remove(DEVICE_EVENT);
            DeviceEvent event = eventService.find(getCurrentScopeId(), eventId);
            stepData.put(DEVICE_EVENT, event);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for an event with a random ID")
    public void findEventByRandomId() throws Exception {
        primeException();
        try {
            stepData.remove(DEVICE_EVENT);
            DeviceEvent event = eventService.find(getCurrentScopeId(), getKapuaId());
            stepData.put(DEVICE_EVENT, event);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete the event with the remembered ID")
    public void deleteEvent() throws Exception {
        KapuaId eventId = (KapuaId) stepData.get(DEVICE_EVENT_ID);
        primeException();
        try {
            eventService.delete(getCurrentScopeId(), eventId);
            stepData.remove(DEVICE_EVENT);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I delete an event with a random ID")
    public void deleteEventWithRandomId() throws Exception {
        primeException();
        try {
            eventService.delete(getCurrentScopeId(), getKapuaId());
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I count events for scope {int}")
    public void countEventsInScope(int scpId) throws Exception {
        updateCount(() -> (int)eventService.count(eventFactory.newQuery(getKapuaId(scpId))));
    }

    @When("I query for {string} events")
    public void queryForSpecificEvents(String eventType) throws Exception {
        DeviceEventQuery tmpQuery = eventFactory.newQuery(getCurrentScopeId());
        Assert.assertNotNull(tmpQuery);
        KapuaMethod tmpMeth = getMethodFromString(eventType);
        Assert.assertNotNull(tmpMeth);
        tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceEventAttributes.ACTION, tmpMeth, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(DEVICE_EVENT_LIST);
            DeviceEventListResult eventList = eventService.query(tmpQuery);
            stepData.put(DEVICE_EVENT_LIST, eventList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I untag device with {string} tag")
    public void iDeleteTag(String deviceTagName) throws Throwable {
        Tag foundTag = (Tag) stepData.get("tag");
        Assert.assertEquals(deviceTagName, foundTag.getName());
        Device device = (Device) stepData.get(DEVICE);
        stepData.remove("tag");
        stepData.remove("tags");
        Set<KapuaId> tags = new HashSet<>();
        device.setTagIds(tags);
        Device updatedDevice = deviceRegistryService.update(device);
        stepData.put(DEVICE, updatedDevice);
        Assert.assertEquals(device.getTagIds().isEmpty(), true);
    }

    @And("I verify that tag {string} is deleted")
    public void iVerifyTagIsDeleted(String deviceTagName) throws Throwable {
        Tag foundTag = (Tag) stepData.get("tag");
        Assert.assertEquals(null, foundTag);
    }

    @When("I search for events from device {string} in account {string}")
    public void searchForEventsGreaterFromDeviceWithClientID(String clientId, String accountName) throws Exception {
        try {
            Account account = accountService.findByName(accountName);
            Assert.assertNotNull(account);
            Device device = deviceRegistryService.findByClientId(account.getId(), clientId);
            Assert.assertNotNull(device);
            DeviceEventQuery eventQuery = eventFactory.newQuery(account.getId());
            eventQuery.setPredicate(eventQuery.attributePredicate(DeviceEventAttributes.DEVICE_ID, device.getId(), AttributePredicate.Operator.EQUAL));
            eventQuery.setSortCriteria(eventQuery.fieldSortCriteria(DeviceEventAttributes.RECEIVED_ON, SortOrder.ASCENDING));
            DeviceEventListResult deviceEventList = eventService.query(eventQuery);
            stepData.put(DEVICE_EVENT_LIST, deviceEventList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for events from device {string} in account {string} I find {int} or more event(s) within {int} second(s)")
    public void searchForEventsGreaterFromDeviceWithClientID(String clientId, String account, int events, int timeout) throws Exception {
        searchForEventsFromDeviceWithClientIDInternal(clientId, account, events, timeout, true);
    }

    @When("I search for events from device {string} in account {string} I find {int} event(s) within {int} second(s)")
    public void searchForEventsFromDeviceWithClientID(String clientId, String account, int events, int timeout) throws Exception {
        searchForEventsFromDeviceWithClientIDInternal(clientId, account, events, timeout, false);
    }

    private void searchForEventsFromDeviceWithClientIDInternal(String clientId, String account, int events, int timeout, boolean greater) throws Exception {
        int executions = 0;
        while (executions++ < timeout) {
            if (searchForEventsFromDeviceWithClientID(clientId, account, events, false, greater)) {
                return;
            }
            Thread.sleep(1000);
        }
        searchForEventsFromDeviceWithClientID(clientId, account, events, true, greater);
    }

    private boolean searchForEventsFromDeviceWithClientID(String clientId, String accountName, int events, boolean timeoutOccurred, boolean greater) throws Exception {
        DeviceEventListResult deviceEventList = null;
        try {
            Account account = accountService.findByName(accountName);
            if (timeoutOccurred) {
                Assert.assertNotNull(account);
            }
            else if (account==null) {
                return false;
            }
            Device device = deviceRegistryService.findByClientId(account.getId(), clientId);
            if (timeoutOccurred) {
                Assert.assertNotNull(device);
            }
            else if (device==null) {
                return false;
            }
            DeviceEventQuery eventQuery = eventFactory.newQuery(account.getId());
            eventQuery.setPredicate(eventQuery.attributePredicate(DeviceEventAttributes.DEVICE_ID, device.getId(), AttributePredicate.Operator.EQUAL));
            eventQuery.setSortCriteria(eventQuery.fieldSortCriteria(DeviceEventAttributes.RECEIVED_ON, SortOrder.ASCENDING));
            deviceEventList = eventService.query(eventQuery);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
        stepData.put(DEVICE_EVENT_LIST, deviceEventList);
        if (timeoutOccurred) {
            printEvents(deviceEventList, events);
            if (greater) {
                Assert.assertEquals("Wrong device events count", events, deviceEventList.getSize());
            }
            else {
                Assert.assertTrue("Wrong device events count", events<=deviceEventList.getSize());
            }
        }
        return greater ? events<=deviceEventList.getSize() : events==deviceEventList.getSize();
    }

    @Then("The event matches the creator parameters")
    public void checkCreatedEventAgainstCreatorParameters() {
        DeviceEventCreator eventCreator = (DeviceEventCreator) stepData.get(DEVICE_EVENT_CREATOR);
        DeviceEvent event = (DeviceEvent) stepData.get(DEVICE_EVENT);
        Assert.assertNotNull(event.getId());
        Assert.assertEquals(eventCreator.getScopeId(), event.getScopeId());
        Assert.assertEquals(eventCreator.getDeviceId(), event.getDeviceId());
        Assert.assertEquals(eventCreator.getSentOn(), event.getSentOn());
        Assert.assertEquals(eventCreator.getReceivedOn(), event.getReceivedOn());
        Assert.assertEquals(eventCreator.getResource(), event.getResource());
        Assert.assertEquals(eventCreator.getResponseCode(), event.getResponseCode());
        Assert.assertEquals(eventCreator.getEventMessage(), event.getEventMessage());
        Assert.assertEquals(eventCreator.getAction(), event.getAction());
        Assert.assertEquals(eventCreator.getPosition().toDisplayString(),
                event.getPosition().toDisplayString());
    }

    @Then("The type of the last event is {string}")
    public void checkLastEventType(String type) {
        DeviceEventListResult tmpList;
        Assert.assertNotNull(stepData.get(DEVICE_EVENT_LIST));
        Assert.assertNotEquals(0, ((DeviceEventListResult) stepData.get(DEVICE_EVENT_LIST)).getSize());
        tmpList = (DeviceEventListResult) stepData.get(DEVICE_EVENT_LIST);
        Assert.assertEquals(type.trim().toUpperCase(), tmpList.getItem(tmpList.getSize() - 1).getResource().trim().toUpperCase());
    }

    @Then("I find {int} device event(s)")
    public void checkEventListForNumberOfItems(int numberOfEvents) throws Exception {
        DeviceEventListResult eventList = (DeviceEventListResult) stepData.get(DEVICE_EVENT_LIST);
        printEvents(eventList, numberOfEvents);
        Assert.assertEquals(numberOfEvents, eventList.getSize());
    }

    @Then("I find {int} or more device event(s)")
    public void checkEventList(int number) throws Exception {
        DeviceEventListResult eventList = (DeviceEventListResult) stepData.get(DEVICE_EVENT_LIST);
        printEvents(eventList, number);
        Assert.assertTrue(eventList.getSize() >= number);
    }

    private void printEvents(DeviceEventListResult eventList, int count) throws Exception {
        logger.info("Events sie: {}", eventList.getSize());
        eventList.getItems().forEach((event) -> logger.info("\ttype: {} - id: {} - date: {} - {}", event.getType(), event.getDeviceId(), event.getCreatedOn(), event.getEventMessage()));
//        if (count > eventList.getSize()) {
//            logger.info("++++++++++++++++++++++++++++++++++++++++++++++++\n===========================================================\n\n");
//            Thread.sleep(30000);
//            searchForEventsFromDeviceWithClientID("rpione3", "kapua-sys" );
//            eventList = (DeviceEventListResult) stepData.get(DEVICE_EVENT_LIST);
//            eventList.getItems().forEach((event) -> logger.info("\ttype: {} - id: {} - date: {} - {}", event.getType(), event.getDeviceId(), event.getCreatedOn(), event.getEventMessage()));
//            logger.info("++++++++++++++++++++++++++++++++++++++++++++++++\n===========================================================\n\n");
//        }
    }

    @Then("There is no such event")
    public void eventIsNull() {
        Assert.assertNull(stepData.get(DEVICE_EVENT));
    }

    @Then("All device event factory functions must return non null objects")
    public void exerciseAllEventFactoryFunctions() {
        DeviceEvent tmpEvent = null;
        DeviceEventCreator tmpCreator = null;
        DeviceEventQuery tmpQuery = null;
        DeviceEventListResult tmpList = null;
        tmpEvent = eventFactory.newEntity(SYS_SCOPE_ID);
        tmpCreator = eventFactory.newCreator(SYS_SCOPE_ID, getKapuaId(), new Date(), "");
        tmpQuery = eventFactory.newQuery(SYS_SCOPE_ID);
        tmpList = eventFactory.newListResult();
        Assert.assertNotNull(tmpEvent);
        Assert.assertNotNull(tmpCreator);
        Assert.assertNotNull(tmpQuery);
        Assert.assertNotNull(tmpList);
    }

    @Then("The device event domain data can be updated")
    public void checkDeviceEventDomainUpdate() {
        Domain tmpDomain = new TestDomain();
        tmpDomain.setName(TEST_DEVICE_NAME);
        tmpDomain.setActions(new HashSet<>(Lists.newArrayList(Actions.connect, Actions.execute)));
        Assert.assertEquals(TEST_DEVICE_NAME, tmpDomain.getName());
        Assert.assertEquals(2, tmpDomain.getActions().size());
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.connect));
        Assert.assertTrue(tmpDomain.getActions().contains(Actions.execute));
    }

    // ************************************************************************************
    // * Device integration steps                                                         *
    // ************************************************************************************

    @When("I search for the device {string} in account {string}")
    public void searchForDeviceWithClientID(String clientId, String account) throws Exception {
        DeviceListResult tmpList = deviceFactory.newListResult();
        primeException();
        try {
            stepData.remove(DEVICE);
            stepData.remove(DEVICE_LIST);
            Account tmpAcc = accountService.findByName(account);
            Device tmpDev = deviceRegistryService.findByClientId(tmpAcc.getId(), clientId);
            if (tmpDev != null) {
                Vector<Device> dv = new Vector<>();
                dv.add(tmpDev);
                tmpList.addItems(dv);
            }
            stepData.put(DEVICE, tmpDev);
            stepData.put(DEVICE_LIST, tmpList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I tag device with {string} tag")
    public void iTagDeviceWithTag(String deviceTagName) throws Exception {
        Account account = (Account) stepData.get(LAST_ACCOUNT);
        Device device = (Device) stepData.get(DEVICE);
        primeException();
        try {
            TagCreator tagCreator = tagFactory.newCreator(account.getId());
            tagCreator.setName(deviceTagName);
            Tag tag = tagService.create(tagCreator);
            Set<KapuaId> tags = new HashSet<>();
            tags.add(tag.getId());
            device.setTagIds(tags);
            Device updatedDevice = deviceRegistryService.update(device);
            stepData.put("tag", tag);
            stepData.put("tags", tags);
            stepData.put(DEVICE, updatedDevice);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @When("I search for device with tag {string}")
    public void iSearchForDeviceWithTag(String deviceTagName) throws Exception {
        Account lastAcc = (Account) stepData.get(LAST_ACCOUNT);
        DeviceQuery deviceQuery = deviceFactory.newQuery(lastAcc.getId());
        TagQuery tagQuery = tagFactory.newQuery(lastAcc.getId());
        tagQuery.setPredicate(tagQuery.attributePredicate(TagAttributes.NAME, deviceTagName, AttributePredicate.Operator.EQUAL));
        primeException();
        try {
            stepData.remove(DEVICE_LIST);
            TagListResult tagQueryResult = tagService.query(tagQuery);
            Tag tag = tagQueryResult.getFirstItem();
            deviceQuery.setPredicate(deviceQuery.attributePredicate(DeviceAttributes.TAG_IDS, tag.getId(), AttributePredicate.Operator.EQUAL));
            DeviceListResult deviceList = deviceRegistryService.query(deviceQuery);
            stepData.put(DEVICE_LIST, deviceList);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("A birth message from device {string}")
    public void createABirthMessage(String clientId) throws KapuaException {
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);
        Assert.assertNotNull(clientId);
        Assert.assertFalse(clientId.isEmpty());
        Assert.assertNotNull(tmpAccount);
        Assert.assertNotNull(tmpAccount.getId());
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaBirthMessage tmpMsg = lifecycleMessageFactory.newKapuaBirthMessage();
        KapuaBirthChannel tmpChan = lifecycleMessageFactory.newKapuaBirthChannel();
        KapuaBirthPayload tmpPayload = prepareDefaultBirthPayload();
        tmpChan.setClientId(clientId);
        tmpSemParts.add(PART1);
        tmpSemParts.add(PART2);
        tmpChan.setSemanticParts(tmpSemParts);
        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setClientId(clientId);
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());
        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }
        deviceLifeCycleService.birth(getKapuaId(), tmpMsg);
    }

    @Given("A disconnect message from device {string}")
    public void createADeathMessage(String clientId) throws Exception {
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaDisconnectMessage tmpMsg = lifecycleMessageFactory.newKapuaDisconnectMessage();
        KapuaDisconnectChannel tmpChan = lifecycleMessageFactory.newKapuaDisconnectChannel();
        KapuaDisconnectPayload tmpPayload = prepareDefaultDeathPayload();
        tmpChan.setClientId(clientId);
        tmpSemParts.add(PART1);
        tmpSemParts.add(PART2);
        tmpChan.setSemanticParts(tmpSemParts);
        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setClientId(clientId);
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());
        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }
        try {
            primeException();
            deviceLifeCycleService.death(getKapuaId(), tmpMsg);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("A missing message from device {string}")
    public void createAMissingMessage(String clientId) throws Exception {
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaMissingMessage tmpMsg = lifecycleMessageFactory.newKapuaMissingMessage();
        KapuaMissingChannel tmpChan = lifecycleMessageFactory.newKapuaMissingChannel();
        KapuaMissingPayload tmpPayload = prepareDefaultMissingPayload();
        tmpChan.setClientId(clientId);
        tmpSemParts.add(PART1);
        tmpSemParts.add(PART2);
        tmpChan.setSemanticParts(tmpSemParts);
        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());
        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }
        try {
            primeException();
            deviceLifeCycleService.missing(getKapuaId(), tmpMsg);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("An application message from device {string}")
    public void createAnApplicationMessage(String clientId) throws Exception {
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);
        Device tmpDev;
        List<String> tmpSemParts = new ArrayList<>();
        KapuaAppsMessage tmpMsg = lifecycleMessageFactory.newKapuaAppsMessage();
        KapuaAppsChannel tmpChan = lifecycleMessageFactory.newKapuaAppsChannel();
        KapuaAppsPayload tmpPayload = prepareDefaultApplicationPayload();
        tmpChan.setClientId(clientId);
        tmpSemParts.add(PART1);
        tmpSemParts.add(PART2);
        tmpChan.setSemanticParts(tmpSemParts);
        tmpMsg.setChannel(tmpChan);
        tmpMsg.setPayload(tmpPayload);
        tmpMsg.setScopeId(tmpAccount.getId());
        tmpMsg.setId(UUID.randomUUID());
        tmpMsg.setReceivedOn(new Date());
        tmpMsg.setPosition(getDefaultPosition());
        tmpDev = deviceRegistryService.findByClientId(tmpAccount.getId(), clientId);
        if (tmpDev != null) {
            tmpMsg.setDeviceId(tmpDev.getId());
        } else {
            tmpMsg.setDeviceId(null);
        }
        try {
            primeException();
            deviceLifeCycleService.applications(getKapuaId(), tmpMsg);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }


    @Given("Such a set of privileged users for account {string}")
    public void createPrivilegedUsers(String accName, List<CucUser> users) throws Throwable {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(accName);
            for (CucUser tmpTestUsr : users) {
                User tmpUser = aclCreator.createUser(account, tmpTestUsr.getName());
                if ((tmpTestUsr.getPassword() != null) && !tmpTestUsr.getPassword().isEmpty()) {
                    aclCreator.attachUserCredentials(account, tmpUser, tmpTestUsr.getPassword());
                } else {
                    aclCreator.attachUserCredentials(account, tmpUser);
                }
                aclCreator.attachFullPermissions(account, tmpUser);
            }
        });
    }

    @Given("A full set of device privileges for account {string}")
    public void setAccountDevicePrivileges(String name) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(name);
            Map<String, Object> valueMap = new HashMap<>();
            valueMap.put("infiniteChildEntities", true);
            valueMap.put("maxNumberChildEntities", 1000);
            deviceRegistryService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        });
    }

    @Given("The default connection coupling mode for account {string} is set to {string}")
    public void setDeviceConnectionCouplingMode(String name, String mode) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(name);
            Map<String, Object> valueMap = new HashMap<>();
            //            valueMap.put("infiniteChildEntities", true);
            //            valueMap.put("maxNumberChildEntities", 1000);
            valueMap.put("deviceConnectionUserCouplingDefaultMode", mode);
            deviceConnectionService.setConfigValues(account.getId(), account.getScopeId(), valueMap);
        });
    }

    @Given("The following device connections")
    public void createConnectionForDevice(List<CucConnection> connections) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            for (CucConnection tmpConn : connections) {
                DeviceConnectionCreator tmpCreator = deviceConnectionFactory.newCreator(tmpConn.getScopeId());
                tmpCreator.setStatus(DeviceConnectionStatus.CONNECTED);
                tmpCreator.setClientId(tmpConn.getClientId());
                tmpCreator.setUserId(tmpConn.getUserId());
                tmpCreator.setReservedUserId(tmpConn.getReservedUserId());
                tmpCreator.setAllowUserChange(tmpConn.getAllowUserChange());
                tmpCreator.setUserCouplingMode(tmpConn.getUserCouplingMode());
                DeviceConnection tmpDevConn = deviceConnectionService.create(tmpCreator);
                tmpDevConn.setStatus(DeviceConnectionStatus.DISCONNECTED);
                deviceConnectionService.update(tmpDevConn);
            }
        });
    }

    @Given("I wait for {int} seconds")
    public void waitForSpecifiedTime(int delay) throws InterruptedException {
        Thread.sleep(delay * 1000);
    }

    @When("I search for a connection from the device {string} in account {string}")
    public void searchForConnectionFromDeviceWithClientID(String clientId, String account) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc;
            DeviceConnection tmpConn;
            DeviceConnectionListResult tmpConnLst = deviceConnectionFactory.newListResult();
            tmpAcc = accountService.findByName(account);
            Assert.assertNotNull(tmpAcc);
            Assert.assertNotNull(tmpAcc.getId());
            tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), clientId);
            Map<String, Object> props = deviceRegistryService.getConfigValues(tmpAcc.getId());
            stepData.put(DEVICE_CONNECTION, tmpConn);
            if (tmpConn != null) {
                Vector<DeviceConnection> dcv = new Vector<>();
                dcv.add(tmpConn);
                tmpConnLst.addItems(dcv);
            }
            stepData.put(DEVICE_CONNECTION_LIST, tmpConnLst);
        });
    }

    @Then("The connection user is {string}")
    public void checkDeviceConnectionUser(String user) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            DeviceConnectionListResult tmpConnLst = (DeviceConnectionListResult) stepData.get(DEVICE_CONNECTION_LIST);
            User tmpUsr = userService.findByName(user);
            Assert.assertNotNull(tmpConnLst);
            Assert.assertNotEquals(0, tmpConnLst.getSize());
            DeviceConnection tmpConnection = tmpConnLst.getFirstItem();
            Assert.assertEquals(tmpUsr.getId(), tmpConnection.getUserId());
        });
    }

    @When("I set the connection status from the device {string} in account {string} to {string}")
    public void modifyDeviceConnectionStatus(String device, String account, String status) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            DeviceConnectionStatus tmpStat = parseConnectionStatusString(status);
            Assert.assertNotNull(tmpStat);
            Assert.assertNotNull(tmpConn);
            tmpConn.setStatus(tmpStat);
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("I set the user coupling mode for the connection from device {string} in account {string} to {string}")
    public void modifyDeviceConnectionCouplingMode(String device, String account, String mode) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            ConnectionUserCouplingMode tmpMode = parseConnectionCouplingString(mode);
            Assert.assertNotNull(tmpMode);
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            Assert.assertNotNull(tmpConn);
            tmpConn.setUserCouplingMode(tmpMode);
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("I set the user change flag for the connection from device {string} in account {string} to {string}")
    public void modifyDeviceConnectionUserChangeFlag(String device, String account, String flag) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(account);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            Assert.assertNotNull(tmpConn);
            tmpConn.setAllowUserChange(parseBooleanFromString(flag));
            deviceConnectionService.update(tmpConn);
        });
    }

    @When("I set the reserved user for the connection from device {string} in account {string} to {string}")
    public void modifyDeviceConnectionReservedUser(String device, String scope, String resUser) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account tmpAcc = accountService.findByName(scope);
            DeviceConnection tmpConn = deviceConnectionService.findByClientId(tmpAcc.getId(), device);
            KapuaId userId;
            Assert.assertNotNull(tmpConn);
            if (resUser.isEmpty() || resUser.trim().toLowerCase().contains("null")) {
                userId = null;
            } else {
                userId = userService.findByName(resUser).getId();
            }
            tmpConn.setReservedUserId(userId);
            stepData.put("ExceptionCaught", false);
            try {
                primeException();
                deviceConnectionService.update(tmpConn);
            } catch (KapuaException ex) {
                verifyException(ex);
            } catch (Exception e) {
                int a = 10;
            }
        });
    }

    @Then("The user for the connection from device {string} in scope {string} is {string}")
    public void checkUserForExistingConnection(String device, String scope, String name) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            Account account = accountService.findByName(scope);
            DeviceConnection connection = deviceConnectionService.findByClientId(account.getId(), device);
            User user = userService.findByName(name);
            Assert.assertNotNull(connection);
            Assert.assertNotNull(user);
            Assert.assertEquals(user.getId(), connection.getUserId());
        });
    }

    // *******************
    // * Private Helpers *
    // *******************

    // Create a device creator object. The creator is pre-filled with default data.
    private DeviceCreator prepareRegularDeviceCreator(KapuaId accountId, String client) {
        DeviceCreator tmpDeviceCreator = deviceFactory.newCreator(accountId, client);
        tmpDeviceCreator.setConnectionId(getKapuaId());
        tmpDeviceCreator.setDisplayName(TEST_DEVICE_NAME);
        tmpDeviceCreator.setSerialNumber("serialNumber");
        tmpDeviceCreator.setModelId("modelId");
        tmpDeviceCreator.setImei(getRandomString());
        tmpDeviceCreator.setImsi(getRandomString());
        tmpDeviceCreator.setIccid(getRandomString());
        tmpDeviceCreator.setBiosVersion("biosVersion");
        tmpDeviceCreator.setFirmwareVersion("firmwareVersion");
        tmpDeviceCreator.setOsVersion("osVersion");
        tmpDeviceCreator.setJvmVersion("jvmVersion");
        tmpDeviceCreator.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDeviceCreator.setApplicationFrameworkVersion("kapuaVersion");
        tmpDeviceCreator.setApplicationIdentifiers("applicationIdentifiers");
        tmpDeviceCreator.setAcceptEncoding("acceptEncoding");
        tmpDeviceCreator.setCustomAttribute1("customAttribute1");
        tmpDeviceCreator.setCustomAttribute2("customAttribute2");
        tmpDeviceCreator.setCustomAttribute3("customAttribute3");
        tmpDeviceCreator.setCustomAttribute4("customAttribute4");
        tmpDeviceCreator.setCustomAttribute5("customAttribute5");
        tmpDeviceCreator.setStatus(DeviceStatus.ENABLED);
        return tmpDeviceCreator;
    }

    // Create a device object. The device is pre-filled with default data.
    private Device prepareRegularDevice(KapuaId accountId, KapuaId deviceId) {
        Device tmpDevice = deviceFactory.newEntity(accountId);
        tmpDevice.setId(deviceId);
        tmpDevice.setConnectionId(getKapuaId());
        tmpDevice.setDisplayName(TEST_DEVICE_NAME);
        tmpDevice.setSerialNumber("serialNumber");
        tmpDevice.setModelId("modelId");
        tmpDevice.setImei(getRandomString());
        tmpDevice.setImsi(getRandomString());
        tmpDevice.setIccid(getRandomString());
        tmpDevice.setBiosVersion("biosVersion");
        tmpDevice.setFirmwareVersion("firmwareVersion");
        tmpDevice.setOsVersion("osVersion");
        tmpDevice.setJvmVersion("jvmVersion");
        tmpDevice.setOsgiFrameworkVersion("osgiFrameworkVersion");
        tmpDevice.setApplicationFrameworkVersion("kapuaVersion");
        tmpDevice.setApplicationIdentifiers("applicationIdentifiers");
        tmpDevice.setAcceptEncoding("acceptEncoding");
        tmpDevice.setCustomAttribute1("customAttribute1");
        tmpDevice.setCustomAttribute2("customAttribute2");
        tmpDevice.setCustomAttribute3("customAttribute3");
        tmpDevice.setCustomAttribute4("customAttribute4");
        tmpDevice.setCustomAttribute5("customAttribute5");
        tmpDevice.setStatus(DeviceStatus.ENABLED);
        return tmpDevice;
    }

    // Create a connection creator object. The creator is pre-filled with default data.
    private DeviceConnectionCreator prepareRegularConnectionCreator(KapuaId scopeId, KapuaId userId) {
        DeviceConnectionCreator creator = deviceConnectionFactory.newCreator(scopeId);
        creator.setUserId(userId);
        creator.setUserCouplingMode(ConnectionUserCouplingMode.LOOSE);
        creator.setReservedUserId(userId);
        creator.setClientId(CLIENT_NAME);
        creator.setClientIp(CLIENT_IP);
        creator.setServerIp(SERVER_IP);
        creator.setProtocol("tcp");
        creator.setAllowUserChange(false);
        return creator;
    }

    // Create a event creator object. The creator is pre-filled with default data.
    private DeviceEventCreator prepareRegularDeviceEventCreator(KapuaId accountId, KapuaId deviceId) {
        DeviceEventCreator tmpCreator = eventFactory.newCreator(accountId);
        KapuaPosition tmpPosition = messageFactory.newPosition();
        Date timeReceived = new Date();
        Date timeSent = new Date(System.currentTimeMillis() - 5 * 60 * 1000);
        tmpCreator.setDeviceId(deviceId);
        tmpCreator.setSentOn(timeSent);
        tmpCreator.setReceivedOn(timeReceived);
        tmpCreator.setAction(KapuaMethod.CREATE);
        tmpCreator.setResource("resource");
        tmpCreator.setResponseCode(KapuaResponseCode.ACCEPTED);
        tmpCreator.setEventMessage("test_message_hello_world");
        tmpPosition.setLatitude(46.4);
        tmpPosition.setLongitude(13.02);
        tmpPosition.setAltitude(323.0);
        tmpPosition.setSpeed(50.0);
        tmpPosition.setHeading(0.0);
        tmpPosition.setPrecision(0.15);
        tmpPosition.setSatellites(16);
        tmpPosition.setStatus(7);
        tmpPosition.setTimestamp(timeSent);
        tmpCreator.setPosition(tmpPosition);
        return tmpCreator;
    }

    private KapuaMethod getMethodFromString(String name) {
        KapuaMethod tmpMeth = null;
        switch (name.trim().toUpperCase()) {
            case "READ":
                tmpMeth = KapuaMethod.READ;
                break;
            case "CREATE":
                tmpMeth = KapuaMethod.CREATE;
                break;
            case "WRITE":
                tmpMeth = KapuaMethod.WRITE;
                break;
            case "DELETE":
                tmpMeth = KapuaMethod.DELETE;
                break;
            case "OPTIONS":
                tmpMeth = KapuaMethod.OPTIONS;
                break;
            case "EXECUTE":
                tmpMeth = KapuaMethod.EXECUTE;
                break;
        }
        Assert.assertNotNull(tmpMeth);
        return tmpMeth;
    }

    private Device getDeviceWithClientId(String clientId) throws KapuaException {
        DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
        // Search for the known bios version string
        tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, clientId, AttributePredicate.Operator.EQUAL));
        DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
        return deviceList.getFirstItem();
    }

    private KapuaPosition getDefaultPosition() {
        KapuaPosition tmpPos = messageFactory.newPosition();
        tmpPos.setAltitude(250.0);
        tmpPos.setHeading(90.0);
        tmpPos.setLatitude(45.5);
        tmpPos.setLongitude(13.6);
        tmpPos.setPrecision(0.3);
        tmpPos.setSatellites(12);
        tmpPos.setSpeed(120.0);
        tmpPos.setStatus(2);
        tmpPos.setTimestamp(new Date());
        return tmpPos;
    }

    private KapuaBirthPayload prepareDefaultBirthPayload() {
        KapuaBirthPayload payload = lifecycleMessageFactory.newKapuaBirthPayload();
        payload.setUptime("500");
        payload.setDisplayName(RELIAGATE_10_20);
        payload.setModelName("ReliaGate");
        payload.setModelId(RELIAGATE_10_20);
        payload.setPartNumber("ABC123456");
        payload.setSerialNumber("12312312312");
        payload.setFirmware("Kura");
        payload.setFirmwareVersion("2.0");
        payload.setBios("BIOStm");
        payload.setBiosVersion(VERSION_NUMBER);
        payload.setOs(LINUX);
        payload.setOsVersion("4.9.18");
        payload.setJvm("J9");
        payload.setJvmVersion("2.4");
        payload.setJvmProfile("J8SE");
        payload.setContainerFramework("OSGi");
        payload.setContainerFrameworkVersion(VERSION_NUMBER);
        payload.setApplicationFramework("Kura");
        payload.setApplicationFrameworkVersion("2.0");
        payload.setConnectionInterface("eth0");
        payload.setConnectionIp("192.168.1.2");
        payload.setAcceptEncoding("gzip");
        payload.setApplicationIdentifiers("CLOUD-V1");
        payload.setAvailableProcessors("1");
        payload.setTotalMemory("1024");
        payload.setOsArch(LINUX);
        payload.setModemImei("123456789ABCDEF");
        payload.setModemImsi("123456789");
        payload.setModemIccid("ABCDEF");
        return payload;
    }

    private KapuaDisconnectPayload prepareDefaultDeathPayload() {
        KapuaDisconnectPayload payload = lifecycleMessageFactory.newKapuaDisconnectPayload();
        payload.setUptime("1000");
        payload.setDisplayName(RELIAGATE_10_20);
        return payload;
    }

    private KapuaMissingPayload prepareDefaultMissingPayload() {
        KapuaMissingPayload tmpPayload = lifecycleMessageFactory.newKapuaMissingPayload();
        return tmpPayload;
    }

    private KapuaAppsPayload prepareDefaultApplicationPayload() {
        KapuaAppsPayload payload = lifecycleMessageFactory.newKapuaAppsPayload();
        payload.setUptime("500");
        payload.setDisplayName(RELIAGATE_10_20);
        payload.setModelName("ReliaGate");
        payload.setModelId(RELIAGATE_10_20);
        payload.setPartNumber("ABC123456");
        payload.setSerialNumber("12312312312");
        payload.setFirmware("Kura");
        payload.setFirmwareVersion("2.0");
        payload.setBios("BIOStm");
        payload.setBiosVersion(VERSION_NUMBER);
        payload.setOs(LINUX);
        payload.setOsVersion("4.9.18");
        payload.setJvm("J9");
        payload.setJvmVersion("2.4");
        payload.setJvmProfile("J8SE");
        payload.setContainerFramework("OSGi");
        payload.setContainerFrameworkVersion(VERSION_NUMBER);
        payload.setApplicationFramework("Kura");
        payload.setApplicationFrameworkVersion("2.0");
        payload.setConnectionInterface("eth0");
        payload.setConnectionIp("192.168.1.2");
        payload.setAcceptEncoding("gzip");
        payload.setApplicationIdentifiers("CLOUD-V1");
        payload.setAvailableProcessors("1");
        payload.setTotalMemory("1024");
        payload.setOsArch(LINUX);
        payload.setModemImei("123456789ABCDEF");
        payload.setModemImsi("123456789");
        payload.setModemIccid("ABCDEF");
        return payload;
    }

    private DeviceCreator prepareDeviceCreatorFromCucDevice(CucDevice dev) {
        Account tmpAccount = (Account) stepData.get(LAST_ACCOUNT);
        DeviceCreator tmpCr;
        KapuaId tmpScope;
        if (dev.getScopeId() != null) {
            tmpScope = dev.getScopeId();
        } else {
            Assert.assertNotNull(tmpAccount);
            Assert.assertNotNull(tmpAccount.getId());
            tmpScope = tmpAccount.getId();
        }
        Assert.assertNotNull(dev.getClientId());
        Assert.assertNotEquals(0, dev.getClientId().length());
        tmpCr = prepareRegularDeviceCreator(tmpScope, dev.getClientId());
        if (dev.getGroupId() != null) {
            tmpCr.setGroupId(dev.getGroupId());
        }
        if (dev.getConnectionId() != null) {
            tmpCr.setConnectionId(dev.getConnectionId());
        }
        if (dev.getDisplayName() != null) {
            tmpCr.setDisplayName(dev.getDisplayName());
        }
        if (dev.getStatus() != null) {
            tmpCr.setStatus(dev.getStatus());
        }
        if (dev.getModelId() != null) {
            tmpCr.setModelId(dev.getModelId());
        }
        if (dev.getSerialNumber() != null) {
            tmpCr.setSerialNumber(dev.getSerialNumber());
        }
        if (dev.getImei() != null) {
            tmpCr.setImei(dev.getImei());
        }
        if (dev.getImsi() != null) {
            tmpCr.setImsi(dev.getImsi());
        }
        if (dev.getIccid() != null) {
            tmpCr.setIccid(dev.getIccid());
        }
        if (dev.getBiosVersion() != null) {
            tmpCr.setBiosVersion(dev.getBiosVersion());
        }
        if (dev.getFirmwareVersion() != null) {
            tmpCr.setFirmwareVersion(dev.getFirmwareVersion());
        }
        if (dev.getOsVersion() != null) {
            tmpCr.setOsVersion(dev.getOsVersion());
        }
        if (dev.getJvmVersion() != null) {
            tmpCr.setJvmVersion(dev.getJvmVersion());
        }
        if (dev.getOsgiFrameworkVersion() != null) {
            tmpCr.setOsgiFrameworkVersion(dev.getOsgiFrameworkVersion());
        }
        if (dev.getApplicationFrameworkVersion() != null) {
            tmpCr.setApplicationFrameworkVersion(dev.getApplicationFrameworkVersion());
        }
        if (dev.getApplicationIdentifiers() != null) {
            tmpCr.setApplicationIdentifiers(dev.getApplicationIdentifiers());
        }
        if (dev.getAcceptEncoding() != null) {
            tmpCr.setAcceptEncoding(dev.getAcceptEncoding());
        }
        return tmpCr;
    }

    DeviceConnectionStatus parseConnectionStatusString(String stat) {
        switch (stat.trim().toUpperCase()) {
            case "CONNECTED":
                return DeviceConnectionStatus.CONNECTED;
            case "DISCONNECTED":
                return DeviceConnectionStatus.DISCONNECTED;
            case "MISSING":
                return DeviceConnectionStatus.MISSING;
        }
        return null;
    }

    ConnectionUserCouplingMode parseConnectionCouplingString(String mode) {
        switch (mode.trim().toUpperCase()) {
            case "INHERITED":
                return ConnectionUserCouplingMode.INHERITED;
            case "LOOSE":
                return ConnectionUserCouplingMode.LOOSE;
            case "STRICT":
                return ConnectionUserCouplingMode.STRICT;
        }
        return null;
    }

    boolean parseBooleanFromString(String value) {
        switch (value.trim().toLowerCase()) {
            case "true":
                return true;
            case "false":
                return false;
        }
        return false;
    }

    @Then("I create a device with name {string}")
    public void iCreateADeviceWithName(String clientId) throws Exception {
        DeviceCreator deviceCreator = deviceFactory.newCreator(getCurrentScopeId());
        deviceCreator.setClientId(clientId);
        stepData.put(DEVICE_CREATOR, deviceCreator);
        try {
            primeException();
            stepData.remove(DEVICE);
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put(DEVICE, device);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Then("I try to edit device to clientId {string}")
    public void iTryToEditDeviceToName(String clientId) throws Exception {
        Device oldDevice = (Device) stepData.get(DEVICE);
        primeException();
        try {
            oldDevice.setClientId(clientId);
            stepData.remove("Device");
            Device newDevice = deviceRegistryService.update(oldDevice);
            stepData.put(DEVICE, newDevice);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Then("I find device with clientId {string}")
    public void iFindDeviceWithClientId(String clientId) throws KapuaException {
        Device device = (Device) stepData.get(DEVICE);
        Device tmpDevice = deviceRegistryService.findByClientId(getCurrentScopeId(), clientId);
        Assert.assertNotNull(tmpDevice);
    }

    @When("I search events from devices in account {string} and {int} (or more )event(s) is/are found")
    public void iSearchForEventsFromDevicesInAccount(String account, int eventsNum) throws Exception {
        ArrayList<Device> devices = (ArrayList<Device>) stepData.get(DEVICE_LIST);
        DeviceEventQuery tmpQuery;
        Device tmpDev;
        DeviceEventListResult tmpList;
        Account tmpAcc;
        try {
            for (Device device : devices) {
                tmpAcc = accountService.findByName(account);
                Assert.assertNotNull(tmpAcc);
                Assert.assertNotNull(tmpAcc.getId());
                tmpDev = deviceRegistryService.findByClientId(tmpAcc.getId(), device.getClientId());
                Assert.assertNotNull(tmpDev);
                Assert.assertNotNull(tmpDev.getId());
                tmpQuery = eventFactory.newQuery(tmpAcc.getId());
                tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceEventAttributes.DEVICE_ID, tmpDev.getId(), AttributePredicate.Operator.EQUAL));
                tmpQuery.setSortCriteria(tmpQuery.fieldSortCriteria(DeviceEventAttributes.RECEIVED_ON, SortOrder.ASCENDING));
                tmpList = eventService.query(tmpQuery);
                Assert.assertNotNull(tmpList);
                stepData.put(DEVICE_EVENT_LIST, tmpList);
                Assert.assertTrue(tmpList.getSize() >= eventsNum);
            }
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @And("I assign tag to device")
    public void iAssignTagToDevice() throws Exception {
        Tag tag = (Tag) stepData.get("tag");
        Device device = (Device) stepData.get("Device");
        try {
            Set<KapuaId> tags = device.getTagIds();
            tags.add(tag.getId());
            device.setTagIds(tags);
            Device newDevice = deviceRegistryService.update(device);
            stepData.put("tags", tags);
            stepData.put("Device", newDevice);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("I assign tag {string} to device {string}")
    public void iAssignTagNamedToDevice(String tagName, String deviceName) throws Exception {
        try {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            Device device = deviceList.getFirstItem();
            TagQuery tagQuery = tagFactory.newQuery(getCurrentScopeId());
            tagQuery.setPredicate(tagQuery.attributePredicate(TagAttributes.NAME, tagName, AttributePredicate.Operator.EQUAL));
            TagListResult tagList = tagService.query(tagQuery);
            Tag tag = tagList.getFirstItem();
            Set<KapuaId> tags = device.getTagIds();
            tags.add(tag.getId());
            device.setTagIds(tags);
            Device newDevice = deviceRegistryService.update(device);
            stepData.put("tags", tags);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("I add device {string} to group {string}")
    public void iAddDeviceToGroup(String deviceName, String groupName) throws Exception {
        try {
            GroupQuery query = groupFactory.newQuery(getCurrentScopeId());
            query.setPredicate(query.attributePredicate(GroupAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            Group foundGroup = groupService.query(query).getFirstItem();
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            Device device = deviceRegistryService.query(tmpQuery).getFirstItem();
            KapuaId groupId = foundGroup.getId();
            device.setGroupId(groupId);
            Device newDevice = deviceRegistryService.update(device);
            stepData.put("Device", newDevice);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @When("I try to edit devices display name to {string}")
    public void iTryToEditDevicesDisplayNameTo(String displayName) throws Exception {
        Device device = (Device) stepData.get("Device");
        try {
            device.setDisplayName(displayName);
            device = deviceRegistryService.update(device);
            stepData.put("Device", device);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Given("Tag {string} is assigned to device {string}")
    public void tagIsAssignedToDevice(String tagName, String deviceName) throws Throwable {
        try {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            Device device = deviceList.getFirstItem();
            TagQuery tagQuery = tagFactory.newQuery(getCurrentScopeId());
            tagQuery.setPredicate(tagQuery.attributePredicate(TagAttributes.NAME, tagName, AttributePredicate.Operator.EQUAL));
            TagListResult tagList = tagService.query(tagQuery);
            Tag tag = tagList.getFirstItem();
            Set<KapuaId> tagIds = device.getTagIds();
            Assert.assertTrue(tagIds.contains(tag.getId()));
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @When("I remove device {string} from all groups")
    public void iChangeDevicesGroupToNoGroup(String deviceName) throws Exception{
        try {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            Device device = deviceRegistryService.query(tmpQuery).getFirstItem();
            device.setGroupId(null);
            device = deviceRegistryService.update(device);
            stepData.put("Device", device);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Given("I unassign tag from device")
    public void iUnassignTagFromDevice() throws Exception {
        Tag tag = (Tag) stepData.get("tag");
        Device device = (Device) stepData.get("Device");
        try {
            Set<KapuaId> tagIds = device.getTagIds();
            tagIds.remove(tag.getId());
            device.setTagIds(tagIds);
            Device newDevice = deviceRegistryService.update(device);
            stepData.put("Device", newDevice);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("I create {int} devices and add them to group {string}")
    public void iCreateDevicesAndAddThemToGroup(int numberOfDevices, String groupName) throws Exception {
        Group group = (Group) stepData.get("Group");
        Assert.assertEquals(group.getName(), groupName);
        try {
            for(int i = 0; i < numberOfDevices; i++) {
                iCreateADeviceWithName(String.format("Device%02d", i));
                iAddDeviceToGroup(String.format("Device%02d", i), group.getName());
            }
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Given("I unassign tag {string} from device {string}")
    public void iUnassignTagNamedFromDevice(String tagName, String deviceName) throws Exception {
        try {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            Device device = deviceList.getFirstItem();
            TagQuery tagQuery = tagFactory.newQuery(getCurrentScopeId());
            tagQuery.setPredicate(tagQuery.attributePredicate(TagAttributes.NAME, tagName, AttributePredicate.Operator.EQUAL));
            TagListResult tagList = tagService.query(tagQuery);
            Tag tag = tagList.getFirstItem();
            Set<KapuaId> tagIds = device.getTagIds();
            tagIds.remove(tag.getId());
            device.setTagIds(tagIds);
            Device newDevice = deviceRegistryService.update(device);
            stepData.put("Device", newDevice);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Then("Device {string} is in Assigned Devices of group {string}")
    public void deviceIsInGroupsAssignedDevices(String deviceName, String groupName) throws Exception {
        try {
            GroupQuery query = groupFactory.newQuery(getCurrentScopeId());
            query.setPredicate(query.attributePredicate(GroupAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            Group foundGroup = groupService.query(query).getFirstItem();
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            Device device = deviceRegistryService.query(tmpQuery).getFirstItem();
            KapuaId expectedGroupId = foundGroup.getId();
            Assert.assertEquals(device.getGroupId(), expectedGroupId);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("The devices display name is {string}")
    public void theDevicesDisplayNameIs(String displayName) throws Throwable {
        Device device = (Device) stepData.get("Device");
        Assert.assertEquals(device.getDisplayName(), displayName);
    }

    @And("I change device status to {string}")
    public void iChangeDeviceStatusTo(String deviceStatus) throws Throwable {
        Device device = (Device) stepData.get("Device");
        try {
            if (deviceStatus.trim().toLowerCase().equals("enabled")) {
                device.setStatus(DeviceStatus.ENABLED);
            } else if (deviceStatus.trim().toLowerCase().equals("disabled")) {
                device.setStatus(DeviceStatus.DISABLED);
            }
            device = deviceRegistryService.update(device);
            stepData.put("Device", device);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Given("Tag {string} is not assigned to device {string}")
    public void tagWithNameIsNotAssignedToDevice(String tagName, String deviceName) throws Throwable {
        try {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            DeviceListResult deviceList = deviceRegistryService.query(tmpQuery);
            Device device = deviceList.getFirstItem();
            TagQuery tagQuery = tagFactory.newQuery(getCurrentScopeId());
            tagQuery.setPredicate(tagQuery.attributePredicate(TagAttributes.NAME, tagName, AttributePredicate.Operator.EQUAL));
            TagListResult tagList = tagService.query(tagQuery);
            Tag tag = tagList.getFirstItem();
            Set<KapuaId> tagIds = device.getTagIds();
            Assert.assertFalse(tagIds.contains(tag.getId()));
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @Then("Device {string} is not in Assigned Devices of group {string}")
    public void deviceIsNotInGroupsAssignedDevices(String deviceName, String groupName) throws Exception {
        try {
            GroupQuery query = groupFactory.newQuery(getCurrentScopeId());
            query.setPredicate(query.attributePredicate(GroupAttributes.NAME, groupName, AttributePredicate.Operator.EQUAL));
            Group foundGroup = groupService.query(query).getFirstItem();
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceName, AttributePredicate.Operator.EQUAL));
            Device device = deviceRegistryService.query(tmpQuery).getFirstItem();
            KapuaId expectedGroupId = foundGroup.getId();
            Assert.assertNotEquals(device.getGroupId(), expectedGroupId);
        } catch (Exception e) {
            verifyException(e);
        }
    }

    @And("I filter devices by")
    public void iFilterDevices(List<CucDevice> parameters) throws Exception {
        CucDevice deviceParams = parameters.get(0);
        DeviceListResult devices;
        stepData.remove("DeviceList");
        if (deviceParams.getClientId() != null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() == null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() == null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() == null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() == null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        } else if (deviceParams.getClientId() != null && deviceParams.getDisplayName() != null && deviceParams.getSerialNumber() != null && deviceParams.getStatus() != null) {
            DeviceQuery tmpQuery = deviceFactory.newQuery(getCurrentScopeId());
            tmpQuery.setPredicate(tmpQuery.andPredicate(tmpQuery.attributePredicate(DeviceAttributes.SERIAL_NUMBER, deviceParams.getSerialNumber(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.DISPLAY_NAME, deviceParams.getDisplayName(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.CLIENT_ID, deviceParams.getClientId(), AttributePredicate.Operator.LIKE),
                                                        tmpQuery.attributePredicate(DeviceAttributes.STATUS, deviceParams.getStatus(), AttributePredicate.Operator.LIKE)));
            devices = deviceRegistryService.query(tmpQuery);
            stepData.put("DeviceList", devices);
        }
    }

    @When("I search for a device with name {string}")
    public void iSearchForADeviceWithName(String clientID) throws Throwable {
        try {
            stepData.remove("Device");
            primeException();
            DeviceQuery query = deviceFactory.newQuery(SYS_SCOPE_ID);
            query.setPredicate(query.attributePredicate(DeviceAttributes.CLIENT_ID, clientID, AttributePredicate.Operator.EQUAL));
            DeviceListResult queryResult = deviceRegistryService.query(query);
            Device foundDevice = queryResult.getFirstItem();
            stepData.put("Device", foundDevice);
            stepData.put("queryResult", queryResult);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }

    @Given("I create a device with null clientID")
    public void iCreateADeviceWithNullClientID() throws Throwable{
        DeviceCreator deviceCreator = deviceFactory.newCreator(getCurrentScopeId());
        deviceCreator.setClientId(null);
        stepData.put("DeviceCreator", deviceCreator);
        try {
            primeException();
            stepData.remove("Device");
            Device device = deviceRegistryService.create(deviceCreator);
            stepData.put("Device", device);
        } catch (Exception ex) {
            verifyException(ex);
        }
    }

    @Given("I try to create devices with invalid symbols {string} in name")
    public void iTryToCreateDevicesWithInvalidSymbolsInName(String invalidCharacters) throws Exception {
        DeviceCreator deviceCreator = deviceFactory.newCreator(getCurrentScopeId());
        for (int i = 0; i < invalidCharacters.length(); i++) {
            String deviceName = "deviceName" + invalidCharacters.charAt(i);
            deviceCreator.setClientId(deviceName);
            primeException();
            try {
                Device device = deviceRegistryService.create(deviceCreator);
                stepData.put("Device", device);
                stepData.put("CurrentDeviceId", device.getId());
            } catch (KapuaException ex) {
                verifyException(ex);
            }
        }
    }

    @When("I update the device clientID from {string} to {string}")
    public void iUpdateTheDeviceClientIDToNewClientId(String oldClientId, String newClientId) throws Exception {
        Device device = (Device) stepData.get("Device");
        Assert.assertEquals(oldClientId, device.getClientId());
        stepData.put("Text", device.getClientId());
        device.setClientId(newClientId);
        primeException();
        try {
            deviceRegistryService.update(device);
        } catch (KapuaException ex) {
            verifyException(ex);
        }
    }
}