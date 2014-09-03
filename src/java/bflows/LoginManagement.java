package bflows;

import java.beans.*;
import javax.servlet.http.*;
import java.io.Serializable;

import blogics.*;
import exceptions.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Guido Pio
 */
public class LoginManagement implements Serializable {

    private String username;
    private String password;
    private Cookie[] cookies;
    private String name;
    private String surname;
    private String email;
    private String creditcard;

    private int subscriptionticket;

    public LoginManagement() {
    }

    // <editor-fold defaultstate="collapsed" desc=" LOGIN-LOGOUT ">
    public void login() {
        try {
            UserModel user = UserManager.get(username, password);

            if (user != null) {
                String[] property = {"username", "password", "admin"};
                cookies = CookieManager.add(property, user.getUsername(), user.getPassword(),
                        user.isAdmin() ? "true" : "false");
                this.setName(user.getName());
                this.setSurname(user.getSurname());
                this.setEmail(user.getEmail());
                this.setCreditcard(Integer.toString(user.getCreditcard()));
            }
        } catch (NotFoundDBException | SQLException ex) {
            // da gestire
        }
    }

    public void logout() {
        CookieManager.delete(cookies);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    public void updateUser() {
        try {
            UserModel newUser = new UserModel(this.getUsername(), this.getPassword(), this.getName(),
                    this.getSurname(), this.getEmail(), Integer.parseInt(this.getCreditcard()));
            UserManager.update(newUser);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Recupero i dati dell'utente dal sistema con incluso la presenza o meno di
     * un abbonamento
     */
    public void getUser() {
        try {
            UserModel user = UserManager.get(this.getUsername(), this.getPassword());
            if (user != null) {
                this.setName(user.getName());
                this.setSurname(user.getSurname());
                this.setEmail(user.getEmail());

                /* gestione carta di credito */
                int creditcard = user.getCreditcard();
                this.setCreditcard(Integer.toString(creditcard));
            }
            SubscriptionModel subscription = SubscriptionManager.get(this.getUsername());
            if (subscription != null) {
                this.setSubscriptionticket(subscription.getIngressi_disp());
            } else {
                this.setSubscriptionticket(-1);
            }
        } catch (Exception ex) {
            // da gestire
        }
    }
    // </editor-fold>

    /**
     * Verifica che nel sistema sia presente un utente con i dati passati come
     * parametri
     *
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @return true se l'utente è presente, false in caso contrario
     */
    public Boolean authenticate(String username, String password) {
        Boolean authorized = false;
        try {
            UserModel user = UserManager.get(username, password);
            if (user != null) {
                authorized = true;
            }
        } catch (NotFoundDBException | SQLException ex) {
            // non faccio niente
        } finally {
            return authorized;
        }
    }

    public String getCookieValue(String name) {
        return CookieManager.getValue(name, cookies);
    }

    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
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
     * Get the value of password
     *
     * @return the value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the value of password
     *
     * @param password new value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the value of cookies
     *
     * @return the value of cookies
     */
    public Cookie[] getCookies() {
        return cookies;
    }

    /**
     * Set the value of cookies
     *
     * @param cookies new value of cookies
     */
    public void setCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    /**
     * Get the value of cookies at specified index
     *
     * @param index the index of cookies
     * @return the value of cookies at specified index
     */
    public Cookie getCookie(int index) {
        return this.cookies[index];
    }

    /**
     * Set the value of cookies at specified index.
     *
     * @param index the index of cookies
     * @param cookie new value of cookies at specified index
     */
    public void setCookie(int index, Cookie cookie) {
        this.cookies[index] = cookie;
    }

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the value of surname
     *
     * @return the value of surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the value of surname
     *
     * @param surname new value of surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Get the value of email
     *
     * @return the value of email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the value of email
     *
     * @param email new value of email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get the value of creditcard
     *
     * @return the value of creditcard
     */
    public String getCreditcard() {
        return creditcard;
    }

    /**
     * Set the value of creditcard
     *
     * @param creditcard new value of creditcard
     */
    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    /**
     * Get the value of subscriptionticket
     *
     * @return the value of subscriptionticket
     */
    public int getSubscriptionticket() {
        return subscriptionticket;
    }

    /**
     * Set the value of subscriptionticket
     *
     * @param subscriptionticket new value of subscriptionticket
     */
    public void setSubscriptionticket(int subscriptionticket) {
        this.subscriptionticket = subscriptionticket;
    }

    // </editor-fold>
}
