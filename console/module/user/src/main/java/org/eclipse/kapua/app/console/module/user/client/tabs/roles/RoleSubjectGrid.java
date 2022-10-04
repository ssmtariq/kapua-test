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
package org.eclipse.kapua.app.console.module.user.client.tabs.roles;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.view.AbstractEntityView;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.client.messages.ConsoleRoleMessages;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtAccessRoleQuery;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class RoleSubjectGrid extends EntityGrid<GwtUser> {

    private GwtRole selectedRole;
    private static final GwtUserServiceAsync USER_SERVICE = GWT.create(GwtUserService.class);
    private static final ConsoleRoleMessages MSGS = GWT.create(ConsoleRoleMessages.class);
    private static final ConsoleMessages C_MSGS = GWT.create(ConsoleMessages.class);
    private static final String GRANTED_USER = "granted user";
    private GwtAccessRoleQuery query;

    RoleSubjectGrid(AbstractEntityView<GwtUser> entityView, GwtSession currentSession) {
        super(entityView, currentSession);
        query = new GwtAccessRoleQuery();
        query.setScopeId(currentSession.getSelectedAccountId());

    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtUser>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtUser>>() {

            @Override
            protected void load(Object loadConfig,
                    AsyncCallback<PagingLoadResult<GwtUser>> callback) {
                if (selectedRole != null) {
                    USER_SERVICE.getUsersForRole((PagingLoadConfig) loadConfig, query, callback);
                } else {
                    callback.onSuccess(new BasePagingLoadResult<GwtUser>(new ArrayList<GwtUser>(), 0, 0));
                }
            }

        };
    }

    @Override
    public String getEmptyGridText() {
        return C_MSGS.gridNoResultAvailable(GRANTED_USER);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return C_MSGS.specificPagingToolbarShowingPost(GRANTED_USER);
            }

            @Override
            public String pagingToolbarNoResult() {
                return C_MSGS.specificPagingToolbarNoResult(GRANTED_USER);
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("id", MSGS.gridRoleSubjectColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", MSGS.gridRoleSubjectColumnHeaderName(), 100);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("displayName", MSGS.gridRoleSubjectColumnHeaderDisplayName(), 100);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtAccessRoleQuery) filterQuery;

    }

    @Override
    protected EntityCRUDToolbar<GwtUser> getToolbar() {
        EntityCRUDToolbar<GwtUser> toolbar = super.getToolbar();
        toolbar.setRefreshButtonVisible(true);
        toolbar.setAddButtonVisible(false);
        toolbar.setEditButtonVisible(false);
        toolbar.setDeleteButtonVisible(false);
        toolbar.setFilterButtonVisible(false);
        toolbar.setBorders(true);

        return toolbar;
    }

    public void setEntity(GwtRole gwtRole) {
        if (gwtRole != null) {
            selectedRole = gwtRole;
            query.setRoleId(selectedRole.getId());
        }
        refresh();
    }

    @Override
    public void refresh() {
        if (super.rendered) {
            super.refresh();
        }
    }

}
