/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.info.internal;

import org.eclipse.kapua.KapuaEntityCloneException;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.service.certificate.info.CertificateInfo;
import org.eclipse.kapua.service.certificate.info.CertificateInfoCreator;
import org.eclipse.kapua.service.certificate.info.CertificateInfoFactory;
import org.eclipse.kapua.service.certificate.info.CertificateInfoListResult;
import org.eclipse.kapua.service.certificate.info.CertificateInfoQuery;

import javax.inject.Singleton;

@Singleton
public class CertificateInfoFactoryImpl implements CertificateInfoFactory {

    @Override
    public CertificateInfo newEntity(KapuaId scopeId) {
        return new CertificateInfoImpl(scopeId);
    }

    @Override
    public CertificateInfoCreator newCreator(KapuaId scopeId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CertificateInfoQuery newQuery(KapuaId scopeId) {
        return new CertificateInfoQueryImpl(scopeId);
    }

    @Override
    public CertificateInfoListResult newListResult() {
        return new CertificateInfoListResultImpl();
    }

    @Override
    public CertificateInfo clone(CertificateInfo certificateInfo) throws KapuaEntityCloneException {
        try {
            return new CertificateInfoImpl(certificateInfo);
        } catch (Exception e) {
            throw new KapuaEntityCloneException(e, CertificateInfo.TYPE, certificateInfo);
        }
    }
}
