package com.subham.breathe;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import ca.antonious.materialdaypicker.MaterialDayPicker;

import static android.content.Context.MODE_PRIVATE;

public class ConfigPersistanceStorage {

    Config config;
    private String sharedPrefFile = "com.subham.breathe.storage";
    private SharedPreferences mPreferences;
    SharedPreferences.Editor preferencesEditor;

    private static String activated = "activated";
    private static String weekDays = "weekDays";
    private static String startTime = "startTime";
    private static String endTime = "endTime";
    private static String breakTime = "breakTime";
    private static String firstTimeActivated = "firstTimeActivated";
    private static String name = "name";
    private static String email = "email";
    private static String token = "token";


    public ConfigPersistanceStorage(Context ctx) {
        this.mPreferences = ctx.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        this.preferencesEditor = mPreferences.edit();
    }

    private void delete() {
//        this.preferencesEditor.clear();
    }

    public void update(Config config) {
        this.delete();
        this.save(config);
    }

    public boolean getActivated() {
        return this.mPreferences.getBoolean(activated, false);
    }

    public void setGName(String _name) {
        preferencesEditor.putString(name, _name);
        preferencesEditor.apply();
    }

    public void setGEmail(String _email) {
        preferencesEditor.putString(email, _email);
        preferencesEditor.apply();
    }

    public String getGName() {
        return this.mPreferences.getString(name, "");
    }

    public String getGEmail() {
        return this.mPreferences.getString(email, "");
    }

    public void setGId(String _token) {
        preferencesEditor.putString(token, _token);
        preferencesEditor.apply();
    }

    public String getGId() {
        return this.mPreferences.getString(token, "");
    }



    public boolean getFirstTimeActivated() {
        return this.mPreferences.getBoolean(firstTimeActivated, false);
    }

    public ArrayList<MaterialDayPicker.Weekday> getWeekDays() {
        ArrayList<MaterialDayPicker.Weekday> result = new ArrayList<>(7);

        String weekDaysString = this.mPreferences.getString(weekDays, "");
        if (weekDaysString.length() > 0) {

            String[] weekDaysArray = weekDaysString.split(",");
            for (String weekday :
                    weekDaysArray) {
                result.add(MaterialDayPicker.Weekday.valueOf(weekday));
            }
            return result;
        }
        else{
            return null;
        }
    }

    public Time getStartTime() {
        return new Time(this.mPreferences.getString(startTime, ""));
    }


    public Time getEndTime() {
        return new Time(this.mPreferences.getString(endTime, ""));
    }

    public BreakTime getBreakTime() {
        return new BreakTime(this.mPreferences.getString(breakTime, ""));
    }


    public void save(Config config) {

        this.preferencesEditor.putBoolean(activated, config.activated);

        if (config.WorkDays.size() > 0) {
            String days = config.WorkDays.get(0).toString();
            for (int i = 1; i < config.WorkDays.size(); i++) {
                days = days + "," + config.WorkDays.get(i).toString();
            }
            preferencesEditor.putString(weekDays, days);
        }
        else{
            preferencesEditor.putString(weekDays, "");
        }

        preferencesEditor.putString(startTime, config.StartTime.Save());
        preferencesEditor.putString(endTime, config.EndTime.Save());
        preferencesEditor.putString(breakTime, config.breakTimeInMinutes.toString());
        preferencesEditor.putBoolean(firstTimeActivated, config.firstTimeActivated == null ? false : config.firstTimeActivated);

        preferencesEditor.apply();

    }



}
