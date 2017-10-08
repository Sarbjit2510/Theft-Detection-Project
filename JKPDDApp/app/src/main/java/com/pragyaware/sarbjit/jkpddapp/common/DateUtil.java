package com.pragyaware.sarbjit.jkpddapp.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by C9 on 10/08/16.
 */
public class DateUtil {

    public static String getToday() {
        try {
            String pattern = "dd/MM/yyyy";
            Date d = new Date();
            SimpleDateFormat dtf = new SimpleDateFormat(pattern);
            return dtf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
