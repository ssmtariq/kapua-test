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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common.cucumber;

import java.util.Map;

/**
 * Data object used in Gherkin to transfer configuration data.
 */
public class CucConfig {

    /** The scope ID of the configuration. */
    private Integer scopeId;

    /** The scope ID of the parent scope. */
    private Integer parentId;

    /** Name of type that config value has. */
    private String type;

    /** Name of config parameter. */
    private String name;

    /** String representation of parameter value. */
    private String value;

    public CucConfig(String scopeId, String parentId, String type, String name, String value) {
        this.scopeId = scopeId != null ? Integer.valueOf(scopeId) : null;
        this.parentId = parentId != null ? Integer.valueOf(parentId) : null;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public Integer getScopeId() {
        return scopeId;
    }

    public void setScopeId(Integer scopeId) {
        this.scopeId = scopeId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Adds this config parameter to specified map.
     *
     * @param valueMap
     *            map to add parameter to
     */
    public void addConfigToMap(Map<String, Object> valueMap) {

        switch (type) {
        case "integer":
            valueMap.put(name, Integer.valueOf(value));
            break;
        case "string":
            valueMap.put(name, value);
            break;
        case "boolean":
            valueMap.put(name, Boolean.valueOf(value));
            break;
        }
    }
}
