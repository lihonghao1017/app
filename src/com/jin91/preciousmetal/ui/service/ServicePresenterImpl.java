package com.jin91.preciousmetal.ui.service;

import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.PlayRoomApi;
import com.jin91.preciousmetal.common.api.ServiceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.common.api.entity.Room;
import com.jin91.preciousmetal.common.api.entity.RoomWrap;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.util.JsonUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by lijinhua on 2015/5/12.
 */
public class ServicePresenterImpl implements ServicePresenter {

    private ServiceView view;
    private String userId;
    private String roomId;

    public ServicePresenterImpl(ServiceView view, String roomId) {
        this.view = view;
        this.roomId = roomId;
        User user = PreciousMetalAplication.INSTANCE.user;
        if (user == null) {
            userId = "";
        } else {
            userId = user.ID;
        }
    }

    @Override
    public void getRoomList(String tag, String uid) {
        ServiceApi.getRoomList(tag, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                TypeToken<RoomWrap> type = new TypeToken<RoomWrap>() {
                };
                RoomWrap roomWrap = JsonUtil.parse(json, type);
                if (roomWrap != null) {
                    List<Room> roomList = roomWrap.Table;
                    if (roomList != null && roomList.size() > 0) {
                        view.setItem(roomList);
                    }
                }
            }

            @Override
            public void onException(VolleyError e) {

            }
        });
    }

    @Override
    public void getNewsPlayDy(String tag, String roomId) {
        ServiceApi.getNewsPlayDy(tag, roomId, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                TypeToken<AllData<DirectPlay>> moreTypeToken = new TypeToken<AllData<DirectPlay>>() {
                };
                AllData<DirectPlay> playAllData = JsonUtil.parse(json, moreTypeToken);
                view.setDirectPlay(playAllData);
            }

            @Override
            public void onException(VolleyError e) {
                view.netError();
            }
        });
    }

    @Override
    public void getPriceNotice(String tag) {
        ServiceApi.getPriceNotice(tag, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                TypeToken<List<Price>> type = new TypeToken<List<Price>>() {
                };
                List<Price> priceList = JsonUtil.parseList(json, type);

                if (priceList.size() > 0) {
                    view.setPriceNotice(priceList.get(0));
                } else {
                    view.netError();
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.netError();
            }
        });
    }

    @Override
    public void sendComment(String tag, final String type, String replyTo, String content) {
        if (TextUtils.isEmpty(content)) {
            view.showToastMessage("发送内容不能为空");
            return;
        }
        if (content.length() > 200) {
            view.showToastMessage("发送内容最多为200个字符");
            return;
        }
        try {
            content = URLEncoder.encode(content, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        view.hideInputMethod();
        view.showProcessDialog();
        PlayRoomApi.sendComment(tag, type, userId, roomId, replyTo, content, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                view.hideProcessDialog();
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (!jsonObject.isNull("status") && jsonObject.getString("status").equals("1")) {
                        if ("1".equals(type)) {
                            // 发送提问
                            view.sendDirectSuccess();
                        } else {
                            // 发送回复，和交流
                            view.sendCommSuccess();
                        }
                    } else {
                        if (!jsonObject.isNull("ErrorInfo")) {
                            view.showToastMessage(jsonObject.getString("ErrorInfo"));
                        } else {
                            view.showToastMessage("发送失败");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(VolleyError e) {
                view.hideProcessDialog();
                view.showToastMessage("网络错误请重新发送");
            }
        });

    }
}
