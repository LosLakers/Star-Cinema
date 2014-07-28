
package bflows;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author Guido Pio
 */
public class BaseBean implements Serializable {
    
    private String message = "";

    private String messagetype = "";

    public BaseBean() {}
    
    // <editor-fold defaultstate="collapsed" desc=" GETTER-SETTER ">
    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the value of messagetype
     *
     * @return the value of messagetype
     */
    public String getMessagetype() {
        return messagetype;
    }

    /**
     * Set the value of messagetype
     *
     * @param messagetype new value of messagetype
     */
    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    // </editor-fold>
}
