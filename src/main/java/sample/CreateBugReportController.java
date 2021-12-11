package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CreateBugReportController implements Initializable
{
    private BugTrackerController bugTrackerController;

    @FXML
    private TextField reportNameTextField;
    @FXML
    private ComboBox priorityComboBox;
    //@FXML
    //private ComboBox statusComboBox;
    @FXML
    private ComboBox assignToComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextArea bugSourceTextArea;
    @FXML
    private Button createReportButton;
    @FXML
    private Label emptyFieldsErrorLabel;

    DatabaseConnection connectNow;
    Connection connectDB;

    public CreateBugReportController()
    {
        connectNow = new DatabaseConnection();
        connectDB = connectNow.getConnection();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        priorityComboBox.getItems().removeAll(priorityComboBox.getItems());
        priorityComboBox.getItems().addAll("Critical", "High", "Normal", "Low");

        initAssignToComboBox();
    }

    //Selects all full names from the database and adds it to assignToComboBox
    private void initAssignToComboBox()
    {
        assignToComboBox.getItems().removeAll(assignToComboBox.getItems());

        String selectAllUsersNames = "SELECT CONCAT(f_name, ' ', l_name) FROM users WHERE admin_id != " + LoginController.currentUser;

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectAllUsersNames);

            while(queryResult.next())
            {
                assignToComboBox.getItems().add(queryResult.getString(1));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    //Adds bug report data into the database
    public void createBugReportOnAction()
    {
        try
        {
            if (reportNameTextField.getText().isEmpty() || priorityComboBox.getValue().equals(null) || assignToComboBox.getValue().equals(null))
            {
                emptyFieldsErrorLabel.setText("Report title, priority, and assign to fields cannot be empty!");
            }
            else
            {
                String insertReportIntoDB = "INSERT INTO bug_reports (name, priority, status, assigned_to, description, bug_source, created_by) " +
                        "VALUES ('" + reportNameTextField.getText() + "', " +
                        "'" + priorityComboBox.getValue().toString() + "', " +
                        "'In-progress', " +
                        "(SELECT admin_id FROM users WHERE CONCAT(f_name, ' ', l_name) = '" + assignToComboBox.getValue().toString() + "'), " +
                        "'" + descriptionTextArea.getText() + "', " +
                        "'" + bugSourceTextArea.getText() + "', " +
                        LoginController.currentUser + ");";

                String insertIntoInprogress = "INSERT INTO inprogress_issues (report_title, assigned_to) " +
                                              "VALUES ('" + reportNameTextField.getText() + "', (SELECT admin_id FROM users WHERE CONCAT(f_name, ' ', l_name) = '" + assignToComboBox.getValue().toString() +"'));";

                String insertIntoAssigned = "INSERT INTO assigned_reports (report_title, assigned_to) " +
                                            "VALUES ('" + reportNameTextField.getText() + "', (SELECT admin_id FROM users WHERE CONCAT(f_name, ' ', l_name) = '" + assignToComboBox.getValue().toString() +"'));";

                String insertIntoCreated = "INSERT INTO created_reports (report_title, assigned_to) " +
                                           "VALUES ('" + reportNameTextField.getText() + "', " + LoginController.currentUser + ");";

                String updateUsersTotalAssigned = "UPDATE users SET total_assigned = total_assigned + 1 WHERE CONCAT(f_name, ' ', l_name) = '" + assignToComboBox.getValue().toString() + "';";
                String updateUsersTotalCreated = "UPDATE users SET total_created = total_created + 1 WHERE admin_id = " + LoginController.currentUser + ";";
                
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(insertReportIntoDB);
                statement.executeUpdate(insertIntoInprogress);
                statement.executeUpdate(insertIntoAssigned);
                statement.executeUpdate(insertIntoCreated);
                statement.executeUpdate(updateUsersTotalAssigned);
                statement.executeUpdate(updateUsersTotalCreated);
                bugTrackerController.updateTables();
                Stage stage = (Stage) createReportButton.getScene().getWindow();
                stage.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void setBugTrackerController(BugTrackerController controller)
    {
        bugTrackerController = controller;
    }
}
