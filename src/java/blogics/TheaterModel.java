package blogics;

import java.sql.*;

/**
 *
 * @author Guido Pio
 */
public class TheaterModel {

    private int id_sala;

    private int posti_disp;

    private int numero_sala;

    // <editor-fold defaultstate="collapsed" desc=" CONSTRUCTORS ">
    public TheaterModel() {
        this.setId_sala(0);
        this.setPosti_disp(0);
        this.setNumero_sala(0);
    }

    public TheaterModel(int id_sala, int posti_disp, int numero_sala) {
        this.setId_sala(id_sala);
        this.setPosti_disp(posti_disp);
        this.setNumero_sala(numero_sala);
    }

    public TheaterModel(ResultSet result) throws SQLException {
        this.setId_sala(result.getInt("id_sala"));
        this.setPosti_disp(result.getInt("posti_disp"));
        this.setNumero_sala(result.getInt("numero_sala"));
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_sala
     *
     * @return the value of id_sala
     */
    public int getId_sala() {
        return id_sala;
    }

    /**
     * Set the value of id_sala
     *
     * @param id_sala new value of id_sala
     */
    public void setId_sala(int id_sala) {
        this.id_sala = id_sala;
    }

    /**
     * Get the value of posti_disp
     *
     * @return the value of posti_disp
     */
    public int getPosti_disp() {
        return posti_disp;
    }

    /**
     * Set the value of posti_disp
     *
     * @param posti_disp new value of posti_disp
     */
    public void setPosti_disp(int posti_disp) {
        this.posti_disp = posti_disp;
    }

    /**
     * Get the value of numero_sala
     *
     * @return the value of numero_sala
     */
    public int getNumero_sala() {
        return numero_sala;
    }

    /**
     * Set the value of numero_sala
     *
     * @param numero_sala new value of numero_sala
     */
    public void setNumero_sala(int numero_sala) {
        this.numero_sala = numero_sala;
    }

    // </editor-fold>
}
