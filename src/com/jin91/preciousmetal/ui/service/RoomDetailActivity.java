package com.jin91.preciousmetal.ui.service;

import org.json.JSONException;import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.ServiceApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.Room;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.ImageUtil;
import com.jin91.preciousmetal.util.PriceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/14. 播间的资料
 */
public class RoomDetailActivity extends BaseActivity {

	@ViewInject(R.id.iv_roomdetail_img)
	ImageView ivRoomdetailImg;
	@ViewInject(R.id.tv_room_name)
	TextView tvRoomName;
	@ViewInject(R.id.tv_room_playname)
	TextView tvRoomPlayname;
	@ViewInject(R.id.tv_room_number)
	TextView tvRoomNumber;
	@ViewInject(R.id.tv_room_style)
	TextView tvRoomStyle;
	@ViewInject(R.id.tv_title_back)
	public TextView tv_title_back;
	@ViewInject(R.id.tv_title_option)
	public TextView tv_title_option;
	@ViewInject(R.id.tv_title_title)
	public TextView tv_title_title;
	@ViewInject(R.id.RoomDetailActivity_bangding)
	public Button bangding_bt;
	private String roomId;

	public static void actionLaunch(Context context, Room room) {
		Intent intent = new Intent(context, RoomDetailActivity.class);
		intent.putExtra("room", room);
		context.startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_detail);
		ViewUtils.inject(this);
		initialize();
	}

	@Override
	public String getRequestTag() {
		return "RoomDetailActivity";
	}

	@OnClick({ R.id.tv_title_back, R.id.RoomDetailActivity_bangding })
	public void OnClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_back:
			finish();
			break;
		case R.id.RoomDetailActivity_bangding:
			if (bangding_bt.getText().toString().equals("绑定")) {
				bangding();
			} else {
				unBangding();
			}
			break;
		}
	}

	@Override
	public void initialize() {
		Room room = (Room) getIntent().getSerializableExtra("room");
		roomId = room.ID;
		tv_title_title.setText(room.Name);
		Glide.with(mContext)
				.load(ImageUtil.ChangeImgUrlToMoblie(room.ImageUrlApp))
				.transform(new GlideCircleTransform(mContext))
				.error(R.mipmap.default_error_avatar)
				.placeholder(R.mipmap.default_normal_avatar)
				.into(ivRoomdetailImg);
		tvRoomName.setText(room.Name);
		tvRoomPlayname.setText(PriceUtil.getRoomTeacherName(room.StaffStatus));
		tvRoomNumber.setText(room.Prestige + "人气");
		tvRoomStyle.setText(room.Style);
	}

	private void bangding() {
		String userId = "";

		if (PreciousMetalAplication.getINSTANCE().user != null) {
			User user = PreciousMetalAplication.getINSTANCE().user;
			userId = user.ID;

		}
		if (!userId.equals("")) {
			ServiceApi.getRoomBangding("RoomDetailActivity", userId, roomId,
					new ResultCallback() {

						@Override
						public void onException(VolleyError e) {
							Toast.makeText(RoomDetailActivity.this,
									"绑定失败：" + e.getMessage(), 1000).show();
						}

						@Override
						public void onEntitySuccess(String json) {
							try {
								JSONObject obj = new JSONObject(json);
								int status = obj.getInt("status");
								switch (status) {
								case 0:
									Toast.makeText(RoomDetailActivity.this,
											"绑定失败：" + obj.getString("Error"),
											1000).show();
									break;
								case 1:
									Toast.makeText(RoomDetailActivity.this,
											"绑定成功", 1000).show();
									bangding_bt.setText("解除绑定");
									break;
								case 2:
									Toast.makeText(RoomDetailActivity.this,
											"未登录", 1000).show();
									break;
								case 3:
									getNotify();
									break;
								default:
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			Toast.makeText(this, "请登录！！！", 1000).show();
		}
	}

	private void unBangding() {
		String userId = "";

		if (PreciousMetalAplication.getINSTANCE().user != null) {
			User user = PreciousMetalAplication.getINSTANCE().user;
			userId = user.ID;

		}
		if (!userId.equals("")) {
			ServiceApi.getRoomUnBangding("RoomDetailActivity", userId, roomId,
					new ResultCallback() {

						@Override
						public void onException(VolleyError e) {
							Toast.makeText(RoomDetailActivity.this,
									"绑定失败：" + e.getMessage(), 1000).show();
						}

						@Override
						public void onEntitySuccess(String json) {
							try {
								JSONObject obj = new JSONObject(json);
								int status = obj.getInt("status");
								switch (status) {
								case 0:
									Toast.makeText(RoomDetailActivity.this,
											"绑定失败：" + obj.getString("Error"),
											1000).show();
									break;
								case 1:
									Toast.makeText(RoomDetailActivity.this,
											"绑定成功", 1000).show();
									bangding_bt.setText("绑定");
									break;
								case 2:

									break;
								case 4:

									break;
								default:
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			Toast.makeText(this, "请登录！！！", 1000).show();
		}
	}
	CustomDialog customDialog;
	public void getNotify(){
		if(customDialog==null){
			customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
	            @Override
	            public void customButtonListener(int postion) {
	                if (postion == 2) {
	                	customDialog.dismiss();
	                	customDialog=null;
	                }
	            }
	        }, false, true);
	          customDialog.setTvDialogTitle("温馨提示");
	          customDialog.setTvDialogContent("不是实盘账户");
	          customDialog.setTvOkText("确定");
	          customDialog.setHideTvCancel();
	          customDialog.show();
			
		}
		
	}

}
