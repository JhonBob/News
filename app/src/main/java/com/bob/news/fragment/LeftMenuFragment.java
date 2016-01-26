package com.bob.news.fragment;

import com.bob.news.R;
import com.bob.news.activity.MainActivity;
import com.bob.news.base.NewsCenterPager;
import com.bob.news.domain.NewsCenterBean.NewsCenterData;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bob.news.base.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/1/16.
 */


public class LeftMenuFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    private List<NewsCenterData> mMenuListData;
    private ListView mListView;
    private MenuAdapter menuAdapter;
    private int selectPosition;


    @Override
    public View initView(LayoutInflater inflater) {
        mListView=new ListView(mActivity);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setDividerHeight(0);
        mListView.setBackgroundColor(Color.BLACK);
        mListView.setPadding(0, 50, 0, 0);
        mListView.setSelector(android.R.color.transparent);
        mListView.setOnItemClickListener(this);
        return mListView;
    }

    public void setMenuListData(List<NewsCenterData> menulistdata ){
        //接收传过来的数据
        this.mMenuListData=menulistdata;
        //System.out.println(mMenuListData.get(0).children.get(0).title);
        selectPosition=0;
        //当传递到时适配
        menuAdapter=new MenuAdapter();
        mListView.setAdapter(menuAdapter);
        switchNewsCenterContentPager();
    }

    private void switchNewsCenterContentPager() {
        //默认选中0号菜单，切换界面
        MainActivity main=(MainActivity)mActivity;
        MainContentFragment mainft=main.getMainContentFragment();
        NewsCenterPager newsCenterPager=mainft.getNewsCenterPager();
        newsCenterPager.switchCurrentPager(selectPosition);
    }

    class MenuAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mMenuListData.size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TextView view;
            if (convertView==null){
                view=(TextView)View.inflate(mActivity, R.layout.menu_item,null);
            }else {
                view=(TextView)convertView;
            }
            view.setText(mMenuListData.get(position).title);
            //选中
            view.setEnabled(position==selectPosition);
            return view;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectPosition=position;
        menuAdapter.notifyDataSetChanged();
        //收回菜单
        ((MainActivity) mActivity).mSlidingMenu.toggle();
        //主界面中心对应显示页面
        switchNewsCenterContentPager();
    }
}
