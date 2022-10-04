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
package org.eclipse.kapua.model.query;

import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.query.predicate.AndPredicate;
import org.eclipse.kapua.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.model.query.predicate.OrPredicate;
import org.eclipse.kapua.model.query.predicate.QueryPredicate;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * {@link KapuaQuery} definition.
 */
public interface KapuaQuery {

    /**
     * Gets the fetch attribute names list.
     *
     * @return The fetch attribute names list.
     * @since 1.0.0
     */
    @XmlElementWrapper(name = "fetchAttributeName")
    @XmlElement(name = "fetchAttributeName")
    List<String> getFetchAttributes();

    /**
     * Adds an attribute to the fetch attribute names list
     *
     * @param fetchAttribute The fetch attribute to add to the list.
     * @since 1.0.0
     */
    void addFetchAttributes(@NotNull String fetchAttribute);

    /**
     * Sets the fetch attribute names list.<br>
     * This list is a list of optional attributes of a {@link KapuaEntity} that can be fetched when querying.
     *
     * @param fetchAttributeNames The fetch attribute names list.
     * @since 1.0.0
     */
    void setFetchAttributes(@NotNull List<String> fetchAttributeNames);

    /**
     * Get the scope {@link KapuaId} in which to query.
     *
     * @return The scope {@link KapuaId} in which to query.
     * @since 1.0.0
     */
    @XmlElement(name = "scopeId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getScopeId();

    /**
     * Set the scope {@link KapuaId} in which to query.
     *
     * @param scopeId The scope {@link KapuaId} in which to query.
     * @since 1.0.0
     */
    void setScopeId(KapuaId scopeId);

    /**
     * Gets the {@link KapuaQuery} {@link QueryPredicate}s.
     *
     * @return The {@link KapuaQuery} {@link QueryPredicate}s.
     * @since 1.0.0
     */
    @XmlTransient
    QueryPredicate getPredicate();

    /**
     * Sets the {@link KapuaQuery} {@link QueryPredicate}s.<br>
     * The {@link QueryPredicate} can be a simple {@link AttributePredicate} or a combination
     * of them by using the {@link AndPredicate}
     *
     * @param queryPredicate The {@link KapuaQuery} {@link QueryPredicate}s.
     * @since 1.0.0
     */
    void setPredicate(@NotNull QueryPredicate queryPredicate);

    /**
     * Gets the {@link KapuaQuery} {@link KapuaSortCriteria}
     *
     * @return The {@link KapuaQuery} {@link KapuaSortCriteria}
     * @since 1.0.0
     */
    @XmlTransient
    KapuaSortCriteria getSortCriteria();

    /**
     * Sets the {@link KapuaQuery} {@link KapuaSortCriteria}.
     *
     * @param sortCriteria The {@link KapuaQuery} {@link KapuaSortCriteria}.
     * @since 1.0.0
     */
    void setSortCriteria(@NotNull KapuaSortCriteria sortCriteria);

    /**
     * Gets the default {@link KapuaSortCriteria} to use if {@link #getSortCriteria()} is not specified.
     *
     * @return The default {@link KapuaSortCriteria}
     * @since 1.5.0
     */
    @XmlTransient
    KapuaSortCriteria getDefaultSortCriteria();

    /**
     * Gets the {@link KapuaQuery} offset.
     *
     * @return The {@link KapuaQuery} offset.
     * @since 1.0.0
     */
    @XmlElement(name = "offset")
    Integer getOffset();

    /**
     * Set the {@link KapuaQuery} offset in the result set from which start query.
     * <p>
     * If set to {@code null} the {@link KapuaQuery} will start from the first result found.
     * This also mean that {@link #setOffset(Integer)} with {@code 0} or {@code null} will produce the same result.
     * <p>
     * This method and {@link #setLimit(Integer)} are meant to be used to paginate through the result set.
     *
     * @param offset The {@link KapuaQuery} offset.
     * @since 1.0.0
     */
    void setOffset(Integer offset);

    /**
     * Gets the {@link KapuaQuery} limit.
     *
     * @return The {@link KapuaQuery} limit.
     * @since 1.0.0
     */
    @XmlElement(name = "limit")
    Integer getLimit();

    /**
     * Sets max number of result that will be fetched by this {@link KapuaEntity}.
     * <p>
     * If set to {@code null} the {@link KapuaQuery} will be unlimited.
     * <p>
     * This method and {@link #setOffset(Integer)} are meant to be used to paginate through the result set.
     *
     * @param limit The max number of result that will be fetched by this {@link KapuaEntity}.
     * @since 1.0.0
     */
    void setLimit(Integer limit);

    /**
     * Get the {@code askTotalCount} flag. If {@literal true}, the returning {@link KapuaListResult} will also return a value in
     * the {@code totalCount} field, indicating how many entries matched the query regardless of {@code limit} and
     * {@code offset}. If {@literal false}, {@code totalCount} will be {@literal null}.
     *
     * @return The value of {@code askTotalCount}
     * @since 1.2.0
     */
    Boolean getAskTotalCount();

    /**
     * Set the {@code askTotalCount} flag.
     *
     * @param askTotalCount
     * @since 1.2.0
     */
    void setAskTotalCount(Boolean askTotalCount);

    //
    // Predicates factory

    /**
     * Creates a new {@link AttributePredicate}
     *
     * @param attributeName  The name of the attribute
     * @param attributeValue The value of the attribute
     * @param <T>            The type of {@code attributeValue}
     * @return A new {@link AttributePredicate} for the given parameters
     */
    <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue);

    /**
     * Creates a new {@link AttributePredicate}
     *
     * @param attributeName  The name of the attribute
     * @param attributeValue The value of the attribute
     * @param operator       The operator to apply
     * @param <T>            The type of {@code attributeValue}
     * @return A new {@link AttributePredicate} for the given parameters
     */
    <T> AttributePredicate<T> attributePredicate(String attributeName, T attributeValue, AttributePredicate.Operator operator);

    /**
     * Creates a new, empty {@link AndPredicate}
     *
     * @return A new, empty {@link AndPredicate}
     */
    AndPredicate andPredicate();

    /**
     * Creates a new {@link AndPredicate} creating a logical AND with all the provided {@link QueryPredicate}
     *
     * @param queryPredicates A list of {@link QueryPredicate}s to create the {@link AndPredicate}
     * @return A new {@link AndPredicate}
     */
    AndPredicate andPredicate(QueryPredicate... queryPredicates);

    /**
     * Creates a new, empty {@link OrPredicate}
     *
     * @return A new, empty {@link OrPredicate}
     */
    OrPredicate orPredicate();

    /**
     * Creates a new {@link OrPredicate} creating a logical OR with all the provided {@link QueryPredicate}
     *
     * @param queryPredicates A list of {@link QueryPredicate}s to create the {@link OrPredicate}
     * @return A new {@link OrPredicate}
     */
    OrPredicate orPredicate(QueryPredicate... queryPredicates);

    /**
     * Creates a new {@link FieldSortCriteria}
     *
     * @param attributeName The name of the attribute
     * @param sortOrder     The {@link SortOrder}
     * @return
     */
    FieldSortCriteria fieldSortCriteria(String attributeName, SortOrder sortOrder);

}
