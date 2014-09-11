package blogics;

import exceptions.*;
import java.io.IOException;
import java.sql.*;
import javax.mail.MessagingException;
import services.database.*;
import services.javaxmail.Email;

/**
 * Manager per la gestione di un utente nel database
 */
public class UserManager {

    // <editor-fold defaultstate="collapsed" desc="CRUD">
    /**
     * Aggiungo un utente nel sistema con invio di una email di registrazione
     *
     * @param user L'utente che aggiungo al sistema
     * @throws NotFoundDBException Eccezione
     * @throws MessagingException Eccezione
     * @throws IOException Eccezione
     */
    public static void add(UserModel user)
            throws NotFoundDBException, MessagingException, IOException {

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
            String message = "Ti ringraziamo per esserti registrato al sito Star Cinema. "
                    + "<br><br>Ti ricordiamo che il tuo username è " + user.getUsername();
            String subject = "Registrazione STAR CINEMA";
            Email.send(user.getEmail(), subject, message);
        } catch (NotFoundDBException | MessagingException | IOException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Aggiorno i dati di un utente
     *
     * @param user L'utente di cui voglio aggiornare i dati
     * @throws NotFoundDBException Eccezione
     */
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
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Recupero un utente solo dal suo username.
     *
     * @param username Identificativo dell'utente che voglio recuperare
     * @return L'utente se è presente nel database, null se non è presente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static UserModel get(String username) 
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        UserModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `utenti` "
                    + "WHERE `username`='" + util.Conversion.getDatabaseString(username) + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new UserModel(result);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    /**
     * Recupero l'utente dal sistema con verifica username e password, usato per
     * autenticazione utente.
     *
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @return L'utente se è presente nel database, null se non è presente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
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
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return user;
    }
    // </editor-fold>

}
