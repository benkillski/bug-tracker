package sample;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;


public class BugTrackerController implements Initializable
{
    @FXML
    private TableView teamMemberTable;
    @FXML
    private TableView bugsAndIssuesTable;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private Label idNumberLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label profileInfoErrorMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        initBugsAndIssuesTable();
        initTeamMembersTable();
        initProfileTab();
    }

    //Adds data to the Bugs and Issues table
    public void initBugsAndIssuesTable()
    {
        TableColumn bugTitle = new TableColumn("Name");
        TableColumn priority = new TableColumn("Priority");
        TableColumn attachments = new TableColumn("Attachments");
        TableColumn status = new TableColumn("Status");
        TableColumn assignedTo = new TableColumn("Assigned to");
        TableColumn description = new TableColumn("Description");
        TableColumn openedDate = new TableColumn("Opened date");
        TableColumn daysOld = new TableColumn("Days old");
        TableColumn duplicates = new TableColumn("Duplicates?");
        TableColumn bugSource = new TableColumn("Bug source");
        TableColumn createdBy = new TableColumn("Created by");
        //TableColumn notificationStatus = new TableColumn("Notification status");
        bugsAndIssuesTable.getColumns().addAll(bugTitle, priority, attachments, status, assignedTo, description, openedDate, daysOld, duplicates, bugSource, createdBy);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        //Query to retrieve data from bug_reports
        String selectAllBugReports = "SELECT " +
                                        "name, " +
                                        "priority, " +
                                        "attachments, " +
                                        "status, " +
                                        "CONCAT(b.f_name, ' ', b.l_name), " +
                                        "description, " +
                                        "opened_date, " +
                                        "DATEDIFF(NOW(), opened_date), " +
                                        "duplicates, " +
                                        "bug_source, " +
                                        "CONCAT(a.f_name, ' ', a.l_name) " +
                                    "FROM bug_reports " +
                                    "INNER JOIN users AS a " +
                                        "ON a.admin_id = bug_reports.created_by " +
                                    "INNER JOIN users AS b " +
                                        "ON b.admin_id = bug_reports.assigned_to;";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectAllBugReports);

            //Define data in an ObservableList and add data from query as you want to show inside table
            final ObservableList<BugsAndIssues> data = FXCollections.observableArrayList();
            while (queryResult.next())
            {
                data.add(new BugsAndIssues( queryResult.getString("name"),
                                            queryResult.getString("priority"),
                                            queryResult.getString("attachments"),
                                            queryResult.getString("status"),
                                            queryResult.getString("CONCAT(b.f_name, ' ', b.l_name)"),
                                            queryResult.getString("description"),
                                            queryResult.getString("opened_date"),
                                            queryResult.getInt("DATEDIFF(NOW(), opened_date)"),
                                            queryResult.getString("duplicates"),
                                            queryResult.getString("bug_source"),
                                            queryResult.getString("CONCAT(a.f_name, ' ', a.l_name)")
                        )
                );

                //Associate data with columns
                bugTitle.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("bugTitle"));
                priority.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("priority"));
                attachments.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("attachments"));
                status.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("status"));
                assignedTo.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("assignedTo"));
                description.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("description"));
                openedDate.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("openedDate"));
                daysOld.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("daysOld"));
                duplicates.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("duplicates"));
                bugSource.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("bugSource"));
                createdBy.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("createdBy"));

                //Add data inside of table
                bugsAndIssuesTable.setItems(data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    //Adds data to the Team Members Table
    public void initTeamMembersTable()
    {
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("Name");
        TableColumn inprogressIssues = new TableColumn("In-progress Issues");
        TableColumn completedIssues = new TableColumn("Completed Issues");
        TableColumn closedIssues = new TableColumn("Closed Issues");
        TableColumn totalAssigned = new TableColumn("Total assigned");
        TableColumn assignedReports = new TableColumn("Assigned Reports");
        TableColumn totalCreated = new TableColumn("Total Created");
        TableColumn createdReports = new TableColumn("Created Reports");
        teamMemberTable.getColumns().addAll(id, name, inprogressIssues, completedIssues, closedIssues, totalAssigned, assignedReports, totalCreated, createdReports);

        //Connects to database
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        //Query to select all rows from the users table
        String selectAllUsernames = "SELECT " +
                                        "admin_id, " +
                                        "CONCAT(f_name, ' ', l_name), " +
                                        //"inprogress_issues, " +
                                        //"completed_issues, " +
                                        //"closed_issues, " +
                                        "total_assigned, " +
                                        //"assigned_reports, " +
                                        "total_created " +
                                        //"created_reports " +
                                    "FROM users;";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectAllUsernames);

            //Define data in an ObservableList and add data from query as you want to show inside table
            final ObservableList<Users> data = FXCollections.observableArrayList();
            while(queryResult.next())
            {
                data.add(new Users( queryResult.getInt("admin_id"),
                                    queryResult.getString("CONCAT(f_name, ' ', l_name)"),
                                    queryResult.getInt("total_assigned"),
                                    queryResult.getInt("total_created")
                                    )
                );
            }

            //Associate data with columns
            id.setCellValueFactory(new PropertyValueFactory<Users, Integer>("id"));
            name.setCellValueFactory(new PropertyValueFactory<Users, String>("name"));
            totalAssigned.setCellValueFactory(new PropertyValueFactory<Users, Integer>("totalAssigned"));
            totalCreated.setCellValueFactory(new PropertyValueFactory<Users, Integer>("totalCreated"));

            //Add data inside of table
            teamMemberTable.setItems(data);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void initProfileTab()
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String selectCurrentUserInfo = "SELECT f_name, l_name, username, password FROM users WHERE admin_id = " + LoginController.currentUser + ";";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectCurrentUserInfo);

            if(queryResult.next())
            {
                firstNameTextField.setText(queryResult.getString("f_name"));
                lastNameTextField.setText(queryResult.getString("l_name"));
                idNumberLabel.setText(Integer.toString(LoginController.currentUser));
                usernameTextField.setText(queryResult.getString("username"));
                passwordField.setText(queryResult.getString("password"));
                confirmPasswordField.setText(queryResult.getString("password"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    //Creates a pop up window to create a bug report
    public void reportBugOnAction(ActionEvent event) throws IOException
    {
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setResizable(false);
        popupWindow.setTitle("Report Bug");

        Parent root = FXMLLoader.load(getClass().getResource("createBugReport.fxml"));
        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.show();
    }

    //Saves profile changes into database
    public void saveProfileChangesOnActions(ActionEvent event) throws IOException
    {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String updateUserData = "UPDATE users " +
                                "SET f_name = '" + firstNameTextField.getText() + "', l_name = '" + lastNameTextField.getText() + "', password = '" + passwordField.getText() + "' " +
                                "WHERE admin_id = " + LoginController.currentUser + ";";

        try
        {
            Statement statement = connectDB.createStatement();

            if (passwordField.getText().equals(confirmPasswordField.getText()) && !firstNameTextField.getText().isEmpty() && !lastNameTextField.getText().isEmpty() && !passwordField.getText().isEmpty())
            {
                statement.executeUpdate(updateUserData);
            }
            else
            {
                profileInfoErrorMessage.setText("Firstname, Lastname, Username, and Password fields cannot be blank! Password fields must also match!");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void updateTables()
    {
        teamMemberTable.refresh();
        bugsAndIssuesTable.refresh();
    }
}
