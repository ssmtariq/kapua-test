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
package org.eclipse.kapua.app.api.resources.v1.resources;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.app.api.core.model.device.management.JsonGenericRequestMessage;
import org.eclipse.kapua.app.api.core.model.device.management.JsonGenericResponseMessage;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.app.api.resources.v1.resources.marker.JsonSerializationFixed;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.type.ObjectValueConverter;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.request.GenericRequestFactory;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestMessage;
import org.eclipse.kapua.service.device.management.request.message.request.GenericRequestPayload;
import org.eclipse.kapua.service.device.management.request.message.response.GenericResponseMessage;
import org.eclipse.kapua.service.device.registry.Device;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @see JsonSerializationFixed
 */
@Path("{scopeId}/devices/{deviceId}/requests")
public class DeviceManagementRequestsJson extends AbstractKapuaResource implements JsonSerializationFixed {

    private static final DeviceManagementRequests DEVICE_MANAGEMENT_REQUESTS = new DeviceManagementRequests();

    private static final KapuaLocator LOCATOR = KapuaLocator.getInstance();
    private static final GenericRequestFactory GENERIC_REQUEST_FACTORY = LOCATOR.getFactory(GenericRequestFactory.class);

    /**
     * Sends a request message to a device.
     * This call is generally used to perform remote management of resources
     * attached to the device such sensors and registries.
     *
     * @param scopeId                   The {@link ScopeId} of the {@link Device}.
     * @param deviceId                  The {@link Device} ID.
     * @param timeout                   The timeout of the request execution
     * @param jsonGenericRequestMessage The input request
     * @return The response output.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public JsonGenericResponseMessage sendRequest(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            JsonGenericRequestMessage jsonGenericRequestMessage) throws KapuaException {

        GenericRequestMessage genericRequestMessage = GENERIC_REQUEST_FACTORY.newRequestMessage();

        genericRequestMessage.setId(jsonGenericRequestMessage.getId());
        genericRequestMessage.setScopeId(scopeId);
        genericRequestMessage.setDeviceId(jsonGenericRequestMessage.getDeviceId());
        genericRequestMessage.setClientId(jsonGenericRequestMessage.getClientId());
        genericRequestMessage.setReceivedOn(jsonGenericRequestMessage.getReceivedOn());
        genericRequestMessage.setSentOn(jsonGenericRequestMessage.getSentOn());
        genericRequestMessage.setCapturedOn(jsonGenericRequestMessage.getCapturedOn());
        genericRequestMessage.setPosition(jsonGenericRequestMessage.getPosition());
        genericRequestMessage.setChannel(jsonGenericRequestMessage.getChannel());

        GenericRequestPayload kapuaDataPayload = GENERIC_REQUEST_FACTORY.newRequestPayload();

        if (jsonGenericRequestMessage.getPayload() != null) {
            kapuaDataPayload.setBody(jsonGenericRequestMessage.getPayload().getBody());

            jsonGenericRequestMessage.getPayload().getMetrics().forEach(
                    jsonMetric -> {
                        String name = jsonMetric.getName();
                        Object value = ObjectValueConverter.fromString(jsonMetric.getValue(), jsonMetric.getValueType());

                        kapuaDataPayload.getMetrics().put(name, value);
                    });
        }

        genericRequestMessage.setPayload(kapuaDataPayload);

        GenericResponseMessage genericResponseMessage = DEVICE_MANAGEMENT_REQUESTS.sendRequest(scopeId, deviceId, timeout, genericRequestMessage);

        return new JsonGenericResponseMessage(genericResponseMessage);
    }
}
