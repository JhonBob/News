package com.bob.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bob.news.R;
import com.lidroid.xutils.view.annotation.event.OnScroll;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;

/**
 * Created by Administrator on 2016/1/18.
 */

//自定义下拉刷新ListView
public class RefreshListView extends ListView implements AbsListView.OnScrollListener{

    //整个头布局对象
    private LinearLayout mHeaderView;
    //添加自定义头布局
    private View mCustomHeaderView;
    //按下时Y轴的偏移量
    private int downY=-1;
    //下拉头布局的高度
    private int mPullDownHeaderViewHeight;
    //下拉头布局对象
    private View mPullDownHeaderView;



    //下拉刷新
    private final int PULL_DOWN=0;
    //释放刷新
    private final int RELEASE_FRESH=1;
    //正在刷新
    private final int REFRESHING=2;
    //当前头布局的的状态，默认为：下拉刷新状态
    private int currentState=PULL_DOWN;


    //向上旋转动画
    private RotateAnimation upAnim;
    //向下旋转动画
    private RotateAnimation downAnim;
    //头布局箭头
    private ImageView ivArrow;
    //头布局进度圈
    private ProgressBar mProgressBar;
    //头布局状态
    private TextView tvState;
    //头布局最后刷新时间
    private TextView tvLastUpdateTime;
    //ListView在Y轴的值
    private int mListViewYOnScreen=-1;


    //脚步局对象
    private View mFooterView;
    //胶布局高度
    private int mFooterViewHeight;
    //是否正在加载更多
    private boolean isLoadingMore=false;

    //回掉刷新
    private OnRefreshListener mOnRefreshListener;



    public RefreshListView(Context context) {
        super(context);
        initHeader();
        initFooter();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
        initFooter();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeader();
        initFooter();
    }

    private void initHeader(){
        mHeaderView=(LinearLayout)View.inflate(getContext(), R.layout.reftresh_headview,null);

        mPullDownHeaderView=mHeaderView.findViewById(R.id.ll_refresh_header_root);
        ivArrow=(ImageView)mHeaderView.findViewById(R.id.iv_refresh_header_view_pull_down_arrow);
        mProgressBar=(ProgressBar)mHeaderView.findViewById(R.id.iv_refresh_header_view_pull_down_pb);
        tvState=(TextView)mHeaderView.findViewById(R.id.iv_refresh_header_view_pull_down_state);
        tvLastUpdateTime=(TextView)mHeaderView.findViewById(R.id.iv_refresh_header_view_pull_down_date);

        tvLastUpdateTime.setText("最后刷新时间:"+ getCurrentTime());

        //测量头高度
        mPullDownHeaderView.measure(0,0);
        //得到高度
        mPullDownHeaderViewHeight=mPullDownHeaderView.getMeasuredHeight();
        //隐藏头布局
        mPullDownHeaderView.setPadding(0,-mPullDownHeaderViewHeight,0,0);

        this.addHeaderView(mHeaderView);

        //初始化动画
        initAnimation();
    }

    //底布局
    public void initFooter(){
        mFooterView=View.inflate(getContext(),R.layout.refresh_foot_view,null);
        mFooterView.measure(0,0);
        mFooterViewHeight=mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
        this.addFooterView(mFooterView);

        //监听滑动
        this.setOnScrollListener(this);
    }

    //加自定义头布局
    public void addCustomHeaderView(View v){
        mCustomHeaderView=v;
        mHeaderView.addView(v);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY=(int)ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (downY==-1){
                    downY=(int)ev.getY();
                }

                //处理轮播图闪回，没完全显示跳出switch,执行父元素的touch事件
                if (mCustomHeaderView!=null){
                    //获取ListView的Y
                    int[] location=new int[2];//0位x,1位位y
                    if (mListViewYOnScreen==-1) {
                        this.getLocationOnScreen(location);
                        mListViewYOnScreen = location[1];
                    }

                    //mCustomHeaderView在屏幕的Y
                    mCustomHeaderView.getLocationOnScreen(location);
                    int mCustomHeaderViewYOnScreen=location[1];
                    //没有完全显示跳出switch
                    if (mListViewYOnScreen>mCustomHeaderViewYOnScreen){
                        break;
                    }

                }


                int moveY=(int)ev.getY();
                //移动的距离
                int diffY=moveY-downY;
                //且处于0位置
                if (diffY>0 && getFirstVisiblePosition()==0){
                    //重计算PaddingTop
                    int paddingTop=-mPullDownHeaderViewHeight+diffY;

                    if (paddingTop>0 && currentState!=RELEASE_FRESH){
                        currentState=RELEASE_FRESH;
                        refreshPullDownHeaderState();
                    }else if (paddingTop<0 && currentState!=PULL_DOWN){
                        currentState=PULL_DOWN;
                        refreshPullDownHeaderState();
                    }

                    mPullDownHeaderView.setPadding(0,paddingTop,0,0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                downY=-1;
                //下拉刷新，隐藏头布局
                if (currentState==PULL_DOWN){
                    mPullDownHeaderView.setPadding(0,-mPullDownHeaderViewHeight,0,0);
                }else if (currentState==RELEASE_FRESH) {
                    //释放刷新，显示头布局
                    mPullDownHeaderView.setPadding(0,0,0,0);
                    currentState=REFRESHING;
                    refreshPullDownHeaderState();

                    //调用回掉
                    if (mOnRefreshListener!=null){
                        mOnRefreshListener.onPullDownRefresh();
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    //刷新
    public void refreshPullDownHeaderState(){
        switch (currentState){
            case PULL_DOWN:
                ivArrow.startAnimation(downAnim);
                tvState.setText("下拉刷新");
                break;
            case RELEASE_FRESH:
                ivArrow.startAnimation(upAnim);
                tvState.setText("松开刷新");
                break;
            case REFRESHING:
                ivArrow.clearAnimation();
                ivArrow.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                tvState.setText("正在刷新...");
                break;
            default:
                break;
        }
    }

    //初始化动画
    public void initAnimation(){
        //顺时针
        upAnim=new RotateAnimation(0,-180, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        upAnim.setDuration(500);
        //暂留
        upAnim.setFillAfter(true);
        //逆时针
        downAnim=new RotateAnimation(-180,-360, Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        downAnim.setDuration(500);
        //暂留
        downAnim.setFillAfter(true);
    }

    public interface OnRefreshListener{
        //数据刷新回掉接口
        void onPullDownRefresh();

        void onLoadingMore();
    }

    public void setOnfreshListener(OnRefreshListener listener){
        this.mOnRefreshListener=listener;
    }


    public void onRefreshFinish(){
        if (isLoadingMore){
            isLoadingMore=false;
            mFooterView.setPadding(0,-mFooterViewHeight,0,0);
        }else {
            //处理下拉刷新
            mPullDownHeaderView.setPadding(0, -mPullDownHeaderViewHeight, 0, 0);
            currentState = PULL_DOWN;
            mProgressBar.setVisibility(INVISIBLE);
            ivArrow.setVisibility(VISIBLE);
            tvLastUpdateTime.setText("最后刷新时间:" + getCurrentTime());
            tvState.setText("下拉刷新");
        }

    }

    private String getCurrentTime(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date());
    }



    //滑动监听
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState==SCROLL_STATE_IDLE || scrollState==SCROLL_STATE_FLING){
            int lastVisiblePosition =getLastVisiblePosition();
            if ((lastVisiblePosition==getCount()-1) && !isLoadingMore){

                isLoadingMore=true;
                //显示脚布局
                mFooterView.setPadding(0,0,0,0);
                //把LastView显示到最底边
                this.setSelection(getCount());

                if (mOnRefreshListener!=null){
                    mOnRefreshListener.onLoadingMore();
                }
            }

        }
    }

    //滚动时
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
