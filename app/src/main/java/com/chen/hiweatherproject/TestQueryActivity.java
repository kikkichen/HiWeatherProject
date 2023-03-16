package com.chen.hiweatherproject;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chen.hiweatherproject.FunSaveData.SaveMyCitiesByJSON;
import com.chen.hiweatherproject.FunSaveData.SaveMyCitiesBySharedPref;
import com.chen.hiweatherproject.POJO.newWeatherData;
import com.chen.hiweatherproject.POJO.singleDayData;
import com.chen.hiweatherproject.POJO.aqiAndSuggestion;
import com.chen.hiweatherproject.POJO.alarm;
import com.chen.hiweatherproject.net.MyOkForCallBack;
import com.chen.hiweatherproject.net.OKHttpUtils;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TestQueryActivity extends Activity {
    private Button buttonOfGet;     // 在这个测试里 ，用来读city json的文件测试
//    private Button buttonOfPost;
    private Button buttonClearAll;
    private TextView textView;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private Handler handler = new Handler(Looper.getMainLooper());

    SaveMyCitiesBySharedPref saveSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_query_ok);
        initView();
        initMySwipeRefreshLayout();
        initSharedPref();
    }

    private void initSharedPref() {
        saveSP = new SaveMyCitiesBySharedPref(this);
    }

    private void initMySwipeRefreshLayout(){
        mySwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        mySwipeRefreshLayout.setColorSchemeResources(R.color.blue_500,
                R.color.red_500,
                R.color.yellow_500,
                R.color.green_500);

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                String city = saveSP.readCitiesSharedPref();
                String targetURL = "http://www.tianqiapi.com/api?version=v9&appid=55639846&appsecret=D5zQt2HJ&city=" + city;
                Log.d("kikkichen", "search local weather......");

                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String content = myDecode(OKHttpUtils.getInstance().doGet(targetURL));
                            newWeatherData nwd = parseWeatherJson(content);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(textView.getText() + nwd.toString() + "\n\n");
                                    // 保存该次更新数据到本地json文件
                                    SaveMyCitiesByJSON saveToFile = new SaveMyCitiesByJSON(TestQueryActivity.this);
                                    saveToFile.saveCityWeatherData(content, nwd.cityName);

                                    // 停止SwipeRefreshLayout的刷新动作
                                    mySwipeRefreshLayout.setRefreshing(false);
                                    Snackbar.make(mySwipeRefreshLayout, "😃刷新成功！！！", Snackbar.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            if (e instanceof SocketTimeoutException) {
                                Snackbar.make(mySwipeRefreshLayout, "连接超时：" + e.toString(), Snackbar.LENGTH_SHORT).show();
                                closeMyReFreshing();
                            }else if (e instanceof ConnectException) {
                                Snackbar.make(mySwipeRefreshLayout, "连接异常：" + e.toString(), Snackbar.LENGTH_SHORT).show();
                                closeMyReFreshing();
                            }else {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });
    }

    private void closeMyReFreshing() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                mySwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initView() {
        buttonOfGet = findViewById(R.id.button_get);
//        buttonOfPost = findViewById(R.id.button_post);
        buttonClearAll = findViewById(R.id.button_clear_all);
        textView = findViewById(R.id.my_testview);

        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(" ");
            }
        });


        // 测试读 json转javaBean
        buttonOfGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMyCitiesByJSON save = new SaveMyCitiesByJSON(TestQueryActivity.this);
                String loaclJsonString = save.readCityWeatherData("浦东新区");
                newWeatherData data = parseWeatherJson(loaclJsonString);
                textView.setText(textView.getText() + "\n\n" + data.toString() + "\n");
            }
        });
    }

    // json解析为newWeatherData类
    public static newWeatherData parseWeatherJson(String json) {
        newWeatherData weaData = new newWeatherData();
        try {
            JSONObject jsonObject1 = new JSONObject(json);
            weaData.setCityName(jsonObject1.getString("city"));
            weaData.setCityId(jsonObject1.getString("cityid"));
            weaData.setCountryEn(jsonObject1.getString("cityEn"));
            weaData.setUpdateTime(jsonObject1.getString("update_time"));
            weaData.pre7_wea_list = new ArrayList<>();
            weaData.aqi = new aqiAndSuggestion();

            // 分批次解析data数组
            JSONArray jarrayData = jsonObject1.getJSONArray("data");
            for (int i=0; i<7; i++) {
                singleDayData weaDay = new singleDayData();
                JSONObject tempJsonObject = (JSONObject) jarrayData.get(i);

                weaDay.setDay(tempJsonObject.getString("day"));
                weaDay.setDate(tempJsonObject.getString("date"));
                weaDay.setWeek(tempJsonObject.getString("week"));
                weaDay.setWea_status_day(tempJsonObject.getString("wea"));
                weaDay.setWea_icon_tag_day(tempJsonObject.getString("wea_img"));
                weaDay.setWea_status_night(tempJsonObject.getString("wea_night"));
                weaDay.setWea_icon_tag_night(tempJsonObject.getString("wea_night_img"));
                weaDay.setTemperatureNow(tempJsonObject.getString("tem"));
                weaDay.setTemperatureHigh(tempJsonObject.getString("tem1"));
                weaDay.setTemperatureLow(tempJsonObject.getString("tem2"));
                weaDay.setHumidity(tempJsonObject.getString("humidity"));
                weaDay.setVisibility(tempJsonObject.getString("visibility"));
                weaDay.setPressure(tempJsonObject.getString("pressure"));
                // 解析数组
                JSONArray jArrayWin = tempJsonObject.getJSONArray("win");
                String[] tempStr = {jArrayWin.getString(0), jArrayWin.getString(1)};
                weaDay.setWindy_direction(tempStr);

                weaDay.setWindy_speed_status(tempJsonObject.getString("win_speed"));
                weaDay.setWindy_speed_value(tempJsonObject.getString("win_meter"));
                weaDay.setSunriseTime(tempJsonObject.getString("sunrise"));
                weaDay.setSunsetTime(tempJsonObject.getString("sunset"));
                weaDay.setAir_weight_value(tempJsonObject.getString("air"));
                weaDay.setAir_weight_weight(tempJsonObject.getString("air_level"));
                weaDay.setAir_weight_tips(tempJsonObject.getString("air_tips"));

                weaData.pre7_wea_list.add(weaDay);
            }

            // aqi采集解析
            JSONObject jsonObjectAqi = (JSONObject) jsonObject1.get("aqi");
            weaData.aqi.setAir_weight_value(jsonObjectAqi.getString("air"));
            weaData.aqi.setAir_weight_level(jsonObjectAqi.getString("air_level"));
            weaData.aqi.setAir_tips(jsonObjectAqi.getString("air_tips"));
            weaData.aqi.setPm25_value(jsonObjectAqi.getString("pm25"));
            weaData.aqi.setPm25_desc(jsonObjectAqi.getString("pm25_desc"));
            weaData.aqi.setPm10_value(jsonObjectAqi.getString("pm10"));
            weaData.aqi.setPm10_desc(jsonObjectAqi.getString("pm10_desc"));
            weaData.aqi.setO3_value(jsonObjectAqi.getString("o3"));
            weaData.aqi.setO3_desc(jsonObjectAqi.getString("o3_desc"));
            weaData.aqi.setNo2_value(jsonObjectAqi.getString("no2"));
            weaData.aqi.setNo2_desc(jsonObjectAqi.getString("no2_desc"));
            weaData.aqi.setSo2_value(jsonObjectAqi.getString("so2"));
            weaData.aqi.setSo2_desc(jsonObjectAqi.getString("so2_desc"));
            weaData.aqi.setCo_value(jsonObjectAqi.getString("co"));
            weaData.aqi.setCo_desc(jsonObjectAqi.getString("co_desc"));
            weaData.aqi.setMask_suggestion(jsonObjectAqi.getString("kouzhao"));
            weaData.aqi.setSport_suggestion(jsonObjectAqi.getString("yundong"));
            weaData.aqi.setOutside_suggestion(jsonObjectAqi.getString("waichu"));
            weaData.aqi.setOpenWindow_suggestion(jsonObjectAqi.getString("kaichuang"));
            weaData.aqi.setJinghuaqi_suggestion(jsonObjectAqi.getString("jinghuaqi"));

        }catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return weaData;
    }

    // Unicode 转 UTF-8
    public static String myDecode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


}
