/*
 * BadDeliveryException.java
 *
 * Created on 26 maggio 2000, 17.24
 */


package services.mailservice.exception;

import services.errorservice.*;
import services.mailservice.*;
import services.logservice.*;

/** 
 * Impossibile inviare il messaggio di e-mail.
 * <p>
 * Errore scatenato nell'impossbilità di inviare una mail, in genere
 * a causa di una indisponibilità del mail gateway.
 *
 * @author Fabio Tarantino
 * 
 * @see BadRecipientException
 * @see MException
 * @see FatalError
 *
 */

public class BadDeliveryException extends MException implements FatalError {

  /** 
   * Costruttore.
   * <p>
   * Costruttore senza parametri.
   *
   */
  public BadDeliveryException() {}
  

  /** 
   * Costruttore con messagio di errore e mail gateway.
   * <p>
   * Viene istanziato l'errore specificando il messaggio
   * di errore e il mail gateway interessato.
   * <p>
   * @param msg Messaggio di errore
   * @param sSMTPGate Mail gateway interessato dall'errore.
   *
   */        
  
  public BadDeliveryException(String msg, String sSMTPGate) {
    super("Fatal Error:" + msg, sSMTPGate);
    this.logMessage="FATAL\n"+msg+"\n";
 
  }
    
   public BadDeliveryException(String msg) {
    super("Fatal Error:" + msg, "ALL");
    this.logMessage="FATAL\n"+msg+"\n";
 
  } 
  public String getLogMessage() {
    return logMessage;
  }  
  
  public void mailError() {
    /* Non è significativo inviare una mail per questo errore. */
  }
  
  public void makeRollBack() {
    /* Nessuna RollBack è necessaria per questo tipo di errore. */
  }
  
}

