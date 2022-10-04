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
package org.eclipse.kapua.kura.simulator.generator.basic;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import org.eclipse.kapua.kura.simulator.generator.Generator;
import org.eclipse.kapua.kura.simulator.generator.Generators;
import org.eclipse.kapua.kura.simulator.util.Get;

public class SineGeneratorFactory extends AbstractGeneratorFactory {

    public SineGeneratorFactory() {
        super("sine");
    }

    @Override
    protected Optional<Generator> createFrom(final Map<String, Object> configuration) {
        final Duration period = Get.getLong(configuration, "period").map(Duration::ofMillis).orElse(Duration.ofSeconds(60));

        final Double amplitude = Get.getDouble(configuration, "amplitude").orElse(100.0);
        final Double offset = Get.getDouble(configuration, "offset").orElse(0.0);
        final Short shift = Get.getInteger(configuration, "shift").map(i -> i.shortValue()).orElse(null);

        return Optional.of(Generator.onlyMetrics(Generators.fromSingle("value", Generators.sine(period, amplitude, offset, shift))));
    }
}
