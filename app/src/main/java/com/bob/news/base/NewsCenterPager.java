package com.bob.news.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.bob.news.NewCenterDetail.InteractMenuDetailPager;
import com.bob.news.NewCenterDetail.NewsMenuDetailPager;
import com.bob.news.NewCenterDetail.PhotosMenuDetailPager;
import com.bob.news.NewCenterDetail.TopicMenuDetailPager;
import com.bob.news.activity.MainActivity;
import com.bob.news.domain.NewsCenterBean;
import com.bob.news.fragment.LeftMenuFragment;
import com.bob.news.utils.Constancts;
import com.bob.news.utils.SPUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.bob.news.domain.NewsCenterBean.NewsCenterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/16.
 */
public class NewsCenterPager extends TabBasePage {

    private List<MenuDetailBasePager> pagers;
    private List<NewsCenterData> leftMenuDataList;

    public NewsCenterPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        tvTitle.setText("新闻中心");
        ibMenu.setVisibility(View.VISIBLE);
        //取出本地缓存数据
        String json=SPUtils.getString(mContext, Constancts.newscenter_url, null);
        if (!TextUtils.isEmpty(json)){
            processData(json);//先显示旧的，下面继续请求
        }

            //服务器抓取数据
            getDataFromNet();

    }


    //服务器抓取数据
    private void getDataFromNet() {
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constancts.newscenter_url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("新闻中心数据请求成功：" + responseInfo.result);
                //存储数据
                SPUtils.putString(mContext,Constancts.newscenter_url,responseInfo.result);
                //解析数据
                processData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("新闻中心数据请求失败：" + s);
            }
        });
    }


    //解析数据
    public void processData(String result){
        Gson gson=new Gson();
        NewsCenterBean bean=gson.fromJson(result, NewsCenterBean.class);

        //初始化菜单对应的页面,必须放到初始化左侧菜单数据之前
        pagers=new ArrayList<>();
        pagers.add(new NewsMenuDetailPager(mContext,bean.data.get(0)));
        pagers.add(new TopicMenuDetailPager(mContext));
        pagers.add(new PhotosMenuDetailPager(mContext,bean.data.get(2)));
        pagers.add(new InteractMenuDetailPager(mContext));

        //初始化左侧菜单数据
        leftMenuDataList=bean.data;
        //把菜单数据传递到左侧Fragment
        LeftMenuFragment leftMenuFragment=((MainActivity) mContext).getLeftMenuFragment();
        leftMenuFragment.setMenuListData(leftMenuDataList);


    }

    //位置切换面
    public void switchCurrentPager(int position){
        final MenuDetailBasePager pager=pagers.get(position);
        //添加到真布局中
        View view=pager.getRootView();
        flContent.removeAllViews();
        flContent.addView(view);
        //改标题
        tvTitle.setText(leftMenuDataList.get(position).title);
       // 初始化数据
        pager.initData();

        if (position==2){
            mListOrGrid.setVisibility(View.VISIBLE);
            mListOrGrid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotosMenuDetailPager photosMenuDetailPager = (PhotosMenuDetailPager)pager;
                    photosMenuDetailPager.switchCurrentPager(mListOrGrid);
                }
            });
        }else {
            mListOrGrid.setVisibility(View.GONE);
        }
    }
}
