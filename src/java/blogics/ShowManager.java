package blogics;

import exceptions.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import services.database.*;

/**
 *
 * @author Guido Pio
 */
public class ShowManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Inserisco un film in programmazione in base alla data e alla sala passate
     * come parametri.
     *
     * @param film il film che voglio inserire in programmazione
     * @param show la data, l'orario di inizio e di fine da associare al film
     * @param theater la sala in cui il film deve essere proiettato
     * @throws Exception
     */
    public static void add(FilmModel film, DateTimeModel show, TheaterModel theater)
            throws Exception {

        // controllo su validità data e ora inizio e fine
        LocalTime inizio = show.getOra_inizio();
        LocalTime fine = show.getOra_fine();

        if (fine.isBefore(inizio)) { // controlla già che anche la durata sia maggiore di 0
            throw new Exception();
        }

        int ora_inizio = inizio.getHour();
        int min_inizio = inizio.getMinute();
        LocalTime durata = fine.minusHours(ora_inizio).minusMinutes(min_inizio);
        if (durata.compareTo(film.getDurata()) < 0) { // < 0 se inferiore a quella del film
            throw new Exception();
        }

        DataBase database = DBService.getDataBase();
        try {
            // gestione inizio e fine rispetto agli altri orari
            String sql = "SELECT P.ora_inizio, P.ora_fine "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE `numero_sala`='" + theater.getNumero_sala() + "' AND "
                    + "P.data='" + show.getData() + "'"
                    + "ORDER BY P.ora_inizio;";
            ResultSet resultSet = database.select(sql);
            while (resultSet.next()) {
                LocalTime _inizio = resultSet.getTime("ora_inizio").toLocalTime();
                LocalTime _fine = resultSet.getTime("ora_fine").toLocalTime();

                // errore se un film inizia dopo un altro film ma prima della fine di quest'ultimo
                if (inizio.isAfter(_inizio) && inizio.isBefore(_fine) && !inizio.equals(_inizio)) {
                    throw new Exception();
                }
                // errore se il film è compreso nell'inizio di un altro film
                if (inizio.isBefore(_inizio) && fine.isAfter(_inizio)) {
                    throw new Exception();
                }
            }
            resultSet.close();

            // inserimento data con recupero id_data
            sql = "INSERT INTO `programmazione`"
                    + "(`data`,`ora_inizio`,`ora_fine`) VALUES "
                    + "('" + show.getData() + "',"
                    + "'" + show.getOra_inizio() + "',"
                    + "'" + show.getOra_fine() + "');";
            resultSet = database.modifyPK(sql);
            if (resultSet.next()) {
                show.setId_data(resultSet.getInt(1));
            }
            resultSet.close();

            // inserimento sala con recupero id_sala
            sql = "INSERT INTO `sale`"
                    + "(`numero_sala`) VALUES "
                    + "('" + theater.getNumero_sala() + "');";
            resultSet = database.modifyPK(sql);
            if (resultSet.next()) {
                theater.setId_sala(resultSet.getInt(1));
            }
            resultSet.close();

            // inserimento id_film - id_data - id_sala nella relativa tabella
            sql = "INSERT INTO `film_sala_programmazione`"
                    + "(`id_film`, `id_sala`, `id_data`) VALUES "
                    + "('" + film.getId_film() + "',"
                    + "'" + theater.getId_sala() + "',"
                    + "'" + show.getId_data() + "');";
            int count = database.modify(sql);
            if (count == 0) {
                throw new Exception();
            }

            database.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Aggiorno la programmazione di un film in base ai modelli passati come
     * parametri.
     *
     * @param film film di cui voglio aggiornare la programmazione
     * @param show nuova data di programmazione
     * @param theater nuova sala di programmazione
     * @throws Exception
     */
    public static void update(FilmModel film, DateTimeModel show, TheaterModel theater)
            throws Exception {

        // controllo su validità data e ora inizio e fine
        LocalTime inizio = show.getOra_inizio();
        LocalTime fine = show.getOra_fine();

        if (fine.isBefore(inizio)) { // controlla già che anche la durata sia maggiore di 0
            throw new Exception();
        }

        int ora_inizio = inizio.getHour();
        int min_inizio = inizio.getMinute();
        LocalTime durata = fine.minusHours(ora_inizio).minusMinutes(min_inizio);
        if (durata.compareTo(film.getDurata()) < 0) { // < 0 se inferiore a quella del film
            throw new Exception();
        }

        DataBase database = DBService.getDataBase();
        try {
            // gestione inizio e fine rispetto agli altri orari
            String sql = "SELECT P.ora_inizio, P.ora_fine "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE `numero_sala`='" + theater.getNumero_sala() + "' AND "
                    + "P.data='" + show.getData() + "'"
                    + "ORDER BY P.ora_inizio;";
            ResultSet resultSet = database.select(sql);
            while (resultSet.next()) {
                LocalTime _inizio = resultSet.getTime("ora_inizio").toLocalTime();
                LocalTime _fine = resultSet.getTime("ora_fine").toLocalTime();

                // errore se un film inizia dopo un altro film ma prima della fine di quest'ultimo
                if (inizio.isAfter(_inizio) && inizio.isBefore(_fine) && !inizio.equals(_inizio)) {
                    throw new Exception();
                }
                // errore se il film è compreso nell'inizio di un altro film
                if (inizio.isBefore(_inizio) && fine.isAfter(_inizio)) {
                    throw new Exception();
                }
            }
            resultSet.close();

            // aggiornamento data
            sql = "UPDATE `programmazione` SET "
                    + "`data`='" + show.getData() + "',"
                    + "`ora_inizio`='" + show.getOra_inizio() + "',"
                    + "`ora_fine`='" + show.getOra_fine() + "' "
                    + "WHERE `id_data`='" + show.getId_data() + "';";
            database.modify(sql);

            // aggiornamento sala
            sql = "UPDATE `sale` SET "
                    + "`numero_sala`='" + theater.getNumero_sala() + "' "
                    + "WHERE `id_sala`='" + theater.getId_sala() + "';";
            database.modify(sql);

            database.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Recupero il modello nel database in base a id_tabella
     *
     * @param id_tabella id_tabella nel database di cui voglio recuperare i dati
     * @return ritorno il modello del database ottenuto
     * @throws Exception
     */
    public static FilmTheaterDateModel get(int id_tabella)
            throws Exception {

        DataBase database = DBService.getDataBase();
        FilmTheaterDateModel model = new FilmTheaterDateModel();
        try {
            String sql = "SELECT * "
                    + "FROM `film_sala_programmazione` "
                    + "WHERE `id_tabella`='" + id_tabella + "';";
            ResultSet result = database.select(sql);
            int id_film = 0;
            int id_sala = 0;
            int id_data = 0;
            if (result.next()) {
                id_film = result.getInt("id_film");
                id_sala = result.getInt("id_sala");
                id_data = result.getInt("id_data");
            }
            result.close();

            if (id_film == 0 || id_sala == 0 || id_data == 0) {
                throw new Exception();
            }

            FilmModel film = FilmManager.get(id_film);
            model.setFilm(film);

            sql = "SELECT * "
                    + "FROM `sale` "
                    + "WHERE `id_sala`='" + id_sala + "';";
            result = database.select(sql);
            if (result.next()) {
                TheaterModel theater = new TheaterModel(result);
                model.setTheater(theater);
            }
            result.close();

            sql = "SELECT * "
                    + "FROM `programmazione` "
                    + "WHERE `id_data`='" + id_data + "';";
            result = database.select(sql);
            if (result.next()) {
                DateTimeModel date = new DateTimeModel(result);
                model.setDate(date);
            }
            result.close();
            database.commit();
        } catch (Exception ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    // </editor-fold>

    /**
     * Recupero tutte le date associate ad una certa sala a partire da una data
     * di inizio fino a una di fine, ordinate per data e ora_inizio.
     *
     * @param num_sala Numero sala di cui ricerco le date
     * @param from Data di inizio
     * @param to Data di fine
     * @return Lista delle date associate alla sala
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static List<DateTimeModel> getDate(int num_sala, LocalDate from, LocalDate to)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<DateTimeModel> model = new ArrayList<>();
        try {
            /*
             SELECT * 
             FROM `film_sala_programmazione` AS FSP 
             JOIN `sale` AS S ON FSP.id_sala=S.id_sala 
             JOIN `programmazione` AS P ON FSP.id_data=P.id_data 
             WHERE S.numero_sala='num_sala' AND P.date BETWEEN from AND to
             ORDER BY P.data, P.ora_inizio
             */
            String sql = "SELECT * "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE S.numero_sala='" + num_sala + "' AND "
                    + "P.data BETWEEN '" + from.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' AND "
                    + "'" + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' "
                    + "ORDER BY P.data, P.ora_inizio";
            ResultSet resultSet = database.select(sql);
            while (resultSet.next()) {
                DateTimeModel tmp = new DateTimeModel(resultSet);
                model.add(tmp);
            }
            resultSet.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    /**
     * Recupero tutte le date associate ad un film a partire da una data di
     * inizio fino a una di fine, ordinate per data e ora_inizio.
     *
     * @param film Film di cui voglio trovare le date
     * @param from Data da cui inizio
     * @param to Data da cui finisco
     * @return Lista delle date associate al film
     * @throws NotFoundDBException
     * @throws SQLException
     */
    public static List<DateTimeModel> getDate(FilmModel film, LocalDate from, LocalDate to)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<DateTimeModel> model = new ArrayList<>();
        try {
            /*
             SELECT * 
             FROM `film_sala_programmazione` AS FSP 
             JOIN `film` AS F ON FSP.id_film=F.id_film 
             JOIN `programmazione` AS P ON FSP.id_data=P.id_data 
             WHERE F.id_film=id_film AND P.date BETWEEN from AND to
             ORDER BY P.data, P.ora_inizio
             */
            String sql = "SELECT * "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `film` AS F ON FSP.id_film=F.id_film "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE F.id_film='" + film.getId_film() + "' AND "
                    + "P.data BETWEEN '" + from.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' AND "
                    + "'" + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' "
                    + "ORDER BY P.data, P.ora_inizio";
            ResultSet resultSet = database.select(sql);
            while (resultSet.next()) {
                DateTimeModel tmp = new DateTimeModel(resultSet);
                model.add(tmp);
            }
            resultSet.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    /**
     * Recupero tutte le coppie Sala-Data associate ad un determinato film
     *
     * @param film modello di cui voglio recuperare le coppie Sala-Data
     * @param from data da cui si vuole iniziare a ricercare
     * @param to data fino a cui si vuole ricercare
     * @return la lista delle coppie Sala-Data in programmazione
     * @throws exceptions.NotFoundDBException
     * @throws java.sql.SQLException
     */
    public static List<TheaterDateModel> getShowList(FilmModel film, LocalDate from, LocalDate to)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<TheaterDateModel> model = new ArrayList<>();
        try {
            /*
             SELECT * 
             FROM `film_sala_programmazione` AS FSP 
             JOIN `programmazione` AS P ON FSP.id_data=P.id_data 
             JOIN `sale` AS S ON FSP.id_sala=S.id_sala 
             WHERE FSP.id_film=film.id_film AND P.data BETWEEN from AND to 
             ORDER BY P.data, S.numero_sala, P.ora_inizio
             */
            String sql = "SELECT * "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "WHERE FSP.id_film='" + film.getId_film() + "' AND "
                    + "P.data BETWEEN '" + from.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' AND "
                    + "'" + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' "
                    + "ORDER BY P.data, S.numero_sala, P.ora_inizio";
            ResultSet result = database.select(sql);
            while (result.next()) {
                TheaterDateModel theaterdate = new TheaterDateModel(result);
                model.add(theaterdate);
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
}
