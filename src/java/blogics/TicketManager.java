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

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Inserisco gli ingressi acquistati nel database
     *
     * @param show Lo show a cui associo i biglietti dello spettacolo
     * @param username Utente che effettua l'acquisto
     * @param seats La lista dei posti che si vuole acquistare
     * @return Gli id_ingresso di ogni posto prenotato
     * @throws Exception
     */
    public static int[] add(FilmTheaterDateModel show, String username, List<SeatModel> seats)
            throws Exception {

        int[] id_ingresso = new int[seats.size()];
        DataBase database = DBService.getDataBase();
        try {
            for (SeatModel seat : seats) {
                String sql = "SELECT * "
                        + "FROM `film_sala_programmazione` AS FSP "
                        + "JOIN `ingressi` AS I ON FSP.id_data=I.id_data "
                        + "JOIN `posti` AS P ON I.id_posto=P.id_posto "
                        + "WHERE FSP.id_tabella='" + show.getId_tabella() + "' AND "
                        + "P.fila='" + util.Conversion.getDatabaseString(seat.getFila()) + "' AND "
                        + "P.numero='" + seat.getNumero() + "';";
                ResultSet result = database.select(sql);
                if (result.next()) {
                    throw new Exception();
                }
                result.close();
            }

            // recupero id_film, id_data e id_sala necessari
            TheaterModel theater = show.getTheater();
            int id_sala = theater.getId_sala();
            FilmModel film = show.getFilm();
            int id_film = film.getId_film();
            DateTimeModel data = show.getDate();
            int id_data = data.getId_data();

            // inserisco posto e ingresso nel sistema
            for (int i = 0; i < seats.size(); i++) {
                SeatModel seat = seats.get(i);
                // con id_sala creo il posto e ne recupero id_posto
                String sql = "INSERT INTO `posti`(`fila`, `numero`, `id_sala`) "
                        + "VALUES ('" + util.Conversion.getDatabaseString(seat.getFila()) + "',"
                        + "'" + seat.getNumero() + "',"
                        + "'" + id_sala + "');";
                ResultSet result = database.modifyPK(sql);
                int id_posto = 0;
                if (result.next()) {
                    id_posto = result.getInt(1);
                    result.close();
                } else {
                    throw new Exception();
                }

                // creo l'ingresso associato all'utente
                sql = "INSERT INTO `ingressi`(`id_data`, `id_posto`, `username`, `id_film`) "
                        + "VALUES "
                        + "('" + id_data + "',"
                        + "'" + id_posto + "',"
                        + "'" + util.Conversion.getDatabaseString(username) + "',"
                        + "'" + id_film + "')";
                result = database.modifyPK(sql);
                if (result.next()) {
                    id_ingresso[i] = result.getInt(1);
                    result.close();
                }
            }
            // aggiorno il numero di posti disponibili nella sala
            String sql = "UPDATE `sale` "
                    + "SET `posti_disp`=`posti_disp`-'" + seats.size() + "' "
                    + "WHERE `id_sala`='" + id_sala + "'";
            database.modify(sql);
            database.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            database.close();
        }
        return id_ingresso;
    }

    /**
     * Aggiorno l'ingresso passato come parametro nel database
     *
     * @param ticket L'ingresso da aggiornare
     * @param id_film Identificativo del nuovo film
     * @param id_data Identificativo della nuova data
     * @param seat Nuovo posto nella sala
     * @throws Exception
     */
    public static void update(TicketModel ticket, int id_film, int id_data, SeatModel seat)
            throws Exception {

        DataBase database = DBService.getDataBase();
        try {
            // controllo che non ci sia già un posto con quella fila e numero
            String sql = "SELECT * "
                    + "FROM `posti` "
                    + "WHERE `fila`='" + util.Conversion.getDatabaseString(seat.getFila()) + "' "
                    + "AND `numero`='" + seat.getNumero() + "' "
                    + "AND `id_sala`='" + seat.getId_sala() + "'";
            ResultSet result = database.select(sql);
            if (result.next()) {
                throw new Exception();
            }
            result.close();

            // controllo se è cambiata la sala oppure no
            if (ticket.getId_film() != id_film || ticket.getId_data() != id_data) {
                sql = "UPDATE `sale` AS S "
                        + "INNER JOIN `posti` AS P ON S.id_sala=P.id_sala "
                        + "SET `posti_disp`=`posti_disp`+1 "
                        + "WHERE P.id_posto='" + ticket.getId_posto() + "'";
                database.modify(sql);
                
                sql = "UPDATE `sale` "
                        + "SET `posti_disp`=`posti_disp`-1 "
                        + "WHERE `id_sala`='" + seat.getId_sala() + "'";
                database.modify(sql);
            }
            
            // aggiorno il posto di ticket con i nuovi dati
            sql = "UPDATE `posti` "
                    + "SET `fila`='" + util.Conversion.getDatabaseString(seat.getFila()) + "',"
                    + "`numero`='" + seat.getNumero() + "',"
                    + "`id_sala`='" + seat.getId_sala() + "' "
                    + "WHERE `id_posto`='" + ticket.getId_posto() + "'";
            database.modify(sql);

            // aggiorno il ticket
            sql = "UPDATE `ingressi` "
                    + "SET `id_data`='" + id_data + "',"
                    + "`id_film`='" + id_film + "' "
                    + "WHERE `id_ingresso`='" + ticket.getId_ingresso() + "'";
            database.modify(sql);
            database.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Recupero il TicketModel a partire dall'id_ingresso
     *
     * @param id_ingresso Identificativo dell'ingreso che sto cercando
     * @return L'ingresso se è presente nel database, null se non è presente
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static TicketModel get(int id_ingresso)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        TicketModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `ingressi` "
                    + "WHERE `id_ingresso`='" + id_ingresso + "'";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new TicketModel(result);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    /**
     * Recupero tutti gli ingressi associati ad un utente, ordinati per data,
     * dalla più recente in poi, e per ora inizio
     *
     * @param username Username dell'utente
     * @return La lista degli ingressi recuperati
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static List<TicketModel> get(String username)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<TicketModel> model = new ArrayList<>();
        try {
            /*
             SELECT * 
             FROM `ingressi` AS I 
             JOIN `programmazione` AS P ON I.id_data=P.id_data 
             WHERE I.username=username 
             ORDER BY P.data DESC, P.ora_inizio
             */
            String sql = "SELECT * "
                    + "FROM `ingressi` AS I "
                    + "JOIN `programmazione` AS P ON I.id_data=P.id_data "
                    + "WHERE I.username='" + util.Conversion.getDatabaseString(username) + "' "
                    + "ORDER BY P.data DESC, P.ora_inizio";
            ResultSet result = database.select(sql);
            while (result.next()) {
                TicketModel ticket = new TicketModel(result);
                model.add(ticket);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }
    // </editor-fold>

    /**
     * Recupero un posto associato ad un dato id_posto
     *
     * @param id_posto Id del posto
     * @return Il posto se è presente nel database, null se non c'è
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static SeatModel getSeat(int id_posto)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        SeatModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `posti` "
                    + "WHERE `id_posto`='" + id_posto + "'";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new SeatModel(result);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    /**
     * Tutti i posti prenotati associati a una terna (film,sala,orario) ricavata
     * grazie al modello di uno show
     *
     * @param show Lo show di cui si ricercano i posti occupati
     * @return I posti prenotati ordinati per fila e numero
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static List<SeatModel> getReserved(FilmTheaterDateModel show)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<SeatModel> reserved = new ArrayList<>();
        try {
            // ricerca posti occupati in base a (id_film,id_sala,id_data)
            FilmModel film = show.getFilm();
            TheaterModel theater = show.getTheater();
            DateTimeModel date = show.getDate();
            /*
             SELECT * 
             FROM `ingressi` AS I 
             JOIN `posti` AS P ON I.id_posto=P.id_posto 
             WHERE I.id_film=id_film AND P.id_sala=id_sala AND I.id_data=id_data
             */
            String sql = "SELECT * "
                    + "FROM `ingressi` AS I "
                    + "JOIN `posti` AS P ON I.id_posto=P.id_posto "
                    + "WHERE I.id_film='" + film.getId_film() + "' AND "
                    + "P.id_sala='" + theater.getId_sala() + "' AND "
                    + "I.id_data='" + date.getId_data() + "' "
                    + "ORDER BY P.fila, P.numero;";
            ResultSet result = database.select(sql);
            /*
             Da modificare con un modello dei posti a sedere
             */
            while (result.next()) {
                SeatModel seat = new SeatModel(result);
                reserved.add(seat);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return reserved;
    }

    /**
     * Tutti i posti prenotati associati ad un dato utente e a una terna
     * (film,sala,orario) ricavata grazie al modello di uno show
     *
     * @param show Il modello dello show
     * @param username L'identificativo utente di cui ricercare le prenotazioni
     * @return I posti prenotati dall'utente, ordinati per fila e numero
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static List<SeatModel> getReserved(FilmTheaterDateModel show, String username)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<SeatModel> reserved = new ArrayList<>();
        try {
            // ricerca posti occupati in base a (id_film,id_sala,id_data)
            FilmModel film = show.getFilm();
            TheaterModel theater = show.getTheater();
            DateTimeModel date = show.getDate();
            /*
             SELECT * 
             FROM `ingressi` AS I 
             JOIN `posti` AS P ON I.id_posto=P.id_posto 
             WHERE I.id_film=id_film AND P.id_sala=id_sala AND I.id_data=id_data
             AND I.username=username
             */
            String sql = "SELECT * "
                    + "FROM `ingressi` AS I "
                    + "JOIN `posti` AS P ON I.id_posto=P.id_posto "
                    + "WHERE I.id_film='" + film.getId_film() + "' AND "
                    + "P.id_sala='" + theater.getId_sala() + "' AND "
                    + "I.id_data='" + date.getId_data() + "' AND "
                    + "I.username='" + util.Conversion.getDatabaseString(username) + "'"
                    + "ORDER BY P.fila, P.numero;";
            ResultSet result = database.select(sql);
            while (result.next()) {
                SeatModel seat = new SeatModel(result);
                reserved.add(seat);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return reserved;
    }
}
