package com.jin91.preciousmetal.ui;

/**
 * Created by lijinhua on 2015/4/29.
 * 常量
 */
public interface Constants {

    public static final  int MAX_LINE = 5;
    public static final int pageSize = 21; // 表情显示的个数
    public static final int ROOM_INTERVAL = 10 * 1000;// 播间每隔10秒获取一次
    public static final int PRICE_INTERVAL = 2 * 1000;// 行情时间间隔

    public static final int PRICE_CHART_TIME_DURING = 2 * 60 * 1000; // 分时图获取的间隔
    /*** *****************操作的类型*******************************/
    // TL 1减持2平仓3撤销4修改
    public static final String TYPE_REDUCE = "1";// 减持
    public static final String TYPE_EMPTY = "2";// 平仓
    public static final String TYPE_REVOCATION = "3";// 撤销
    public static final String TYPE_UPDATE = "4";// 修改

    /**
     * *****************分享******************************
     */
    // 微信APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String WECHAT_APP_ID = "wxa0370a7b2fbc7736";
    /* 新浪 */
    public static final String SINA_APP_KEY = "2758792823";
    // 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
    public static final String SINA_REDIRECT_URL = "http://www.91jin.com";
    // Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0
    // 授权页中有权利 选择赋予应用的功能。
    public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write," + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog,"
            + "invitation_write";


    /**
     * ***************************mqtt***********************************
     */
    // mq用户名和密码
   public static final String MQTT_HOST = "211.152.5.39";
   //public static final String MQTT_HOST = "192.168.99.104";
    public static final int MQTT_BROKER_PORT_NUM = 1883;
    public static final String MQTT_BROKER_USER_NAME = "gxmquser";
    public static final String MQTT_BROKER_USER_PWD = "mqpass@91net!M";

    public static final int TIME_OUT = 15 * 1000;// 连接网络超时
    public static final int MQTT_EXCEPTION_INTERVAL = 60 * 1000;// 推送服务器异常后连接间隔 // 2秒

    public static final int MQTT_DELAY_CONNEC = 0;// 定时检查连接情况首次延迟

    public static final int MQTT_CHECK_CONNEC = 45 * 60;// 定时检查连接情况(秒) 45分钟


    // 订阅主题相关
    public static final String topic_calls = "calls/";// 指定房间的即时建议
    public static final String topic_myquestions = "myquestions/";// 指定客户的顾问回复
    public static final String topic_gxmsgs = "gxmsgs";// 发给所有客户的国鑫消息
    public static final String topic_night_news = "nightnews";// 国鑫研究
    public static final String topic_push_price = "pushprice/";// 推送行情
    public static final String topic_gxmsgs_role = "gxmsgs/role/";// 发给某个角色客户的国鑫消息
    public static final String topic_gxmsgs_client = "gxmsgs/client/";// 发给某个客户的国鑫消息
    public static final String topic_client_ = "client_";// 客户id


    /**
     * **********************数据库的key*******************************
     */
    public static final String DB_STRING_KEY_ROOM = "db.string.room"; // 数据库 DBString 存储房间
    public static final String DB_STRING_KEY_USER = "db.string.user"; // 数据库 user 存储房间

    public static String DOWN_PATH = "/downlond/PreciousMetal/";

}
