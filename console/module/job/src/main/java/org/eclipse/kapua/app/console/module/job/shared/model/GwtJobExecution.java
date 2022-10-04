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

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

import java.util.Date;

public class GwtJobExecution extends GwtUpdatableEntityModel {

    @Override
    public <X> X get(String property) {
        if ("startedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getStartedOn()));
        } else if ("endedOnFormatted".equals(property)) {
            return (X) (DateUtils.formatDateTime(getEndedOn()));
        } else {
            return super.get(property);
        }
    }

    public String getJobId() {
        return get("jobId");
    }

    public void setJobId(String jobId) {
        set("jobId", jobId);
    }

    public Date getStartedOn() {
        return get("startedOn");
    }

    public String getStartedOnFormatted() {
        return get("StartedOnFormatted");
    }

    public void setStartedOn(Date startedOn) {
        set("startedOn", startedOn);
    }

    public Date getEndedOn() {
        return get("endedOn");
    }

    public String getEndedOnFormatted() {
        return get("EndedOnFormatted");
    }

    public void setEndedOn(Date endedOn) {
        set("endedOn", endedOn);
    }

    public String getLog() {
        return get("log");
    }

    public String getUnescapedLog() {
        return getUnescaped("log");
    }

    public void setLog(String log) {
        set("log", log);
    }
}
