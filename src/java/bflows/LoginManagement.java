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

    public LoginManagement() {
    }

    public void login() {
        try {
            UserModel user = UserManager.get(username, password);

            if (user != null) {
                String[] property = {"username", "admin"};
                cookies = CookieManager.add(property, user.getUsername(), 
                                        user.isAdmin() ? "true" : "false");
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
    
    public String getCookieValue(String name) {
        return CookieManager.getValue(name, cookies);
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
}
