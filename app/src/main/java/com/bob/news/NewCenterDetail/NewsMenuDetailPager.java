package com.bob.news.NewCenterDetail;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bob.news.R;
import com.bob.news.TabPageIndicator.TabPageIndicator;
import com.bob.news.activity.MainActivity;
import com.bob.news.base.MenuDetailBasePager;
import com.bob.news.domain.NewsCenterBean.NewsCenterData;
import com.bob.news.domain.NewsCenterBean.ChildRen;
import com.bob.news.slidingmenu.CustomViewAbove;
import com.bob.news.slidingmenu.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
//新闻菜单对于的界面
public class NewsMenuDetailPager  extends MenuDetailBasePager implements
        View.OnClickListener,ViewPager.OnPageChangeListener{

    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private ImageButton next;
    private List<ChildRen> childRenList;
    private NewsMenuAdapter newsMenuAdapter;
    private List<NewsMenuTabDetailPager> tabPagerList;

    public NewsMenuDetailPager(Context context) {
        super(context);
    }
    public NewsMenuDetailPager(Context context,NewsCenterData newsCenterData) {
        super(context);
        childRenList=newsCenterData.children;
    }


    @Override
    public View initView() {
        View view=View.inflate(mContext, R.layout.news_menu,null);
        mIndicator=(TabPageIndicator)view.findViewById(R.id.tpi_news_menu);
        mViewPager=(ViewPager)view.findViewById(R.id.vp_news_content);
        next=(ImageButton)view.findViewById(R.id.next);
        next.setOnClickListener(this);
        return view;
    }

    @Override
    public void initData() {
        //准备详情界面
        tabPagerList=new ArrayList<>();
        for(int i=0;i<childRenList.size();i++){
            tabPagerList.add(new NewsMenuTabDetailPager(mContext,childRenList.get(i)));
           // System.out.println(childRenList.size());
        }



        newsMenuAdapter=new NewsMenuAdapter();
        mViewPager.setAdapter(newsMenuAdapter);
        //将ViewPager对象设置给TabPageIndicator,关联完后页签的数据都来自适配器
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(this);
    }


    class NewsMenuAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return childRenList.size();
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
            NewsMenuTabDetailPager pager=tabPagerList.get(position);
            View rootView=pager.getRootView();
            container.addView(rootView);
            pager.initData();
            return rootView;
        }

        //返回的字符串会作为也签数据展示
        @Override
        public CharSequence getPageTitle(int position) {
            return childRenList.get(position).title;
        }
    }

    @Override
    public void onClick(View v) {
        //下一个页签
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
    }


    //页改变监听
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        isEnableSlidingMenu(position == 0);
    }

    //是否启用滑动菜单
    private void isEnableSlidingMenu(boolean isEnable) {
        if(isEnable) {
            ((MainActivity) mContext).mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            ((MainActivity) mContext).mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
}
