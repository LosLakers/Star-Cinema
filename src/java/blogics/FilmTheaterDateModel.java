package blogics;

import java.sql.*;

/**
 * Modello per associare ad un film una data e una sala
 */
public class FilmTheaterDateModel {

    private int id_tabella;
    private FilmModel film;
    private TheaterModel theater;
    private DateTimeModel date;

    public FilmTheaterDateModel() {
    }

    /**
     * Costruttore pubblico basato su risultato di una query
     *
     * @param result Risultato di una query
     * @throws SQLException Eccezione
     */
    public FilmTheaterDateModel(ResultSet result)
            throws SQLException {
        this.setId_tabella(result.getInt("id_tabella"));
        this.setFilm(new FilmModel(result));
        this.setTheater(new TheaterModel(result));
        this.setDate(new DateTimeModel(result));
    }

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
     * Get the value of film
     *
     * @return the value of film
     */
    public FilmModel getFilm() {
        return film;
    }

    /**
     * Set the value of film
     *
     * @param film new value of film
     */
    public void setFilm(FilmModel film) {
        this.film = film;
    }

    /**
     * Get the value of theater
     *
     * @return the value of theater
     */
    public TheaterModel getTheater() {
        return theater;
    }

    /**
     * Set the value of theater
     *
     * @param theater new value of theater
     */
    public void setTheater(TheaterModel theater) {
        this.theater = theater;
    }

    /**
     * Get the value of date
     *
     * @return the value of date
     */
    public DateTimeModel getDate() {
        return date;
    }

    /**
     * Set the value of date
     *
     * @param date new value of date
     */
    public void setDate(DateTimeModel date) {
        this.date = date;
    }

    // </editor-fold>
}
