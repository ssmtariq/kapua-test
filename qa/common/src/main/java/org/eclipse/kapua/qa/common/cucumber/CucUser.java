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

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import org.eclipse.kapua.service.user.UserStatus;
import org.eclipse.kapua.service.user.UserType;

/**
 * Data object used in Gherkin to transfer User data.
 */
public class CucUser {

    private String name;

    private String displayName;

    private String email;

    private String phoneNumber;

    private UserStatus status;

    private UserType userType;

    private BigInteger scopeId;

    private String password;

    private String expirationDate;

    public CucUser(String name, String displayName, String email, String phoneNumber, UserStatus status, UserType userType, BigInteger scopeId, String password, String expirationDate) {
        this.name = name;
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.userType = userType;
        this.scopeId = scopeId;
        this.password = password;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public BigInteger getScopeId() {
        return scopeId;
    }

    public void setScopeId(BigInteger scopeId) {
        this.scopeId = scopeId;
    }

    public Date getExpirationDate() {
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date expDate = null;
        Instant now = Instant.now();

        if (expirationDate == null) {
            return null;
        }
        // Special keywords for date
        switch (expirationDate) {
            case "yesterday":
                expDate = Date.from(now.minus(Duration.ofDays(1)));
                break;
            case "today":
                expDate = Date.from(now);
                break;
            case "tomorrow":
                expDate = Date.from(now.plus(Duration.ofDays(1)));
                break;
        }
        // Just parse date
        try {
            expDate = df.parse(expirationDate);
        } catch (ParseException | NullPointerException e) {
            // skip, leave date null
        }

        return expDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
