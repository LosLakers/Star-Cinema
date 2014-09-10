package blogics;

import exceptions.*;
import java.sql.*;
import services.database.*;

/**
 * Manager per la gestione di un Abbonamento
 */
public class SubscriptionManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Aggiungo un nuovo abbonamento nel sistema associato ad un utente e con un
     * certo numero di posti di ingresso
     *
     * @param username Identificativo dell'utente
     * @param ingressi Numero di ingressi del nuovo abbonamento
     * @return Id nel database dell'abbonamento
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static int add(String username, int ingressi)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        int id = 0;
        try {
            String sql = "INSERT INTO `abbonamenti`(`ingressi_disp`, `username`) "
                    + "VALUES "
                    + "('" + ingressi + "',"
                    + "'" + util.Conversion.getDatabaseString(username) + "')";
            ResultSet result = database.modifyPK(sql);
            if (result.next()) {
                id = result.getInt(1);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return id;
    }

    /**
     * Aggiorno un abbonamento con username passato come parametro con un nuovo
     * numero di ingressi disponibili
     *
     * @param username Identificativo dell'utente
     * @param ingressi Nuovo numero di ingressi dell'abbonamento
     * @throws NotFoundDBException Eccezione
     */
    public static void update(String username, int ingressi)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            String sql = "UPDATE `abbonamenti` "
                    + "SET `ingressi_disp`='" + ingressi + "' "
                    + "WHERE `username`='" + util.Conversion.getDatabaseString(username) + "'";
            database.modify(sql);
            database.commit();
        } catch (NotFoundDBException ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Recupero l'abbonamento associato allo username usato come parametro
     *
     * @param username Utente di cui voglio trovare l'abbonamento
     * @return L'abbonamento se viene trovato, null se inesistente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static SubscriptionModel get(String username)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        SubscriptionModel subscription = null;
        try {
            String sql = "SELECT * "
                    + "FROM `abbonamenti` "
                    + "WHERE `username`='" + util.Conversion.getDatabaseString(username) + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                subscription = new SubscriptionModel(result);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return subscription;
    }

    /**
     * Recupero l'abbonamento associato all'id_abbonamento usato come parametro
     *
     * @param id_abbonamento Id dell'abbonamento nel database
     * @return L'abbonamento se viene trovato, null se inesistente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static SubscriptionModel get(int id_abbonamento)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        SubscriptionModel subscription = null;
        try {
            String sql = "SELECT * "
                    + "FROM `abbonamenti` "
                    + "WHERE `id_abbonamento`='" + id_abbonamento + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                subscription = new SubscriptionModel(result);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return subscription;
    }
    // </editor-fold>

    /**
     * Uso un abbonamento per pagare i biglietti acquistati
     *
     * @param username Username dell'utente che fa l'acquisto
     * @param subscription Abbonamento usato
     * @param id_ingresso Ingressi acquistati
     * @return I biglietti ancora da pagare
     * @throws Exception Eccezione
     */
    public static int useSubscription(String username, SubscriptionModel subscription, int... id_ingresso)
            throws Exception {

        // controllo eventuali errori e/o furbetti
        if (!username.equals(subscription.getUsername())) {
            throw new Exception();
        }

        int topay = 0;
        DataBase database = DBService.getDataBase();
        try {
            if (subscription.getIngressi_disp() >= id_ingresso.length) {
                // aggiorno ogni singolo ingresso
                for (int i = 0; i < id_ingresso.length; i++) {
                    String sql = "UPDATE `ingressi` "
                            + "SET `id_abbonamento`='" + subscription.getId_abbonamento() + "' "
                            + "WHERE `id_ingresso`='" + id_ingresso[i] + "';";
                    database.modify(sql);
                }
                // aggiorno i nuovi ingressi disponibili nell'abbonamento
                String sql = "UPDATE `abbonamenti` "
                        + "SET `ingressi_disp`=`ingressi_disp`-'" + id_ingresso.length + "' "
                        + "WHERE `id_abbonamento`='" + subscription.getId_abbonamento() + "';";
                database.modify(sql);
                database.commit();
            } else { // gli ingressi disponibili sono inferiori ai biglietti prenotati
                for (int i = 0; i < subscription.getIngressi_disp(); i++) {
                    String sql = "UPDATE `ingressi` "
                            + "SET `id_abbonamento`='" + subscription.getId_abbonamento() + "' "
                            + "WHERE `id_ingresso`='" + id_ingresso[i] + "';";
                    database.modify(sql);
                }
                String sql = "UPDATE `abbonamenti` "
                        + "SET `ingressi_disp`='0' "
                        + "WHERE `id_abbonamento`='" + subscription.getId_abbonamento() + "';";
                database.modify(sql);
                database.commit();
                topay = id_ingresso.length - subscription.getIngressi_disp();
            }
        } catch (NotFoundDBException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return topay;
    }
}
