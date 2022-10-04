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
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadConfig;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.util.SwappableListStore;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.data.client.messages.ConsoleDataMessages;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreAsset;
import org.eclipse.kapua.app.console.module.data.shared.model.GwtDatastoreDevice;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataService;
import org.eclipse.kapua.app.console.module.data.shared.service.GwtDataServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class AssetTable extends LayoutContainer {

    private static final ConsoleDataMessages MSGS = GWT.create(ConsoleDataMessages.class);

    private static GwtDataServiceAsync dataService = GWT.create(GwtDataService.class);

    private BaseListLoader<ListLoadResult<GwtDatastoreAsset>> loader;
    private GwtSession currentSession;
    private Grid<GwtDatastoreAsset> assetGrid;
    private ContentPanel tableContainer;
    private List<SelectionChangedListener<GwtDatastoreAsset>> listeners = new ArrayList<SelectionChangedListener<GwtDatastoreAsset>>();

    private GwtDatastoreDevice selectedDevice;
    private GwtDatastoreAsset selectedAsset;

    public AssetTable(GwtSession currentSession) {
        this.currentSession = currentSession;
    }

    public GwtDatastoreAsset getSelectedAsset() {
        if (assetGrid != null) {
            return assetGrid.getSelectionModel().getSelectedItem();
        }
        return null;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        setLayout(new FitLayout());
        setBorders(false);

        initAssetTable();
        add(tableContainer);

    }

    private void initAssetTable() {
        initAssetGrid();

        tableContainer = new ContentPanel();
        tableContainer.setBorders(false);
        tableContainer.setBodyBorder(true);
        tableContainer.setHeaderVisible(true);
        tableContainer.setHeading(MSGS.assetTableHeader());
        tableContainer.setScrollMode(Scroll.AUTOY);
        tableContainer.setLayout(new FitLayout());
        tableContainer.add(assetGrid);
    }

    private void initAssetGrid() {
        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        ColumnConfig column = new ColumnConfig("asset", MSGS.assetTableAssetHeader(), 100);
        configs.add(column);
        RpcProxy<ListLoadResult<GwtDatastoreAsset>> proxy = new RpcProxy<ListLoadResult<GwtDatastoreAsset>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<ListLoadResult<GwtDatastoreAsset>> callback) {
                if (selectedDevice != null) {
                    dataService.findAssets((LoadConfig) loadConfig, currentSession.getSelectedAccountId(), selectedDevice, callback);
                }
            }
        };

        loader = new BaseListLoader<ListLoadResult<GwtDatastoreAsset>>(proxy);
        loader.load();
        SwappableListStore<GwtDatastoreAsset> store = new SwappableListStore<GwtDatastoreAsset>(loader);
        assetGrid = new Grid<GwtDatastoreAsset>(store, new ColumnModel(configs));
        assetGrid.setBorders(false);
        assetGrid.setStateful(false);
        assetGrid.setLoadMask(true);
        assetGrid.setStripeRows(true);
        assetGrid.getView().setAutoFill(true);
        assetGrid.getView().setForceFit(true);
        assetGrid.getView().setEmptyText(MSGS.assetTableEmptyText());
        assetGrid.disableTextSelection(false);
        assetGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (SelectionChangedListener<GwtDatastoreAsset> listener : listeners) {
            assetGrid.getSelectionModel().addSelectionChangedListener(listener);
        }

        final GridSelectionModel<GwtDatastoreAsset> gridSelectionModel = assetGrid.getSelectionModel();
        GridSelectionChangedListener<GwtDatastoreAsset> gridSelectionChangedListener = new GridSelectionChangedListener<GwtDatastoreAsset>();
        gridSelectionChangedListener.setSelectionModel(gridSelectionModel);
        gridSelectionModel.addListener(Events.SelectionChange, gridSelectionChangedListener);
    }

    public void clearTable() {
        assetGrid.getStore().removeAll();
        this.selectedDevice = null;
    }

    private void refresh() {
        assetGrid.getStore().getLoader().load();
    }

    public void refresh(GwtDatastoreDevice selectedDevice) {
        this.selectedDevice = selectedDevice;
        refresh();
    }

    public void refresh(GwtDatastoreAsset selectedAsset) {
        this.selectedAsset = selectedAsset;
        refresh();
    }

    public void addSelectionChangedListener(SelectionChangedListener<GwtDatastoreAsset> listener) {
        listeners.add(listener);
    }

    @Override
    public void onUnload() {
        super.onUnload();
    }
}
