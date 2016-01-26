package com.bob.news.domain;

import com.bob.news.base.NewsCenterPager;

import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
public class NewsCenterBean {
    public int retcode;
    public List<NewsCenterData> data;
    public List<String> extend;

    public class NewsCenterData {
        public List<ChildRen> children;
        public int id;
        public String title;
        public int type;
        public String url;
        public String url1;
        public String dayurl;
        public String exurl;
        public String weekurl;

        @Override
        public String toString() {
            return "NewsCenterData{" +
                    "children=" + children +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    ", url1='" + url1 + '\'' +
                    ", dayurl='" + dayurl + '\'' +
                    ", exurl='" + exurl + '\'' +
                    ", weekurl='" + weekurl + '\'' +
                    '}';
        }
    }

    public class ChildRen{
        public int id;
        public String title;
        public int type;
        public String url;
    }

}
