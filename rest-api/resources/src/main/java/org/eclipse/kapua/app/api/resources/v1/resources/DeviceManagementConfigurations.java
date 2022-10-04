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
package org.eclipse.kapua.app.api.resources.v1.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.device.management.configuration.DeviceComponentConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfiguration;
import org.eclipse.kapua.service.device.management.configuration.DeviceConfigurationManagementService;
import org.eclipse.kapua.service.device.registry.Device;

@Path("{scopeId}/devices/{deviceId}/configurations")
public class DeviceManagementConfigurations extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final DeviceConfigurationManagementService configurationService = locator.getService(DeviceConfigurationManagementService.class);

    /**
     * Returns the current configuration of the device.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceConfiguration get(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return getComponent(scopeId, deviceId, null, timeout);
    }

    /**
     * Updates the configuration of a {@link Device}
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @param deviceConfiguration
     *            The configuration to send to the {@link Device}
     * @return The {@link Response} of the operation
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @QueryParam("timeout") Long timeout,
            DeviceConfiguration deviceConfiguration) throws KapuaException {
        configurationService.put(scopeId, deviceId, deviceConfiguration, timeout);

        return returnNoContent();
    }

    /**
     * Returns the configuration of a device or the configuration of the OSGi component
     * identified with specified PID (service's persistent identity).
     * In the OSGi framework, the service's persistent identity is defined as the name attribute of the
     * Component Descriptor XML file; at runtime, the same value is also available
     * in the component.name and in the service.pid attributes of the Component Configuration.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param componentId
     *            An optional id of the component to get the configuration for
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{componentId}")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public DeviceConfiguration getComponent(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("componentId") String componentId,
            @QueryParam("timeout") Long timeout) throws KapuaException {
        return configurationService.get(scopeId, deviceId, null, componentId, timeout);
    }

    /**
     * Updates the configuration of the OSGi component
     * identified with specified PID (service's persistent identity).
     * In the OSGi framework, the service's persistent identity is defined as the name attribute of the
     * Component Descriptor XML file; at runtime, the same value is also available
     * in the component.name and in the service.pid attributes of the Component Configuration.
     *
     * @param scopeId
     *            The {@link ScopeId} of the {@link Device}.
     * @param deviceId
     *            The id of the device
     * @param componentId
     *            An optional id of the component to get the configuration for
     * @param timeout
     *            The timeout of the operation in milliseconds
     * @param deviceComponentConfiguration
     *            The component configuration to send to the {@link Device}
     * @return The requested configurations
     * @throws KapuaException
     *             Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Path("{componentId}")
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response updateComponent(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("deviceId") EntityId deviceId,
            @PathParam("componentId") String componentId,
            @QueryParam("timeout") Long timeout,
            DeviceComponentConfiguration deviceComponentConfiguration) throws KapuaException {
        deviceComponentConfiguration.setId(componentId);

        configurationService.put(scopeId, deviceId, deviceComponentConfiguration, timeout);

        return returnNoContent();
    }
}
