package project.view_controller;

import com.sun.deploy.net.HttpResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.model.User;
import project.utils.DBConnector;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class LoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Label test;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    private String errorMessage;

    public String getPublicIP() throws IOException
    {
        URL url = new URL("http://icanhazip.com/");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }

    private void getLocation() throws IOException {
        String IP = getPublicIP();
        //http://api.ipstack.com/185.134.176.80
        URL url = new URL("http://api.ipstack.com/" + IP + "?access_key=2d05b0a8ae42e07b5ac4b8b77047f6ce&format=1&fields=country_name");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        String countryName = content.toString().split(":\"")[1];
        countryName = countryName.substring(0, countryName.length()-2);
        System.out.println(countryName);
        if(!countryName.equals("United States")){
            errorMessage = "Mot de passe incorrecte";
        }
        else{
            errorMessage = "Incorrect Password";
        }
    }

    public void loginButtonHandler(ActionEvent actionEvent) throws Exception {
        Parent root;
        Stage stage;
        getLocation();
        int userID = login(usernameField.getText(), passwordField.getText());
        if(userID != -1){
            //Write To Log
            try {
                String fileName = "loginLogs";
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
                writer.append(LocalDateTime.now() + " " + usernameField.getText() + " " + "\n");
                writer.flush();
                writer.close();
            } catch (IOException e) {
                System.out.println(e);
            }
            User user = new User(usernameField.getText(), passwordField.getText(), userID);
            root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.show();
        }
        else{
            test.setText(errorMessage);
        }
    }

    private int login(String username, String password) throws Exception {

        Statement loginStatement = DBConnector.makeConnection().createStatement();
        String query = "SELECT userID, password FROM user WHERE username = '" + username + "'";
        ResultSet result = loginStatement.executeQuery(query);
        while(result.next()){
            if(result.getString("password").equals(password)){
                return result.getInt("userID");
            }
        }
        return -1;
    }
}
