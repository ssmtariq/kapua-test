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
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.service.device.registry.steps;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.broker.BrokerDomains;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.domain.Domain;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.account.Account;
import org.eclipse.kapua.service.account.AccountCreator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountService;
import org.eclipse.kapua.service.authentication.credential.CredentialCreator;
import org.eclipse.kapua.service.authentication.credential.CredentialFactory;
import org.eclipse.kapua.service.authentication.credential.CredentialService;
import org.eclipse.kapua.service.authentication.credential.CredentialStatus;
import org.eclipse.kapua.service.authentication.credential.CredentialType;
import org.eclipse.kapua.service.authorization.access.AccessInfoCreator;
import org.eclipse.kapua.service.authorization.access.AccessInfoFactory;
import org.eclipse.kapua.service.authorization.access.AccessInfoService;
import org.eclipse.kapua.service.authorization.permission.Permission;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.datastore.DatastoreDomains;
import org.eclipse.kapua.service.device.management.DeviceManagementDomains;
import org.eclipse.kapua.service.device.registry.DeviceDomains;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Creator of Accounts, Users, Permissions that are used in ACL tests
 */
public class AclCreator {

    private static final Logger logger = LoggerFactory.getLogger(MqttDevice.class);

    private static final KapuaId SYS_ID = new KapuaEid(BigInteger.ONE);

    private static final KapuaId ROOT_SCOPE_ID = new KapuaEid(BigInteger.ONE);

    /**
     * Credential service.
     */
    private CredentialService credentialService;
    private CredentialFactory credentialFactory;
    private PermissionFactory permissionFactory;

    /**
     * User service.
     */
    private UserService userService;
    private UserFactory userFactory;

    /**
     * Accessinfo service.
     */
    private AccessInfoService accessInfoService;
    private AccessInfoFactory accessInfoFactory;

    /**
     * Account service.
     */
    private AccountService accountService;

    /**
     * Account factory.
     */
    private AccountFactory accountFactory;


    /**
     * Constructor with all support services.
     */
    public AclCreator() {
        KapuaLocator locator = KapuaLocator.getInstance();

        accountService = locator.getService(AccountService.class);
        accountFactory = locator.getFactory(AccountFactory.class);

        userService = locator.getService(UserService.class);
        userFactory = locator.getFactory(UserFactory.class);

        accessInfoService = locator.getService(AccessInfoService.class);
        accessInfoFactory = locator.getFactory(AccessInfoFactory.class);

        credentialService = locator.getService(CredentialService.class);
        credentialFactory = locator.getFactory(CredentialFactory.class);
        permissionFactory = locator.getFactory(PermissionFactory.class);
    }

    /**
     * Configure user service with reasonable default values.
     *
     * @param accId   account id
     * @param scopeId scope id
     */
    private void configureUserService(KapuaId accId, KapuaId scopeId) {

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("infiniteChildEntities", true);
        valueMap.put("maxNumberChildEntities", 5);
        valueMap.put("lockoutPolicy.enabled", false);
        valueMap.put("lockoutPolicy.maxFailures", 3);
        valueMap.put("lockoutPolicy.resetAfter", 300);
        valueMap.put("lockoutPolicy.lockDuration", 3);

        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            logger.warn("Error", ex);
        }
    }

    /**
     * Configure account service with reasonable default values.
     *
     * @param accId   account id
     * @param scopeId scope id
     */
    private void configureAccountService(KapuaId accId, KapuaId scopeId) {

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("infiniteChildEntities", true);
        valueMap.put("maxNumberChildEntities", 5);

        try {
            userService.setConfigValues(accId, scopeId, valueMap);
        } catch (KapuaException ex) {
            logger.warn("Error", ex);
        }
    }

    /**
     * Creates permissions for user with specified account. Permissions are created in privileged mode.
     *
     * @param permissionList list of permissions for user, if targetScopeId is not set user scope that is
     *                       specified as account
     * @param user           user for whom permissions are set
     * @param account        account in which user is defined
     * @throws Exception
     */
    private void createPermissions(List<AclPermission> permissionList, User user, Account account)
            throws Exception {

        KapuaSecurityUtils.doPrivileged(() -> {
            try {
                accessInfoService.create(accessInfoCreatorCreator(permissionList, user, account));
            } catch (KapuaException ke) {
                logger.warn("Error", ke);
            }

            return null;
        });
    }

    /**
     * Create accessInfoCreator instance with data about user permissions.
     * If target scope is not defined in permission list use account scope.
     *
     * @param permissionList list of all permissions
     * @param user           user for which permissions are set
     * @param account        that user belongs to
     * @return AccessInfoCreator instance for creating user permissions
     */
    private AccessInfoCreator accessInfoCreatorCreator(List<AclPermission> permissionList,
                                                       User user, Account account) {

        AccessInfoCreator accessInfoCreator = accessInfoFactory.newCreator(account.getId());
        accessInfoCreator.setUserId(user.getId());
        accessInfoCreator.setScopeId(user.getScopeId());
        Set<Permission> permissions = new HashSet<>();
        for (AclPermission permissionData : permissionList) {
            Actions action = permissionData.getAction();
            KapuaEid targetScopeId = permissionData.getTargetScopeId();
            if (targetScopeId == null) {
                targetScopeId = (KapuaEid) account.getId();
            }
            Domain domain = permissionData.getDomain();
            Permission permission = permissionFactory.newPermission(domain, action, targetScopeId);
            permissions.add(permission);
        }
        accessInfoCreator.setPermissions(permissions);

        return accessInfoCreator;
    }

    public void attachUserCredentials(Account account, User user) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            CredentialCreator credentialCreator;
            credentialCreator = credentialFactory.newCreator(account.getId(), user.getId(), CredentialType.PASSWORD, "KeepCalm123.", CredentialStatus.ENABLED, null);
            try {
                credentialService.create(credentialCreator);
            } catch (KapuaException ke) {
                // skip
            }

            return null;
        });
    }

    public void attachUserCredentials(Account account, User user, String password) throws KapuaException {
        KapuaSecurityUtils.doPrivileged(() -> {
            CredentialCreator credentialCreator;
            credentialCreator = credentialFactory.newCreator(account.getId(), user.getId(), CredentialType.PASSWORD, password, CredentialStatus.ENABLED, null);
            try {
                credentialService.create(credentialCreator);
            } catch (KapuaException ke) {
                // skip
            }

            return null;
        });
    }

    public User createUser(Account account, String name) throws KapuaException {
        configureUserService(account.getId(), SYS_ID);
        UserCreator userCreator = userFactory.newCreator(account.getId(), name);
        return userService.create(userCreator);
    }

    Account createAccount(String name, String orgName, String orgEmail) throws KapuaException {
        configureAccountService(ROOT_SCOPE_ID, SYS_ID);

        AccountCreator accountCreator = accountFactory.newCreator(ROOT_SCOPE_ID);
        accountCreator.setName(name);
        accountCreator.setOrganizationName(orgName);
        accountCreator.setOrganizationEmail(orgEmail);

        return accountService.create(accountCreator);
    }

    void attachBrokerPermissions(Account account, User user) throws Exception {
        List<AclPermission> permissionList = new ArrayList<>();
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.delete, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    void attachDevicePermissions(Account account, User user) throws Exception {
        List<AclPermission> permissionList = new ArrayList<>();
        permissionList.add(new AclPermission(DeviceManagementDomains.DEVICE_MANAGEMENT_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    void attachDataViewPermissions(Account account, User user) throws Exception {
        List<AclPermission> permissionList = new ArrayList<>();
        permissionList.add(new AclPermission(DatastoreDomains.DATASTORE_DOMAIN, Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    void attachDataManagePermissions(Account account, User user) throws Exception {
        List<AclPermission> permissionList = new ArrayList<>();
        permissionList.add(new AclPermission(DatastoreDomains.DATASTORE_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));
        createPermissions(permissionList, user, account);
    }

    public void attachFullPermissions(Account account, User user) throws Exception {
        List<AclPermission> permissionList = new ArrayList<>();
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(BrokerDomains.BROKER_DOMAIN, Actions.delete, (KapuaEid) user.getScopeId()));

        permissionList.add(new AclPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.delete, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(DeviceDomains.DEVICE_CONNECTION_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));

        permissionList.add(new AclPermission(DeviceDomains.DEVICE_DOMAIN, Actions.read, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(DeviceDomains.DEVICE_DOMAIN, Actions.write, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(DeviceDomains.DEVICE_DOMAIN, Actions.delete, (KapuaEid) user.getScopeId()));
        permissionList.add(new AclPermission(DeviceDomains.DEVICE_DOMAIN, Actions.connect, (KapuaEid) user.getScopeId()));

        createPermissions(permissionList, user, account);
    }
}
