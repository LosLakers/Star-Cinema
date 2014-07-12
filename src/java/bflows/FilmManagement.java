
package bflows;

import blogics.*;
import java.beans.*;
import java.io.Serializable;
import java.sql.*;

/**
 *
 * @author Guido Pio
 */
public class FilmManagement implements Serializable {

    private int id_film;
    
    private String titolo;
    
    private String descrizione;
    
    private String trailer;
    
    private String durata;
    
    private String locandina;
    
    public FilmManagement() {
    }

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    public void addFilm() {
        try {
            FilmModel film = new FilmModel(0, this.getTitolo(), this.getDurata(), 
                    this.getDescrizione(), this.getTrailer(), this.getLocandina());
            FilmManager.add(film);
            this.setId_film(film.getId_film());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateFilm() {
        try {
            FilmModel film = new FilmModel(this.getId_film(), this.getTitolo(), this.getDurata(), 
                    this.getDescrizione(), this.getTrailer(), this.getLocandina());
            FilmManager.update(film);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void deleteFilm() {
        try {
            FilmManager.delete(this.getId_film());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void getFilm() {
        try {
            FilmModel film = FilmManager.get(this.getId_film());
            this.setTitolo(film.getTitolo());
            this.setDescrizione(film.getDescrizione());
            this.setTrailer(film.getTrailer());
            this.setDurata(film.getDurata());
            this.setLocandina(film.getLocandina());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
     * Get the value of descrizione
     *
     * @return the value of descrizione
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Set the value of descrizione
     *
     * @param descrizione new value of descrizione
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Get the value of trailer
     *
     * @return the value of trailer
     */
    public String getTrailer() {
        return trailer;
    }

    /**
     * Set the value of trailer
     *
     * @param trailer new value of trailer
     */
    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    /**
     * Get the value of durata
     *
     * @return the value of durata
     */
    public String getDurata() {
        return durata;
    }

    /**
     * Set the value of durata
     *
     * @param durata new value of durata
     */
    public void setDurata(String durata) {
        if (durata.length() == 5)
            durata = durata + ":00";
        this.durata = durata;
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

    // </editor-fold>
    
}
