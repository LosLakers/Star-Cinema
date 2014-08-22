package bflows;

import global.*;
import java.io.Serializable;
import java.util.*;
import blogics.*;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Guido Pio
 */
public class TicketManagement implements Serializable {

    private String username;
    private int id_film;
    private String data;
    private int id_tabella;

    // uso per controllo ticket che possono essere acquistati
    private int ticketCounter;
    private String[] reserved;

    public TicketManagement() {
    }

    public void populate() {
        try {
            // controllo che a id_tabella possa corrispondere id_film - data
            FilmTheaterDateModel model = ShowManager.get(this.getId_tabella());
            FilmModel film = model.getFilm();
            DateTimeModel data = model.getDate();
            if (this.getId_film() != film.getId_film()
                    || !this.getData().equals(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                throw new Exception();
            }

            List<String> userReserved = TicketManager.getReserved(this.getId_tabella(), this.getUsername());
            // diminuisco contatore in base a reserved
            if (userReserved != null) {
                this.setTicketCounter(Constants.MAX_TICKETS - userReserved.size());
            } else {
                this.setTicketCounter(Constants.MAX_TICKETS);
            }
            // recupero abbonamento - null se non esiste
            SubscriptionModel subscription = TicketManager.getSubscription(this.getUsername());
            if (subscription != null) {
                // setto valori per posti disponibili
            } else {
                // setto valori negativi per posti disponibili
            }

            // recupero i posti prenotati - formato [FILA_NUM]
            List<String> reserved = TicketManager.getReserved(this.getId_tabella());
            this.setReserved(reserved.toArray(new String[reserved.size()]));
        } catch (Exception ex) {
            /* 
             ritorno alla pagina di scelta di orario-sala con un messaggio di errore
             e passando come parametri id_film e data che ho.
             */
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of username
     *
     * @return the value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the value of username
     *
     * @param username new value of username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the value of id_film
     *
     * @return the value of id_film
     */
    public int getId_film() {
        return id_film;
    }

    /**
     * Set the value of id_film
     *
     * @param id_film new value of id_film
     */
    public void setId_film(int id_film) {
        this.id_film = id_film;
    }

    /**
     * Get the value of data
     *
     * @return the value of data
     */
    public String getData() {
        return data;
    }

    /**
     * Set the value of data
     *
     * @param data new value of data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Get the value of id_tabella
     *
     * @return the value of id_tabella
     */
    public int getId_tabella() {
        return id_tabella;
    }

    /**
     * Set the value of id_tabella
     *
     * @param id_tabella new value of id_tabella
     */
    public void setId_tabella(int id_tabella) {
        this.id_tabella = id_tabella;
    }

    /**
     * Get the value of ticketCounter
     *
     * @return the value of ticketCounter
     */
    public int getTicketCounter() {
        return ticketCounter;
    }

    /**
     * Set the value of ticketCounter
     *
     * @param ticketCounter new value of ticketCounter
     */
    public void setTicketCounter(int ticketCounter) {
        this.ticketCounter = ticketCounter;
    }

    /**
     * Get the value of reserved
     *
     * @return the value of reserved
     */
    public String[] getReserved() {
        return reserved;
    }

    /**
     * Set the value of reserved
     *
     * @param reserved new value of reserved
     */
    public void setReserved(String[] reserved) {
        this.reserved = reserved;
    }

    /**
     * Get the value of reserved at specified index
     *
     * @param index the index of reserved
     * @return the value of reserved at specified index
     */
    public String getReserved(int index) {
        return this.reserved[index];
    }

    /**
     * Set the value of reserved at specified index.
     *
     * @param index the index of reserved
     * @param reserved new value of reserved at specified index
     */
    public void setReserved(int index, String reserved) {
        this.reserved[index] = reserved;
    }

    // </editor-fold>
}
