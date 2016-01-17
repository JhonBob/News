package com.bob.news.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bob.news.R;
import com.bob.news.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener,View.OnClickListener{

    private Button btn_start_guide;
    private LinearLayout ll_guide_point_group;
    private  List<ImageView> imageViewList;
    private  View normal;
    private LinearLayout.LayoutParams params;
    private View select;
    private int piontwidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//在setContentView 之前调用
        setContentView(R.layout.activity_guide);
        initView();
    }

    //初始化界面
    public void initView(){
        ViewPager viewPager=(ViewPager)findViewById(R.id.vp_guide);
        btn_start_guide=(Button)findViewById(R.id.btn_start_guide);
        ll_guide_point_group=(LinearLayout)findViewById(R.id.ll_guide_point_group);
        select=findViewById(R.id.select);
        btn_start_guide.setOnClickListener(this);

        initData();
        GuideAdapter mAdapter=new GuideAdapter();
        viewPager.setAdapter(mAdapter);
        viewPager.setOnPageChangeListener(this);

        //监听，select控件的布局
        //获得视图树的观察者，监听全局布局回掉
        select.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //只执行一次，把当前事件从视图观察者移除
                select.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                //取出两点间的宽度
                piontwidth=ll_guide_point_group.getChildAt(1).getLeft()-ll_guide_point_group.getChildAt(0).getLeft();
            }
        });
    }

    //向集合添加引导图片
    public void initData(){
        int[] imageResID={
                R.mipmap.guide_1,
                R.mipmap.guide_2,
                R.mipmap.guide_3
        };
        imageViewList=new ArrayList<>();
        ImageView iv;
        for(int i=0;i<imageResID.length;i++){
            iv=new ImageView(this);
            iv.setBackgroundResource(imageResID[i]);
            imageViewList.add(iv);

            //循环加点
            normal=new View(this);
            normal.setBackgroundResource(R.drawable.point_normal);
            params=new LinearLayout.LayoutParams(15,15);
            if (i!=0){
                params.leftMargin=10;
            }
            normal.setLayoutParams(params);
            ll_guide_point_group.addView(normal);
        }
    }



    //引导适配
    public class GuideAdapter  extends PagerAdapter{
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
           container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //1.向ViewPage中添加一个对象
            ImageView imageView=imageViewList.get(position);
            container.addView(imageView);
            //2.返回当前添加的对象
            return imageView;
        }
    }



    //ViewPage滑动监听
    //position 当前选中的页面
    //positionOffset 比例
    //positionOffsetPixels 偏移像素
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //计算红点相对左边界每次的相对位置
        int leftMargin=(int)(piontwidth*(positionOffset+position));
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)select.getLayoutParams();
        //重定位红点的当前位置
        params.leftMargin=leftMargin;
        select.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {
        //处理最后一个页面的按钮
        if(position==imageViewList.size()-1){
            btn_start_guide.setVisibility(View.VISIBLE);
        }else {
            btn_start_guide.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onClick(View v) {
        //重设引导变量
        SPUtils.putBoolean(GuideActivity.this,WelcomeActivity.IS_OPEN_MAIN_PAGE,true);
        //打开主界面
        Intent intent=new Intent(GuideActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
