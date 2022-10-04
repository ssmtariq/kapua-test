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
package org.eclipse.kapua.app.console.module.device.client.device.assets;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelStringProvider;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanelSelectionModel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.ui.button.DiscardButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.RefreshButton;
import org.eclipse.kapua.app.console.module.api.client.ui.button.SaveButton;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.InfoDialog.InfoDialogType;
import org.eclipse.kapua.app.console.module.api.client.ui.dialog.KapuaMessageBox;
import org.eclipse.kapua.app.console.module.api.client.ui.label.Label;
import org.eclipse.kapua.app.console.module.api.client.util.ConsoleInfo;
import org.eclipse.kapua.app.console.module.api.client.util.CssLiterals;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaLoadListener;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenService;
import org.eclipse.kapua.app.console.module.api.shared.service.GwtSecurityTokenServiceAsync;
import org.eclipse.kapua.app.console.module.device.client.messages.ConsoleDeviceMessages;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.assets.GwtDeviceAsset;
import org.eclipse.kapua.app.console.module.device.shared.model.management.assets.GwtDeviceAssets;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceAssetService;
import org.eclipse.kapua.app.console.module.device.shared.service.GwtDeviceAssetServiceAsync;

import java.util.ArrayList;
import java.util.List;

public class DeviceAssetsValues extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);
    private static final ConsoleDeviceMessages DEVICE_MSGS = GWT.create(ConsoleDeviceMessages.class);

    private final GwtDeviceAssetServiceAsync gwtDeviceAssetService = GWT.create(GwtDeviceAssetService.class);
    private final GwtSecurityTokenServiceAsync gwtXSRFService = GWT.create(GwtSecurityTokenService.class);

    private GwtSession currentSession;

    private boolean dirty;
    private boolean initialized;
    private GwtDevice selectedDevice;
    private DeviceTabAssets tabAssets;

    private ToolBar toolBar;

    private Button refreshButton;
    private boolean refreshProcess;

    private Button apply;
    private Button reset;

    private ContentPanel assetValuesContainer;
    private DeviceAssetsPanel assetValuesPanel;
    private BorderLayoutData centerData;

    private BaseTreeLoader loader;
    private TreeStore<ModelData> treeStore;
    private TreePanel<ModelData> tree;

    protected boolean resetProcess;

    protected boolean applyProcess;

    public DeviceAssetsValues(GwtSession currentSession,
                              DeviceTabAssets tabAssets) {
        this.currentSession = currentSession;
        this.tabAssets = tabAssets;
        dirty = false;
        initialized = false;
    }

    public void setDevice(GwtDevice selectedDevice) {
        dirty = true;
        this.selectedDevice = selectedDevice;
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        setLayout(new FitLayout());
        setBorders(false);

        // init components
        initToolBar();
        initAssetPanel();

        ContentPanel devicesAssetPanel = new ContentPanel();
        devicesAssetPanel.setBorders(false);
        devicesAssetPanel.setBodyBorder(false);
        devicesAssetPanel.setHeaderVisible(false);
        devicesAssetPanel.setLayout(new FitLayout());
        devicesAssetPanel.setScrollMode(Scroll.AUTO);
        devicesAssetPanel.setTopComponent(toolBar);
        devicesAssetPanel.add(assetValuesContainer);

        add(devicesAssetPanel);
        toolBar.setStyleAttribute("border-left", CssLiterals.border1PxSolidRgb(208, 208, 208));
        toolBar.setStyleAttribute("border-right", CssLiterals.border1PxSolidRgb(208, 208, 208));
        toolBar.setStyleAttribute("border-top", CssLiterals.border1PxSolidRgb(208, 208, 208));
        toolBar.setStyleAttribute("border-bottom", "0px none");
        initialized = true;
        layout(true);
    }

    private void initToolBar() {
        toolBar = new ToolBar();
        toolBar.setBorders(true);

        //
        // Refresh Button
        refreshButton = new RefreshButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!refreshProcess) {
                    refreshButton.setEnabled(false);
                    refreshProcess = true;

                    dirty = true;
                    refresh();
                    refreshProcess = false;
                }
            }
        });

        apply = new SaveButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!applyProcess) {
                    applyProcess = true;
                    apply.setEnabled(false);

                    apply();

                    apply.setEnabled(true);
                    applyProcess = false;
                }
            }
        });

        reset = new DiscardButton(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                if (!resetProcess) {
                    resetProcess = true;
                    reset.setEnabled(false);

                    reset();

                    reset.setEnabled(true);
                    resetProcess = false;
                }
            }
        });
        apply.setEnabled(false);
        reset.setEnabled(false);
        refreshButton.setEnabled(false);
        toolBar.add(refreshButton);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(apply);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(reset);
    }

    private void initAssetPanel() {
        assetValuesContainer = new ContentPanel();
        assetValuesContainer.setBorders(false);
        assetValuesContainer.setBodyBorder(false);
        assetValuesContainer.setHeaderVisible(false);
        assetValuesContainer.setStyleAttribute("background-color", "white");
        assetValuesContainer.setScrollMode(Scroll.AUTO);

        BorderLayout borderLayout = new BorderLayout();
        assetValuesContainer.setLayout(borderLayout);

        // center
        centerData = new BorderLayoutData(LayoutRegion.CENTER);
        centerData.setMargins(new Margins(0));

        // west
        BorderLayoutData westData = new BorderLayoutData(LayoutRegion.WEST, 200);
        westData.setSplit(true);
        westData.setCollapsible(true);
        westData.setMargins(new Margins(0, 5, 0, 0));

        // loader and store
        RpcProxy<List<GwtDeviceAsset>> proxy = new RpcProxy<List<GwtDeviceAsset>>() {

            @Override
            protected void load(Object loadConfig, AsyncCallback<List<GwtDeviceAsset>> callback) {
                if (selectedDevice != null && dirty && initialized) {
                    if (selectedDevice.isOnline()) {
                        tree.mask(MSGS.loading());
                        gwtDeviceAssetService.get(
                                (PagingLoadConfig) loadConfig,
                                currentSession.getSelectedAccountId(),
                                selectedDevice.getId(),
                                new GwtDeviceAssets(),
                                callback);
                    } else {
                        List<GwtDeviceAsset> assets = new ArrayList<GwtDeviceAsset>();
                        GwtDeviceAsset asset = new GwtDeviceAsset();
                        asset.setName(DEVICE_MSGS.assetNoAssets());
                        assets.add(asset);
                        callback.onSuccess(assets);
                    }
                } else {
                    List<GwtDeviceAsset> assets = new ArrayList<GwtDeviceAsset>();
                    GwtDeviceAsset asset = new GwtDeviceAsset();
                    asset.setName(DEVICE_MSGS.assetNoAssets());
                    assets.add(asset);
                    callback.onSuccess(assets);
                }
                dirty = false;
            }
        };

        loader = new BaseTreeLoader<GwtDeviceAsset>(proxy);
        loader.addLoadListener(new DataLoadListener());
        treeStore = new TreeStore<ModelData>(loader);

        tree = new TreePanel<ModelData>(treeStore);
        tree.setWidth(200);
        tree.setDisplayProperty("componentName");
        tree.setBorders(true);
        tree.setLabelProvider(modelStringProvider);
        tree.setAutoSelect(true);
        tree.setStyleAttribute("background-color", "white");

        assetValuesContainer.add(tree, westData);

        //
        // Selection Listener for the component
        // make sure the form is not dirty before switching.
        tree.getSelectionModel().addListener(Events.BeforeSelect, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                BaseEvent theEvent = be;
                SelectionEvent<ModelData> se = (SelectionEvent<ModelData>) be;

                final GwtDeviceAsset assetToSwitchTo = (GwtDeviceAsset) se.getModel();
                if (assetValuesPanel != null && assetValuesPanel.isDirty()) {

                    // cancel the event first
                    theEvent.setCancelled(true);

                    // need to reselect the current entry
                    // as the BeforeSelect event cleared it
                    // we need to do this without raising events
                    TreePanelSelectionModel selectionModel = (TreePanelSelectionModel) tree.getSelectionModel();
                    selectionModel.setFiresEvents(false);
                    selectionModel.select(false, assetValuesPanel.getAsset());
                    selectionModel.setFiresEvents(true);

                    // ask for confirmation before switching
                    MessageBox.confirm(MSGS.confirm(),
                            DEVICE_MSGS.deviceConfigDirty(),
                            new Listener<MessageBoxEvent>() {

                                @Override
                                public void handleEvent(MessageBoxEvent ce) {
                                    // if confirmed, delete
                                    Dialog dialog = ce.getDialog();
                                    if (dialog.yesText.equals(ce.getButtonClicked().getText())) {
                                        assetValuesPanel.removeFromParent();
                                        assetValuesPanel = null;
                                        tree.getSelectionModel().select(false, assetToSwitchTo);
                                    }
                                }
                            });
                } else {
                    refreshAssetPanel(assetToSwitchTo);

                    // this is needed to select the item in the Tree
                    // Temporarly disable the firing of the selection events
                    // to avoid an infinite loop as BeforeSelect would be invoked again.
                    TreePanelSelectionModel selectionModel = (TreePanelSelectionModel) tree.getSelectionModel();
                    selectionModel.setFiresEvents(false);
                    selectionModel.select(false, assetToSwitchTo);

                    // renable firing of the events
                    selectionModel.setFiresEvents(true);
                }
            }
        });

        refreshAssetPanel(null);
    }

    public void refresh() {
        if (dirty && initialized) {
            if (selectedDevice != null) {
                refreshButton.setEnabled(true);
            } else {
                refreshButton.setEnabled(false);
            }
            // clear the tree and disable the toolbar
            apply.setEnabled(false);
            reset.setEnabled(false);

            treeStore.removeAll();

            // clear the panel
            if (assetValuesPanel != null) {
                assetValuesPanel.removeAll();
                assetValuesPanel.removeFromParent();
                assetValuesPanel = null;
                refreshAssetPanel(null);
                assetValuesContainer.layout();
            }

            loader.load();
        }
    }

    public void refreshAssetPanel(GwtDeviceAsset asset) {
        apply.setEnabled(false);
        reset.setEnabled(false);

        if (assetValuesPanel != null) {
            assetValuesPanel.removeFromParent();
        }
        if (asset != null) {

            assetValuesPanel = new DeviceAssetsPanel(asset, currentSession);
            assetValuesPanel.addListener(Events.Change, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    apply.setEnabled(true);
                    reset.setEnabled(true);
                }
            });

        } else {
            assetValuesPanel = new DeviceAssetsPanel(null, currentSession);
        }
        assetValuesContainer.add(assetValuesPanel, centerData);
        assetValuesContainer.layout();
    }

    public void apply() {
        if (!assetValuesPanel.isValid()) {
            InfoDialog exitDialog = new InfoDialog(InfoDialogType.ERROR, DEVICE_MSGS.deviceConfigError());
            exitDialog.show();
            return;
        }

        // ask for confirmation
        String assetName = assetValuesPanel.getAsset().getName();
        String message = DEVICE_MSGS.deviceAssetConfirmation();

        KapuaMessageBox.confirm(MSGS.confirm(),
                message,
                new Listener<MessageBoxEvent>() {

                    @Override
                    public void handleEvent(MessageBoxEvent ce) {

                        // if confirmed, push the update
                        // if confirmed, delete
                        Dialog dialog = ce.getDialog();
                        if (dialog.yesText.equals(ce.getButtonClicked().getText())) {

                            assetValuesPanel.mask(MSGS.applying());
                            tree.mask();
                            apply.setEnabled(false);
                            reset.setEnabled(false);
                            refreshButton.setEnabled(false);

                            //
                            // Getting XSRF token
                            gwtXSRFService.generateSecurityToken(new AsyncCallback<GwtXSRFToken>() {

                                @Override
                                public void onFailure(Throwable ex) {
                                    FailureHandler.handle(ex);
                                }

                                @Override
                                public void onSuccess(GwtXSRFToken token) {
                                    GwtDeviceAsset updatedDeviceAsset = assetValuesPanel.getUpdatedAsset();

                                    gwtDeviceAssetService.write(
                                            token,
                                            currentSession.getSelectedAccountId(),
                                            selectedDevice.getId(),
                                            updatedDeviceAsset,
                                            new AsyncCallback<Void>() {

                                                @Override
                                                public void onFailure(Throwable caught) {
                                                    FailureHandler.handle(caught);

                                                    assetValuesPanel.unmask();
                                                    tree.unmask();

                                                    apply.setEnabled(true);
                                                    reset.setEnabled(true);
                                                    refreshButton.setEnabled(true);
                                                }

                                                @Override
                                                public void onSuccess(Void arg0) {
                                                    dirty = true;
                                                    refresh();
                                                }
                                            });
                                }
                            });

                            // start the configuration update
                        }
                    }
                });
    }

    public void reset() {
        final GwtDeviceAsset asset = (GwtDeviceAsset) tree.getSelectionModel().getSelectedItem();
        if (assetValuesPanel != null && asset != null) {
            KapuaMessageBox.confirm(MSGS.confirm(),
                    DEVICE_MSGS.deviceConfigDirty(),
                    new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent ce) {
                            // if confirmed, delete
                            Dialog dialog = ce.getDialog();
                            if (dialog.yesText.equals(ce.getButtonClicked().getText())) {
                                refreshAssetPanel(asset);
                            }
                        }
                    });
        }
    }

    private ModelStringProvider<ModelData> modelStringProvider = new ModelStringProvider<ModelData>() {

        @Override
        public String getStringValue(ModelData model, String property) {

            Label label = new Label(((GwtDeviceAsset) model).getName(), null);
            return label.getText();
        }
    };

    // --------------------------------------------------------------------------------------
    //
    // Data Load Listener
    //
    // --------------------------------------------------------------------------------------

    private class DataLoadListener extends KapuaLoadListener {

        public DataLoadListener() {
        }

        @Override
        public void loaderBeforeLoad(LoadEvent le) {
            super.loaderBeforeLoad(le);
            refreshButton.disable();
        }

        @Override
        public void loaderLoad(LoadEvent le) {
            if (le.exception != null) {
                FailureHandler.handle(le.exception);
            }
            tree.unmask();
            refreshButton.enable();
        }

        @Override
        public void loaderLoadException(LoadEvent le) {
            if (le.exception != null && le.exception instanceof GwtKapuaException) {
                FailureHandler.handle(le.exception);
            } else {
                ConsoleInfo.display(MSGS.popupError(), DEVICE_MSGS.assetNoAssetsErrorMessage());
            }

            List<ModelData> assets = new ArrayList<ModelData>();
            GwtDeviceAsset asset = new GwtDeviceAsset();
            asset.setName(DEVICE_MSGS.assetNoAssets());
            asset.setDescription(DEVICE_MSGS.noAsset());
            assets.add(asset);
            treeStore.removeAll();
            treeStore.add(assets, false);

            tree.unmask();
            refreshButton.enable();
        }
    }
}
