package com.chen.hiweatherproject.FunSaveData;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class SaveMyCitiesByJSON {

    // 外部设备可用
    boolean mExternalStorageAvailable = false;
    // 外部设备可写
    boolean mExternalStorageWriteable = false;

    Context context;

    private final static String path = "/data/data/com.chen.hiweatherproject/files/local/";

    public SaveMyCitiesByJSON(Context context) {
        checkExternalStorage();
        this.context = context;
    }

    // 保存json文件
    public void saveCityWeatherData(String jsonStr, String localCity) {
        FileOutputStream out = null;
        BufferedWriter writer = null;

        // 首先判断外部储存状态
        checkExternalStorage();
        if (mExternalStorageWriteable) {
            try {
                out = context.openFileOutput(localCity + ".json", context.MODE_PRIVATE);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(jsonStr);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 读json文件
    public String readCityWeatherData(String localCity) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        checkExternalStorage();
        if (mExternalStorageAvailable) {
            try {
                in = context.openFileInput(localCity + ".json");
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return content.toString();
    }

    public void checkExternalStorage() {
        String state = Environment.getExternalStorageState();

        // 外部设备可用
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = true;
        }else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable =true;
            mExternalStorageWriteable = false;
        }else {
            mExternalStorageWriteable = mExternalStorageAvailable = false;
        }
    }
}
