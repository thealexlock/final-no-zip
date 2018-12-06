package edu.uh.tech.cis3368.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

/**  This class is the controller for the editEmployeeWindo
        It will take text from textfields and update the current Employee
        the changes then will be added to the database and table.
 */

public class editEmployeeController {
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

    public String argumentPassed;


    /** One of the most important functions, this will allow us to use controllers from
     * two different classes and communicate with each other to focus on one project
     *
     * @param id which passes an integer from another window
     */
    public void myFunction(Integer id){
        argumentPassed = Integer.toString(id);

        String id_, name_, position_, date_;
        Double rate_ = 0.0;

        try {
            Connection connection = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM employees WHERE id =" + argumentPassed);
            while(rs.next()){
                name_ = rs.getString("name");
                position_ = rs.getString("position");
                rate_ = rs.getDouble("rate");
                date_ = rs.getString("date");
                name.setText(name_);
                position.setText(position_);
                rate.setText(Double.toString(rate_));
                date.setText(date_);
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * This function is accessed when the user clicks the accept button
     * @param event to handle action event.
     */
    @FXML
    void accept(ActionEvent event) {
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM employees WHERE id=" + argumentPassed);

            String query1 = "UPDATE employees SET \"name\" = ?, \"position\" = ?, rate = ?, date = ? WHERE id= " + argumentPassed;
            PreparedStatement preparedStatement = conn.prepareStatement(query1);
            preparedStatement.setString(1, name.getText());
            preparedStatement.setString(2, position.getText());
            preparedStatement.setDouble(3, Double.parseDouble(rate.getText()));
            preparedStatement.setString(4, date.getText());
            preparedStatement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeWindow.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();

                Stage thisstage = (Stage) cancelButton.getScene().getWindow();
                thisstage.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Employee #" + argumentPassed + " has been updated");
        alert.show();
        cancel(new ActionEvent());
    }

    /** closes window
     *
     * @param event to handle this event
     */
    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
