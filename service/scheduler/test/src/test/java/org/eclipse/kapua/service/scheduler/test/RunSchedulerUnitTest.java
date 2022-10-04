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
package org.eclipse.kapua.service.scheduler.test;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "classpath:features/SchedulerService.feature"
                   },
        glue = {"org.eclipse.kapua.service.scheduler.test",
                "org.eclipse.kapua.service.scheduler.steps",
                "org.eclipse.kapua.qa.common",
                "org.eclipse.kapua.service.job.steps"
               },
        plugin = { "pretty",
                   "html:target/cucumber",
                   "json:target/cucumber.json" },
        monochrome = true)
public class RunSchedulerUnitTest {
}
