package bflows;

import blogics.*;
import exceptions.*;
import global.*;
import java.io.Serializable;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.sql.*;

/**
 * JavaBean per la gestione della programmazione e dei vari spettacoli
 */
public class NowShowingManagement extends BaseBean implements Serializable {

    // proprietà da ottenere
    private int id_tabella;
    private int id_film;
    private int sala;
    private String data;
    private String ora_inizio;
    private String ora_fine;

    // proprietà da settare per occupazione posti
    private TheaterDate[] theaterDate;
    private String titolo_film;
    private String durata_film;

    // proprietà da settare per programmazione film
    private FilmTheaterDate[] filmTheaterDate;

    // proprietà da settare per lista film da aggiungere in programmazione
    private String[] films;

    public NowShowingManagement() {
        super();
    }

    /**
     * Recupero la lista di tutti i film che posso inserire in programmazione
     */
    public void index() {
        try {
            List<FilmModel> films = FilmManager.searchFilm("");
            String[] array = new String[films.size()];
            for (int i = 0; i < films.size(); i++) {
                FilmModel model = films.get(i);
                String film = model.getId_film() + "_" + model.getTitolo();
                array[i] = film;
            }
            this.setFilms(array);
        } catch (NotFoundDBException | SQLException ex) {
            // da gestire
        }
    }

    /**
     * Creo la programmazione per ogni singola sala popolando l'array
     * theaterDate
     */
    public void populateTheater() {
        try {
            /*
             Costruisco un numero di array di TheaterDateModel pari al numero
             delle posti del cinema. Per ogni array ricavo data e orario
             ordinando gli elementi prima per data e poi per orario.
             */
            TheaterDate[] model = new TheaterDate[Constants.NUMERO_SALE];
            for (int i = 0; i < Constants.NUMERO_SALE; i++) {
                TheaterModel sala = new TheaterModel(0, 0, i + 1);
                List<DateTimeModel> datetime = ShowManager.getDate(sala.getNumero_sala(),
                        this.getFirstDayOfTheWeek(), this.getLastDayOfTheWeek());
                List<FilmModel> film = new ArrayList<>();
                int[] posti_disp = new int[datetime.size()];
                for (int j = 0; j < datetime.size(); j++) {
                    DateTimeModel date = datetime.get(j);
                    // recupero il film con id_data e numero della sala [UNICO]
                    film.add(FilmManager.get(date.getId_data(), sala.getNumero_sala()));
                    /* 
                     recupero i posti disponibili della sala con id_film, id_data e
                     numero sala predefiniti [UNICO]
                     */
                    posti_disp[j] = TicketManager.getSeat(film.get(j).getId_film(),
                            date.getId_data(), sala.getNumero_sala());
                }
                model[i] = new TheaterDate(sala, datetime, film, posti_disp);
            }
            // ricrea la proprietà con i get e set private in modo che non possano essere usati fuori
            this.theaterDate = model;

            /*
             Recupero film che voglio inserire nella programmazione dal
             id_film. Le uniche proprietà che per ora voglio visualizzare sono
             Titolo e Durata.
             */
            FilmModel film = FilmManager.get(this.getId_film());
            this.setTitolo_film(film.getTitolo());
            this.setDurata_film(film.getDurata().toString());
        } catch (Exception ex) {
            // gestione messaggi di errore
        }
    }

    /**
     * Creo la programmazione popolando l'array filmTheaterDate
     */
    public void populate() {
        try {
            // recupero la lista dei film in programmazione
            List<FilmModel> films = FilmManager.getFilms(this.getFirstDayOfTheWeek(),
                    this.getLastDayOfTheWeek());
            List<FilmTheaterDate> filmTheaterDate = new ArrayList<>();

            for (FilmModel tmp : films) {
                // recupero l'array sala-data relativo al film
                List<TheaterDateModel> model = ShowManager.getShowList(tmp, this.getFirstDayOfTheWeek(),
                        this.getLastDayOfTheWeek());

                // recupero id_tabella
                int[] id_tabella = new int[model.size()];
                for (int i = 0; i < model.size(); i++) {
                    TheaterModel theater = model.get(i).getSala();
                    DateTimeModel date = model.get(i).getData();
                    id_tabella[i] = ShowManager.get(tmp.getId_film(), theater.getId_sala(),
                            date.getId_data());
                }

                // creo un modello filmTheaterDate e lo aggiungo nella lista filmTheaterDate
                FilmTheaterDate film = new FilmTheaterDate(tmp, model, id_tabella);
                filmTheaterDate.add(film);
            }

            int size = filmTheaterDate.size();
            this.filmTheaterDate = filmTheaterDate.toArray(new FilmTheaterDate[size]);
        } catch (Exception ex) {
            // da gestire
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Aggiungo uno show nel database tramite le property da ottenere
     *
     * @throws java.lang.Exception Eccezione
     */
    public void addShow() throws Exception {
        try {
            /**
             * controllo su validità della data e degli orari di inizio e fine
             * viene fatta dal manager
             */
            FilmModel film = FilmManager.get(this.getId_film());
            DateTimeModel show = ShowManager.getDate(LocalDate.parse(this.getData()),
                    LocalTime.parse(this.getOra_inizio()), LocalTime.parse(this.getOra_fine()));
            TheaterModel theater = new TheaterModel(0, 0, this.getSala());
            ShowManager.add(film, show, theater);

            this.setAlert(Message.INSERTSUCCESS);
        } catch (Exception ex) {
            this.setAlert(Message.INSERTERROR);
            throw ex;
        }
    }

    /**
     * Aggiorno uno show nel database tramite le property da ottenere
     * @throws java.lang.Exception Eccezione
     */
    public void updateShow() throws Exception {
        try {
            FilmTheaterDateModel model = ShowManager.get(this.getId_tabella());

            FilmModel film = FilmManager.get(this.getId_film());
            if (film == null) {
                throw new Exception();
            }

            // recupero tutti i biglietti associati allo show
            List<TicketModel> ticket = TicketManager.get(model);
            
            DateTimeModel show = model.getDate();
            show.setData(LocalDate.parse(this.getData()));
            show.setOra_inizio(LocalTime.parse(this.getOra_inizio()));
            show.setOra_fine(LocalTime.parse(this.getOra_fine()));
            show = ShowManager.getDate(show.getData(), show.getOra_inizio(),
                    show.getOra_fine());

            TheaterModel theater = model.getTheater();
            theater.setNumero_sala(this.getSala());

            model.setTheater(theater);
            model.setDate(show);

            ShowManager.update(model, ticket);
        } catch (Exception ex) {
            this.setAlert(Message.UPDATEERROR);
            throw ex;
        }
    }

    /**
     * Recupero uno show dal database tramite la property id_tabella
     */
    public void getShow() {
        try {
            FilmTheaterDateModel model = ShowManager.get(this.getId_tabella());

            // gestione film
            FilmModel film = model.getFilm();
            this.setId_film(film.getId_film());
            this.setTitolo_film(film.getTitolo());
            this.setDurata_film(film.getDurata().toString());

            // gestione sala
            TheaterModel sala = model.getTheater();
            this.setSala(sala.getNumero_sala());

            // gestione data-orario
            DateTimeModel data = model.getDate();
            this.setData(data.getData().toString());
            this.setOra_inizio(data.getOra_inizio().toString());
            this.setOra_fine(data.getOra_fine().toString());
        } catch (Exception ex) {
            // messaggio di errore
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom per TheaterDate ">
    /**
     * Recupero il numero totale di sale
     *
     * @return La lunghezza dell'array TheaterDate, cioè il numero di sale
     */
    public int TheaterDate_Length() {
        return this.theaterDate.length;
    }

    /**
     * Recupero l'array delle date associate ad un elemento di TheaterDate
     *
     * @param index Indice dell'elemento
     * @return L'array delle date
     */
    public String[] TheaterDate_Date(int index) {
        return this.theaterDate[index].getDate();
    }

    /**
     * Recupero l'array dei film associati ad un elemento di TheaterDate
     *
     * @param index Indice dell'elemento
     * @return L'array dei film
     */
    public String[] TheaterDate_Film(int index) {
        return this.theaterDate[index].getFilm();
    }

    /**
     * Recupero l'array degli orari associati ad un elemento di TheaterDate
     *
     * @param index Indice dell'elemento
     * @return L'array degli orari
     */
    public String[] TheaterDate_Orario(int index) {
        String[] inizio = this.theaterDate[index].getOra_inizio();
        String[] fine = this.theaterDate[index].getOra_fine();
        String[] orario = new String[inizio.length];
        for (int i = 0; i < inizio.length; i++) {
            orario[i] = inizio[i] + "-" + fine[i];
        }
        return orario;
    }

    /**
     * Recupero l'array dei posti disponibili associati ad un elemento di
     * TheaterDate
     *
     * @param index Indice dell'elemento
     * @return L'array dei posti disponibili
     */
    public int[] TheaterDate_Posti(int index) {
        return this.theaterDate[index].getPosti_disp();
    }

    /**
     * Recupera l'ora di inizio associati ad una sala in un certo giorno
     *
     * @param num_sala Il numero della sala
     * @param day Il giorno preso in considerazione
     * @return L'array con tutti gli orari di inizio associati
     */
    public String[] TheaterDate_OraInizio(int num_sala, String day) {
        List<String> oraInizio = new ArrayList<>();
        for (TheaterDate tmp : this.theaterDate) {
            if (tmp.getNum_sala() == num_sala) {
                int num_date = tmp.getDate().length;
                for (int i = 0; i < num_date; i++) {
                    if (tmp.getDate(i).equals(day)) {
                        oraInizio.add(tmp.getOra_inizio(i));
                    }
                }
            }
        }
        String[] model = new String[oraInizio.size()];
        model = oraInizio.toArray(model);

        return model;
    }

    /**
     * Recupera l'ora di fine associati ad una sala in un certo giorno
     *
     * @param num_sala Il numero della sala
     * @param day Il giorno preso in considerazione
     * @return L'array con tutti gli orari di fine associati
     */
    public String[] TheaterDate_OraFine(int num_sala, String day) {
        List<String> oraFine = new ArrayList<>();
        for (TheaterDate tmp : this.theaterDate) {
            if (tmp.getNum_sala() == num_sala) {
                int num_date = tmp.getDate().length;
                for (int i = 0; i < num_date; i++) {
                    if (tmp.getDate(i).equals(day)) {
                        oraFine.add(tmp.getOra_fine(i));
                    }
                }
            }
        }
        String[] model = new String[oraFine.size()];
        model = oraFine.toArray(model);

        return model;
    }

    /**
     * Recupera i film associati ad una sala in un certo giorno
     *
     * @param num_sala Il numero della sala
     * @param day Il giorno preso in considerazione
     * @return L'array con tutti i film associati
     */
    public String[] TheaterDate_Film(int num_sala, String day) {
        List<String> film = new ArrayList<>();
        for (TheaterDate tmp : this.theaterDate) {
            if (tmp.getNum_sala() == num_sala) {
                int num_date = tmp.getDate().length;
                for (int i = 0; i < num_date; i++) {
                    if (tmp.getDate(i).equals(day)) {
                        film.add(tmp.getFilm(i));
                    }
                }
            }
        }
        String[] model = new String[film.size()];
        model = film.toArray(model);

        return model;
    }

    /**
     * Recupera i posti disponibili associati ad una sala in un certo giorno
     *
     * @param num_sala Il numero della sala
     * @param day Il giorno preso in considerazione
     * @return L'array con tutti i posti disponibili
     */
    public int[] TheaterDate_Posti(int num_sala, String day) {
        List<Integer> posti = new ArrayList<>();
        for (TheaterDate tmp : this.theaterDate) {
            if (tmp.getNum_sala() == num_sala) {
                int num_date = tmp.getDate().length;
                for (int i = 0; i < num_date; i++) {
                    if (tmp.getDate(i).equals(day)) {
                        posti.add(tmp.getPosti_disp(i));
                    }
                }
            }
        }
        int[] model = new int[posti.size()];
        for (int i = 0; i < posti.size(); i++) {
            model[i] = posti.get(i);
        }
        return model;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom per FilmTheaterDate ">
    /**
     * Recupero il numero di film in programmazione
     *
     * @return Il numero di film
     */
    public int FilmTheaterDate_Films() {
        return this.filmTheaterDate.length;
    }

    /**
     * Recupero il titolo di un film
     *
     * @param index L'indice nell'array
     * @return Il titolo del film
     */
    public String FilmTheaterDate_Titolo(int index) {
        return this.filmTheaterDate[index].getTitolo();
    }

    /**
     * Recupero id di un film
     *
     * @param index L'indice nell'array
     * @return Id del film
     */
    public int FilmTheaterDate_IdFilm(int index) {
        return this.filmTheaterDate[index].getId_film();
    }

    /**
     * Recupero l'array con gli id della tabella nel database riferiti al film
     *
     * @param index L'indice nell'array
     * @param day Il giorno che considero
     * @return L'array con gli id tabella
     */
    public int[] FilmTheaterDate_IdTabella(int index, String day) {
        List<Integer> id_tabella = new ArrayList<>();
        FilmTheaterDate model = this.filmTheaterDate[index];
        for (int i = 0; i < model.getDate().length; i++) {
            if (model.getDate(i).equals(day)) {
                id_tabella.add(model.getId_tabella(i));
            }
        }
        int[] tabella = new int[id_tabella.size()];
        for (int i = 0; i < tabella.length; i++) {
            tabella[i] = id_tabella.get(i);
        }
        return tabella;
    }

    /**
     * Recupero gli orari di inizio del film
     *
     * @param index L'indice nell'array
     * @param day Il giorno che considero
     * @return L'array con gli orari di inizio
     */
    public String[] FilmTheaterDate_OraInizio(int index, String day) {
        List<String> inizio = new ArrayList<>();
        FilmTheaterDate model = this.filmTheaterDate[index];
        int date = model.getDate().length;
        for (int i = 0; i < date; i++) {
            if (model.getDate(i).equals(day)) {
                inizio.add(model.getOra_inizio(i));
            }
        }
        return inizio.toArray(new String[inizio.size()]);
    }

    /**
     * Recupero gli orari di fine del film
     *
     * @param index L'indice nell'array
     * @param day Il giorno che considero
     * @return L'array con gli orari di fine
     */
    public String[] FilmTheaterDate_OraFine(int index, String day) {
        List<String> fine = new ArrayList<>();
        FilmTheaterDate model = this.filmTheaterDate[index];
        int date = model.getDate().length;
        for (int i = 0; i < date; i++) {
            if (model.getDate(i).equals(day)) {
                fine.add(model.getOra_fine(i));
            }
        }
        return fine.toArray(new String[fine.size()]);
    }

    /**
     * Recupero le sale di programmazione del film
     *
     * @param index L'indice nell'array
     * @param day Il giorno che considero
     * @return L'array con le sale di programmazione
     */
    public Integer[] FilmTheaterDate_Sale(int index, String day) {
        List<Integer> sale = new ArrayList<>();
        FilmTheaterDate model = this.filmTheaterDate[index];
        int date = model.getDate().length;
        for (int i = 0; i < date; i++) {
            if (model.getDate(i).equals(day)) {
                sale.add(model.getNum_sala(i));
            }
        }
        return sale.toArray(new Integer[sale.size()]);
    }

    /**
     * Recupero l'array con i posti disponibili per il film
     *
     * @param index L'indice nell'array
     * @param day Il giorno che considero
     * @return L'array dei posti disponibili
     */
    public Integer[] FilmTheaterDate_Posti(int index, String day) {
        List<Integer> posti = new ArrayList<>();
        FilmTheaterDate model = this.filmTheaterDate[index];
        int date = model.getDate().length;
        for (int i = 0; i < date; i++) {
            if (model.getDate(i).equals(day)) {
                posti.add(model.getPosti_disp(i));
            }
        }
        return posti.toArray(new Integer[posti.size()]);
    }

    /**
     * Controllo che se un film è in programmazione o meno in un certo giorno
     *
     * @param index L'indice nell'array
     * @param day Il giorno che considero
     * @return true se è in programmazione, false se non lo è.
     */
    public Boolean FilmTheaterDate_Show(int index, String day) {
        Boolean show = false;
        FilmTheaterDate model = this.filmTheaterDate[index];
        int date = model.getDate().length;
        for (int i = 0; i < date; i++) {
            if (model.getDate(i).equals(day)) {
                show = true;
                break;
            }
        }
        return show;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
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
     * Get the value of sala
     *
     * @return the value of sala
     */
    public int getSala() {
        return sala;
    }

    /**
     * Set the value of sala
     *
     * @param sala new value of sala
     */
    public void setSala(int sala) {
        this.sala = sala;
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
     * Get the value of ora_inizio
     *
     * @return the value of ora_inizio
     */
    public String getOra_inizio() {
        return ora_inizio;
    }

    /**
     * Set the value of ora_inizio
     *
     * @param ora_inizio new value of ora_inizio
     */
    public void setOra_inizio(String ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    /**
     * Get the value of ora_fine
     *
     * @return the value of ora_fine
     */
    public String getOra_fine() {
        return ora_fine;
    }

    /**
     * Set the value of ora_fine
     *
     * @param ora_fine new value of ora_fine
     */
    public void setOra_fine(String ora_fine) {
        this.ora_fine = ora_fine;
    }

    /**
     * Get the value of titolo_film
     *
     * @return the value of titolo_film
     */
    public String getTitolo_film() {
        return titolo_film;
    }

    /**
     * Set the value of titolo_film
     *
     * @param titolo_film new value of titolo_film
     */
    public void setTitolo_film(String titolo_film) {
        this.titolo_film = titolo_film;
    }

    /**
     * Get the value of durata_film
     *
     * @return the value of durata_film
     */
    public String getDurata_film() {
        return durata_film;
    }

    /**
     * Set the value of durata_film
     *
     * @param durata_film new value of durata_film
     */
    public void setDurata_film(String durata_film) {
        this.durata_film = durata_film;
    }

    /**
     * Get the value of films
     *
     * @return the value of films
     */
    public String[] getFilms() {
        return films;
    }

    /**
     * Set the value of films
     *
     * @param films new value of films
     */
    public void setFilms(String[] films) {
        this.films = films;
    }

    /**
     * Get the value of films at specified index
     *
     * @param index the index of films
     * @return the value of films at specified index
     */
    public String getFilms(int index) {
        return this.films[index];
    }

    /**
     * Set the value of films at specified index.
     *
     * @param index the index of films
     * @param films new value of films at specified index
     */
    public void setFilms(int index, String films) {
        this.films[index] = films;
    }
    // </editor-fold>

}

class TheaterDate {

    private int num_sala;
    private String[] date;
    private String[] ora_inizio;
    private String[] ora_fine;
    private int[] posti_disp;
    private String[] film;

    /**
     * Costruttore di default
     */
    public TheaterDate() {
    }

    /**
     * Costruttore specifico che ad ogni sala associa una lista di date, film e
     * relativi posti disponibili
     *
     * @param sala Modello della sala
     * @param data Lista delle date
     * @param film Lista dei film
     * @param posti_disp Array dei posti disponibili
     * @throws Exception
     */
    public TheaterDate(TheaterModel sala, List<DateTimeModel> data, List<FilmModel> film, int[] posti_disp)
            throws Exception {

        this.setNum_sala(sala.getNumero_sala());
        List<String> date = new ArrayList<>();
        List<String> inizio = new ArrayList<>();
        List<String> fine = new ArrayList<>();
        for (DateTimeModel model : data) {
            date.add(model.getData().format(DateTimeFormatter.ISO_LOCAL_DATE));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            inizio.add(model.getOra_inizio().format(formatter));
            fine.add(model.getOra_fine().format(formatter));
        }

        List<String> films = new ArrayList<>();
        for (FilmModel model : film) {
            films.add(model.getTitolo());
        }
        if (date.size() != films.size() || date.size() != posti_disp.length) {
            throw new Exception();
        }
        int size = data.size();
        this.setDate(date.toArray(new String[size]));
        this.setOra_inizio(inizio.toArray(new String[size]));
        this.setOra_fine(fine.toArray(new String[size]));
        this.setFilm(films.toArray(new String[size]));
        this.setPosti_disp(posti_disp);
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of num_sala
     *
     * @return the value of num_sala
     */
    public int getNum_sala() {
        return num_sala;
    }

    /**
     * Set the value of num_sala
     *
     * @param num_sala new value of num_sala
     */
    public void setNum_sala(int num_sala) {
        this.num_sala = num_sala;
    }

    /**
     * Get the value of show
     *
     * @return the value of show
     */
    public String[] getDate() {
        return date;
    }

    /**
     * Set the value of show
     *
     * @param date new value of show
     */
    public void setDate(String[] date) {
        this.date = date;
    }

    /**
     * Get the value of show at specified index
     *
     * @param index the index of show
     * @return the value of show at specified index
     */
    public String getDate(int index) {
        return this.date[index];
    }

    /**
     * Set the value of show at specified index.
     *
     * @param index the index of show
     * @param date new value of show at specified index
     */
    public void setDate(int index, String date) {
        this.date[index] = date;
    }

    /**
     * Get the value of ora_inizio
     *
     * @return the value of ora_inizio
     */
    public String[] getOra_inizio() {
        return ora_inizio;
    }

    /**
     * Set the value of ora_inizio
     *
     * @param ora_inizio new value of ora_inizio
     */
    public void setOra_inizio(String[] ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    /**
     * Get the value of ora_inizio at specified index
     *
     * @param index the index of ora_inizio
     * @return the value of ora_inizio at specified index
     */
    public String getOra_inizio(int index) {
        return this.ora_inizio[index];
    }

    /**
     * Set the value of ora_inizio at specified index.
     *
     * @param index the index of ora_inizio
     * @param ora_inizio new value of ora_inizio at specified index
     */
    public void setOra_inizio(int index, String ora_inizio) {
        this.ora_inizio[index] = ora_inizio;
    }

    /**
     * Get the value of ora_fine
     *
     * @return the value of ora_fine
     */
    public String[] getOra_fine() {
        return ora_fine;
    }

    /**
     * Set the value of ora_fine
     *
     * @param ora_fine new value of ora_fine
     */
    public void setOra_fine(String[] ora_fine) {
        this.ora_fine = ora_fine;
    }

    /**
     * Get the value of ora_fine at specified index
     *
     * @param index the index of ora_fine
     * @return the value of ora_fine at specified index
     */
    public String getOra_fine(int index) {
        return this.ora_fine[index];
    }

    /**
     * Set the value of ora_fine at specified index.
     *
     * @param index the index of ora_fine
     * @param ora_fine new value of ora_fine at specified index
     */
    public void setOra_fine(int index, String ora_fine) {
        this.ora_fine[index] = ora_fine;
    }

    /**
     * Get the value of posti_disp
     *
     * @return the value of posti_disp
     */
    public int[] getPosti_disp() {
        return posti_disp;
    }

    /**
     * Set the value of posti_disp
     *
     * @param posti_disp new value of posti_disp
     */
    public void setPosti_disp(int[] posti_disp) {
        this.posti_disp = posti_disp;
    }

    /**
     * Get the value of posti_disp at specified index
     *
     * @param index the index of posti_disp
     * @return the value of posti_disp at specified index
     */
    public int getPosti_disp(int index) {
        return this.posti_disp[index];
    }

    /**
     * Set the value of posti_disp at specified index.
     *
     * @param index the index of posti_disp
     * @param posti_disp new value of posti_disp at specified index
     */
    public void setPosti_disp(int index, int posti_disp) {
        this.posti_disp[index] = posti_disp;
    }

    /**
     * Get the value of film
     *
     * @return the value of film
     */
    public String[] getFilm() {
        return film;
    }

    /**
     * Set the value of film
     *
     * @param film new value of film
     */
    public void setFilm(String[] film) {
        this.film = film;
    }

    /**
     * Get the value of film at specified index
     *
     * @param index the index of film
     * @return the value of film at specified index
     */
    public String getFilm(int index) {
        return this.film[index];
    }

    /**
     * Set the value of film at specified index.
     *
     * @param index the index of film
     * @param film new value of film at specified index
     */
    public void setFilm(int index, String film) {
        this.film[index] = film;
    }
    // </editor-fold>
}

class FilmTheaterDate {

    private int id_film;
    private String titolo;
    private int[] id_tabella;
    private Integer[] num_sala;
    private Integer[] posti_disp;
    private String[] date;
    private String[] ora_inizio;
    private String[] ora_fine;

    /**
     * Costruttore di default
     */
    public FilmTheaterDate() {
    }

    /**
     * Costruttore specifico che utilizza il film e l'array sala-data ad esso
     * accoppiato
     *
     * @param film il film di cui voglio le informazioni
     * @param theaterDate l'array sala-data associato al film
     */
    public FilmTheaterDate(FilmModel film, List<TheaterDateModel> theaterDate, int[] id_tabella) {
        this.setId_film(film.getId_film());
        this.setTitolo(film.getTitolo());
        this.setId_tabella(id_tabella);
        List<Integer> num_sala = new ArrayList<>();
        List<Integer> posti_disp = new ArrayList<>();
        List<String> date = new ArrayList<>();
        List<String> ora_inizio = new ArrayList<>();
        List<String> ora_fine = new ArrayList<>();
        // recupero le informazioni riguardo sala e data
        for (TheaterDateModel tmp : theaterDate) {
            TheaterModel sala = tmp.getSala();
            num_sala.add(sala.getNumero_sala());
            posti_disp.add(sala.getPosti_disp());

            DateTimeModel data = tmp.getData();
            date.add(data.getData().toString());
            ora_inizio.add(data.getOra_inizio().toString());
            ora_fine.add(data.getOra_fine().toString());
        }

        int size = theaterDate.size();
        this.setNum_sala(num_sala.toArray(new Integer[size]));
        this.setPosti_disp(posti_disp.toArray(new Integer[size]));
        this.setDate(date.toArray(new String[size]));
        this.setOra_inizio(ora_inizio.toArray(new String[size]));
        this.setOra_fine(ora_fine.toArray(new String[size]));
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
     * Get the value of id_tabella
     *
     * @return the value of id_tabella
     */
    public int[] getId_tabella() {
        return id_tabella;
    }

    /**
     * Set the value of id_tabella
     *
     * @param id_tabella new value of id_tabella
     */
    public void setId_tabella(int[] id_tabella) {
        this.id_tabella = id_tabella;
    }

    /**
     * Get the value of id_tabella at specified index
     *
     * @param index the index of id_tabella
     * @return the value of id_tabella at specified index
     */
    public int getId_tabella(int index) {
        return this.id_tabella[index];
    }

    /**
     * Set the value of id_tabella at specified index.
     *
     * @param index the index of id_tabella
     * @param id_tabella new value of id_tabella at specified index
     */
    public void setId_tabella(int index, int id_tabella) {
        this.id_tabella[index] = id_tabella;
    }

    /**
     * Get the value of num_sala
     *
     * @return the value of num_sala
     */
    public Integer[] getNum_sala() {
        return num_sala;
    }

    /**
     * Set the value of num_sala
     *
     * @param num_sala new value of num_sala
     */
    public void setNum_sala(Integer[] num_sala) {
        this.num_sala = num_sala;
    }

    /**
     * Get the value of num_sala at specified index
     *
     * @param index the index of num_sala
     * @return the value of num_sala at specified index
     */
    public Integer getNum_sala(int index) {
        return this.num_sala[index];
    }

    /**
     * Set the value of num_sala at specified index.
     *
     * @param index the index of num_sala
     * @param num_sala new value of num_sala at specified index
     */
    public void setNum_sala(int index, Integer num_sala) {
        this.num_sala[index] = num_sala;
    }

    /**
     * Get the value of posti_disp
     *
     * @return the value of posti_disp
     */
    public Integer[] getPosti_disp() {
        return posti_disp;
    }

    /**
     * Set the value of posti_disp
     *
     * @param posti_disp new value of posti_disp
     */
    public void setPosti_disp(Integer[] posti_disp) {
        this.posti_disp = posti_disp;
    }

    /**
     * Get the value of posti_disp at specified index
     *
     * @param index the index of posti_disp
     * @return the value of posti_disp at specified index
     */
    public Integer getPosti_disp(int index) {
        return this.posti_disp[index];
    }

    /**
     * Set the value of posti_disp at specified index.
     *
     * @param index the index of posti_disp
     * @param posti_disp new value of posti_disp at specified index
     */
    public void setPosti_disp(int index, Integer posti_disp) {
        this.posti_disp[index] = posti_disp;
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

    /**
     * Get the value of ora_inizio
     *
     * @return the value of ora_inizio
     */
    public String[] getOra_inizio() {
        return ora_inizio;
    }

    /**
     * Set the value of ora_inizio
     *
     * @param ora_inizio new value of ora_inizio
     */
    public void setOra_inizio(String[] ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    /**
     * Get the value of ora_inizio at specified index
     *
     * @param index the index of ora_inizio
     * @return the value of ora_inizio at specified index
     */
    public String getOra_inizio(int index) {
        return this.ora_inizio[index];
    }

    /**
     * Set the value of ora_inizio at specified index.
     *
     * @param index the index of ora_inizio
     * @param ora_inizio new value of ora_inizio at specified index
     */
    public void setOra_inizio(int index, String ora_inizio) {
        this.ora_inizio[index] = ora_inizio;
    }

    /**
     * Get the value of ora_fine
     *
     * @return the value of ora_fine
     */
    public String[] getOra_fine() {
        return ora_fine;
    }

    /**
     * Set the value of ora_fine
     *
     * @param ora_fine new value of ora_fine
     */
    public void setOra_fine(String[] ora_fine) {
        this.ora_fine = ora_fine;
    }

    /**
     * Get the value of ora_fine at specified index
     *
     * @param index the index of ora_fine
     * @return the value of ora_fine at specified index
     */
    public String getOra_fine(int index) {
        return this.ora_fine[index];
    }

    /**
     * Set the value of ora_fine at specified index.
     *
     * @param index the index of ora_fine
     * @param ora_fine new value of ora_fine at specified index
     */
    public void setOra_fine(int index, String ora_fine) {
        this.ora_fine[index] = ora_fine;
    }

    // </editor-fold>
}
