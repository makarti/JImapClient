package code.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import code.dto.Letter;

public class DateComparator implements Comparator<Integer> {

    Map<Integer, Letter> base;
    public DateComparator(Map<Integer, Letter> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(Integer a, Integer b) {
    	DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        
        Date d1 = null; Date d2 = null;
        
        try {
            d1 = df.parse(base.get(a).received);
        } catch (ParseException e) 
        { 
           //System.out.println("[WARNING] v1 " + v1);
            d1 = new Date("01.01.1900");
        }
        
        try {               
            d2 = df.parse(base.get(b).received);
        } catch (ParseException e) 
        { 
            d2 = new Date("01.01.1900");
        }

        if (d1.equals(d2))
            return 0;
        else
        	return d1.before(d2)?-1:1;
    }
}
