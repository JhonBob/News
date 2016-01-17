package com.bob.news.fragment;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bob.news.base.BaseFragment;

/**
 * Created by Administrator on 2016/1/16.
 */


public class LeftMenuFragment extends BaseFragment{

    @Override
    public View initView(LayoutInflater inflater) {
        TextView tv=new TextView(mActivity);
        tv.setText("LEFTMENU");
        tv.setTextSize(20);
        return tv;
    }
}
