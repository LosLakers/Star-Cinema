package blogics;

import java.sql.*;
import java.time.*;

/**
 * Modello per associare ad una data una certa sala
 */
public class TheaterDateModel {

    private TheaterModel sala;

    private DateTimeModel data;

    // <editor-fold defaultstate="collapsed" desc=" CONSTRUCTORS ">
    public TheaterDateModel() {
    }

    /**
     * Costruttore con parametri
     *
     * @param result Risultato di una query
     * @throws SQLException Eccezione
     */
    public TheaterDateModel(ResultSet result) throws SQLException {
        this.setSala(new TheaterModel(result));
        this.setData(new DateTimeModel(result));
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of sala
     *
     * @return the value of sala
     */
    public TheaterModel getSala() {
        return sala;
    }

    /**
     * Set the value of sala
     *
     * @param sala new value of sala
     */
    public void setSala(TheaterModel sala) {
        this.sala = sala;
    }

    /**
     * Get the value of data
     *
     * @return the value of data
     */
    public DateTimeModel getData() {
        return data;
    }

    /**
     * Set the value of data
     *
     * @param data new value of data
     */
    public void setData(DateTimeModel data) {
        this.data = data;
    }

    // </editor-fold>
}
