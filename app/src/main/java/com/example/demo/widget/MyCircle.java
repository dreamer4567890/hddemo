package com.example.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by lemon on 2018/3/28.
 * 自定义圆形
 */

public class MyCircle extends View {

    private Context context;
    /**
     * 屏幕的宽
     */
    private int width;
    /**
     * 屏幕的高
     */
    private int height;
    /**
     * 大圆的半径
     */
    private float bigR;
    /**
     * 小圆的半径
     */
    private float litterR;
    /**
     * 屏幕中间点的X坐标 - 小圆 大圆 圆心的x坐标
     */
    private float centerX,centerXlittle;
    /**
     * 屏幕中间点的Y坐标 - 小圆 大圆 圆心的y坐标
     */
    private float centerY,centerYlittle;

    /**
     *构造函数，由调用此类的的context传递width height
     */
    public MyCircle(Context context, int width, int height) {
        super(context);
        this.context = context ;
        this.width = width ;
        this.height = height ;
        setFocusable(true);
        //Toast.makeText(context,"width="+width+"<---->height="+height,Toast.LENGTH_SHORT).show();
        //设置两个圆的半径
        bigR = (width - 100)/4;
        litterR = bigR/2;

        //屏幕的中点-大圆的中心坐标
        centerX = width/2;
        centerY = height/2;
        //小圆的中心坐标
        centerXlittle = centerX + bigR ;
        centerYlittle = centerY + bigR - 120;
    }

    /**
     *绘制两个圆的情况
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //画背景颜色
        /*Paint bg = new Paint();
        bg.setColor(Color.WHITE);
        Rect bgR = new Rect(0,0,width,height);
        canvas.drawRect(bgR,bg);*/

        //float start = 0F;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 构建Paint时直接加上去锯齿属性
        //我们一共画两个圆
        for(int i=0 ; i<2 ; i++){
            /**
             * 第一种是用圆弧来画一个圆
             在android中Rect和RextF都是用来创建一个矩形的
             Rect的参数是int型 ，RectF的参数是float型.
             根据中心点 来确定上下左右 的矩形
             RectF bigOval = new RectF(centerX - bigR,centerY - bigR,centerX + bigR,
             centerY + bigR);
             // drawArc - 画圆弧
             // 第一个参数定义的圆弧的形状和大小的范围
             // 第二个参数的作用是设置圆弧是从哪个角度来顺时针绘画的
             // 第三个参数 这个参数的作用是设置圆弧扫过的角度
             // 第四个参数 这个参数的作用是设置我们的圆弧在绘画的时候，是否经过圆形
             // 第五个参数 这个参数的作用是设置我们的画笔对象的属性
             paint.setColor(Color.GREEN);
             canvas.drawArc(bigOval,start,360,true,paint);
             */


            /**
             * 第二种可以直接画出圆
             */
            paint.setColor(Color.GREEN);
            //（圆心x0,圆心y0,半径，paint）
            canvas.drawCircle(centerX,centerY,bigR,paint);


           /* RectF littleOval = new RectF(centerXlittle - litterR,centerYlittle -  litterR,
                    centerXlittle +  litterR, centerYlittle  +  litterR);
            paint.setColor(Color.YELLOW);
            canvas.drawArc(littleOval,start,360,true,paint);*/

            paint.setColor(Color.YELLOW);
            //（圆心x0,圆心y0,半径，paint）
            canvas.drawCircle(centerXlittle,centerYlittle,litterR,paint);
        }
        super.onDraw(canvas);
    }


    /**
     * View类的dispatchTouchEvent（）方法默认实现就是能帮你调用View自己的onTouchEvent方法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);//默认实现，可以不写
    }


    /**
     * 如果View想处理事件，则view设置为clickable 或 复写 onTouchEvent(),return true;
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取点击屏幕时的点的坐标
        float x = event.getX();
        float y = event.getY();
        whichCircle(x,y);
        return true;//返回true，代表事件已经消费，事件已经终止。
        // return false;  调用父类的onTouchEvent()
        // return super.onTouchEvent(event);
    }


    /**
     * 确定点击的点在哪个圆内
     */
    private void whichCircle(float x,float y){
        //将屏幕中的点转换成以屏幕中心为原点的坐标点
        float mx = x - centerX;
        float my = y - centerY;
        float result = mx*mx + my*my ;

        float lx = x - centerXlittle;
        float ly = y - centerYlittle;
        float resultlittle = lx*lx + ly *ly;


        if(resultlittle <= litterR*litterR){
            Toast.makeText(context,"点击了小圆的区域", Toast.LENGTH_SHORT).show();
        }else if(result <= bigR*bigR){
            Toast.makeText(context,"点击了大圆的区域",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"点击了圆以外的区域",Toast.LENGTH_SHORT).show();
        }

    }
}
