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
package org.eclipse.kapua.commons.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;

/**
 * Default Kapua cache implementation
 *
 * @param <K>
 *            keys type
 * @param <V>
 *            values type
 *
 * @since 1.0
 */
public class LocalCache<K, V> implements Cache<K, V> {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(LocalCache.class);

    private String namespace;
    private com.google.common.cache.Cache<K, V> cache;
    private V defaultValue;

    /**
     * Construct local cache setting the provided max size, expire time and default value
     *
     * @param sizeMax
     *            max cache size
     * @param expireAfter
     *            values ttl
     * @param defaultValue
     *            default value (if no value is found for a specific key)
     */
    public LocalCache(int sizeMax, int expireAfter, final V defaultValue) {
        this.defaultValue = defaultValue;
        cache = CacheBuilder.newBuilder().maximumSize(sizeMax).expireAfterWrite(expireAfter, TimeUnit.SECONDS).build();
    }

    /**
     * Construct local cache setting the provided max size and default value. <b>ttl is disabled, so no time based eviction will be performed.</b>
     *
     * @param sizeMax
     *            max cache size
     * @param defaultValue
     *            default value (if no value is found for a specific key)
     */
    public LocalCache(int sizeMax, final V defaultValue) {
        this.defaultValue = defaultValue;
        // from google javadoc ("https://google.github.io/guava/releases/19.0/api/docs/com/google/common/cache/CacheBuilder.html")
        // By default cache instances created by CacheBuilder will not perform any type of eviction.

        cache = CacheBuilder.newBuilder().maximumSize(sizeMax).build();
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public void setNamespace(String namespace) {
        this.namespace = namespace;

    }

    @Override
    public V get(K k) {
        if (cache != null) {
            V v = cache.getIfPresent(k);
            if (v != null) {
                return v;
            }
        }
        return defaultValue;
    }

    /**
     * Return the list of all the keys present in the cache
     *
     * @return
     */
    public List<K> getAllKeys() {
        ArrayList<K> keys = new ArrayList<K>();
        if (cache != null) {
            keys.addAll(cache.asMap().keySet());
        }
        return keys;
    }

    @Override
    public void put(K k, V v) {
        if (cache != null) {
            cache.put(k, v);
        }
    }

    @Override
    public void remove(K k) {
        if (cache != null) {
            cache.invalidate(k);
        }
    }

    @Override
    public void invalidateAll() {
        cache.invalidateAll();
    }
}
