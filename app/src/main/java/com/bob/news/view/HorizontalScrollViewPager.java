package com.bob.news.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2016/1/18.
 */
public class HorizontalScrollViewPager extends ViewPager{

    private int downX;
    private int downY;

    public HorizontalScrollViewPager(Context context) {
        super(context);
    }

    public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //拦截事件
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                downX=(int)ev.getX();
                downY=(int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX=(int)ev.getX();
                int moveY=(int)ev.getY();

                int diffX=downX-moveX;
                int diffY=downY=moveY;
                if(Math.abs(diffX)>Math.abs(diffY)){
                    //横向滑动，不需要拦截
                    //当前页面是第一个，从左向右拦截事件，显示菜单
                    if (getCurrentItem()==0 && diffX<0){
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else if ((getCurrentItem()==(getAdapter().getCount())-1) &&diffX>0){
                        //最后一个页面，从右向左滑动，设置可拦截
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }else {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }else {
                    //竖向滑动，可以拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
