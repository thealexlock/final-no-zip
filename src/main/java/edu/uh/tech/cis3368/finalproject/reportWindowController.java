package edu.uh.tech.cis3368.finalproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.time.LocalDate;

/**
 * Controller for reportWindow will allow you to choose timeframe to generate a report.
 */

public class reportWindowController {
    public static LocalDate start1;
    public static LocalDate finish1;
    @FXML
    private DatePicker beginningDate;
    @FXML
    private DatePicker endingDate;

    @FXML
    public void getReport(){
        LocalDate start = beginningDate.getValue();
        LocalDate finish = endingDate.getValue();
        start1 = start;
        finish1 = finish;
        if(start.isAfter(finish)){
            Alert alert = new Alert(Alert.AlertType.ERROR,"The beginning date is after the ending date. \n Please enter valid time-frame.", ButtonType.OK);
            alert.setTitle(null);
            alert.setHeaderText("Invalid Parameters.");
            alert.show();
        } else {
            try{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("resultWindow.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Report Results");
                stage.setScene(new Scene(root1));
                stage.show();
            }
            catch (Exception e){
                System.out.print("System couldn't load other window");
            }
        }
    }


}
