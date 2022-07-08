package com.secure.practice.entity.vo;

import java.util.Date;

public class Request<T> {
    private Date time;
    private T data;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    public boolean dateAvailable(){
        Date now = new Date();
        if (Math.abs(time.getTime()-now.getTime())<60*1000)
        {
            return true;
        }
        else return false;
    }
}
