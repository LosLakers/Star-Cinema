package blogics;

import java.sql.*;
import services.database.*;

/**
 *
 * @author Guido Pio
 */
public class UserModel {

    private String username;

    private String password;

    private String name;

    private String surname;

    private String email;

    private int creditcard;

    /* valore per distinguere un semplice utente da un admin */
    private Boolean admin;

    // <editor-fold defaultstate="collapsed" desc=" CONSTRUCTORS ">
    public UserModel() {
    }

    public UserModel(String username, String password, String name, String surname, String email, int creditcard) {
        this.setUsername((username != null) ? username : "");
        this.setPassword((password != null) ? password : "");
        this.setName((name != null) ? name : "");
        this.setSurname((surname != null) ? surname : "");
        this.setEmail((email != null) ? email : "");
        this.setCreditcard(creditcard);
        this.setAdmin(false);
    }

    public UserModel(ResultSet result) {
        try {
            if (result.getInt("active") != 0) {
                this.setUsername(result.getString("username"));
                this.setName(result.getString("name"));
                this.setSurname(result.getString("surname"));
                this.setPassword(result.getString("password"));
                this.setEmail(result.getString("email"));
                this.setCreditcard(result.getInt("credit_card"));
                this.setAdmin(result.getString("role").equals("admin") ? true : false);
            }
        } catch (SQLException ex) {
            /* non ci devono essere campi senza valore */
        }
    }

    // </editor-fold>
    
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
     * Get the value of creditcard
     *
     * @return the value of creditcard
     */
    public int getCreditcard() {
        return creditcard;
    }

    /**
     * Set the value of creditcard
     *
     * @param creditcard new value of creditcard
     */
    public void setCreditcard(int creditcard) {
        this.creditcard = creditcard;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean isAdmin() {
        return this.admin;
    }

    // </editor-fold>
}
