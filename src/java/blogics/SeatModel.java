package blogics;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Guido Pio
 */
public class SeatModel {

    private int id_posto;
    private String fila;
    private int numero;
    private int id_sala;

    private TheaterModel theater;

    public SeatModel() {

    }

    /**
     * Costruttore di default con dati forniti come parametri
     *
     * @param id_posto Id del posto
     * @param fila Lettera della fila
     * @param numero Numero del posto nella fila
     * @param id_sala Id della sala del posto
     */
    public SeatModel(int id_posto, String fila, int numero, int id_sala) {
        this.setId_posto(id_posto);
        this.setFila(fila);
        this.setNumero(numero);
        this.setId_sala(id_sala);
        
        try {
            this.setTheater(ShowManager.getTheater(id_sala));
        } catch (Exception ex) {
            // non fare niente
        }
    }

    /**
     * Costruttore di default con dati forniti dal risultato di una chiamata al
     * database
     *
     * @param result Il risultato della chiamata al database
     * @throws SQLException
     */
    public SeatModel(ResultSet result) throws SQLException {
        this.setId_posto(result.getInt("id_posto"));
        this.setFila(result.getString("fila"));
        this.setNumero(result.getInt("numero"));
        this.setId_sala(result.getInt("id_sala"));

        try {
            this.setTheater(ShowManager.getTheater(this.getId_sala()));
        } catch (Exception ex) {
            // non fare niente
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_posto
     *
     * @return the value of id_posto
     */
    public int getId_posto() {
        return id_posto;
    }

    /**
     * Set the value of id_posto
     *
     * @param id_posto new value of id_posto
     */
    public void setId_posto(int id_posto) {
        this.id_posto = id_posto;
    }

    /**
     * Get the value of fila
     *
     * @return the value of fila
     */
    public String getFila() {
        return fila;
    }

    /**
     * Set the value of fila
     *
     * @param fila new value of fila
     */
    public void setFila(String fila) {
        this.fila = fila;
    }

    /**
     * Get the value of numero
     *
     * @return the value of numero
     */
    public int getNumero() {
        return numero;
    }

    /**
     * Set the value of numero
     *
     * @param numero new value of numero
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }

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
    // </editor-fold>
}
