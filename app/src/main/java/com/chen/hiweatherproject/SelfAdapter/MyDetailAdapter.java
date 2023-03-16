package com.chen.hiweatherproject.SelfAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.hiweatherproject.POJO.aqiAndSuggestion;
import com.chen.hiweatherproject.POJO.newWeatherData;
import com.chen.hiweatherproject.POJO.singleDayData;
import com.chen.hiweatherproject.R;
import com.chen.hiweatherproject.VisualWeather.DrawAirWeight;
import com.chen.hiweatherproject.VisualWeather.DrawPre15day;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> cardTagList = new ArrayList<>();   // 卡片标签
    private Context context;
    private LayoutInflater layoutInflater;
    private newWeatherData totalWeatherData;    // 天气数据接收属性

    private int lastPosition;   // 用于在动画载入属性中充当位置判断的参数
    private List<Boolean> passAnimationViewList;    // 判断清楚动画执行列表的依据列表

    public final static int TYPE0 = 0x33;   // now_show
    public final static int TYPE1 = 0x34;   // 未来15天
    public final static int TYPE2 = 0x35;   // 空气质量
    public final static int TYPE3 = 0x36;   // 日升日落
    public final static int TYPE4 = 0x37;   // 详细数据
    public final static int TYPE5 = 0x38;
    public final static int TYPE6 = 0x39;

    public MyDetailAdapter( Context context, List<String> lists, newWeatherData data) {
        this.cardTagList = lists;
        this.context = context;
        this.totalWeatherData = data;
        this.layoutInflater = LayoutInflater.from(context);     // 初始化填充器
        passAnimationViewList = new ArrayList<Boolean>();
        for (int i=0; i<5; i++) {
            passAnimationViewList.add(false);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE0;
            case 1:
                return TYPE1;
            case 2:
                return TYPE2;
            case 3:
                return TYPE3;
            case 4:
                return TYPE4;
            case 5:
                return TYPE5;
            case 6:
                return TYPE6;
        }
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE0) {
            return new nowLightWeatherHolder(layoutInflater.inflate(R.layout.now_light_weather_block, parent, false));
        }else if (viewType == TYPE1) {
            return new pre15WeatherCardHolder(layoutInflater.inflate(R.layout.pre_15day_cardview, parent, false));
        }else if (viewType == TYPE2) {
            return new airWeightCardHolder(layoutInflater.inflate(R.layout.air_weight_cardview, parent, false));
        }else if (viewType == TYPE3) {
            return new sunriseAndSunsetCardHolder(layoutInflater.inflate(R.layout.sunrise_and_sunset_cardview, parent, false));
        }else if (viewType == TYPE4) {
            return new detailInfoCardHolder(layoutInflater.inflate(R.layout.detail_card, parent, false));
        }
        return new sunriseAndSunsetCardHolder(layoutInflater.inflate(R.layout.sunrise_and_sunset_cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof nowLightWeatherHolder) {
            nowLightWeatherHolder holder1 = (nowLightWeatherHolder) holder;
            singleDayData dayData = totalWeatherData.pre7_wea_list.get(0);      // 今日数据
            holder1.tv_temperature_now.setText(dayData.temperatureNow);
            holder1.tv_air_weight.setText("空气质量 - " + dayData.air_weight_weight);
            holder1.tv_weather_status.setText(dayData.wea_status_day);
            holder1.tv_humidity.setText("湿度 - " + dayData.humidity);
            holder1.tv_update_time.setText(totalWeatherData.updateTime.substring(11,16));   // 截取时分单位
            setAnimation(holder1.itemView, position);

        }else if (holder instanceof pre15WeatherCardHolder) {
            pre15WeatherCardHolder holder1 = (pre15WeatherCardHolder) holder;
            List<singleDayData> singleDayList = totalWeatherData.pre7_wea_list;     // 拷贝高低温数据
            String[] pre7HighTemperature = new String[totalWeatherData.pre7_wea_list.size()],
                    pre7LowTemperature = new String[totalWeatherData.pre7_wea_list.size()],
                    pre7XLabelDate = new String[totalWeatherData.pre7_wea_list.size()];

            for (int i=0; i<7; i++) {
                pre7HighTemperature[i] = singleDayList.get(i).TemperatureHigh;
                pre7LowTemperature[i] = singleDayList.get(i).temperatureLow;
                pre7XLabelDate[i] = singleDayList.get(i).day;
            }

            DrawPre15day drawPre15day = new DrawPre15day(context, holder1.lineChart, pre7HighTemperature, pre7LowTemperature, pre7XLabelDate);
            setAnimation(holder1.itemView, position);

        }else if (holder instanceof airWeightCardHolder) {
            airWeightCardHolder holder1 = (airWeightCardHolder) holder;
            aqiAndSuggestion aqiData = totalWeatherData.aqi;
            HashMap<String, String> airPieDatas = new HashMap<String, String>();
            airPieDatas.put("PM2.5", aqiData.pm25_value);
            airPieDatas.put("PM10", aqiData.pm10_value);
            airPieDatas.put("O3", aqiData.o3_value);
            airPieDatas.put("NO2", aqiData.no2_value);
            airPieDatas.put("SO2", aqiData.so2_value);
            airPieDatas.put("CO", aqiData.co_value);
            airPieDatas.put("level", aqiData.air_weight_level);
            DrawAirWeight drawAirWeight = new DrawAirWeight(context, holder1.pieChart, airPieDatas);

            holder1.pb_pm25.setProgress(Integer.parseInt(aqiData.pm25_value));
            holder1.tv_pm25.setText(aqiData.pm25_value);
            holder1.pb_pm10.setProgress(Integer.parseInt(aqiData.pm10_value));
            holder1.tv_pm10.setText(aqiData.pm10_value);
            holder1.pb_o3.setProgress(Integer.parseInt(aqiData.o3_value));
            holder1.tv_o3.setText(aqiData.o3_value);
            holder1.pb_no2.setProgress(Integer.parseInt(aqiData.no2_value));
            holder1.tv_no2.setText(aqiData.no2_value);
            holder1.pb_so2.setProgress(Integer.parseInt(aqiData.so2_value));
            holder1.tv_so2.setText(aqiData.so2_value);
            holder1.pb_co.setProgress(1);           // 默认<1
            holder1.tv_co.setText("<1");
            setAnimation(holder1.itemView, position);

        }else  if (holder instanceof sunriseAndSunsetCardHolder) {
            sunriseAndSunsetCardHolder holder1 = (sunriseAndSunsetCardHolder) holder;
            holder1.sunriseTime.setText(totalWeatherData.pre7_wea_list.get(1).sunriseTime);
            holder1.sunsetTime.setText(totalWeatherData.pre7_wea_list.get(1).sunsetTime);
            setAnimation(holder1.itemView, position);

        }else if (holder instanceof detailInfoCardHolder) {
            detailInfoCardHolder holder1 = (detailInfoCardHolder) holder;
            singleDayData dayData = totalWeatherData.pre7_wea_list.get(0);
            aqiAndSuggestion aqiData = totalWeatherData.aqi;
            holder1.tv_windy_speed_value.setText(dayData.windy_speed_value);
            holder1.tv_windy_speed_status.setText("(" + dayData.windy_speed_status + ")");
            holder1.tv_detail_windy_day_status.setText(dayData.windy_direction[0]);
            holder1.tv_detail_windy_night_status.setText(dayData.windy_direction[1]);
            holder1.tv_show_pressure_value.setText(dayData.pressure);
            holder1.tv_mask_value.setText(aqiData.mask_suggestion);
            holder1.tv_do_sport_value.setText(aqiData.sport_suggestion);
            holder1.tv_out_side_value.setText(aqiData.outside_suggestion);
            holder1.tv_window_value.setText(aqiData.openWindow_suggestion);
            setAnimation(holder1.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    // FreshDataFrom SwipeFreshLayout
    public void FreshItemAndData(newWeatherData newData) {
        this.totalWeatherData = newData;
        notifyDataSetChanged();
    }

    // 设置底部进入动画
    private void setAnimation(View viewToAnimation, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimation.getContext(), R.anim.item_bottom_in);
            viewToAnimation.startAnimation(animation);
            lastPosition = position;
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    passAnimationViewList.set(position, true);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // 已经DetachedFromWindow的view清理动画
        holder.itemView.clearAnimation();
//        System.out.println("onViewDetachedFromWindow" + holder.getPosition() + ": " + passAnimationViewList.get(holder.getPosition()));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 防止上滑动过程还没有DetachedFromWindow的View动画被再加载
        if ( passAnimationViewList.get(holder.getLayoutPosition())) {
            holder.itemView.clearAnimation();
//            System.out.println("onViewAttachedToWindow" + holder.getPosition() + ": " + passAnimationViewList.get(holder.getPosition()));
        }
    }


    // Holder of Now
    static class nowLightWeatherHolder extends RecyclerView.ViewHolder {
        TextView tv_temperature_now, tv_weather_status, tv_humidity, tv_air_weight, tv_update_time;

        public nowLightWeatherHolder(@NonNull View itemView) {
            super(itemView);
            tv_temperature_now = itemView.findViewById(R.id.tv_show_now_temperature);
            tv_weather_status = itemView.findViewById(R.id.tv_show_now_weather_status);
            tv_humidity = itemView.findViewById(R.id.tv_show_now_humidity);
            tv_air_weight = itemView.findViewById(R.id.tv_show_now_air_weight);
            tv_update_time = itemView.findViewById(R.id.tv_update_time);
        }
    }

    // Holder of 未来15天天气
    static class pre15WeatherCardHolder extends RecyclerView.ViewHolder{
        CardView pre_15_cardView;
        LineChart lineChart;

        public pre15WeatherCardHolder(@NonNull View itemView) {
            super(itemView);
            pre_15_cardView = itemView.findViewById(R.id.pre_15day_cardBody);
            lineChart = itemView.findViewById(R.id.pre15_line_Chart);
        }
    }

    // Holder of 空气指数
    static class airWeightCardHolder extends RecyclerView.ViewHolder{
        CardView airWeight_cardView;
        TextView tv_pm25, tv_pm10, tv_o3, tv_no2, tv_so2, tv_co;
        ProgressBar pb_pm25, pb_pm10, pb_o3, pb_no2, pb_so2, pb_co;
        PieChart pieChart;

        public airWeightCardHolder(@NonNull View itemView) {
            super(itemView);
            airWeight_cardView = itemView.findViewById(R.id.air_weight_cardBody);
            tv_pm25 = itemView.findViewById(R.id.pm25_air_value);
            tv_pm10 = itemView.findViewById(R.id.pm10_air_value);
            tv_o3 = itemView.findViewById(R.id.o3_air_value);
            tv_no2 = itemView.findViewById(R.id.no2_air_value);
            tv_so2 = itemView.findViewById(R.id.so2_air_value);
            tv_co = itemView.findViewById(R.id.co_air_value);
            pb_pm25 = itemView.findViewById(R.id.progressbar_pm25);
            pb_pm10 = itemView.findViewById(R.id.progressbar_pm10);
            pb_o3 = itemView.findViewById(R.id.progressbar_o3);
            pb_no2 = itemView.findViewById(R.id.progressbar_no2);
            pb_so2 = itemView.findViewById(R.id.progressbar_so2);
            pb_co = itemView.findViewById(R.id.progressbar_co);
            pieChart = itemView.findViewById(R.id.air_weight_pie_chart);
        }
    }

    // Holder of 日升日落
    static class sunriseAndSunsetCardHolder extends RecyclerView.ViewHolder{
        CardView sunCardView;
        TextView sunriseTime;
        TextView sunsetTime;

        public sunriseAndSunsetCardHolder(@NonNull View itemView) {
            super(itemView);
            sunCardView = itemView.findViewById(R.id.sunrise_info_cardBody);
            sunriseTime = itemView.findViewById(R.id.text_show_sunrise_time);
            sunsetTime = itemView.findViewById(R.id.text_show_sunset_time);
        }
    }

    // Holder of 详细数据
    static class detailInfoCardHolder extends RecyclerView.ViewHolder {
        CardView detailCardBody;
        TextView tv_windy_speed_value;
        TextView tv_windy_speed_status;
        TextView tv_detail_windy_day_status;
        TextView tv_detail_windy_night_status;
        TextView tv_show_pressure_value;
        TextView tv_mask_value;
        TextView tv_do_sport_value;
        TextView tv_out_side_value;
        TextView tv_window_value;

        public detailInfoCardHolder(@NonNull View itemView) {
            super(itemView);
            detailCardBody = itemView.findViewById(R.id.detail_info_cardBody);
            tv_windy_speed_value = itemView.findViewById(R.id.tv_win_speed_value);
            tv_windy_speed_status = itemView.findViewById(R.id.tv_win_speed_status);
            tv_detail_windy_day_status = itemView.findViewById(R.id.tv_detail_win_speed_status_value_day);
            tv_detail_windy_night_status = itemView.findViewById(R.id.tv_detail_win_speed_status_value_night);
            tv_show_pressure_value = itemView.findViewById(R.id.tv_show_pressure_value);
            tv_mask_value = itemView.findViewById(R.id.tv_mask_value);
            tv_do_sport_value = itemView.findViewById(R.id.tv_do_sport_value);
            tv_out_side_value = itemView.findViewById(R.id.tv_out_side_value);
            tv_window_value = itemView.findViewById(R.id.tv_window_value);

        }
    }
}
