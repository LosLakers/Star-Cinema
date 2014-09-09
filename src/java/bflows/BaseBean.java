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

    private String[] week;
    private final LocalDate firstDayOfTheWeek;
    private final LocalDate lastDayOfTheWeek;

    private String message = "";
    private String messagetype = "";

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
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the value of messagetype
     *
     * @return the value of messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * Set the value of messagetype
     *
     * @param messagetype new value of messagetype
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }
    // </editor-fold>
}
