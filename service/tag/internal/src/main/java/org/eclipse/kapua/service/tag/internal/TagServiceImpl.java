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
package org.eclipse.kapua.service.tag.internal;

import org.eclipse.kapua.KapuaEntityNotFoundException;
import org.eclipse.kapua.KapuaException;
import org.eclipse.kapua.commons.configuration.AbstractKapuaConfigurableResourceLimitedService;
import org.eclipse.kapua.commons.service.internal.KapuaNamedEntityServiceUtils;
import org.eclipse.kapua.commons.util.ArgumentValidator;
import org.eclipse.kapua.model.domain.Actions;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.KapuaQuery;
import org.eclipse.kapua.service.authorization.AuthorizationService;
import org.eclipse.kapua.service.authorization.permission.PermissionFactory;
import org.eclipse.kapua.service.tag.Tag;
import org.eclipse.kapua.service.tag.TagCreator;
import org.eclipse.kapua.service.tag.TagDomains;
import org.eclipse.kapua.service.tag.TagFactory;
import org.eclipse.kapua.service.tag.TagListResult;
import org.eclipse.kapua.service.tag.TagQuery;
import org.eclipse.kapua.service.tag.TagService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * {@link TagService} implementation.
 *
 * @since 1.0.0
 */
@Singleton
public class TagServiceImpl extends AbstractKapuaConfigurableResourceLimitedService<Tag, TagCreator, TagService, TagListResult, TagQuery, TagFactory> implements TagService {

    @Inject
    private AuthorizationService authorizationService;

    @Inject
    private PermissionFactory permissionFactory;

    public TagServiceImpl() {
        super(TagService.class.getName(),
                TagDomains.TAG_DOMAIN,
                TagEntityManagerFactory.getInstance(),
                TagService.class,
                TagFactory.class);
    }

    @Override
    public Tag create(TagCreator tagCreator) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(tagCreator, "tagCreator");
        ArgumentValidator.notNull(tagCreator.getScopeId(), "tagCreator.scopeId");
        ArgumentValidator.validateEntityName(tagCreator.getName(), "tagCreator.name");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.write, tagCreator.getScopeId()));

        //
        // Check entity limit
        checkAllowedEntities(tagCreator.getScopeId(), "Tags");

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, tagCreator);

        //
        // Do create
        return entityManagerSession.doTransactedAction(em -> TagDAO.create(em, tagCreator));
    }

    @Override
    public Tag update(Tag tag) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(tag, "tag");
        ArgumentValidator.notNull(tag.getId(), "tag.id");
        ArgumentValidator.notNull(tag.getScopeId(), "tag.scopeId");
        ArgumentValidator.validateEntityName(tag.getName(), "tag.name");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.write, tag.getScopeId()));

        //
        // Check existence
        if (find(tag.getScopeId(), tag.getId()) == null) {
            throw new KapuaEntityNotFoundException(Tag.TYPE, tag.getId());
        }

        //
        // Check duplicate name
        KapuaNamedEntityServiceUtils.checkEntityNameUniqueness(this, tag);

        //
        // Do Update
        return entityManagerSession.doTransactedAction(em -> TagDAO.update(em, tag));
    }

    @Override
    public void delete(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.delete, scopeId));

        //
        // Check existence
        if (find(scopeId, tagId) == null) {
            throw new KapuaEntityNotFoundException(Tag.TYPE, tagId);
        }

        //
        //
        entityManagerSession.doTransactedAction(em -> TagDAO.delete(em, scopeId, tagId));
    }

    @Override
    public Tag find(KapuaId scopeId, KapuaId tagId) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(scopeId, "scopeId");
        ArgumentValidator.notNull(tagId, "tagId");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.read, scopeId));

        //
        // Do find
        return entityManagerSession.doAction(em -> TagDAO.find(em, scopeId, tagId));
    }

    @Override
    public TagListResult query(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do query
        return entityManagerSession.doAction(em -> TagDAO.query(em, query));
    }

    @Override
    public long count(KapuaQuery query) throws KapuaException {
        //
        // Argument validation
        ArgumentValidator.notNull(query, "query");

        //
        // Check Access
        authorizationService.checkPermission(permissionFactory.newPermission(TagDomains.TAG_DOMAIN, Actions.read, query.getScopeId()));

        //
        // Do count
        return entityManagerSession.doAction(em -> TagDAO.count(em, query));
    }
}
