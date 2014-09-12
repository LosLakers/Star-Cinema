package services.javaxmail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Guido Pio
 */
public class Email {

    private static Properties mailServerProperties;
    private static Session getMailSession;
    private static MimeMessage generateMailMessage;

    /**
     * Invio una email con un soggetto e un testo inseriti come parametri
     *
     * @param email La email a cui inviare il messaggio
     * @param subject Il soggetto della email
     * @param message Il messaggio della email
     * @throws AddressException Eccezione
     * @throws MessagingException Eccezione
     * @throws IOException Eccezione
     */
    public static void send(String email, String subject, String message)
            throws AddressException, MessagingException, IOException {

        try {
            //Read connection file
            BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Guido Pio\\Documents\\connection.txt"));
            String userid = "";
            String pass = "";
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] line = sCurrentLine.split(":");
                switch (line[0]) {
                    case "Username":
                        userid = line[1];
                        break;
                    case "Password":
                        pass = line[1];
                        break;
                }
            }
            //Step1     
            System.out.println("\n 1st ===> setup Mail Server Properties..");
            mailServerProperties = System.getProperties();
            mailServerProperties.put("mail.smtp.port", "587"); // TLS Port
            mailServerProperties.put("mail.smtp.auth", "true"); // Enable Authentication
            mailServerProperties.put("mail.smtp.starttls.enable", "true"); // Enable StartTLS
            System.out.println("Mail Server Properties have been setup successfully..");

            //Step2     
            System.out.println("\n\n 2nd ===> get Mail Session..");
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            generateMailMessage.setSubject(subject);
            String emailBody = message
                    + "<br><br>Cordiali Saluti,"
                    + "<br>Lo staff di Star Cinema";
            generateMailMessage.setContent(emailBody, "text/html");
            System.out.println("Mail Session has been created successfully..");

            //Step3     
            System.out.println("\n\n 3rd ===> Get Session and Send mail");
            Transport transport = getMailSession.getTransport("smtp");
            // Enter your correct gmail UserID and Password
            transport.connect("smtp.gmail.com", userid, pass);
            transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
            transport.close();
        } catch (IOException | MessagingException ex) {
            throw ex;
        }
    }
}
