/*******************************************************************************
 * Copyright (c) 2016, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.console.module.account.client.childuser;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

import org.eclipse.kapua.app.console.module.account.client.messages.ConsoleAccountMessages;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.color.Color;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.CreatedByNameCellRenderer;
import org.eclipse.kapua.app.console.module.api.client.ui.grid.EntityGrid;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.EntityCRUDToolbar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolbarMessages;
import org.eclipse.kapua.app.console.module.api.shared.model.query.GwtQuery;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.user.client.messages.ConsoleUserMessages;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUser;
import org.eclipse.kapua.app.console.module.user.shared.model.GwtUserQuery;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserService;
import org.eclipse.kapua.app.console.module.user.shared.service.GwtUserServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class AccountChildUserGrid extends EntityGrid<GwtUser> {

    private static final ConsoleUserMessages MSGS = GWT.create(ConsoleUserMessages.class);
    private static final ConsoleMessages CMSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleAccountMessages ACCOUNT_MSGS = GWT.create(ConsoleAccountMessages.class);
    private static final String CHILD_USER = "child user";

    private static final GwtUserServiceAsync GWT_USER_SERVICE = GWT.create(GwtUserService.class);

    private GwtUserQuery query;
    private AccountChildUserToolbar toolbar;

    public AccountChildUserGrid(GwtSession currentSession) {
        super(null, currentSession);
        query = new GwtUserQuery();
        query.setScopeId(null);
        setBodyBorder(false);
    }

    public void setSelectedAccount(GwtAccount account) {
        query = new GwtUserQuery();
        String accountId = account != null ? account.getId() : null;
        // Set correct scopeId for the grid
        query.setScopeId(accountId);
        // Set selected account for the toolbar
        ((AccountChildUserToolbar) getToolbar()).setSelectedAccountId(accountId);
    }

    @Override
    protected RpcProxy<PagingLoadResult<GwtUser>> getDataProxy() {
        return new RpcProxy<PagingLoadResult<GwtUser>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<PagingLoadResult<GwtUser>> callback) {
                if (query.getScopeId() == null) {
                    callback.onSuccess(new BasePagingLoadResult<GwtUser>(new ArrayList<GwtUser>()));
                } else {
                    GWT_USER_SERVICE.getUsersForAccount((PagingLoadConfig) loadConfig, query,
                            currentSession.getSelectedAccountId(), callback);
                }
            }
        };
    }

    @Override
    protected List<ColumnConfig> getColumns() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();

        ColumnConfig columnConfig = new ColumnConfig("status", MSGS.gridUserColumnHeaderStatus(), 50);
        GridCellRenderer<GwtUser> setStatusIcon = new GridCellRenderer<GwtUser>() {

            @Override
            public String render(GwtUser gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtUser> deviceList, Grid<GwtUser> grid) {

                KapuaIcon icon;
                if (gwtUser.getStatusEnum() != null) {
                    switch (gwtUser.getStatusEnum()) {
                        case DISABLED:
                            icon = new KapuaIcon(IconSet.USER);
                            icon.setColor(Color.RED);
                            break;
                        case ENABLED:
                            icon = new KapuaIcon(IconSet.USER);
                            icon.setColor(Color.GREEN);
                            break;
                        default:
                            icon = new KapuaIcon(IconSet.USER);
                            icon.setColor(Color.GREY);
                            break;
                    }
                } else {
                    icon = new KapuaIcon(IconSet.USER);
                    icon.setColor(Color.GREY);
                }

                return icon.getInlineHTML();
            }
        };
        columnConfig.setRenderer(setStatusIcon);
        columnConfig.setAlignment(HorizontalAlignment.CENTER);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("id", MSGS.gridUserColumnHeaderId(), 100);
        columnConfig.setHidden(true);
        columnConfig.setSortable(false);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("username", MSGS.gridUserColumnHeaderUsername(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("displayName", MSGS.gridUserColumnHeaderDisplayName(), 400);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("phoneNumber", MSGS.gridUserColumnHeaderPhoneNumber(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("email", MSGS.gridUserColumnHeaderEmail(), 200);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("expirationDateFormatted", MSGS.gridUserColumnHeaderExpirationDate(), 400);
        GridCellRenderer<GwtUser> setExpirationDate = new GridCellRenderer<GwtUser>() {

            @Override
            public Object render(GwtUser gwtUser, String property, ColumnData config, int rowIndex, int colIndex, ListStore<GwtUser> store, Grid<GwtUser> grid) {
                if (gwtUser.getExpirationDateFormatted() != null) {
                    return gwtUser.getExpirationDateFormatted();
                } else {
                    return CMSGS.never();
                }
            }
        };
        columnConfig.setRenderer(setExpirationDate);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdByName", MSGS.gridUserColumnHeaderCreatedBy(), 200);
        columnConfig.setRenderer(new CreatedByNameCellRenderer<GwtUser>());
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnConfig("createdOnFormatted", MSGS.gridUserColumnHeaderCreatedOn(), 200);
        columnConfigs.add(columnConfig);

        return columnConfigs;
    }

    @Override
    public GwtQuery getFilterQuery() {
        return query;
    }

    @Override
    public void setFilterQuery(GwtQuery filterQuery) {
        this.query = (GwtUserQuery) filterQuery;
    }

    @Override
    protected void selectionChangedEvent(GwtUser selectedItem) {
        super.selectionChangedEvent(selectedItem);
        ((AccountChildUserToolbar) getToolbar()).updateToolBarButtons();
    }

    @Override
    protected EntityCRUDToolbar<GwtUser> getToolbar() {
        if (toolbar == null) {
            toolbar = new AccountChildUserToolbar(currentSession);
        }
        return toolbar;
    }

    @Override
    protected void onRender(Element target, int index) {
        super.onRender(target, index);
    }

    @Override
    public String getEmptyGridText() {
        return CMSGS.gridNoResultFound(CHILD_USER);
    }

    @Override
    protected KapuaPagingToolbarMessages getKapuaPagingToolbarMessages() {
        return new KapuaPagingToolbarMessages() {

            @Override
            public String pagingToolbarShowingPost() {
                return CMSGS.specificPagingToolbarShowingPost(CHILD_USER);
            }

            @Override
            public String pagingToolbarNoResult() {
                return CMSGS.specificPagingToolbarNoResult(CHILD_USER);
            }
        };
    }
}
