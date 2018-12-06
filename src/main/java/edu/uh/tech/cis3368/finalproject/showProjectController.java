package edu.uh.tech.cis3368.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller for showProjectWindow will use id passed from selected item and open a new
 * window that will show its data.
 */

public class showProjectController implements Initializable {

    MainController mc = new MainController();
    String DatabaseURL = mc.getURL();

    @FXML
    private TextField jobIDLabel;
    @FXML
    private TextField dateLabel;
    @FXML
    private TextField nameLabel;
    @FXML
    private TextArea descriptionLabel;
    @FXML
    private TextField costLabel;
    @FXML
    private TextArea addressLabel;
    @FXML
    private TextField phoneLabel;
    @FXML
    private Button okButton;

    @FXML
    public void exit(ActionEvent event){
        Stage stage = (Stage)okButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle){
        try {
            int id = MainController.id1;
            Connection connection = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM customerprojects WHERE job_id = " + id);
            while(rs.next()){
                String id2 = Integer.toString(id);
                String name = rs.getString("customer_name");
                String date = rs.getString("date");
                String phone = rs.getString("phone_number");
                String street = rs.getString("street_address");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zipcode = rs.getString("zipcode");
                String cost = rs.getString("cost");
                String description_ = rs.getString("description");
                jobIDLabel.setText(id2);
                dateLabel.setText(date);
                if(street.isEmpty() || city.isEmpty() || state.isEmpty() || zipcode.isEmpty()){
                    addressLabel.setText("N/A");
                }
                else {
                    addressLabel.setText(street + "\n" + city + ", " + state + " " + zipcode);
                }
                descriptionLabel.setText(description_);
                costLabel.setText("$"+cost);
                phoneLabel.setText(phone);
                nameLabel.setText(name);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
