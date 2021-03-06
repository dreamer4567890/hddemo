package com.example.demo.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.SqlPresenter;
import com.example.demo.sql.MyDatabase;

public class SqlActivity extends BasePresenterActivity<SqlPresenter, IBaseView> implements IBaseView, View.OnClickListener {

    private Button btAdd;
    private Button btDelete;
    private Button btUpdate;
    private Button btQuery;
    private MyDatabase myDatabase;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected SqlPresenter initPresenter(){
        return new SqlPresenter();
    }

    @Override
    protected int getLayoutId(){
        return R.layout.activity_sql;
    }

    @Override
    protected void initData(){
        setMyActionBar("SQLite",false);
        btAdd = findViewById(R.id.bt_add);
        btDelete = findViewById(R.id.bt_delete);
        btUpdate = findViewById(R.id.bt_update);
        btQuery = findViewById(R.id.bt_query);
        btAdd.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
        btQuery.setOnClickListener(this);
        myDatabase = new MyDatabase(this,"Book.db",null,1);
        sqLiteDatabase = myDatabase.getWritableDatabase();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_add:
                mPresenter.insertData(sqLiteDatabase);
                showToast("insert");
                break;
            case R.id.bt_delete:
                mPresenter.deleteData(sqLiteDatabase);
                showToast("delete");
                break;
            case R.id.bt_update:
                mPresenter.updateData(sqLiteDatabase);
                showToast("update");
                break;
            case R.id.bt_query:
                mPresenter.queryData(sqLiteDatabase);
                showToast("query");
                break;
            default:
                break;
        }
    }
}
