/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.service;

import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author edo
 */
public class DateService {

    public Date convertStringDate(String d) {
        if (d.equals("0000-00-00")) {
            d = "1901-01-01";
        }
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dt = formatter.parseDateTime(d);
        return dt.toDate();
    }

    public DateTime convertStringToDateTime(String d,String pattern){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        DateTime dt = formatter.parseDateTime(d);
        return dt;
    }

    public String convertDateTimeToString(DateTime d,String pattern){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(d);
    }
    public String convertDateRequest(String d) {
        String[] dateSplit = d.split("/");
        return dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];
    }

    public String convertDateFileNameDBF(String d) {
        String[] dateSplit = d.split("/");
        return dateSplit[2].substring(2, 4) + dateSplit[1];
    }

    public String convertDateDBF(String d) {
        String[] dateSplit = d.split("/");
        return dateSplit[1] + dateSplit[0] + (dateSplit[2]).substring(2);
    }

    public String convertDateSales(String d) {
        String Z = d.substring(d.length() - 8);
        String timeZone = Z.substring(0, 3);
        d = d.replace(timeZone, "+0700");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("E MMM dd HH:mm:ss Z yyyy");
        DateTime dt = formatter.parseDateTime(d);
        DateTimeFormatter formatterOut = DateTimeFormat.forPattern("yyyy-MM-dd");
        return formatterOut.print(dt);
    }
}
