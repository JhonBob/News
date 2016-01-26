package com.bob.news.NewCenterDetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bob.news.R;
import com.bob.news.base.MenuDetailBasePager;
import com.bob.news.domain.NewsCenterBean.NewsCenterData;
import com.bob.news.domain.PhotosBean;
import com.bob.news.utils.Constancts;
import com.bob.news.utils.ImageUtils;
import com.bob.news.utils.SPUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.bob.news.domain.PhotosBean.PhotosItem;
import com.bob.news.utils.NetCache;

import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
//新闻菜单对于的界面
public class PhotosMenuDetailPager extends MenuDetailBasePager{

    private ListView mListView;
    private GridView mGridView;
    private  HttpUtils httpUtils;

    private List<PhotosItem> photosList;
    private PhotosAdapter mAdapter;

    private boolean isDisplayList=true;
    private ImageUtils imageUtils;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NetCache.success:
                    Bitmap bitmap=(Bitmap)msg.obj;
                    int tag=msg.arg1;//抓取TAG
                    ImageView iv=(ImageView)mListView.findViewWithTag(tag);
                    iv.setImageBitmap(bitmap);
                    break;
                case NetCache.failed:
                    Toast.makeText(mContext,"网络请求失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    public PhotosMenuDetailPager(Context context,NewsCenterData newsCenterData) {
        super(context);

        System.out.println("组图："+newsCenterData.toString());
    }


    @Override
    public View initView() {
        View view=View.inflate(mContext, R.layout.photos,null);
        mListView=(ListView)view.findViewById(R.id.lv_photos);
        mGridView=(GridView)view.findViewById(R.id.gv_photos);
        return view;
    }

    @Override
    public void initData() {
        imageUtils=new ImageUtils(handler);
        String json=SPUtils.getString(mContext,Constancts.photo_url,null);

        if (!TextUtils.isEmpty(json)){
            processData(json);
        }

        httpUtils=new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constancts.photo_url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println("组图数据请求成功："+responseInfo.result);

                SPUtils.putString(mContext, Constancts.photo_url, responseInfo.result);

                processData(responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                System.out.println("组图数据请求失败："+s);
            }
        });
    }


    public void processData(String result){
        Gson gson=new Gson();
        PhotosBean bean=gson.fromJson(result, PhotosBean.class);
        photosList=bean.data.news;
        mAdapter=new PhotosAdapter();
        mListView.setAdapter(mAdapter);
        System.out.println(bean.data.news.get(0).title);
    }


    class PhotoViewHolder{
        public ImageView imageView;
        public TextView mTextView;
    }

    public  class PhotosAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return photosList.size();
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
            PhotoViewHolder mHolder=null;
            if (convertView==null){
                convertView=View.inflate(mContext,R.layout.photos_item,null);
                mHolder=new PhotoViewHolder();
                mHolder.imageView=(ImageView)convertView.findViewById(R.id.photos_im);
                mHolder.mTextView=(TextView)convertView.findViewById(R.id.photos_te);
                convertView.setTag(mHolder);
            }else {
                mHolder=(PhotoViewHolder)convertView.getTag();
            }

            mHolder.mTextView.setText(photosList.get(position).title);

            //Tag标识图片
            mHolder.imageView.setTag(position);

            //默认图片
            mHolder.imageView.setImageResource(R.mipmap.pic_item_list_default);
            //网络抓取图片
            Bitmap bitmap=imageUtils.getImageFromUtils(photosList.get(position).listimage,position);
            if (bitmap!=null){
                mHolder.imageView.setImageBitmap(bitmap);
            }
            return convertView;
        }
    }

    //切换样式
    public void switchCurrentPager(ImageButton imageButton){
        if (isDisplayList){
            //显示GridView
            mListView.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
            mGridView.setAdapter(mAdapter);
            isDisplayList=false;
            imageButton.setImageResource(R.mipmap.icon_pic_list_type);
        }else {
            mListView.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
            mListView.setAdapter(mAdapter);
            isDisplayList=true;
            imageButton.setImageResource(R.mipmap.icon_pic_grid_type);
        }
    }

}
