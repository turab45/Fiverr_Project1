package project.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sun.java2d.pipe.SpanShapeRenderer;

import java.sql.Date;

public class Customer {

    private int customerID;
    private int active;
    private Date createDate;
    private Date lastUpdate;
    private StringProperty customerName;
    private String createdBy;
    private StringProperty address;
    private StringProperty address2;
    private StringProperty city;
    private StringProperty phone;
    private StringProperty postalCode;
    private StringProperty country;
    private String lastUpdateBy;
    private int addressId;

    //Default Constructor
    public Customer(){

    }

    //Main Constructor
    public Customer(int customerID, int addressId, int active, Date createDate, Date lastUpdate, String customerName, String createdBy, String address, String address2, String city, String phone, String postalCode, String country, String lastUpdateBy){
        this.addressId = addressId;
        this.customerID = customerID;
        this.active = active;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
        this.customerName = new SimpleStringProperty(customerName);
        this.createdBy = createdBy;
        this.address = new SimpleStringProperty(address);
        this.address2 = new SimpleStringProperty(address2);
        this.city = new SimpleStringProperty(city);
        this.phone = new SimpleStringProperty(phone);
        this.postalCode = new SimpleStringProperty(postalCode);
        this.country = new SimpleStringProperty(country);
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getAddressId(){ return addressId;}
    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public StringProperty getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = new SimpleStringProperty(customerName);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public StringProperty getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = new SimpleStringProperty(address);
    }

    public StringProperty getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = new SimpleStringProperty(address2);
    }

    public StringProperty getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = new SimpleStringProperty(city);
    }

    public StringProperty getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = new SimpleStringProperty(phone);
    }

    public StringProperty getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = new SimpleStringProperty(postalCode);
    }

    public StringProperty getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = new SimpleStringProperty(country);
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

}
