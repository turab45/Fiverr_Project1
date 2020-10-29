package project.view_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.model.City;
import project.model.Country;
import project.model.Customer;
import project.model.User;
import project.utils.Queries;

import java.sql.Date;
import java.sql.Timestamp;

public class AppointmentAddController {
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField titleField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField contactField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField urlField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;
    @FXML
    private DatePicker startDateField;
    @FXML
    private DatePicker endDateField;

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
            customerNameField.setText(customer.getCustomerName().getValue());
        }
    }
    @FXML
    private void addAppointment() throws Exception {
        boolean result = addAppointmentToDB();
        Alert alert;
        if (result){
            alert = new Alert(Alert.AlertType.INFORMATION, "Appointment successfully added!");
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR, "Error while adding appointment!");
        }
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK);
    }
    public boolean addAppointmentToDB() throws Exception{
        int startHours = Integer.parseInt(startTimeField.getText().split(":")[0]);
        int startMinutes = Integer.parseInt(startTimeField.getText().split(":")[1]);
        Timestamp s = Timestamp.valueOf(startDateField.getValue().atTime(startHours, startMinutes));
        int endHours = Integer.parseInt(endTimeField.getText().split(":")[0]);
        int endMinutes = Integer.parseInt(endTimeField.getText().split(":")[1]);
        Timestamp e = Timestamp.valueOf(endDateField.getValue().atTime(endHours, endMinutes));
        int result = Queries.insertAppointment(this.customer.getCustomerID(), User.getUserID(), titleField.getText(), descriptionField.getText(), locationField.getText(), contactField.getText(), typeField.getText(), urlField.getText(), s, e);
        if(result != -1) return true;
        return false;
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
