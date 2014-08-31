package bflows;

import blogics.*;
import global.*;
import java.io.Serializable;
import java.util.*;
import java.time.*;
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
    private String[] seat;
    private int topay;

    // film in programmazione
    private FilmDate[] film;
    private String[] week;
    private int[] id_tab;

    // uso per controllo ticket che possono essere acquistati
    private int ticketCounter;
    private String[] reserved;
    private int subscriptionSeat;

    public TicketManagement() {
    }

    /**
     * Creo la lista dei film in programmazione nella settimana con le relative
     * date
     */
    public void index() {
        try {
            // Creo la settimana di programmazione che voglio vedere
            String[] week = new String[7];
            LocalDate day = LocalDate.now();
            for (int i = 0; i < week.length; i++) {
                day = day.plusDays(1);
                week[i] = day.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            this.setWeek(week);
            LocalDate firstDayOfTheWeek = LocalDate.parse(week[0]);
            LocalDate lastDayOfTheWeek = day;

            // recupero film in programmazione
            List<FilmModel> films = FilmManager.getFilms(firstDayOfTheWeek, lastDayOfTheWeek);
            List<FilmDate> filmDate = new ArrayList<>();

            for (FilmModel tmp : films) {
                // recupero le date associate al film
                List<DateTimeModel> date = ShowManager.getDate(tmp, firstDayOfTheWeek, lastDayOfTheWeek);
                FilmDate film = new FilmDate(tmp.getId_film(), tmp.getTitolo(), date);
                filmDate.add(film);
            }
            this.film = filmDate.toArray(new FilmDate[filmDate.size()]);
        } catch (Exception ex) {
            // da gestire
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Acquisto uno o più ticket con controllo sull'uso o meno di un abbonamento
     * 
     * @throws Exception 
     */
    public void addTicket() throws Exception {
        try {
            // formatto i posti in lista di file e array di numeri
            String[] seat = this.getSeat();
            List<String> fila = new ArrayList<>();
            int[] posto = new int[seat.length];
            for (int i = 0; i < seat.length; i++) {
                String[] tmp = seat[i].split("-");
                if (tmp.length != 2) {
                    throw new Exception();
                }
                fila.add(tmp[0]);
                posto[i] = Integer.parseInt(tmp[1]);
            }
            // controllo che il numero di elementi in fila sia uguale a quelli in posto
            if (fila.size() != posto.length) {
                throw new Exception();
            }
            
            // verifico che l'utente possa effettivamente prenotare i posti
            FilmTheaterDateModel show = ShowManager.get(this.getId_tabella());
            List<String> userReserved = TicketManager.getReserved(show, this.getUsername());
            if (userReserved.size() + seat.length > Constants.MAX_TICKETS) {
                throw new Exception();
            }
            
            int[] id_ingresso = TicketManager.add(show, this.getUsername(), fila, posto);
            
            // gestione abbonamento
            if (this.getSubscriptionSeat() > 0 && 
                    this.getSubscriptionSeat() <= Constants.MAX_TICKETS_SUBSCRIPTION) {
                SubscriptionModel subscription = SubscriptionManager.get(this.getUsername());
                int topay = SubscriptionManager.useSubscription(this.getUsername(), subscription, id_ingresso);
                this.setTopay(topay);
            }
        } catch (Exception ex) {
            // gestione eccezione
        }
    }

    // </editor-fold>
    /**
     * Popolo la sala di selezione dei posti. Recupero tutti i posti già
     * occupati da un utente, quelli prenotati da altri utenti e i posti ancora
     * prenotabili dall'utente.
     *
     * @throws java.lang.Exception
     */
    public void populate() throws Exception {
        try {
            // controllo che a id_tabella possa corrispondere id_film - data
            FilmTheaterDateModel model = ShowManager.get(this.getId_tabella());
            FilmModel film = model.getFilm();
            DateTimeModel data = model.getDate();
            if (this.getId_film() != film.getId_film()
                    || !this.getData().equals(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                throw new Exception();
            }

            FilmTheaterDateModel show = ShowManager.get(this.getId_tabella());
            List<String> userReserved = TicketManager.getReserved(show, this.getUsername());
            // diminuisco contatore in base a reserved
            if (userReserved != null) {
                this.setTicketCounter(Constants.MAX_TICKETS - userReserved.size());
            } else {
                this.setTicketCounter(Constants.MAX_TICKETS);
            }
            // recupero abbonamento - null se non esiste
            SubscriptionModel subscription = SubscriptionManager.get(this.getUsername());
            if (subscription != null) {
                // setto valori per posti disponibili
                this.setSubscriptionSeat(subscription.getIngressi_disp());
            } else {
                this.setSubscriptionSeat(-1);
            }

            // recupero i posti prenotati - formato [FILA_NUM]
            List<String> reserved = TicketManager.getReserved(show);
            this.setReserved(reserved.toArray(new String[reserved.size()]));
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Restituisco gli orari disponibili per l'acquisto dei biglietti del film
     * specificato nella data inserita
     *
     * @return L'array con gli orari
     * @throws java.lang.Exception
     */
    public String[] populateTime() throws Exception {
        List<String> model = new ArrayList<>();
        try {
            int[] id_tab = ShowManager.get(this.getId_film(), LocalDate.parse(this.getData()));
            /*
             Lancio una eccezione se qualcuno prova a inserire un film o una data
             che non è segnata in programmazione
             */
            if (id_tab.length == 0) {
                throw new Exception();
            }
            List<FilmTheaterDateModel> lista = new ArrayList<>();
            for (int i : id_tab) {
                FilmTheaterDateModel tmp = ShowManager.get(i);
                lista.add(tmp);
            }
            this.setId_tab(id_tab);

            for (FilmTheaterDateModel tmp : lista) {
                TheaterModel theater = tmp.getTheater();
                DateTimeModel date = tmp.getDate();
                String stringa = date.getOra_inizio().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        + " - " + date.getOra_fine().format(DateTimeFormatter.ISO_LOCAL_TIME)
                        + " - Sala #" + theater.getNumero_sala() + " - Posti " + theater.getPosti_disp();
                model.add(stringa);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return model.toArray(new String[model.size()]);
    }

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom FilmDate ">
    /**
     * Recupero la lista di tutti i film in programmazione
     *
     * @return L'array con tutti gli id_film
     */
    public int[] ticketId_film() {
        int[] id_film = new int[this.film.length];
        for (int i = 0; i < id_film.length; i++) {
            id_film[i] = this.film[i].getId_film();
        }
        return id_film;
    }

    /**
     * Recupero il titolo del film in base all'id_film
     *
     * @param id_film Id del film di cui voglio recuperare il titolo
     * @return Il titolo del film
     */
    public String ticketTitolo(int id_film) {
        FilmDate[] film = this.film;
        String titolo = "";
        for (FilmDate tmp : film) {
            if (tmp.getId_film() == id_film) {
                titolo = tmp.getTitolo();
                break;
            }
        }
        return titolo;
    }

    public String[] ticketDate(int id_film) {
        String[] date = null;
        for (FilmDate tmp : this.film) {
            if (tmp.getId_film() == id_film) {
                date = tmp.getDate();
                break;
            }
        }
        return date;
    }
    // </editor-fold>

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
     * Get the value of seat
     *
     * @return the value of seat
     */
    public String[] getSeat() {
        return seat;
    }

    /**
     * Set the value of seat
     *
     * @param seat new value of seat
     */
    public void setSeat(String[] seat) {
        this.seat = seat;
    }

    /**
     * Get the value of seat at specified index
     *
     * @param index the index of seat
     * @return the value of seat at specified index
     */
    public String getSeat(int index) {
        return this.seat[index];
    }

    /**
     * Set the value of seat at specified index.
     *
     * @param index the index of seat
     * @param seat new value of seat at specified index
     */
    public void setSeat(int index, String seat) {
        this.seat[index] = seat;
    }

    /**
     * Get the value of topay
     *
     * @return the value of topay
     */
    public int getTopay() {
        return topay;
    }

    /**
     * Set the value of topay
     *
     * @param topay new value of topay
     */
    public void setTopay(int topay) {
        this.topay = topay;
    }

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
     * Get the value of id_tab
     *
     * @return the value of id_tab
     */
    public int[] getId_tab() {
        return id_tab;
    }

    /**
     * Set the value of id_tab
     *
     * @param id_tab new value of id_tab
     */
    public void setId_tab(int[] id_tab) {
        this.id_tab = id_tab;
    }

    /**
     * Get the value of id_tab at specified index
     *
     * @param index the index of id_tab
     * @return the value of id_tab at specified index
     */
    public int getId_tab(int index) {
        return this.id_tab[index];
    }

    /**
     * Set the value of id_tab at specified index.
     *
     * @param index the index of id_tab
     * @param id_tab new value of id_tab at specified index
     */
    public void setId_tab(int index, int id_tab) {
        this.id_tab[index] = id_tab;
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

    /**
     * Get the value of subscriptionSeat
     *
     * @return the value of subscriptionSeat
     */
    public int getSubscriptionSeat() {
        return subscriptionSeat;
    }

    /**
     * Set the value of subscriptionSeat
     *
     * @param subscriptionSeat new value of subscriptionSeat
     */
    public void setSubscriptionSeat(int subscriptionSeat) {
        this.subscriptionSeat = subscriptionSeat;
    }

    // </editor-fold>
}

class FilmDate {

    private int id_film;
    private String titolo;
    private String[] date;

    public FilmDate(int id_film, String titolo, List<DateTimeModel> date) {
        this.setId_film(id_film);
        this.setTitolo(titolo);

        List<String> model = new ArrayList<>();
        String help = "2000-01-01";
        for (DateTimeModel data : date) {
            String day = data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE);
            // evito di aggiungere date uguali
            if (!day.equals(help)) {
                model.add(day);
                help = day; // new String(day)
            }
        }
        this.setDate(model.toArray(new String[model.size()]));
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
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
     * Get the value of titolo
     *
     * @return the value of titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Set the value of titolo
     *
     * @param titolo new value of titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
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

    // </editor-fold>
}
