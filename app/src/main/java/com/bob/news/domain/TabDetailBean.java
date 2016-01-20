package com.bob.news.domain;

import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class TabDetailBean {

    public int retcode;
    public TabDetailData data;

    public class TabDetailData{
        public String countcommenturl;
        public String more;
        public String title;
        public List<Topic> topic;
        public List<News> news;
        public List<TopNews> topnews;
    }

    public class News{
        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;
    }

    public class Topic{
        public String description;
        public String id;
        public String listimage;
        public String sort;
        public String title;
        public String url;
    }

    public class TopNews{
        public String comment;
        public String commentlist;
        public String commenturl;
        public String id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;
    }
}
