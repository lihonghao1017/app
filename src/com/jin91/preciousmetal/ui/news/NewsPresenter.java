package com.jin91.preciousmetal.ui.news;

/**
 * Created by lijinhua on 2015/4/28.
 */
public interface NewsPresenter {
    /**
     * 获取新闻
     * @param tag
     * @param isShowLoading 是否显示loading
     * @param  type 0 国鑫日评
     *              1 分析研究
     *              2 机构观点
     *              3 资讯导读
     *              4 市场动态
     * @param pageNumber
     */
    public void getNewsList(String tag,boolean isShowLoading,int type,int pageNumber);
}
