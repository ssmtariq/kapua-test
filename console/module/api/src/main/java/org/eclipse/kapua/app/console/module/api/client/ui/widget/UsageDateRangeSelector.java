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
package org.eclipse.kapua.app.console.module.api.client.ui.widget;

import java.util.Date;

import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.DateUtils;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class UsageDateRangeSelector extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private SimpleComboBox<String> dateRange;
    private UsageDateRangeSelectorListener listener;

    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        dateRange = new SimpleComboBox<String>();
        dateRange.setEditable(false);
        dateRange.setTypeAhead(true);
        dateRange.setTriggerAction(TriggerAction.ALL);
        dateRange.add(MSGS.accountUsageBillingCurrent());
        dateRange.add(MSGS.accountUsageBillingLast());
        dateRange.setSimpleValue(MSGS.accountUsageBillingCurrent());

        dateRange.addSelectionChangedListener(new SelectionChangedListener<SimpleComboValue<String>>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<SimpleComboValue<String>> se) {
                if (listener != null) {
                    listener.onUpdate();
                }
            }
        });

        add(dateRange);
    }

    public UsageDateRangeSelectorListener getListener() {
        return listener;
    }

    public void setListener(UsageDateRangeSelectorListener listener) {
        this.listener = listener;
    }

    public Date getStartDate() {
        Date startDate = null;

        if (dateRange == null) {
            startDate = new Date();
            startDate = DateUtils.setDayOfMonth(startDate, 1);
            startDate = DateUtils.setHour(startDate, 0);
            startDate = DateUtils.setMinute(startDate, 0);
            startDate = DateUtils.setSecond(startDate, 0);
            startDate = DateUtils.setMillisecond(startDate, 0);
            return startDate;
        }

        try {
            switch (dateRange.getSelectedIndex()) {
            case 0: // current billing cycle
                startDate = new Date();
                startDate = DateUtils.setDayOfMonth(startDate, 1);
                startDate = DateUtils.setHour(startDate, 0);
                startDate = DateUtils.setMinute(startDate, 0);
                startDate = DateUtils.setSecond(startDate, 0);
                startDate = DateUtils.setMillisecond(startDate, 0);
                break;
            case 1: // last billing cycle
                startDate = new Date();
                if (DateUtils.getMonth(startDate) == 0) {
                    startDate = DateUtils.setYear(startDate, DateUtils.getYear(startDate) - 1);
                    startDate = DateUtils.setMonth(startDate, 11);
                    startDate = DateUtils.setDayOfMonth(startDate, 1);
                    startDate = DateUtils.setHour(startDate, 0);
                    startDate = DateUtils.setMinute(startDate, 0);
                    startDate = DateUtils.setSecond(startDate, 0);
                    startDate = DateUtils.setMillisecond(startDate, 0);
                } else {
                    startDate = DateUtils.setMonth(startDate, DateUtils.getMonth(startDate) - 1);
                    startDate = DateUtils.setDayOfMonth(startDate, 1);
                    startDate = DateUtils.setHour(startDate, 0);
                    startDate = DateUtils.setMinute(startDate, 0);
                    startDate = DateUtils.setSecond(startDate, 0);
                    startDate = DateUtils.setMillisecond(startDate, 0);
                }

                break;
            default: // default is current
                startDate = new Date();
                startDate = DateUtils.setDayOfMonth(startDate, 1);
                startDate = DateUtils.setHour(startDate, 0);
                startDate = DateUtils.setMinute(startDate, 0);
                startDate = DateUtils.setSecond(startDate, 0);
                startDate = DateUtils.setMillisecond(startDate, 0);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            startDate = new Date();
            startDate = DateUtils.setDayOfMonth(startDate, 1);
            startDate = DateUtils.setHour(startDate, 0);
            startDate = DateUtils.setMinute(startDate, 0);
            startDate = DateUtils.setSecond(startDate, 0);
            startDate = DateUtils.setMillisecond(startDate, 0);
        }

        return startDate;
    }

    public Date getEndDate() {
        Date endDate = new Date();

        if (dateRange == null) {
            return endDate;
        }

        try {
            switch (dateRange.getSelectedIndex()) {
            case 1: // last billing cycle
                endDate = new Date();
                if (DateUtils.getMonth(endDate) == 0) {
                    // We are currently in January, previous month is December in previous year
                    endDate = DateUtils.setYear(endDate, DateUtils.getYear(endDate) - 1);
                    endDate = DateUtils.setMonth(endDate, 11);
                    endDate = DateUtils.setToLastDayOfMonth(endDate);
                    endDate = DateUtils.setHour(endDate, 23);
                    endDate = DateUtils.setMinute(endDate, 59);
                    endDate = DateUtils.setSecond(endDate, 59);
                    endDate = DateUtils.setMillisecond(endDate, 999);
                } else {
                    endDate = DateUtils.setMonth(endDate, DateUtils.getMonth(endDate) - 1);
                    endDate = DateUtils.setToLastDayOfMonth(endDate);
                    endDate = DateUtils.setHour(endDate, 23);
                    endDate = DateUtils.setMinute(endDate, 59);
                    endDate = DateUtils.setSecond(endDate, 59);
                    endDate = DateUtils.setMillisecond(endDate, 999);
                }

                break;
            default: // just return the current date/time
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return endDate;
    }
}
