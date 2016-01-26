package com.bob.news.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.bob.news.R;
import com.bob.news.fragment.LeftMenuFragment;
import com.bob.news.fragment.MainContentFragment;
import com.bob.news.slidingmenu.SlidingMenu;

public class MainActivity extends FragmentActivity {

    private static final String LEFT_TAG="left_menu";
    private static final String MAIN_TAG="main";

    //滑动菜单
    public SlidingMenu mSlidingMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);//主
        initSlidingMenu();
        initFragment();
    }

    // 配置滑动菜单
    public void initSlidingMenu(){
        mSlidingMenu = new SlidingMenu(this);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        mSlidingMenu.setMenu(R.layout.left_menu);
        mSlidingMenu.setBehindOffset(200);
    }


    private void initFragment(){
        //管理器
        FragmentManager fragmentManager=getSupportFragmentManager();
        //开启事务
        FragmentTransaction ft=fragmentManager.beginTransaction();
        //替换
        ft.replace(R.id.left_menu, new LeftMenuFragment(), LEFT_TAG);
        ft.replace(R.id.fl_main_container, new MainContentFragment(), MAIN_TAG);
        //提取
        ft.commit();
    }

    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        LeftMenuFragment leftMenuFragment=(LeftMenuFragment)fragmentManager.findFragmentByTag(LEFT_TAG);
        return leftMenuFragment;
    }

    public MainContentFragment getMainContentFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        MainContentFragment mainContentFragment=(MainContentFragment)fragmentManager.findFragmentByTag(MAIN_TAG);
        return mainContentFragment;
    }

}
