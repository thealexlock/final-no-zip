package edu.uh.tech.cis3368.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;

/**  This class is the controller for the editProjectWindow
        It will take text from textfields and update the current Project
        the changes then will be added to the database and table.
 */

public class editProjectWindowController{

    MainController mc = new MainController();
    String DatabaseURL = mc.getURL();

    @FXML
    private TextField date;
    @FXML
    private TextField cost;
    @FXML
    private TextField phone;
    @FXML
    private TextField streetAddress;
    @FXML
    private TextField city;
    @FXML
    private TextField state;
    @FXML
    private TextField zipcode;
    @FXML
    private TextField custName;
    @FXML
    private Button acceptButton;
    @FXML
    private TextArea description;
    @FXML
    private Button cancelButton;

    public String argumentPassed;


    /** One of the most important functions, this will allow us to use controllers from
     * two different classes and communicate with each other to focus on one project
     *
     * @param id which passes an integer from another window
     */
    public void myFunction(Integer id){
        argumentPassed = Integer.toString(id);

        String id_, name_, date_, phonenumber_, street_adress_, city_, state_, zipcode_, cost_, description_;

        try {
            Connection connection = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM customerprojects WHERE job_id =" + argumentPassed);
            while(rs.next()){ ;
                name_ = rs.getString("customer_name");
                date_ = rs.getString("date");
                phonenumber_ = rs.getString("phone_number");
                street_adress_ = rs.getString("street_address");
                city_ = rs.getString("city");
                state_ = rs.getString("state");
                zipcode_ = rs.getString("zipcode");
                cost_ = rs.getString("cost");
                description_ = rs.getString("description");
                custName.setText(name_);
                date.setText(date_);
                phone.setText(phonenumber_);
                streetAddress.setText(street_adress_);
                city.setText(city_);
                state.setText(state_);
                zipcode.setText(zipcode_);
                cost.setText(cost_);
                description.setText(description_);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    void cancelClick(ActionEvent event) {
        Stage stage = (Stage)cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void acceptClick(ActionEvent event) {
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kanbanboard WHERE job_id=" + argumentPassed);

            String query1 = "UPDATE customerprojects SET customer_name = ?, date = ?, phone_number = ?, street_address = ?, city = ?, state = ?, zipcode = ?, cost = ?, description = ? WHERE job_id= " + argumentPassed;
            PreparedStatement preparedStatement = conn.prepareStatement(query1);
            preparedStatement.setString(1, custName.getText());
            preparedStatement.setString(2, date.getText());
            preparedStatement.setString(3, phone.getText());
            preparedStatement.setString(4, streetAddress.getText());
            preparedStatement.setString(5, city.getText());
            preparedStatement.setString(6, state.getText());
            preparedStatement.setString(7, zipcode.getText());
            preparedStatement.setString(8, cost.getText());
            preparedStatement.setString(9, description.getText());
            preparedStatement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Job #" + argumentPassed + " has been updated");
        alert.show();
        cancelClick(new ActionEvent());
    }

}
