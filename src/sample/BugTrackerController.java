package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        initBugsAndIssuesTable();
        initTeamMembersTable();
    }

    //Adds data to the Bugs and Issues table
    private void initBugsAndIssuesTable() {
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

        String selectAllBugReports = "SELECT " +
                                        "name, " +
                                        "priority, " +
                                        "attachments, " +
                                        "status, " +
                                        "assigned_to, " +
                                        "description, " +
                                        "opened_date, " +
                                        "DATEDIFF(NOW(), opened_date), " +
                                        "duplicates, " +
                                        "bug_source, " +
                                        "created_by " +
                                    "FROM bug_reports";

        try {
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
                                            queryResult.getInt("assigned_to"),
                                            queryResult.getString("description"),
                                            queryResult.getString("opened_date"),
                                            queryResult.getInt("DATEDIFF(NOW(), opened_date)"),
                                            queryResult.getString("duplicates"),
                                            queryResult.getString("bug_source"),
                                            queryResult.getInt("created_by")
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
                createdBy.setCellValueFactory(new PropertyValueFactory<BugsAndIssues, Integer>("createdBy"));

                //Add data inside of table
                bugsAndIssuesTable.setItems(data);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            e.getCause();
            System.out.println("ERROR LOADING DATA!");
        }
    }

    //Adds data to the Team Members Table
    private void initTeamMembersTable()
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
                                        "inprogress_issues, " +
                                        "completed_issues, " +
                                        "closed_issues, " +
                                        "total_assigned, " +
                                        "assigned_reports, " +
                                        "total_created, " +
                                        "created_reports " +
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
                                    queryResult.getInt("inprogress_issues"),
                                    queryResult.getInt("completed_issues"),
                                    queryResult.getInt("closed_issues"),
                                    queryResult.getInt("total_assigned"),
                                    queryResult.getInt("assigned_reports"),
                                    queryResult.getInt("total_created"),
                                    queryResult.getInt("created_reports")
                                    )
                );
            }

            //Associate data with columns
            id.setCellValueFactory(new PropertyValueFactory<Users, Integer>("id"));
            name.setCellValueFactory(new PropertyValueFactory<Users, String>("name"));
            inprogressIssues.setCellValueFactory(new PropertyValueFactory<Users, Integer>("inprogressIssues"));
            completedIssues.setCellValueFactory(new PropertyValueFactory<Users, Integer>("completedIssues"));
            closedIssues.setCellValueFactory(new PropertyValueFactory<Users, Integer>("closedIssues"));
            totalAssigned.setCellValueFactory(new PropertyValueFactory<Users, Integer>("totalAssigned"));
            assignedReports.setCellValueFactory(new PropertyValueFactory<Users, Integer>("assignedReports"));
            totalCreated.setCellValueFactory(new PropertyValueFactory<Users, Integer>("totalCreated"));
            createdReports.setCellValueFactory(new PropertyValueFactory<Users, Integer>("createdReports"));


            //Add data inside of table
            teamMemberTable.setItems(data);

        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
            System.out.println("ERROR LOADING DATA!");
        }
    }

    //Adds data to the features table
    private void initFeaturesTable()
    {

    }
}
