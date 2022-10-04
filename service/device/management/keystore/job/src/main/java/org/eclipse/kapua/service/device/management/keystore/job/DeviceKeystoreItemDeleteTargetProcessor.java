/*******************************************************************************
 * Copyright (c) 2021, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.device.management.keystore.job;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.security.KapuaSecurityUtils;
import org.eclipse.kapua.job.engine.commons.operation.AbstractTargetProcessor;
import org.eclipse.kapua.job.engine.commons.wrappers.JobTargetWrapper;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.device.management.keystore.DeviceKeystoreManagementService;
import org.eclipse.kapua.service.device.management.keystore.job.definition.DeviceKeystoreItemDeletePropertyKeys;
import org.eclipse.kapua.service.device.management.keystore.model.DeviceKeystoreCertificate;
import org.eclipse.kapua.service.job.operation.TargetProcessor;
import org.eclipse.kapua.service.job.targets.JobTarget;

import javax.batch.runtime.context.JobContext;
import javax.batch.runtime.context.StepContext;
import javax.inject.Inject;

/**
 * {@link TargetProcessor} for {@link DeviceKeystoreManagementService#createKeystoreCertificate(KapuaId, KapuaId, DeviceKeystoreCertificate, Long)}.
 *
 * @since 1.0.0
 */
public class DeviceKeystoreItemDeleteTargetProcessor extends AbstractTargetProcessor implements TargetProcessor {

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final DeviceKeystoreManagementService KEYSTORE_MANAGEMENT_SERVICE = LOCATOR.getService(DeviceKeystoreManagementService.class);

    @Inject
    JobContext jobContext;

    @Inject
    StepContext stepContext;

    @Override
    protected void initProcessing(JobTargetWrapper wrappedJobTarget) {
        setContext(jobContext, stepContext);
    }

    @Override
    public void processTarget(JobTarget jobTarget) throws KapuaException {

        String keystoreId = stepContextWrapper.getStepProperty(DeviceKeystoreItemDeletePropertyKeys.KEYSTORE_ID, String.class);
        String alias = stepContextWrapper.getStepProperty(DeviceKeystoreItemDeletePropertyKeys.ALIAS, String.class);
        Long timeout = stepContextWrapper.getStepProperty(DeviceKeystoreItemDeletePropertyKeys.TIMEOUT, Long.class);

        KapuaSecurityUtils.doPrivileged(() -> KEYSTORE_MANAGEMENT_SERVICE.deleteKeystoreItem(jobTarget.getScopeId(), jobTarget.getJobTargetId(), keystoreId, alias, timeout));
    }
}
