
package blogics;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Guido Pio
 */
public class CommentModel {

    private int id_commento;

    private int voto;

    private String giudizio;

    private String username;

    private int id_film;

    public CommentModel() {
        this.setId_commento(0);
    }
    
    public CommentModel(int id_commento, int voto, String giudizio, String username, int id_film) {
        this.setId_commento(id_commento);
        this.setVoto(voto);
        this.setGiudizio(giudizio);
        this.setUsername(username);
        this.setId_film(id_film);
    }
    
    public CommentModel(ResultSet result) {
        try {
            this.setId_commento(result.getInt("id_commento"));
            this.setVoto(result.getInt("voto"));
            this.setGiudizio(result.getString("giudizio"));
            this.setUsername(result.getString("username"));
            this.setId_film(result.getInt("id_film"));
        } catch (SQLException ex) {
            /* nessun valore deve essere null */
        }
    }
    
    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
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

// </editor-fold>   
}
