/*******************************************************************************
 * Copyright (c) 2017, 2022 Red Hat Inc and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.client.gateway.kura;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.kapua.client.gateway.Topic;
import org.eclipse.kapua.client.gateway.mqtt.MqttNamespace;

public class KuraNamespace implements MqttNamespace {

    public static final class Builder {

        private String accountName;

        public Builder accountName(final String accountName) {
            Objects.requireNonNull(accountName);
            Topic.ensureNotSpecial(accountName);

            this.accountName = accountName;
            return this;
        }

        public String accountName() {
            return accountName;
        }

        public KuraNamespace build() {
            if (accountName == null || accountName.isEmpty()) {
                throw new IllegalArgumentException("'accountName' must be set");
            }

            return new KuraNamespace(accountName);
        }
    }

    private final String accountName;

    private KuraNamespace(final String accountName) {
        this.accountName = accountName;
    }

    @Override
    public String dataTopic(final String clientId, final String applicationId, final Topic topic) {
        Topic.ensureNotSpecial(clientId);
        Topic.ensureNotSpecial(applicationId);

        return Stream.concat(
                Stream.of(
                        accountName,
                        clientId,
                        applicationId),
                topic.stream())
                .collect(Collectors.joining("/"));
    }

}
