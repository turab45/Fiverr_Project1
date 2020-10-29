package project.view_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.model.Customer;
import project.model.User;
import project.utils.DBConnector;
import project.utils.Queries;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;

public class AppointmentsController {

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    @FXML
    private Button testButton;
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> address2Column;
    @FXML
    private TableColumn<Customer, String> cityColumn;
    @FXML
    private TableColumn<Customer, String> countryColumn;
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label address2Label;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;

    @FXML
    private void initialize() throws Exception {

        // Clear Customer Details Labels
        showCustomerDetails(null);
        // Initialize the table with the two columns.
        getCustomers();
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCustomerDetails(newValue)); //Lambda Expression #1
        customerTable.setItems(customerData);
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerName()); //Lambda Expression #2
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().getPhone());
        postalCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getPostalCode());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().getAddress());
        address2Column.setCellValueFactory(cellData -> cellData.getValue().getAddress2());
        cityColumn.setCellValueFactory(cellData -> cellData.getValue().getCity());
        countryColumn.setCellValueFactory(cellData -> cellData.getValue().getCountry());

        System.out.println(customerTable.getItems());
    }
    private void showCustomerDetails(Customer customer) {
        if (customer != null) {
            // Fill the labels with info from the customer object.
            nameLabel.setText(customer.getCustomerName().getValue());
            phoneLabel.setText(customer.getPhone().getValue());
            addressLabel.setText(customer.getAddress().getValue());
            address2Label.setText(customer.getAddress2().getValue());
            postalCodeLabel.setText(customer.getPostalCode().getValue());
            cityLabel.setText(customer.getCity().getValue());
            countryLabel.setText(customer.getCountry().getValue());
        }
        else{
            nameLabel.setText("");
            phoneLabel.setText("");
            addressLabel.setText("");
            address2Label.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            countryLabel.setText("");
        }
    }

    private void getCustomers() throws Exception {
        customerData = FXCollections.observableArrayList();
        Statement loginStatement = DBConnector.makeConnection().createStatement();
        String query = "SELECT * FROM customer " +
                       "INNER JOIN address ON address.addressId = customer.addressId " +
                       "INNER JOIN city ON city.cityId = address.cityId " +
                       "INNER JOIN country ON country.countryId = city.countryId ";
        ResultSet result = loginStatement.executeQuery(query);
        while(result.next()){
            Customer c = new Customer(result.getInt("customer.customerId"), result.getInt("customer.addressId"), result.getInt("customer.active"), result.getDate("customer.createDate"), result.getDate("customer.lastUpdate"), result.getString("customer.customerName"), result.getString("customer.createdBy"), result.getString("address.address"), result.getString("address.address2"), result.getString("city.city"), result.getString("address.phone"), result.getString("address.postalCode"), result.getString("country.country"), result.getString("customer.lastUpdateBy"));
            customerData.add(c);
        }
    }

    public void trigger(ActionEvent actionEvent) {
        testButton.setText(String.valueOf(User.getUserID()));
    }

    public void deleteCustomer(ActionEvent actionEvent) throws Exception {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        Alert alert;
        // Clear Customer Details Label
        if(selectedCustomer != null){
            if(Queries.deleteCustomer(selectedCustomer)){
                showCustomerDetails(null);
                getCustomers();
                customerTable.setItems(customerData);
                alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted!");
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR, "Error while deleting customer!");
            }
        }
        else{
            alert = new Alert(Alert.AlertType.WARNING, "You haven't selected any customer!");
        }
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
    }

    public void addCustomer(ActionEvent actionEvent){
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CustomerEdit.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CustomerEditController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(null);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            // Clear Customer Details Labels
            showCustomerDetails(null);
            getCustomers();
            customerTable.setItems(customerData);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
