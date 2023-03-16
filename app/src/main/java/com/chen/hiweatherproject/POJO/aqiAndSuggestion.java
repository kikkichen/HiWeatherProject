package com.chen.hiweatherproject.POJO;

public class aqiAndSuggestion {
    public String air_weight_value;    // 空气质量
    public String air_weight_level;   // 空气状态
    public String air_tips;        // 空气建议
    public String pm25_value;
    public String pm25_desc;
    public String pm10_value;
    public String pm10_desc;
    public String o3_value;
    public String o3_desc;
    public String no2_value;
    public String no2_desc;
    public String no3_value;
    public String no3_desc;
    public String so2_value;
    public String so2_desc;
    public String co_value;
    public String co_desc;
    public String mask_suggestion;     // 佩戴口罩的建议😷
    public String sport_suggestion;        // 运动的建议
    public String outside_suggestion;      // 外出建议
    public String openWindow_suggestion;       // 开窗建议🍵
    public String jinghuaqi_suggestion;        // 空气净化器建议

    public String getAir_weight_value() {
        return air_weight_value;
    }

    public void setAir_weight_value(String air_weight_value) {
        this.air_weight_value = air_weight_value;
    }

    public String getAir_weight_level() {
        return air_weight_level;
    }

    public void setAir_weight_level(String air_weight_level) {
        this.air_weight_level = air_weight_level;
    }

    public String getAir_tips() {
        return air_tips;
    }

    public void setAir_tips(String air_tips) {
        this.air_tips = air_tips;
    }

    public String getPm25_value() {
        return pm25_value;
    }

    public void setPm25_value(String pm25_value) {
        this.pm25_value = pm25_value;
    }

    public String getPm25_desc() {
        return pm25_desc;
    }

    public void setPm25_desc(String pm25_desc) {
        this.pm25_desc = pm25_desc;
    }

    public String getPm10_value() {
        return pm10_value;
    }

    public void setPm10_value(String pm10_value) {
        this.pm10_value = pm10_value;
    }

    public String getPm10_desc() {
        return pm10_desc;
    }

    public void setPm10_desc(String pm10_desc) {
        this.pm10_desc = pm10_desc;
    }

    public String getO3_value() {
        return o3_value;
    }

    public void setO3_value(String o3_value) {
        this.o3_value = o3_value;
    }

    public String getO3_desc() {
        return o3_desc;
    }

    public void setO3_desc(String o3_desc) {
        this.o3_desc = o3_desc;
    }

    public String getNo2_value() {
        return no2_value;
    }

    public void setNo2_value(String no2_value) {
        this.no2_value = no2_value;
    }

    public String getNo2_desc() {
        return no2_desc;
    }

    public void setNo2_desc(String no2_desc) {
        this.no2_desc = no2_desc;
    }

    public String getNo3_value() {
        return no3_value;
    }

    public void setNo3_value(String no3_value) {
        this.no3_value = no3_value;
    }

    public String getNo3_desc() {
        return no3_desc;
    }

    public void setNo3_desc(String no3_desc) {
        this.no3_desc = no3_desc;
    }

    public String getSo2_value() {
        return so2_value;
    }

    public void setSo2_value(String so2_value) {
        this.so2_value = so2_value;
    }

    public String getSo2_desc() {
        return so2_desc;
    }

    public void setSo2_desc(String so2_desc) {
        this.so2_desc = so2_desc;
    }

    public String getCo_value() {
        return co_value;
    }

    public void setCo_value(String co_value) {
        this.co_value = co_value;
    }

    public String getCo_desc() {
        return co_desc;
    }

    public void setCo_desc(String co_desc) {
        this.co_desc = co_desc;
    }

    public String getMask_suggestion() {
        return mask_suggestion;
    }

    public void setMask_suggestion(String mask_suggestion) {
        this.mask_suggestion = mask_suggestion;
    }

    public String getSport_suggestion() {
        return sport_suggestion;
    }

    public void setSport_suggestion(String sport_suggestion) {
        this.sport_suggestion = sport_suggestion;
    }

    public String getOutside_suggestion() {
        return outside_suggestion;
    }

    public void setOutside_suggestion(String outside_suggestion) {
        this.outside_suggestion = outside_suggestion;
    }

    public String getOpenWindow_suggestion() {
        return openWindow_suggestion;
    }

    public void setOpenWindow_suggestion(String openWindow_suggestion) {
        this.openWindow_suggestion = openWindow_suggestion;
    }

    public String getJinghuaqi_suggestion() {
        return jinghuaqi_suggestion;
    }

    public void setJinghuaqi_suggestion(String jinghuaqi_suggestion) {
        this.jinghuaqi_suggestion = jinghuaqi_suggestion;
    }

    @Override
    public String toString() {
        return "aqiAndSuggestion{" +
                "air_weight_value='" + air_weight_value + '\'' +
                ", air_weight_level='" + air_weight_level + '\'' +
                ", air_tips='" + air_tips + '\'' +
                ", pm25_value='" + pm25_value + '\'' +
                ", pm25_desc='" + pm25_desc + '\'' +
                ", pm10_value='" + pm10_value + '\'' +
                ", pm10_desc='" + pm10_desc + '\'' +
                ", o3_value='" + o3_value + '\'' +
                ", o3_desc='" + o3_desc + '\'' +
                ", no2_value='" + no2_value + '\'' +
                ", no2_desc='" + no2_desc + '\'' +
                ", no3_value='" + no3_value + '\'' +
                ", no3_desc='" + no3_desc + '\'' +
                ", so2_value='" + so2_value + '\'' +
                ", so2_desc='" + so2_desc + '\'' +
                ", co_value='" + co_value + '\'' +
                ", co_desc='" + co_desc + '\'' +
                ", mask_suggestion='" + mask_suggestion + '\'' +
                ", sport_suggestion='" + sport_suggestion + '\'' +
                ", outside_suggestion='" + outside_suggestion + '\'' +
                ", openWindow_suggestion='" + openWindow_suggestion + '\'' +
                ", jinghuaqi_suggestion='" + jinghuaqi_suggestion + '\'' +
                '}';
    }
}
