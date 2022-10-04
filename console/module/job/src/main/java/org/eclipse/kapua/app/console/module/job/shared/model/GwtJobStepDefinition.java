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
package org.eclipse.kapua.app.console.module.job.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;

import java.util.List;

public class GwtJobStepDefinition extends GwtEntityModel {

    public enum GwtJobStepType {
        TARGET, GENERIC
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("jobStepTypeEnum".equals(property)) {
            return (X) GwtJobStepType.valueOf(getStepType());
        } else {
            return super.get(property);
        }
    }

    public String getJobStepDefinitionName() {
        return get("jobStepDefinitionName");
    }

    public void setJobStepDefinitionName(String jobStepDefinitionName) {
        set("jobStepDefinitionName", jobStepDefinitionName);
    }

    public String getDescription() {
        return get("description");
    }

    public void setDescription(String description) {
        set("description", description);
    }

    public String getStepType() {
        return get("jobStepType");
    }

    public GwtJobStepType getStepTypeEnum() {
        return get("jobStepTypeEnum");
    }

    public void setStepType(String jobStepType) {
        set("jobStepType", jobStepType);
    }

    public String getReaderName() {
        return get("readerName");
    }

    public void setReaderName(String readerName) {
        set("readerName", readerName);
    }

    public String getProcessorName() {
        return get("processorName");
    }

    public void setProcessorName(String processorName) {
        set("processorName", processorName);
    }

    public String getWriterName() {
        return get("writerName");
    }

    public void setWriterName(String writerName) {
        set("writerName", writerName);
    }

    public <P extends GwtJobStepProperty> List<P> getStepProperties() {
        return get("jobStepProperties");
    }

    public void setStepProperties(List<GwtJobStepProperty> jobStepProperties) {
        set("jobStepProperties", jobStepProperties);
    }
}
