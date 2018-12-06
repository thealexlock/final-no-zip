package edu.uh.tech.cis3368.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller fro resultWindow will show a report of the selected time frame.
 */

public class resultWindowController implements Initializable {

    public int id;
    public String preProduction;
    public String production;
    public String closeOut;
    public String archived;
    @FXML
    private Button okButton;
    @FXML
    private Label pendingJobsLabel;
    @FXML
    private Label completedJobsLabel;
    @FXML
    private Label profitLabel;
    @FXML
    private TableColumn<ProjectReports, Integer> job_id;
    @FXML
    private TableColumn<ProjectReports, String> customerName;
    @FXML
    private TableColumn<ProjectReports, String> cost;
    @FXML
    private TableColumn<ProjectReports, Integer> job_id2;
    @FXML
    private TableColumn<ProjectReports, String> customerName2;
    @FXML
    private TableColumn<ProjectReports, String> cost2;
    @FXML
    private TableView<ProjectReports> pendingJobsTable;
    @FXML
    private TableView<ProjectReports> completedJobsTable;
    @FXML
    private TableColumn<ProjectReports, Date> date;
    @FXML
    private TableColumn<ProjectReports, Date> date2;
    @FXML
    private Label summaryLabel;

    ObservableList<ProjectReports> list = FXCollections.observableArrayList();
    ObservableList<ProjectReports> list1 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        int pendingCounter = 0;
        int completedCounter = 0;
        int profit = 0;
        try {
            LocalDate startDate = reportWindowController.start1;
            LocalDate finishDate = reportWindowController.finish1;
            Connection connection = DriverManager.getConnection(MainController.databaseURL, MainController.databaseUser, MainController.databasePass);
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT *\n" +
                                                                                "FROM kanbanboard\n" +
                                                                                "WHERE job_id IN (SELECT customerprojects.job_id\n"+
                                                                                "                 FROM customerprojects\n"+
                                                                                "                 WHERE date BETWEEN '" +startDate+ "' AND '" +finishDate+ "')");
            while (resultSet.next()){
                preProduction = resultSet.getString("preproduction");
                production = resultSet.getString("production");
                closeOut = resultSet.getString("closeout");
                archived = resultSet.getString("archived");
                id = resultSet.getInt("job_id");
                if(resultSet.getString("archived").equals("Current Status")){
                    ResultSet resultSet1 = connection.createStatement().executeQuery("SELECT * FROM customerprojects WHERE job_id =" +id);
                    while(resultSet1.next()){
                        String name = resultSet1.getString("customer_name");
                        String cost1 = ("$" + resultSet1.getString("cost"));
                        String costWithoutSign = resultSet1.getString("cost");
                        Date date1 = resultSet1.getDate("date");
                        list.add(new ProjectReports(id, date1, name, cost1));
                        profit += Integer.parseInt(costWithoutSign);
                    }
                    completedCounter++;
                }
                else{
                    ResultSet resultSet2 = connection.createStatement().executeQuery("SELECT * FROM customerprojects WHERE job_id =" +id);
                    while(resultSet2.next()){
                        String name = resultSet2.getString("customer_name");
                        String cost1 = ("$" + resultSet2.getString("cost"));
                        String costWithoutSign = resultSet2.getString("cost");
                        Date date1 = resultSet2.getDate("date");
                        list1.add(new ProjectReports(id, date1, name, cost1));
                        profit += Integer.parseInt(costWithoutSign);
                    }
                    pendingCounter++;
                }
            }

            job_id.setCellValueFactory(new PropertyValueFactory<>("JobID"));
            date.setCellValueFactory(new PropertyValueFactory<>("Date"));
            customerName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
            cost.setCellValueFactory(new PropertyValueFactory<>("Cost"));
            pendingJobsTable.setItems(list1);

            job_id2.setCellValueFactory(new PropertyValueFactory<>("JobID"));
            date2.setCellValueFactory(new PropertyValueFactory<>("Date"));
            customerName2.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
            cost2.setCellValueFactory(new PropertyValueFactory<>("Cost"));
            completedJobsTable.setItems(list);

            pendingJobsLabel.setText(Integer.toString(pendingCounter));
            completedJobsLabel.setText(Integer.toString(completedCounter));
            profitLabel.setText("$" +Integer.toString(profit));
            summaryLabel.setText("*** QUERY RESULTS FROM " + startDate + " TO " + finishDate + " ***");

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void close(ActionEvent event){
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }
}
