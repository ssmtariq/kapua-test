/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.datastore.model.query.predicate;

import org.eclipse.kapua.service.storable.model.query.predicate.ExistsPredicate;

/**
 * Query predicate for matching messages with existing metrics
 *
 * @since 1.2.0
 */
public interface MetricExistsPredicate extends ExistsPredicate {

    /**
     * Gets the metric type to search.
     * This is required because metric with the same name can have different types.
     *
     * @return The metric type
     * @since 1.2.0
     */
    Class<?> getType();

    /**
     * Sets the metric type so search.
     *
     * @param type The metric type to search.
     * @since 1.2.0
     */
    void setType(Class<?> type);

}
