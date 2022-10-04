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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

public abstract class EntityFilterPanel<M extends GwtEntityModel> extends ContentPanel {

    private final VerticalPanel fieldsPanel;
    private static Button searchButton;
    private static Button resetButton;

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final int WIDTH = 200;

    public EntityFilterPanel(AbstractEntityView<M> entityView, GwtSession currentSession) {
        super();

        setScrollMode(Scroll.AUTOY);
        setBorders(false);
        setLayout(new RowLayout(Orientation.VERTICAL));
        setBodyStyle("background-color:#F0F0F0");

        //
        // Top explanation
        Text infoLabel = new Text(MSGS.filteringPanelInfo());
        infoLabel.setWidth(WIDTH + 5);
        infoLabel.setStyleAttribute("margin", "5px");

        add(infoLabel);

        fieldsPanel = new VerticalPanel();
        add(fieldsPanel);

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setBorders(false);
        buttonPanel.setStyleAttribute("background-color", "#F0F0F0");
        buttonPanel.setStyleAttribute("margin-left", "5px");
        buttonPanel.setStyleAttribute("margin-top", "5px");
        buttonPanel.setHorizontalAlign(HorizontalAlignment.RIGHT);
        buttonPanel.setHeight(50);

        // Search and Reset buttons
        resetButton = new Button(MSGS.filteringPanelReset());
        resetButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                resetFields();
            }

        });

        searchButton = new Button(MSGS.filteringPanelSearch());
        searchButton.setStyleAttribute("margin-right", "5px");
        searchButton.addListener(Events.OnClick, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                doFilter();
            }

        });

        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);
        setIcon(new KapuaIcon(IconSet.FILTER));
        add(buttonPanel);
    }

    public VerticalPanel getFieldsPanel() {
        return fieldsPanel;
    }

    public void setEntityGrid(EntityGrid<?> entityTable) {

    }

    public abstract void resetFields();

    public abstract void doFilter();

    public static Button getSearchButton() {
        return searchButton;
    }

    public static Button getResetButton() {
        return resetButton;
    }
}
