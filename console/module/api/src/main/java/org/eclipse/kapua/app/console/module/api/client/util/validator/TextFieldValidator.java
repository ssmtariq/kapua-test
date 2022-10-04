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
package org.eclipse.kapua.app.console.module.api.client.util.validator;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.google.gwt.core.client.GWT;

import org.eclipse.kapua.app.console.module.api.client.messages.ValidationMessages;

import java.util.MissingResourceException;

public class TextFieldValidator implements Validator {

    private static final ValidationMessages MSGS = GWT.create(ValidationMessages.class);

    protected FieldType textFieldType;
    protected TextField<String> textField;

    public TextFieldValidator(TextField<String> textField, FieldType textFieldType) {
        this.textField = textField;
        this.textFieldType = textFieldType;

        // initialize the field for its validation
        if (this.textFieldType.getRegex() != null) {
            this.textField.setRegex(this.textFieldType.getRegex());
        }
        if (this.textFieldType.getToolTipMessage() != null) {
            this.textField.setToolTip(this.textFieldType.getToolTipMessage());
        }
        if (this.textFieldType.getRequiredMessage() != null) {
            this.textField.getMessages().setBlankText(this.textFieldType.getRequiredMessage());
        }
        if (this.textFieldType.getRegexMessage() != null) {
            this.textField.getMessages().setRegexText(this.textFieldType.getRegexMessage());
        }
    }

    @Override
    public String validate(Field<?> field, String value) {
        String result = null;
        if (!value.matches(textFieldType.getRegex())) {
            result = textField.getMessages().getRegexText();
        }
        return result;
    }

    public enum FieldType {
        SIMPLE_NAME("simple_name", "^[a-zA-Z0-9\\-]{3,}$"),
        DEVICE_CLIENT_ID("device_client_id", "^((?!#|\\+|\\*|&|,|\\?|>|\\/|\\:\\:).)*$"),
        SNAPSHOT_FILE("snapshot_file", "^([a-zA-Z0-9\\:\\_\\-\\\\]){1,255}(\\.xml)"),
        NAME("name", "^[a-zA-Z0-9\\_\\-]{3,}$"),
        NAME_SPACE("name_space", "^[a-zA-Z0-9\\ \\_\\-]{3,}$"),
        NAME_SPACE_COLON("name_space_colon", "^[a-zA-Z0-9\\ \\_\\-\\:]{3,}$"),
        PASSWORD("password", "^.*(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!\\\"\\#$%&'()*+,\\-./:;<=>?@\\[\\]\\\\^_`{|}~]).*$"),
        EMAIL("email", "^(\\w+)([-+.][\\w]+)*@(\\w[-\\w]*\\.){1,5}([A-Za-z]){2,63}$"),
        PHONE("phone", "^\\+? ?[0-9_]+( [0-9_]+)*$"),
        ALPHABET("alphabet", "^[a-zA-Z_]+$"),
        ALPHANUMERIC("alphanumeric", "^[a-zA-Z0-9_]+$"),
        NUMERIC("numeric", "^[+0-9.]+$"),
        PACKAGE_VERSION("package_version", "^[a-zA-Z0-9.\\-\\_]*$"),
        URL("url", "(http(s)?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");

        private String name;
        private String regex;
        private String regexMsg;
        private String toolTipMsg;
        private String requiredMsg;

        FieldType(String name, String regex) {

            this.name = name;
            this.regex = regex;
            regexMsg = name + "RegexMsg";
            toolTipMsg = name + "ToolTipMsg";
            requiredMsg = name + "RequiredMsg";
        }

        public String getName() {
            return name;
        }

        public String getRegex() {
            return regex;
        }

        public String getRegexMessage() {
            try {
                return MSGS.getString(regexMsg);
            } catch (MissingResourceException mre) {
                return null;
            }
        }

        public String getToolTipMessage() {
            try {
                return MSGS.getString(toolTipMsg);
            } catch (MissingResourceException mre) {
                return null;
            }
        }

        public String getRequiredMessage() {
            try {
                return MSGS.getString(requiredMsg);
            } catch (MissingResourceException mre) {
                return null;
            }
        }

    }

}
