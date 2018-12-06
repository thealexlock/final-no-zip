package edu.uh.tech.cis3368.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.text.DecimalFormat;
/**
 * Controller for employeeWindow that will display a table of all employees.
 */

public class employeeWindowController {

    MainController mc = new MainController();
    String DatabaseURL = mc.getURL();
    @FXML
    private TableColumn<Employee, Integer> idCol;
    @FXML
    private TableColumn<Employee, String> dateCol;
    @FXML
    private Button editButton;
    @FXML
    private TableColumn<Employee, String> nameCol;
    @FXML
    private Button removeButton;
    @FXML
    private TableColumn<Employee, String> positionCol;
    @FXML
    private Button addButton;
    @FXML
    private TableView<Employee> table;
    @FXML
    private TableColumn<Employee, Double> rateCol;
    @FXML
    private Button raiseButton;

    ObservableList<Employee> list = FXCollections.observableArrayList();


    /**
     * Initialize will connect to the database, set columns, and show observable list on table.
     */
    public void initialize(){
        ConnectToDB();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.setItems(list);
    }

    public void ConnectToDB(){
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM employees ORDER BY id ASC");
            while(rs.next()){
                list.add(new Employee(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getDouble(4), rs.getString(5)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }


    @FXML
    void addEmployee(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addEmployeeWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Employee");
            stage.setScene(new Scene(root1));
            stage.show();
            closeWindow();
        }
        catch (Exception e){
            System.out.print("System couldn't load other window");
        }
    }

    @FXML
    void removeEmployee(ActionEvent event) {
        Integer id = table.getSelectionModel().getSelectedItem().getId();

        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            String query = "DELETE FROM employees WHERE id=" + id;

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            refreshTable();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    void giveRaise(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            Integer id = table.getSelectionModel().getSelectedItem().getId();
            Double oldRate = table.getSelectionModel().getSelectedItem().getRate();
            Double newRate = oldRate * 1.05;

            DecimalFormat df = new DecimalFormat("0.00");
            Double newFormatedRate = Double.parseDouble(df.format(newRate));
            try {
                Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
                String query = "UPDATE employees SET rate = ?  WHERE id=" + id;

                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setDouble(1, newFormatedRate);
                preparedStmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            refreshTable();
        }
    }

    public void refreshTable() {
        list.removeAll(list);
        ConnectToDB();
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        table.setItems(list);
    }

    @FXML
    void editInfo(ActionEvent event) {
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                Integer id = table.getSelectionModel().getSelectedItem().getId();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editEmployeeWindow.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                Stage stage = new Stage();
                stage.setTitle("Edit Employee");
                stage.setScene(new Scene(root1));
                stage.show();

                editEmployeeController secondController = fxmlLoader.getController();
                secondController.myFunction(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        closeWindow();
    }

    public void closeWindow(){
        Stage stage = (Stage) table.getScene().getWindow();
        stage.close();
    }

    public Button returnAddButton(){
        return addButton;
    }

}