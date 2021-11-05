package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Users
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleIntegerProperty inprogressIssues;
    private SimpleIntegerProperty completedIssues;
    private SimpleIntegerProperty closedIssues;
    private SimpleIntegerProperty totalAssigned;
    private SimpleIntegerProperty assignedReports;
    private SimpleIntegerProperty totalCreated;
    private SimpleIntegerProperty createdReports;

    Users(int id, String name, int inprogressIssues, int completedIssues, int closedIssues, int totalAssigned, int assignedReports, int totalCreated, int createdReports)
    {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.inprogressIssues = new SimpleIntegerProperty(inprogressIssues);
        this.completedIssues = new SimpleIntegerProperty(completedIssues);
        this.closedIssues = new SimpleIntegerProperty(closedIssues);
        this.totalAssigned = new SimpleIntegerProperty(totalAssigned);
        this.assignedReports = new SimpleIntegerProperty(assignedReports);
        this.totalCreated = new SimpleIntegerProperty(totalCreated);
        this.createdReports = new SimpleIntegerProperty(createdReports);
    }

    public int getId()
    {
        return id.get();
    }

    public String getName()
    {
        return name.get();
    }

    public int getInprogressIssues() {
        return inprogressIssues.get();
    }

    public int getCompletedIssues() {
        return completedIssues.get();
    }

    public int getClosedIssues() {
        return closedIssues.get();
    }

    public int getTotalAssigned() {
        return totalAssigned.get();
    }

    public int getAssignedReports() {
        return assignedReports.get();
    }

    public int getTotalCreated() {
        return totalCreated.get();
    }

    public int getCreatedReports() {
        return createdReports.get();
    }
}
