package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CreateBugReportController implements Initializable
{
    @FXML
    private TextField reportNameTextField;
    @FXML
    private ComboBox priorityComboBox;
    @FXML
    private ComboBox statusComboBox;
    @FXML
    private ComboBox assignToComboBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextArea bugSourceTextArea;
    @FXML
    private Button createReportButton;

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

        statusComboBox.getItems().removeAll(statusComboBox.getItems());
        statusComboBox.getItems().addAll("Duplicate", "By design", "Won't fix", "Can't reproduce", "Complete", "In-progress", "Blocked");

        initAssignToComboBox();
    }

    //Selects all full names from the database and adds it to assignToComboBox
    private void initAssignToComboBox()
    {
        assignToComboBox.getItems().removeAll(assignToComboBox.getItems());

        String selectAllUsersNames = "SELECT CONCAT(f_name, ' ', l_name) FROM users";

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
        String insertReportIntoDB = "INSERT INTO bug_reports (name, priority, status, assigned_to, description, bug_source, created_by) " +
                                    "VALUES ('" + reportNameTextField.getText() + "', " +
                                    "'" + priorityComboBox.getValue().toString() + "', " +
                                    "\"" + statusComboBox.getValue().toString() + "\", " +
                                    "(SELECT admin_id FROM users WHERE CONCAT(f_name, ' ', l_name) = '" + assignToComboBox.getValue().toString() + "'), " +
                                    "'" + descriptionTextArea.getText() + "', " +
                                    "'" + bugSourceTextArea.getText() + "', " +
                                    LoginController.currentUser + ");";

        try
        {
            if (reportNameTextField.getText().isEmpty() || priorityComboBox.getValue().equals(null) || assignToComboBox.getValue().equals(null))
            {
                //TODO: Display "Report title, priority, and assigned to fields cannot be empty!" error
            }
            else
            {
                Statement statement = connectDB.createStatement();
                statement.executeUpdate(insertReportIntoDB);
                //TODO: Refresh tables
                Stage stage = (Stage) createReportButton.getScene().getWindow();
                stage.close();//TODO: Close window
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
}
