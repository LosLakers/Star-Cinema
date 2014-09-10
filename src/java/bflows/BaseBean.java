package bflows;

import java.beans.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Guido Pio
 */
public class BaseBean implements Serializable {

    // settimana di programmazione
    private String[] week;
    private final LocalDate firstDayOfTheWeek;
    private final LocalDate lastDayOfTheWeek;

    // gestione errori
    protected enum Message {

        COMMENTERROR,
        COMMENTGETERROR,
        DELETESUCCESS,
        DELETEERROR,
        INSERTSUCCESS,
        INSERTERROR,
        SEARCHERROR,
    }
    private String message = "";
    private String messagetype = "";

    /**
     * Costruttore di default
     */
    public BaseBean() {
        // inizializzo la settimana
        String[] week = new String[7];
        LocalDate day = LocalDate.now();
        for (int i = 0; i < week.length; i++) {
            week[i] = day.format(DateTimeFormatter.ISO_LOCAL_DATE);
            day = day.plusDays(1);
        }
        this.setWeek(week);
        this.firstDayOfTheWeek = LocalDate.now();
        this.lastDayOfTheWeek = day;
    }

    /**
     * Utilizzato per il settaggio dei messaggi di un alert
     *
     * @param message Tipologia di alert dell'enum Message
     */
    protected void setAlert(Message message) {
        switch (message) {
            case COMMENTERROR:
                this.message = "Errore nell'inserimento del commento, riprovare. "
                        + "Se il problema persiste contattare un Amministratore";
                this.messagetype = "alert-danger";
                break;

            case COMMENTGETERROR:
                this.message = "Errore nel recupero messaggi, riprovare. "
                        + "Se il problema persite, contattare un Amministratore";
                this.messagetype = "alert-danger";
                break;

            case DELETESUCCESS:
                this.message = "Eliminazione avvenuta con successo.";
                this.messagetype = "alert-success";
                break;

            case DELETEERROR:
                this.message = "Errore nell'eliminazione, riprovare.";
                this.messagetype = "alert-danger";
                break;

            case INSERTSUCCESS:
                this.message = "Inserimento avvenuto con successo.";
                this.messagetype = "alert-success";
                break;

            case INSERTERROR:
                this.message = "Errore nell'inserimento, riprovare. "
                        + "Se il problema persiste contattare un Amministratore";
                this.messagetype = "alert-danger";
                break;
                
            case SEARCHERROR:
                this.message = "Errore nella ricerca, riprovare. "
                        + "Se il problema persiste contattare un Amministratore";
                this.messagetype = "alert-danger";
                break;
                
            default:
                this.message = "";
                this.messagetype = "";
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of week
     *
     * @return the value of week
     */
    public String[] getWeek() {
        return week;
    }

    /**
     * Set the value of week
     *
     * @param week new value of week
     */
    public void setWeek(String[] week) {
        this.week = week;
    }

    /**
     * Get the value of week at specified index
     *
     * @param index the index of week
     * @return the value of week at specified index
     */
    public String getWeek(int index) {
        return this.week[index];
    }

    /**
     * Set the value of week at specified index.
     *
     * @param index the index of week
     * @param week new value of week at specified index
     */
    public void setWeek(int index, String week) {
        this.week[index] = week;
    }

    /**
     * Get the value of firstDayOfTheWeek
     *
     * @return the value of firstDayOfTheWeek
     */
    public LocalDate getFirstDayOfTheWeek() {
        return firstDayOfTheWeek;
    }

    /**
     * Get the value of lastDayOfTheWeek
     *
     * @return the value of lastDayOfTheWeek
     */
    public LocalDate getLastDayOfTheWeek() {
        return lastDayOfTheWeek;
    }

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Get the value of messagetype
     *
     * @return the value of messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }
    // </editor-fold>
}
