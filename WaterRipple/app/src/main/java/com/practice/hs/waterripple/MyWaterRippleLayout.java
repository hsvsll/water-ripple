package com.practice.hs.waterripple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by huha on 2017/5/11.
 * 水波纹实现父布局
 */

public class MyWaterRippleLayout extends LinearLayout implements Runnable{
    private static final long INVALIDATE_DURATION = 40;
    private float mCenterX;
    private float mCenterY;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mRevealRadius = 0;
    private float mMaxRadius;
    private float mMinRadius;
    private View mTouchTarget;
    private float mWidth;
    private float mHeight;
    private float mRevealRadiusGap;

    private int[] mLocationInScreen = new int[2];
    private boolean mIsPressed = false;
    private boolean mShouldDoAnimation = true;
    private DispatchUpTouchEventRunnable  mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

    public MyWaterRippleLayout(Context context) {
        super(context);
        init();
    }

    public MyWaterRippleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWaterRippleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int action = ev.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                View touchView = getTouchView(this, x, y);
                if(touchView != null && touchView.isClickable() && touchView.isEnabled()){
                    mTouchTarget = touchView;
                    initData(ev,mTouchTarget);
//                    initParametersForChild(event, touchTarget);
                    postInvalidateDelayed(INVALIDATE_DURATION);
                }

                break;
            case MotionEvent.ACTION_UP:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                mDispatchUpTouchEventRunnable.event = ev;
                postDelayed(mDispatchUpTouchEventRunnable, 400);
                return true;
            case MotionEvent.ACTION_CANCEL:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getLocationOnScreen(mLocationInScreen);
    }

    private void initData(MotionEvent event, View mTouchTarget) {
        mCenterX = event.getX();
        mCenterY = event.getY();
        mWidth = mTouchTarget.getMeasuredWidth();
        mHeight = mTouchTarget.getMeasuredHeight();
//        mMaxRadius = Math.max(mWidth,mHeight);//暂时取长边
        mMinRadius = Math.min(mWidth,mHeight);//暂时取短边
        mRevealRadius = 0;
        mRevealRadiusGap = mMinRadius / 8;
        mShouldDoAnimation = true;
        mIsPressed = true;

        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        int left = location[0] - mLocationInScreen[0];
        int top = location[1] - mLocationInScreen[1];
        int transformedCenterX = (int)mCenterX - left;
        int transformedCenterY = (int)mCenterY - top;
//        int transformedCenterMax = Math.max(transformedCenterX,transformedCenterY);
        mMaxRadius = (float) Math.sqrt(Math.pow(mWidth,2) + Math.pow(mHeight,2));
//        mMaxRadius = Math.max(transformedCenterX, mWidth - transformedCenterX);
    }

    private void init() {
        setWillNotDraw(false);
        mPaint.setColor(ContextCompat.getColor(getContext(),R.color.reveal_color));
    }


    /**绘制子布局时调用*/
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (!mShouldDoAnimation || mWidth <= 0 || mTouchTarget == null) {
            return;
        }

        if (mRevealRadius > mMinRadius / 2) {
            mRevealRadius += mRevealRadiusGap * 4;
        } else {
            mRevealRadius += mRevealRadiusGap;
        }

        this.getLocationOnScreen(mLocationInScreen);
        int[] location = new int[2];
        mTouchTarget.getLocationOnScreen(location);
        int left = location[0] - mLocationInScreen[0];
        int top = location[1] - mLocationInScreen[1];
        int right = left + mTouchTarget.getMeasuredWidth();
        int bottom = top + mTouchTarget.getMeasuredHeight();

        canvas.save();
        canvas.clipRect(left,top,right,bottom);
        canvas.drawCircle(mCenterX,mCenterY,mRevealRadius,mPaint);
        canvas.restore();

        if (mRevealRadius <= mMaxRadius) {
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        } else if (!mIsPressed) {
            mShouldDoAnimation = false;
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        }
    }

    @Override
    public void run() {
        super.performClick();
    }

    @Override
    public boolean performClick() {
        postDelayed(this, 400);
        return true;
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;

        @Override
        public void run() {
            if (mTouchTarget == null || !mTouchTarget.isEnabled()) {
                return;
            }

            if (isTouchPointInView(mTouchTarget, (int)event.getRawX(), (int)event.getRawY())) {
                mTouchTarget.performClick();
            }
        }
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
