package com.subham.breathe;

import java.util.ArrayList;
import java.util.List;

public class BreakTime {
    public int time;
    public String formatter;

    public BreakTime(int time, String formatter) {
        this.time = time;
        this.formatter = formatter;
    }

    public BreakTime(String breakTime) {
        String[] parts = breakTime.split(" ");
        this.time = Integer.parseInt(parts[0]);
        this.formatter = parts[1];
    }


    @Override
    public String toString() {
        return String.format("%s %s", time, formatter);
    }

    public static ArrayList<String> getDefaultBreakTimes(){
        ArrayList<String> categories = new ArrayList<String>();
        categories.add(new BreakTime(30, "min").toString());
        categories.add(new BreakTime(45, "min").toString());
        categories.add(new BreakTime(60, "min").toString());
        categories.add(new BreakTime(90, "min").toString());
        categories.add(new BreakTime(120, "min").toString());
        return categories;
    }
}
