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
package org.eclipse.kapua.app.console.module.data.client;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.KeyNav;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.module.api.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.module.api.client.ui.button.KapuaButton;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaPagingToolBar;
import org.eclipse.kapua.app.console.module.api.client.ui.widget.KapuaTextField;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceTable extends LayoutContainer {

    private static final int DEVICE_PAGE_SIZE = 50;
    private static final ConsoleDataMessages DATA_MSGS = GWT.create(ConsoleDataMessages.class);

    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private BasePagingLoader<PagingLoadResult<GwtDatastoreDevice>> loader;
    private GwtSession currentSession;
    private Grid<GwtDatastoreDevice> deviceGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtDatastoreDevice>> listeners = new ArrayList<SelectionChangedListener<GwtDatastoreDevice>>();
    private KapuaPagingToolBar pagingToolBar;
    private KapuaTextField<String> filterField;
    private KapuaButton refreshButton;

    public DeviceTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public GwtDatastoreDevice getSelectedDevice() {
        if (deviceGrid != null) {
            return deviceGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        initDeviceTable();
        add(tableContainer);

    }

    private void initDeviceTable() {
        initDeviceGrid();

        refreshButton = new KapuaButton(DATA_MSGS.searchButton(), new KapuaIcon(IconSet.FILTER), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                refresh();
            }
        });

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(true);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(DATA_MSGS.deviceInfoTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(deviceGrid);

        filterField = new KapuaTextField<String>();
        filterField.setMaxLength(255);
        filterField.setEmptyText(DATA_MSGS.deviceInfoTableFilter());
        new KeyNav<ComponentEvent>(filterField) {
            public void onKeyPress(ComponentEvent ce) {
                if (ce.getKeyCode() == KeyCodes.KEY_ENTER ) {
                    refresh();
                }
            }
        };

        ToolBar tb = new ToolBar();
        tb.add(filterField);
        tb.add(refreshButton);
        tableContainer.setTopComponent(tb);

        pagingToolBar = new KapuaPagingToolBar(DEVICE_PAGE_SIZE);
        pagingToolBar.bind(loader);
        pagingToolBar.enable();

        tableContainer.setBottomComponent(pagingToolBar);
    }

    private void initDeviceGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("friendlyDevice", DATA_MSGS.deviceInfoTableTopicHeader(), 150);
        configs.add(column);

        column = new ColumnConfig("timestamp", DATA_MSGS.deviceInfoTableLastPostedHeader(), 150);
        column.setRenderer(new DeviceTimestampCellRenderer(currentSession));
        configs.add(column);

        RpcProxy<PagingLoadResult<GwtDatastoreDevice>> proxy = new RpcProxy<PagingLoadResult<GwtDatastoreDevice>>() {

            @Override
            protected void load(Object loadConfig, final AsyncCallback<PagingLoadResult<GwtDatastoreDevice>> callback) {
                updateTimestamps((PagingLoadConfig) loadConfig, callback);
            }
        };

        loader = new BasePagingLoader<PagingLoadResult<GwtDatastoreDevice>>(proxy);
        loader.addLoadListener(new DeviceTableLoadListener());
        loader.setRemoteSort(true);
        loader.setSortDir(SortDir.ASC);
        loader.setSortField("friendlyDevice");
        //
        SwappableListStore<GwtDatastoreDevice> store = new SwappableListStore<GwtDatastoreDevice>(loader);
        deviceGrid = new Grid<GwtDatastoreDevice>(store, new ColumnModel(configs));
        deviceGrid.setBorders(false);
        deviceGrid.setStateful(false);
        deviceGrid.setLoadMask(true);
        deviceGrid.setStripeRows(true);
        deviceGrid.getView().setAutoFill(true);
        deviceGrid.getView().setForceFit(true);
        deviceGrid.getView().setEmptyText(DATA_MSGS.deviceTableEmptyText());
        deviceGrid.disableTextSelection(false);
        deviceGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (SelectionChangedListener<GwtDatastoreDevice> listener : listeners) {
            deviceGrid.getSelectionModel().addSelectionChangedListener(listener);
        }
        deviceGrid.addListener(Events.Render, new Listener<ComponentEvent>() {

            @Override
            public void handleEvent(ComponentEvent be) {
                loader.load();
            }
        });

        final GridSelectionModel<GwtDatastoreDevice> gridSelectionModel = deviceGrid.getSelectionModel();
        GridSelectionChangedListener<GwtDatastoreDevice> gridSelectionChangedListener = new GridSelectionChangedListener<GwtDatastoreDevice>();
        gridSelectionChangedListener.setSelectionModel(gridSelectionModel);
        gridSelectionModel.addListener(Events.SelectionChange, gridSelectionChangedListener);
    }

    public void addSelectionChangedListener(SelectionChangedListener<GwtDatastoreDevice> listener) {
        listeners.add(listener);
    }

    @Override
    public void onUnload() {
        super.onUnload();
    }

    public void refresh() {
        deviceGrid.getStore().getLoader().load();
    }

    private void updateTimestamps(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<GwtDatastoreDevice>> callback) {
        dataService.findDevices(loadConfig, currentSession.getSelectedAccountId(), filterField.getValue(), new AsyncCallback<PagingLoadResult<GwtDatastoreDevice>>() {

            @Override
            public void onFailure(Throwable caught) {
                FailureHandler.handle(caught);
                deviceGrid.unmask();
            }

            @Override
            public void onSuccess(PagingLoadResult<GwtDatastoreDevice> result) {
                callback.onSuccess(result);
                dataService.updateDeviceTimestamps(currentSession.getSelectedAccountId(), result.getData(), new AsyncCallback<List<GwtDatastoreDevice>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        FailureHandler.handle(caught);
                        deviceGrid.unmask();
                    }

                    @Override
                    public void onSuccess(List<GwtDatastoreDevice> result) {
                        for (GwtDatastoreDevice device : result) {
                            deviceGrid.getStore().findModel(device).setTimestamp(device.getTimestamp() != null ? device.getTimestamp() : new Date(GwtDatastoreDevice.NO_TIMESTAMP));
                        }
                        deviceGrid.getView().refresh(false);
                    }
                });
            }
        });
    }

    private class DeviceTableLoadListener extends LoadListener {
        @Override
        public void loaderBeforeLoad(LoadEvent le) {
            if (refreshButton != null) {
                refreshButton.disable();
            }
        }

        @Override
        public void loaderLoad(LoadEvent le) {
            if (refreshButton != null) {
                refreshButton.enable();
            }
        }

        @Override
        public void loaderLoadException(LoadEvent le) {
            if (refreshButton != null) {
                refreshButton.enable();
            }
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
        }
    }
}
