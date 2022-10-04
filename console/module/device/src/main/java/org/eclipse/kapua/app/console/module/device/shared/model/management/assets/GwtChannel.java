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
package org.eclipse.kapua.app.console.module.device.shared.model.management.assets;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.io.Serializable;

public class GwtChannel extends GwtUpdatableEntityModel implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6939874517542354331L;

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getRead() {
        return get("read");
    }

    public void setRead(String read) {
        set("read", read);
    }

    public String getAction() {
        return get("action");
    }

    public void setAction(String action) {
        set("action", action);
    }

    public String getValue() {
        return get("value");
    }

    public void setValue(String value) {
        set("value", value);
    }

}
