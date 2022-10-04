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
package org.eclipse.kapua.app.console.module.api.client.ui.panel;

import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.ui.Widget;

public class FormPanel extends com.extjs.gxt.ui.client.widget.form.FormPanel {

    FormData formData = new FormData("-15");

    public FormPanel(int formLabelWidth) {
        super();

        FormLayout formLayout = new FormLayout();
        formLayout.setLabelWidth(formLabelWidth);

        setFrame(false);
        setHeaderVisible(false);
        setBodyBorder(false);
        setBorders(false);
        setLayout(formLayout);
    }

    @Override
    public boolean add(Widget widget) {
        return super.add(widget, formData);
    }
}
