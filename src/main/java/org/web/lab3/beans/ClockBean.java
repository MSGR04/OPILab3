package org.web.lab3.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Named
@RequestScoped
public class ClockBean {

    private String currentTime;

    @PostConstruct
    public void init() {
        updateTime();
    }

    public void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        formatter.setTimeZone(TimeZone.getDefault());
        this.currentTime = formatter.format(new Date());
    }

    public String getCurrentTime() {
        updateTime();
        return currentTime;
    }
}