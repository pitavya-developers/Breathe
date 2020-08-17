package com.subham.breathe;

import java.util.ArrayList;
import java.util.List;


class Config {
    public boolean activated;
    public String Id;
    public String Email;
    public String Name;
    public List<Integer> WorkDays;
    public Time StartTime;
    public Time EndTime;
    public BreakTime breakTimeInMinutes;
    public Boolean permissionGiven;
    public Boolean firstTimeActivated;

    public Config() {
        this.WorkDays = new ArrayList<>();
        this.StartTime = new Time(9, 30);
        this.EndTime = new Time(18, 0);
        this.breakTimeInMinutes = new BreakTime(30, "min");
        this.activated = false;
    }


    public void setWeekDays(List<Integer> days) {
        this.WorkDays = days;
    }

    @Override
    public String toString() {
        return "Config{" +
                "Id='" + Id + '\'' +
                ", Email='" + Email + '\'' +
                ", Name='" + Name + '\'' +
                ", WorkDays=" + WorkDays +
                ", StartTime=" + StartTime.toString() +
                ", EndTime=" + EndTime.toString() +
                ", breakTimeInMinutes=" + breakTimeInMinutes.toString() +
                '}';
    }
}
