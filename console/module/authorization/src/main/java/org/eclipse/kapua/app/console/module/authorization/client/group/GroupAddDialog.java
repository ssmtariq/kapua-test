/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.authorization.client.group;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator;
import org.eclipse.kapua.app.console.module.api.client.util.validator.TextFieldValidator.FieldType;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupCreator;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupService;
import org.eclipse.kapua.app.console.module.authorization.shared.service.GwtGroupServiceAsync;

public class GroupAddDialog extends EntityAddEditDialog {

    private static final ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);
    private static final ConsoleMessages CONSOLE_MSGS = GWT.create(ConsoleMessages.class);

    private static final GwtGroupServiceAsync GWT_GROUP_SERVICE = GWT.create(GwtGroupService.class);

    protected KapuaTextField<String> groupNameField;
    protected KapuaTextField<String> groupDescriptionField;

    public GroupAddDialog(GwtSession currentSession) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 400, 200);
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel groupFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        groupNameField = new KapuaTextField<String>();
        groupNameField.setAllowBlank(false);
        groupNameField.setMinLength(3);
        groupNameField.setMaxLength(255);
        groupNameField.setFieldLabel("* " + MSGS.dialogAddFieldName());
        groupNameField.setValidator(new TextFieldValidator(groupNameField, FieldType.NAME_SPACE));
        groupNameField.setToolTip(MSGS.dialogAddFieldNameTooltip());
        groupFormPanel.add(groupNameField);

        groupDescriptionField = new KapuaTextField<String>();
        groupDescriptionField.setAllowBlank(true);
        groupDescriptionField.setMaxLength(255);
        groupDescriptionField.setName("description");
        groupDescriptionField.setFieldLabel(MSGS.dialogAddFieldDescription());
        groupNameField.setToolTip(MSGS.dialogAddFieldDescriptionTooltip());
        groupFormPanel.add(groupDescriptionField);
        bodyPanel.add(groupFormPanel);
    }

    public void validateGroups() {
        if (groupNameField.getValue() == null) {
            ConsoleInfo.display("Error", CONSOLE_MSGS.allFieldsRequired());
        }
    }

    @Override
    protected void preSubmit() {
        validateGroups();
        super.preSubmit();
    }


    @Override
    public void submit() {
        GwtGroupCreator gwtGroupCreator = new GwtGroupCreator();
        gwtGroupCreator.setScopeId(currentSession.getSelectedAccountId());
        gwtGroupCreator.setName(groupNameField.getValue());
        gwtGroupCreator.setDescription(groupDescriptionField.getValue());
        GWT_GROUP_SERVICE.create(gwtGroupCreator, new AsyncCallback<GwtGroup>() {

            @Override
            public void onSuccess(GwtGroup gwtGroup) {
                exitStatus = true;
                exitMessage = MSGS.dialogAddConfirmation();
                hide();
            }

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                status.hide();
                formPanel.getButtonBar().enable();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (!isPermissionErrorMessage(cause)) {
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        if (gwtCause.getCode().equals(GwtKapuaErrorCode.DUPLICATE_NAME)) {
                            groupNameField.markInvalid(gwtCause.getMessage());
                        }
                    }
                    FailureHandler.handleFormException(formPanel, cause);
                }
            }
        });

    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogAddHeader();
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogAddInfo();
    }

}
