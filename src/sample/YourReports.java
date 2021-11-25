package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class YourReports
{
    private SimpleIntegerProperty id;
    private SimpleStringProperty reportName;
    private SimpleStringProperty priority;
    private SimpleStringProperty attachments;   //TODO: Changes type to hold pictures
    private SimpleStringProperty status;
    private SimpleStringProperty description;
    private SimpleStringProperty openedDate;
    private SimpleIntegerProperty daysOld;
    private SimpleStringProperty duplicates;
    private SimpleStringProperty bugSource;

    public YourReports(int id, String reportName, String priority, String attachments, String status, String description, String openedDate, int daysOld, String duplicates, String bugSource)
    {
        this.id = new SimpleIntegerProperty(id);
        this.reportName = new SimpleStringProperty(reportName);
        this. priority = new SimpleStringProperty(priority);
        this.attachments = new SimpleStringProperty(attachments);
        this.status = new SimpleStringProperty(status);
        this.description = new SimpleStringProperty(description);
        this.openedDate = new SimpleStringProperty(openedDate);
        this.daysOld = new SimpleIntegerProperty(daysOld);
        this.duplicates = new SimpleStringProperty(duplicates);
        this.bugSource = new SimpleStringProperty(bugSource);
    }

    public int getId()
    {
        return id.get();
    }

    public String getReportName()
    {
        return reportName.get();
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
}
