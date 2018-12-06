package edu.uh.tech.cis3368.finalproject;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**  The Employee class is used to create new instances of Employees
    that are then used to display the database data in the table.
 */

public class Employee {
    private SimpleIntegerProperty Id;
    SimpleStringProperty Name;
    SimpleStringProperty Position;
    SimpleDoubleProperty Rate;
    SimpleStringProperty Date;

    /**
     * Default constructor initializes values to 0, "", or null
     */
    Employee(){
        Id = new SimpleIntegerProperty(0);
        Name = new SimpleStringProperty("");
        Position = new SimpleStringProperty("");
        Rate = new SimpleDoubleProperty(0);
        Date = new SimpleStringProperty("");
    }

    /**
     * Constructor with arguments will take necessary info to create a new Employee
     * @param id
     * @param name
     * @param position
     * @param rate
     * @param date
     */
    Employee(Integer id, String name, String position, Double rate, String date){
        Id = new SimpleIntegerProperty(id);
        Name = new SimpleStringProperty(name);
        Position = new SimpleStringProperty(position);
        Rate = new SimpleDoubleProperty(rate);
        Date = new SimpleStringProperty(date);
    }

    public int getId() {
        return Id.get();
    }
    public String getName() {
        return Name.get();
    }
    public String getPosition() {
        return Position.get();
    }
    public double getRate() {
        return Rate.get();
    }
    public String getDate() {
        return Date.get();
    }

    public void setRate(double rate) {
        this.Rate.set(rate);
    }
}
