package sample;


import javafx.beans.property.*;

public class BugsAndIssues
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty bugTitle;
    private SimpleStringProperty priority;
    private SimpleStringProperty attachments;   //TODO: Changes type to hold pictures
    private SimpleStringProperty status;
    private SimpleStringProperty assignedTo;
    private SimpleStringProperty description;
    private SimpleStringProperty openedDate;
    private SimpleIntegerProperty daysOld;
    private SimpleStringProperty duplicates;
    private SimpleStringProperty bugSource;
    private SimpleStringProperty createdBy;

    public BugsAndIssues(int id, String bugTitle, String priority, String attachments, String status, String assignedTo, String description, String openedDate, int daysOld, String duplicates, String bugSource, String createdBy)
    {
        this.id = new SimpleIntegerProperty(id);
        this.bugTitle = new SimpleStringProperty(bugTitle);
        this. priority = new SimpleStringProperty(priority);
        this.attachments = new SimpleStringProperty(attachments);
        this.status = new SimpleStringProperty(status);
        this.assignedTo = new SimpleStringProperty(assignedTo);
        this.description = new SimpleStringProperty(description);
        this.openedDate = new SimpleStringProperty(openedDate);
        this.daysOld = new SimpleIntegerProperty(daysOld);
        this.duplicates = new SimpleStringProperty(duplicates);
        this.bugSource = new SimpleStringProperty(bugSource);
        this.createdBy = new SimpleStringProperty(createdBy);
    }

    public int getId()
    {
        return id.get();
    }

    public String getBugTitle()
    {
        return bugTitle.get();
    }

    public String getPriority()
    {
        return priority.get();
    }

    public String getAttachments()
    {
        return attachments.get();
    }

    public String getStatus()
    {
        return status.get();
    }

    public String getAssignedTo()
    {
        return assignedTo.get();
    }

    public String getDescription()
    {
        return description.get();
    }

    public String getOpenedDate()
    {
        return openedDate.get();
    }

    public int getDaysOld()
    {
        return daysOld.get();
    }

    public String getDuplicates()
    {
        return duplicates.get();
    }

    public String getBugSource()
    {
        return bugSource.get();
    }

    public String getCreatedBy()
    {
        return createdBy.get();
    }
}
