
package bflows;

import blogics.*;
import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Guido Pio
 */
public class FilmManagement implements Serializable {
    
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
            FilmModel film = new FilmModel(this.getTitolo(), this.getDurata(), this.getDescrizione(),
                    this.getTrailer(), this.getLocandina());
            FilmManager.add(film);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void updateFilm() {
        try {
            FilmModel film = new FilmModel(this.getTitolo(), this.getDurata(), this.getDescrizione(),
                    this.getTrailer(), this.getLocandina());
            FilmManager.update(film);
        } catch (Exception ex) {
            
        }
    }
    
    public void deleteFilm() {
        try {
            FilmManager.delete(this.getTitolo());
        } catch (Exception ex) {
            
        }
    }
    
    public void getFilm() {
        try {
            FilmModel film = FilmManager.get(titolo);
            this.setDescrizione(film.getDescrizione());
            this.setTrailer(film.getTrailer());
            this.setDurata(film.getDurata());
            this.setLocandina(film.getLocandina());
        } catch (Exception ex) {
            
        }
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
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
