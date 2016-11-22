package getaroom.floor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Created by Chris on 11/22/2016.
 */

public class DecisionTree {

    private int occupancy;

    private double prob;

    private Timestamp recordTime= Timestamp.valueOf("2016-11-20 22:00:00");

    private int timeUpdated;

    private int roomNum;
    GregorianCalendar calendar;
    private String day;

    public DecisionTree(String timestamp, String students, int roomNumber) {
        if(!students.equals(""+0))
            occupancy = 1;
        else occupancy = 0;
        roomNum = roomNumber;
        day = "Tues";


    }

//    private Calendar timestampToCal(String timestamp) {
//        String month, day, year, time;
//        Calendar cal;
//
//        String[] tokens = timestamp.split("/");
//        String[] year_time = tokens[tokens.length-1].split(" ");
//        month = tokens[0];
//        day = tokens[]
//
//        return
//    }

    public boolean predict() { //Returns TRUE if prediction is FULL
        if(Math.abs(recordTime.compareTo(new Timestamp(System.currentTimeMillis())))< 1 && occupancy == 0) {
            return FALSE;
        }

        int after = 1;
        if(timeUpdated < 1030) //Set to false if updated before 10:30
            after = 0;
        switch (after) {
            case 0: return beforeTenThirty();
            case 1: return afterTenThirty();
        }
        return TRUE;
    }

    private boolean beforeTenThirty() {
        switch (day) {
            case "Sun": return FALSE;
            case "Mon": return TRUE;
            case "Tues": prob = occupancy*.5-1/(1-Math.log(timeElapsed()));
                         return prob >= .25;
            default: return FALSE;
        }
    }

    private boolean afterTenThirty() {
        switch (day) {
            case "Sun": return FALSE;
            case "Mon": return TRUE;
            case "Tues": prob = occupancy*.5-1/(1-Math.log(timeElapsed()));
                        return prob >= .25;
            default: return FALSE;
        }
    }

    private double timeElapsed() {

        return roomNum;
    }
}
