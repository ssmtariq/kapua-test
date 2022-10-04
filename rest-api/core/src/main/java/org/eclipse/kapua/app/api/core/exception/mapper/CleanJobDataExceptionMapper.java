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
package org.eclipse.kapua.app.api.core.exception.mapper;

import org.eclipse.kapua.app.api.core.exception.model.CleanJobDataExceptionInfo;
import org.eclipse.kapua.job.engine.exception.CleanJobDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CleanJobDataExceptionMapper implements ExceptionMapper<CleanJobDataException> {

    private static final Logger LOG = LoggerFactory.getLogger(CleanJobDataExceptionMapper.class);

    @Override
    public Response toResponse(CleanJobDataException cleanJobDataException) {
        LOG.error(cleanJobDataException.getMessage(), cleanJobDataException);

        return Response//
                .status(Status.INTERNAL_SERVER_ERROR) //
                .entity(new CleanJobDataExceptionInfo(cleanJobDataException)) //
                .build();
    }

}
