package com.dmtech.iw;

import android.app.Application;
import android.util.Log;

public class IWeatherApp extends Application {

    // 数据库名字
    private static final String DB_NAME = "database";
    // GreenDao会话对象


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("iWeather", "应用程序已创建");
        //在这里初始化GreenDao

    }
}
