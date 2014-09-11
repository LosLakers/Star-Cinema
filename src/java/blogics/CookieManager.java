package blogics;

import javax.servlet.http.*;

/**
 * Manager per la gestione dei Cookie
 */
public class CookieManager {

    // <editor-fold defaultstate="collapsed" desc=" CRUD ">
    /**
     * Inserimento Cookie in base ad un array di nomi e una lista di valori
     *
     * @param names Array di nomi per i cookie
     * @param values Lista di valori per singolo cookie
     * @return L'array dei cookies creati
     */
    public static Cookie[] add(String[] names, String... values) {
        Cookie[] cookies = new Cookie[values.length];
        int i = 0;
        for (String temp : values) {
            cookies[i] = new Cookie(names[i], temp);
            i++;
        }
        return cookies;
    }

    /**
     * Elimina uno o più cookie
     *
     * @param cookies Lista dei cookies
     */
    public static void delete(Cookie... cookies) {
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
        }
    }

    /**
     * Recupera il valore di un cookie in base al nome
     *
     * @param name Nome del cookie
     * @param cookies Array dei cookie
     * @return Il valore del cookie se è presente, null se non lo è
     */
    public static String getValue(String name, Cookie[] cookies) {
        String value = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                value = cookie.getValue();
            }
        }
        return value;
    }
    // </editor-fold>
}
