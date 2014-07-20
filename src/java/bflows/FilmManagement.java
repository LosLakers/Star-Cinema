
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

    private int id_commento;

    private int voto;

    private String giudizio;

    private String user;
    
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

    public void addComment() {
        try {
            CommentModel commento = new CommentModel(this.getVoto(), this.getGiudizio(),
                            this.getUser(), this.getId_film());
            CommentManager.add(commento);
            this.setId_commento(commento.getId_commento());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    /**
     * Get the value of id_commento
     *
     * @return the value of id_commento
     */
    public int getId_commento() {
        return id_commento;
    }

    /**
     * Set the value of id_commento
     *
     * @param id_commento new value of id_commento
     */
    public void setId_commento(int id_commento) {
        this.id_commento = id_commento;
    }

    /**
     * Get the value of voto
     *
     * @return the value of voto
     */
    public int getVoto() {
        return voto;
    }

    /**
     * Set the value of voto
     *
     * @param voto new value of voto
     */
    public void setVoto(int voto) {
        this.voto = voto;
    }

    /**
     * Get the value of giudizio
     *
     * @return the value of giudizio
     */
    public String getGiudizio() {
        return giudizio;
    }

    /**
     * Set the value of giudizio
     *
     * @param giudizio new value of giudizio
     */
    public void setGiudizio(String giudizio) {
        this.giudizio = giudizio;
    }

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(String user) {
        this.user = user;
    }

    // </editor-fold>
    
}
