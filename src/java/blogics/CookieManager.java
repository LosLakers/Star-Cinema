package blogics;

import javax.servlet.http.*;

/**
 *
 * @author Guido Pio
 */
public class CookieManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    public static Cookie[] add(String[] property, String... properties) {
        Cookie[] cookies = new Cookie[properties.length];
        int i = 0;
        
        for (String temp : properties) {
            cookies[i] = new Cookie(property[i], temp);
            i++;
        }
        
        return cookies;
    }
    
    public static void delete(Cookie... cookies) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
        }
    }
    
    public static String getValue(String name, Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        
        return "";
    }

    // </editor-fold>
}
