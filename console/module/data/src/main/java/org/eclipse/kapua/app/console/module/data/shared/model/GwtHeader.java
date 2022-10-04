/*******************************************************************************
 * Copyright (c) 2017, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.data.shared.model;

import java.io.Serializable;

import org.eclipse.kapua.app.console.module.data.client.util.HeaderTypeUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.KapuaBaseModel;

public class GwtHeader extends KapuaBaseModel implements Serializable {
    private static final long serialVersionUID = -6683882186670183772L;

    public enum GwtHeaderType {
        FLOAT,
        STRING,
        INT,
        DOUBLE,
        LONG,
        BOOLEAN,
        BYTE_ARRAY;
    }

    public GwtHeader() {
    }

    public GwtHeader(String name) {
        set("name", name);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X get(String property) {
        if ("typeFormatted".equals(property)) {
            return (X) HeaderTypeUtils.format(this);
        } else {
            return super.get(property);
        }
    }

    public String getName() {
        return (String) get("name");
    }

    public String getUnescapedName() {
        return getUnescaped("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getType() {
        return (String) get("type");
    }

    public void setType(String type) {
        set("type", type);
    }

    public String getTypeFormatted() {
        return (String) get("typeFormatted");
    }
}
