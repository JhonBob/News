package com.bob.news.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bob.news.R;

/**
 * Created by Administrator on 2016/1/16.
 */
public class TabBasePage {
    public Context mContext;
    public TextView tvTitle;
    public ImageButton ibMenu;
    public FrameLayout flContent;
    public View rootView;

    public TabBasePage(Context context) {
        this.mContext=context;
        rootView=initView();
    }

   private View initView(){
       View view=View.inflate(mContext, R.layout.table_base_pager,null);
       tvTitle=(TextView)view.findViewById(R.id.tv_title_bar_title);
       ibMenu=(ImageButton)view.findViewById(R.id.ib_title_bar_menu);
       flContent=(FrameLayout)view.findViewById(R.id.fl_tab_pager_content);
       return view;
   }

    public View getRootView(){
        return rootView;
    }

    public void initData(){}

}
