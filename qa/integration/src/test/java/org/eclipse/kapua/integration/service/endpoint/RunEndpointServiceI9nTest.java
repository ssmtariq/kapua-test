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
package org.eclipse.kapua.integration.service.endpoint;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features/endpoint/EndpointServiceI9n.feature" },
        glue = {
                "org.eclipse.kapua.service.user.steps",
                "org.eclipse.kapua.qa.integration.steps",
                "org.eclipse.kapua.service.endpoint.steps",
                "org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.account.steps",
                "org.eclipse.kapua.service.authorization.steps"
        },
        plugin = { "pretty",
                "html:target/cucumber/EndpointServiceI9n",
                "json:target/EndpointServiceI9n_cucumber.json" },
        monochrome = true)
public class RunEndpointServiceI9nTest {
}
