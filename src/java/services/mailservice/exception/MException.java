/*
 * MException.java
 *
 * Created on 26 maggio 2000, 16.42
 */

package services.mailservice.exception;

import services.mailservice.*;
import services.logservice.*;

/** 
 * Errore del Mail service.
 * <p>
 * Superclasse per tutte le eccezioni del Mail Service.
 *
 * @author Fabio Tarantino
 *
 * @see BadDeliveryException
 * @see BadRecipientException
 * @see services.mailservice
 *
 */

public class MException extends Exception {
  
  /** 
   * Gateway connesso all'eccezione scatanata.
   * <p>
   * Ogni errore del servizio di mail viene scatenato contestualmente
   * ad un mail gateway. Questa variabile contiene il Gateway interessato.
   *
   */    
  
  protected String sSMTPGate;
  
  /** 
   * Messaggio di errore.
   * <p>
   * Contiene il messaggio di errore da scivere sul file di log.
   *
   */      
  
  protected String logMessage;

  /** 
   * Costruttore.
   * <p>
   * Costruttore senza parametri.
   *
   */
  
  public MException() {
  }
  
  /** 
   * Costruttore con messagio di errore e mail gateway.
   * <p>
   * Viene istanziato l'errore del mail service specificando il messaggio
   * di errore e il mail gateway interessato.
   * <p>
   * @param msg Messaggio di errore
   * @param sSMTPGate Mail gateway interessato dall'errore.
   *
   */        
  
  public MException(String msg, String sSMTPGate) {
    super(msg);
    this.sSMTPGate=sSMTPGate;
    logMessage="Created Exception: Gateway:"+ sSMTPGate + " Message: "+ msg;
    log();
  }
  
  /** 
   * Aggiorna il file di log degli errori del Mail Service.
   * <p> 
   * 
   * @see services.logservice.ErrorLog
   *
   */        
  
  public void log() {
    ErrorLog.mailErrorLog(logMessage);    
  }  
  
}

