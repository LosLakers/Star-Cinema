package blogics;

import exceptions.NotFoundDBException;
import exceptions.ResultSetDBException;
import java.sql.*;
import services.database.*;
import util.*;

public class UserManager {

    // <editor-fold defaultstate="collapsed" desc="CRUD">
    
    public static void update(UserModel user) {
        try {
            DataBase database = DBService.getDataBase();
            
            String sql = "UPDATE `utenti` " +
                        "SET `name`='" + util.Conversion.getDatabaseString(user.getName()) + "'," +
                        "`surname`='" + util.Conversion.getDatabaseString(user.getSurname()) + "'," +
                        "`email`='" + util.Conversion.getDatabaseString(user.getEmail()) + "'," +
                        "`credit_card`='" + util.Conversion.getDatabaseString(user.getCreditcard()) + "' " +
                        "WHERE `username`='" + util.Conversion.getDatabaseString(user.getUsername()) + "'";
            database.modify(sql);
            database.commit();
            database.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* metodo per ottenere l'user dal database */
    public static UserModel get(String username, String password)
            throws NotFoundDBException, ResultSetDBException {
        
        DataBase database = DBService.getDataBase();
        try {
            UserModel user = null;
            
            String sql = " SELECT * "
                    + " FROM UTENTI "
                    + " WHERE "
                    + " USERNAME = '" + util.Conversion.getDatabaseString(username) + "'";
            
            ResultSet result = database.select(sql);
            if (result.next()) { // true c'è un'altra riga
                user = new UserModel(result);
                user.setAdmin(false);
            }
            result.close();

            /* TODO */
            if (user == null) {
                sql = " SELECT * "
                        + " FROM ADMIN "
                        + " WHERE "
                        + " USERNAME = '" + util.Conversion.getDatabaseString(username) + "'";
                result = database.select(sql);
                if (result.next()) { // true c'è un'altra riga
                    user = new UserModel(result);
                    user.setAdmin(true);
                }
                result.close();
            }
            
            database.commit();
            /* se ho trovato un utente e combacia con la password allora è l'utente che mi serve */
            if (user == null || !user.getPassword().equals(password)) {
                return null;
            }
            return user;
        } catch (SQLException ex) {
            throw new ResultSetDBException("UserManager: getUser(): ResultSetDBException: "
                    + ex.getMessage());
        } finally {
            database.close();
        }
    }

    // </editor-fold>
}
