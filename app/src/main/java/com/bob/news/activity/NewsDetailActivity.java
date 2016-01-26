package com.bob.news.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bob.news.R;
import com.bob.news.utils.ShareUtils;

public class NewsDetailActivity extends Activity implements View.OnClickListener{

    private String url;
    private WebView webView;
    private ProgressBar mProgressBar;
    private int tempSelectTextSizePosition; // 在对话框中临时选择的字体
    private int currentSelectTextSizePosition = 2; // 当前选中的字体, 默认为: 正常大小
    private WebSettings settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        Intent intent=getIntent();
        url=intent.getStringExtra("url");

        initView();
    }

    public void initView(){
        findViewById(R.id.ib_title_bar_menu).setVisibility(View.GONE);
        findViewById(R.id.tv_title_bar_title).setVisibility(View.GONE);
        findViewById(R.id.ib_title_bar_back).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_title_bar_share).setVisibility(View.VISIBLE);
        findViewById(R.id.ib_title_bar_textsize).setVisibility(View.VISIBLE);

        findViewById(R.id.ib_title_bar_back).setOnClickListener(this);
        findViewById(R.id.ib_title_bar_share).setOnClickListener(this);
        findViewById(R.id.ib_title_bar_textsize).setOnClickListener(this);

        webView=(WebView)findViewById(R.id.wv_news_detail);
        mProgressBar=(ProgressBar)findViewById(R.id.pb_news_detail);

        settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);//配置JavaScript可用
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_title_bar_back:
                finish();
                break;
            case R.id.ib_title_bar_share:
                ShareUtils shareUtils=new ShareUtils();
                shareUtils.showShare(this,"分享",this.getFilesDir().getPath()+"qzone.png");
                break;
            case R.id.ib_title_bar_textsize:
                showTextSizeDialog();
                break;
            default:
                break;
        }
    }

    //改变字体对话框
    /**
     * 弹出改变字体的对话框
     */
    private void showTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择字体大小");
        String[] items = {"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

        tempSelectTextSizePosition = currentSelectTextSizePosition;
        builder.setSingleChoiceItems(items, currentSelectTextSizePosition,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tempSelectTextSizePosition = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentSelectTextSizePosition = tempSelectTextSizePosition;
                changeWebViewTextSize();
            }
        });
        builder.setNegativeButton("取消", null);

        builder.show();
    }

    /**
     * 根据currentSelectTextSizePosition变量来改变字体
     */
    protected void changeWebViewTextSize() {
        switch (currentSelectTextSizePosition) {
            case 0:
                settings.setTextSize(WebSettings.TextSize.LARGEST);
                break;
            case 1:
                settings.setTextSize(WebSettings.TextSize.LARGER);
                break;
            case 2:
                settings.setTextSize(WebSettings.TextSize.NORMAL);
                break;
            case 3:
                settings.setTextSize(WebSettings.TextSize.SMALLER);
                break;
            case 4:
                settings.setTextSize(WebSettings.TextSize.SMALLEST);
                break;
            default:
                break;
        }
    }
}
