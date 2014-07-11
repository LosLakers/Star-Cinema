 
package exceptions;

import java.io.*;

import global.*;
import services.database.DataBase;
import services.errorservice.*;
import services.logservice.*;
//import services.mailservice.*;

public class NotFoundDBException extends DBException /*implements FatalError*/ {
  
  
  public NotFoundDBException(String msg, DataBase database) {
    
    super("Fatal Error: " + msg);
    this.database = database;
       
    this.logMessage = "Fatal Error\n" + msg + "\n";
    
    ByteArrayOutputStream stackTrace=new ByteArrayOutputStream();
    this.printStackTrace(new PrintWriter(stackTrace, true));        
    
    this.logMessage = this.logMessage + stackTrace.toString();        
    
  }    
  
  public NotFoundDBException(String msg) {    
    this(msg, null);            
  }  
 
 /** Ritorna il messaggio di Errore corrispondente al Fatal Error **/   
  public String getLogMessage() {    
    return logMessage;    
  }
   
 /** Chiamata di RollBack (implementazione classe Astratta FatalError) **/   
  public void makeRollBack() {          
    if (database != null) this.database.rollBack();           
  }
  
  /** Chiamata di invio Mail (implementazione classe Astratta FatalError) */  
  /*public void mailError() {
                                   
    try {
      MService mailService = new MService();       
      String from = "error@error.com";
      String to = Constants.APPLICATION_MANAGER_MAIL;
      String subject = "Fatal Error Notification";
      mailService.send(from, to, subject, logMessage, false, null);
    } catch (Exception e) {            
      e.printStackTrace();
    }                    
  }*/

}