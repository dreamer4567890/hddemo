package com.example.demo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String CREATE_BOOK= "create table book(id integer primary key autoincrement,"
            + "name text," + "author text," + "price real)";
    private Context mContext;

    public MyDatabase(Context context, String name,
                      SQLiteDatabase.CursorFactory factory, int version) {
        super( context, name, factory, version);
        mContext= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        Toast.makeText(mContext , "数据库创建成功" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
