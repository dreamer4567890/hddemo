package com.example.demo.activity;

import android.support.v7.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.bean.greendao.Card;
import com.example.demo.bean.greendao.User;
import com.example.demo.greendao.CardDao;
import com.example.demo.greendao.DaoMaster;
import com.example.demo.greendao.DaoSession;
import com.example.demo.greendao.UserDao;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.GreenDaoPresenter;

public class GreenDaoActivity extends BasePresenterActivity<GreenDaoPresenter, IBaseView> implements IBaseView {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private UserDao userDao;
    private CardDao cardDao;

    @Override
    protected int getLayoutId(){
        return R.layout.activity_green_dao;
    }

    @Override
    protected GreenDaoPresenter initPresenter(){
        return new GreenDaoPresenter();
    }

    @Override
    protected void initData(){
        //mDaoSession = DaoSessionManager.getInstace().getDaoSession(getApplicationContext());

        //User
        User user1 = new User();
        user1.setName("Mike");
        user1.setUsercode("001");
        user1.setUserAddress("China");
        User user2 = new User();
        user2.setName("John");
        user2.setUsercode("002");
        user2.setUserAddress("USA");
        User user3 = new User();
        user3.setName("James");
        user3.setUsercode("003");
        user3.setUserAddress("UK");

        //Card
        Card card1 = new Card();
        card1.setCardCode("19961010");
        Card card2 = new Card();
        card2.setCardCode("19970110");
        Card card3 = new Card();
        card3.setCardCode("19981201");

        //User--Card

    }
}
