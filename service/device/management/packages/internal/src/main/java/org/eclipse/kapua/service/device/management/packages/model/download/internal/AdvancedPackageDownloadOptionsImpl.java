/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.packages.model.download.internal;

import org.eclipse.kapua.service.device.management.packages.model.download.AdvancedPackageDownloadOptions;

/**
 * {@link AdvancedPackageDownloadOptions} implementation.
 *
 * @since 1.1.0
 */
public class AdvancedPackageDownloadOptionsImpl implements AdvancedPackageDownloadOptions {

    private Boolean restart;
    private Integer blockSize;
    private Integer blockDelay;
    private Integer blockTimeout;
    private Integer notifyBlockSize;
    private String installVerifierURI;

    /**
     * Constructor.
     *
     * @since 1.1.0
     */
    public AdvancedPackageDownloadOptionsImpl() {
    }

    /**
     * Clone Constructor.
     *
     * @param advancedPackageDownloadOptions The {@link AdvancedPackageDownloadOptions} to clone.
     * @since 1.1.0
     */
    public AdvancedPackageDownloadOptionsImpl(AdvancedPackageDownloadOptions advancedPackageDownloadOptions) {
        super();

        setRestart(advancedPackageDownloadOptions.getRestart());
        setBlockSize(advancedPackageDownloadOptions.getBlockSize());
        setBlockDelay(advancedPackageDownloadOptions.getBlockDelay());
        setBlockTimeout(advancedPackageDownloadOptions.getBlockTimeout());
        setNotifyBlockSize(advancedPackageDownloadOptions.getNotifyBlockSize());
        setInstallVerifierURI(advancedPackageDownloadOptions.getInstallVerifierURI());
    }

    @Override
    public Boolean getRestart() {
        return restart;
    }

    @Override
    public void setRestart(Boolean restart) {
        this.restart = restart;
    }

    @Override
    public Integer getBlockSize() {
        return blockSize;
    }

    @Override
    public void setBlockSize(Integer blockSize) {
        this.blockSize = blockSize;
    }

    @Override
    public Integer getBlockDelay() {
        return blockDelay;
    }

    @Override
    public void setBlockDelay(Integer blockDelay) {
        this.blockDelay = blockDelay;
    }

    @Override
    public Integer getBlockTimeout() {
        return blockTimeout;
    }

    @Override
    public void setBlockTimeout(Integer blockTimeout) {
        this.blockTimeout = blockTimeout;
    }

    @Override
    public Integer getNotifyBlockSize() {
        return notifyBlockSize;
    }

    @Override
    public void setNotifyBlockSize(Integer notifyBlockSize) {
        this.notifyBlockSize = notifyBlockSize;
    }

    @Override
    public String getInstallVerifierURI() {
        return installVerifierURI;
    }

    @Override
    public void setInstallVerifierURI(String installVerifierURI) {
        this.installVerifierURI = installVerifierURI;
    }

    public static AdvancedPackageDownloadOptionsImpl parse(AdvancedPackageDownloadOptions advancedPackageDownloadOptions) {
        return advancedPackageDownloadOptions != null ? (advancedPackageDownloadOptions instanceof AdvancedPackageDownloadOptionsImpl ? (AdvancedPackageDownloadOptionsImpl) advancedPackageDownloadOptions : new AdvancedPackageDownloadOptionsImpl(advancedPackageDownloadOptions)) : null;
    }
}
