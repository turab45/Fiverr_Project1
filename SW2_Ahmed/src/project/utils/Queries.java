package project.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import project.model.*;

import java.sql.*;

public class Queries {

    public static City getCity(String cityName) throws Exception {
        //int cityId, String city, int countryId, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy
        Statement loginStatement = DBConnector.makeConnection().createStatement();
        String query = "SELECT * FROM city WHERE city = '" + cityName +"'";
        ResultSet result = loginStatement.executeQuery(query);
        while(result.next()){
            City c = new City(result.getInt("cityId"), result.getString("city"), result.getInt("countryId"), result.getDate("createDate"), result.getString("createdBy"), result.getDate("lastUpdate"), result.getString("lastUpdateBy"));
            return c;
        }
        return null;
    }

    public static Country getCountry(String countryName) throws Exception {
        //int countryID, String country, Date createDate, String createdBy, Date lastUpdate, String lastUpdatedBy
        Statement loginStatement = DBConnector.makeConnection().createStatement();
        String query = "SELECT * FROM country WHERE country = '" + countryName +"'";
        ResultSet result = loginStatement.executeQuery(query);
        while(result.next()){
            Country c = new Country(result.getInt("countryId"), result.getString("country"), result.getDate("createDate"), result.getString("createdBy"), result.getDate("lastUpdate"), result.getString("lastUpdateBy"));
            return c;
        }
        return null;
    }

    public static int insertCountry(String country) throws Exception {
        int newCountryId = -1;
        //Insert new country into DB
        PreparedStatement ps = DBConnector.makeConnection().prepareStatement("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, country);
        ps.setString(2, User.getUsername());
        ps.setString(3, User.getUsername());
        int result = ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            newCountryId = rs.getInt(1);
        }
        return newCountryId;
    }

    public static int insertCity(String city, int countryId) throws Exception {
        //int cityId, String city, int countryId, Date createDate, String createdBy, Date lastUpdate, String lastUpdateBy
        int newCityId = -1;
        //Insert new city into DB
        PreparedStatement ps = DBConnector.makeConnection().prepareStatement("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, city);
        ps.setInt(2, countryId);
        ps.setString(3, User.getUsername());
        ps.setString(4, User.getUsername());
        int result = ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            newCityId = rs.getInt(1);
        }
        return newCityId;
    }

    public static int insertAddress(int cityId, String address, String address2, String postalCode, String phone) throws Exception {
        //int addressID, String address, String address2, int cityID, Date createDate, String createdBy, java.sql.Date lastUpdate, String lastUpdatedBy, String postalCode, String phone) {
        int newAddressId = -1;
        //Insert new city into DB
        PreparedStatement ps = DBConnector.makeConnection().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createdBy, createDate, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, address);
        ps.setString(2, address2);
        ps.setInt(3, cityId);
        ps.setString(4, postalCode);
        ps.setString(5, phone);
        ps.setString(6, User.getUsername());
        ps.setString(7, User.getUsername());
        int result = ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            newAddressId = rs.getInt(1);
        }
        return newAddressId;
    }

    public static Customer insertCustomer(String customerName, int addressId) throws Exception {
        //int addressID, String address, String address2, int cityID, Date createDate, String createdBy, java.sql.Date lastUpdate, String lastUpdatedBy, String postalCode, String phone) {
        int newCustomerId = -1;
        //Insert new city into DB
        PreparedStatement ps = DBConnector.makeConnection().prepareStatement("INSERT INTO customer (customerName, addressId, active, createdBy, createDate, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, customerName);
        ps.setInt(2, addressId);
        ps.setInt(3, 1);
        ps.setString(4, User.getUsername());
        ps.setString(5, User.getUsername());
        int result = ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            newCustomerId = rs.getInt(1);
        }
        if(newCustomerId == -1) return null;
        //Get Customer
        Statement customerStatement = DBConnector.makeConnection().createStatement();
        String query = "SELECT * FROM customer " +
                "INNER JOIN address ON address.addressId = customer.addressId " +
                "INNER JOIN city ON city.cityId = address.cityId " +
                "INNER JOIN country ON country.countryId = city.countryId " +
                "WHERE customer.customerId = " + newCustomerId;
        ResultSet rss = customerStatement.executeQuery(query);
        while(rss.next()){
            Customer c = new Customer(rss.getInt("customer.customerId"), rss.getInt("customer.addressId"), rss.getInt("customer.active"), rss.getDate("customer.createDate"), rss.getDate("customer.lastUpdate"), rss.getString("customer.customerName"), rss.getString("customer.createdBy"), rss.getString("address.address"), rss.getString("address.address2"), rss.getString("city.city"), rss.getString("address.phone"), rss.getString("address.postalCode"), rss.getString("country.country"), rss.getString("customer.lastUpdateBy"));
            return c;
        }
        return null;
    }

    public static boolean deleteCustomer(Customer customer){
        try {
            PreparedStatement ps = DBConnector.makeConnection().prepareStatement("DELETE FROM customer WHERE customerId = " + customer.getCustomerID());
            int result = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Delete Customer SQL statement contains an error!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<Appointment> getAppointments() throws Exception {
        //String startTime, String endTime, String customerName, Date createDate, String createdBy, Date lastUpdate, String lastUpdatedBy) {
        ObservableList<Appointment> apps = FXCollections.observableArrayList();
        Statement customerStatement = DBConnector.makeConnection().createStatement();
        String query = "SELECT * FROM appointment INNER JOIN customer ON appointment.customerId = customer.customerId WHERE appointment.userId = " + User.getUserID();
        ResultSet r = customerStatement.executeQuery(query);
        while(r.next()){
            Appointment a = new Appointment();
            a.setAppointmentID(r.getInt("appointment.appointmentId"));
            a.setCustomerID(r.getInt("appointment.customerId"));
            a.setCustomerName(r.getString("customer.customerName"));
            a.setUserID(r.getInt("appointment.userId"));
            a.setContact(r.getString("appointment.contact"));
            a.setType(r.getString("appointment.type"));
            a.setUrl(r.getString("appointment.url"));
            a.setTitle(r.getString("appointment.title"));
            a.setDescription(r.getString("appointment.description"));
            a.setLocation(r.getString("appointment.location"));
            a.setStartTime(r.getTimestamp("appointment.start"));
            a.setEndTime(r.getTimestamp("appointment.end"));
            a.setCreateDate(r.getDate("appointment.createDate"));
            a.setCreatedBy(r.getString("appointment.createdBy"));
            a.setLastUpdate(r.getDate("appointment.lastUpdate"));
            a.setLastUpdatedBy(r.getString("appointment.lastUpdateBy"));
            apps.add(a);
        }
        return apps;
    }

    public static int insertAppointment(int customerId, int userId, String title, String description, String location, String contact, String type, String url, Timestamp start, Timestamp end) throws Exception {
        //String startTime, String endTime, String customerName, Date createDate, String createdBy, Date lastUpdate, String lastUpdatedBy) {
        int newAppointmentId = -1;
        //Insert new city into DB
        PreparedStatement ps = DBConnector.makeConnection().prepareStatement("INSERT INTO appointment (customerId, userId, title, description, location, contact, type, url, start, end, createdBy, createDate, lastUpdate, lastUpdateBy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, customerId);
        ps.setInt(2, userId);
        ps.setString(3, title);
        ps.setString(4, description);
        ps.setString(5, location);
        ps.setString(6, contact);
        ps.setString(7, type);
        ps.setString(8, url);
        ps.setTimestamp(9, start);
        ps.setTimestamp(10, end);
        ps.setString(11, User.getUsername());
        ps.setString(12, User.getUsername());
        int result = ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
            newAppointmentId = rs.getInt(1);
        }
        return newAppointmentId;
    }


    public static boolean deleteAppointment(Appointment selectedAppointment) {
        try {
            PreparedStatement ps = DBConnector.makeConnection().prepareStatement("DELETE FROM appointment WHERE appointmentId = " + selectedAppointment.getAppointmentID());
            int result = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Delete Appointment SQL statement contains an error!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
