package com.example.demo.presenter;

import android.util.Log;

import com.example.demo.bean.greendao.Card;
import com.example.demo.bean.greendao.Course;
import com.example.demo.bean.greendao.Orders;
import com.example.demo.bean.greendao.Teacher;
import com.example.demo.bean.greendao.User;
import com.example.demo.greendao.CardDao;
import com.example.demo.greendao.CourseDao;
import com.example.demo.greendao.OrdersDao;
import com.example.demo.greendao.TeacherDao;
import com.example.demo.greendao.UserDao;
import com.example.demo.mvp.BasePresenter;
import com.example.demo.mvp.IBaseView;

import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreenDaoPresenter extends BasePresenter<IBaseView> {

    //根据CardID查询User 双向关联
    public void queryUser(CardDao cardDao){
        Card card=cardDao.queryBuilder().where(CardDao.Properties.CardCode.eq("19961010")).build().unique();
        User user=card.getUser();
        if(user!=null && card!=null){
            Log.d("GreenDaoActivity", "姓名："+user.getName()+"\n" +"Card表的id主键值："+card.getId()+"\n" +"User表的外键cardId的值为："+user.getCardId());
        }
    }

    //根据User查询Orders 一对多
    public void queryOrders(UserDao userDao){
        List<Orders> ordersList;
        User user = userDao.queryBuilder().where(UserDao.Properties.Name.eq("Mike")).build().unique();

        ordersList = user.getOrders();
        Log.d("GreenDaoActivity", user.getName() + "的订单内容为：");

        int i = 0;
        if (ordersList != null) {
            for (Orders order : ordersList) {
                i = i + 1;
                Log.d("GreenDaoActivity", "第" + i + "条订单的结果：" + ",id:" + order.getId() + ",商品名：" + order.getGoodsName() + ",用户名：" + user.getName());
            }
        }
    }

    // 多对多查询 通过老师查询课程
    public void queryManyToManyT(TeacherDao teacherDao) {
        Teacher teacher = teacherDao.queryBuilder().where(TeacherDao.Properties.Name.eq("张老师")).build().unique();
        List<Course> courses = teacher.getCourses();

        if (courses != null) {
            Log.d("GreenDaoActivity", teacher.getName() + "所教的课程：");
            for (Course course : courses) {
                Log.d("GreenDaoActivity", "课程名：" + course.getName());
            }
        }
    }

    // 多对多查询 通过课程找到老师
    public void queryManyToManyC(CourseDao courseDao) {
        Course course = courseDao.queryBuilder().where(CourseDao.Properties.Name.eq("数学")).build().unique();
        List<Teacher> teachers = course.getTeachers();

        if (teachers != null) {
            Log.d("GreenDaoActivity", "教" + course.getName() + "的老师有：");
            for (Teacher teacher : teachers) {
                Log.d("GreenDaoActivity", "教师名：" + teacher.getName());
            }
        }
    }

    //多表查询 两表查询 购买网球的用户
    public void multiQueryTwoTb(UserDao userDao) {
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.join(Orders.class, OrdersDao.Properties.UserId).where(OrdersDao.Properties.GoodsName.eq("网球"));
        List<User> users = qb.list();

        if (users != null) {
            for (User u : users) {
                Log.d("GreenDaoActivity", "购买网球的用户有：" + u.getName());
            }
        }
    }

    //多表查询 三表查询 购买网球的前四位身份证号是1998的用户
    public void queryThreeTb(CardDao cardDao) {
        QueryBuilder<Card> qb = cardDao.queryBuilder().where(CardDao.Properties.CardCode.like("1998%"));
        Join user = qb.join(CardDao.Properties.UserId, User.class);
        Join order = qb.join(user, UserDao.Properties.Id, Orders.class, OrdersDao.Properties.UserId);
        order.where(OrdersDao.Properties.GoodsName.eq("网球"));
        List<Card> cardList = qb.list();

        if (cardList != null) {
            Log.d("GreenDaoActivity", "买了网球的身份证前四位是1998的用户：");
            for (Card card : cardList) {
                Log.d("GreenDaoActivity", "身份证：" + card.getCardCode() + " 名字：" + card.getUser().getName());
            }
        }
    }

    //插入user4
    public void insertUser(UserDao userDao){
        User user = new User(4L,4L,"004","cgh","China");
        userDao.insertOrReplace(user);
        searchAll(userDao);
    }

    //更新user4
    public void updateUser(UserDao userDao){
        User user = userDao.load(4L);
        user.setUserAddress("Poland");
        userDao.update(user);
        searchAll(userDao);
    }

    //删除user4
    public void deleteUser(UserDao userDao){
        userDao.queryBuilder().where(UserDao.Properties.Name.eq("cgh")).buildDelete().executeDeleteWithoutDetachingEntities();
        searchAll(userDao);
    }

    //查询userDao所有数据
    public void searchAll(UserDao userDao){
        List<User> userInfors = userDao.queryBuilder().list();
        for(User user:userInfors){
            Log.d("GreenDaoActivity", "姓名："+user.getName()+"\n" +"编号："+ user.getUsercode()+"\n" +"地址："+user.getUserAddress());
        }
    }
}
