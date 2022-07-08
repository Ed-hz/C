package com.secure.practice.entity.vo;

import java.util.Date;

public class TimeAndKey {
    String key;
    Date date;

    @Override
    public String toString() {
        return "TimeAndKey{" +
                "key='" + key + '\'' +
                ", date=" + date +
                '}';
    }
    public boolean valid()
    {
        return Math.abs(date.getTime() - new Date().getTime()) < 60*1000;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
