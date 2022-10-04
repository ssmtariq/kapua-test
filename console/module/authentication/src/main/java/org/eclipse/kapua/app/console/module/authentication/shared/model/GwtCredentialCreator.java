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
package org.eclipse.kapua.app.console.module.authentication.shared.model;

import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityCreator;

import java.io.Serializable;
import java.util.Date;

public class GwtCredentialCreator extends GwtEntityCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;
    private GwtCredentialType credentialType;
    private String credentialPlainKey;
    private GwtCredentialStatus credentialStatus;
    private Date expirationDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GwtCredentialType getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(GwtCredentialType credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialPlainKey() {
        return credentialPlainKey;
    }

    public void setCredentialPlainKey(String credentialPlainKey) {
        this.credentialPlainKey = credentialPlainKey;
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public GwtCredentialStatus getCredentialStatus() {
        return credentialStatus;
    }

    public void setCredentialStatus(GwtCredentialStatus credentialStatus) {
        this.credentialStatus= credentialStatus;
    }

    public void setCredentialStatus(String credentialStatus) {
        setCredentialStatus(GwtCredentialStatus.valueOf(credentialStatus));
    }
}
