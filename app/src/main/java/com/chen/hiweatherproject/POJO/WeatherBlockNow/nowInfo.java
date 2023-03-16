package com.chen.hiweatherproject.POJO.WeatherBlockNow;

public class nowInfo {
    private String address;     // 地址
    private int temperature_now;        // 当前气温
    private String weather_status;      // 当前天气状态
    private String humidity;        // 湿度
    private int value_air_weight;      // 空气质量-值
    private String updateTime;

    public nowInfo(String address, int temperature_now, String weather_status, String humidity, int value_air_weight) {
        this.address = address;
        this.temperature_now = temperature_now;
        this.weather_status = weather_status;
        this.humidity = humidity;
        this.value_air_weight = value_air_weight;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getTemperature_now() {
        return temperature_now;
    }

    public void setTemperature_now(int temperature_now) {
        this.temperature_now = temperature_now;
    }

    public String getWeather_status() {
        return weather_status;
    }

    public void setWeather_status(String weather_status) {
        this.weather_status = weather_status;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public int getValue_air_weight() {
        return value_air_weight;
    }

    public void setValue_air_weight(int value_air_weight) {
        this.value_air_weight = value_air_weight;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
