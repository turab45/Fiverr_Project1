package project.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.model.Address;
import project.model.City;
import project.model.Country;
import project.model.Customer;
import project.utils.DBConnector;
import project.utils.Queries;

import java.sql.ResultSet;
import java.sql.Statement;

public class CustomerEditController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField address2Field;
    @FXML
    private TextField postalCodeField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField countryField;

    private Stage dialogStage;
    private Customer customer;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if(customer != null){
            nameField.setText(customer.getCustomerName().getValue());
            phoneField.setText(customer.getPhone().getValue());
            addressField.setText(customer.getAddress().getValue());
            address2Field.setText(customer.getAddress2().getValue());
            postalCodeField.setText(customer.getPostalCode().getValue());
            cityField.setText(customer.getCity().getValue());
            countryField.setText(customer.getCountry().getValue());
        }
    }
    @FXML
    private void handleOk() throws Exception {
        boolean result = addCustomerToDB();
        Alert alert;
        if (result){
            alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added!");
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR, "Error while adding customer!");
        }
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
    }
    public boolean addCustomerToDB() throws Exception{
        //First Step: Check if country exists
        int countryId = -1;
        Country c = Queries.getCountry(countryField.getText());
        if(c == null){
            //We need to add country to database
            countryId = Queries.insertCountry(countryField.getText());
        }
        else{
            countryId = c.getCountryID();
        }
        if(countryId == -1) return false;
        //Second Step: City
        int cityId = -1;
        City city = Queries.getCity(cityField.getText());
        if(city == null){
            cityId = Queries.insertCity(cityField.getText(), countryId);
        }
        else{
            cityId = city.getCityId();
        }
        if(cityId == -1) return false;
        //Third Step: Insert Address
        int addressId = Queries.insertAddress(cityId, addressField.getText(), address2Field.getText(), postalCodeField.getText(), phoneField.getText());
        if(addressId == -1) return false;
        //Final Step: Insert Customer
        Customer customer = Queries.insertCustomer(nameField.getText(), addressId);
        if(customer != null) return true;
        return false;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
