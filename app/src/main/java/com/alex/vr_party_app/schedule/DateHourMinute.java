package com.alex.vr_party_app.schedule;

public class DateHourMinute {

    private int date;
    private int hour;

    @Override
    public String toString() {
        return "DateHourMinute{" +
                "date=" + date +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    private int minute;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public DateHourMinute(int date, int hour, int minute) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
    }
}
