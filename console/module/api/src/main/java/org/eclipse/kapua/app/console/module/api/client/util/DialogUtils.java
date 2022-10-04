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
package org.eclipse.kapua.app.console.module.api.client.util;

import com.extjs.gxt.ui.client.widget.Window;

public class DialogUtils {

    private DialogUtils() {
    }

    /**
     * It resize the dialog if the max format height exceeds the browser window size.
     * If needed the resizing will be performed by 10% steps and caps at 50% of the original size of the dialog.
     *
     *
     * @param window
     *            The window to resize.
     * @param originalWidth
     *            The original width of this window.
     * @param originalHeight
     *            The original height of this window.
     */
    public static void resizeDialog(Window window,
            Integer originalWidth,
            Integer originalHeight) {
        int browserHeight = com.google.gwt.user.client.Window.getClientHeight();

        originalHeight += 8; // Needed for window shadow and appearance.
        int stepHeight = originalHeight / 10;

        if (browserHeight < originalHeight) {
            if (browserHeight > 0.90 * originalHeight) {
                originalHeight = stepHeight * 9;
            } else if (browserHeight > 0.80 * originalHeight) {
                originalHeight = stepHeight * 8;
            } else if (browserHeight > 0.70 * originalHeight) {
                originalHeight = stepHeight * 7;
            } else if (browserHeight > 0.60 * originalHeight) {
                originalHeight = stepHeight * 6;
            } else {
                originalHeight = stepHeight * 5;
            }
        }

        window.setSize(originalWidth, originalHeight - 8);
    }
}
