package com.example.demo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.demo.R;
import com.example.demo.adapter.ContactsAdapter;
import com.example.demo.bean.Contacts;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.ContactsPresenter;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends BasePresenterActivity<ContactsPresenter, IBaseView> implements IBaseView {

    private RecyclerView recyclerView;
    private List<Contacts> mContactsList;
    private ContactsAdapter contactsAdapter;

    @Override
    protected ContactsPresenter initPresenter(){
        return new ContactsPresenter();
    }

    @Override
    protected int getLayoutId(){
        return R.layout.activity_contacts;
    }

    @Override
    protected void initData(){
        setMyActionBar("Contacts",true);
        recyclerView = findViewById(R.id.recyclerView);
        mContactsList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(mContactsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactsAdapter);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
        else {
            mPresenter.getReadContacts(this,mContactsList,contactsAdapter);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permission,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mPresenter.getReadContacts(this,mContactsList,contactsAdapter);
                }else {
                    showToast("You denied the permission");
                }
                break;
            default:
                break;
        }
    }

}
