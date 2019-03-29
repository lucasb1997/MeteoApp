package com.app.compagnietropes.actionapp.models;

public class Weather {
    private String description;
    private String icon;
    private String temp;
    private String pressure;
    private String humidity;
    private String tempMin;
    private String tempMax;
    private String speed;
    private String cloud;

    public Weather(String description, String icon, String temp, String pressure, String humidity, String tempMin, String tempMax, String speed, String cloud) {
        this.description = description;
        this.icon = icon;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.speed = speed;
        this.cloud = cloud;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getTemp() {
        return temp;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getSpeed() {
        return speed;
    }

    public String getCloud() {
        return cloud;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }
}
