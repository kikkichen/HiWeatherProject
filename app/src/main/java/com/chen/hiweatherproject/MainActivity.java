package com.chen.hiweatherproject;

import static com.chen.hiweatherproject.TestQueryActivity.myDecode;
import static com.chen.hiweatherproject.TestQueryActivity.parseWeatherJson;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.chen.hiweatherproject.FunSaveData.SaveMyCitiesByJSON;
import com.chen.hiweatherproject.FunSaveData.SaveMyCitiesBySharedPref;
import com.chen.hiweatherproject.POJO.newWeatherData;
import com.chen.hiweatherproject.net.OKHttpUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;

import com.chen.hiweatherproject.SelfAdapter.MyDetailAdapter;
import com.google.android.material.snackbar.Snackbar;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    AppBarLayout appBarLayout;
    CoordinatorLayout coordinatorLayout;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    MyDetailAdapter adapter;
    ActivityResultLauncher lancherToSearchCity; // 从搜索新城市Activity返回的值处理逻辑
    SaveMyCitiesBySharedPref saveSP;        // 动态更新默认城市
    private String DEFAULT_PAGE_CITY = "";
    private static final String targetURL = "http://www.tianqiapi.com/api?version=v9&appid=55639846&appsecret=D5zQt2HJ&city=";

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPuDongJson();

        setStatusBar();
        // 初始化SharePref
        initSharedPref();
        // 读默认城市数据
        loadDefaultCityName();
        // 初始化所有组件
        initViewCompose();
        // 配置RecyclerView卡片栏
        initRecyclerView();
        // 配置下滑刷新
        initSwipeFreshLayout();

        // 配置其他Activity的返回逻辑
        setMyResultActivityFunction();
    }

    private void setMyResultActivityFunction() {
        // 从搜索新城市Activity返回的值处理逻辑
        lancherToSearchCity = registerForActivityResult(new launcherMyContract(), new ActivityResultCallback<String>() {
            @Override
            public void onActivityResult(String result) {
                String newCityName = result;
                if (result != null || !result.equals("")) {
                    extracted(targetURL + newCityName);
                    writeDefaultCityName(result);
                }else {
                    extracted(targetURL + saveSP.readCitiesSharedPref());
                }
            }
        });
    }

    private void initViewCompose() {
        appBarLayout = findViewById(R.id.main_appBarLayout);
        coordinatorLayout = findViewById(R.id.main_coordinatorLayout);
        toolbar = findViewById(R.id.toolbar_show_localAddr);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test_query:
                        // 跳转到测试天气api请求页
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, TestQueryActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.search_other_city:
                        lancherToSearchCity.launch("nothing");
                }
                return true;
            }
        });

        // 解决CoodinatorLayout与SwipeFreshLayout在下拉动作中滑动冲突
        appBarLayout.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, i) -> {
            if (i >= 0) {
                swipeRefreshLayout.setEnabled(true); //当滑动到顶部的时候开启
            } else {
                swipeRefreshLayout.setEnabled(false); //否则关闭
            }
        });

        swipeRefreshLayout = findViewById(R.id.main_swipeRefreshLayout);
    }


    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_weatherInfo_cardSet);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> list = new ArrayList<>();
        for (int i=0; i<4; i++) {
            list.add("" + i);
        }

        SaveMyCitiesByJSON jsonFileIO = new SaveMyCitiesByJSON(this);
        newWeatherData data = parseWeatherJson(jsonFileIO.readCityWeatherData(DEFAULT_PAGE_CITY));     // 从本地文件读取上次刷新的天气数据

        // 更新壁纸
        setWeatherWallpaper(data.pre7_wea_list.get(0).wea_icon_tag_day);
        toolbar.setTitle(data.cityName);        // 更新ToolBar的城市名
        adapter = new MyDetailAdapter(this, null, data);    // 将数据传递给RecyclerView的Adapter
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("ResourceType")
    private void initSwipeFreshLayout() {
        swipeRefreshLayout = findViewById(R.id.main_swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_500, R.color.red_500, R.color.yellow_500, R.color.green_500);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                extracted(targetURL + saveSP.readCitiesSharedPref());
            }
        });
    }

    private void extracted(String targetURL) {
        new Thread(){
            @Override
            public void run() {
                try {
                    String content = myDecode(OKHttpUtils.getInstance().doGet(targetURL));
                    newWeatherData nwd = parseWeatherJson(content);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // 保存该次更新数据到本地json文件
                            SaveMyCitiesByJSON saveToFile = new SaveMyCitiesByJSON(MainActivity.this);
                            saveToFile.saveCityWeatherData(content, nwd.cityName);

                            adapter.FreshItemAndData(nwd);
                            toolbar.setTitle(nwd.cityName);

                            // 停止SwipeRefreshLayout的刷新动作
                            swipeRefreshLayout.setRefreshing(false);
                            // 壁纸更新
                            setWeatherWallpaper(nwd.pre7_wea_list.get(0).wea_icon_tag_day);
                            Snackbar.make(swipeRefreshLayout, "😃刷新成功!  [" + nwd.cityName +"] - " + nwd.pre7_wea_list.get(0).temperatureNow + "℃", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    if (e instanceof SocketTimeoutException) {
                        Snackbar.make(swipeRefreshLayout, "连接超时：" + e.toString(), Snackbar.LENGTH_SHORT).show();
                        closeMyReFreshing();
                    }else if (e instanceof ConnectException) {
                        Snackbar.make(swipeRefreshLayout, "连接异常：" + e.toString(), Snackbar.LENGTH_SHORT).show();
                        closeMyReFreshing();
                    }else {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    // Result API 设置协议类
    class launcherMyContract extends ActivityResultContract<String, String> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String input) {
            return new Intent(MainActivity.this, SearchCityActivity.class).putExtra("mes", input);
        }

        @Override
        public String parseResult(int resultCode, @Nullable Intent intent) {
            String answer = intent.getStringExtra("newCity");
            if (answer != null || !answer.equals("")){
                return answer;
            }else return "";
        }
    }

    // 初始化SharePref
    private void initSharedPref() {
        saveSP = new SaveMyCitiesBySharedPref(this);
    }

    // 从SharedPref中读默认城市数据
    private void loadDefaultCityName() {
        String temp = saveSP.readCitiesSharedPref();
        if (temp == null || temp.equals("")){
            DEFAULT_PAGE_CITY = "浦东新区";
        }else DEFAULT_PAGE_CITY = temp;
    }

    // 用SharePref写数据
    private void writeDefaultCityName(String cityName) {
        saveSP.saveCitiesSharedPref(cityName);
    }


    private void closeMyReFreshing() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    // 为Menu创建菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.materialtoolbar_menu, menu);
        return true;
    }

    // 设置状态栏
    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.parseColor("#FFFFFFFF"));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    // 设置天气背景
    private void setWeatherWallpaper(String status) {
        switch(status) {
            case "xue":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.snow));
                break;
            case "lei":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.thunder));
                break;
            case "shachen":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.smog));
                break;
            case "wu":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.fog));
                break;
            case "bingbao":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.hail));
                break;
            case "yun":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.cloudy));
                break;
            case "yu":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.rain));
                break;
            case "yin":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.coudyrain));
                break;
            case "qing":
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.sunny));
                break;
            default:
                coordinatorLayout.setBackground(getResources().getDrawable(R.drawable.test_out_bg));
                break;
        }
    }

    // 修复初始化无 浦东新区.json 导致的bug
    private void initPuDongJson() {
        // 从Assets资源中读SQL语句
        AssetManager am = getAssets();
        BufferedReader reader = null;
        String allContent = "";
        try {
            String line = "";
            InputStream input = am.open("pudongJSON");
            System.out.println(":::onCreate+DatabaseHelper");
            reader = new BufferedReader(new InputStreamReader(input));
            // 保存json
            while ((line = reader.readLine()) != null) {
                allContent += line;
            }
        } catch (IOException e) {
            System.out.println(":::onCreate+DatabaseHelper:  !![error]");
            e.printStackTrace();
        } finally {
            try {
                if (am != null) {
                    am.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SaveMyCitiesByJSON save = new SaveMyCitiesByJSON(this);
        save.saveCityWeatherData(myDecode(allContent), "浦东新区");
    }
}