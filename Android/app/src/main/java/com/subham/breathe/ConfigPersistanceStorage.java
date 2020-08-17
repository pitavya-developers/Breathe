package com.subham.breathe;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

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
        this.preferencesEditor.clear();
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

    public ArrayList<Integer> getWeekDays() {
        ArrayList<Integer> result = new ArrayList<>(7);

        String weekDaysString = this.mPreferences.getString(weekDays, "");
        if (weekDaysString.length() > 0) {

            String[] weekDaysArray = weekDaysString.split(",");
            for (String weekday :
                    weekDaysArray) {
                result.add(Integer.parseInt(weekday));
            }
            return result;
        }
        else {
            result.add(0, 1);
            result.add(1, 2);
            result.add(2, 3);
            result.add(3, 4);
            result.add(4, 5);
            return result;
        }
    }

    public void setWeekDays(Config config) {
        if (config.WorkDays.size() > 0) {
            String days = config.WorkDays.get(0).toString();
            for (int i = 1; i < config.WorkDays.size(); i++) {
                days = days + "," + config.WorkDays.get(i).toString();
            }
            preferencesEditor.putString(weekDays, days);
        } else {
            preferencesEditor.putString(weekDays, "");
        }
        preferencesEditor.apply();

    }

    public Time getStartTime() {
        return new Time(this.mPreferences.getString(startTime, "9:30"));
    }

    public void setStartTime(Config config) {
        preferencesEditor.putString(startTime, config.StartTime.Save());
        preferencesEditor.apply();

    }

    public Time getEndTime() {
        return new Time(this.mPreferences.getString(endTime, "18:00"));
    }

    public void setEndTime(Config config) {
        preferencesEditor.putString(endTime, config.EndTime.Save());
        preferencesEditor.apply();

    }

    public BreakTime getBreakTime() {
        return new BreakTime(this.mPreferences.getString(breakTime, "30 min"));
    }

    public void setBreakTime(Config config) {
        preferencesEditor.putString(breakTime, config.breakTimeInMinutes.toString());
        preferencesEditor.apply();

    }

    public void setActivated(Config config) {
        this.preferencesEditor.putBoolean(activated, config.activated);
        preferencesEditor.apply();

    }

    public void setFirstTimeActivated(Config config) {
        preferencesEditor.putBoolean(firstTimeActivated, config.firstTimeActivated == null ? false : config.firstTimeActivated);
        preferencesEditor.apply();

    }

    public void save(Config config) {

        setActivated(config);
        setWeekDays(config);
        setBreakTime(config);
        setFirstTimeActivated(config);
        setStartTime(config);
        setEndTime(config);

    }


}
