package com.chen.hiweatherproject.POJO;

public class alarm {
    private String alarm_type;      // 警报类型
    private String alarm_level;     // 警报等级
    private String alarm_content;   // 警报内容

    public alarm(String alarm_type, String alarm_level, String alarm_content) {
        this.alarm_type = alarm_type;
        this.alarm_level = alarm_level;
        this.alarm_content = alarm_content;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getAlarm_level() {
        return alarm_level;
    }

    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }

    public String getAlarm_content() {
        return alarm_content;
    }

    public void setAlarm_content(String alarm_content) {
        this.alarm_content = alarm_content;
    }

    @Override
    public String toString() {
        return "alarm{" +
                "alarm_type='" + alarm_type + '\'' +
                ", alarm_level='" + alarm_level + '\'' +
                ", alarm_content='" + alarm_content + '\'' +
                '}';
    }
}
