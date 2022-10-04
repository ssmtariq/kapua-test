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
package org.eclipse.kapua.app.console.module.device.client.connection;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaErrorCode;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityAddEditDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.FormPanel;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleConnectionMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnection.GwtConnectionUserCouplingMode;
import org.eclipse.kapua.app.console.module.device.shared.model.connection.GwtDeviceConnectionOption;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionOptionService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceConnectionOptionServiceAsync;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.permission.UserSessionPermission;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

public class ConnectionEditDialog extends EntityAddEditDialog {

    private static final GwtUserServiceAsync GWT_USER_SERVICE = GWT.create(GwtUserService.class);
    private static final GwtDeviceConnectionOptionServiceAsync GWT_CONNECTION_OPTION_SERVICE = GWT.create(GwtDeviceConnectionOptionService.class);
    private static final ConsoleConnectionMessages MSGS = GWT.create(ConsoleConnectionMessages.class);

    private GwtDeviceConnection selectedDeviceConnection;
    // Security Options fields
    private SimpleComboBox<String> couplingModeCombo;
    private ComboBox<GwtUser> reservedUserCombo;
    private CheckBox allowUserChangeCheckbox;
    private LabelField lastUserField;

    private static final GwtUser NO_USER;

    static {
        NO_USER = new GwtUser();
        NO_USER.setUsername(MSGS.connectionFormReservedUserNoUser());
        NO_USER.setId(null);
    }

    public ConnectionEditDialog(GwtSession currentSession, GwtDeviceConnection selectedDeviceConnection) {
        super(currentSession);
        DialogUtils.resizeDialog(this, 440, 260);
        this.selectedDeviceConnection = selectedDeviceConnection;
    }

    @Override
    public void createBody() {
        submitButton.disable();
        FormPanel groupFormPanel = new FormPanel(FORM_LABEL_WIDTH);
        FormLayout layoutSecurityOptions = new FormLayout();
        layoutSecurityOptions.setLabelWidth(Constants.LABEL_WIDTH_DEVICE_FORM);

        Listener<BaseEvent> comboBoxListener = new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                formPanel.fireEvent(Events.OnClick);
            }
        };

        lastUserField = new LabelField();
        lastUserField.setName("connectionUserLastUserField");
        lastUserField.setLabelSeparator(":");
        lastUserField.setFieldLabel(MSGS.connectionFormLastUser());
        lastUserField.setToolTip(MSGS.connectionFormLastUserTooltip());
        lastUserField.setWidth(225);
        lastUserField.setReadOnly(true);
        groupFormPanel.add(lastUserField);

        // Connection user coupling mode
        couplingModeCombo = new SimpleComboBox<String>();
        couplingModeCombo.setName("connectionUserCouplingModeCombo");
        couplingModeCombo.setEditable(false);
        couplingModeCombo.setTypeAhead(false);
        couplingModeCombo.setAllowBlank(false);
        couplingModeCombo.setFieldLabel(MSGS.connectionFormUserCouplingMode());
        couplingModeCombo.setToolTip(MSGS.connectionFormUserCouplingModeTooltip());
        couplingModeCombo.setTriggerAction(TriggerAction.ALL);

        couplingModeCombo.add(GwtConnectionUserCouplingMode.INHERITED.getLabel());
        couplingModeCombo.add(GwtConnectionUserCouplingMode.LOOSE.getLabel());
        couplingModeCombo.add(GwtConnectionUserCouplingMode.STRICT.getLabel());

        couplingModeCombo.setSimpleValue(GwtConnectionUserCouplingMode.INHERITED.getLabel());
        groupFormPanel.add(couplingModeCombo);

        reservedUserCombo = new ComboBox<GwtUser>();
        reservedUserCombo.setName("connectionUserReservedUserCombo");
        reservedUserCombo.setEditable(false);
        reservedUserCombo.setTypeAhead(false);
        reservedUserCombo.setAllowBlank(false);
        reservedUserCombo.setFieldLabel(MSGS.connectionFormReservedUser());
        reservedUserCombo.setToolTip(MSGS.connectionFormReservedUserTooltip());
        reservedUserCombo.setTriggerAction(TriggerAction.ALL);
        reservedUserCombo.setStore(new ListStore<GwtUser>());
        reservedUserCombo.setDisplayField("username");
        reservedUserCombo.setTemplate("<tpl for=\".\"><div role=\"listitem\" class=\"x-combo-list-item\" title={username}>{username}</div></tpl>");
        reservedUserCombo.setValueField("id");
        reservedUserCombo.addListener(Events.Select, comboBoxListener);

        // Allow credential change
        allowUserChangeCheckbox = new CheckBox();
        allowUserChangeCheckbox.setName("connectionUserAllowUserChangeCheckbox");
        allowUserChangeCheckbox.setFieldLabel(MSGS.connectionFormAllowUserChange());
        allowUserChangeCheckbox.setToolTip(MSGS.connectionFormAllowUserChangeTooltip());
        allowUserChangeCheckbox.setBoxLabel("");

        if (currentSession.hasPermission(UserSessionPermission.read())) {
            // Device User
            GWT_USER_SERVICE.findAll(currentSession.getSelectedAccountId(), new AsyncCallback<ListLoadResult<GwtUser>>() {

                @Override
                public void onFailure(Throwable caught) {
                    exitStatus = false;
                    if (!isPermissionErrorMessage(caught)) {
                        FailureHandler.handle(caught);
                        hide();
                    }
                }

                @Override
                public void onSuccess(ListLoadResult<GwtUser> result) {
                    reservedUserCombo.getStore().removeAll();
                    reservedUserCombo.getStore().add(NO_USER);
                    for (GwtUser gwtUser : result.getData()) {
                        reservedUserCombo.getStore().add(gwtUser);
                    }
                    setReservedUser();
                }

            });

            groupFormPanel.add(reservedUserCombo);
            groupFormPanel.add(allowUserChangeCheckbox);

        } else {
            GwtUser selectedUser = new GwtUser();
            selectedUser.setId(selectedDeviceConnection.getReservedUserId());
            reservedUserCombo.getStore().add(selectedUser);
            reservedUserCombo.setValue(selectedUser);
        }

        bodyPanel.add(groupFormPanel);
        populateEditDialog(selectedDeviceConnection);
    }

    @Override
    public void submit() {
        // convertDeviceAssetChannel the connection to connection option
        GwtDeviceConnectionOption selectedDeviceConnectionOption = new GwtDeviceConnectionOption(selectedDeviceConnection);
        selectedDeviceConnectionOption.setAllowUserChange(allowUserChangeCheckbox.getValue());
        selectedDeviceConnectionOption.setConnectionUserCouplingMode(couplingModeCombo.getValue() != null ? couplingModeCombo.getValue().getValue() : null);
        selectedDeviceConnectionOption.setReservedUserId(reservedUserCombo.getValue() != null ? reservedUserCombo.getValue().getId() : null);

        GWT_CONNECTION_OPTION_SERVICE.update(xsrfToken, selectedDeviceConnectionOption, new AsyncCallback<GwtDeviceConnectionOption>() {

            @Override
            public void onFailure(Throwable cause) {
                exitStatus = false;
                status.hide();
                unmask();
                submitButton.enable();
                cancelButton.enable();
                if (!isPermissionErrorMessage(cause)) {
                    if (cause instanceof GwtKapuaException) {
                        GwtKapuaException gwtCause = (GwtKapuaException) cause;
                        if (gwtCause.getCode().equals(GwtKapuaErrorCode.INTERNAL_ERROR)) {
                            reservedUserCombo.markInvalid(cause.getMessage());
                        }
                        FailureHandler.handle(cause);
                    }
                }
            }

            @Override
            public void onSuccess(GwtDeviceConnectionOption gwtDeviceConnectionOption) {
                exitStatus = true;
                exitMessage = MSGS.dialogEditConfirmation();
                hide();
            }
        });
    }

    @Override
    public String getHeaderMessage() {
        return MSGS.dialogEditHeader(selectedDeviceConnection.getClientId());
    }

    @Override
    public String getInfoMessage() {
        return MSGS.dialogEditInfo();
    }

    private void populateEditDialog(GwtDeviceConnection gwtDeviceConnection) {
        if (currentSession.hasPermission(UserSessionPermission.read()) && gwtDeviceConnection.getUserId() != null) {
            GWT_USER_SERVICE.find(currentSession.getSelectedAccountId(), gwtDeviceConnection.getUserId(), new AsyncCallback<GwtUser>() {

                @Override
                public void onFailure(Throwable caught) {
                    exitStatus = false;
                    if (!isPermissionErrorMessage(caught)) {
                        FailureHandler.handle(caught);
                        hide();
                    }
                }

                @Override
                public void onSuccess(GwtUser gwtUser) {
                    if (gwtUser != null) {
                        lastUserField.setValue(gwtUser.getUsername());
                    } else {
                        lastUserField.setValue("N/A");
                    }
                }
            });
        } else {
            lastUserField.setValue("N/A");
        }
        GwtConnectionUserCouplingMode gwtConnectionUserCouplingMode = null;
        if (gwtDeviceConnection.getConnectionUserCouplingMode() != null) {
            gwtConnectionUserCouplingMode = GwtConnectionUserCouplingMode.getEnumFromLabel(gwtDeviceConnection.getConnectionUserCouplingMode());
        }
        couplingModeCombo.setSimpleValue(gwtConnectionUserCouplingMode != null ? gwtConnectionUserCouplingMode.getLabel() : "N/A");
        allowUserChangeCheckbox.setValue(gwtDeviceConnection.getAllowUserChange());
        formPanel.clearDirtyFields();
    }

    private void setReservedUser() {
        for (GwtUser gwtUser : reservedUserCombo.getStore().getModels()) {
            if (gwtUser.getId() == null) {
                if (selectedDeviceConnection.getReservedUserId() == null) {
                    reservedUserCombo.setValue(gwtUser);
                }
            } else if (gwtUser.getId().equals(selectedDeviceConnection.getReservedUserId())) {
                reservedUserCombo.setValue(gwtUser);
                break;
            } else {
                reservedUserCombo.setValue(NO_USER);
            }
        }
        formPanel.clearDirtyFields();
    }

}
