package blogics;

import exceptions.*;
import java.sql.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import services.database.*;

/**
 * Manager per la gestione dei film
 */
public class FilmManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Aggiunge un film nel database
     *
     * @param film Il modello del film da aggiungere
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static void add(FilmModel film)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        try {
            /* controllo che non ci sia già un film con lo stesso titolo */
            String sql = "SELECT *"
                    + " FROM `film` "
                    + " WHERE "
                    + " `titolo` ='" + util.Conversion.getDatabaseString(film.getTitolo()) + "'";

            ResultSet result = database.select(sql);

            if (result.next()) { // se true riporto un errore
                result.close();
                throw new SQLException();
            }

            /* aggiungo il film al database */
            sql = "INSERT INTO `film` "
                    + "(`titolo`, `trailer`, `descrizione`, `durata`, `locandina`) "
                    + "VALUES "
                    + "('" + util.Conversion.getDatabaseString(film.getTitolo()) + "',"
                    + "'" + util.Conversion.getDatabaseString(film.getTrailer()) + "',"
                    + "'" + util.Conversion.getDatabaseString(film.getDescrizione()) + "',"
                    + "'" + film.getDurata() + "',"
                    + "'" + util.Conversion.getDatabaseString(film.getLocandina()) + "');";
            result = database.modifyPK(sql);
            database.commit();

            /* leggo la chiave generata dal DB */
            if (result.next()) {
                film.setId_film(result.getInt(1));
                result.close();
            }
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Aggiorna il film nel database
     *
     * @param film Modello del film
     * @throws NotFoundDBException Eccezione
     */
    public static void update(FilmModel film)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            /* aggiorno il film nel database */
            String sql = "UPDATE `film` "
                    + "SET "
                    + "`titolo`='" + util.Conversion.getDatabaseString(film.getTitolo()) + "',"
                    + "`trailer`='" + util.Conversion.getDatabaseString(film.getTrailer()) + "',"
                    + "`descrizione`='" + util.Conversion.getDatabaseString(film.getDescrizione()) + "',"
                    + "`durata`='" + film.getDurata() + "',"
                    + "`locandina`='" + util.Conversion.getDatabaseString(film.getLocandina()) + "' "
                    + "WHERE `id_film`='" + film.getId_film() + "';";
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
     * Elimina uno o più film dal database
     *
     * @param id_film Identificativo del film
     * @throws NotFoundDBException Eccezione
     */
    public static void delete(int... id_film)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            for (int id : id_film) {
                /* elimino il film dal database */
                String sql = "DELETE FROM `film` "
                        + "WHERE `id_film`='" + id + "';";
                database.modify(sql);
            }
            database.commit();
        } catch (NotFoundDBException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
    }

    /**
     * Recupera un film dal database
     *
     * @param id_film Identificativo del film
     * @return Il film se è presente, null se non lo è
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static FilmModel get(int id_film)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        FilmModel film = null;
        try {
            String sql = " SELECT * "
                    + " FROM `film` "
                    + " WHERE "
                    + " `id_film`='" + id_film + "'";

            ResultSet result = database.select(sql);
            if (result.next()) { // true c'è un'altra riga
                film = new FilmModel(result);
            }
            database.commit();
        } catch (SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return film;
    }

    /**
     * Recupero un film a cui è assegnato un id_data ed è in programmazione in
     * una certa sala
     *
     * @param id_data Identificativo della data
     * @param num_sala Numero della sala
     * @return Il film se è presente nel database, null se non c'è
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static FilmModel get(int id_data, int num_sala)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        FilmModel model = null;
        try {
            String sql = "SELECT * "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `sale` AS S ON FSP.id_sala=S.id_sala "
                    + "JOIN `film` AS F ON FSP.id_film=F.id_film "
                    + "WHERE FSP.id_data='" + id_data + "' AND "
                    + "S.numero_sala='" + num_sala + "';";
            ResultSet result = database.select(sql);
            if (result.next()) {
                model = new FilmModel(result);
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
     * Ricerco una lista di film che possono combaciare con il titolo inserito
     *
     * @param titolo Il titolo di paragone
     * @return La lista dei film se è presente nel database
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static List<FilmModel> searchFilm(String titolo)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<FilmModel> list = new ArrayList<>();
        try {
            String sql = "SELECT * "
                    + "FROM `film` "
                    + "WHERE `titolo` LIKE "
                    + "'%" + util.Conversion.getDatabaseString(titolo) + "%' "
                    + "ORDER BY `titolo`";
            ResultSet result = database.select(sql);
            while (result.next()) {
                FilmModel model = new FilmModel(result);
                list.add(model);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return list;
    }

    /**
     * Ricerco uno o più film in programmazione in una certa data
     *
     * @param date La data di interesse
     * @return La lista dei film trovati
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static List<FilmModel> searchFilm(LocalDate date)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<FilmModel> film = new ArrayList<>();
        try {
            /*
             SELECT DISTINCT F.id_film, F.titolo
             FROM `film` AS F JOIN `film_sala_programmazione` AS FSP ON F.id_film=FSP.id_film
             JOIN `programmazione` AS P ON P.id_data=FSP.id_data
             WHERE P.data='date'
             ORDER BY F.titolo
             */
            String sql = "SELECT DISTINCT F.id_film, F.titolo, F.trailer, F.descrizione, F.durata, F.locandina "
                    + "FROM `film` AS F JOIN `film_sala_programmazione` AS FSP ON F.id_film=FSP.id_film "
                    + "JOIN `programmazione` AS P ON P.id_data=FSP.id_data "
                    + "WHERE P.data='" + date + "' "
                    + "ORDER BY F.titolo";
            ResultSet result = database.select(sql);
            while (result.next()) {
                FilmModel model = new FilmModel(result);
                film.add(model);
            }
            result.close();
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            database.rollBack();
            throw ex;
        } finally {
            database.close();
        }
        return film;
    }

    /**
     * Recupero la lista dei film in programmazione in un intervallo di date,
     * ordinata per id_film
     *
     * @param from data di inizio
     * @param to data di fine
     * @return la lista dei film ordinata per id_film
     * @throws NotFoundDBException Eccezione
     * @throws SQLException Eccezione
     */
    public static List<FilmModel> getFilms(LocalDate from, LocalDate to)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        List<FilmModel> model = new ArrayList<>();
        try {
            // recupero la lista dei film in programmazione
            /*
             SELECT DISTINCT F.id_film 
             FROM `film_sala_programmazione` AS FSP 
             JOIN `film` AS F ON FSP.id_film=F.id_film 
             JOIN `programmazione` AS P ON FSP.id_data=P.id_data 
             WHERE P.data BETWEEN from AND to 
             ORDER BY FSP.id_film
             */
            String sql = "SELECT DISTINCT F.id_film "
                    + "FROM `film_sala_programmazione` AS FSP "
                    + "JOIN `film` AS F ON FSP.id_film=F.id_film "
                    + "JOIN `programmazione` AS P ON FSP.id_data=P.id_data "
                    + "WHERE P.data "
                    + "BETWEEN '" + from.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' AND '"
                    + to.format(DateTimeFormatter.ISO_LOCAL_DATE) + "' "
                    + "ORDER BY FSP.id_film";
            ResultSet result = database.select(sql);
            // aggiungo tutti i film trovati nella lista
            while (result.next()) {
                FilmModel film = FilmManager.get(result.getInt("id_film"));
                model.add(film);
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
