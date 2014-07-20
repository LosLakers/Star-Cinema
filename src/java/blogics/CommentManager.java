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

    public static void add(CommentModel model)
            throws NotFoundDBException {

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
            sql = "INSERT INTO `commenti`" +
                    "(`voto`, `giudizio`, `username`, `id_film`) " +
                    "VALUES " +
                    "('" + model.getVoto() + "'," +
                    "'" + util.Conversion.getDatabaseString(model.getGiudizio()) + "'," +
                    "'" + util.Conversion.getDatabaseString(model.getUsername()) + "'," +
                    "'" + model.getId_film() + "');";
           resultSet = database.modifyPK(sql);
           if (resultSet.next()) {
               model.setId_commento(resultSet.getInt(1));
               resultSet.close();
           }
           database.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            database.close();
        }
    }
}
