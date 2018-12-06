package edu.uh.tech.cis3368.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
 /**  This class is the controller for the addEmployeeWindow
        It will take text from textfields and create a new Employee
        that then will be added to the  database and table.
 */

public class addEmployeeWindowController {

    MainController mc = new MainController();
    String DatabaseURL = mc.getURL();
    @FXML
    private TextField date;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField rate;
    @FXML
    private TextField name;
    @FXML
    private TextField id;
    @FXML
    private TextField position;
    @FXML
    private Button acceptButton;

     /**
      * Will accept textfield entries and create a new employee
      * Also, will refresh the table so we can see the updated database in the main employee window
      * @param event
      */
    @FXML
    void accept(ActionEvent event) {

        String name_ = name.getText();
        String position_ = position.getText();
        Double rate_ = Double.parseDouble(rate.getText());
        String date_ = date.getText();

        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            String query = "INSERT INTO employees (name, position, rate, date)\n" +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString (1, name_);
            preparedStmt.setString  (2, position_);
            preparedStmt.setDouble(3, rate_);
            preparedStmt.setString    (4, date_);

            preparedStmt.execute();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeWindow.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();

                Stage thisstage = (Stage) cancelButton.getScene().getWindow();
                thisstage.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

     /**
      * Function will serve as exit for the addEmployeeWindow when the cancel button is clicked
      * or finished adding a new employee
      * @param event
      */
    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
