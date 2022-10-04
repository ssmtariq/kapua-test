/*******************************************************************************
 * Copyright (c) 2018, 2022 Eurotech and/or its affiliates and others
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
package org.eclipse.kapua.service.certificate.info;

import org.eclipse.kapua.model.KapuaNamedEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.id.KapuaIdAdapter;
import org.eclipse.kapua.model.xml.BinaryXmlAdapter;
import org.eclipse.kapua.model.xml.DateXmlAdapter;
import org.eclipse.kapua.service.certificate.CertificateStatus;
import org.eclipse.kapua.service.certificate.CertificateUsage;
import org.eclipse.kapua.service.certificate.KeyUsageSetting;
import org.eclipse.kapua.service.certificate.info.xml.CertificateInfoXmlRegistry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.Set;

/**
 * @since 1.1.0
 */
@XmlRootElement(name = "certificateInfo")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(factoryClass = CertificateInfoXmlRegistry.class, factoryMethod = "newCertificateInfo")
public interface CertificateInfo extends KapuaNamedEntity {

    String TYPE = "CertificateInfo";

    @Override
    default String getType() {
        return TYPE;
    }

    @XmlElement(name = "certificate")
    String getCertificate();

    void setCertificate(String certificate);

    @XmlElement(name = "version")
    Integer getVersion();

    void setVersion(Integer version);

    @XmlElement(name = "serial")
    String getSerial();

    void setSerial(String serial);

    /**
     * Gets the {@link #getSerial()} in hexadecimal format.
     *
     * @return The {@link #getSerial()} in hexadecimal format.
     * @since 1.5.0
     */
    @XmlElement(name = "serialHex")
    String getSerialHex();

    @XmlElement(name = "algorithm")
    String getAlgorithm();

    void setAlgorithm(String algorithm);

    @XmlElement(name = "signature")
    @XmlJavaTypeAdapter(BinaryXmlAdapter.class)
    byte[] getSignature();

    void setSignature(byte[] signature);

    @XmlElement(name = "subject")
    String getSubject();

    void setSubject(String subject);

    @XmlElement(name = "issuer")
    String getIssuer();

    void setIssuer(String issuer);

    @XmlElement(name = "notBefore")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotBefore();

    void setNotBefore(Date notBefore);

    @XmlElement(name = "notAfter")
    @XmlJavaTypeAdapter(DateXmlAdapter.class)
    Date getNotAfter();

    void setNotAfter(Date notAfter);

    @XmlElement(name = "status")
    CertificateStatus getStatus();

    void setStatus(CertificateStatus status);

    @XmlElement(name = "ca")
    Boolean getCa();

    void setCa(Boolean isCa);

    @XmlElement(name = "caId")
    @XmlJavaTypeAdapter(KapuaIdAdapter.class)
    KapuaId getCaId();

    void setCaId(KapuaId caId);

    @XmlElementWrapper(name = "keyUsageSettings")
    @XmlElement(name = "keyUsageSetting")
    <K extends KeyUsageSetting> Set<K> getKeyUsageSettings();

    void setKeyUsageSettings(Set<KeyUsageSetting> keyUsages);

    void addKeyUsageSetting(KeyUsageSetting keyUsage);

    void removeKeyUsageSetting(KeyUsageSetting keyUsage);

    @XmlElementWrapper(name = "certificateUsages")
    @XmlElement(name = "certificateUsage")
    <C extends CertificateUsage> Set<C> getCertificateUsages();

    void setCertificateUsages(Set<CertificateUsage> certificateUsages);

    void addCertificateUsage(CertificateUsage certificateUsage);

    void removeCertificateUsage(CertificateUsage certificateUsage);

    @XmlElement(name = "forwardable")
    Boolean getForwardable();

    void setForwardable(Boolean forwardable);
}
