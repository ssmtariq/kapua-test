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
package org.eclipse.kapua.service.datastore.internal;

import org.eclipse.kapua.model.id.KapuaId;

import java.math.BigInteger;
import java.util.Base64;


public class ScopeId implements KapuaId {

    private static final long serialVersionUID = 6893262093856905182L;

    private BigInteger id;

    /**
     * Builds the {@link KapuaId} from the given {@link String} compact scopeId.
     * If the given parameter equals to "_" the current session scope will be used.
     *
     * @param compactScopeId The compact scopeId to parse.
     * @since 1.0.0
     */
    public ScopeId(String compactScopeId) {

        byte[] bytes = Base64.getUrlDecoder().decode(compactScopeId);
        setId(new BigInteger(bytes));
    }

    @Override
    public BigInteger getId() {
        return id;
    }

    protected void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}