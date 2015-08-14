package com.jin91.preciousmetal.wxapi;

import java.io.Serializable;

/**
 * Created by lijinhua on 2015/4/29.
 */
public class ShareModel implements Serializable{

    public String url;
    public String type_title; // 分享类型的title
    public String content_title;// 分享内容的title

    public int weixin_type;// 分享微信的类型  0表示微信好友  1表示朋友圈
}
