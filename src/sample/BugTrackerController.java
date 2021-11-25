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
import java.sql.*;
import java.util.ResourceBundle;


public class BugTrackerController implements Initializable
{
    @FXML
    private TableView teamMemberTable;
    @FXML
    private TableView bugsAndIssuesTable;
    @FXML
    private TableView yourReportsTable;
    @FXML
    private Button changeStatusButton;
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

    private ObservableList<BugsAndIssues> bugData;
    private ObservableList<YourReports> yourReportsData;
    private ObservableList<Users> memberData;

    private DatabaseConnection connectNow;
    private Connection connectDB;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        connectNow = new DatabaseConnection();
        connectDB = connectNow.getConnection();

        initBugsAndIssuesTable();
        initYourReportsTable();
        changeStatusButton.setDisable(true);
        initTeamMembersTable();
        initProfileTab();
    }

    //Adds data to the Bugs and Issues table
    public void initBugsAndIssuesTable()
    {
        TableColumn reportId = new TableColumn("ID");
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
        bugsAndIssuesTable.getColumns().addAll(reportId, bugTitle, priority, attachments, status, assignedTo, description, openedDate, daysOld, duplicates, bugSource, createdBy);

        //Query to retrieve data from bug_reports
        String selectAllBugReports = "SELECT " +
                                        "id, " +
                                        "name, " +
                                        "priority, " +
                                        "attachments, " +
                                        "status, " +
                                        "CONCAT(b.f_name, ' ', b.l_name)  AS assigned, " +
                                        "description, " +
                                        "opened_date, " +
                                        "DATEDIFF(NOW(), opened_date) AS days_old, " +
                                        "duplicates, " +
                                        "bug_source, " +
                                        "CONCAT(a.f_name, ' ', a.l_name) AS creator " +
                                    "FROM bug_reports " +
                                    "INNER JOIN users AS a " +
                                        "ON a.admin_id = bug_reports.created_by " +
                                    "INNER JOIN users AS b " +
                                        "ON b.admin_id = bug_reports.assigned_to " +
                                    "ORDER BY id;";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectAllBugReports);

            //Define data in an ObservableList and add data from query as you want to show inside table
            bugData = FXCollections.observableArrayList();
            while (queryResult.next())
            {
                bugData.add(new BugsAndIssues( queryResult.getInt("id"),
                                            queryResult.getString("name"),
                                            queryResult.getString("priority"),
                                            queryResult.getString("attachments"),
                                            queryResult.getString("status"),
                                            queryResult.getString("assigned"),
                                            queryResult.getString("description"),
                                            queryResult.getString("opened_date"),
                                            queryResult.getInt("days_old"),
                                            queryResult.getString("duplicates"),
                                            queryResult.getString("bug_source"),
                                            queryResult.getString("creator")
                        )
                );

                //Associate data with columns
                reportId.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("id"));
                bugTitle.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("bugTitle"));
                priority.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("priority"));
                attachments.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("attachments"));
                status.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("status"));
                assignedTo.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("assignedTo"));
                description.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("description"));
                openedDate.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("openedDate"));
                daysOld.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("daysOld"));
                duplicates.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("duplicates"));
                bugSource.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("bugSource"));
                createdBy.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, String>("createdBy"));

                //Add data inside of table
                bugsAndIssuesTable.setItems(bugData);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    //Adds data to the Your Reports Table
    public void initYourReportsTable()
    {
        TableColumn reportId = new TableColumn("ID");
        TableColumn reportName = new TableColumn("Title");
        TableColumn priority = new TableColumn("Priority");
        TableColumn attachments = new TableColumn("Attachments");
        TableColumn status = new TableColumn("Status");
        TableColumn description = new TableColumn("Description");
        TableColumn openedDate = new TableColumn("Opened date");
        TableColumn daysOld = new TableColumn("Days old");
        TableColumn duplicates = new TableColumn("Duplicates?");
        TableColumn bugSource = new TableColumn("Bug source");
        TableColumn createdBy = new TableColumn("Created by");
        yourReportsTable.getColumns().addAll(reportId, reportName, priority, attachments, status, description, openedDate, daysOld, duplicates, bugSource, createdBy);

        //Listener to enable/disable the Change status button based on if a row has been selected or not
        yourReportsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null)
            {
                changeStatusButton.setDisable(false);
                changeStatusButton.setOpacity(1);
            }
            else
            {
                changeStatusButton.setDisable(true);
                changeStatusButton.setOpacity(0.5);
            }
        });

        String selectAllBugReports = "SELECT " +
                                        "id, " +
                                        "name, " +
                                        "priority, " +
                                        "attachments, " +
                                        "status, " +
                                        "description, " +
                                        "opened_date, " +
                                        "DATEDIFF(NOW(), opened_date) AS days_old, " +
                                        "duplicates, " +
                                        "bug_source, " +
                                        "CONCAT(f_name, ' ', l_name) AS creator " +
                                    "FROM bug_reports " +
                                    "INNER JOIN users " +
                                        "ON users.admin_id = bug_reports.created_by " +
                                    "WHERE assigned_to = " + LoginController.currentUser +
                                    " ORDER BY opened_date;";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectAllBugReports);

            //Define data in an ObservableList and add data from query as you want to show inside table
            yourReportsData = FXCollections.observableArrayList();
            while (queryResult.next())
            {
                yourReportsData.add(new YourReports( queryResult.getInt("id"),
                                queryResult.getString("name"),
                                queryResult.getString("priority"),
                                queryResult.getString("attachments"),
                                queryResult.getString("status"),
                                queryResult.getString("description"),
                                queryResult.getString("opened_date"),
                                queryResult.getInt("days_old"),
                                queryResult.getString("duplicates"),
                                queryResult.getString("bug_source"),
                                queryResult.getString("creator")
                        )
                );
            }

            //Associate data with columns
            reportId.setCellValueFactory(new PropertyValueFactory<YourReports, Integer>("id"));
            reportName.setCellValueFactory(new PropertyValueFactory<YourReports, String>("reportName"));
            priority.setCellValueFactory(new PropertyValueFactory<YourReports, String>("priority"));
            attachments.setCellValueFactory(new PropertyValueFactory<YourReports, String>("attachments"));
            status.setCellValueFactory(new PropertyValueFactory<YourReports, String>("status"));
            description.setCellValueFactory(new PropertyValueFactory<YourReports, String>("description"));
            openedDate.setCellValueFactory(new PropertyValueFactory<YourReports, String>("openedDate"));
            daysOld.setCellValueFactory(new PropertyValueFactory<YourReports, Integer>("daysOld"));
            duplicates.setCellValueFactory(new PropertyValueFactory<YourReports, String>("duplicates"));
            bugSource.setCellValueFactory(new PropertyValueFactory<YourReports, String>("bugSource"));
            createdBy.setCellValueFactory(new PropertyValueFactory<YourReports, String>("createdBy"));

            //Add data inside of table
            yourReportsTable.setItems(yourReportsData);
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

        //Query to select all rows from the users table
        String selectAllUsernames = "SELECT " +
                                        "admin_id, " +
                                        "CONCAT(f_name, ' ', l_name), " +
                                        "total_assigned, " +
                                        "total_created " +
                                    "FROM users " +
                                    "ORDER BY admin_id;";

        try
        {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(selectAllUsernames);

            //Define data in an ObservableList and add data from query as you want to show inside table
            memberData = FXCollections.observableArrayList();
            while(queryResult.next())
            {
                memberData.add(new Users( queryResult.getInt("admin_id"),
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
            teamMemberTable.setItems(memberData);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void initProfileTab()
    {
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("createBugReport.fxml"));
        Parent root = loader.load();

        CreateBugReportController createBugReportController = loader.getController();
        createBugReportController.setBugTrackerController(this);

        Scene scene = new Scene(root);
        popupWindow.setScene(scene);
        popupWindow.show();
    }

    //Creates a popup window to change the status of a selected report
    public void changeStatusOnAction(ActionEvent event) throws IOException
    {
        Stage popupWindow = new Stage();

        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setResizable(false);
        popupWindow.setTitle("Change Status");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("changeStatus.fxml"));
        Parent root = loader.load();

        ChangeStatusController changeStatusController = loader.getController();
        changeStatusController.selectRow(yourReportsTable.getSelectionModel().getSelectedItems());
        changeStatusController.setBugTrackerController(this);
        changeStatusController.initChangeStatusWindowElements();

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
        updateBugsAndIssuesTable();
        updateYourReportsTable();
        updateTeamMembersTable();
    }

    private void updateBugsAndIssuesTable()
    {
        try
        {
            bugData.clear();

            //Query to retrieve data from bug_reports
            String selectAllBugReports = "SELECT " +
                                            "id, " +
                                            "name, " +
                                            "priority, " +
                                            "attachments, " +
                                            "status, " +
                                            "CONCAT(b.f_name, ' ', b.l_name)  AS assigned, " +
                                            "description, " +
                                            "opened_date, " +
                                            "DATEDIFF(NOW(), opened_date) AS days_old, " +
                                            "duplicates, " +
                                            "bug_source, " +
                                            "CONCAT(a.f_name, ' ', a.l_name) AS creator " +
                                        "FROM bug_reports " +
                                        "INNER JOIN users AS a " +
                                            "ON a.admin_id = bug_reports.created_by " +
                                        "INNER JOIN users AS b " +
                                            "ON b.admin_id = bug_reports.assigned_to " +
                                        "ORDER BY id;";


            PreparedStatement preparedStatement = connectDB.prepareStatement(selectAllBugReports);
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next())
            {
                bugData.add(new BugsAndIssues( queryResult.getInt("id"),
                                queryResult.getString("name"),
                                queryResult.getString("priority"),
                                queryResult.getString("attachments"),
                                queryResult.getString("status"),
                                queryResult.getString("assigned"),
                                queryResult.getString("description"),
                                queryResult.getString("opened_date"),
                                queryResult.getInt("days_old"),
                                queryResult.getString("duplicates"),
                                queryResult.getString("bug_source"),
                                queryResult.getString("creator")
                        )
                );
            }
            bugsAndIssuesTable.setItems(bugData);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void updateYourReportsTable()
    {
        try
        {
            yourReportsData.clear();

            String selectAllBugReports = "SELECT " +
                                            "id, " +
                                            "name, " +
                                            "priority, " +
                                            "attachments, " +
                                            "status, " +
                                            "description, " +
                                            "opened_date, " +
                                            "DATEDIFF(NOW(), opened_date) AS days_old, " +
                                            "duplicates, " +
                                            "bug_source, " +
                                            "CONCAT(f_name, ' ', l_name) AS creator " +
                                        "FROM bug_reports " +
                                        "INNER JOIN users " +
                                            "ON users.admin_id = bug_reports.created_by " +
                                        "WHERE assigned_to = " + LoginController.currentUser +
                                        " ORDER BY opened_date;";

            PreparedStatement preparedStatement = connectDB.prepareStatement(selectAllBugReports);
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next())
            {
                yourReportsData.add(new YourReports( queryResult.getInt("id"),
                                queryResult.getString("name"),
                                queryResult.getString("priority"),
                                queryResult.getString("attachments"),
                                queryResult.getString("status"),
                                queryResult.getString("description"),
                                queryResult.getString("opened_date"),
                                queryResult.getInt("days_old"),
                                queryResult.getString("duplicates"),
                                queryResult.getString("bug_source"),
                                queryResult.getString("creator")
                        )
                );
            }
            yourReportsTable.setItems(yourReportsData);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void updateTeamMembersTable()
    {
        try
        {
            memberData.clear();

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
                                        "FROM users " +
                                        "ORDER BY admin_id;";

            PreparedStatement preparedStatement = connectDB.prepareStatement(selectAllUsernames);
            ResultSet queryResult = preparedStatement.executeQuery();

            while (queryResult.next())
            {
                memberData.add(new Users( queryResult.getInt("admin_id"),
                                queryResult.getString("CONCAT(f_name, ' ', l_name)"),
                                queryResult.getInt("total_assigned"),
                                queryResult.getInt("total_created")
                        )
                );
            }
            teamMemberTable.setItems(memberData);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
}
