/*
 * MService.java
 *
 * Created on 2 maggio 2000, 16.12
 */

package services.mailservice;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import global.*;
import services.mailservice.exception.*;
import services.logservice.*;

/** 
 * Servizio di invio delle mail.
 * <p>
 * Istanziando la classe <code>MService</code> è possibile inviare mail
 * con il metodo <code>Send</code>
 * <p>
 * Il Mail Service gestisce un sistema di load balancing fra 2 mail server.
 *
 * @author  Fabio Tarantino
 * 
 */

public class MService {

  private Properties oProps;
  private Session oSession;
  private Transport oTransport;
  
  /** 
   * Il mail gateway selezionato.
   * <p>
   * Una volta istanziato, il mail service seleziona un mail gateway
   * per l'invio delle mail.
   *
   */  
  
  public String sMailGateWay;
 
  private int numGateWay=Constants.MAIL_GATEWAYS.length;
  private String[] sGateWay=Constants.MAIL_GATEWAYS;
//  private String[] sGateWay3={"151.99.250.6"};
//  private String[] sGateWay={"151.99.250.6","1.1.1.1","151.99.250.6"};

  /** 
   * Instanzia il Mail Service.
   * <p>
   * Quando la classe viene istanziata viene verificata la
   * presenza di mail server disponibili se non trova il primo va in cascata
   *
   * @exception BadDeliveryException Impossibile inizializzare un Mail Gateway
   *
   * @see services.mailservice.exception
   *
   */

  public MService() throws BadDeliveryException {


      if ( !Constants.ENABLE_MAIL_SERVICE.equals("NO") ) {




    int iMailer;

    for(iMailer=0;iMailer<numGateWay;iMailer++){

        sMailGateWay=sGateWay[iMailer];
        if (InitMailer(sGateWay[iMailer])) return;
    }

    // se arrivo qui significa che non ho mail server a disposizione
    throw new BadDeliveryException("MService: MService(): Mail Server non disponibili - mancata connessione");
      }
  }

  /**
   * Instanzia il Mail Service per uno specifico Mail Gateway.
   * <p>
   * Viene istanziato il servizio con il mail gateway
   * specificato.
   *
   * @exception Exception Impossibile inizializzare il Mail Gateway
   *
   * @deprecated Utilizzare {@link #MService()}
   *
   */

  public MService(String sSMPTGateWay) throws Exception {

    sMailGateWay=new String(sSMPTGateWay);
    InitMailer(sSMPTGateWay);
  }

  /** Private initialisation */
  private boolean InitMailer(String sSMPTGateWay) {

     try{
        oProps = new Properties();
        oProps.put("mail.smtp.host", sSMPTGateWay);
        oSession = Session.getInstance(oProps, null);
        oTransport = oSession.getTransport(oSession.getProvider("smtp"));
        oTransport.connect();
        if(!oTransport.isConnected()){
            ErrorLog.warningLog("MService: MService(): Mail Server " + sMailGateWay + " non operativo. Tento un'altro Gateway.");
            return false;
        }
//        util.Debug.println("Ottenuto server "+ sSMPTGateWay);
     } catch (Exception e1) {
       e1.printStackTrace();
       ErrorLog.warningLog("MService: MService(): Mail Server " + sMailGateWay + " non operativo. Tento un'altro Gateway.");
       return false;
     }

     return true;
  }
  /**
  * @deprecated Usare {@link #send(String, String, String, String, boolean)}
  */
  public void send(String sFrom, String sTo, String sSubject, String sBody)
  throws BadRecipientException,BadDeliveryException {

    this.send(sFrom, sTo, sSubject, sBody, false,null);

  }

  /**
  * @deprecated Usare {@link #send(String, String, String, String, boolean)}
  */
  public void send(String sFrom, String sTo, String sSubject, String sBody,boolean html)
  throws BadRecipientException,BadDeliveryException {

    this.send(sFrom, sTo, sSubject, sBody, html,null);

  }

  /**
   * Invia la mail.
   *
   * @param sFrom e-mail address del mittente del messaggio.
   * @param sTo e-mail address del destinatario del messaggio.
   * @param sSubject soggetto della e-mail.
   * @param sBody corpo della e-mail.
   * @param HTML Specifica se il testo della mail e' in formato HTML o testo.
   *
   * @exception BadRecipientException L'indirizzo del destinatario non è corretto.
   * @exception BadDeliveryException Non è possibile inizializzare un
   * mail gatway.
   *
   */
  public void send(String sFrom, String sTo,String sSubject,String sBody, boolean HTML,File attachment)
    throws BadRecipientException,BadDeliveryException {

    util.Debug.println("sending from("+sFrom+") to("+sTo+") subjet("+sSubject+") html("+HTML+")");
    try{
      if (attachment!=null) sendMessageWithAttachment(sFrom, sTo,sSubject,sBody,attachment);
      else {
        if (HTML) sendHTMLMessage(sFrom, sTo,sSubject,sBody);
        else sendMessage(sFrom, sTo,sSubject,sBody);
      }
    } catch (Exception e1) {
      if (oTransport.isConnected())
        throw new BadRecipientException("MService: Send(): Destinatario di Posta errato: mail non spedita " + e1, sMailGateWay);
      else {
        ErrorLog.warningLog("MService: Send(): Mail Server " + sMailGateWay + " non operativo. Tento un'altro Gateway.");
        int i=0;
        if (sMailGateWay == sGateWay[i])
         i++;
        try{
          InitMailer(sGateWay[i]);
          sMailGateWay=sGateWay[i];
          sendMessage(sFrom, sTo,sSubject,sBody);
        } catch (SendFailedException es) {
          throw new BadRecipientException("MService: Send(): Destinatario di Posta errato: mail non spedita " + es, sMailGateWay);
        } catch(Exception e2){
          throw new BadDeliveryException("MService: Send(): Mail Server non disponibili - mancata spedizione" + e2 + e1, sMailGateWay);
        }
      }
    }
  }

  /* Invia il messaggio */
  private void sendMessage(String sFrom, String sTo,String sSubject,String sBody) throws Exception {

      if ( Constants.ENABLE_MAIL_SERVICE.equals("NO") ){
           ErrorLog.warningLog("Tentativo di invio, Mail disabilitata: \n From:"+sFrom+"\n To:"+ sTo +"\n Subject: "+sSubject + "\n Body:\n"+sBody);
          return;
      }
    if ( Constants.ENABLE_MAIL_SERVICE.equals("TEST") ) sTo=Constants.TEST_MAIL;

    MimeMessage msg = new MimeMessage(oSession);
    msg.setFrom(new InternetAddress(sFrom));
    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(sTo));
    msg.setSubject(sSubject, "iso-8859-1");
    msg.setSentDate(new Date());
    msg.setText(sBody, "iso-8859-1");

    oTransport.send(msg);

  }



  /* Invia la mail in formato HTML */
  private void sendHTMLMessage(String sFrom, String sTo,String sSubject,String sBody)
  throws Exception {

      if ( Constants.ENABLE_MAIL_SERVICE.equals("NO") ) {
           ErrorLog.warningLog("Tentativo di invio, Mail disabilitata: \n From:"+sFrom+"\n To:"+ sTo +"\n Subject: "+sSubject + "\n Body:\n"+sBody);
           return;
      }
    if ( Constants.ENABLE_MAIL_SERVICE.equals("TEST") ) sTo=Constants.TEST_MAIL;

    Message msg = new MimeMessage(oSession);
    // set the from
    InternetAddress from = new InternetAddress(sFrom);
    msg.setFrom(from);
    InternetAddress[] address = {new InternetAddress(sTo)};
    msg.setRecipients(Message.RecipientType.TO, address);
    msg.setSubject(sSubject);
    MimeBodyPart mbp1 = new MimeBodyPart();
    mbp1.setContent(sBody, "text/HTML");
    // create the Multipart and its parts to it
    Multipart mp = new MimeMultipart();
    mp.addBodyPart(mbp1);
    // add the Multipart to the message
    msg.setContent(mp);
    oTransport.send(msg);
  }

  /* Invia la mail con un attach */
  private void sendMessageWithAttachment(String sFrom, String sTo,String sSubject,String sBody,File attachment)
  throws Exception {

    if ( Constants.ENABLE_MAIL_SERVICE.equals("NO") ) return;
    if ( Constants.ENABLE_MAIL_SERVICE.equals("TEST") ) sTo=Constants.TEST_MAIL;

    Message msg = new MimeMessage(oSession);
    // set the from
    InternetAddress from = new InternetAddress(sFrom);
    msg.setFrom(from);
    InternetAddress[] address = {new InternetAddress(sTo)};
    msg.setRecipients(Message.RecipientType.TO, address);
    msg.setSubject(sSubject);
    MimeBodyPart mbp1 = new MimeBodyPart();
    mbp1.setContent(sBody, "text/HTML");
    MimeBodyPart mbp2 = new MimeBodyPart();
    FileDataSource fds=new FileDataSource(attachment);
    mbp2.setDataHandler(new DataHandler(fds));
    mbp2.setFileName(attachment.getName());

    // create the Multipart and its parts to it
    Multipart mp = new MimeMultipart();
    mp.addBodyPart(mbp1);
    mp.addBodyPart(mbp2);
    // add the Multipart to the message
    msg.setContent(mp);
    oTransport.send(msg);
  }

  /* Invia la mail html con test alternativo */
  public void sendHTMLAndText(String sFrom, String sTo,String sSubject,String sBodyHtml,String sBodyText)
  throws Exception {

    if ( Constants.ENABLE_MAIL_SERVICE.equals("NO") ) return;
    if ( Constants.ENABLE_MAIL_SERVICE.equals("TEST") ) sTo=Constants.TEST_MAIL;

    Message msg = new MimeMessage(oSession);
    // set the from
    InternetAddress from = new InternetAddress(sFrom);
    msg.setFrom(from);
    InternetAddress[] address = {new InternetAddress(sTo)};
    msg.setRecipients(Message.RecipientType.TO, address);
    msg.setSubject(sSubject);
    MimeBodyPart mbp1 = new MimeBodyPart();
    MimeBodyPart mbp2 = new MimeBodyPart();
    mbp1.setContent(sBodyText,"text/plain");
    mbp2.setContent(sBodyHtml,"text/HTML");
    // create the Multipart and its parts to it
    Multipart mp = new MimeMultipart();
    mp.addBodyPart(mbp1);
    mp.addBodyPart(mbp2);
    // add the Multipart to the message
    msg.setContent(mp);
    oTransport.send(msg);
  }


}