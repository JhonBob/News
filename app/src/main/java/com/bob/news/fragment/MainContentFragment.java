package com.bob.news.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ViewUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;


import com.bob.news.R;
import com.bob.news.activity.MainActivity;
import com.bob.news.base.BaseFragment;
import com.bob.news.base.GovaffirsPager;
import com.bob.news.base.HomePager;
import com.bob.news.base.NnewsCenterPager;
import com.bob.news.base.SettingPager;
import com.bob.news.base.SmartServicePager;
import com.bob.news.base.TabBasePage;
import com.bob.news.slidingmenu.SlidingMenu;
import com.bob.news.view.NoScrollViewPage;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/16.
 */


public class MainContentFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    @ViewInject(R.id.vp_cf_page)
    private NoScrollViewPage mViewPager;
    @ViewInject(R.id.rg_cf_group)
    private RadioGroup mRadioGroup;

    private  List<TabBasePage> pageList;
    private ContentAdapter mAdapter;


    @Override
    public View initView(LayoutInflater inflater) {
        View view=inflater.inflate(R.layout.content_fragment,null);
        //当前view对象注入到xUtils框架中
        com.lidroid.xutils.ViewUtils.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        pageList=new ArrayList<TabBasePage>();
        pageList.add(new HomePager(mActivity));
        pageList.add(new NnewsCenterPager(mActivity));
        pageList.add(new SmartServicePager(mActivity));
        pageList.add(new GovaffirsPager(mActivity));
        pageList.add(new SettingPager(mActivity));
        mAdapter=new ContentAdapter();
        mViewPager.setAdapter(mAdapter);

        //一开始选中第零个，加载第零个的数据
        mRadioGroup.check(R.id.rb_cf_home);
        pageList.get(0).initData();
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    class ContentAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return pageList.size();
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
            TabBasePage pager=pageList.get(position);
            //初始化布局
            View rootView=pager.getRootView();
            container.addView(rootView);
            //取消预加载
           // pager.initData();
            return rootView;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        int index=-1;
        switch (checkedId){
            case R.id.rb_cf_home:
                index=0;
                break;
            case R.id.rb_cf_new:
                index=1;
                break;
            case R.id.rb_cf_sm:
                index=2;
                break;
            case R.id.rb_cf_ga:
                index=3;
                break;
            case R.id.rb_cf_set:
                index=4;
                break;
            default:
                break;
        }

        if(index!=-1){
            mViewPager.setCurrentItem(index);
            //初始化数据
            pageList.get(index).initData();
            //处理滑动菜单
            if (index==0 || index==4){
                //0,1位置不可用
                ((MainActivity)mActivity).mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }else {
                ((MainActivity)mActivity).mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
            }
        }
    }
}
