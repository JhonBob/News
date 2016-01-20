package com.bob.news.NewCenterDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bob.news.R;
import com.bob.news.base.MenuDetailBasePager;
import com.bob.news.domain.NewsCenterBean;
import com.bob.news.domain.TabDetailBean;
import com.bob.news.utils.Constancts;
import com.bob.news.utils.SPUtils;
import com.bob.news.view.HorizontalScrollViewPager;
import com.bob.news.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.bob.news.domain.NewsCenterBean.ChildRen;
import com.bob.news.domain.TabDetailBean.TopNews;
import com.bob.news.domain.TabDetailBean.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class NewsMenuTabDetailPager extends MenuDetailBasePager implements ViewPager.OnPageChangeListener{
    @ViewInject(R.id.vp_news_menu_tab_detail_top_news)
    private HorizontalScrollViewPager mViewPager;
    @ViewInject(R.id.tv_news_menu_tab_detail_dec)
    private TextView tvDescription;
    @ViewInject(R.id.ll_news_menu_tab_detail_points)
    private LinearLayout llPoints;
    @ViewInject(R.id.lv_news)
    private RefreshListView lvnews;

    //页签详情数据
    private ChildRen mChildRen;
    private String url;
    private List<TopNews> topNewses;
    private TopNewAdapter topNewAdapter;
    private BitmapUtils bitmapUtils;

    //默认前一点索引
    private int previousEnabledPosition;
    private InternalHandler mHandler;

    private List<News> newsList;
    private NewsAdapter newsAdapter;

    public NewsMenuTabDetailPager(Context context) {
        super(context);
    }
    public NewsMenuTabDetailPager(Context context,ChildRen childRen) {
        super(context);
        this.mChildRen=childRen;

        topNewses=new ArrayList<>();
        newsList=new ArrayList<>();
        bitmapUtils=new BitmapUtils(context);
        bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.ARGB_4444);
    }


    @Override
    public View initView() {
        View view=View.inflate(mContext, R.layout.news_menu_tab_detail, null);
        ViewUtils.inject(this,view);

        //头布局,使得成为ListView的一部分，能够整体拖动
        View topview=View.inflate(mContext, R.layout.news_menu_tab_detail_topnews, null);
        ViewUtils.inject(this, topview);
       // lvnews.addHeaderView(topview);
        return view;
    }

    @Override
    public void initData() {
        //请求数据
        url=Constancts.serveiceurl+mChildRen.url;
        //缓存数据
        String json=SPUtils.getString(mContext, url, null);
        if (!TextUtils.isEmpty(json)){
            processData(json);
        }
        getDataFromNet();
    }

    private void getDataFromNet(){
        HttpUtils httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println(mChildRen.title + "MenuTab请求数据成功：");
                //存储数据
                SPUtils.putString(mContext, url, responseInfo.result);
                //解析数据
                processData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println(mChildRen.title + "请求数据失败：" + s);

            }
        });
    }

    //处理数据
    private void processData(String result){
        Gson gson=new Gson();
        TabDetailBean bean=gson.fromJson(result, TabDetailBean.class);
        //System.out.println(bean.data.news.get(0).title);
        topNewses=bean.data.topnews;
        topNewAdapter=new TopNewAdapter();
        mViewPager.setAdapter(topNewAdapter);
        mViewPager.setOnPageChangeListener(this);

        llPoints.removeAllViews();
        //初始化图片描述和点
        for (int i=0;i<topNewses.size();i++){
            View view=new View(mContext);
            view.setBackgroundResource(R.drawable.tab_detail_top_news_point_bg);
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(5,5);
            if(i!=0){
                params.leftMargin=10;
            }
            view.setLayoutParams(params);
            view.setEnabled(false);
            llPoints.addView(view);
        }

        previousEnabledPosition=0;
        tvDescription.setText(topNewses.get(previousEnabledPosition).title);
        llPoints.getChildAt(previousEnabledPosition).setEnabled(true);

        //动态图轮播切换

        //1.使用Handler执行延时任务,
        //2.任务类Runable()的方法会被执行，
        //3. HandlerMessage接收消息，
        //4.Viewpager切换页面,
        //5.再次使用Handler执行延时任务
        if (mHandler==null){
            mHandler=new InternalHandler();
        }
        mHandler.removeCallbacksAndMessages(null);//清空任务
        mHandler.postDelayed(new AutoSwitchPagerRunable(),3000);


        //初始化列表数据
        newsList=bean.data.news;
        newsAdapter=new NewsAdapter();
        lvnews.setAdapter(newsAdapter);

    }

    class TopNewAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return topNewses.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image=new ImageView(mContext);
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            image.setImageResource(R.mipmap.home_scroll_default);
            image.setOnTouchListener(new TopNewItemTouchListener());

            String topimageurl=topNewses.get(position).topimage;

            //1.显示的控件 2.图片地址 imageView.setImageBitmap()
            bitmapUtils.display(image, topimageurl);
            container.addView(image);
            return image;
        }
    }


    //触摸处理
    class TopNewItemTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
           switch (event.getAction()){
               case MotionEvent.ACTION_DOWN:
                   //停下
                   mHandler.removeCallbacksAndMessages(null);
                   break;
               case MotionEvent.ACTION_MOVE:
                   //开始
                   mHandler.postDelayed(new AutoSwitchPagerRunable(),3000);
                   break;
               default:
                   break;
           }
            return true;
        }
    }




    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        llPoints.getChildAt(previousEnabledPosition).setEnabled(false);
        llPoints.getChildAt(position).setEnabled(true);
        tvDescription.setText(topNewses.get(position).title);
        previousEnabledPosition=position;
    }


    class InternalHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            int currentItem=mViewPager.getCurrentItem()+1;
            mViewPager.setCurrentItem(currentItem%topNewses.size());
            mHandler.postDelayed(new AutoSwitchPagerRunable(),3000);
            super.handleMessage(msg);
        }
    }


    //自动切换页面任务类
    class AutoSwitchPagerRunable implements Runnable{
        @Override
        public void run() {
            mHandler.obtainMessage().sendToTarget();
        }
    }



    //列表适配
    class NewsAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return newsList.size();
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
            NewsViewHolder mHolder=null;
            if (convertView==null){
                convertView=View.inflate(mContext,R.layout.news_menu_tab_detail_item,null);
                mHolder=new NewsViewHolder();
                mHolder.ivImageView=(ImageView)convertView.findViewById(R.id.iv_news_menu_tab_detail_item_image);
                mHolder.tvTitle=(TextView)convertView.findViewById(R.id.iv_news_menu_tab_detail_item_title);
                mHolder.tvDate=(TextView)convertView.findViewById(R.id.iv_news_menu_tab_detail_item_date);
                convertView.setTag(mHolder);
            }else {
                mHolder=(NewsViewHolder)convertView.getTag();
            }

            //控件赋值
            News news=newsList.get(position);
            mHolder.tvTitle.setText(news.title);
            mHolder.tvDate.setText(news.pubdate);
            bitmapUtils.display(mHolder.ivImageView,news.listimage);
            return convertView;
        }
    }

    class NewsViewHolder{
        public ImageView ivImageView;
        public TextView tvTitle;
        public TextView tvDate;

    }
}
