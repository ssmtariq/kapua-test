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
package org.eclipse.kapua.service.datastore.internal.converter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.kapua.service.elasticsearch.client.QueryConverter;
import org.eclipse.kapua.service.elasticsearch.client.SchemaKeys;
import org.eclipse.kapua.service.elasticsearch.client.exception.QueryMappingException;
import org.eclipse.kapua.service.storable.exception.MappingException;
import org.eclipse.kapua.service.storable.model.query.SortField;
import org.eclipse.kapua.service.storable.model.query.StorableQuery;
import org.eclipse.kapua.service.storable.model.utils.MappingUtils;

import java.util.List;

/**
 * Query converter implementation
 *
 * @since 1.0
 */
public class QueryConverterImpl implements QueryConverter {

    @Override
    public JsonNode convertQuery(Object query) throws QueryMappingException {
        if (!(query instanceof StorableQuery)) {
            throw new QueryMappingException("Given query is not a StorableQuery");
        }

        try {
            StorableQuery storableQuery = (StorableQuery) query;

            ObjectNode includesFields = MappingUtils.newObjectNode();
            includesFields.set(SchemaKeys.KEY_INCLUDES, MappingUtils.newArrayNode(storableQuery.getIncludes(storableQuery.getFetchStyle())));
            includesFields.set(SchemaKeys.KEY_EXCLUDES, MappingUtils.newArrayNode(storableQuery.getExcludes(storableQuery.getFetchStyle())));

            ObjectNode rootNode = MappingUtils.newObjectNode();
            rootNode.set(SchemaKeys.KEY_SOURCE, includesFields);

            // query
            if (storableQuery.getPredicate() != null) {
                rootNode.set(SchemaKeys.KEY_QUERY, storableQuery.getPredicate().toSerializedMap());
            }

            // sort
            ArrayNode sortNode = MappingUtils.newArrayNode();
            List<SortField> sortFields = storableQuery.getSortFields();
            if (sortFields != null) {
                for (SortField field : sortFields) {
                    sortNode.add(MappingUtils.newObjectNode(field.getField(), field.getSortDirection().name()));
                }
            }

            // offset and limit settings
            Integer offset = storableQuery.getOffset();
            if (offset != null) {
                rootNode.set(SchemaKeys.KEY_FROM, MappingUtils.newNumericNode(offset));
            }
            Integer limit = storableQuery.getLimit();
            if (limit != null) {
                rootNode.set(SchemaKeys.KEY_SIZE, MappingUtils.newNumericNode(limit));
            }
            rootNode.set(SchemaKeys.KEY_SORT, sortNode);
            return rootNode;
        } catch (MappingException me) {
            throw new QueryMappingException(me, "Cannot convert Storable Query");
        }
    }

    @Override
    public Object getFetchStyle(Object query) throws QueryMappingException {
        if (!(query instanceof StorableQuery)) {
            throw new QueryMappingException("Given query is not a StorableQuery");
        }

        return ((StorableQuery) query).getFetchStyle();
    }

}
