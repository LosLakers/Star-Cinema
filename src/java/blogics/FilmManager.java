package blogics;

import exceptions.*;
import java.sql.*;
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
        
        try {
            /* controllo che non ci sia già un film con lo stesso titolo */
            String sql = "SELECT *" +
                        " FROM FILM " +
                        " WHERE " +
                        " TITOLO ='" + util.Conversion.getDatabaseString(film.getTitolo()) + "'";
            
            DataBase database = DBService.getDataBase();
            ResultSet resultSet = database.select(sql);
            
            if (resultSet.next()) { // se true riporto un errore
                resultSet.close();
                throw new SQLException();
            }

            /* aggiungo il film al database */
            film.setDurata(film.getDurata() + ":00");
            sql = "INSERT INTO `film` " +
                    "(`titolo`, `trailer`, `descrizione`, `durata`, `locandina`) " +
                    "VALUES " +
                    "('" + util.Conversion.getDatabaseString(film.getTitolo()) + "'," +
                    "'" + util.Conversion.getDatabaseString(film.getTrailer()) + "'," +
                    "'" + util.Conversion.getDatabaseString(film.getDescrizione()) + "'," +
                    "'" + film.getDurata() + "'," +
                    "'" + util.Conversion.getDatabaseString(film.getLocandina()) + "');";
            database.modify(sql);
            resultSet.close();
            database.commit();
            database.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void update(FilmModel film) {
        try {
            DataBase database = DBService.getDataBase();

            /* aggiorno il film nel database */
            String sql = "UPDATE `film` " +
                        "SET " +
                        "`trailer`='" + util.Conversion.getDatabaseString(film.getTrailer()) + "'," +
                        "`descrizione`='" + util.Conversion.getDatabaseString(film.getDescrizione()) + "'," +
                        "`durata`='" + util.Conversion.getDatabaseString(film.getDurata()) + "'," +
                        "`locandina`='" + util.Conversion.getDatabaseString(film.getLocandina()) + "' " +
                        "WHERE `titolo`='" + util.Conversion.getDatabaseString(film.getTitolo()) + "';" ;
            database.modify(sql);
            database.commit();
            database.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void delete(String titolo) {
        
    }

    /* metodo per ottenere un film dal titolo */
    public static FilmModel get(String titolo)
            throws NotFoundDBException, ResultSetDBException {
        DataBase database = DBService.getDataBase();
        try {
            FilmModel film = new FilmModel();

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

            return film;
        } catch (SQLException ex) {
            throw new ResultSetDBException("FilmManager: getFilm(): ResultSetDBException: "
                    + ex.getMessage());
        } finally {
            database.close();
        }
    }

    // </editor-fold>
    
    /* metodo per ottenere una lista di film con all'interno il titolo */
    //public static List<FilmModel> getFilmList(/* TODO */) {
    //}
}
