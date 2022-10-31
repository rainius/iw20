package com.dmtech.iw;

import android.app.Application;
import android.util.Log;

import com.dmtech.iw.entity.DaoMaster;
import com.dmtech.iw.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

public class IWeatherApp extends Application {

    // 数据库名字
    private static final String DB_NAME = "database";
    // GreenDao会话对象
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("iWeather", "应用程序已创建");
        //在这里初始化GreenDao
        // 指定数据库名字（没有就创建）并建立会话对象
        // 如果数据库不存在，就创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DB_NAME);
        Database db = helper.getWritableDb();   //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);    //创建主管类
        mDaoSession = daoMaster.newSession();   //创建新的(唯一的)数据访问会话
    }

    /**
     * 获取会话对象
     * @return 会话对象
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
