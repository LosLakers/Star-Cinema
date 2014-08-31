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
     * @param rows Lettera fila di ogni posto
     * @param numseat Numero di ogni posto
     * @return Gli id_ingresso di ogni posto prenotato
     * @throws Exception
     */
    public static int[] add(FilmTheaterDateModel show, String username, List<String> rows, int[] numseat)
            throws Exception {
        if (rows.size() != numseat.length) {
            throw new Exception();
        }
        int[] id_ingresso = new int[numseat.length];
        DataBase database = DBService.getDataBase();
        try {
            // controlo che nessun posto sia già occupato
            for (int i = 0; i < numseat.length; i++) {
                String sql = "SELECT * "
                        + "FROM `film_sala_programmazione` AS FSP "
                        + "JOIN `ingressi` AS I ON FSP.id_data=I.id_data "
                        + "JOIN `posti` AS P ON I.id_posto=P.id_posto "
                        + "WHERE FSP.id_tabella='" + show.getId_tabella() + "' AND "
                        + "P.fila='" + util.Conversion.getDatabaseString(rows.get(i)) + "' AND "
                        + "P.numero='" + numseat[i] + "';";
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
            for (int i = 0; i < numseat.length; i++) {
                // recupero id_film, id_data e id_sala da id_tabella

                // con id_sala creo il posto e ne recupero id_posto
                String sql = "INSERT INTO `posti`(`fila`, `numero`, `id_sala`) "
                        + "VALUES ('" + util.Conversion.getDatabaseString(rows.get(i)) + "',"
                        + "'" + numseat[i] + "',"
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
            String sql = "UPDATE `sale` "
                    + "SET `posti_disp`=`posti_disp`-'" + numseat.length + "' "
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

    // </editor-fold>
    /**
     * Tutti i posti prenotati associati a una terna (film,sala,orario) ricavata
     * grazie al modello di uno show
     *
     * @param show Lo show di cui si ricercano i posti occupati
     * @return I posti prenotati ordinati per fila e numero
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static List<String> getReserved(FilmTheaterDateModel show)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<String> reserved = new ArrayList<>();
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
                String seat = result.getString("fila") + "-" + result.getInt("numero");
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
    public static List<String> getReserved(FilmTheaterDateModel show, String username)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<String> reserved = new ArrayList<>();
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
            /*
             Da cambiare con un modello dei singoli posti
             */
            while (result.next()) {
                String seat = result.getString("fila") + "-" + result.getInt("numero");
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
