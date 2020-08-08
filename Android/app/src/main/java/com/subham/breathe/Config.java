package com.subham.breathe;


import java.util.Calendar;
import java.util.HashSet;

import ca.antonious.materialdaypicker.MaterialDayPicker;

class Config {
    public String Id;
    public String Email;
    public String Name;
    public HashSet<MaterialDayPicker.Weekday> WorkDays;
    public Time StartTime;
    public Time EndTime;
    public BreakTime breakTimeInMinutes;

    public Config() {
        this.WorkDays = new HashSet<>();

        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        this.StartTime = new Time(mHour, mMinute);
        this.EndTime = this.StartTime;
        this.breakTimeInMinutes = new BreakTime(30, "min");
    }

    public void setWeekDays(MaterialDayPicker.Weekday day, boolean selected) {
        if (this.WorkDays.contains(day)) {
            if (! selected) {
                this.WorkDays.remove(day);
            }
        }
        else{
            if (selected) {
                this.WorkDays.add(day);
            }
        }
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
