###############################################################################
# Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#     Eurotech - initial API and implementation
#
###############################################################################
@datastore
@datastoreNewIndex
@env_docker

Feature: Datastore tests

@setup
  Scenario: Start full docker environment
    Given Init Jaxb Context
    And Init Security Context
    And Start full docker environment

  Scenario: Simple positive scenario for creating default - weekly index
  Create elasticsearch index with default setting for index creation which is weekly
  index creation. Index gets created when user publishes data. Index is based on
  capture date, which is set to specific value, this value determines index name.

    Given Server with host "127.0.0.1" on port "9200"
    When I delete all indices
    And I login as user with name "kapua-sys" and password "kapua-password"
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    Then REST response containing text "1-data-message-2018-01"
    And I delete all indices

  Scenario: Simple positive scenario for creating daily index
  Create elasticsearch index with setting for daily index creation. Index gets created when
  user publishes data. Index is based on capture date, which is set to specific value, this
  value determines index name.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP"
    And System property "datastore.index.window" with value "day"
    When I delete all indices
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    And REST response containing text "1-data-message-2018-01-02"
    And I delete all indices

  Scenario: Simple positive scenario for creating hourly index
  Create elasticsearch index with setting for hour index creation. Index gets created when
  user publishes data. Index is based on capture date, which is set to specific value, this
  value determines index name.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP"
    And System property "datastore.index.window" with value "hour"
    When I delete all indices
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    And REST response containing text "1-data-message-2018-01-02-10"
    And I delete all indices

  Scenario: Creating two indexes with weekly index
  Creation of two indexes by publishing data with different capture date. As creation date if responsible
  for index name, this should result in two different indexes.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP"
    And System property "datastore.index.window" with value "week"
    When I delete all indices
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I prepare a random message with capture date "2018-01-07T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    And REST response containing text "1-data-message-2018-01"
    And REST response containing text "1-data-message-2018-02"
    And I delete all indices

  Scenario: Creating two indexes with daily index
  Creation of two indexes by publishing data with different capture date. As creation date if responsible
  for index name, this should result in two different indexes.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP"
    And System property "datastore.index.window" with value "day"
    When I delete all indices
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I prepare a random message with capture date "2018-01-02T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    And REST response containing text "1-data-message-2018-01-02"
    And REST response containing text "1-data-message-2018-01-03"
    And I delete all indices

  Scenario: Creating two indexes with hourly index
  Creation of two indexes by publishing data with different capture date. As creation date if responsible
  for index name, this should result in two different indexes.

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP"
    And System property "datastore.index.window" with value "hour"
    When I delete all indices
    And I select account "kapua-sys"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I prepare a random message with capture date "2018-01-01T15:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    And REST response containing text "1-data-message-2018-01-02-10"
    And REST response containing text "1-data-message-2018-01-02-15"
    And I delete all indices

  Scenario: Creating index with regular user
  Creating single

    Given Server with host "127.0.0.1" on port "9200"
    And I login as user with name "kapua-sys" and password "kapua-password"
    Given Dataservice config enabled "true", dataTTL 30, rxByteLimit 0, dataIndexBy "DEVICE_TIMESTAMP"
    And System property "datastore.index.window" with value "week"
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
    And I configure the device registry service
      | type    | name                   | value |
      | boolean | infiniteChildEntities  | true  |
      | integer | maxNumberChildEntities | 10    |
    And User A
      | name    | displayName  | email             | phoneNumber     | status  | userType |
      | kapua-a | Kapua User A | kapua_a@kapua.com | +386 31 323 444 | ENABLED | INTERNAL |
    And I add credentials
      | name    | password          | enabled |
      | kapua-a | ToManySecrets123# | true    |
    And Full permissions
    And I delete all indices
    And I logout
    When I login as user with name "kapua-a" and password "ToManySecrets123#"
    And I select account "account-a"
    Given The device "test-device-1"
    When I prepare a random message with capture date "2018-01-01T10:21:32.123Z" and save it as "RandomDataMessage"
    And I store the message "RandomDataMessage" and remember its ID as "RandomDataMessageId"
    And I refresh all indices
    When REST GET call at "/_cat/indices/"
    And REST response containing "-2018-01" with prefix account "LastAccount"
    And I delete all indices

@teardown
  Scenario: Stop full docker environment
    Given Stop full docker environment
