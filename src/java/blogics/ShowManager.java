package blogics;

import exceptions.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import services.database.*;

/**
 * Manager per la gestione di uno show
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
     * @throws Exception Eccezione
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

                // errore se il film inizia allo stesso orario di un altro
                if (inizio.compareTo(_inizio) == 0) {
                    throw new Exception();
                }
                // errore se un film inizia dopo un altro film ma prima della fine di quest'ultimo
                if (inizio.isAfter(_inizio) && inizio.isBefore(_fine)) {
                    throw new Exception();
                }
                // errore se il film è compreso nell'inizio di un altro film
                if (inizio.isBefore(_inizio) && fine.isAfter(_inizio)) {
                    throw new Exception();
                }
            }
            resultSet.close();

            // inserimento data con recupero id_data se show ha id_data = 0
            if (show.getId_data() == 0) {
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
            }

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
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Aggiorno la programmazione di un film in base al modelli passato come
     * parametro. La data viene creata nel database nel caso non sia stata
     * recuperata in precedenza
     *
     * @param model Il modello da inserire nel database
     * @throws Exception Eccezione
     */
    public static void update(FilmTheaterDateModel model)
            throws Exception {

        DateTimeModel show = model.getDate();
        TheaterModel theater = model.getTheater();
        FilmModel film = model.getFilm();

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
            String sql = "SELECT FSP.id_tabella, P.ora_inizio, P.ora_fine "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE `numero_sala`='" + theater.getNumero_sala() + "' AND "
                    + "P.data='" + show.getData() + "'"
                    + "ORDER BY P.ora_inizio;";
            ResultSet result = database.select(sql);
            while (result.next()) {
                // evito il confronto con la vecchia data inserita
                if (result.getInt("id_tabella") != model.getId_tabella()) {
                    LocalTime _inizio = result.getTime("ora_inizio").toLocalTime();
                    LocalTime _fine = result.getTime("ora_fine").toLocalTime();

                    // errore se il film inizia allo stesso orario di un altro
                    if (inizio.compareTo(_inizio) == 0) {
                        throw new Exception();
                    }
                    // errore se un film inizia dopo un altro film ma prima della fine di quest'ultimo
                    if (inizio.isAfter(_inizio) && inizio.isBefore(_fine)) {
                        throw new Exception();
                    }
                    // errore se il film è compreso nell'inizio di un altro film
                    if (inizio.isBefore(_inizio) && fine.isAfter(_inizio)) {
                        throw new Exception();
                    }
                }
            }
            result.close();

            if (show.getId_data() == 0) {
                // creo data
                sql = "INSERT INTO `programmazione`"
                        + "(`data`,`ora_inizio`,`ora_fine`) VALUES "
                        + "('" + show.getData() + "',"
                        + "'" + show.getOra_inizio() + "',"
                        + "'" + show.getOra_fine() + "');";
                result = database.modifyPK(sql);
                if (result.next()) {
                    show.setId_data(result.getInt(1));
                }
                result.close();
            }
            // aggiorno film-sala-programmazione con la nuova data
            sql = "UPDATE `film_sala_programmazione` SET "
                    + "`id_data`='" + show.getId_data() + "' "
                    + "WHERE `id_tabella`='" + model.getId_tabella() + "';";
            database.modify(sql);

            // aggiornamento sala
            sql = "UPDATE `sale` SET "
                    + "`numero_sala`='" + theater.getNumero_sala() + "' "
                    + "WHERE `id_sala`='" + theater.getId_sala() + "';";
            database.modify(sql);

            database.commit();
        } catch (Exception ex) {
            database.rollBack();
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
     * @throws Exception Eccezione
     */
    public static FilmTheaterDateModel get(int id_tabella)
            throws Exception {

        DataBase database = DBService.getDataBase();
        FilmTheaterDateModel model = new FilmTheaterDateModel();
        model.setId_tabella(id_tabella);
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
            model.setId_tabella(id_tabella);
            result.close();
            database.commit();
        } catch (Exception ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }

    /**
     * Recupero id_tabella in base a id_film, id_sala e id_data
     *
     * @param id_film Id del film
     * @param id_sala Id della sala
     * @param id_data Id della data
     * @return Id della tabella corrispondente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static int get(int id_film, int id_sala, int id_data)
            throws NotFoundDBException, SQLException {

        int id_tabella = 0;
        DataBase database = DBService.getDataBase();
        try {
            String sql = "SELECT `id_tabella` "
                    + "FROM `film_sala_programmazione` "
                    + "WHERE `id_film`='" + id_film + "' AND "
                    + "`id_sala`='" + id_sala + "' AND "
                    + "`id_data`='" + id_data + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                id_tabella = result.getInt("id_tabella");
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return id_tabella;
    }

    /**
     * Recupero la lista degli id_tabella con id_film e data inseriti come
     * parametri
     *
     * @param id_film Id del film che mi interessa
     * @param data Data di programmazione di cui sono interessato
     * @return Array contenente gli id_tabella
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static int[] get(int id_film, LocalDate data)
            throws NotFoundDBException, SQLException {

        int[] id_tabella = new int[0];
        DataBase database = DBService.getDataBase();
        try {
            String sql = "SELECT `id_tabella` "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `film` AS F ON FSP.id_film=F.id_film "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE FSP.id_film='" + id_film + "' AND "
                    + "P.data='" + data + "';";
            ResultSet result = database.select(sql);
            List<Integer> list = new ArrayList<>();
            while (result.next()) {
                Integer id = result.getInt("id_tabella");
                list.add(id);
            }

            // converto lista Integer in array int
            id_tabella = new int[list.size()];
            for (int i = 0; i < id_tabella.length; i++) {
                id_tabella[i] = list.get(i);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return id_tabella;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Gestione Date ">
    /**
     * Recupero una data in base all'id_data passato come parametro
     *
     * @param id_data Identificativo della data
     * @return La data se è presente nel database, null se non è presente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static DateTimeModel getData(int id_data)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        DateTimeModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `programmazione` "
                    + "WHERE `id_data`='" + id_data + "'";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new DateTimeModel(result);
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
     * Recupero un modello di data e orario
     *
     * @param data La data che cerco
     * @param inizio L'orario di inizio che cerco
     * @param fine L'orario di fine che cerco
     * @return Il modello di data trovato o un modello con id_data = 0
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static DateTimeModel getDate(LocalDate data, LocalTime inizio, LocalTime fine)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        DateTimeModel model = new DateTimeModel(0, data, inizio, fine);
        try {
            String sql = "SELECT id_data "
                    + "FROM `programmazione` "
                    + "WHERE `data`='" + data + "' AND "
                    + "`ora_inizio`='" + inizio + "' AND "
                    + "`ora_fine`='" + fine + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model.setId_data(result.getInt(1));
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
     * Recupero tutte le date associate ad una certa sala a partire da una data
     * di inizio fino a una di fine, ordinate per data e ora_inizio.
     *
     * @param num_sala Numero sala di cui ricerco le date
     * @param from Data di inizio
     * @param to Data di fine
     * @return Lista delle date associate alla sala
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
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
            database.rollBack();
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
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
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
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Gestione Sale ">
    /**
     * Recupero una sala in base al suo id_sala
     *
     * @param id_sala Identificativo della sala
     * @return La sala se è presente nel database, null se non è presente
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static TheaterModel getTheater(int id_sala)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        TheaterModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `sale` "
                    + "WHERE `id_sala`='" + id_sala + "'";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new TheaterModel(result);
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
    // </editor-fold>

    /**
     * Recupero tutte le coppie Sala-Data associate ad un determinato film
     *
     * @param film modello di cui voglio recuperare le coppie Sala-Data
     * @param from data da cui si vuole iniziare a ricercare
     * @param to data fino a cui si vuole ricercare
     * @return la lista delle coppie Sala-Data in programmazione
     * @throws exceptions.NotFoundDBException Eccezione
     * @throws java.sql.SQLException Eccezione
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
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return model;
    }
}
