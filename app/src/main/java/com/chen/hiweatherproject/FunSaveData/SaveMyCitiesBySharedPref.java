package com.chen.hiweatherproject.FunSaveData;

import android.content.Context;
import android.content.SharedPreferences;

public class SaveMyCitiesBySharedPref {

    private Context mContext;
    public SharedPreferences sp;

    public SaveMyCitiesBySharedPref(Context context) {
        this.mContext = context;
        this.sp = mContext.getSharedPreferences("local_city", Context.MODE_PRIVATE);
    }

    // 读城市
    public String readCitiesSharedPref() {
        String city = sp.getString("local_city", "");
        return city;
    }

    // 写城市
    public void saveCitiesSharedPref(String newCity) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("local_city", newCity);
        editor.commit();
    }
}
