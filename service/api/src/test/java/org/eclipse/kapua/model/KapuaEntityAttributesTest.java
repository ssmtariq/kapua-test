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
package org.eclipse.kapua.model;

import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@Category(JUnitTests.class)
public class KapuaEntityAttributesTest extends Assert {

    @Test
    public void kapuaEntityAttributesTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<KapuaEntityAttributes> kapuaEntityAttributes = KapuaEntityAttributes.class.getDeclaredConstructor();
        assertTrue(Modifier.isProtected(kapuaEntityAttributes.getModifiers()));
        kapuaEntityAttributes.setAccessible(true);
        kapuaEntityAttributes.newInstance();
    }
}
