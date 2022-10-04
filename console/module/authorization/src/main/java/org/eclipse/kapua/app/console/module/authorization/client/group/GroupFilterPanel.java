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
package org.eclipse.kapua.app.console.module.authorization.client.group;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.google.gwt.core.client.GWT;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.panel.EntityFilterPanel;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleGroupMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroup;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtGroupQuery;

public class GroupFilterPanel extends EntityFilterPanel<GwtGroup> {

    private static final int WIDTH = 193;
    private static final int MAX_LEN = 255;
    private final EntityGrid<GwtGroup> entityGrid;
    private final GwtSession currentSession;
    private final KapuaTextField<String> nameField;
    private final KapuaTextField<String> descriptionField;
    private static final ConsoleGroupMessages MSGS = GWT.create(ConsoleGroupMessages.class);

    public GroupFilterPanel(AbstractEntityView<GwtGroup> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        entityGrid = entityView.getEntityGrid(entityView, currentSession);
        this.currentSession = currentSession;
        setHeading(MSGS.filterHeader());

        VerticalPanel verticalPanel = getFieldsPanel();
        Label nameLabel = new Label(MSGS.filterFieldGroupNameLabel());
        nameLabel.setWidth(WIDTH);
        nameLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(nameLabel);
        nameField = new KapuaTextField<String>();
        nameField.setName("name");
        nameField.setWidth(WIDTH);
        nameField.setMaxLength(MAX_LEN);
        nameField.setStyleAttribute("margin-top", "0px");
        nameField.setStyleAttribute("margin-left", "5px");
        nameField.setStyleAttribute("margin-right", "5px");
        nameField.setStyleAttribute("margin-bottom", "10px");
        verticalPanel.add(nameField);

        Label descriptionLabel = new Label(MSGS.filterFieldGroupDescriptionLabel());
        descriptionLabel.setWidth(WIDTH);
        descriptionLabel.setStyleAttribute("margin", "5px");
        verticalPanel.add(descriptionLabel);
        descriptionField = new KapuaTextField<String>();
        descriptionField.setName("name");
        descriptionField.setWidth(WIDTH);
        descriptionField.setMaxLength(MAX_LEN);
        descriptionField.setStyleAttribute("margin-top", "0px");
        descriptionField.setStyleAttribute("margin-left", "5px");
        descriptionField.setStyleAttribute("margin-right", "5px");
        descriptionField.setStyleAttribute("margin-bottom", "10px");
        verticalPanel.add(descriptionField);

    }

    @Override
    public void resetFields() {
        nameField.setValue(null);
        descriptionField.setValue(null);
        GwtGroupQuery query = new GwtGroupQuery();
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);

    }

    @Override
    public void doFilter() {
        GwtGroupQuery query = new GwtGroupQuery();
        query.setName(nameField.getValue());
        query.setDescription(descriptionField.getValue());
        query.setScopeId(currentSession.getSelectedAccountId());
        entityGrid.refresh(query);

    }

}
