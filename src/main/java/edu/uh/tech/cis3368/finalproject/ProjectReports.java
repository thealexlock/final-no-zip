package edu.uh.tech.cis3368.finalproject;

import java.sql.Date;

/**
 * Project Reports class will create an object similar to Project.java that then can be accessed for reports.
 */

public class ProjectReports {
    private int jobID;
    private String customerName;
    private String cost;
    private Date date;

    // default constructor
    ProjectReports(){

    }

    //overloaded constructor
    ProjectReports(int jobID, Date date, String customerName, String cost){
        this.jobID = jobID;
        this.date = date;
        this.customerName = customerName;
        this.cost = cost;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }
}
