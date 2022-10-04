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
package org.eclipse.kapua.app.console.module.api.client.ui.color;

public enum Color {
    /**
     * Dark blue used in Kapua logo
     */
    BLUE_KAPUA(16, 38, 68),

    /**
     * Shade of cyan used in Kapua logo
     */
    CYAN_KAPUA(64, 139, 211),

    /**
     * Regular white
     */
    WHITE(255, 255, 255),

    /**
     * Nice green
     */
    GREEN(29, 158, 116),

    /**
     * Yellow
     */
    YELLOW(230, 200, 29),

    /**
     * Red
     */
    RED(190, 70, 70),

    /**
     * Grey
     */
    GREY(220, 220, 220),

    /**
     * Blue Sky - No, it's not a typo. Walter approved ;)
     */
    BLUE_SKY(75,144,207),

    ;

    int r;
    int g;
    int b;

    private Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }
}
