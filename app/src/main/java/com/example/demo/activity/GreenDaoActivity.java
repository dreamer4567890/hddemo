package com.example.demo.activity;

import android.support.v7.app.AppCompatActivity;

import com.example.demo.R;
import com.example.demo.bean.greendao.Card;
import com.example.demo.bean.greendao.Course;
import com.example.demo.bean.greendao.JoinTeacherWithCourse;
import com.example.demo.bean.greendao.Orders;
import com.example.demo.bean.greendao.Teacher;
import com.example.demo.bean.greendao.User;
import com.example.demo.greendao.CardDao;
import com.example.demo.greendao.CourseDao;
import com.example.demo.greendao.DaoMaster;
import com.example.demo.greendao.DaoSession;
import com.example.demo.greendao.JoinTeacherWithCourseDao;
import com.example.demo.greendao.OrdersDao;
import com.example.demo.greendao.TeacherDao;
import com.example.demo.greendao.UserDao;
import com.example.demo.mvp.IBaseView;
import com.example.demo.presenter.GreenDaoPresenter;
import com.example.demo.sql.DaoManager;

import java.util.ArrayList;
import java.util.List;

public class GreenDaoActivity extends BasePresenterActivity<GreenDaoPresenter, IBaseView> implements IBaseView {

    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private UserDao userDao;
    private CardDao cardDao;
    private OrdersDao ordersDao;
    private TeacherDao teacherDao;
    private CourseDao courseDao;
    private JoinTeacherWithCourseDao joinTeacherWithCourseDao;

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
        setMyActionBar("GreenDAO",false);
        mDaoSession = DaoManager.getInstance().getDaoSession(getApplicationContext());
        userDao = mDaoSession.getUserDao();
        cardDao = mDaoSession.getCardDao();
        ordersDao = mDaoSession.getOrdersDao();
        teacherDao = mDaoSession.getTeacherDao();
        courseDao = mDaoSession.getCourseDao();
        joinTeacherWithCourseDao = mDaoSession.getJoinTeacherWithCourseDao();

        //User
        User user1 = new User(1L,1L,"001","Mike","China");
        User user2 = new User(2L,2L,"002","John","USA");
        User user3 = new User(3L,3L,"003","James","UK");

        //Card
        Card card1 = new Card(1L,"19961010",1L);
        Card card2 = new Card(2L,"19970110",2L);
        Card card3 = new Card(3L,"19981201",3L);

        //User--Card
        userDao.insertOrReplace(user1);
        userDao.insertOrReplace(user2);
        userDao.insertOrReplace(user3);
        //card1.setUser(user1);
        //card2.setUser(user2);
        //card3.setUser(user3);
        cardDao.insertOrReplace(card1);
        cardDao.insertOrReplace(card2);
        cardDao.insertOrReplace(card3);
        //user1.setCard(card1);
        //user2.setCard(card2);
        //user3.setCard(card3);
        userDao.update(user1);
        userDao.update(user2);
        userDao.update(user3);

        //User--Orders
        List<Orders> orderList=new ArrayList<Orders>();

        Orders order1=new Orders(1L,"篮球",1L);
        //order1.setUser(user1);

        Orders order2=new Orders(2L,"足球",2L);
        //order2.setUser(user2);

        Orders order3=new Orders(3L,"网球",3L);
        //order3.setUser(user3);

        Orders order4=new Orders(4L,"网球",1L);
        //order4.setUser(user1);

        orderList.add(order1);
        orderList.add(order2);
        orderList.add(order3);
        orderList.add(order4);

        ordersDao.insertOrReplaceInTx(orderList);

        //Teacher--Course
        List<Course> courses = new ArrayList<>();
        Course course1 = new Course(1L,"语文");
        //course1.setName("语文");

        Course course2 = new Course(2L,"数学");
        //course2.setName("数学");

        Course course3 = new Course(3L,"英语");
        //course3.setName("英语");

        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courseDao.insertOrReplaceInTx(courses);

        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher1 = new Teacher(1L,"张老师");
        //teacher1.setName("张老师");

        Teacher teacher2 = new Teacher(2L,"陈老师");
        //teacher2.setName("陈老师");

        Teacher teacher3 = new Teacher(3L,"李老师");
        //teacher3.setName("李老师");

        teacherList.add(teacher1);
        teacherList.add(teacher2);
        teacherList.add(teacher3);
        teacherDao.insertOrReplaceInTx(teacherList);

        List<JoinTeacherWithCourse> teacherWithCourses = new ArrayList<>();
        JoinTeacherWithCourse teacherWithCourse1 = new JoinTeacherWithCourse(1L,1L,1L);
        //teacherWithCourse1.setTId(teacher1.getId());
        //teacherWithCourse1.setCId(course1.getId());

        JoinTeacherWithCourse teacherWithCourse2 = new JoinTeacherWithCourse(2L,1L,2L);
        //teacherWithCourse2.setTId(teacher1.getId());
        //teacherWithCourse2.setCId(course2.getId());

        JoinTeacherWithCourse teacherWithCourse3 = new JoinTeacherWithCourse(3L,2L,2L);
        //teacherWithCourse3.setTId(teacher2.getId());
        //teacherWithCourse3.setCId(course2.getId());

        JoinTeacherWithCourse teacherWithCourse4 = new JoinTeacherWithCourse(4L,3L,3L);
        //teacherWithCourse4.setTId(teacher3.getId());
        //teacherWithCourse4.setCId(course3.getId());

        teacherWithCourses.add(teacherWithCourse1);
        teacherWithCourses.add(teacherWithCourse2);
        teacherWithCourses.add(teacherWithCourse3);
        teacherWithCourses.add(teacherWithCourse4);
        joinTeacherWithCourseDao.insertOrReplaceInTx(teacherWithCourses);

        initView();
    }

    private void initView(){
        mPresenter.queryUser(cardDao);
        mPresenter.queryOrders(userDao);
        mPresenter.queryManyToManyT(teacherDao);
        mPresenter.queryManyToManyC(courseDao);
        mPresenter.multiQueryTwoTb(userDao);
        mPresenter.queryThreeTb(cardDao);
        mPresenter.insertUser(userDao);
        mPresenter.updateUser(userDao);
        mPresenter.deleteUser(userDao);
    }
}
