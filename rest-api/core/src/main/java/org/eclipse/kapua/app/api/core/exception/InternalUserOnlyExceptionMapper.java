/*******************************************************************************
 * Copyright (c) 2020, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.app.api.core.exception;

import org.eclipse.kapua.app.api.core.exception.model.InternalUserOnlyExceptionInfo;
import org.eclipse.kapua.service.authorization.shiro.exception.InternalUserOnlyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalUserOnlyExceptionMapper implements ExceptionMapper<InternalUserOnlyException> {

    private static final Logger LOG = LoggerFactory.getLogger(InternalUserOnlyExceptionMapper.class);

    @Override
    public Response toResponse(InternalUserOnlyException exception) {
        LOG.error("Only internal users allowed!", exception);
        return Response//
                .status(Status.FORBIDDEN) //
                .entity(new InternalUserOnlyExceptionInfo(Status.FORBIDDEN, exception)) //
                .build();
    }
}
