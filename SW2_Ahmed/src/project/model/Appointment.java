package project.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Appointment {
    private int appointmentID;
    private IntegerProperty customerID;
    private int userID;
    private StringProperty contact;
    private StringProperty type;
    private StringProperty url;
    private StringProperty title;
    private StringProperty description;
    private StringProperty location;
    private ObjectProperty<Timestamp> startTime;
    private ObjectProperty<Timestamp> endTime;
    private String customerName;
    private Date createDate;
    private String createdBy;
    private Date lastUpdate;
    private String lastUpdatedBy;

    //Default Constructor
    public Appointment(){

    }



    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public IntegerProperty getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = new SimpleIntegerProperty(customerID);
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }


    public StringProperty getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = new SimpleStringProperty(contact);
    }

    public StringProperty getType() {
        return type;
    }

    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }

    public StringProperty getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = new SimpleStringProperty(url);
    }

    public StringProperty getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    public StringProperty getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public StringProperty getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = new SimpleStringProperty(location);
    }

    public ObjectProperty<Timestamp> getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = new SimpleObjectProperty<>(startTime);
    }

    public ObjectProperty<Timestamp> getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = new SimpleObjectProperty<>(endTime);;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }
}
