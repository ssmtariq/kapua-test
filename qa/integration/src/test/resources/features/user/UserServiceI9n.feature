###############################################################################
# Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
###############################################################################
@userIntegrationBase
@userService
@env_docker_base

Feature: User Service Integration
  User Service integration scenarios

  @setup
  Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context
    And Start base docker environment

  Scenario: Deleting user in account that is lower in hierarchy
  Using user A in in different scope than user B, try to delete user B. Scope of user A is one
  level higher than scope of B. Scope of A is parent of scope B. This allows user A to delete
  user B.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And A generic user
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-g | Kapua User G | kapua_g@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And Account
      | name      |
      | account-b |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User B
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User B | kapua_b@kapua.com | +386 31 323 555 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          |
      | kapua-b | ToManySecrets123# |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    When I try to delete user "kapua-g"
    Then No exception was thrown
    Given I expect the exception "SubjectUnauthorizedException" with the text "Required permission: user:read:"
    When I try to delete user "kapua-b"
    Then An exception was thrown
    And I logout

  Scenario: Deleting user in account that is higher in hierarchy
  Using user B in in different scope than user A, try to delete user A. Scope of user B is one
  level lower than scope of A. Scope of A is parent of scope B. Subordinate scope should not be
  allowed to delete user in parent scope, unless it has permissions in that scope.
    When I login as user with name "kapua-sys" and password "kapua-password"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 50    |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given Account
      | name      | scopeId |
      | account-a | 1       |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    Given Account
      | name      |
      | account-b |
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And User B
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-b | Kapua User B | kapua_b@kapua.com | +386 31 323 555 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-b | ToManySecrets123# | true    |
    And Add permissions to the last created user
      | domain | action |
      | user   | read   |
      | user   | write  |
      | user   | delete |
    And I logout
    When I login as user with name "kapua-b" and password "ToManySecrets123#"
    Then No exception was thrown
    Given I expect the exception "SubjectUnauthorizedException" with the text "Required permission: user:read:"
    When I try to delete user "kapua-a"
    Then An exception was thrown
    And I logout

  Scenario: Create same user in different accounts
  Creating user with name "TestUser" as user kapua-sys, creating child account,
  trying to create user with same name in new account. An exception should be thrown.

    Given I login as user with name "kapua-sys" and password "kapua-password"
    And Scope with ID 1
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And I configure user service
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 50    |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    And I create user with name "TestUser"
    And I create an account with name "SubAccount", organization name "TestOrganization" and email address "test@gmail.com"
    And The account with name "kapua-sys" has 1 subaccount
    And I find account with name "SubAccount"
    And I configure account service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 50    |
    And I configure user service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 5     |
    And I expect the exception "KapuaDuplicateNameInAnotherAccountError" with the text "An entity with the same name"
    When I create user with name "TestUser" in account "SubAccount"
    Then An exception was thrown
    And I logout

  @teardown
  Scenario: Stop test environment
    Given Stop full docker environment
    And Reset Security Context
