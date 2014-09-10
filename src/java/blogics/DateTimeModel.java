package blogics;

import java.sql.*;
import java.time.*;

/**
 * Modello di Data e Orario nel database
 */
public class DateTimeModel {

    private int id_data;
    private LocalDate data;
    private LocalTime ora_inizio;
    private LocalTime ora_fine;

    // <editor-fold defaultstate="collapsed" desc=" CONSTRUCTORS ">
    public DateTimeModel() {
    }

    /**
     * Costruttore con parametri
     *
     * @param id_data Identificativo modello
     * @param data Data
     * @param ora_inizio Ora di inizio
     * @param ora_fine Ora di fine
     */
    public DateTimeModel(int id_data, LocalDate data, LocalTime ora_inizio, LocalTime ora_fine) {
        this.setId_data(id_data);
        this.setData(data);
        this.setOra_inizio(ora_inizio);
        this.setOra_fine(ora_fine);
    }

    /**
     * Costruttore con parametri
     *
     * @param result Risultato di una query
     * @throws SQLException Eccezione
     */
    public DateTimeModel(ResultSet result) throws SQLException {
        this.setId_data(result.getInt("id_data"));
        this.setData(result.getDate("data").toLocalDate());
        this.setOra_inizio(result.getTime("ora_inizio").toLocalTime());
        this.setOra_fine(result.getTime("ora_fine").toLocalTime());
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_data
     *
     * @return the value of id_data
     */
    public int getId_data() {
        return id_data;
    }

    /**
     * Set the value of id_data
     *
     * @param id_data new value of id_data
     */
    public void setId_data(int id_data) {
        this.id_data = id_data;
    }

    /**
     * Get the value of data
     *
     * @return the value of data
     */
    public LocalDate getData() {
        return data;
    }

    /**
     * Set the value of data
     *
     * @param data new value of data
     */
    public void setData(LocalDate data) {
        this.data = data;
    }

    /**
     * Get the value of ora_inizio
     *
     * @return the value of ora_inizio
     */
    public LocalTime getOra_inizio() {
        return ora_inizio;
    }

    /**
     * Set the value of ora_inizio
     *
     * @param ora_inizio new value of ora_inizio
     */
    public void setOra_inizio(LocalTime ora_inizio) {
        this.ora_inizio = ora_inizio;
    }

    /**
     * Get the value of ora_fine
     *
     * @return the value of ora_fine
     */
    public LocalTime getOra_fine() {
        return ora_fine;
    }

    /**
     * Set the value of ora_fine
     *
     * @param ora_fine new value of ora_fine
     */
    public void setOra_fine(LocalTime ora_fine) {
        this.ora_fine = ora_fine;
    }

    // </editor-fold>
}
