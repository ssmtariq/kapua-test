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
package org.eclipse.kapua.app.console.module.api.shared.model;

import java.io.Serializable;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModel;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;

public class KapuaBaseModel extends BaseModel implements Serializable {

    private static final long serialVersionUID = -240340584288457781L;

    public KapuaBaseModel() {
        super();
    }

    @Override
    public <X> X get(String property) {
        return super.get(property);
    }

    @SuppressWarnings({ "unchecked" })
    public <X> X getUnescaped(String property) {
        X value = get(property);

        if (value instanceof String) {
            value = (X) KapuaSafeHtmlUtils.htmlUnescape((String) value);
        }

        return value;
    }

    @Override
    public <X> X set(String name, X value) {
        return set(name, value, true);
    }

    @SuppressWarnings({ "unchecked" })
    public <X> X set(String name, X value, boolean htmlEscape) {
        if (htmlEscape && value != null && value instanceof String) {
            value = (X) KapuaSafeHtmlUtils.htmlEscape(((String) value));
        }

        return super.set(name, value);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        for (String property : properties.keySet()) {
            set(property, properties.get(property));
        }
    }

}
