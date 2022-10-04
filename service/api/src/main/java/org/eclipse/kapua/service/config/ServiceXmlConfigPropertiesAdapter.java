/*******************************************************************************
 * Copyright (c) 2019, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.config;

import org.eclipse.kapua.service.config.ServiceXmlConfigPropertyAdapted.ConfigPropertyType;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Xml configuration properties adapter. It marshal and unmarshal configuration properties in a proper way.
 *
 * @since 1.0
 */
public class ServiceXmlConfigPropertiesAdapter extends XmlAdapter<ServiceXmlConfigPropertiesAdapted, Map<String, Object>> {

    @Override
    public ServiceXmlConfigPropertiesAdapted marshal(Map<String, Object> props) {
        List<ServiceXmlConfigPropertyAdapted> adaptedValues = new ArrayList<>();

        if (props != null) {
            props.forEach((name, value) -> {

                ServiceXmlConfigPropertyAdapted adaptedValue = new ServiceXmlConfigPropertyAdapted();
                adaptedValue.setName(name);

                if (value instanceof String) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.stringType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Long) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.longType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Double) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.doubleType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Float) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.floatType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Integer) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.integerType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Byte) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.byteType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Character) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.charType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Boolean) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.booleanType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof Short) {
                    adaptedValue.setArray(false);
                    adaptedValue.setType(ConfigPropertyType.shortType);
                    adaptedValue.setValues(new String[] { value.toString() });
                } else if (value instanceof String[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.stringType);
                    adaptedValue.setValues((String[]) value);
                } else if (value instanceof Long[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.longType);
                    Long[] nativeValues = (Long[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Double[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.doubleType);
                    Double[] nativeValues = (Double[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Float[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.floatType);
                    Float[] nativeValues = (Float[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Integer[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.integerType);
                    Integer[] nativeValues = (Integer[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Byte[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.byteType);
                    Byte[] nativeValues = (Byte[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Character[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.charType);
                    Character[] nativeValues = (Character[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Boolean[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.booleanType);
                    Boolean[] nativeValues = (Boolean[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                } else if (value instanceof Short[]) {
                    adaptedValue.setArray(true);
                    adaptedValue.setType(ConfigPropertyType.shortType);
                    Short[] nativeValues = (Short[]) value;
                    String[] stringValues = new String[nativeValues.length];
                    for (int i = 0; i < nativeValues.length; i++) {
                        if (nativeValues[i] != null) {
                            stringValues[i] = nativeValues[i].toString();
                        }
                    }
                    adaptedValue.setValues(stringValues);
                }

                adaptedValues.add(adaptedValue);
            });
        }

        ServiceXmlConfigPropertiesAdapted result = new ServiceXmlConfigPropertiesAdapted();
        result.setProperties(adaptedValues.toArray(new ServiceXmlConfigPropertyAdapted[] {}));
        return result;
    }

    @Override
    public Map<String, Object> unmarshal(ServiceXmlConfigPropertiesAdapted adaptedPropsAdapted) {
        ServiceXmlConfigPropertyAdapted[] adaptedProps = adaptedPropsAdapted.getProperties();
        if (adaptedProps == null) {
            return new HashMap<>();
        }

        Map<String, Object> properties = new HashMap<>();
        for (ServiceXmlConfigPropertyAdapted adaptedProp : adaptedProps) {
            String propName = adaptedProp.getName();
            ConfigPropertyType type = adaptedProp.getType();
            if (type != null) {
                Object propValue = null;
                if (!adaptedProp.getArray()) {
                    switch (adaptedProp.getType()) {
                    case stringType:
                        propValue = adaptedProp.getValues()[0];
                        break;
                    case longType:
                        propValue = Long.parseLong(adaptedProp.getValues()[0]);
                        break;
                    case doubleType:
                        propValue = Double.parseDouble(adaptedProp.getValues()[0]);
                        break;
                    case floatType:
                        propValue = Float.parseFloat(adaptedProp.getValues()[0]);
                        break;
                    case integerType:
                        propValue = Integer.parseInt(adaptedProp.getValues()[0]);
                        break;
                    case byteType:
                        propValue = Byte.parseByte(adaptedProp.getValues()[0]);
                        break;
                    case charType:
                        String s = adaptedProp.getValues()[0];
                        propValue = s.charAt(0);
                        break;
                    case booleanType:
                        propValue = Boolean.parseBoolean(adaptedProp.getValues()[0]);
                        break;
                    case shortType:
                        propValue = Short.parseShort(adaptedProp.getValues()[0]);
                        break;
                    }
                } else {
                    switch (adaptedProp.getType()) {
                    case stringType:
                        propValue = adaptedProp.getValues();
                        break;
                    case longType:
                        Long[] longValues = new Long[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                longValues[i] = Long.parseLong(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = longValues;
                        break;
                    case doubleType:
                        Double[] doubleValues = new Double[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                doubleValues[i] = Double.parseDouble(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = doubleValues;
                        break;
                    case floatType:
                        Float[] floatValues = new Float[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                floatValues[i] = Float.parseFloat(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = floatValues;
                        break;
                    case integerType:
                        Integer[] intValues = new Integer[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                intValues[i] = Integer.parseInt(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = intValues;
                        break;
                    case byteType:
                        Byte[] byteValues = new Byte[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                byteValues[i] = Byte.parseByte(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = byteValues;
                        break;
                    case charType:
                        Character[] charValues = new Character[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                String s = adaptedProp.getValues()[i];
                                charValues[i] = s.charAt(0);
                            }
                        }
                        propValue = charValues;
                        break;
                    case booleanType:
                        Boolean[] booleanValues = new Boolean[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                booleanValues[i] = Boolean.parseBoolean(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = booleanValues;
                        break;
                    case shortType:
                        Short[] shortValues = new Short[adaptedProp.getValues().length];
                        for (int i = 0; i < adaptedProp.getValues().length; i++) {
                            if (adaptedProp.getValues()[i] != null) {
                                shortValues[i] = Short.parseShort(adaptedProp.getValues()[i]);
                            }
                        }
                        propValue = shortValues;
                        break;
                    }
                }
                properties.put(propName, propValue);
            }
        }
        return properties;
    }
}
