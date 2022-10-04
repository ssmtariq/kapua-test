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
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.qa.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

/**
 * Simple container for between step data that is persisted between steps and
 * between step implementation classes.
 */
@Singleton
public class StepData {

    private static final String COUNT = "count";
    private static final String INITIAL_COUNT = "InitialCount";

    /**
     * Generic map that accepts string key that represents data and data
     * as any object.
     * Dev-user has to know type of data stored under specified key.
     * Key could be class name.
     */
    Map<String, Object> stepDataMap;

    public StepData() {
        stepDataMap = new HashMap<>();
    }

    public void clear() {
        stepDataMap.clear();
    }

    public void put(String key, Object value) {
        stepDataMap.put(key, value);
    }

    public Object get(String key) {
        return stepDataMap.get(key);
    }

    public boolean contains(String key) {
        return stepDataMap.containsKey(key);
    }

    public void remove(String key) {
        stepDataMap.remove(key);
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        Set<String> setOfKeys = stepDataMap.keySet();
        Iterator<String> keyIterator = setOfKeys.iterator();
        while (keyIterator.hasNext()) {
            keys.add(keyIterator.next());
        }

        return keys;
    }

    public int getCount() {
        return (int)(get(COUNT)!=null ? get(COUNT) : -1);
    }

    public void updateCount(int count) {
        remove(COUNT);
        put(COUNT, count);
    }

    public int getInitialCount() {
        return (int)(get(INITIAL_COUNT)!=null ? get(INITIAL_COUNT) : -1);
    }

    public void updateInitialCount(int count) {
        remove(INITIAL_COUNT);
        put(INITIAL_COUNT, count);
    }
}
