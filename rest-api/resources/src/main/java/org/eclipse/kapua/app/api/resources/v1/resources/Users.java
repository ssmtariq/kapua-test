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

import com.google.common.base.Strings;
import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.app.api.core.resources.AbstractKapuaResource;
import org.eclipse.kapua.app.api.core.model.CountResult;
import org.eclipse.kapua.app.api.core.model.EntityId;
import org.eclipse.kapua.app.api.core.model.ScopeId;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaNamedEntityAttributes;
import org.eclipse.kapua.model.query.SortOrder;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.service.KapuaService;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOption;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionCreator;
import org.eclipse.kapua.service.authentication.credential.mfa.MfaOptionService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserCreator;
import org.eclipse.kapua.service.user.UserFactory;
import org.eclipse.kapua.service.user.UserListResult;
import org.eclipse.kapua.service.user.UserQuery;
import org.eclipse.kapua.service.user.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("{scopeId}/users")
public class Users extends AbstractKapuaResource {

    private final KapuaLocator locator = KapuaLocator.getInstance();
    private final UserService userService = locator.getService(UserService.class);
    private final UserFactory userFactory = locator.getFactory(UserFactory.class);
    private final MfaOptionService mfaOptionService = locator.getService(MfaOptionService.class);

    /**
     * Gets the {@link User} list in the scope.
     *
     * @param scopeId       The {@link ScopeId} in which to search results.
     * @param name          The {@link User} name in which to search results.
     * @param sortParam     The name of the parameter that will be used as a sorting key
     * @param sortDir       The sort direction. Can be ASCENDING (default), DESCENDING. Case-insensitive.
     * @param matchTerm     A term to be matched in at least one of the configured fields of this entity
     * @param askTotalCount Ask for the total count of the matched entities in the result
     * @param offset        The result set offset.
     * @param limit         The result set limit.
     * @return The {@link UserListResult} of all the users associated to the current selected scope.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public UserListResult simpleQuery(
            @PathParam("scopeId") ScopeId scopeId,
            @QueryParam("name") String name,
            @QueryParam("matchTerm") String matchTerm,
            @QueryParam("sortParam") String sortParam,
            @QueryParam("sortDir") @DefaultValue("ASCENDING") SortOrder sortDir,
            @QueryParam("askTotalCount") boolean askTotalCount,
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("limit") @DefaultValue("50") int limit) throws KapuaException {
        UserQuery query = userFactory.newQuery(scopeId);

        AndPredicate andPredicate = query.andPredicate();
        if (!Strings.isNullOrEmpty(name)) {
            andPredicate.and(query.attributePredicate(KapuaNamedEntityAttributes.NAME, name));
        }
        if (matchTerm != null && !matchTerm.isEmpty()) {
            andPredicate.and(query.matchPredicate(matchTerm));
        }
        if (!Strings.isNullOrEmpty(sortParam)) {
            query.setSortCriteria(query.fieldSortCriteria(sortParam, sortDir));
        }

        query.setPredicate(andPredicate);

        query.setAskTotalCount(askTotalCount);
        query.setOffset(offset);
        query.setLimit(limit);

        return query(scopeId, query);
    }

    /**
     * Queries the results with the given {@link UserQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to search results.
     * @param query   The {@link UserQuery} to use to filter results.
     * @return The {@link UserListResult} of all the result matching the given {@link UserQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_query")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public UserListResult query(
            @PathParam("scopeId") ScopeId scopeId,
            UserQuery query) throws KapuaException {
        query.setScopeId(scopeId);

        return userService.query(query);
    }

    /**
     * Counts the results with the given {@link UserQuery} parameter.
     *
     * @param scopeId The {@link ScopeId} in which to count results.
     * @param query   The {@link UserQuery} to use to filter results.
     * @return The count of all the result matching the given {@link UserQuery} parameter.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Path("_count")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public CountResult count(
            @PathParam("scopeId") ScopeId scopeId,
            UserQuery query) throws KapuaException {
        query.setScopeId(scopeId);

        return new CountResult(userService.count(query));
    }

    /**
     * Creates a new User based on the information provided in UserCreator
     * parameter.
     *
     * @param scopeId     The {@link ScopeId} in which to create the {@link User}
     * @param userCreator Provides the information for the new User to be created.
     * @return The newly created User object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response create(
            @PathParam("scopeId") ScopeId scopeId,
            UserCreator userCreator) throws KapuaException {
        userCreator.setScopeId(scopeId);

        return returnCreated(userService.create(userCreator));
    }

    /**
     * Returns the User specified by the "userId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link User}.
     * @param userId  The id of the requested User.
     * @return The requested User object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @GET
    @Path("{userId}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public User find(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId) throws KapuaException {
        User user = userService.find(scopeId, userId);

        if (user == null) {
            throw new KapuaEntityNotFoundException(User.TYPE, userId);
        }

        return user;
    }

    /**
     * Updates the User based on the information provided in the User parameter.
     *
     * @param scopeId The ScopeId of the requested {@link User}.
     * @param userId  The id of the requested {@link User}
     * @param user    The modified User whose attributed need to be updated.
     * @return The updated user.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @PUT
    @Path("{userId}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public User update(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId,
            User user) throws KapuaException {
        user.setScopeId(scopeId);
        user.setId(userId);

        return userService.update(user);
    }

    /**
     * Deletes the User specified by the "userId" path parameter.
     *
     * @param scopeId The ScopeId of the requested {@link User}.
     * @param userId  The id of the User to be deleted.
     * @return HTTP 200 if operation has completed successfully.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.0.0
     */
    @DELETE
    @Path("{userId}")
    public Response deleteUser(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId) throws KapuaException {
        userService.delete(scopeId, userId);

        return returnNoContent();
    }

    /**
     * Creates a new {@link MfaOption} for the user specified by the "userId" path parameter.
     *
     * @param scopeId The {@link ScopeId} in which to create the {@link MfaOption}
     * @param userId  The {@link EntityId} of the User to which the {@link MfaOption} belongs
     * @return The newly created {@link MfaOption} object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.4.0
     */
    @POST
    @Path("{userId}/mfa")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createMfa(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId,
            MfaOptionCreator mfaOptionCreator) throws KapuaException {
        mfaOptionCreator.setScopeId(scopeId);
        mfaOptionCreator.setUserId(userId);

        return returnCreated(mfaOptionService.create(mfaOptionCreator));
    }

    /**
     * Returns the {@link MfaOption} of the user specified by the "userId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link MfaOption}
     * @param userId  The {@link EntityId} of the User to which the {@link MfaOption} belongs
     * @return The requested {@link MfaOption} object.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.4.0
     */
    @GET
    @Path("{userId}/mfa")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public MfaOption findMfa(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId) throws KapuaException {
        MfaOption mfaOption = mfaOptionService.findByUserId(scopeId, userId);
        if (mfaOption == null) {
            throw new KapuaEntityNotFoundException(MfaOption.TYPE, "MfaOption");  // TODO: not sure "MfaOption" it's the best value to return here
        }

        // Set the mfa secret key to null before returning the mfaOption, due to improve the security
        mfaOption.setMfaSecretKey(null);

        return mfaOption;
    }

    /**
     * Deletes the {@link MfaOption} of the user specified by the "userId" path parameter.
     *
     * @param scopeId The {@link ScopeId} of the requested {@link MfaOption}
     * @param userId  The {@link EntityId} of the User to which the {@link MfaOption} belongs
     * @return HTTP 200 if operation has completed successfully.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.4.0
     */
    @DELETE
    @Path("{userId}/mfa")
    public Response deleteMfa(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId) throws KapuaException {
        MfaOption mfaOption = findMfa(scopeId, userId);
        mfaOptionService.delete(scopeId, mfaOption.getId());

        return returnNoContent();
    }

    /**
     * Disable trusted machine for a given {@link MfaOption}.
     *
     * @param scopeId The ScopeId of the requested {@link MfaOption}.
     * @param userId  The {@link EntityId} of the User to which the {@link MfaOption} belongs
     * @return HTTP 200 if operation has completed successfully.
     * @throws KapuaException Whenever something bad happens. See specific {@link KapuaService} exceptions.
     * @since 1.4.0
     */
    @DELETE
    @Path("{userId}/mfa/disableTrust")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response disableTrust(
            @PathParam("scopeId") ScopeId scopeId,
            @PathParam("userId") EntityId userId) throws KapuaException {
        MfaOption mfaOption = findMfa(scopeId, userId);
        mfaOptionService.disableTrust(scopeId, mfaOption.getId());

        return returnNoContent();
    }
}
