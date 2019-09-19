package com.example.demo.presenter;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;
import com.example.demo.sql.MyDatabase;

public class SqlPresenter extends BasePresenter<IBaseView> {

    public void insertData(SQLiteDatabase sqLiteDatabase){
        ContentValues values = new ContentValues();

        values.put("name","第一行代码");
        values.put("author","郭霖");
        values.put("price",100);
        sqLiteDatabase.insert("book",null,values);
        values.clear();

        values.put("name","Java从入门到放弃");
        values.put("author","Tony");
        values.put("price",80);
        sqLiteDatabase.insert("book",null,values);
        values.clear();

        values.put("name","Android从入门到放弃");
        values.put("author","DW");
        values.put("price",50);
        sqLiteDatabase.insert("book",null,values);
    }

    public void deleteData(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.delete("book","price>?",new String[]{"90"});
    }

    public void updateData(SQLiteDatabase sqLiteDatabase){
        ContentValues values = new ContentValues();
        values.put("price",30);
        sqLiteDatabase.update("book",values,"name=?",new String[]{"Android从入门到放弃"});
    }

    public void queryData(SQLiteDatabase sqLiteDatabase){
        Cursor cursor = sqLiteDatabase.query("book",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));

                Log.d("SqlActivity","书名:" + name);
                Log.d("SqlActivity","作者:" + author);
                Log.d("SqlActivity","价格:" + price);
            }
        }
        cursor.close();
    }
}
