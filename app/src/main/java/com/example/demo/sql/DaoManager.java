package com.example.demo.sql;

import android.content.Context;

import com.example.demo.greendao.DaoMaster;
import com.example.demo.greendao.DaoSession;

public class DaoManager {

    private final String DB_NAME = "dao.db";
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static DaoManager mInstance;

    private DaoManager() {
    }

    public static DaoManager getInstance() {
        if (mInstance == null) {
            synchronized (DaoManager.class) {
                if (mInstance == null) {
                    mInstance = new DaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getDaoMaster(Context mContext) {

        DaoMaster.DevOpenHelper mHelper = new DaoMaster
                .DevOpenHelper(mContext, DB_NAME, null);
        daoMaster = new DaoMaster(mHelper.getWritableDatabase());
        return daoMaster;
    }

    public DaoSession getDaoSession(Context mContext) {

        if (daoSession == null) {

            if (daoMaster == null) {
                getDaoMaster(mContext);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }
}
