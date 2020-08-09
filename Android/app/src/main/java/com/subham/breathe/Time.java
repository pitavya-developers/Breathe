package com.subham.breathe;

import java.util.Comparator;

class Time implements Comparator<Time> {
    public int hour;
    public int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public int compare(Time time, Time t1) {
        if(time.hour == t1.hour && time.minute == t1.minute){
            return 0;
        }

        if((time.hour == t1.hour && time.minute > t1.minute) || (time.hour > t1.hour)){
            return 1;
        }
        return -1;
    }

    public String Save() {
        return String.format("%s:%s", this.hour, this.minute);
    }

    public Time(String savedString) {
        String[] components = savedString.split(":");
        this.hour = Integer.parseInt(components[0]);
        this.minute = Integer.parseInt(components[1]);
    }




    @Override
    public String toString() {
        return String.format("%s:%s%s %s", hour % 12, minute > 9 ? "" : "0", minute, (hour > 12 ? " PM" : " AM"));
    }
}
