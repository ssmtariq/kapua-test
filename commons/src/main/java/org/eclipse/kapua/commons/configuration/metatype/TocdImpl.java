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
import org.eclipse.kapua.model.config.metatype.KapuaTicon;
import org.eclipse.kapua.model.config.metatype.KapuaTocd;
import org.w3c.dom.Element;

/**
 * <p>
 * Java class for Tocd complex type.
 * <p>
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType name="Tocd"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="AD" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Tad" maxOccurs="unbounded"/&gt;
 *         &lt;element name="Icon" type="{http://www.osgi.org/xmlns/metatype/v1.2.0}Ticon" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;anyAttribute/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @since 1.0
 */
public class TocdImpl implements KapuaTocd {

    protected List<KapuaTad> ad;
    protected List<KapuaTicon> icon;
    protected List<Object> any;
    protected String name;
    protected String description;
    protected String id;
    private Map<QName, String> otherAttributes = new HashMap<>();

    /**
     * Gets the value of the ad property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ad property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <p>
     * <pre>
     * getAD().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TadImpl }
     */
    public List<KapuaTad> getAD() {
        if (ad == null) {
            ad = new ArrayList<>();
        }
        return this.ad;
    }

    @Override
    public void setAD(List<KapuaTad> ad) {
        this.ad = ad;
    }

    /**
     * Add Tad to the internal list
     *
     * @param ad
     */
    public void addAD(KapuaTad ad) {
        if (this.ad == null) {
            this.ad = new ArrayList<>();
        }

        this.ad.add(ad);
    }

    /**
     * Gets the value of the icon property.
     * <p>
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the icon property.
     * <p>
     * <p>
     * For example, to add a new item, do as follows:
     * <p>
     * <pre>
     * getIcon().add(newItem);
     * </pre>
     * <p>
     * <p>
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TiconImpl }
     */
    public List<KapuaTicon> getIcon() {
        if (icon == null) {
            icon = new ArrayList<>();
        }
        return this.icon;
    }

    @Override
    public void setIcon(List<KapuaTicon> icon) {
        this.icon = icon;
    }

    /**
     * Add icon to the internal list
     *
     * @param icon
     */
    public void addIcon(KapuaTicon icon) {
        if (this.icon == null) {
            this.icon = new ArrayList<>();
        }
        this.icon.add((TiconImpl) icon);
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
            any = new ArrayList<>();
        }
        return this.any;
    }

    @Override
    public void setAny(List<Object> any) {
        this.any = any;
    }

    /**
     * Add a generic object to the internal list
     *
     * @param object
     */
    public void addAny(Object object) {
        getAny().add(object);
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setId(String value) {
        this.id = value;
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

    /**
     * Insert a generic value for the provided QName
     *
     * @param key
     * @param value
     */
    public void putOtherAttribute(QName key, String value) {
        getOtherAttributes().put(key,
                value);
    }

    @Override
    public void setOtherAttributes(Map<QName, String> otherAttributes) {
        this.otherAttributes = otherAttributes;
    }

}
