/*
 * BadRecipientException.java
 *
 * Created on 26 maggio 2000, 15.26
 */

package services.mailservice.exception;

import services.errorservice.*;
import services.mailservice.*;
import services.logservice.*;

import java.io.*;

/** 
 * Indirizzo del destinatario errato.
 * <p>
 * Se, nel tentativo di inviare un messaggio di e-mail viene verificata
 * la presenza di un indirizzo del destinatario scorretto, viene scatenata
 * questa eccezione.
 *
 * @author Fabio Tarantino
 * 
 * @see BadDeliveryException
 * @see MException
 * @see GeneralError
 *
 */


public class BadRecipientException extends MException implements GeneralException {

  /** 
   * Costruttore.
   * <p>
   * Costruttore senza parametri.
   *
   */
  
  public BadRecipientException() {}
  
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
  
  public BadRecipientException(String msg, String sSMTPGate) {
    super("General Exception:" + msg, sSMTPGate);
    this.logMessage="General Exception \n"+msg+"\n";    
  }
  
  public String getLogMessage() {
    return logMessage;
  }
  
}

