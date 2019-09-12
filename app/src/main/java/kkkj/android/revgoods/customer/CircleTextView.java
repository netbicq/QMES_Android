package kkkj.android.revgoods.customer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import kkkj.android.revgoods.R;

/**
 * Name: KotlinDemo
 * Package Name：com.quickcq.kotlindemo
 * Author: Admin
 * Time: 2019/9/11 17:09
 * Describe: 自定义View初探
 */
public class CircleTextView extends View {

    private Paint paint;
    private int frontSize;//字体大小
    //背景色
    private int colorBackGround;

    private onClickListener onClickListener;

    public interface onClickListener{
        void onClick();
    }

    public void setClickListener(onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public CircleTextView(Context context) {
        super(context);
    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
        paint = new Paint();
        //给画笔设置颜色
        paint.setAntiAlias(true);//抗锯齿
        colorBackGround = Color.RED;

    }

    public CircleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CircleTextView,defStyleAttr,0);
        frontSize = typedArray.getDimensionPixelSize(R.styleable.CircleTextView_textSize,42);
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        paint.setColor(Color.WHITE);
//        设置画笔属性
        paint.setStyle(Paint.Style.STROKE);//画笔属性是空心圆
        paint.setStrokeWidth(2);//设置画笔粗细
        /*四个参数：
                参数一：圆心的x坐标
                参数二：圆心的y坐标
                参数三：圆的半径
                参数四：定义好的画笔
                */
        float strokeWeight = paint.getStrokeWidth();
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f - strokeWeight / 2, paint);

        paint.setStyle(Paint.Style.FILL);//画笔属性是实心圆
        paint.setColor(colorBackGround);
        paint.setStrokeWidth(0);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2,getWidth() / 2f - strokeWeight,paint);


        paint.setTextSize(frontSize);
        float textHeight  =  paint.descent() - paint.ascent();//字体高度

        paint.setColor(Color.WHITE);
        String text="手工";
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, getWidth() / 2,getHeight()/2 - paint.descent(), paint);
        text = "计重";
        canvas.drawText(text, getWidth() / 2,getHeight() / 2 + textHeight - paint.descent(), paint);

        Log.d("发现新版本V2.0.0","11111");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //按下状态切换背景颜色
                colorBackGround = getResources().getColor(R.color.deep_red);
                //重绘
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                colorBackGround = Color.RED;
                if (onClickListener != null) {
                    onClickListener.onClick();
                }
                invalidate();
                return true;
        }


        return super.onTouchEvent(event);
    }

}
