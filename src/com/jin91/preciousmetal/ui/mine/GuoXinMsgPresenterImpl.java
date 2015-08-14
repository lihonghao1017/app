package com.jin91.preciousmetal.ui.mine;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.MineMessageApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.MessageAll;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.util.JsonUtil;

/**
 * Created by lijinhua on 2015/5/11.
 */
public class GuoXinMsgPresenterImpl<T> implements GuoXinMsgPresenter {

    @SuppressWarnings("rawtypes")
	private GuoXinMsgView view;
    String roleId = "";
    String userId ="";
    @SuppressWarnings("rawtypes")
	public GuoXinMsgPresenterImpl(GuoXinMsgView view) {
        this.view = view;

        User  user = PreciousMetalAplication.INSTANCE.user;
        if (user != null) {
            roleId = user.RoleId;
            userId = user.ID;
        }
    }

    @Override
    public void getGuoXinMsgList(@SuppressWarnings("rawtypes") final TypeToken typeToken, final boolean isShowLoading, String tag, String type, final String startid, String islater) {
        if (isShowLoading) {
            view.showLoading();
        }

        MineMessageApi.getGuoXinMsgList(tag, type, startid, roleId, userId, islater, new ResultCallback() {
            @SuppressWarnings("unchecked")
			@Override
            public void onEntitySuccess(String json) {

                MessageAll<T> messageAll = (MessageAll<T>) JsonUtil.parse(json, typeToken);
                if (isShowLoading && "0".equals(startid)) {
                    // 第一次加载
                    if (messageAll ==null ||messageAll.Table == null || (messageAll.Table != null && messageAll.Table.size() == 0)) {
                        view.showNoDataView();
                        return;
                    }
                }
                if (messageAll.Table != null && messageAll.Table.size() > 0) {
                    view.setItems(messageAll._SugguestNote, messageAll.Table);
                } else {
                    if (!"0".equals(startid)) {
                        // 不是第一页
                        view.showToastNoMoreData();
                    }
                }
                if (isShowLoading && messageAll.Table != null && messageAll.Table.size() != 0) {
                    // 加载成功, 隐藏loading加载
                    view.hideLoading();
                }
            }

            @Override
            public void onException(VolleyError e) {
                if (isShowLoading) {
                    // 加载失败
                    view.showNetErrView();
                } else {
                    view.showToastNetErr();
                }
            }
        });
    }


}
