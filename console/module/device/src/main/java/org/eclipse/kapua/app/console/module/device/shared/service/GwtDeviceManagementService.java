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
package org.eclipse.kapua.app.console.module.device.shared.service;

import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.eclipse.kapua.app.console.module.api.client.GwtKapuaException;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtXSRFToken;
import org.eclipse.kapua.app.console.module.device.shared.model.GwtDevice;
import org.eclipse.kapua.app.console.module.device.shared.model.management.bundles.GwtBundle;
import org.eclipse.kapua.app.console.module.device.shared.model.management.command.GwtDeviceCommandInput;
import org.eclipse.kapua.app.console.module.device.shared.model.management.command.GwtDeviceCommandOutput;
import org.eclipse.kapua.app.console.module.device.shared.model.management.configurations.GwtSnapshot;
import org.eclipse.kapua.app.console.module.device.shared.model.management.packages.GwtDeploymentPackage;
import org.eclipse.kapua.app.console.module.device.shared.model.management.packages.GwtPackageInstallRequest;
import org.eclipse.kapua.app.console.module.device.shared.model.management.packages.GwtPackageOperation;
import org.eclipse.kapua.app.console.module.device.shared.model.management.packages.GwtPackageUninstallRequest;

import java.util.List;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deviceManagement")
public interface GwtDeviceManagementService extends RemoteService {

    //
    // Packages
    //

    /**
     * Returns the packages installed on a Device.
     *
     * @param scopeShortId  device scope id in short form
     * @param deviceShortId device id in short form
     * @return list of installed packages installed on the selected device
     * @throws GwtKapuaException
     * @since 1.0.0
     */
    List<GwtDeploymentPackage> findDevicePackages(String scopeShortId, String deviceShortId)
            throws GwtKapuaException;

    /**
     * Install a package into a device.
     *
     * @param xsrfToken                XARF token associated with the request
     * @param gwtPackageInstallRequest the package install request
     * @throws GwtKapuaException
     * @since 1.0.0
     */
    void installPackage(GwtXSRFToken xsrfToken, GwtPackageInstallRequest gwtPackageInstallRequest)
            throws GwtKapuaException;

    /**
     * Get the status of downloads operations running on the device
     *
     * @param scopeShortId  device scope id in short form
     * @param deviceShortId device id in short form
     * @return list of current running download operation on the device
     * @throws GwtKapuaException
     * @since 1.0.0
     */
    ListLoadResult<GwtPackageOperation> getDownloadOperations(String scopeShortId, String deviceShortId)
            throws GwtKapuaException;

    /**
     * Uninstalls a package from a device.
     *
     * @param xsrfToken                  XARF token associated with the request
     * @param gwtPackageUninstallRequest the package uninstall request
     * @throws GwtKapuaException
     * @since 1.0.0
     */
    void uninstallPackage(GwtXSRFToken xsrfToken, GwtPackageUninstallRequest gwtPackageUninstallRequest)
            throws GwtKapuaException;

    //
    // Configurations
    //

    /**
     * Returns the configuration of a Device as the list of all the configurable components.
     *
     * @param device
     * @return
     */
    List<GwtConfigComponent> findDeviceConfigurations(GwtDevice device)
            throws GwtKapuaException;

    /**
     * Updates the configuration of the provided component.
     *
     * @param device
     * @param configComponent
     */
    void updateComponentConfiguration(GwtXSRFToken xsrfToken, GwtDevice device, GwtConfigComponent configComponent)
            throws GwtKapuaException;

    //
    // Snapshots
    //

    /**
     * @param device
     * @return
     * @throws GwtKapuaException
     */
    ListLoadResult<GwtSnapshot> findDeviceSnapshots(GwtDevice device)
            throws GwtKapuaException;

    /**
     * @param device
     * @param snapshot
     * @throws GwtKapuaException
     */
    void rollbackDeviceSnapshot(GwtXSRFToken xsfrToken, GwtDevice device, GwtSnapshot snapshot)
            throws GwtKapuaException;

    //
    // Bundles
    //
    ListLoadResult<GwtBundle> findBundles(GwtDevice device)
            throws GwtKapuaException;

    void startBundle(GwtXSRFToken xsfrToken, GwtDevice device, GwtBundle pair)
            throws GwtKapuaException;

    void stopBundle(GwtXSRFToken xsfrToken, GwtDevice device, GwtBundle pair)
            throws GwtKapuaException;

    //
    // Commands
    //

    /**
     * Executes a command on a remote Device.
     *
     * @param xsfrToken
     * @param device
     * @param commandInput
     * @return
     */
    GwtDeviceCommandOutput executeCommand(GwtXSRFToken xsfrToken, GwtDevice device, GwtDeviceCommandInput commandInput)
            throws GwtKapuaException;

}
