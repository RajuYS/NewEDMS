//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.05 at 07:14:16 AM IST 
//


package com.lmh.search_ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="repositoryName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="c_account_no" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="c_mobile_no" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="c_customer_name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="c_bill_date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "repositoryName",
    "cAccountNo",
    "cMobileNo",
    "cCustomerName",
    "cBillDate"
})
@XmlRootElement(name = "getBillsListRequest")
public class GetBillsListRequest {

    @XmlElement(required = true)
    protected String repositoryName;
    @XmlElement(name = "c_account_no", required = true)
    protected String cAccountNo;
    @XmlElement(name = "c_mobile_no", required = true)
    protected String cMobileNo;
    @XmlElement(name = "c_customer_name", required = true)
    protected String cCustomerName;
    @XmlElement(name = "c_bill_date", required = true)
    protected String cBillDate;

    /**
     * Gets the value of the repositoryName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * Sets the value of the repositoryName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepositoryName(String value) {
        this.repositoryName = value;
    }

    /**
     * Gets the value of the cAccountNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCAccountNo() {
        return cAccountNo;
    }

    /**
     * Sets the value of the cAccountNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCAccountNo(String value) {
        this.cAccountNo = value;
    }

    /**
     * Gets the value of the cMobileNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCMobileNo() {
        return cMobileNo;
    }

    /**
     * Sets the value of the cMobileNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCMobileNo(String value) {
        this.cMobileNo = value;
    }

    /**
     * Gets the value of the cCustomerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCCustomerName() {
        return cCustomerName;
    }

    /**
     * Sets the value of the cCustomerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCCustomerName(String value) {
        this.cCustomerName = value;
    }

    /**
     * Gets the value of the cBillDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCBillDate() {
        return cBillDate;
    }

    /**
     * Sets the value of the cBillDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCBillDate(String value) {
        this.cBillDate = value;
    }

}
