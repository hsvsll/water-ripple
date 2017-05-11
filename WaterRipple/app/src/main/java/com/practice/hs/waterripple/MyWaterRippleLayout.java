package com.practice.hs.waterripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by huha on 2017/5/11.
 * 水波纹实现父布局
 */

public class MyWaterRippleLayout extends LinearLayout{
    private float mCenterX;
    private float mCenterY;
    private Paint mPaint;
    private float mRevealRadius;
    private View mTouchTarget;

    private boolean mIsPressed = false;

    public MyWaterRippleLayout(Context context) {
        super(context);
    }

    public MyWaterRippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWaterRippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                View touchView = getTouchView(this, x, y);
                if(touchView.isClickable() && touchView.isEnabled()){
                    mTouchTarget = touchView;
                    //TODO 数据初始化
                    postInvalidateDelayed(1000);
                }

                break;
            case MotionEvent.ACTION_UP:
                //TODO 消耗事件延迟绘制
                break;
            case MotionEvent.ACTION_CANCEL:
//TODO 取消延迟绘制
                break;
        }

        return super.dispatchTouchEvent(ev);
    }


    /**绘制子布局时调用*/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
//TODO  逻辑判断是否需要绘制和半径计算
        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + mTouchTarget.getMeasuredWidth();
        int bottom = top + mTouchTarget.getMeasuredHeight();


        canvas.save();
        canvas.clipRect(left,top,right,bottom);
        canvas.drawCircle(mCenterX,mCenterY,mRevealRadius,mPaint);
        canvas.restore();
//TODO 延迟绘制
    }

    public View getTouchView(View view, int x, int y) {
        View target = null;
        ArrayList<View> arrayList = view.getTouchables();
        for(View child : arrayList){
            if(isTouchPointInView(child,x,y)){
                target = child;
                break;
            }
        }
        return target;
    }

    private boolean isTouchPointInView(View child, int x, int y) {
        int [] location = new int[2];
        child.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + child.getMeasuredWidth();
        int bottom = top + child.getMeasuredHeight();
        if(child.isClickable() && x >= left && x <= right && y >= top
                && y <= bottom){
            return true;
        }
        return false;
    }
}
