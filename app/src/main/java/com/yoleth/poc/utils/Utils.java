package com.yoleth.poc.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by mael on 04/07/16.
 */
public class Utils {

    public static Date addPeriod(Date date, int count, int period) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(period, count);
        return cal.getTime();
    }

    public static String ucfirst(String chaine) {
        return chaine.substring(0, 1).toUpperCase() + chaine.substring(1).toLowerCase();
    }

}
