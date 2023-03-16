package com.chen.hiweatherproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.chen.hiweatherproject.DBHelper.mySqliteDBHelper;
import com.chen.hiweatherproject.FunSaveData.SaveMyCitiesBySharedPref;
import com.chen.hiweatherproject.SelfAdapter.myListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchCityActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    myListAdapter listAdapter;
    private int resultCode = 0;
    SaveMyCitiesBySharedPref saveSP;
    private HashMap<String, String> cities = new HashMap<>();    // 储存查询结果

    SQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        // 初始化dbhepler
        dbHelper = mySqliteDBHelper.getmInstance(this);

        initSharedPref();
        initViewCompose();
    }

    private void initViewCompose() {
        toolbar = findViewById(R.id.search_city_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                mIntent.putExtra("newCity", saveSP.readCitiesSharedPref());
                SearchCityActivity.this.setResult(resultCode, mIntent);
                finish();
            }
        });

        listView = findViewById(R.id.list_search_answer);
        listView.setVisibility(View.INVISIBLE);   // 初始化设置为不可见
        listAdapter = new myListAdapter(this, R.layout.list_answer_item, getHashMapValue(cities));
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String local = getHashMapKey(cities).get(position);
                Intent mIntent = new Intent();
                mIntent.putExtra("newCity", local);
                SearchCityActivity.this.setResult(resultCode, mIntent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setSubmitButtonEnabled(true);

        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            // SearchView 展开
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                // 展开逻辑
                listView.setVisibility(View.VISIBLE);
                return true;
            }

            // SearchView 折叠
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // 折叠逻辑
                listView.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 输入文字提交时
            @Override
            public boolean onQueryTextSubmit(String query) {
                listView.setVisibility(View.INVISIBLE);
                return true;
            }

            // 输入文字变动时
            @Override
            public boolean onQueryTextChange(String newText) {
                listView.setVisibility(View.VISIBLE);
                cities.clear();
                if (!newText.equals("") && newText != null) {
                    // 查询sql结果得到数组
                    cities = SearchCityList(newText);
                    // 集合 县级市 - 地级市 - 行省
                    listAdapter.setItemList(getHashMapValue(cities));
                    listAdapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String local = getHashMapKey(cities).get(position);
                            Intent mIntent = new Intent();
                            mIntent.putExtra("newCity", local);
                            SearchCityActivity.this.setResult(resultCode, mIntent);
                            finish();
                        }
                    });
                }
                return true;
            }
        });

        return true;
    }

    // 调用
    @SuppressLint("Range")
    public HashMap<String, String> SearchCityList(String cityName) {
        HashMap<String, String> list = new HashMap<>();
        int _id;
        int answerCount = 0;
        String answer_local ,answer_city, answer_province = null;
//        SQLiteOpenHelper dbHelper = mySqliteDBHelper.getmInstance(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (db.isOpen()){
            Cursor cursor = db.rawQuery("SELECT cityZh,  leaderZh, provinceZh FROM city WHERE cityZh like '%"+ cityName +"%' LIMIT 25;", null);
            while (cursor.moveToNext()) {
                answerCount += 1;
//                _id = cursor.getInt(cursor.getColumnIndex("_id"));
                answer_local = cursor.getString(cursor.getColumnIndex("cityZh"));
                answer_city = cursor.getString(cursor.getColumnIndex("leaderZh"));
                answer_province = cursor.getString(cursor.getColumnIndex("provinceZh"));
                list.put(answer_local , answer_local + " - " + answer_city + " - " + answer_province );
                System.out.println("get : (" + answer_local + ", " + answer_city + ", " + answer_province + ");");
            }
            cursor.close();
        }
        db.close();

        return list;
    }

    // 初始化SharePref
    private void initSharedPref() {
        saveSP = new SaveMyCitiesBySharedPref(this);
    }

    private List<String> getHashMapValue(HashMap<String, String> h) {
        List<String> l = new ArrayList<>();
        for (String s: h.keySet()) {
            l.add(h.get(s));
        }
        return l;
    }

    private List<String> getHashMapKey(HashMap<String, String> h) {
        List<String> l = new ArrayList<>();
        for (String s: h.keySet()) {
            l.add(s);
        }
        return l;
    }
}
