
package blogics;

import java.sql.*;

/**
 *
 * @author Guido Pio
 */
public class FilmModel {

    private int id_film;
    
    private String titolo;

    private String durata;

    private String descrizione;

    private String trailer;

    private String locandina;

    // <editor-fold defaultstate="collapsed" desc=" CONSTRUCTORS ">
    public FilmModel() {
        this.setTitolo("");
        this.setDurata("00:00:00");
        this.setDescrizione("");
        this.setTrailer("");
        this.setLocandina("");
    }
    
    public FilmModel(int id_film, String titolo, String durata, String descrizione, String trailer, String locandina) {
        this.setId_film(id_film);
        this.setTitolo(titolo);
        this.setDurata(durata);
        this.setDescrizione(descrizione);
        this.setTrailer(trailer);
        this.setLocandina(locandina);
    }
    
    public FilmModel(ResultSet result) {
        try {
            this.setId_film(result.getInt("id_film"));
            this.setTitolo(result.getString("titolo"));
            this.setDurata(result.getString("durata"));
            this.setDescrizione(result.getString("descrizione"));
            this.setTrailer(result.getString("trailer"));
            this.setLocandina(result.getString("locandina"));
        } catch (SQLException ex) {
            /* nessun valore deve essere null */
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
     * @return the value of Titolo
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Set the value of titolo
     *
     * @param titolo new value of Titolo
     */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
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