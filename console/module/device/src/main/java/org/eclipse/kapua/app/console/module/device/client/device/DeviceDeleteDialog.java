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
package org.eclipse.kapua.app.console.module.device.client.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.entity.EntityDeleteDialog;
import org.eclipse.kapua.app.console.module.api.client.util.DialogUtils;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceServiceAsync;

public class DeviceDeleteDialog extends EntityDeleteDialog {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private static final GwtDeviceServiceAsync GWT_DEVICE_SERVICE = GWT.create(GwtDeviceService.class);

    private GwtDevice gwtDevice;

    public DeviceDeleteDialog(GwtDevice gwtUser) {
        this.gwtDevice = gwtUser;

        DialogUtils.resizeDialog(this, 300, 135);
        setDisabledFormPanelEvents(true);
    }

    @Override
    public String getHeaderMessage() {
        return DEVICE_MSGS.dialogDeleteHeader(gwtDevice.getClientId());
    }

    @Override
    public String getInfoMessage() {
        return DEVICE_MSGS.dialogDeleteInfo();
    }

    @Override
    public void submit() {
        final GwtDevice toDeleteDevice = gwtDevice;

        GWT_DEVICE_SERVICE.deleteDevice(xsrfToken,
                toDeleteDevice.getScopeId(),
                toDeleteDevice.getClientId(),
                new AsyncCallback<Void>() {

                    public void onFailure(Throwable caught) {
                        exitStatus = false;
                        if (!isPermissionErrorMessage(caught)) {
                            exitMessage = DEVICE_MSGS.dialogDeviceDeleteError(caught.getLocalizedMessage());
                        }
                        hide();
                    }

                    public void onSuccess(Void arg0) {
                        // refresh(filterPredicates);
                        exitStatus = true;
                        // TODO
                         exitMessage = DEVICE_MSGS.dialogDeleteConfirmation();
                        hide();
                    }
                }
        );
    }

}
