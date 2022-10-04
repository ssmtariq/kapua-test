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

import java.util.Date;

import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtUpdatableEntityModel;

public class GwtCredential extends GwtUpdatableEntityModel {

    private static final long serialVersionUID = -469650746033310482L;

    @Override
    @SuppressWarnings({ "unchecked" })
    public <X> X get(String property) {
        if ("credentialTypeEnum".equals(property)) {
            return (X) GwtCredentialType.valueOf(getCredentialType());
        } else if ("statusEnum".equals(property)) {
            return (X) GwtCredentialStatus.valueOf(getStatus());
        } else if ("subjectTypeEnum".equals(property)) {
            return (X) GwtSubjectType.valueOf(getSubjectType());
        } else if ("credentialStatusEnum".equals(property)) {
            return (X) GwtCredentialStatus.valueOf(getCredentialStatus());
        } else if ("expirationDateFormatted".equals(property)) {
            if (getExpirationDate() != null) {
                return (X) ((DateUtils.formatDateTime(getExpirationDate())));
            } else {
                return (X) null;
            }
        } else if ("firstLoginFailureFormatted".equals(property)) {
            if (getFirstLoginFailure() != null) {
                return (X) (getFirstLoginFailure().toString());
            } else {
                return (X) "N/A";
            }
        } else if ("loginFailuresResetFormatted".equals(property)) {
            if (getLoginFailuresReset() != null) {
                return (X) (getLoginFailuresReset().toString());
            } else {
                return (X) "N/A";
            }
        } else if ("lockoutResetFormatted".equals(property)) {
            if (getLockoutReset() != null) {
                return (X) (getLockoutReset().toString());
            } else {
                return (X) "N/A";
            }
        } else {
            return super.get(property);
        }
    }

    public String getStatus() {
        return (String) get("status");
    }

    public GwtCredential() {
        super();
    }

    public String getUserId() {
        return get("userId");
    }

    public void setUserId(String userId) {
        set("userId", userId);
    }

    public String getCredentialType() {
        return get("credentialType");
    }

    public GwtCredentialType getCredentialTypeEnum() {
        return get("credentialTypeEnum");
    }

    public void setCredentialType(String credentialType) {
        set("credentialType", credentialType);
    }

    public String getSubjectType() {
        return get("subjectType");
    }

    public GwtSubjectType getSubjectTypeEnum() {
        return get("subjectTypeEnum");
    }

    public void setSubjectType(String subjectType) {
        set("subjectType", subjectType);
    }

    public String getCredentialKey() {
        return get("credentialKey");
    }

    public void setCredentialKey(String credentialKey) {
        set("credentialKey", credentialKey, false);
    }

    public String getUsername() {
        return get("username");
    }

    public void setUsername(String username) {
        set("username", username);
    }

    public String getCredentialStatus() {
        return (String) get("credentialStatus");
    }

    public GwtCredentialStatus getCredentialStatusEnum() {
        return (GwtCredentialStatus) get("credentialStatusEnum");
    }

    public void setCredentialStatus(String credentialStatus) {
        set("credentialStatus", credentialStatus);
    }

    public Date getExpirationDate() {
        return get("expirationDate");
    }

    public String getExpirationDateFormatted() {
        return get("expirationDateFormatted");
    }

    public void setExpirationDate(Date expirationDate) {
        set("expirationDate", expirationDate);
    }

    public String getFirstLoginFailureFormatted() {
        return get("firstLoginFailureFormatted");
    }

    public Integer getLoginFailures() {
        return get("loginFailures");
    }

    public void setLoginFailures(Integer loginFailures) {
        set("loginFailures", loginFailures);
    }

    public Date getFirstLoginFailure() {
        return get("firstLoginFailure");
    }

    public void setFirstLoginFailure(Date firstLoginFailure) {
        set("firstLoginFailure", firstLoginFailure);
    }

    public String getLoginFailuresResetFormatted() {
        return get("loginFailuresResetFormatted");
    }

    public Date getLoginFailuresReset() {
        return get("loginFailuresReset");
    }

    public void setLoginFailuresReset(Date loginFailuresReset) {
        set("loginFailuresReset", loginFailuresReset);
    }

    public String getLockoutResetFormatted() {
        return get("lockoutResetFormatted");
    }

    public Date getLockoutReset() {
        return get("lockoutReset");
    }

    public void setLockoutReset(Date lockoutReset) {
        set("lockoutReset", lockoutReset);
    }

    public GwtMfaCredentialOptions getMfaOptions() {
        return get("mfaOptions");
    }

    public void setMfaOptions(GwtMfaCredentialOptions mfaOptions) {
        set("mfaOptions", mfaOptions);
    }

}
