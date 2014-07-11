
package blogics;

import java.text.*;
import java.util.*;
import services.database.*;
import services.errorservice.*;

/**
 *
 * @author Guido Pio
 */
public class NowShowingManager {
    
    /**
     * funzione che permette di creare un periodo di giorni consecutivi
     * @param numDay il numero di giorni che si vogliono
     * @return array del periodo
     */
    public static String[] getPeriod(int numDay) {
        String period[] = new String[numDay];
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        for (int day = 0; day < numDay; day++) {
            Date data = Calendar.getInstance().getTime();
            Calendar c = Calendar.getInstance();
            c.setTime(data);
            c.add(Calendar.DATE, day);
            data = c.getTime();
            period[day] = sdf.format(data);
        }
        
        return period;
    }
    
    /*public static NowShowingModel getNowShowing(String[] date) {
        
    }*/
    
}
