package services.database;

import exceptions.NotFoundDBException;
import java.io.*;
import java.sql.*;

public class DataBase {

    private Connection connection;
    private Statement statement;

    /**
     * Costruttore database
     *
     * @param connection Connessione
     * @throws NotFoundDBException Eccezione
     */
    public DataBase(Connection connection) throws NotFoundDBException {

        try {
            this.connection = connection;
            if (this.connection == null) {
                throw new NotFoundDBException("DataBase: DataBase(): Impossibile Aprire la Connessione Logica");
            }

            this.connection.setAutoCommit(false);
            statement = this.connection.createStatement();

        } catch (NotFoundDBException | SQLException ex) {
            ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
            ex.printStackTrace(new PrintWriter(stackTrace, true));
            throw new NotFoundDBException("DataBase: DataBase(): Impossibile Aprire la Connessione col DataBase: \n"
                    + stackTrace);
        }

    }

    /**
     * Esegue una select
     *
     * @param sql Stringa sql per la select
     * @return Risultato
     * @throws NotFoundDBException Eccezione
     */
    public ResultSet select(String sql) throws NotFoundDBException {

        ResultSet resultSet;

        try {
            resultSet = statement.executeQuery(sql);
            return resultSet;
        } catch (SQLException ex) {
            throw new NotFoundDBException("DataBase: select(): Impossibile eseguire la query sul DB. Eccezione: "
                    + ex + "\n " + sql, this);
        }
    }

    /**
     * Esegue un Insert o un Update nel database
     *
     * @param sql Stringa sql
     * @return Il numero di records inseriti/aggiornati
     * @throws NotFoundDBException Eccezione
     */
    public int modify(String sql) throws NotFoundDBException {

        int recordsNumber;
        try {
            recordsNumber = statement.executeUpdate(sql);
        } catch (SQLException ex) {

            throw new NotFoundDBException("DataBase: modify(): Impossibile eseguire la update sul DB. Eccezione: "
                    + ex + "\n " + sql, this);

        }

        return recordsNumber;

    }

    /**
     * Restituisce una istanza di ResultSet contente la colonna con la chiave
     * auto-generata.
     *
     * @param sql Stringa sql
     * @return La chiave primaria generata
     * @throws NotFoundDBException Eccezione
     */
    public ResultSet modifyPK(String sql) throws NotFoundDBException {

        int recordsNumber;
        try {
            recordsNumber = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            return resultSet;
        } catch (SQLException ex) {

            throw new NotFoundDBException("DataBase: modify(): Impossibile eseguire la update sul DB. Eccezione: "
                    + ex + "\n " + sql, this);
        }
    }

    /**
     * Esegue il commit nel database
     *
     * @throws NotFoundDBException Eccezione
     */
    public void commit() throws NotFoundDBException {

        try {

            connection.commit();
            statement.close();
            statement = connection.createStatement();
            return;

        } catch (SQLException ex) {
            throw new NotFoundDBException("DataBase: commit(): Impossibile eseguire la commit sul DB. Eccezione: "
                    + ex, this);
        }

    }

    /**
     * Esegue il rollback nel database
     */
    public void rollBack() {

        try {
            connection.rollback();
            statement.close();
            statement = connection.createStatement();
            return;
        } catch (SQLException ex) {
            new NotFoundDBException("DataBase: rollback(): Impossibile eseguire il RollBack sul DB. Eccezione: "
                    + ex);
        }
    }

    /**
     * chiude il database
     *
     * @throws NotFoundDBException Eccezione
     */
    public void close() throws NotFoundDBException {
        try {
            connection.close();
        } catch (SQLException ex) {
            throw new NotFoundDBException("DataBase: close(): Impossibile chiudere il DB. Eccezione: "
                    + ex);
        }
    }

    protected void finalize() throws Throwable {
        this.close();
    }

}
