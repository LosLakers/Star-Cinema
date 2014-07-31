package blogics;

import exceptions.*;
import java.sql.*;
import services.database.*;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class UserManager {

    // <editor-fold defaultstate="collapsed" desc="CRUD">
    public static void add(UserModel user)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            String sql = "INSERT INTO `utenti` "
                    + "(`username`,`password`,`name`,`surname`,`email`,`credit_card`) "
                    + "VALUES "
                    + "('" + util.Conversion.getDatabaseString(user.getUsername()) + "',"
                    + "'" + util.Conversion.getDatabaseString(user.getPassword()) + "',"
                    + "'" + util.Conversion.getDatabaseString(user.getName()) + "',"
                    + "'" + util.Conversion.getDatabaseString(user.getSurname()) + "',"
                    + "'" + util.Conversion.getDatabaseString(user.getEmail()) + "',"
                    + "'" + user.getCreditcard() + "');";
            database.modify(sql);
            database.commit();

            // invio mail di registrazione non funzionante
            /*String from = "";
            String to = user.getEmail();
            String host = "smtp.mail.apac.microsoftonline.com";
            Properties props = System.getProperties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "smtp.live.com");
            props.put("mail.smtp.user", "");
            props.put("mail.smtp.password", "");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            Session session = Session.getInstance(props);
            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);

                // Set From: header field of the header.
                message.setFrom(new InternetAddress(from));

                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));

                // Set Subject: header field
                message.setSubject("Registrazione Star Cinema");

                // Now set the actual message
                message.setText("Registrazione Completata con Successo");

                // Send message
                Transport.send(message);
            } catch (MessagingException mex) {
                mex.printStackTrace();
            }*/
        } catch (NotFoundDBException ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    public static void update(UserModel user)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            String sql = "UPDATE `utenti` "
                    + "SET `name`='" + util.Conversion.getDatabaseString(user.getName()) + "',"
                    + "`surname`='" + util.Conversion.getDatabaseString(user.getSurname()) + "',"
                    + "`email`='" + util.Conversion.getDatabaseString(user.getEmail()) + "',"
                    + "`credit_card`='" + user.getCreditcard() + "' "
                    + "WHERE `username`='" + util.Conversion.getDatabaseString(user.getUsername()) + "'";
            database.modify(sql);
            database.commit();
            database.close();
        } catch (NotFoundDBException ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    /* metodo per ottenere l'user dal database */
    public static UserModel get(String username, String password)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        UserModel user = null;
        try {
            String sql = " SELECT * "
                    + " FROM `utenti` "
                    + " WHERE "
                    + " `username`= '" + util.Conversion.getDatabaseString(username) + "'";

            ResultSet result = database.select(sql);
            if (result.next()) { // true c'è un'altra riga
                user = new UserModel(result);
            }
            result.close();
            database.commit();

            /* se non ho trovato l'utente o la password non combacia allora ritorno null */
            if (user == null || !user.getPassword().equals(password)) {
                throw new NotFoundDBException("");
            }
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return user;
    }

    // </editor-fold>
}
