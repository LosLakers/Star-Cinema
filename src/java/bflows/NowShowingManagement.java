package bflows;

import java.beans.*;
import javax.servlet.http.*;
import java.io.Serializable;
import java.util.*;
import java.text.*;

import blogics.*;

/**
 *
 * @author Guido Pio
 */
public class NowShowingManagement implements Serializable {

    private String[] date;

    public NowShowingManagement() {
    }

    public void setFilms() {
        /* creazione lista date a partire da oggi per una settimana */
        this.setDate(NowShowingManager.getPeriod(7));
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public String[] getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(String[] date) {
        this.date = date;
    }

    /**
     * Get the value of date at specified index
     *
     * @param index the index of date
     * @return the value of date at specified index
     */
    public String getDate(int index) {
        return this.date[index];
    }

    /**
     * Set the value of date at specified index.
     *
     * @param index the index of date
     * @param date new value of date at specified index
     */
    public void setDate(int index, String date) {
        this.date[index] = date;
    }

}
