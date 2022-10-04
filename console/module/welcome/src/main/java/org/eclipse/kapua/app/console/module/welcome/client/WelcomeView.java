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
package org.eclipse.kapua.app.console.module.welcome.client;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Image;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractView;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.welcome.client.messages.ConsoleWelcomeMessages;

public class WelcomeView extends AbstractView {

    private static final ConsoleWelcomeMessages MSGS = GWT.create(ConsoleWelcomeMessages.class);

    public WelcomeView(GwtSession currentSession) {
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setBorders(true);
        setLayout(new FitLayout());

        // Kapua logo
        Image kapuaLogo = new Image("img/logo-color.svg");
        kapuaLogo.setHeight("200px");

        // Kapua welcome
        Text welcomeMessage = new Text();
        welcomeMessage.setText(MSGS.welcomeMessage());

        TableLayout tableLayout = new TableLayout(1);
        tableLayout.setWidth("100%");
        tableLayout.setHeight("100%");
        tableLayout.setCellPadding(40);
        tableLayout.setCellHorizontalAlign(HorizontalAlignment.CENTER);
        tableLayout.setCellVerticalAlign(VerticalAlignment.MIDDLE);

        ContentPanel centerPanel = new ContentPanel(tableLayout);
        centerPanel.setBodyBorder(false);
        centerPanel.setBorders(false);
        centerPanel.setHeaderVisible(false);
        centerPanel.setScrollMode(Scroll.AUTO);

        centerPanel.add(kapuaLogo, new TableData());
        centerPanel.add(welcomeMessage, new TableData(HorizontalAlignment.CENTER, VerticalAlignment.TOP));

        add(centerPanel);
    }

    public static String getName() {
        return MSGS.title();
    }
}
