package bflows;

import java.beans.*;
import java.io.Serializable;
import blogics.*;

/**
 *
 * @author Guido Pio
 */
public class RegistrationManagement implements Serializable {

    private String username;
    private String password;
    private String name;
    private String surname;
    private String email;
    private String creditcard;

    public RegistrationManagement() {
    }

    /**
     * Registro un utente nel sistema
     */
    public void registration() {
        try {
            int credit_card = this.getCreditcard() != null ? Integer.parseInt(this.getCreditcard()) : 0;
            UserModel user = new UserModel(this.getUsername(), this.getPassword(), this.getName(),
                    this.getSurname(), this.getEmail(), credit_card);
            UserManager.add(user);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    // </editor-fold>
}
