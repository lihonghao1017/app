package com.jin91.preciousmetal.ui.service.play;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.jin91.preciousmetal.common.api.PlayRoomApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.Exchange;
import com.jin91.preciousmetal.common.api.entity.ImmedSuggest;
import com.jin91.preciousmetal.common.api.entity.LiveRoom;
import com.jin91.preciousmetal.common.api.entity.Marrow;
import com.jin91.preciousmetal.common.api.entity.MySay;
import com.jin91.preciousmetal.common.api.entity.RoomNotice;
import com.jin91.preciousmetal.common.api.entity.Tables;
import com.jin91.preciousmetal.common.api.entity.Theme;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.jin91.preciousmetal.util.JsonUtil;

/**
 * Created by lijinhua on 2015/5/13.
 * @param <VolleyError>
 */
public class PlayPresenterImpl<T> implements PlayPresenter {

    private PlayView view;
    private String roomId;
    private String userId;


    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public PlayPresenterImpl(PlayView view, String roomId) {
        this.view = view;
        this.roomId = roomId;
        User user = PreciousMetalAplication.INSTANCE.user;
        if (user == null) {
            userId = "";
        } else {
            userId = user.ID;
        }

        preferences = PreciousMetalAplication.getContext().getSharedPreferences(roomId, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    
    
    public void getExchangeFirstList(final Object typeToken, final boolean isShowLoading, final String type, final String startId) {
        if (isShowLoading) {
            view.showLoading();
        }
        PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, getFirstMapParams(type), startId, new ResultCallback() {
			@Override
            public void onEntitySuccess(String json) {
            	
                try {
					LiveRoom<T> liveRoom = (LiveRoom<T>) JsonUtil.parse(json, (Class<T>)typeToken);
                    if (liveRoom == null || liveRoom.Alldata == null) {
                        if (isShowLoading) {
                            view.showNetErrView();
                        }
                        return;
                    }
                    // 设置新的消息
                    view.setNewMsgCount(liveRoom.Alldata);
                    if (liveRoom.Alldata.Table == null || liveRoom.Alldata.Table.size() == 0) {
                        if (isShowLoading) {
                            view.showNoDataView("暂无直播");
                        }
                        return;
                    }
                    view.setFistDataList(liveRoom.Alldata.Table);
                    if (isShowLoading) {
                        view.hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    view.setRefreshComplete();
                }
            }

			@Override
			public void onException(com.android.volley.VolleyError e) {
				 playFirstListErrCall(isShowLoading);
			}
        });
    }
    /**
     * @param typeToken
     * @param isShowLoading
     * @param type          0,直播 1,交流 2，发言 3,主题 4,精华 5表示建议
     * @param startId       第一次传0,如果已经获取到，传最新的一条数据的id
     */
    public void getDirectPlayFirstList(final Object typeToken, final boolean isShowLoading, final String type, final String startId) {
        if (isShowLoading) {
            view.showLoading();
        }
        PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, getFirstMapParams(type), startId, new ResultCallback() {
            @SuppressWarnings("unchecked")
			@Override
            public void onEntitySuccess(String json) {
            	TypeToken obj=null;
            	if (type.equals("0")) {
            		obj=new TypeToken<LiveRoom<DirectPlay>>() {
                    };
				}else if (type.equals("1")) {
					obj=new TypeToken<LiveRoom<Exchange>>() {
                    };
				}else if (type.equals("2")) {
					obj=new TypeToken<LiveRoom<MySay>>() {
                    };
				}else if (type.equals("3")) {
					obj=new TypeToken<LiveRoom<Theme>>() {
                    };
				}else if (type.equals("4")) {
					obj=new TypeToken<LiveRoom<Marrow>>() {
                    };
				}
                try {
					LiveRoom<T> liveRoom = (LiveRoom<T>) JsonUtil.parse(json, obj);
                    if (liveRoom == null || liveRoom.Alldata == null) {
                        if (isShowLoading) {
                            view.showNetErrView();
                        }
                        return;
                    }
                    // 设置新的消息
                    view.setNewMsgCount(liveRoom.Alldata);
                    if (liveRoom.Alldata.Table == null || liveRoom.Alldata.Table.size() == 0) {
                        if (isShowLoading) {
                            view.showNoDataView("暂无直播");
                        }
                        return;
                    }
                    view.setFistDataList(liveRoom.Alldata.Table);
                    if (isShowLoading) {
                        view.hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    view.setRefreshComplete();
                }
            }

			@Override
			public void onException(com.android.volley.VolleyError e) {
				 playFirstListErrCall(isShowLoading);
			}
        });
    }

    @Override
    public void getSuggestFirstList(final boolean isShowLoading, String startId) {
        if (isShowLoading) {
            view.showLoading();
        }
        PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, getFirstMapParams("5"), startId, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                TypeToken<LiveRoom<ImmedSuggest>> firstTypeToken = new TypeToken<LiveRoom<ImmedSuggest>>() {
                };
                LiveRoom<ImmedSuggest> liveRoom = JsonUtil.parse(json, firstTypeToken);
                view.setRefreshComplete();
                if (liveRoom == null || liveRoom.Alldata == null) {
                    if (isShowLoading) {
                        view.showNetErrView();
                    }
                    return;
                }

                // 设置新的消息
                view.setNewMsgCount(liveRoom.Alldata);
                if (liveRoom.Alldata.Table == null || liveRoom.Alldata.Table.size() == 0) {
                    if (isShowLoading) {
                        view.showNoDataView("暂无即时建议");
                    }
                    return;
                }
                if (liveRoom.Alldata.Table.size() >= 1) {
                    ImmedSuggest suggest = liveRoom.Alldata.Table.get(0);
                    if (!suggest._isLook) {
                        // 没有绑定播间的状态
                        if (isShowLoading) {
                            view.showNoDataView("您还没有设此直播间为自己的顾问间，只有在自己的顾问间才能查看即时建议");
                        }
                        return;
                    } else {
                        // 绑定了播间
                        view.setSuggestNote(liveRoom.Alldata.Table.remove(0)._SugguestNote);
                        if (liveRoom.Alldata.Table.size() > 0) {
                            view.setFistDataList(liveRoom.Alldata.Table);
                        }
                    }
                }

                if (isShowLoading) {
                    view.hideLoading();
                }
            }

			@Override
			public void onException(com.android.volley.VolleyError e) {
				  playFirstListErrCall(isShowLoading);
			}
        });
    }

    private void playFirstListErrCall(boolean isShowLoading) {
        if (isShowLoading) {
            // 加载失败
            view.showNetErrView();
        } else {
            view.showToastNetErr();
        }
        view.setRefreshComplete();
    }

    @Override
    public void getDirectPlayMoreList(final Object typeToken, String startId, String action,final String type) {
    	
    	PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, userId, roomId, startId, action, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
            	TypeToken obj=null;
            	if (type.equals("0")) {
            		obj=new TypeToken<AllData<DirectPlay>>() {
                    };
        		}else if (type.equals("1")) {
        			obj=new TypeToken<AllData<Exchange>>() {
                    };
        		}else if (type.equals("2")) {
        			obj=new TypeToken<AllData<MySay>>() {
                    };
        		}else if (type.equals("3")) {
        			obj=new TypeToken<AllData<Theme>>() {
                    };
        		}else if (type.equals("4")) {
        			obj=new TypeToken<AllData<Marrow>>() {
                    };
        		}
                try {
                    AllData<T> allData = (AllData<T>) JsonUtil.parse(json, obj);
                    if (allData != null && allData.Table != null && allData.Table.size() > 0) {
                        view.setMoreDataList(allData.Table);
                    } else {
                        view.showToastNoMoreData();
                    }
                } finally {
                    view.setRefreshComplete();
                }

            }


			@Override
			public void onException(com.android.volley.VolleyError e) {
				 view.setRefreshComplete();
	                view.showToastNetErr();
			}
        });
    }

    @Override
    public void getMySayMoreList(final Object typeToken, String startId, String action) {
        PlayRoomApi.getMySayMoreList(ServiceDetailActivity.TAG, userId, roomId, startId, action, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                    AllData<T> allData = (AllData<T>) JsonUtil.parse(json, (Class<T>) typeToken);
                    if (allData != null && allData.Table != null && allData.Table.size() > 0) {
                        view.setMoreDataList(allData.Table);
                    } else {
                        view.showToastNoMoreData();
                    }
                } finally {
                    view.setRefreshComplete();
                }

            }

            @Override
            public void onException(com.android.volley.VolleyError e) {
                view.setRefreshComplete();
                view.showToastNetErr();
            }

        });
    }

    @Override
    public void getThemePlayMoreList(final Object typeToken, String startId, String action) {
        PlayRoomApi.getThemePlayMoreList(ServiceDetailActivity.TAG, userId, roomId, startId, action, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                try {
                	TypeToken<AllData<Theme>> moreTypeToken = new TypeToken<AllData<Theme>>() {
                    };
                    AllData<T> allData = (AllData<T>) JsonUtil.parse(json, moreTypeToken);
                    if (allData != null && allData.Table != null && allData.Table.size() > 0) {
                        view.setMoreDataList(allData.Table);
                    } else {
                        view.showToastNoMoreData();
                    }
                } finally {
                    view.setRefreshComplete();
                }

            }

            @Override
            public void onException(VolleyError e) {
                view.setRefreshComplete();
                view.showToastNetErr();
            }
        });
    }


    @Override
    public void getSuggestMoreList(final Object typeToken, String startId, String action) {
        PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, userId, roomId, startId, action, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    boolean _isLook = jsonObject.getBoolean("_isLook");
                    if (_isLook) {
                        // 已经绑定了播间
                        String _Call = jsonObject.getString("_Call");
                        if (!TextUtils.isEmpty(_Call)) {
                            AllData<T> allData = (AllData<T>) JsonUtil.parse(_Call, (Class<T>) typeToken);
                            if (allData != null && allData.Table != null && allData.Table.size() > 0) {

                                view.setMoreDataList(allData.Table);
                            } else {
                                view.showToastNoMoreData();
                            }
                        } else {
                            view.showToastNoMoreData();
                        }
                    } else {
                        view.showToastNoMoreData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    view.setRefreshComplete();
                }


            }

            @Override
            public void onException(VolleyError e) {
                view.showToastNetErr();
                view.setRefreshComplete();
            }
        });
    }

    @Override
    public void getRoomNoticeList(String tag, final boolean isShowLoading, String startid, String islater) {
        if (isShowLoading) {
            view.showLoading();
        }
        PlayRoomApi.getRoomNoticeList(tag, userId, startid, islater, roomId, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                TypeToken<Tables<RoomNotice>> type = new TypeToken<Tables<RoomNotice>>() {
                };
                Tables<RoomNotice> currentList = JsonUtil.parse(json, type);
                if (currentList != null && currentList.Table != null && currentList.Table.size() > 0) {
                    view.setDataList(currentList.Table);
                } else {
                    if (isShowLoading) {
                        //
                        view.showNoDataView("");
                        return;
                    } else {
                        view.showToastNoMoreData();
                    }
                }
                if (isShowLoading) {
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


    @Override
    public void sendComment(String tag, String type, String replyTo, String content) {
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
                        view.sendSuccess();
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


    private HashMap<String, String> getFirstMapParams(String type) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("liveid", preferences.getString("liveid", "0"));
        map.put("speakid", preferences.getString("speakid", "0"));
        map.put("mydataid", preferences.getString("mydataid", "0"));
        map.put("topicid", preferences.getString("topicid", "0"));
        map.put("marrowid", preferences.getString("marrowid", "0"));
        map.put("callid", preferences.getString("callid", "0"));
        //
        map.put("roomid", roomId);
        map.put("uid", userId);
        map.put("type", type);
        return map;
    }

    @Override
    public void setLiveid(String liveid) {
        editor.putString("liveid", liveid).commit();
    }

    @Override
    public void setSpeakid(String speakid) {
        editor.putString("speakid", speakid).commit();
    }

    @Override
    public void setMydataid(String mydataid) {
        editor.putString("mydataid", mydataid).commit();
    }

    @Override
    public void setTopicid(String topicid) {
        editor.putString("topicid", topicid).commit();
    }

    @Override
    public void setMarrowid(String marrowid) {
        editor.putString("marrowid", marrowid).commit();
    }

    @Override
    public void setCallid(String callid) {
        editor.putString("callid", callid).commit();
    }



	@Override
	public void getDirectPlayFirstNewList(Object typeToken,
			final boolean isShowLoading, final String type, String startId) {
		if (isShowLoading) {
            view.showLoading();
        }
		HashMap<String, String> map=getFirstMapParams("0");
		 map.put("LeftIndex", type);
        PlayRoomApi.getDirectPlayMoreList(ServiceDetailActivity.TAG, map, startId, new ResultCallback() {
            @SuppressWarnings("unchecked")
			@Override
            public void onEntitySuccess(String json) {
            	TypeToken obj=new TypeToken<LiveRoom<DirectPlay>>() {
                };
                try {
					LiveRoom<T> liveRoom = (LiveRoom<T>) JsonUtil.parse(json, obj);
                    if (liveRoom == null || liveRoom.Alldata == null) {
                        if (isShowLoading) {
                            view.showNetErrView();
                        }
                        return;
                    }
                    // 设置新的消息
                    view.setNewMsgCount(liveRoom.Alldata);
                    if (liveRoom.Alldata.Table == null || liveRoom.Alldata.Table.size() == 0) {
                        if (isShowLoading) {
                            view.showNoDataView("暂无直播");
                        }
                        return;
                    }
                    view.setFistDataList(liveRoom.Alldata.Table);
                    if (isShowLoading) {
                        view.hideLoading();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    view.setRefreshComplete();
                }
            }

			@Override
			public void onException(com.android.volley.VolleyError e) {
				 playFirstListErrCall(isShowLoading);
			}
        });
	}



	@Override
	public void getDirectPlayMoreNewList(Object typeToken, String startId,
			String action, String type) {
		PlayRoomApi.getDirectPlayMoreNewList(ServiceDetailActivity.TAG, userId, roomId, startId, action,type, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
            	TypeToken obj=new TypeToken<AllData<DirectPlay>>() {
                    };
        		
                try {
                    AllData<T> allData = (AllData<T>) JsonUtil.parse(json, obj);
                    if (allData != null && allData.Table != null && allData.Table.size() > 0) {
                        view.setMoreDataList(allData.Table);
                    } else {
                        view.showToastNoMoreData();
                    }
                } finally {
                    view.setRefreshComplete();
                }

            }


			@Override
			public void onException(com.android.volley.VolleyError e) {
				 view.setRefreshComplete();
	                view.showToastNetErr();
			}
        });
	}





}
