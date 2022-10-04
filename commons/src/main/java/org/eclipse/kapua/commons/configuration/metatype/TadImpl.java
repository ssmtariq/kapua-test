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
package org.eclipse.kapua.commons.configuration.metatype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.kapua.model.config.metatype.KapuaTad;
import org.eclipse.kapua.model.config.metatype.KapuaToption;
import org.eclipse.kapua.model.config.metatype.KapuaTscalar;
import org.w3c.dom.Element;

/**
 * <p>
 * Java class for Tad complex type.
 * <p>
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 *
 * <pre>
 * &lt;complexType name="Tad"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Option" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Toption" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="type" use="required" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tscalar" /&gt;
 *       &lt;attribute name="cardinality" type="{http://www.w3.org/2001/XMLSchema}int" default="0" /&gt;
 *       &lt;attribute name="min" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="max" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="default" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="required" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @since 1.0
 */

public class TadImpl implements KapuaTad {

    protected List<ToptionImpl> option;
    protected List<Object> any;
    protected String name;
    protected String description;
    protected String id;
    protected TscalarImpl type;
    protected Integer cardinality;
    protected String min;
    protected String max;
    protected String defaultValue;
    protected Boolean required;
    private Map<QName, String> otherAttributes = new HashMap<>();

    /**
     * Gets the value of the option property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the option property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <p>
     *
     * <pre>
     * getOption().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ToptionImpl }
     */
    public List<KapuaToption> getOption() {
        if (option == null) {
            option = new ArrayList<>();
        }
        return new ArrayList<>(this.option);
    }

    /**
     * Add an option to the internal list
     *
     * @param option
     */
    public void addOption(KapuaToption option) {
        if (this.option == null) {
            this.option = new ArrayList<>();
        }

        this.option.add((ToptionImpl) option);
    }

    @Override
    public void setOption(List<KapuaToption> option) {
        this.option = new ArrayList<>();
        for (KapuaToption singleOption : option) {
            this.option.add((ToptionImpl) singleOption);
        }
    }

    /**
     * Gets the value of the any property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the any property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <p>
     *
     * <pre>
     * getAny().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Element }
     * {@link Object }
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

    @Override
    public void setAny(List<Object> any) {
        this.any = any;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is
     *         {@link TscalarImpl }
     */
    public TscalarImpl getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *            allowed object is
     *            {@link TscalarImpl }
     */
    public void setType(KapuaTscalar value) {
        this.type = (TscalarImpl) value;
    }

    /**
     * Gets the value of the cardinality property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public Integer getCardinality() {
        if (cardinality == null) {
            return 0;
        } else {
            return cardinality;
        }
    }

    /**
     * Sets the value of the cardinality property.
     *
     * @param value
     *            allowed object is
     *            {@link Integer }
     */
    public void setCardinality(Integer value) {
        this.cardinality = value;
    }

    /**
     * Gets the value of the min property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMin() {
        return min;
    }

    /**
     * Sets the value of the min property.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setMin(String value) {
        this.min = value;
    }

    /**
     * Gets the value of the max property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getMax() {
        return max;
    }

    /**
     * Sets the value of the max property.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setMax(String value) {
        this.max = value;
    }

    /**
     * Gets the value of the default property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getDefault() {
        return defaultValue;
    }

    /**
     * Sets the value of the default property.
     *
     * @param value
     *            allowed object is
     *            {@link String }
     */
    public void setDefault(String value) {
        this.defaultValue = value;
    }

    /**
     * Gets the value of the required property.
     *
     * @return possible object is
     *         {@link Boolean }
     */
    public Boolean isRequired() {
        if (required == null) {
            return true;
        } else {
            return required;
        }
    }

    /**
     * Sets the value of the required property.
     *
     * @param value
     *            allowed object is
     *            {@link Boolean }
     */
    public void setRequired(Boolean value) {
        this.required = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * <p>
     * <p>
     * the map is keyed by the name of the attribute and
     * the value is the string value of the attribute.
     * <p>
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }

    public void putOtherAttribute(QName key, String value) {
        getOtherAttributes().put(key,
                value);
    }
}
