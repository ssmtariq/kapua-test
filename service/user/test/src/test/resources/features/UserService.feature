###############################################################################
# Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
@user
@env_none

Feature: User Service
  User Service is responsible for CRUD operations on User objects in Kapua
  database.

  @setup
  @KapuaProperties("locator.class.impl=org.eclipse.kapua.qa.common.MockedLocator")
  Scenario: Initialize test environment
    Given Init Jaxb Context
    And Init Security Context

  Scenario: Creating user
  Create user with all User entity fields set and persist it in database. Then try to
  find it by name and check all the fields.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I search for user with name "kapua-u1"
    Then I find user
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |

  Scenario: Create user with short name
  Create user that has less than required 3 characters in name.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided"
    And I have the following user
      | name | displayName  | email              | phoneNumber     | status  |
      | u1   | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    Then An exception was thrown

  Scenario: Create user that has more than DB allowed length
  Create user with name that contains 280 characters.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided for the argument userCreator.name: Value over than allowed max length. Max length is: 255"
    And I have the following user
      | name                                                                                                                                                                                                                                                                                     | displayName  | email              | phoneNumber     | status  |
      | uuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaauuuuuuuuuuaaaaaaaaaa | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    Then An exception was thrown

  Scenario: Create user with special characters in his name
  Create user with #$% characters in his name

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided"
    And I have the following user
      | name      | displayName  | email              | phoneNumber     | status  |
      | ###$$$%%% | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    Then An exception was thrown

  Scenario: Update user
  First create user with all User entity fields set. Then persist this user in database.
  After that find that same user and modify all the fields by appending modified.
  Persist changes to database. At the end check that changes ware persisted

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    And I search for user with name "kapua-u1"
    And I change user to
      | name     | displayName      | email                  | phoneNumber     | status   |
      | kapua-u1 | Kapua User 1 mod | kapua_u1_mod@kapua.com | +386 31 323 444 | DISABLED |
    And I search for user with name "kapua-u1"
    Then I find user
      | name     | displayName      | email                  | phoneNumber     | status   |
      | kapua-u1 | Kapua User 1 mod | kapua_u1_mod@kapua.com | +386 31 323 444 | DISABLED |

  Scenario: Try to change User name
  First create user with all User entity fields set. Then persist this user in database.
  After that find that same user and modify all the fields by appending modified.
  Persist changes to database. At the end check that changes ware persisted

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    And I expect the exception "KapuaIllegalArgumentException"
    When I search for user with name "kapua-u1"
    And I change user to
      | name         | displayName      | email                  | phoneNumber     | status   |
      | kapua-u1-mod | Kapua User 1 mod | kapua_u1_mod@kapua.com | +386 31 323 444 | DISABLED |
    Then An exception was thrown

  Scenario: Delete user
  Create user with name kapua-user. Then delete this user and check it is
  deleted. This means that if trying to search user, no such user is found.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User with name "kapua-user" in scope with id 42
    When I create user
    And I delete user
    Then I don't find user with name "kapua-user"

  Scenario: Query user
  Create user with name kapua-user, than issue query for user based on scopeId.
  List of matching users should match single user.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User with name "kapua-user" in scope with id 42
    When I create user
    And I query for users in scope with id 42
    Then I count 1 user as query result list

  Scenario: Count user
  Create user with name kapua-user, than issue count based on query that has
  scopeId specified. It is same as Query user, just that it only retrieves count
  of results and not list of users. Count should match just one user.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User with name "kapua-user" in scope with id 42
    When I create user
    And I count users in scope 42
    Then I count 1 user

  Scenario: Find user by id
  Create user with all User entity fields set. When user is created it receives
  user id. Use this user id to search for user. Then check that user found is the same
  user as the one created. Check all fields.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I search for created user by id
    Then I find user
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |

  Scenario: Find user by name
  Create user with all User entity fields set. Use user name to search for user.
  Check that user found is the same user as the one created. Check all fields.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |
    When I search for created user by name
    Then I find user
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 555 | ENABLED |

  Scenario: Create user that already exist
  Create user whit name kapua-user and than try to persist it two times.
  KapuaException should be thrown in such scenario.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given User with name "kapua-user" in scope with id 42
    When I create user
    And I expect the exception "KapuaDuplicateNameException" with the text "An entity with the same name kapua-user already exists."
    When I create same user
    Then An exception was thrown

  Scenario: Update user that doesn't exist
  Create user that is not persisted and than run update statement on that user.
  As user doesn't exist KapuaException should be thrown.

    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type user with id/name"
    And User that doesn't exist
    When I update nonexistent user
    Then An exception was thrown

  Scenario: Delete user that doesn't exist
  Create user that is not persisted and than try to delete that user. As user
  doesn't exist KapuaException should be thrown.

    Given I expect the exception "KapuaEntityNotFoundException" with the text "The entity of type user with id/name"
    And User that doesn't exist
    When I delete nonexistent user
    Then An exception was thrown

  Scenario: Find user with id and scope id that doesn't exist
  Try to find user that doesn't exist in database. As no user is present for current scopeId,
  issuing find by scopeId and unused user id, should return no user.

    When I search for user with id 123 in scope with id 456
    Then I find no user

  Scenario: Find user by name that doesn't exist
  Search for user with name kapua-user. That user doesn't exist in database. As a result no
  user should be returned.

    When I search for user with name "kapua-user"
    Then I find no user

  Scenario: Delete Kapua system user
  Deletion of user with name "kapua-sys" should not be allowed. This is system user. So search
  for "kapua-sys" user and than delete it. KapuaException should be thrown.

    Given I search for user with name "kapua-sys"
    And I expect the exception "KapuaIllegalArgumentException" with the text "An illegal value was provided"
    When I delete user
    Then An exception was thrown

  Scenario: Create multiple users
  Create three ordinary users in same scopeId and then count to see if there are 3 users in
  that scopeId.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following users
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
      | kapua-u3 | Kapua User 3 | kapua_u3@kapua.com | +386 31 323 333 | ENABLED |
    When I count users in scope 42
    Then I count 3 users

  Scenario: Find multiple users
  Create three ordinary users in same scopeId and then find all users and see if there are
  users with same data as those created.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following users
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
      | kapua-u3 | Kapua User 3 | kapua_u3@kapua.com | +386 31 323 333 | ENABLED |
    When I search for users
    Then I find users
      | name     | displayName  | email              | phoneNumber     | status  |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
      | kapua-u2 | Kapua User 2 | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
      | kapua-u3 | Kapua User 3 | kapua_u3@kapua.com | +386 31 323 333 | ENABLED |

  Scenario: Get metadata
  Query for service specific metadata.

    When I retrieve metadata in scope 1
    Then I have metadata

  Scenario: Finding user by expiration date in the future
  Create user with expiration date in the future (e.g "tomorrow") and try to find it.
  No exception should be raised.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  | expirationDate |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED | tomorrow       |
    And I find user with expiration date in the future
      | name     | displayName  | email              | phoneNumber     | status  | expirationDate |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED | tomorrow       |
    And No exception was thrown

  Scenario: Finding user by expiration date in the present
  Create a user with expiration date in the present (e.g. "today") and after that try to find it.
  No exception should be raised.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  | expirationDate |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED | today          |
    Then I search for the user with expiration date in the present
    And I find user with expiration date in the present
      | name     | displayName  | email              | phoneNumber     | status  | expirationDate |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED | today          |
    And No exception was thrown

  Scenario: Finding user by expiration date in the past
  Create a user with expiration date in the past (e.g. "yesterday") and after that try to find it.
  No exception should be raised.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName  | email              | phoneNumber     | status  | expirationDate |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED | yesterday      |
    Then I search for the user with expiration date in the past
    And I find user with expiration date in the past
      | name     | displayName  | email              | phoneNumber     | status  | expirationDate |
      | kapua-u1 | Kapua User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED | yesterday      |
    And No exception was thrown

  Scenario: Find user by its phone number
  Create user with all valid parameters and find them by phone number.
  No any exception should be thrown.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following user
      | name     | displayName    | email              | phoneNumber     | status  |
      | kapua-u1 | Nemanja User 1 | kapua_u1@kapua.com | +386 31 323 111 | ENABLED |
      | kapua-u2 | Nemanja User 2 | kapua_u2@kapua.com | +386 31 323 222 | ENABLED |
      | kapua-u3 | Nemanja User 3 | kapua_u3@kapua.com | +386 31 323 111 | ENABLED |
    And I search users with phone number "+386 31 323 111"
    Then I find users with phone number "+386 31 323 111"
    And No exception was thrown

  Scenario: Find user by its email
  Create a user with all valid parameters and find it by its email.
  No exceptions should be thrown.

    When I configure the user service for the account with the id 42
      | type    | name                       | value |
      | boolean | infiniteChildEntities      | true  |
      | integer | maxNumberChildEntities     | 5     |
      | boolean | lockoutPolicy.enabled      | false |
      | integer | lockoutPolicy.maxFailures  | 3     |
      | integer | lockoutPolicy.resetAfter   | 300   |
      | integer | lockoutPolicy.lockDuration | 3     |
    Given I have the following users
      | name     | displayName    | email              | phoneNumber     | status  |
      | kapua-u1 | Nemanja User 1 | nemanja1@kapua.com | +386 31 323 111 | ENABLED |
      | kapua-u2 | Nemanja User 2 | nemanja2@kapua.com | +386 31 323 222 | ENABLED |
      | kapua-u3 | Nemanja User 3 | nemanja2@kapua.com | +386 31 323 333 | ENABLED |
    And I search users with email "nemanja2@kapua.com"
    Then I find users with email "nemanja2@kapua.com"
    And No exception was thrown

  @teardown
  Scenario: Reset Security Context for all scenarios
    Given Reset Security Context