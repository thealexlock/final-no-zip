package edu.uh.tech.cis3368.finalproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDate;
/**  This class is the controller for the addProjectWindow
        It will take text from textfields and create a new Project
        that then will be added to the database and table.
 */

public class addProjectController {

    @FXML
    private DatePicker dateField;
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
    private CheckBox shippingCheck;
    @FXML
    private TextField custName;
    @FXML
    private Button acceptButton;
    @FXML
    private Button estimateCalcButton;
    @FXML
    private TextArea description;


    private boolean isValid;


    // checks if all the fields are filled in before adding the project.
    public boolean checkForm(boolean x){
        if(shippingCheck.isSelected() && (custName.getText().isEmpty() || dateField.getValue()==null || phone.getText().isEmpty() || cost.getText().isEmpty() || streetAddress.getText().isEmpty() || city.getText().isEmpty() || state.getText().isEmpty() || zipcode.getText().isEmpty() || description.getText().isEmpty())){
            return false;
        }
        else if(!shippingCheck.isSelected() && custName.getText().isEmpty() || dateField.getValue()==null|| phone.getText().isEmpty() || cost.getText().isEmpty() || description.getText().isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Function will take the strings from the textfields and will create a Project instance with
     * that information. It will ad it to the database and
     * @param event
     */
    @FXML
    void AddClicked(ActionEvent event) {
        if(checkForm(isValid)) {
            try {
                String name = custName.getText();
                LocalDate date1 = dateField.getValue();
                String phone1 = phone.getText();
                String address = streetAddress.getText();
                String city1 = city.getText();
                String state1 = state.getText();
                String zip = zipcode.getText();
                String cost1 = cost.getText();
                String desc = description.getText();
                Connection connection = DriverManager.getConnection(MainController.databaseURL,MainController.databaseUser,MainController.databasePass);
                String q = "INSERT INTO customerprojects (customer_name,date,phone_number,street_address,city,state,zipcode,cost,description)\n" +
                        "VALUES ('"+name+"', '"+date1+"', '"+phone1+"', '"+address+"', '"+city1+"', '"+state1+"', '"+zip+"', '"+cost1+"', '"+desc+"');";
                PreparedStatement ps = connection.prepareStatement(q);
                ps.execute();
                custName.setText(null);
                dateField.setValue(null);
                cost.setText(null);
                phone.setText(null);
                streetAddress.setText(null);
                city.setText(null);
                state.setText(null);
                zipcode.setText(null);
                description.setText(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                Connection connection = DriverManager.getConnection(MainController.databaseURL,MainController.databaseUser,MainController.databasePass);
                String q = "INSERT INTO kanbanboard (preproduction, production, closeout, archived)\n" +
                        "VALUES ('Current Status',null,null,null);";
                PreparedStatement ps = connection.prepareStatement(q);
                ps.execute();
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully added project.", ButtonType.OK);
                alert.setTitle(null);
                alert.setHeaderText("Success!");
                alert.show();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Alert a = new Alert(Alert.AlertType.ERROR,"Please fill out all of the fields.",ButtonType.OK);
            a.setHeaderText("Some fields are empty!");
            a.show();
        }
    }

    /**
     * Function will activate if shipping option is checked.
     * It will also activate address text fields so user can input the address.
     * @param event
     */
    @FXML
    void ShippingChecked(ActionEvent event) {
        if (shippingCheck.isSelected()) {
            streetAddress.setDisable(false);
            streetAddress.setEditable(true);
            city.setDisable(false);
            city.setEditable(true);
            state.setDisable(false);
            state.setEditable(true);
            zipcode.setDisable(false);
            zipcode.setEditable(true);

        }
        else {
            streetAddress.setDisable(true);
            streetAddress.setEditable(true);
            city.setDisable(true);
            city.setEditable(true);
            state.setDisable(true);
            state.setEditable(true);
            zipcode.setDisable(true);
            zipcode.setEditable(true);
        }
    }

    /**
     * Function will open calculator window to set the cost of the project.
     * @param event
     */
    @FXML
    void EstimateCalcOpen(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("estimateWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Estimate Calculator");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.print("System couldn't load other window");
        }
    }

}
