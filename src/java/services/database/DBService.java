
package services.database;

import exceptions.NotFoundDBException;
import java.sql.*;

import global.*;

public class DBService extends Object {
  
  public DBService() {}

  public static synchronized DataBase getDataBase() throws NotFoundDBException {

     try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        Connection connection = DriverManager.getConnection(Constants.DB_CONNECTION_STRING);               
        return new DataBase(connection);
      } catch (NotFoundDBException | ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
        throw new NotFoundDBException("DBService: Impossibile creare la Connessione al DataBase: " + e);
      }
    }

    


}