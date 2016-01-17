package com.bob.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.bob.news.R;
import com.bob.news.utils.SPUtils;

public class WelcomeActivity extends Activity implements Animation.AnimationListener{

    public static final String IS_OPEN_MAIN_PAGE="IS_OPEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
    }

    public void initView(){
        View welcome=findViewById(R.id.welcome);
        RotateAnimation rotatewelcome=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        rotatewelcome.setDuration(1000);
        rotatewelcome.setFillAfter(true);//设置动画执行完毕，停留于该状态

        ScaleAnimation scalewelcome=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scalewelcome.setDuration(1000);
        scalewelcome.setFillAfter(true);

        AlphaAnimation alhpawelcome=new AlphaAnimation(0,1);
        alhpawelcome.setDuration(2000);
        alhpawelcome.setFillAfter(true);

        //集合动画
        AnimationSet animationSet=new AnimationSet(false);
        animationSet.addAnimation(rotatewelcome);
        animationSet.addAnimation(scalewelcome);
        animationSet.addAnimation(alhpawelcome);
        animationSet.setAnimationListener(this);

        welcome.startAnimation(animationSet);
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        //选择引导
        boolean isOpen= SPUtils.getBoolean(WelcomeActivity.this, IS_OPEN_MAIN_PAGE, false);
        Intent intent=new Intent();
        if (isOpen){
            //主界面
            intent.setClass(WelcomeActivity.this,MainActivity.class);
        }else {
            //引导界面
            intent.setClass(WelcomeActivity.this,GuideActivity.class);
        }
        startActivity(intent);
        //关闭当前
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }
}
