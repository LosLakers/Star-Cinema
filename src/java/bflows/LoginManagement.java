package bflows;

import java.beans.*;
import javax.servlet.http.*;
import java.io.Serializable;

import blogics.*;
import exceptions.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.mail.MessagingException;

/**
 * JavaBean per la gestione di un utente
 */
public class LoginManagement extends BaseBean implements Serializable {

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
    /**
     * Effettuo il login nel sistema
     *
     * @throws exceptions.NotFoundDBException Eccezione
     * @throws java.sql.SQLException Eccezione
     */
    public void login() throws NotFoundDBException, SQLException {
        try {
            UserModel user = UserManager.get(this.getUsername(), this.getPassword());

            if (user != null) {
                if (user.isAdmin()) {
                    String[] property = {"username", "password", "admin"};
                    Cookie[] cookies = CookieManager.add(property, user.getUsername(), 
                            user.getPassword(), user.isAdmin() ? "true" : "false");
                    for (Cookie tmp : cookies) {
                        // per gli admin i cookie hanno una giornata di vita
                        tmp.setMaxAge(24*60*60);
                    }
                    this.setCookies(cookies);
                } else {
                    String[] property = {"username", "password"};
                    this.setCookies(CookieManager.add(property, user.getUsername(), 
                            user.getPassword()));
                }
                this.setName(user.getName());
                this.setSurname(user.getSurname());
                this.setEmail(user.getEmail());
                this.setCreditcard(Integer.toString(user.getCreditcard()));
                this.setAlert(Message.LOGINSUCCESS);
            }
        } catch (NotFoundDBException ex) {
            this.setAlert(Message.LOGINUNSUCCESS);
            throw ex;
        } catch (SQLException ex) {
            this.setAlert(Message.LOGINERROR);
            throw ex;
        }
    }

    /**
     * Effettuo il logout dal sistema
     */
    public void logout() {
        CookieManager.delete(cookies);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Inserisco un utente nel sistema
     *
     * @throws java.lang.Exception Eccezione
     */
    public void addUser() throws Exception {

        UserModel user = null;
        try {
            user = UserManager.get(this.getUsername());
        } catch (NotFoundDBException | SQLException ex) {
            this.setAlert(Message.INSERTERROR);
            throw ex;
        }
        if (user != null) {
            this.setAlert(Message.USEREXIST);
            throw new Exception();
        }
        try {
            int credit_card = this.getCreditcard() != null ? Integer.parseInt(this.getCreditcard()) : 0;
            user = new UserModel(this.getUsername(), this.getPassword(), this.getName(),
                    this.getSurname(), this.getEmail(), credit_card);
            UserManager.add(user);
        } catch (NotFoundDBException | IOException | NumberFormatException | MessagingException ex) {
            this.setAlert(Message.INSERTERROR);
            throw ex;
        }
    }

    /**
     * Aggiorno i dati di un utente nel sistema
     *
     * @throws exceptions.NotFoundDBException Eccezione
     */
    public void updateUser() throws NumberFormatException, NotFoundDBException {
        try {
            String creditcard = this.getCreditcard() == null ? "0" : this.getCreditcard();
            UserModel newUser = new UserModel(this.getUsername(), this.getPassword(), this.getName(),
                    this.getSurname(), this.getEmail(), Integer.parseInt(creditcard));
            UserManager.update(newUser);
            this.setAlert(Message.UPDATESUCCESS);
        } catch (NumberFormatException | NotFoundDBException ex) {
            this.setAlert(Message.UPDATEERROR);
            throw ex;
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
        } catch (NotFoundDBException | SQLException ex) {
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

    /**
     * Recupero il valore di un cookie dal nome
     *
     * @param name Nome associato al cookie
     * @return Valore del cookie
     */
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
