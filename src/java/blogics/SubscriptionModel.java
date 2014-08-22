
package blogics;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Guido Pio
 */
public class SubscriptionModel {
    
    private int id_abbonamento;
    private int ingressi_disp;
    private String username;

    /**
     * Costruttore basato sul risultato di una query
     * 
     * @param result        Risultato query
     * @throws SQLException 
     */
    public SubscriptionModel(ResultSet result) throws SQLException {
        this.setId_abbonamento(result.getInt("id_abbonamento"));
        this.setIngressi_disp(result.getInt("ingressi_disp"));
        this.setUsername(result.getString("username"));
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_abbonamento
     *
     * @return the value of id_abbonamento
     */
    public int getId_abbonamento() {
        return id_abbonamento;
    }

    /**
     * Set the value of id_abbonamento
     *
     * @param id_abbonamento new value of id_abbonamento
     */
    public void setId_abbonamento(int id_abbonamento) {
        this.id_abbonamento = id_abbonamento;
    }

    /**
     * Get the value of ingressi_disp
     *
     * @return the value of ingressi_disp
     */
    public int getIngressi_disp() {
        return ingressi_disp;
    }

    /**
     * Set the value of ingressi_disp
     *
     * @param ingressi_disp new value of ingressi_disp
     */
    public void setIngressi_disp(int ingressi_disp) {
        this.ingressi_disp = ingressi_disp;
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

    // </editor-fold>

}
