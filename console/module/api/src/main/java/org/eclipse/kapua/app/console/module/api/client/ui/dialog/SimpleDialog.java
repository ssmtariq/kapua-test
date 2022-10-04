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
package org.eclipse.kapua.app.console.module.api.client.ui.dialog;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.google.gwt.user.client.Element;

public abstract class SimpleDialog extends ActionDialog {

    protected ContentPanel bodyPanel;

    public SimpleDialog() {
        super();
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);

        //
        // Body setup
        bodyPanel = new ContentPanel();
        bodyPanel.setBorders(false);
        bodyPanel.setHeight(1000);
        bodyPanel.setBodyBorder(false);
        bodyPanel.setHeaderVisible(false);
        bodyPanel.setStyleAttribute("background-color", "#F0F0F0");
        bodyPanel.setBodyStyle("background-color: #F0F0F0");

        createBody();

        formPanel.add(bodyPanel);
    }

    @Override
    protected void preSubmit() {
        if (formPanel.isValid()) {
            super.preSubmit();
        }
    }

    public abstract void createBody();
}
