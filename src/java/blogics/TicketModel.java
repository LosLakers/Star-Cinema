package blogics;

import exceptions.NotFoundDBException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Modello di un ingresso nel database
 */
public class TicketModel {

    private int id_ingresso;
    private int id_data;
    private int id_posto;
    private String username;
    private int id_abbonamento;
    private int id_film;

    private DateTimeModel data;
    private SeatModel seat;
    private SubscriptionModel subscription;
    private FilmModel film;

    /**
     * Costruttore basato sul risultato di una query
     *
     * @param result Risultato di una query
     * @throws SQLException Eccezione
     */
    public TicketModel(ResultSet result) throws SQLException {
        this.setId_ingresso(result.getInt("id_ingresso"));
        this.setId_data(result.getInt("id_data"));
        this.setId_posto(result.getInt("id_posto"));
        this.setUsername(result.getString("username"));
        this.setId_abbonamento(result.getInt("id_abbonamento"));
        this.setId_film(result.getInt("id_film"));

        try {
            this.setData(ShowManager.getData(this.getId_data()));
            this.setSeat(TicketManager.getSeat(this.getId_posto()));
            this.setSubscription(SubscriptionManager.get(this.getId_abbonamento()));
            this.setFilm(FilmManager.get(this.getId_film()));
        } catch (NotFoundDBException | SQLException ex) {
            // non fare niente
        }
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of id_ingresso
     *
     * @return the value of id_ingresso
     */
    public int getId_ingresso() {
        return id_ingresso;
    }

    /**
     * Set the value of id_ingresso
     *
     * @param id_ingresso new value of id_ingresso
     */
    public void setId_ingresso(int id_ingresso) {
        this.id_ingresso = id_ingresso;
    }

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

    /**
     * Get the value of seat
     *
     * @return the value of seat
     */
    public SeatModel getSeat() {
        return seat;
    }

    /**
     * Set the value of seat
     *
     * @param seat new value of seat
     */
    public void setSeat(SeatModel seat) {
        this.seat = seat;
    }

    /**
     * Get the value of subscription
     *
     * @return the value of subscription
     */
    public SubscriptionModel getSubscription() {
        return subscription;
    }

    /**
     * Set the value of subscription
     *
     * @param subscription new value of subscription
     */
    public void setSubscription(SubscriptionModel subscription) {
        this.subscription = subscription;
    }

    /**
     * Get the value of film
     *
     * @return the value of film
     */
    public FilmModel getFilm() {
        return film;
    }

    /**
     * Set the value of film
     *
     * @param film new value of film
     */
    public void setFilm(FilmModel film) {
        this.film = film;
    }

    // </editor-fold>
}
