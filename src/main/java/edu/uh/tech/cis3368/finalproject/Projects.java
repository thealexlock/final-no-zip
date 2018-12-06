package edu.uh.tech.cis3368.finalproject;

/**
 * Project class will have constructors and methods that allow you to create new instances
 * and that can then be displayed in observable lists.
 */

public class Projects {
   private int jobID;
   private String preProduction;
   private String production;
   private String closeOut;
   private String archived;

   //default constructor
   Projects(){

   }
   //overloaded constructor
    Projects(int jobID,String preProduction, String production, String closeOut, String archived){
       this.jobID = jobID;
       this.preProduction = preProduction;
       this.production = production;
       this.closeOut = closeOut;
       this.archived = archived;
    }
   //setters
   public void setJobID(int value){
       this.jobID = value;
   }
   public void setPreProduction(String value){
       this.preProduction = value;
   }
   public void setProduction(String value){
       this.production = value;
   }
   public void setCloseOut(String value){
       this.closeOut = value;
   }
   public void setArchived(String value){
       this.archived = value;
   }
   // getters
   public int getJobID(){
       return this.jobID;
   }
   public String getPreProduction(){
       return this.preProduction;
   }
   public String getProduction(){
       return this.production;
   }
   public String getCloseOut(){
       return this.closeOut;
   }
   public String getArchived(){
       return this.archived;
   }
}
