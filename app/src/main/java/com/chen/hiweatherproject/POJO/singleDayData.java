package com.chen.hiweatherproject.POJO;

import java.util.Arrays;

public class singleDayData {
    public String day;         // 日期
    public String date;        // 详细日期
    public String week;        // 星期
    public String wea_status_day;      // 日间天气状态
    public String wea_icon_tag_day;    // 日间天气图标
    public String wea_status_night;    // 夜间天气状态
    public String wea_icon_tag_night;    // 夜间天气图标
    public String temperatureNow;  // 当前气温
    public String temperatureLow;  // 当日最低气温
    public String TemperatureHigh;    // 当日最高气温
    public String humidity;        // 当日湿度
    public String visibility;      // 能见度
    public String pressure;        // 大气压强
    public String[] windy_direction;       // 风向
    public String windy_speed_status;     // 风级
    public String windy_speed_value;       // 风速
    public String sunriseTime;     // 日升时间
    public String sunsetTime;      // 日落时间
    public String air_weight_value;        // 空气质量
    public String air_weight_weight;       // 空气质量等级
    public String air_weight_tips;     // 空气质量建议

    public alarm wea_alarm;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWea_status_day() {
        return wea_status_day;
    }

    public void setWea_status_day(String wea_status_day) {
        this.wea_status_day = wea_status_day;
    }

    public String getWea_icon_tag_day() {
        return wea_icon_tag_day;
    }

    public void setWea_icon_tag_day(String wea_icon_tag_day) {
        this.wea_icon_tag_day = wea_icon_tag_day;
    }

    public String getWea_status_night() {
        return wea_status_night;
    }

    public void setWea_status_night(String wea_status_night) {
        this.wea_status_night = wea_status_night;
    }

    public String getWea_icon_tag_night() {
        return wea_icon_tag_night;
    }

    public void setWea_icon_tag_night(String wea_icon_tag_night) {
        this.wea_icon_tag_night = wea_icon_tag_night;
    }

    public String getTemperatureNow() {
        return temperatureNow;
    }

    public void setTemperatureNow(String temperatureNow) {
        this.temperatureNow = temperatureNow;
    }

    public String getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(String temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public String getTemperatureHigh() {
        return TemperatureHigh;
    }

    public void setTemperatureHigh(String temperatureHigh) {
        TemperatureHigh = temperatureHigh;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String[] getWindy_direction() {
        return windy_direction;
    }

    public void setWindy_direction(String[] windy_direction) {
        this.windy_direction = windy_direction;
    }

    public String getWindy_speed_status() {
        return windy_speed_status;
    }

    public void setWindy_speed_status(String windy_speed_status) {
        this.windy_speed_status = windy_speed_status;
    }

    public String getWindy_speed_value() {
        return windy_speed_value;
    }

    public void setWindy_speed_value(String windy_speed_value) {
        this.windy_speed_value = windy_speed_value;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getAir_weight_value() {
        return air_weight_value;
    }

    public void setAir_weight_value(String air_weight_value) {
        this.air_weight_value = air_weight_value;
    }

    public String getAir_weight_weight() {
        return air_weight_weight;
    }

    public void setAir_weight_weight(String air_weight_weight) {
        this.air_weight_weight = air_weight_weight;
    }

    public String getAir_weight_tips() {
        return air_weight_tips;
    }

    public void setAir_weight_tips(String air_weight_tips) {
        this.air_weight_tips = air_weight_tips;
    }

    public alarm getWea_alarm() {
        return wea_alarm;
    }

    public void setWea_alarm(alarm wea_alarm) {
        this.wea_alarm = wea_alarm;
    }

    @Override
    public String toString() {
        return "singleDayData{" +
                "day='" + day + '\'' +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", wea_status_day='" + wea_status_day + '\'' +
                ", wea_icon_tag_day='" + wea_icon_tag_day + '\'' +
                ", wea_status_night='" + wea_status_night + '\'' +
                ", wea_icon_tag_night='" + wea_icon_tag_night + '\'' +
                ", temperatureNow='" + temperatureNow + '\'' +
                ", temperatureLow='" + temperatureLow + '\'' +
                ", TemperatureHigh='" + TemperatureHigh + '\'' +
                ", humidity='" + humidity + '\'' +
                ", visibility='" + visibility + '\'' +
                ", pressure='" + pressure + '\'' +
                ", windy_direction=" + Arrays.toString(windy_direction) +
                ", windy_speed_status='" + windy_speed_status + '\'' +
                ", windy_speed_value='" + windy_speed_value + '\'' +
                ", sunriseTime='" + sunriseTime + '\'' +
                ", sunsetTime='" + sunsetTime + '\'' +
                ", air_weight_value='" + air_weight_value + '\'' +
                ", air_weight_weight='" + air_weight_weight + '\'' +
                ", air_weight_tips='" + air_weight_tips + '\'' +
                ", wea_alarm=" + wea_alarm +
                '}';
    }
}
