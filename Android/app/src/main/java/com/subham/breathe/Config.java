package com.subham.breathe;


class Config {
    String Id;
    String Email;
    String Name;
    String[] WorkDays;
    String StartTime;
    String EndTime;
    int breakTimeInMinutes;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String[] getWorkDays() {
        return WorkDays;
    }

    public void setWorkDays(String[] workDays) {
        WorkDays = workDays;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public int getBreakTimeInMinutes() {
        return breakTimeInMinutes;
    }

    public void setBreakTimeInMinutes(int breakTimeInMinutes) {
        this.breakTimeInMinutes = breakTimeInMinutes;
    }
}
