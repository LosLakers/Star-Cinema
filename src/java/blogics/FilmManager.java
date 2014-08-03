package blogics;

import exceptions.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import services.database.*;

/**
 *
 * @author Guido Pio
 */
public class FilmManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    public static void add(FilmModel film)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        try {
            /* controllo che non ci sia già un film con lo stesso titolo */
            String sql = "SELECT *"
                    + " FROM `film` "
                    + " WHERE "
                    + " `titolo` ='" + util.Conversion.getDatabaseString(film.getTitolo()) + "'";

            ResultSet resultSet = database.select(sql);

            if (resultSet.next()) { // se true riporto un errore
                resultSet.close();
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
            resultSet = database.modifyPK(sql);
            database.commit();

            /* leggo la chiave generata dal DB */
            if (resultSet.next()) {
                film.setId_film(resultSet.getInt(1));
                resultSet.close();
            }
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

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
            throw ex;
        } finally {
            database.close();
        }
    }

    public static void delete(int id_film)
            throws NotFoundDBException {

        DataBase database = DBService.getDataBase();
        try {
            /* elimino il film dal database */
            String sql = "DELETE FROM `film` "
                    + "WHERE `id_film`='" + id_film + "';";
            database.modify(sql);
            database.commit();
        } catch (NotFoundDBException ex) {
            throw ex;
        } finally {
            database.close();
        }
    }

    public static FilmModel get(int id_film)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        FilmModel film = new FilmModel();
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
            throw ex;
        } finally {
            database.close();
        }
        
        return film;
    }

    /* metodo per ottenere un film dal titolo */
    public static FilmModel get(String titolo)
            throws NotFoundDBException, SQLException {

        DataBase database = DBService.getDataBase();
        FilmModel film = new FilmModel();
        try {
            /* che succede se trovo più film con lo stesso titolo? */
            String sql = " SELECT * "
                    + " FROM FILM "
                    + " WHERE "
                    + " TITOLO = '" + util.Conversion.getDatabaseString(titolo) + "'";

            ResultSet result = database.select(sql);
            if (result.next()) { // true c'è un'altra riga
                film = new FilmModel(result);
            }
            result.close();
            database.commit();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        
        return film;
    }

    // </editor-fold>
    
    // metodo per ottenere una lista di film con all'interno il titolo
    public static FilmModel[] searchFilm(String titolo) 
            throws NotFoundDBException, SQLException {
        
        DataBase database = DBService.getDataBase();
        FilmModel[] film = null;
        try {
            List<FilmModel> list = new ArrayList<>();
            String sql = "SELECT * " +
                        "FROM `film` " +
                        "WHERE `titolo` LIKE " +
                        "'%" + util.Conversion.getDatabaseString(titolo) + "%' " +
                        "ORDER BY `titolo`";
            ResultSet resultSet = database.select(sql);
            while (resultSet.next()) {
                FilmModel model = new FilmModel(resultSet);
                list.add(model);
            }
            resultSet.close();
            database.commit();
            
            film = new FilmModel[list.size()];
            for (int i = 0; i < list.size(); i++) {
                film[i] = list.get(i);
            }
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return film;
    }

    public static FilmModel[] searchFilm(LocalDate date) 
            throws NotFoundDBException, SQLException {
        
        DataBase database = DBService.getDataBase();
        FilmModel[] film = null;
        try {
            List<FilmModel> list = new ArrayList<>();
            /*
                SELECT DISTINCT F.id_film, F.titolo
                FROM `film` AS F JOIN `film_sala_programmazione` AS FSP ON F.id_film=FSP.id_film
                    JOIN `programmazione` AS P ON P.id_data=FSP.id_data
                WHERE P.data='date'
                ORDER BY F.titolo
            */
            String sql = "SELECT DISTINCT F.id_film, F.titolo, F.trailer, F.descrizione, F.durata, F.locandina " +
                        "FROM `film` AS F JOIN `film_sala_programmazione` AS FSP ON F.id_film=FSP.id_film " +
                        "JOIN `programmazione` AS P ON P.id_data=FSP.id_data " +
                        "WHERE P.data='" + date + "' " +
                        "ORDER BY F.titolo";
            ResultSet resultSet = database.select(sql);
            while (resultSet.next()) {
                FilmModel model = new FilmModel(resultSet);
                list.add(model);
            }
            resultSet.close();
            database.commit();
            
            film = new FilmModel[list.size()];
            for (int i = 0; i < list.size(); i++) {
                film[i] = list.get(i);
            }
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return film;
    }
}
