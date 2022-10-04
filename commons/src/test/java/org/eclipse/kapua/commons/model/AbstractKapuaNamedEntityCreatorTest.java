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
package org.eclipse.kapua.commons.model;

import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.qa.markers.junit.JUnitTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@Category(JUnitTests.class)
@RunWith(value = Parameterized.class)
public class AbstractKapuaNamedEntityCreatorTest extends Assert {

    private final String name;

    private final String description;

    KapuaId scopeId = new KapuaEid();

    AbstractKapuaNamedEntityCreator namedEntityCreator = new ActualKapuaNamedEntityCreator(scopeId);

    public AbstractKapuaNamedEntityCreatorTest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Parameters
    public static Collection<Object[]> strings() {
        return Arrays.asList(new Object[][]{
                {"", ""},
                {"name", "description"},
                {"NAME", "DESCRIPTION"},
                {"&name%", "#description!"},
                {"1234", "5678"},
                {"make space", "make space"},
        });
    }

    private class ActualKapuaNamedEntityCreator<E extends KapuaEntity> extends AbstractKapuaNamedEntityCreator<E> {

        protected ActualKapuaNamedEntityCreator(KapuaId scopeId) {
            super(scopeId);
        }

        protected ActualKapuaNamedEntityCreator(KapuaId scopeId, String name) {
            super(scopeId, name);
        }
    }

    @Test
    public void abstractKapuaNamedEntityCreatorNameTest() {
        namedEntityCreator.setName(name);
        AbstractKapuaNamedEntityCreator namedCopyEntityCreator = new ActualKapuaNamedEntityCreator(scopeId, name);
        assertEquals("Expected and actual values should be the same!", namedEntityCreator.getName(), namedCopyEntityCreator.getName());
    }

    @Test
    public void getDescriptionTest() {
        namedEntityCreator.setDescription(description);
        assertEquals("Expected and actual values should be the same!", description, namedEntityCreator.getDescription());
    }
}
