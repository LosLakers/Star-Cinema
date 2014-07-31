package blogics;

import exceptions.*;
import java.sql.*;
import java.util.*;
import services.database.*;

/**
 *
 * @author Guido Pio
 */
public class CommentManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
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
            throw ex;
        } finally {
            database.close();
        }
    }
    
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
            throw ex;
        } finally {
            database.close();
        }
    }
    
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
            throw ex;
        } finally {
            database.close();
        }
    }

    public static CommentModel get(String user, int id_film) 
            throws NotFoundDBException, SQLException {
        
        DataBase database = DBService.getDataBase();
        CommentModel commento = new CommentModel();
        try {
            String sql = "SELECT * " +
                        "FROM `commenti` " +
                        "WHERE " +
                        "`id_film`='" + id_film + "' AND " +
                        "`username`='" + util.Conversion.getDatabaseString(user) + "';";
            ResultSet resultSet = database.select(sql);
            if (resultSet.next()) {
                commento = new CommentModel(resultSet);
            }
            database.commit();
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
        }
        return commento;
    }
    
    public static CommentModel[] getCommenti(int id_film) 
            throws NotFoundDBException, SQLException {
        
        DataBase database = DBService.getDataBase();
        CommentModel[] commenti = null;
        try {
            // recupero la lista dei commenti per un dato film
            List<CommentModel> commentList;
            commentList = new ArrayList<>();
            String sql = "SELECT * " +
                        "FROM `commenti` " +
                        "WHERE " +
                        "`id_film`='" + id_film + "';";
            ResultSet resultSet = database.select(sql);
            // creo il vettore con i commenti trovati
            while(resultSet.next()) {
                CommentModel tmp = new CommentModel(resultSet);
                commentList.add(tmp);
            }
            resultSet.close();
            database.commit();
            commenti = new CommentModel[commentList.size()];
            for (int i = 0; i < commenti.length; i++) {
                commenti[i] = commentList.get(i);
            }
        } catch (NotFoundDBException | SQLException ex) {
            throw ex;
        } finally {
            database.close();
            
        }
        return commenti;
    }
    
    // </editor-fold>
}
