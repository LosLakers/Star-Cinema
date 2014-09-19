package bflows;

import blogics.*;
import exceptions.NotFoundDBException;
import global.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * JavaBean per la gestione di un Ingresso
 */
public class TicketManagement extends BaseBean implements Serializable {

    private int id_ingresso;
    private String username;
    private int id_film;
    private String data;
    private int id_tabella;
    private String[] seat;
    private int topay;

    // film in programmazione
    private FilmDate[] film;
    private int[] id_tab;

    // ticket utente
    private Ticket[] tickets;

    // uso per controllo ticket che possono essere acquistati
    private int ticketCounter;
    private String[] reserved;
    private int subscriptionSeat;

    public TicketManagement() {
        super();
    }

    /**
     * Creo la lista dei film in programmazione nella settimana con le relative
     * date
     */
    public void index() {
        try {
            // recupero film in programmazione
            List<FilmModel> films = FilmManager.getFilms(this.getFirstDayOfTheWeek(),
                    this.getLastDayOfTheWeek());
            List<FilmDate> filmDate = new ArrayList<>();

            for (FilmModel tmp : films) {
                // recupero le date associate al film
                List<DateTimeModel> date = ShowManager.getDate(tmp, this.getFirstDayOfTheWeek(),
                        this.getLastDayOfTheWeek());
                FilmDate film = new FilmDate(tmp, date);
                filmDate.add(film);
            }
            this.film = filmDate.toArray(new FilmDate[filmDate.size()]);
        } catch (NotFoundDBException | SQLException ex) {
            // da gestire
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Acquisto uno o più ticket con controllo sull'uso o meno di un abbonamento
     *
     * @throws Exception Eccezione
     */
    public void addTicket() throws Exception {
        try {
            // formatto i posti in lista di file e array di numeri
            String[] seat = this.getSeat();
            List<SeatModel> seats = new ArrayList<>();
            for (int i = 0; i < seat.length; i++) {
                String[] tmp = seat[i].split("-");
                if (tmp.length != 2) {
                    throw new Exception();
                }
                SeatModel model = new SeatModel(0, tmp[0], Integer.parseInt(tmp[1]), 0);
                seats.add(model);
            }

            // verifico che l'utente possa effettivamente prenotare i posti
            FilmTheaterDateModel show = ShowManager.get(this.getId_tabella());
            List<SeatModel> userReserved = TicketManager.getReserved(show, this.getUsername());
            if (userReserved.size() + seat.length > Constants.MAX_TICKETS) {
                throw new Exception();
            }

            int[] id_ingresso = TicketManager.add(show, this.getUsername(), seats);

            // gestione abbonamento
            if (this.getSubscriptionSeat() > 0
                    && this.getSubscriptionSeat() <= Constants.MAX_TICKETS_SUBSCRIPTION) {
                SubscriptionModel subscription = SubscriptionManager.get(this.getUsername());
                int topay = SubscriptionManager.useSubscription(this.getUsername(), subscription, id_ingresso);
                this.setTopay(topay);
            } else {
                this.setTopay(id_ingresso.length);
            }
        } catch (Exception ex) {
            this.setAlert(Message.TICKETADDERROR);
            throw ex;
        }
    }

    /**
     * Aggiorno il ticket con le nuove informazioni
     *
     * @throws java.lang.Exception Eccezione
     */
    public void updateTicket() throws Exception {
        try {
            FilmTheaterDateModel model = ShowManager.get(this.getId_tabella());
            String[] tmpseat = this.getSeat();
            // controllo che sia stato selezionato un solo posto
            if (seat.length > 1 || seat.length == 0) {
                throw new Exception();
            }
            tmpseat = tmpseat[0].split("-");
            // recupero 
            TheaterModel theater = model.getTheater();
            SeatModel seat = new SeatModel(0, tmpseat[0], Integer.parseInt(tmpseat[1]),
                    theater.getId_sala());

            FilmModel film = model.getFilm();
            DateTimeModel date = model.getDate();
            TicketModel ticket = TicketManager.get(this.getId_ingresso());
            TicketManager.update(ticket, film.getId_film(), date.getId_data(),
                    seat);
        } catch (Exception ex) {
            this.setAlert(Message.UPDATEERROR);
            throw ex;
        }
    }

    /**
     * Recupero l'ingresso a partire dal suo id.
     */
    public void getTicket() {
        try {
            TicketModel ticket = TicketManager.get(this.getId_ingresso());
            if (ticket != null) {
                this.setId_film(ticket.getId_film());
                DateTimeModel data = ShowManager.getData(ticket.getId_data());
                this.setData(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        } catch (Exception ex) {
            // da gestire
        }
    }

    /**
     * Recupero tutti gli ingressi associati ad un utente
     *
     * @throws exceptions.NotFoundDBException Eccezione
     * @throws java.sql.SQLException Eccezione
     */
    public void getTicketList() throws NotFoundDBException, SQLException {
        try {
            // recupero la lista dei ticket associati ad uno username
            List<TicketModel> ticketlist = TicketManager.get(this.getUsername());

            // creo un modello da passare alla jsp
            List<Ticket> array = new ArrayList<>();
            for (TicketModel ticket : ticketlist) {
                DateTimeModel data = ticket.getData();
                FilmModel film = ticket.getFilm();
                SeatModel seat = ticket.getSeat();

                Ticket tmp = new Ticket(ticket.getId_ingresso(), data, film, seat);
                array.add(tmp);
            }

            // setto il modello
            this.setTickets(array.toArray(new Ticket[array.size()]));
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.TICKETLISTERROR);
            throw ex;
        }
    }
    // </editor-fold>

    /**
     * Popolo la sala di selezione dei posti. Recupero tutti i posti già
     * occupati da un utente, quelli prenotati da altri utenti e i posti ancora
     * prenotabili dall'utente.
     *
     * @throws java.lang.Exception Eccezione
     */
    public void populateAdd() throws Exception {
        try {
            // controllo che a id_tabella possa corrispondere id_film - data
            FilmTheaterDateModel show = ShowManager.get(this.getId_tabella());
            FilmModel film = show.getFilm();
            DateTimeModel data = show.getDate();
            if (this.getId_film() != film.getId_film()
                    || !this.getData().equals(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                throw new Exception();
            }

            List<SeatModel> userReserved = TicketManager.getReserved(show, this.getUsername());
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

            // recupero i posti prenotati - creo formato [FILA-NUM]
            List<SeatModel> reserved = TicketManager.getReserved(show);
            String[] array = new String[reserved.size()];
            for (int i = 0; i < reserved.size(); i++) {
                SeatModel tmp = reserved.get(i);
                array[i] = tmp.getFila() + "-" + tmp.getNumero();
            }
            this.setReserved(array);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Popolo la sala di selezione dei posti. Recupero tutti i posti già
     * occupati da un utente, quelli prenotati da altri utenti e i posti ancora
     * prenotabili dall'utente.
     *
     * @throws Exception Eccezione
     */
    public void populateUpdate() throws Exception {
        try {
            // controllo che a id_tabella possa corrispondere id_film - data
            FilmTheaterDateModel show = ShowManager.get(this.getId_tabella());
            FilmModel film = show.getFilm();
            DateTimeModel data = show.getDate();
            if (this.getId_film() != film.getId_film()
                    || !this.getData().equals(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE))) {
                throw new Exception();
            }

            // solo un biglietto è prenotabile
            this.setTicketCounter(1);

            // recupero i posti prenotati - creo formato [FILA-NUM]
            List<SeatModel> reserved = TicketManager.getReserved(show);
            String[] array = new String[reserved.size()];
            for (int i = 0; i < reserved.size(); i++) {
                SeatModel tmp = reserved.get(i);
                array[i] = tmp.getFila() + "-" + tmp.getNumero();
            }
            this.setReserved(array);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Restituisco gli orari disponibili per l'acquisto dei biglietti del film
     * specificato nella data inserita
     *
     * @return L'array con gli orari
     * @throws java.lang.Exception Eccezione
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String stringa = date.getOra_inizio().format(formatter)
                        + " - " + date.getOra_fine().format(formatter)
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
    public int[] FilmDate_IdFilm() {
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
    public String FilmDate_Titolo(int id_film) {
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

    /**
     * Recupero la locandina di un film in base a di_film
     *
     * @param id_film Id del film di cui voglio recuperare la locandina
     * @return La locandina del film
     */
    public String FilmDate_Locandina(int id_film) {
        FilmDate[] film = this.film;
        String locandina = "";
        for (FilmDate tmp : film) {
            if (tmp.getId_film() == id_film) {
                locandina = tmp.getLocandina();
                break;
            }
        }
        return locandina;
    }

    /**
     * Recupero la lista delle date in base all'id del film
     *
     * @param id_film Id del film
     * @return Array delle date del film
     */
    public String[] FilmDate_Date(int id_film) {
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

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom Ticket ">
    /**
     * Recupero lunghezza array Ticket
     *
     * @return Il numero di elementi nell'array
     */
    public int ticket_Length() {
        return this.tickets.length;
    }

    /**
     * Recupero l'id di un elemento di Ticket
     *
     * @param index Indice dell'elemento
     * @return Id dell'elemento
     */
    public int ticket_IdIngresso(int index) {
        return this.tickets[index].getId_ingresso();
    }

    /**
     * Recupero la data di un elemento di Ticket
     *
     * @param index Indice dell'elemento
     * @return La data dell'elemento
     */
    public String ticket_Data(int index) {
        return this.tickets[index].getData();
    }

    /**
     * Recupero il titolo del film di un elemento di Ticket
     *
     * @param index Indice dell'elemento
     * @return Il titolo del film dell'elemento
     */
    public String ticket_Titolo(int index) {
        return this.tickets[index].getTitolo();
    }

    /**
     * Recupero la sala di un elemento di Ticket
     *
     * @param index Indice dell'elemento
     * @return La sala dell'elemento
     */
    public String ticket_Sala(int index) {
        return this.tickets[index].getSala();
    }

    /**
     * Recupero l'orario di un elemento di Ticket
     *
     * @param index Indice dell'elemento
     * @return L'orario di un elemento
     */
    public String ticket_Orario(int index) {
        return this.tickets[index].getOrario();
    }

    /**
     * Recupero il posto di un elemento di Ticket
     *
     * @param index Indice dell'elemento
     * @return Il posto di un elemento
     */
    public String ticket_Posto(int index) {
        return this.tickets[index].getPosto();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_ingresso
     *
     * @return the value of id_ingresso
     */
    public int getId_ingresso() {
        return id_ingresso;
    }

    /**
     * Set the value of id_ingresso
     *
     * @param id_ingresso new value of id_ingresso
     */
    public void setId_ingresso(int id_ingresso) {
        this.id_ingresso = id_ingresso;
    }

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
     * Get the value of tickets
     *
     * @return the value of tickets
     */
    public Ticket[] getTickets() {
        return tickets;
    }

    /**
     * Set the value of tickets
     *
     * @param tickets new value of tickets
     */
    public void setTickets(Ticket[] tickets) {
        this.tickets = tickets;
    }

    /**
     * Get the value of tickets at specified index
     *
     * @param index the index of tickets
     * @return the value of tickets at specified index
     */
    public Ticket getTickets(int index) {
        return this.tickets[index];
    }

    /**
     * Set the value of tickets at specified index.
     *
     * @param index the index of tickets
     * @param tickets new value of tickets at specified index
     */
    public void setTickets(int index, Ticket tickets) {
        this.tickets[index] = tickets;
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
    private String locandina;
    private String[] date;

    /**
     * Costruttore della classe
     *
     * @param film Il film da inserire
     * @param date Lista date e orari del film
     */
    public FilmDate(FilmModel film, List<DateTimeModel> date) {
        this.setId_film(film.getId_film());
        this.setTitolo(film.getTitolo());
        this.setLocandina(film.getLocandina());

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
     * Get the value of locandina
     *
     * @return the value of locandina
     */
    public String getLocandina() {
        return locandina;
    }

    /**
     * Set the value of locandina
     *
     * @param locandina new value of locandina
     */
    public void setLocandina(String locandina) {
        this.locandina = locandina;
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

class Ticket {

    private int id_ingresso;
    private String data;
    private String titolo;
    private String sala;
    private String posto;
    private String orario;

    /**
     * Costruttore della classe
     *
     * @param id_ingresso Identificativo del ticket
     * @param data Data e orario del ticket
     * @param film Film del ticket
     * @param seat Posto del ticket
     */
    public Ticket(int id_ingresso, DateTimeModel data, FilmModel film, SeatModel seat) {
        this.setId_ingresso(id_ingresso);
        this.setData(data.getData().format(DateTimeFormatter.ISO_LOCAL_DATE));
        this.setTitolo(film.getTitolo());
        TheaterModel theater = seat.getTheater();
        this.setSala("Sala #" + theater.getNumero_sala());
        this.setPosto(seat.getFila() + "-" + seat.getNumero());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.setOrario(data.getOra_inizio().format(formatter) + "-"
                + data.getOra_fine().format(formatter));
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_ingresso
     *
     * @return the value of id_ingresso
     */
    public int getId_ingresso() {
        return id_ingresso;
    }

    /**
     * Set the value of id_ingresso
     *
     * @param id_ingresso new value of id_ingresso
     */
    public void setId_ingresso(int id_ingresso) {
        this.id_ingresso = id_ingresso;
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
     * Get the value of sala
     *
     * @return the value of sala
     */
    public String getSala() {
        return sala;
    }

    /**
     * Set the value of sala
     *
     * @param sala new value of sala
     */
    public void setSala(String sala) {
        this.sala = sala;
    }

    /**
     * Get the value of posto
     *
     * @return the value of posto
     */
    public String getPosto() {
        return posto;
    }

    /**
     * Set the value of posto
     *
     * @param posto new value of posto
     */
    public void setPosto(String posto) {
        this.posto = posto;
    }

    /**
     * Get the value of orario
     *
     * @return the value of orario
     */
    public String getOrario() {
        return orario;
    }

    /**
     * Set the value of orario
     *
     * @param orario new value of orario
     */
    public void setOrario(String orario) {
        this.orario = orario;
    }
    // </editor-fold>
}
