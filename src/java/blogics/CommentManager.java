package blogics;

import exceptions.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.mail.MessagingException;
import services.database.*;
import services.javaxmail.Email;

/**
 * Manager per la gestione dei commenti
 */
public class CommentManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Aggiunta di un commento nel database
     *
     * @param model Il commento da aggiungere
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static void add(CommentModel model)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        try {
            /* controllo esistenza film */
            String sql = "SELECT `id_film` "
                    + "FROM `film`"
                    + "WHERE `id_film`='" + model.getId_film() + "'";
            ResultSet resultSet = database.select(sql);
            if (!resultSet.next()) {
                throw new SQLException();
            }
            resultSet.close();

            /* controllo esistenza user */
            sql = "SELECT `username` "
                    + "FROM `utenti`"
                    + "WHERE `username`='" + util.Conversion.getDatabaseString(model.getUsername()) + "'";
            resultSet = database.select(sql);
            if (!resultSet.next()) {
                throw new SQLException();
            }
            resultSet.close();

            /* inserisco il commento nel database */
            sql = "INSERT INTO `commenti`"
                    + "(`voto`, `giudizio`, `username`, `id_film`) "
                    + "VALUES "
                    + "('" + model.getVoto() + "',"
                    + "'" + util.Conversion.getDatabaseString(model.getGiudizio()) + "',"
                    + "'" + util.Conversion.getDatabaseString(model.getUsername()) + "',"
                    + "'" + model.getId_film() + "');";
            resultSet = database.modifyPK(sql);
            if (resultSet.next()) {
                model.setId_commento(resultSet.getInt(1));
                resultSet.close();
            }
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Aggiornamento di un commento
     *
     * @param model Il commento da aggiornare
     * @throws NotFoundDBException Eccezione
     */
    public static void update(CommentModel model)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            /* aggiorno il film nel database */
            String sql = "UPDATE `commenti` "
                    + "SET "
                    + "`voto`='" + model.getVoto() + "',"
                    + "`giudizio`='" + util.Conversion.getDatabaseString(model.getGiudizio()) + "' "
                    + "WHERE `id_commento`='" + model.getId_commento() + "';";
            database.modify(sql);
            database.commit();
        } catch (NotFoundDBException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Eliminazione di un commento
     *
     * @param id_commento Identificativo del commento nel database
     * @throws NotFoundDBException Eccezione
     */
    public static void delete(int id_commento)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            /* elimino il film dal database */
            String sql = "DELETE FROM `commenti` "
                    + "WHERE `id_commento`='" + id_commento + "';";
            database.modify(sql);
            database.commit();
        } catch (NotFoundDBException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Eliminazione di un commento con notifica via email
     *
     * @param commento Il commento da eliminare
     * @param userEmail L'email dell'utente
     * @param titolo Il titolo del film
     * @throws NotFoundDBException Eccezione
     * @throws MessagingException Eccezione
     * @throws IOException Eccezione
     */
    public static void delete(CommentModel commento, String userEmail, String titolo)
            throws NotFoundDBException, MessagingException, IOException {

        DataBase database = DBService.getDataBase();
        try {
            /* elimino il film dal database */
            String sql = "DELETE FROM `commenti` "
                    + "WHERE `id_commento`='" + commento.getId_commento() + "';";
            database.modify(sql);
            database.commit();

            String message = "Il tuo commento al film " + titolo
                    + " è stato elimato da un amministratore perchè viola le regole del sito";
            String subject = "Eliminazione commento";
            Email.send(userEmail, subject, message);
        } catch (NotFoundDBException | MessagingException | IOException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Recupero di un commento
     *
     * @param id_commento Identificativo del commento
     * @return Il commento se presente nel database, null se non lo è
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static CommentModel get(int id_commento)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        CommentModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `commenti` "
                    + "WHERE `id_commento`='" + id_commento + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new CommentModel(result);
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
     * Recupero di un commento
     *
     * @param user Username dell'utente
     * @param id_film Identificativo del film
     * @return Il commento se è presente nel database, null se non lo è
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static CommentModel get(String user, int id_film)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        CommentModel commento = null;
        try {
            String sql = "SELECT * "
                    + "FROM `commenti` "
                    + "WHERE "
                    + "`id_film`='" + id_film + "' AND "
                    + "`username`='" + util.Conversion.getDatabaseString(user) + "';";
            ResultSet resultSet = database.select(sql);
            if (resultSet.next()) {
                commento = new CommentModel(resultSet);
            }
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return commento;
    }

    /**
     * Recupero di una lista di commenti associati ad un film
     *
     * @param id_film Identificativo del film
     * @return La lista dei commenti
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static List<CommentModel> getCommenti(int id_film)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<CommentModel> model = model = new ArrayList<>();
        try {
            // recupero la lista dei commenti per un dato film
            String sql = "SELECT * "
                    + "FROM `commenti` "
                    + "WHERE "
                    + "`id_film`='" + id_film + "';";
            ResultSet resultSet = database.select(sql);
            // creo il vettore con i commenti trovati
            while (resultSet.next()) {
                CommentModel tmp = new CommentModel(resultSet);
                model.add(tmp);
            }
            resultSet.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }
    // </editor-fold>
}
