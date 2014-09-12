package exceptions;

import services.database.DataBase;

public class DBException extends Exception {

    protected DataBase database = null;
    protected String logMessage;

    public DBException() {
    }

    public DBException(String msg) {
        this(msg, null);
    }

    /**
     * Constructs an <code>DBException</code> with the specified detail message
     * and the specified DataBase.
     *
     * @param msg the detail message.
     * @param database parametro
     */
    public DBException(String msg, DataBase database) {

        super(msg);
        this.database = database;
        logMessage = " Created Exception: " + msg;
        log();
    }

    public void log() {
        //ErrorLog.databaseErrorLog(logMessage);    
    }
}
