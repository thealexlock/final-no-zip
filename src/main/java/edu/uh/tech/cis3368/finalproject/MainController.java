package edu.uh.tech.cis3368.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
/**This is the controller for the main window where the table will show current projects.

  String "DatabaseURL" can be changed and will access the same
  url for the database throughout the entire application.
*/

@Component
public class MainController implements Initializable{

    public String DatabaseURL = "jdbc:postgresql://localhost:5432/groupprojectdatabase";
    public static String databaseURL = "jdbc:postgresql://localhost:5432/groupprojectdatabase";
    public static String databaseUser = "postgres";
    public static String databasePass = "postgres";
    public static int id1;
    @FXML
    private Button addProjectButton;
    @FXML
    private TableColumn<Projects, Integer> job_id;
    @FXML
    private TableColumn<Projects, String> preProduction;
    @FXML
    private TableColumn<Projects, String> production;
    @FXML
    private TableColumn<Projects, String> closeOut;
    @FXML
    private TableColumn<Projects, String> archived;
    @FXML
    private TableView<Projects> kanbanBoard;
    @FXML
    private Label PreProductionBox;
    @FXML
    private Label ProductionBox;
    @FXML
    private Label CloseOutnBox;
    @FXML
    private Label ArchivesBox;

    ObservableList<Projects> list = FXCollections.observableArrayList();

    public String getURL(){
        return DatabaseURL;
    }

    /** Initialize will set the table columns, connect to the database and add items to the table.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        ConnectToDB();
            job_id.setCellValueFactory(new PropertyValueFactory<>("jobID"));
            preProduction.setCellValueFactory(new PropertyValueFactory<>("preProduction"));
            production.setCellValueFactory(new PropertyValueFactory<>("production"));
            closeOut.setCellValueFactory(new PropertyValueFactory<>("closeOut"));
            archived.setCellValueFactory(new PropertyValueFactory<>("archived"));
            kanbanBoard.setItems(list);

    }

    public void ConnectToDB(){
        try {
            Connection connection = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM kanbanboard ORDER BY job_id");
            while(rs.next()){
                list.add(new Projects(rs.getInt(1),rs.getString(2),rs.getString(3), rs.getString(4), rs.getString(5)));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Returning job id for project selected from the table
    public int returnJobID() {
        try {
            Projects selected = kanbanBoard.getSelectionModel().getSelectedItem();
            int jobID = 0;
            if (selected != null){
                jobID = selected.getJobID();
                return jobID;
            }
            return 0;

        } catch (Exception e) {
            return 0;
        }
    }


    //Click on any of the buttons:

    /**
     * editProject will open a new window that will display the job selected and will take user's
     * input to make changes
     * @param event
     */
    @FXML
    void editProject(ActionEvent event){
        try {
            Integer id = kanbanBoard.getSelectionModel().getSelectedItem().getJobID();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("editProjectWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Edit Project");
            stage.setScene(new Scene(root1));
            stage.show();

            editProjectWindowController secondController = fxmlLoader.getController();
            secondController.myFunction(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showProject(ActionEvent event) {
        id1 = returnJobID();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("showProject.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Show Project Information");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            System.out.print("System couldn't load other window");
        }
    }

    /**
     * Function will update the connection and add new items to the table.
     * @param event
     */
    @FXML
    void refreshTable(ActionEvent event){
        list.removeAll(list);
        ConnectToDB();
        job_id.setCellValueFactory(new PropertyValueFactory<>("jobID"));
        preProduction.setCellValueFactory(new PropertyValueFactory<>("preProduction"));
        production.setCellValueFactory(new PropertyValueFactory<>("production"));
        closeOut.setCellValueFactory(new PropertyValueFactory<>("closeOut"));
        archived.setCellValueFactory(new PropertyValueFactory<>("archived"));
        kanbanBoard.setItems(list);

    }

    @FXML
    void deleteProject(ActionEvent event){
        id1 = returnJobID();
        if (id1 > 0) {
            try {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this project (Job ID: " + id1 + ")?", ButtonType.YES, ButtonType.NO);
                a.showAndWait();
                if (a.getResult() == ButtonType.YES) {
                    Connection connection = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
                    PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM customerprojects \n" +
                            "WHERE job_id = " + id1);
                    preparedStatement.execute();
                    PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM kanbanboard \n" +
                            "WHERE job_id = " + id1);
                    preparedStatement1.execute();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully deleted project (Job ID: " + id1 + ").", ButtonType.OK);
                    alert.show();
                }
                if (a.getResult() == ButtonType.NO) {
                    a.hide();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "No rows selected.");
            a.showAndWait();
        }
    }

    @FXML
    void addProjectClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addProjectWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Project");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.print("System couldn't load other window");
        }
    }

    @FXML
    void EstimateClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("estimateWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Estimate Cost");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.print("System couldn't load other window");
        }
    }

    @FXML
    void reportClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reportWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Report");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.print("System couldn't load other window");
            e.printStackTrace();
        }
    }

    @FXML
    void employeesClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employeeWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Employees");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e){
            System.out.print("System couldn't load other window");
        }
    }



    // Handling drag and drop functions depending on the target:
    @FXML
    void dragDroppedPreProduction(DragEvent dragEvent){
        Dragboard dragboard = dragEvent.getDragboard();
        String result = dragboard.getString();
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kanbanboard WHERE job_id=" + result);
            while (rs.next()) {
                String pre = rs.getString("preproduction");
                String production = rs.getString("production");
                String closeout = rs.getString("closeout");
                String arch = rs.getString("archived");
                if (Objects.equals(production, "Current Status")){
                    String query1 = "UPDATE kanbanboard SET preproduction = ?, production = null, closeout = null, archived = null WHERE job_id= " + result;
                    PreparedStatement preparedStatement = conn.prepareStatement(query1);
                    preparedStatement.setString (1, "Current Status");
                    preparedStatement.execute();
                }
                else {
                    System.out.print("Yeah, not happening");
                }
            }
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTable(new ActionEvent());
    }

    @FXML
    void dragDroppedProduction(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        String result = dragboard.getString();
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kanbanboard WHERE job_id=" + result);
            while (rs.next()) {
                String pre = rs.getString("preproduction");
                String production = rs.getString("production");
                String closeout = rs.getString("closeout");
                String arch = rs.getString("archived");
                if (Objects.equals(pre, "Current Status")){
                    String query1 = "UPDATE kanbanboard SET preproduction = null, production = ?, closeout = null, archived = null WHERE job_id= " + result;
                    PreparedStatement preparedStatement = conn.prepareStatement(query1);
                    preparedStatement.setString (1, "Current Status");
                    preparedStatement.execute();
                }
                else if (Objects.equals(closeout, "Current Status")){
                    String query1 = "UPDATE kanbanboard SET preproduction = null, production = ?, closeout = null, archived = null WHERE job_id= " + result;
                    PreparedStatement preparedStatement = conn.prepareStatement(query1);
                    preparedStatement.setString (1, "Current Status");
                    preparedStatement.execute();
                }
                else {
                    System.out.print("Yeah, not happening");
                }
            }
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTable(new ActionEvent());
    }

    @FXML
    void dragDroppedCloseOut(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        String result = dragboard.getString();
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kanbanboard WHERE job_id=" + result);
            while (rs.next()) {
                String pre = rs.getString("preproduction");
                String production = rs.getString("production");
                String closeout = rs.getString("closeout");
                String arch = rs.getString("archived");
                if (Objects.equals(production, "Current Status")){
                    String query1 = "UPDATE kanbanboard SET preproduction = null, production = null, closeout = ?, archived = null WHERE job_id= " + result;
                    PreparedStatement preparedStatement = conn.prepareStatement(query1);
                    preparedStatement.setString (1, "Current Status");
                    preparedStatement.execute();
                }
                else if (Objects.equals(arch, "Current Status")){
                    String query1 = "UPDATE kanbanboard SET preproduction = null, production = null, closeout = ?, archived = null WHERE job_id= " + result;
                    PreparedStatement preparedStatement = conn.prepareStatement(query1);
                    preparedStatement.setString (1, "Current Status");
                    preparedStatement.execute();
                }
                else {
                    System.out.print("Yeah, not happening");
                }
            }
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTable(new ActionEvent());
    }

    @FXML
    void dragDroppedArchives(DragEvent dragEvent) {
        Dragboard dragboard = dragEvent.getDragboard();
        String result = dragboard.getString();
        try {
            Connection conn = DriverManager.getConnection(DatabaseURL, "postgres", "postgres");
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM kanbanboard WHERE job_id=" + result);
            while (rs.next()) {
                String pre = rs.getString("preproduction");
                String production = rs.getString("production");
                String closeout = rs.getString("closeout");
                String arch = rs.getString("archived");
                if (Objects.equals(closeout, "Current Status")){
                    String query1 = "UPDATE kanbanboard SET preproduction = null, production = null, closeout = null, archived = ? WHERE job_id= " + result;
                    PreparedStatement preparedStatement = conn.prepareStatement(query1);
                    preparedStatement.setString (1, "Current Status");
                    preparedStatement.execute();
                }
                else {
                    System.out.print("Yeah, not happening");
                }
            }
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        refreshTable(new ActionEvent());
    }

    @FXML
    void startDrag(MouseEvent mouseEvent){
        Dragboard dragboard = kanbanBoard.startDragAndDrop(TransferMode.ANY);
        ClipboardContent clipboardContent = new ClipboardContent();
        if (kanbanBoard.getSelectionModel().getSelectedItem() != null) {
            int id = kanbanBoard.getSelectionModel().getSelectedItem().getJobID();
            clipboardContent.putString(Integer.toString(id));
            dragboard.setContent(clipboardContent);
            mouseEvent.consume();
        }
    }
}


