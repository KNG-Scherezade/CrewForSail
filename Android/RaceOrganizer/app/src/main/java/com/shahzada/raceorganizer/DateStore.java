package com.shahzada.raceorganizer;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Scherezade on 5/22/2016.
 */
public class DateStore {
    public int hour;
    public int minute;
    public Date date;



    public static class DateStores {
        Date[] dateTimes;
        int numberOfDates;
        public List<Date> datesL;

        public DateStores(String deadlineString) {
            String[] deadlineArray = deadlineString.split(",_");
            numberOfDates = deadlineArray.length;
            dateTimes = new Date[numberOfDates];
            //fill array
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            DateFormat formatter = new SimpleDateFormat("yy MMMM dd HH:mm");
            try {
                for (int i = 0; i < numberOfDates; i++) {
                    deadlineArray[i] = deadlineArray[i].replaceAll("-", " ").replace("th", "").replace("rd", "")
                            .replace("nd", "").replace("st", "");
                    Date date = formatter.parse(year + " " + deadlineArray[i]);
                    dateTimes[i] = date;
                    System.out.println(date);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datesL = Arrays.asList(dateTimes);
        }

        public boolean checkDates() {
            this.sortDates(datesL);
            Date initial = Calendar.getInstance().getTime();
            Date future = datesL.get(numberOfDates - 1);
            int comparision = initial.compareTo(future);
                if (comparision == 1) {
                    return true;
                }
            return false;
        }

        public void sortDates(List<Date> array) {
            System.out.println(array);
            Collections.sort(array, new Comparator<Date>() {
                public int compare(Date o1, Date o2) {
                    return o1.compareTo(o2);
                }
            });
        }

        public String modifyDates() {
            return null;
        }
    }

    public DateStore(){
        this(0,0,null);
    }

    public DateStore(int hr, int min, Date dt){
        hour = hr;
        minute = min;
        date = dt;
    }
    public boolean equals(Object o){
        DateStore ds = (DateStore) o;
        return (ds.date.toString()).equals(this.date.toString());
    }
    public String toString(){
        int hourT = hour % 12;
        String timeSwitch = "";
        if (hour / 12 == 1){
            timeSwitch = "PM";
        }
        else{
            timeSwitch = "AM";
        }
        return getMonthForInt(date.getMonth()) + " " + date.getDate() + getPrefixForDate(date.getDate())
                + " "+ setZeros(hour, minute);
    }

    String getPrefixForDate(int num){
        if (num > 10 && num < 13) return "th";
        switch(num){
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    //set zeros for clock
    public String setZeros(int hour, int minute){
        String zeroTime = "";
            if (hour < 10){
                zeroTime += "" +  0 + hour + ":";
            }
            else{
                zeroTime += "" + hour + ":";
            }
            if (minute < 10){
                zeroTime += "" +  0 + minute;
            }
            else{
                zeroTime += "" + minute ;
            }
        return zeroTime;
    }



    public int formatHour(int...args){
        hour = args[0];
        return hour % 24;
    }
    public String formatAMPM(int hour, String AMPM){
        if (hour / 12 == 1){
            return "PM";
        }
        else{
            return "AM";
        }
    }
}
