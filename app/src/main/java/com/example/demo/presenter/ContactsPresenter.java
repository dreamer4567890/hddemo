package com.example.demo.presenter;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.demo.adapter.ContactsAdapter;
import com.example.demo.bean.Contacts;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;

import java.util.List;

public class ContactsPresenter extends BasePresenter<IBaseView> {

    public void getReadContacts(Context context, List<Contacts> list, ContactsAdapter adapter){
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
            if(cursor != null){
                while (cursor.moveToNext()){
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    list.add(new Contacts(displayName,number));
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }
}
