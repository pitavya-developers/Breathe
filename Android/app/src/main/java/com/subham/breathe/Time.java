package com.subham.breathe;

class Time {
    public int hour;
    public int time;

    public Time(int hour, int time) {
        this.hour = hour;
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("%s:%s%s %s", hour % 12, time > 9 ? "" : "0", time, (hour > 12 ? " PM" : " AM"));
    }
}
