package global;

public class Constants {

    /* Constants for StarCinema */
    public static final int NUMERO_SALE = 4;
    
    /* Constants for language codes */
    public static final String CD_LANGUAGE_ITALIAN = "ITA";
    public static final String CD_LANGUAGE_ENGLISH = "ENG";

    /* Contants for Mail Service.
     * Choose between (YES,NO,TEST).
     */
    public static final String ENABLE_MAIL_SERVICE = "SI";
    public static final String TEST_MAIL = "null@null.com";
    public static final String[] MAIL_GATEWAYS = {"192.168.0.1"};

    /* Constants for Debug */
    public static final boolean DEBUG = true;

    /**
     * Constants for db connection
     */
    public static final String DB_USER_NAME = "root";
    public static final String DB_PASSWORD = "";
    public static final String DB_CONNECTION_STRING = "jdbc:mysql://localhost/starcinema?user=" + DB_USER_NAME + "&password=" + DB_PASSWORD;

    /* Constants for log files*/
    public final static String LOG_DIR = "C:\\logs\\";
    public final static String FRONTEND_ERROR_LOG_FILE = LOG_DIR + "frontenderror.log";
    public final static String FATAL_LOG_FILE = LOG_DIR + "fatalerror.log";
    public final static String GENERAL_LOG_FILE = LOG_DIR + "generalerror.log";
    public final static String GENERAL_EXCEPTION_LOG_FILE = LOG_DIR + "generalexception.log";
    public final static String WARNING_LOG_FILE = LOG_DIR + "warning.log";
    public final static String DATABASE_SERVICE_LOG_FILE = LOG_DIR + "databaseservice.log";
    public final static String MAIL_SERVICE_LOG_FILE = LOG_DIR + "mailservice.log";
    public static final String APPLICATION_MANAGER_MAIL = "null@null.com";

}
