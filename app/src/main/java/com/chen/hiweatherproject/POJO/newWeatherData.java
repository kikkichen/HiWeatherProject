package com.chen.hiweatherproject.POJO;

import java.util.List;

public class newWeatherData {
    public String cityName;    // 城市名
    public String cityId;      //城市Id
    public String countryEn;       // 国家
    public String updateTime;      // 更新时间
    public List<singleDayData> pre7_wea_list;     // 未来15天天气
    public aqiAndSuggestion aqi;   // 空气质量与建议

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<singleDayData> getPre7_wea_list() {
        return pre7_wea_list;
    }

    public void setPre7_wea_list(List<singleDayData> pre7_wea_list) {
        this.pre7_wea_list = pre7_wea_list;
    }

    public aqiAndSuggestion getAqi() {
        return aqi;
    }

    public void setAqi(aqiAndSuggestion newAqi) {
        this.aqi = newAqi;
    }

    @Override
    public String toString() {
        return "newWeatherData{" +
                "cityName='" + cityName + '\'' +
                ", cityId='" + cityId + '\'' +
                ", countryEn='" + countryEn + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", pre7_wea_list=" + pre7_wea_list +
                ", aqi=" + aqi +
                '}';
    }
}
