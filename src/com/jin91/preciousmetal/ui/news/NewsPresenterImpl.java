package com.jin91.preciousmetal.ui.news;

import com.android.volley.VolleyError;
import com.jin91.preciousmetal.common.api.NewsApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Study;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lijinhua on 2015/4/28.
 */
public class NewsPresenterImpl implements NewsPresenter {


    NewsView newsView;

    public NewsPresenterImpl(NewsView newsView) {
        this.newsView = newsView;
    }

    @Override
    public void getNewsList(String tag, boolean isShowLoading, int type, int pageNumber) {
        switch (type) {
            case 0: // 国鑫日评
                jin91DayCom(tag,isShowLoading,pageNumber);
                break;
            case 1: // 分析研究
                analyzeStudy(tag, isShowLoading, pageNumber);
                break;
            case 2:// 机构观点
                orgViewPoint(tag, isShowLoading, pageNumber);
                break;
            case 3:// 资讯导读
                infoGuid(tag, isShowLoading, pageNumber);
                break;
            case 4: // 市场动态
                marketDynamic(tag, isShowLoading, pageNumber);
                break;
        }
    }

    /**
     * 解析数据并返回
     *
     * @param json
     */
    public void parseDataCall(final boolean isShowloading, String json) {
        List<Study> list = parseStudyOther(json);
        if (list != null && list.size() > 0) {
            newsView.setItems(list);
        }
        if (isShowloading) {
            newsView.hideLoading();
        }
        newsView.refreshComplete();
    }

    /**
     * 国鑫日评
     *
     * @param tag
     * @param isShowloading
     * @param pageNumber
     */
    public void jin91DayCom(String tag, final boolean isShowloading, int pageNumber) {
        if (isShowloading) {
            newsView.showLoading();
        }
        NewsApi.jin91DayCom(tag, pageNumber + "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                parseDataCall(isShowloading, json);
            }

            @Override
            public void onException(VolleyError e) {
                if (isShowloading) {
                    newsView.showNetErrView();
                }
                newsView.refreshComplete();
            }
        });
    }

    /**
     * 分析研究
     *
     * @param tag
     * @param isShowloading 是否显示加载loading
     * @param pageNumber
     */
    public void analyzeStudy(String tag, final boolean isShowloading, final int pageNumber) {
        if (isShowloading) {
            newsView.showLoading();
        }
        NewsApi.analyzeStudy(tag, pageNumber + "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                parseDataCall(isShowloading, json);
            }

            @Override
            public void onException(VolleyError e) {
                if (isShowloading) {
                    newsView.showNetErrView();
                }
                newsView.refreshComplete();

            }
        });
    }
    /**
     * 机构观点
     *
     * @param tag
     * @param isShowloading 是否显示加载loading
     * @param pageNumber
     */
    public void orgViewPoint(String tag, final boolean isShowloading, final int pageNumber) {
        if (isShowloading) {
            newsView.showLoading();
        }
        NewsApi.orgViewPoint(tag, pageNumber + "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                parseDataCall(isShowloading, json);
            }

            @Override
            public void onException(VolleyError e) {
                if (isShowloading) {
                    newsView.showNetErrView();
                }
                newsView.refreshComplete();

            }
        });
    }

    /**
     * 资讯导读
     *
     * @param tag
     * @param isShowloading 是否显示加载loading
     * @param pageNumber
     */
    public void infoGuid(String tag, final boolean isShowloading, final int pageNumber) {
        if (isShowloading) {
            newsView.showLoading();
        }
        NewsApi.infoGuid(tag, pageNumber + "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                parseDataCall(isShowloading, json);
            }

            @Override
            public void onException(VolleyError e) {
                if (isShowloading) {
                    newsView.showNetErrView();
                }
                newsView.refreshComplete();

            }
        });
    }
    /**
     * 市场动态
     *
     * @param tag
     * @param isShowloading 是否显示加载loading
     * @param pageNumber
     */
    public void marketDynamic(String tag, final boolean isShowloading, final int pageNumber) {
        if (isShowloading) {
            newsView.showLoading();
        }
        NewsApi.marketDynamic(tag, pageNumber + "", new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                parseDataCall(isShowloading, json);
            }

            @Override
            public void onException(VolleyError e) {
                if (isShowloading) {
                    newsView.showNetErrView();
                }
                newsView.refreshComplete();
            }
        });
    }
    /**
     * @param json
     * @return
     * @Description:解析国鑫研究其它选项卡
     * @author: qiaoshl
     * @date:2014年3月5日
     */

    public List<Study> parseStudyOther(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            String resposeCode = object.getString("success");
            if (resposeCode != null && resposeCode.equals("1")) {
                String html = object.getString("list_html");
                return parseHtml(html);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Study> parseHtml(String html) {
        List<Study> list = new ArrayList<Study>();
        Document document = Jsoup.parse(html);
        Elements hrefss = document.select("h3");
        for (Element element : hrefss) {
            String date = element.ownText();
            date = date.replace('/', '-');
            date = date.substring(1, date.length() - 1);
            Element link = element.select("a").first();
            String title = link.text();
            String linkHref = link.attr("href");
            int startIndex = linkHref.indexOf("-");
            int endIndex = linkHref.indexOf(".");
            String id = linkHref.substring(startIndex + 1, endIndex);
            Study study = new Study();
            study.id = id;
            study.adddate = date;
            study.title = title;
            list.add(study);
        }
        return list;
    }

}
