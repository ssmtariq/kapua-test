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
@broker
@deviceData
@env_docker

Feature: Device data scenarios

@setup
  Scenario: Start docker environment
    Given Init Jaxb Context
    And Init Security Context
    And Start full docker environment

  Scenario: Connect to the system and publish some data

    Given The account name is "kapua-sys" and the client ID is "sim-1"
    And The broker URI is "tcp://kapua-broker:kapua-password@localhost:1883"
    And My credentials are username "kapua-sys" and password "kapua-password"
    And I have a mock data application named "my-app-1"

    When I start the simulator

    Then Device "sim-1" for account "kapua-sys" is registered after 15 seconds
    And I expect the device to report the applications
      | DEPLOY-V2 |
      | CMD-V1    |
      | my-app-1  |

    Given I publish for the application "my-app-1"
    And I delete the messages for this device

    When I publish on the topic "my-topic-1/data" timestamped now
      | key        | type   | value |
      | foo.string | STRING | bar   |
    And  I publish on the topic "my-topic-2/data" timestamped now
      | key       | type  | value |
      | foo.int32 | INT32 | 123   |
    And  I publish on the topic "my-topic-3/data" timestamped now
      | key       | type  | value |
      | foo.int64 | INT64 | 456   |
    And  I publish on the topic "my-topic-4/data" timestamped now
      | key        | type   | value |
      | foo.double | DOUBLE | 42.42 |
    And  I publish on the topic "my-topic-5/data" timestamped now
      | key         | type    | value |
      | foo.boolean | BOOLEAN | true  |

    And  I wait 10 seconds
    And  I refresh all indices

    Then I expect the number of messages for this device to be 5
    And  I expect the latest captured message on channel "my-app-1/my-topic-5/data" to have the metrics
      | key         | type    | value |
      | foo.boolean | BOOLEAN | true  |
    Then Device "sim-1" for account "kapua-sys" is registered after 15 seconds
    When I stop the simulator
    Then Device "sim-1" for account "kapua-sys" is not registered after 5 seconds
    And I delete the messages for this device

@teardown
  Scenario: Stop docker environment
    Given Stop full docker environment