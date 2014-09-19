package bflows;

import java.beans.*;
import java.io.Serializable;
import blogics.*;
import exceptions.NotFoundDBException;
import global.Constants;
import java.sql.SQLException;

/**
 * JavaBean per la gestione di un abbonamento
 */
public class SubscriptionManagement extends BaseBean implements Serializable {

    private int id_abbonamento;
    private int num_ticket;

    public SubscriptionManagement() {
    }

    /**
     * Associo a un utente un nuovo abbonamento
     *
     * @param username Username dell'utente a cui aggiungo l'abbonamento
     */
    public void addSubscription(String username) {
        try {
            SubscriptionModel subscription = SubscriptionManager.get(username);
            // non è presente
            if (subscription == null) {
                int id = SubscriptionManager.add(username, Constants.MAX_TICKETS_SUBSCRIPTION);
                this.setId_abbonamento(id);
                this.setNum_ticket(Constants.MAX_TICKETS_SUBSCRIPTION);
            }
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.SUBSCRIPTIONERROR);
        }
    }

    /**
     * Aggiorno un abbonamento associato ad un utente
     *
     * @param username Identificativo dell'utente
     */
    public void updateSubscription(String username) {
        try {
            SubscriptionModel subscription = SubscriptionManager.get(username);
            // controllo che sia presente e abbia gli ingressi = 0
            if (subscription != null && subscription.getIngressi_disp() == 0) {
                SubscriptionManager.update(username, Constants.MAX_TICKETS_SUBSCRIPTION);
                this.setNum_ticket(Constants.MAX_TICKETS_SUBSCRIPTION);
                this.setId_abbonamento(subscription.getId_abbonamento());
            }
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.SUBSCRIPTIONERROR);
        }
    }

    /**
     * Recupero o meno un abbonamento associato ad un utente
     *
     * @param username Identificativo dell'utente
     */
    public void getSubscription(String username) {
        try {
            SubscriptionModel subscription = SubscriptionManager.get(username);
            if (subscription != null) {
                this.setId_abbonamento(subscription.getId_abbonamento());
                this.setNum_ticket(subscription.getIngressi_disp());
            } else {
                this.setId_abbonamento(0);
                this.setNum_ticket(-1);
            }
        } catch (NotFoundDBException | SQLException ex) {
            // da gestire
        }
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
     * Get the value of num_ticket
     *
     * @return the value of num_ticket
     */
    public int getNum_ticket() {
        return num_ticket;
    }

    /**
     * Set the value of num_ticket
     *
     * @param num_ticket new value of num_ticket
     */
    public void setNum_ticket(int num_ticket) {
        this.num_ticket = num_ticket;
    }

    // </editor-fold>
}
