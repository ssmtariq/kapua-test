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
package org.eclipse.kapua.app.console.module.account.client;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.MultiField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.NumberPropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccount;
import org.eclipse.kapua.app.console.module.api.client.messages.ConsoleMessages;
import org.eclipse.kapua.app.console.module.api.client.util.Constants;
import org.eclipse.kapua.app.console.module.api.client.util.FailureHandler;
import org.eclipse.kapua.app.console.module.api.client.util.FormUtils;
import org.eclipse.kapua.app.console.module.api.client.util.KapuaSafeHtmlUtils;
import org.eclipse.kapua.app.console.module.api.client.util.MessageUtils;
import org.eclipse.kapua.app.console.module.api.client.util.UserAgentUtils;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.module.api.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.module.api.shared.model.session.GwtSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@SuppressWarnings("Duplicates")
public class AccountConfigPanel extends LayoutContainer {

    private static final ConsoleMessages MSGS = GWT.create(ConsoleMessages.class);

    private static final Logger logger = Logger.getLogger(AccountConfigPanel.class.getName());

    private GwtConfigComponent configComponent;
    private FormPanel actionFormPanel;
    private FieldSet actionFieldSet;

    private ComponentPlugin infoPlugin;
    private ComponentPlugin dirtyPlugin;

    private final GwtSession currentSession;

    private String selectedAccountId;

    private static final String CONFIG_MIN_VALUE = "configMinValue";
    private static final String CONFIG_MAX_VALUE = "configMaxValue";

    public AccountConfigPanel(GwtConfigComponent configComponent, GwtSession currentSession, GwtAccount selectedAccount) {
        super(new FitLayout());
        this.currentSession = currentSession;
        selectedAccountId = selectedAccount.getId();
        setScrollMode(Scroll.AUTO);
        setBorders(false);

        this.configComponent = configComponent;
        infoPlugin = new ComponentPlugin() {

            @Override
            public void init(Component component) {
                component.addListener(Events.Render, new Listener<ComponentEvent>() {

                    @Override
                    public void handleEvent(ComponentEvent be) {
                        El elem = be.getComponent().el().findParent(".x-form-element", 3);
                        if (elem != null) {
                            // should style in external CSS rather than directly
                            elem.appendChild(XDOM.create("<div style='color: #615f5f; padding: 1px 25px 5px 0px;'>" + be.getComponent().getData("text") + "</div>"));
                        }
                    }
                });
            }
        };

        final AccountConfigPanel thePanel = this;
        dirtyPlugin = new ComponentPlugin() {

            @Override
            public void init(Component component) {
                component.addListener(Events.Change, new Listener<ComponentEvent>() {

                    @Override
                    public void handleEvent(ComponentEvent be) {
                        El elem = be.getComponent().el().findParent(".x-form-element", 7);
                        if (elem != null) {
                            El dirtyIcon = elem.createChild("");
                            dirtyIcon.setStyleName("x-grid3-dirty-cell");
                            dirtyIcon.setStyleAttribute("top", "0");
                            dirtyIcon.setStyleAttribute("position", "absolute");
                            dirtyIcon.setSize(10, 5);
                            dirtyIcon.show();
                        }
                        thePanel.fireEvent(Events.Change);
                    }
                });
            }
        };
        paintConfig();
    }

    public boolean isValid() {
        return FormUtils.isValid(actionFieldSet);
    }

    public boolean isDirty() {
        return FormUtils.isDirty(actionFieldSet);
    }

    public GwtConfigComponent getConfiguration() {
        return configComponent;
    }

    public GwtConfigComponent getUpdatedConfiguration() {

        List<Component> fields = actionFieldSet.getItems();
        for (Component component : fields) {
            if (component instanceof Field<?>) {

                Field<?> field = (Field<?>) component;
                String fieldName = field.getItemId();
                GwtConfigParameter param = configComponent.getParameter(fieldName);
                if (param == null) {
                    logger.severe(fieldName);
                } else {
                    if (!(field instanceof MultiField) || (field instanceof RadioGroup)) {
                        // get the updated values for the single field
                        String value = getUpdatedFieldConfiguration(param, field);
                        param.setValue(value);
                    } else {

                        // iterate over the subfields and extract each value
                        List<String> multiFieldValues = new ArrayList<String>();
                        MultiField<?> multiField = (MultiField<?>) field;
                        List<Field<?>> childFields = multiField.getAll();
                        for (Field<?> childField : childFields) {

                            String value = getUpdatedFieldConfiguration(param, childField);
                            if (value != null) {
                                multiFieldValues.add(value);
                            }
                        }
                        param.setValues(multiFieldValues.toArray(new String[]{ }));
                    }
                }
            }
        }
        return configComponent;
    }

    private String getUpdatedFieldConfiguration(GwtConfigParameter param, Field<?> field) {
        Map<String, String> options = param.getOptions();
        if (options != null && options.size() > 0) {
            @SuppressWarnings("unchecked")
            SimpleComboValue<String> scv = (SimpleComboValue<String>) field.getValue();
            String value = scv.getValue();
            Map<String, String> oMap = param.getOptions();
            return oMap.get(value);
        } else {
            switch (param.getType()) {
                case LONG:
                    NumberField longField = (NumberField) field;
                    Number longNumber = longField.getValue();
                    if (longNumber != null) {
                        return String.valueOf(longNumber.longValue());
                    } else {
                        return null;
                    }

                case DOUBLE:
                    NumberField doubleField = (NumberField) field;
                    Number doubleNumber = doubleField.getValue();
                    if (doubleNumber != null) {
                        return String.valueOf(doubleNumber.doubleValue());
                    } else {
                        return null;
                    }

                case FLOAT:
                    NumberField floatField = (NumberField) field;
                    Number floatNumber = floatField.getValue();
                    if (floatNumber != null) {
                        return String.valueOf(floatNumber.floatValue());
                    } else {
                        return null;
                    }

                case INTEGER:
                    NumberField integerField = (NumberField) field;
                    Number integerNumber = integerField.getValue();
                    if (integerNumber != null) {
                        return String.valueOf(integerNumber.intValue());
                    } else {
                        return null;
                    }

                case SHORT:
                    NumberField shortField = (NumberField) field;
                    Number shortNumber = shortField.getValue();
                    if (shortNumber != null) {
                        return String.valueOf(shortNumber.shortValue());
                    } else {
                        return null;
                    }

                case BYTE:
                    NumberField byteField = (NumberField) field;
                    Number byteNumber = byteField.getValue();
                    if (byteNumber != null) {
                        return String.valueOf(byteNumber.byteValue());
                    } else {
                        return null;
                    }

                case BOOLEAN:
                    RadioGroup radioGroup = (RadioGroup) field;
                    Radio radio = radioGroup.getValue();
                    return radio.getItemId();

                case PASSWORD:
                case CHAR:
                case STRING:
                    return (String) field.getValue();

                default:
                    break;
            }
        }
        return null;
    }

    private void paintConfig() {

        LayoutContainer lcAction = new LayoutContainer();
        lcAction.setLayout(new BorderLayout());
        lcAction.setBorders(true);
        lcAction.setSize(475, -1);
        add(lcAction);

        // center panel: action form
        BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER, .75F);
        centerData.setSplit(false);
        centerData.setMargins(new Margins(0, 0, 0, 0));

        FormData formData = new FormData("-20");
        formData.setMargins(new Margins(0, 10, 0, 0));

        if (!UserAgentUtils.isIE()) {
            actionFormPanel = new FormPanel();
            actionFormPanel.setFrame(false);
            actionFormPanel.setBodyBorder(false);
            actionFormPanel.setHeaderVisible(false);
            actionFormPanel.setLabelWidth(Constants.LABEL_WIDTH_CONFIG_FORM);
            actionFormPanel.setStyleAttribute("padding", "0px");
            actionFormPanel.setScrollMode(Scroll.AUTO);
            actionFormPanel.setLayout(new FlowLayout());
            actionFormPanel.addListener(Events.Render, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    NodeList<Element> nl = actionFormPanel.getElement().getElementsByTagName("form");
                    if (nl.getLength() > 0) {
                        Element elemForm = nl.getItem(0);
                        elemForm.setAttribute("autocomplete", "off");
                    }
                }
            });
        }

        actionFieldSet = new FieldSet();
        actionFieldSet.setId("configuration-form");
        actionFieldSet.setBorders(false);
        actionFieldSet.setStyleAttribute("padding", "0px");
        actionFieldSet.setScrollMode(Scroll.AUTO);
        if (configComponent.getComponentDescription() != null &&
                configComponent.getComponentDescription().trim().length() > 0) {
            actionFieldSet.addText(KapuaSafeHtmlUtils.htmlEscape(configComponent.getComponentDescription()));
        }

        FormLayout layout = new FormLayout();
        layout.setLabelWidth(Constants.LABEL_WIDTH_CONFIG_FORM);
        actionFieldSet.setLayout(layout);

        for (final GwtConfigParameter param : configComponent.getParameters()) {
            if (Boolean.parseBoolean(param.getOtherAttributes().get("hidden"))) {
                continue;
            }
            final Field<?> field;
            if (param.getCardinality() == 0 || param.getCardinality() == 1 || param.getCardinality() == -1) {
                field = paintConfigParameter(param);
            } else {
                field = paintMultiFieldConfigParameter(param);
            }
            String allowSelfEditValue = param.getOtherAttributes() != null ? param.getOtherAttributes().get("allowSelfEdit") : null;
            boolean allowSelfEdit = allowSelfEditValue != null && allowSelfEditValue.equals("true");
            boolean isEditingSelf = selectedAccountId == null || selectedAccountId.equals(currentSession.getSelectedAccountId());
            if (isEditingSelf && !allowSelfEdit) {
                field.disable();
            }
            actionFieldSet.add(field, formData);
        }

        if (!UserAgentUtils.isIE()) {
            actionFormPanel.add(actionFieldSet, formData);
            lcAction.add(actionFormPanel, centerData);
        } else {
            lcAction.add(actionFieldSet, centerData);
        }
    }

    private Field<?> paintMultiFieldConfigParameter(GwtConfigParameter param) {
        @SuppressWarnings("rawtypes")
        MultiField<?> multiField = new MultiField();
        multiField.setName(param.getId());
        multiField.setItemId(param.getId());
        multiField.setFieldLabel(param.getName());
        multiField.addPlugin(dirtyPlugin);
        multiField.setOrientation(Orientation.VERTICAL);
        if (param.isRequired()) {
            multiField.setFieldLabel("* " + param.getName());
        }

        applyDescription(param, multiField);

        if (param.getMin() != null && param.getMin().equals(param.getMax())) {
            multiField.setReadOnly(true);
            multiField.setEnabled(false);
        }

        Field<?> field;
        String value;
        String[] values = param.getValues();
        for (int i = 0; i < Math.min(param.getCardinality(), 10); i++) {

            // temporary set the param value to the current one in the array
            // use a value from the one passed in if we have it.
            value = null;
            if (values != null && i < values.length) {
                value = values[i];
            }
            param.setValue(value);
            field = paintConfigParameter(param);
            multiField.add(field);
        }

        // restore a null current value
        param.setValue(null);

        return multiField;
    }

    private Field<?> paintConfigParameter(GwtConfigParameter param) {
        Field<?> field;
        Map<String, String> options = param.getOptions();
        if (options != null && options.size() > 0) {
            field = paintChoiceActionParameter(param);
        } else {
            String minValue = param.getMin();
            String maxValue = param.getMax();
            switch (param.getType()) {
                case LONG:
                    field = paintNumberConfigParameter(param, new LongValidator(minValue, maxValue));
                    break;

                case DOUBLE:
                    field = paintNumberConfigParameter(param, new DoubleValidator(minValue, maxValue));
                    break;

                case FLOAT:
                    field = paintNumberConfigParameter(param, new FloatValidator(minValue, maxValue));
                    break;

                case INTEGER:
                    field = paintNumberConfigParameter(param, new IntegerValidator(minValue, maxValue));
                    break;

                case SHORT:
                    field = paintNumberConfigParameter(param, new ShortValidator(minValue, maxValue));
                    break;

                case BYTE:
                    field = paintNumberConfigParameter(param, new ByteValidator(minValue, maxValue));
                    break;

                case BOOLEAN:
                    field = paintBooleanConfigParameter(param);
                    break;

                case PASSWORD:
                    field = paintPasswordConfigParameter(param);
                    break;

                case CHAR:
                    field = paintTextConfigParameter(param, new CharValidator(minValue, maxValue));
                    break;

                default:
                case STRING:
                    field = paintTextConfigParameter(param, new StringValidator(minValue, maxValue));
                    break;
            }
        }

        field.setName(param.getId());
        field.setItemId(param.getId());
        return field;
    }

    private Field<?> paintTextConfigParameter(GwtConfigParameter param, Validator validator) {

        TextField<String> field = createTextFieldOrArea(param);
        field.setName(param.getId());
        field.setValue(param.getValue());
        field.setAllowBlank(true);
        field.setFieldLabel(param.getName());
        field.addPlugin(dirtyPlugin);

        if (param.isRequired()) {
            field.setAllowBlank(false);
            field.setFieldLabel("* " + param.getName());
        }

        applyDescription(param, field);

        if (param.getMin() != null && param.getMin().equals(param.getMax())) {
            field.setReadOnly(true);
            field.setEnabled(false);
        }

        if (param.getValue() != null) {
            field.setValue(param.getValue());
            field.setOriginalValue(param.getValue());
        }
        if (validator instanceof CharValidator) {
            field.setMaxLength(1);
            field.setValidator(validator);
        }
        if (validator instanceof StringValidator) {
            field.setValidator(validator);
        }
        return field;
    }

    private Field<?> paintPasswordConfigParameter(GwtConfigParameter param) {
        TextField<String> field = new TextField<String>();
        field.setName(param.getId());
        field.setValue(param.getValue());
        field.setAllowBlank(true);
        field.setPassword(true);
        field.setFieldLabel(param.getName());
        field.addPlugin(dirtyPlugin);
        if (param.isRequired()) {
            field.setAllowBlank(false);
            field.setFieldLabel("* " + param.getName());
        }

        applyDescription(param, field);

        if (param.getMin() != null && param.getMin().equals(param.getMax())) {
            field.setReadOnly(true);
            field.setEnabled(false);
        }

        if (param.getValue() != null) {
            field.setValue(param.getValue());
            field.setOriginalValue(param.getValue());
        }
        return field;
    }

    private Field<?> paintNumberConfigParameter(GwtConfigParameter param, Validator validator) {
        NumberField field = new NumberField();
        field.setName(param.getId());
        field.setAllowBlank(true);
        field.setFieldLabel(param.getName());
        field.addPlugin(dirtyPlugin);
        field.setMaxLength(255);
        if (validator != null) {
            field.setValidator(validator);
        }
        if (param.isRequired()) {
            field.setAllowBlank(false);
            field.setFieldLabel("* " + param.getName());
        }

        applyDescription(param, field);

        if (param.getMin() != null && param.getMin().equals(param.getMax())) {
            field.setReadOnly(true);
            field.setEnabled(false);
        }

        switch (param.getType()) {
            case LONG:
                field.setPropertyEditorType(Long.class);
                if (param.getValue() != null) {
                    field.setValue(Long.parseLong(param.getValue()));
                    field.setOriginalValue(Long.parseLong(param.getValue()));
                }
                break;
            case DOUBLE:
                field.setPropertyEditorType(Double.class);
                if (param.getValue() != null) {
                    field.setValue(Double.parseDouble(param.getValue()));
                    field.setOriginalValue(Double.parseDouble(param.getValue()));
                }
                break;
            case FLOAT:
                field.setPropertyEditorType(Float.class);
                if (param.getValue() != null) {
                    field.setValue(Float.parseFloat(param.getValue()));
                    field.setOriginalValue(Float.parseFloat(param.getValue()));
                }
                break;
            case SHORT:
                field.setPropertyEditorType(Short.class);
                if (param.getValue() != null) {
                    field.setValue(Short.parseShort(param.getValue()));
                    field.setOriginalValue(Short.parseShort(param.getValue()));
                }
                break;
            case BYTE:
                field.setPropertyEditor(new BytePropertyEditor());
                if (param.getValue() != null) {
                    field.setValue(Byte.parseByte(param.getValue()));
                    field.setOriginalValue(Byte.parseByte(param.getValue()));
                }
                break;
            default:
            case INTEGER:
                field.setPropertyEditorType(Integer.class);
                if (param.getValue() != null) {
                    field.setValue(Integer.parseInt(param.getValue()));
                    field.setOriginalValue(Integer.parseInt(param.getValue()));
                }
                break;
        }
        return field;
    }

    private Field<?> paintChoiceActionParameter(GwtConfigParameter param) {
        SimpleComboBox<String> field = new SimpleComboBox<String>();
        field.setName(param.getId());
        field.setEditable(false);
        field.setAllowBlank(true);
        field.setTriggerAction(TriggerAction.ALL);
        field.setFieldLabel(param.getName());
        field.setEditable(false);
        field.addPlugin(dirtyPlugin);
        if (param.isRequired()) {
            field.setAllowBlank(false);
            field.setFieldLabel("* " + param.getName());
        }

        applyDescription(param, field);

        if (param.getMin() != null && param.getMin().equals(param.getMax())) {
            field.setReadOnly(true);
            field.setEnabled(false);
        }

        Map<String, String> oMap = param.getOptions();
        for (String s : oMap.keySet()) {
            field.add(s);
        }

        if (param.getDefault() != null) {
            field.setSimpleValue(getKeyFromValue(oMap, param.getDefault()));
        }
        if (param.getValue() != null) {
            field.setSimpleValue(getKeyFromValue(oMap, param.getValue()));
        }
        return field;
    }

    private String getKeyFromValue(Map<String, String> m, String value) {
        String key = "";

        for (Map.Entry<String, String> es : m.entrySet()) {
            if (es.getValue().equals(value)) {
                key = es.getKey();
            }
        }
        return key;
    }

    private Field<?> paintBooleanConfigParameter(GwtConfigParameter param) {

        Radio radioTrue = new Radio();
        radioTrue.setBoxLabel(MSGS.trueLabel());
        radioTrue.setItemId("true");

        Radio radioFalse = new Radio();
        radioFalse.setBoxLabel(MSGS.falseLabel());
        radioFalse.setItemId("false");

        RadioGroup radioGroup = new RadioGroup();
        radioGroup.setName(param.getId());
        radioGroup.setItemId(param.getId());
        radioGroup.setFieldLabel(param.getName());
        radioGroup.add(radioTrue);
        radioGroup.add(radioFalse);
        if (param.isRequired()) {
            radioGroup.setFieldLabel("* " + param.getName());
        }

        radioGroup.addPlugin(dirtyPlugin);

        applyDescription(param, radioGroup);

        boolean bool = Boolean.parseBoolean(param.getValue());
        if (bool) {
            radioTrue.setValue(true);
            radioGroup.setOriginalValue(radioTrue);
        } else {
            radioFalse.setValue(true);
            radioGroup.setOriginalValue(radioFalse);
        }

        return radioGroup;
    }

    /**
     * Apply the description of a parameter to a field
     *
     * @param param the parameter to take the description from
     * @param field the field to apply the description to
     */
    private void applyDescription(GwtConfigParameter param, Field<?> field) {
        String description = extractDescription(param);
        if (description != null && !description.isEmpty()) {
            field.addPlugin(infoPlugin);
            field.setData("text", description);
        }
    }

    /**
     * Split up the description string into description and type
     * <br>
     * This method returns either one or two elements, depending
     * if there is a field type attached to the description or not.
     * The first element will always contain the description. If
     * available the second type will contain the field type. If
     * the description only consists of a field type, then the first
     * field will be an empty string.
     *
     * @param param the parameter to take the description from
     * @return {@code null} when the parameter or description of
     * the parameter is {@code null}, otherwise an array containing
     * description and field type
     */
    private static String[] splitDescription(GwtConfigParameter param) {
        if (param == null || param.getDescription() == null) {
            return new String[0];
        }
        String description = param.getDescription();
        int idx = description.lastIndexOf('|');
        if (idx < 0) {
            return new String[]{ description };
        }
        if (idx < 1) {
            return new String[]{ "", description.substring(idx + 1) };
        }
        return new String[]{ description.substring(0, idx), description.substring(idx + 1) };
    }

    /**
     * Extract the description from the configuration parameter
     * <br>
     * This cuts of any field type definition at the end of the
     * description
     *
     * @param param the parameter to take the description from
     * @return only the description information to two to the user
     */
    private static String extractDescription(GwtConfigParameter param) {
        String[] desc = splitDescription(param);
        if (desc.length == 0) {
            return null;
        }
        return desc[0];
    }

    /**
     * Create either a text field or text area
     * <br>
     * This method will inspect the field type inside the description
     * and create the correct text field for this configuration
     * parameter.
     *
     * @param param the parameter to create a field for
     * @return a new field, never returns {@code null}
     */
    private static TextField<String> createTextFieldOrArea(GwtConfigParameter param) {
        String[] desc = splitDescription(param);
        if (desc.length > 1 && desc[1].equals("TextArea")) {
            return new TextArea();
        }
        return new TextField<String>();
    }

    private static class IntegerValidator implements Validator {

        private Integer minValue;
        private Integer maxValue;

        public IntegerValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = Integer.valueOf(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = Integer.valueOf(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            int intValue;
            try {
                intValue = Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                return MSGS.numberTooLargeErrorMessage();
            }
            if (minValue != null && intValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }
            if (maxValue != null && intValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class LongValidator implements Validator {

        private Long minValue;
        private Long maxValue;

        public LongValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = Long.valueOf(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = Long.valueOf(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            long longValue;
            try {
                longValue = Long.parseLong(value);
            } catch (NumberFormatException nfe) {
                return MSGS.numberTooLargeErrorMessage();
            }
            if (minValue != null && longValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }

            if (maxValue != null && longValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class DoubleValidator implements Validator {

        private Double minValue;
        private Double maxValue;

        public DoubleValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = Double.valueOf(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = Double.valueOf(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            double doubleValue;
            try {
                doubleValue = Double.parseDouble(value);
            } catch (NumberFormatException nfe) {
                return nfe.getMessage();
            }
            if (minValue != null && doubleValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }

            if (maxValue != null && doubleValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class FloatValidator implements Validator {

        private Float minValue;
        private Float maxValue;

        public FloatValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = Float.valueOf(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = Float.valueOf(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            float floatValue;
            try {
                floatValue = Float.parseFloat(value);
            } catch (NumberFormatException nfe) {
                return nfe.getMessage();
            }
            if (minValue != null && floatValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }
            if (maxValue != null && floatValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class ShortValidator implements Validator {

        private Short minValue;
        private Short maxValue;

        public ShortValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = Short.valueOf(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = Short.valueOf(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            short shortValue;
            try {
                shortValue = Short.parseShort(value);
            } catch (NumberFormatException nfe) {
                return nfe.getMessage();
            }
            if (minValue != null && shortValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }

            if (maxValue != null && shortValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class ByteValidator implements Validator {

        private Byte minValue;
        private Byte maxValue;

        public ByteValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = Byte.valueOf(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = Byte.valueOf(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            byte byteValue;
            try {
                byteValue = Byte.parseByte(value);
            } catch (NumberFormatException nfe) {
                return nfe.getMessage();
            }
            if (minValue != null && byteValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }

            if (maxValue != null && byteValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class CharValidator implements Validator {

        private Character minValue;
        private Character maxValue;

        public CharValidator(String minValue, String maxValue) {
            this.minValue = null;
            if (minValue != null) {
                try {
                    this.minValue = minValue.charAt(0);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            this.maxValue = null;
            if (maxValue != null) {
                try {
                    this.maxValue = maxValue.charAt(0);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            char charValue;
            try {
                charValue = value.charAt(0);
            } catch (NumberFormatException nfe) {
                return nfe.getMessage();
            }
            if (minValue != null && charValue < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }
            if (maxValue != null && charValue > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, maxValue);
            }
            return null;
        }
    }

    private static class StringValidator implements Validator {

        private int minValue;
        private int maxValue = 255;

        public StringValidator(String minValue, String maxValue) {
            if (minValue != null) {
                try {
                    this.minValue = Integer.parseInt(minValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
            if (maxValue != null) {
                try {
                    this.maxValue = Integer.parseInt(maxValue);
                } catch (NumberFormatException nfe) {
                    FailureHandler.handle(nfe);
                }
            }
        }

        @Override
        public String validate(Field<?> field, String value) {
            if (value.length() > maxValue) {
                return MessageUtils.get(CONFIG_MAX_VALUE, (maxValue + 1));
            }
            if (value.length() < minValue) {
                return MessageUtils.get(CONFIG_MIN_VALUE, minValue);
            }
            return null;
        }

    }

    private static class BytePropertyEditor extends NumberPropertyEditor {

        @Override
        public String getStringValue(Number value) {
            return value.toString();
        }

        @Override
        public Number convertStringValue(String value) {
            return Byte.valueOf(value);
        }
    }
}
