package com.chen.hiweatherproject.DBHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class mySqliteDBHelper extends SQLiteOpenHelper {

    public static final String DATA_BASE_FILENAME = "city_cn.db";
    public static final String TABLE_NAME = "city";
    private List tables = new ArrayList();
    private Context mContext;

    private static SQLiteOpenHelper mInstance;
    public static synchronized SQLiteOpenHelper getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new mySqliteDBHelper(context, DATA_BASE_FILENAME, null ,1);
        }
        return mInstance;
    }

    public mySqliteDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 从Assets资源中读SQL语句
        AssetManager am = mContext.getAssets();
        BufferedReader reader = null;
        try {
            InputStream input = am.open("city_cn");
            System.out.println(":::onCreate+DatabaseHelper");
            reader = new BufferedReader(new InputStreamReader(input));
            // 执行SQL语句
            executeSqlScript(db, reader);

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    // 执行SQL文件文本
    public void executeSqlScript(SQLiteDatabase db, BufferedReader reader)
            throws IOException {
        StringBuilder sql = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (TextUtils.isEmpty(line) || line.startsWith("#")) {
                continue;
            }
            if (TextUtils.isEmpty(line) || line.startsWith("=")) {
                line = line.replaceAll("=", "");
                tables.add(line);
                continue;
            }
//            System.out.println("###### line : " + line);
            line = line.trim();
            int index = line.indexOf(';');
            if (index >= 0) {
                String firstStr = line.substring(0, index + 1);
                sql.append(firstStr).append('\n');
                try {
                    db.execSQL(sql.toString()); // make database
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                sql = new StringBuilder();
                if (index < line.length()) {
                    String lastStr = line.substring(index + 1);
                    if (!TextUtils.isEmpty(lastStr)) {
                        sql.append(lastStr);
                    }
                }
            } else {
                sql.append(line).append('\n');
            }
        }
        if (sql.length() > 0) {
            try {
                db.execSQL(sql.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

