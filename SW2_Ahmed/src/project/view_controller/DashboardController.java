package project.view_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.Main;
import project.model.Appointment;
import project.model.Customer;
import project.model.User;
import project.utils.CalendarView;
import project.utils.DBConnector;
import project.utils.Queries;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardController {

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    private ObservableList<Appointment> appointmentsData = FXCollections.observableArrayList();
    private DateTimeFormatter datetimeDTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
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
    //Appointments Tab
    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> appCustomerIdColumn;
    @FXML
    private TableColumn<Appointment, String> appTitleColumn;
    @FXML
    private TableColumn<Appointment, String> appDescriptionColumn;
    @FXML
    private TableColumn<Appointment, String> appLocationColumn;
    @FXML
    private TableColumn<Appointment, String> appContactColumn;
    @FXML
    private TableColumn<Appointment, String> appTypeColumn;
    @FXML
    private TableColumn<Appointment, String> appUrlColumn;
    @FXML
    private TableColumn<Appointment, String> appStartColumn;
    @FXML
    private TableColumn<Appointment, String> appEndColumn;
    @FXML
    private Pane calendarPane;
    @FXML
    private BarChart reportsChart;

    @FXML
    private void initialize() throws Exception {

        // Clear Customer Details Labels
        showCustomerDetails(null);

        //CUSTOMER Tab
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

        //Appointments Tab
        getAppointments();
        appointmentTable.setItems(appointmentsData);
        appCustomerIdColumn.setCellValueFactory(cellData -> cellData.getValue().getCustomerID().asString());
        appTitleColumn.setCellValueFactory(cellData -> cellData.getValue().getTitle());
        appDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getDescription());
        appLocationColumn.setCellValueFactory(cellData -> cellData.getValue().getLocation());
        appContactColumn.setCellValueFactory(cellData -> cellData.getValue().getContact());
        appTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getType());
        appUrlColumn.setCellValueFactory(cellData -> cellData.getValue().getUrl());
        appStartColumn.setCellValueFactory(cellData -> cellData.getValue().getStartTime().asString());
        appEndColumn.setCellValueFactory(cellData -> cellData.getValue().getEndTime().asString());

        //Add Calendar
        calendarPane.getChildren().add(new CalendarView(appointmentsData, YearMonth.now()).getView());

        //Appointment in the next 15 minutes alert?
        appointmentAlert();
        //Initialize reports chart
        initializeReportsChart();
        System.out.println(customerTable.getItems());
    }

    private void initializeReportsChart(){

        reportsChart.setTitle("Appointments Count per Type");
        reportsChart.getXAxis().setLabel("Appointment Type");
        reportsChart.getYAxis().setLabel("Count");
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Appointment Count");
        Set<String> types = uniqueTypes(appointmentsData);
        for (String type : types) {
            series1.getData().add(new XYChart.Data(type, getTypeCount(appointmentsData, type)));
        }
        reportsChart.getData().add(series1);
    }

    public int getTypeCount(ObservableList<Appointment> appointments, String type){
        int count = 0;
        for (Appointment a : appointments) {
            if(a.getType().get().equals(type)) count++;
        }
        return count;
    }

    public Set<String> uniqueTypes(final ObservableList<Appointment> appointments) {
        ArrayList<String> types = new ArrayList<>();
        for (Appointment a : appointments) {
            types.add(a.getType().get());
        }
        return new HashSet<>(types);
    }

    private void appointmentAlert() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);

        FilteredList<Appointment> filteredData = new FilteredList<>(appointmentsData);

        //lambda expression to find any appointment starting within the next 15 minutes
        filteredData.setPredicate(row -> {
                    LocalDateTime rowDate = LocalDateTime.parse(row.getStartTime().get().toString().substring(0, 16), datetimeDTF);
                    return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
                }
        );
        if (filteredData.isEmpty()) {
            System.out.println("No upcoming appointment.");
        } else {
            String type = filteredData.get(0).getType().toString();
            String customer = filteredData.get(0).getCustomerName();
            String start = filteredData.get(0).getStartTime().get().toString().substring(0, 16);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Reminder");
            alert.setHeaderText("Reminder - An appointment within the next 15 minutes..");
            alert.setContentText("Your appointment with " + customer
                    + " is set to begin at " + start + ".");
            alert.showAndWait();
        }
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

    private void getAppointments() throws Exception {
        appointmentsData = Queries.getAppointments();
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

    public void deleteAppointment(ActionEvent actionEvent) throws Exception {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        Alert alert;
        // Clear Customer Details Label
        if(selectedAppointment != null){
            if(Queries.deleteAppointment(selectedAppointment)){
                getAppointments();
                appointmentTable.setItems(appointmentsData);
                alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted!");
            }
            else{
                alert = new Alert(Alert.AlertType.ERROR, "Error while deleting appointment!");
            }
        }
        else{
            alert = new Alert(Alert.AlertType.WARNING, "You haven't selected any appointment!");
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
            dialogStage.setTitle("Add Customer");
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

    public void addAppointment(ActionEvent actionEvent) {
        if(customerTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Customer Not Selected");
            alert.setHeaderText("Error:");
            alert.setContentText("Please select a customer first!");
            alert.showAndWait();
        }
        else {
            try {
                // Load the fxml file and create a new stage for the popup dialog.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("AppointmentAdd.fxml"));
                AnchorPane page = (AnchorPane) loader.load();

                // Create the dialog Stage.
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Add Appointment");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                // Set the person into the controller.
                AppointmentAddController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setCustomer(customerTable.getSelectionModel().getSelectedItem());

                // Show the dialog and wait until the user closes it
                dialogStage.showAndWait();
                getAppointments();
                appointmentTable.setItems(appointmentsData);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
