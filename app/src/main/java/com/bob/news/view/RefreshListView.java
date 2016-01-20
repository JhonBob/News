package com.bob.news.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.bob.news.R;

/**
 * Created by Administrator on 2016/1/18.
 */
public class RefreshListView extends ListView {
    public RefreshListView(Context context) {
        super(context);
        initHeader();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeader();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeader();
    }

    private void initHeader(){
        View mHeaderView=View.inflate(getContext(), R.layout.reftresh_headview,null);
        this.addHeaderView(mHeaderView);
    }
}
