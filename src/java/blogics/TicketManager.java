
package blogics;

import exceptions.NotFoundDBException;
import java.sql.*;
import java.util.*;
import services.database.*;

/**
 *
 * @author Guido Pio
 */
public class TicketManager {

    /**
     * Tutti i posti prenotati associati a una terna (film,sala,orario) ricavata
     * grazie a id_tabella
     * 
     * @param id_tabella        L'id per ricavare (film,sala,orario)
     * @return                  I posti prenotati ordinati per fila e numero
     * @throws NotFoundDBException
     * @throws SQLException 
     */
    public static List<String> getReserved(int id_tabella) 
            throws NotFoundDBException, SQLException {
        
        DataBase database = DBService.getDataBase();
        List<String> reserved = new ArrayList<>();
        try {
            String sql = "Ricerca in id_tabella";
            ResultSet result = database.select(sql);
            FilmTheaterDateModel model;
            if (result.next()) {
                model = new FilmTheaterDateModel(result);
            } else {
                throw new SQLException();
            }
            result.close();
            
            // ricerca posti occupati in base a (id_film,id_sala,id_data)
            FilmModel film = model.getFilm();
            TheaterModel theater = model.getTheater();
            DateTimeModel date = model.getDate();
            /*
            Unire ingressi con posti e ordinare per fila e per numero
            */
            sql = "Recupero ticket con (id_film,id_sala,id_data)";
            result = database.select(sql);
            while (result.next()) {
                String seat = result.getString("fila") + "_" + result.getInt("numero");
                reserved.add(seat);
            }
            result.close();
            database.commit();
        } catch(NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return reserved;
    }

    /**
     * Tutti i posti prenotati associati ad un dato utente e a una terna (film,sala,orario)
     * ricavata grazie a id_tabella
     * 
     * @param id_tabella        L'id per ricavare (film,sala,orario)
     * @param username          L'identificativo utente di cui ricercare le prenotazioni
     * @return                  I posti prenotati dall'utente, ordinati per fila e numero
     * @throws NotFoundDBException
     * @throws SQLException 
     */
    public static List<String> getReserved(int id_tabella, String username) 
            throws NotFoundDBException, SQLException {
        
        DataBase database = DBService.getDataBase();
        List<String> reserved = new ArrayList<>();
        try {
            String sql = "Ricerca in id_tabella";
            ResultSet result = database.select(sql);
            FilmTheaterDateModel model;
            if (result.next()) {
                model = new FilmTheaterDateModel(result);
            } else {
                throw new SQLException();
            }
            result.close();
            
            // ricerca posti occupati in base a (id_film,id_sala,id_data)
            FilmModel film = model.getFilm();
            TheaterModel theater = model.getTheater();
            DateTimeModel date = model.getDate();
            /*
            Unire ingressi con posti e ordinare per fila e per numero
            In ingresso considerare anche username nella clausola WHERE
            */
            sql = "Recupero ticket con (id_film,id_sala,id_data)";
            result = database.select(sql);
            while (result.next()) {
                String seat = result.getString("fila") + "_" + result.getInt("numero");
                reserved.add(seat);
            }
            result.close();
            database.commit();
        } catch(NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return reserved;
    }

    /**
     * Recupero l'abbonamento associato allo username usato come parametro
     * 
     * @param username      Utente di cui voglio trovare l'abbonamento
     * @return              L'abbonamento se viene trovato, null se inesistente
     * @throws NotFoundDBException
     * @throws SQLException 
     */
    public static SubscriptionModel getSubscription(String username) 
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
        } catch(NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return subscription;
    }
}
