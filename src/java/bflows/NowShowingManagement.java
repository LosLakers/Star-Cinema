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
 *
 * @author Guido Pio
 */
public class NowShowingManagement implements Serializable {

    // proprietà da ottenere
    private int id_film;

    private int sala;

    private String data;

    private String ora_inizio;

    private String ora_fine;

    // proprietà da settare
    private TheaterDate[] theaterDate;

    private String titolo_film;

    private String durata_film;

    private String[] week;

    public NowShowingManagement() {
    }

    public void populateTheater() {
        try {
            /**
             * Creo la settimana di programmazione che voglio vedere
             */
            String[] week = new String[7];
            LocalDate day = LocalDate.now();
            for (int i = 0; i < week.length; i++) { // funziona se sostituisco con il ciclo foreach??
                day = day.plusDays(1);
                week[i] = day.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            this.setWeek(week);
            LocalDate firstDayOfTheWeek = LocalDate.parse(week[0]);
            LocalDate lastDayOfTheWeek = day;
            
            /**
             * Costruisco un numero di array di TheaterDateModel pari al numero 
             * delle sale del cinema. Per ogni array ricavo data e orario ordinando
             * gli elementi prima per data e poi per orario.
             */
            TheaterDate[] model = new TheaterDate[Constants.NUMERO_SALE];
            for (int i = 0; i < Constants.NUMERO_SALE; i++) {
                TheaterModel sala = new TheaterModel();
                sala.setNumero_sala(i+1);
                DateTimeModel[] datetime = TheaterDateManager.getDate(sala.getNumero_sala(),
                        firstDayOfTheWeek, lastDayOfTheWeek);
                model[i] = new TheaterDate(sala, datetime);
            }
            // ricrea la proprietà con i get e set private in modo che non possano essere usati fuori
            this.theaterDate = model;

            /**
             * Recupero film che voglio inserire nella programmazione dal id_film.
             * Le uniche proprietà che per ora voglio visualizzare sono Titolo e
             * Durata.
             */
            FilmModel film = FilmManager.get(this.getId_film());
            this.setTitolo_film(film.getTitolo());
            this.setDurata_film(film.getDurata().toString());
        } catch (NotFoundDBException | SQLException ex) {
            // gestione messaggi di errore
        }
    }
    
    public void addShow() {
        try {
            /**
             * controllo su validità della data e degli orari di inizio e fine
             * viene fatta dal manager
             */
            FilmModel film = FilmManager.get(this.getId_film());
            DateTimeModel show = new DateTimeModel(0, LocalDate.parse(this.getData()),
                        LocalTime.parse(this.getOra_inizio()), LocalTime.parse(this.getOra_fine()));
            TheaterModel theater = new TheaterModel(0,0,this.getSala());
            TheaterDateManager.add(film, show, theater);
            
            // messaggio di avvenuto inserimento
        } catch (Exception ex) {
            // messaggio di errore
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" Metodi Custom per TheaterDate ">
    public int numberOfTheater() {
        return this.theaterDate.length;
    }
    
    public String[] oraInizioTheater(int num_sala, String day) {
        List<String> oraInizio = new ArrayList<>();
        for (TheaterDate tmp : this.theaterDate) {
            if (tmp.getNum_sala() == num_sala) {
                int num_date = tmp.getDate().length;
                for (int i = 0; i < num_date; i++) {
                    if(tmp.getDate(i).equals(day)) {
                        oraInizio.add(tmp.getOra_inizio(i));
                    }
                }
            }
        }
        String[] model = new String[oraInizio.size()];
        model = oraInizio.toArray(model);
        
        return model;
    }
    
    public String[] oraFineTheater(int num_sala, String day) {
        List<String> oraFine = new ArrayList<>();
        for (TheaterDate tmp : this.theaterDate) {
            if (tmp.getNum_sala() == num_sala) {
                int num_date = tmp.getDate().length;
                for (int i = 0; i < num_date; i++) {
                    if(tmp.getDate(i).equals(day)) {
                        oraFine.add(tmp.getOra_fine(i));
                    }
                }
            }
        }
        String[] model = new String[oraFine.size()];
        model = oraFine.toArray(model);
        
        return model;
    }

    // </editor-fold>
    
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

    // </editor-fold>
}

class TheaterDate {

    private int num_sala;

    private String[] date;

    private String[] ora_inizio;

    private String[] ora_fine;

    public TheaterDate() {}
    
    public TheaterDate(TheaterModel sala, DateTimeModel[] data) {
        this.setNum_sala(sala.getNumero_sala());
        List<String> date = new ArrayList<>();
        List<String> inizio = new ArrayList<>();
        List<String> fine = new ArrayList<>();
        for (DateTimeModel model : data) {
            date.add(model.getData().toString());
            inizio.add(model.getOra_inizio().toString());
            fine.add(model.getOra_fine().toString());
        }
        
        int size = date.size();
        this.setDate(date.toArray(new String[size]));
        this.setOra_inizio(inizio.toArray(new String[size]));
        this.setOra_fine(fine.toArray(new String[size]));
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

    // </editor-fold>
}