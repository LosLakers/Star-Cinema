package bflows;

import java.beans.*;
import javax.servlet.http.*;
import java.io.Serializable;

import blogics.*;
import exceptions.*;

/**
 *
 * @author Guido Pio
 */
public class LoginManagement implements Serializable {

    /* username property */
    private String username;

    /* password property */
    private String password;

    /* cookies property */
    private Cookie[] cookies;

    private String name;

    private String surname;

    private String email;

    private String creditcard;

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
        } catch (NotFoundDBException ex) {
            // da gestire
        } catch (ResultSetDBException ex) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // </editor-fold>
    
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

    // </editor-fold>
}
