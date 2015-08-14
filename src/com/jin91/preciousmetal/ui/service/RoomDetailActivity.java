package com.jin91.preciousmetal.ui.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Room;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.util.ImageUtil;
import com.jin91.preciousmetal.util.PriceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/14.
 * 播间的资料
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
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

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


    @OnClick(R.id.tv_title_back)
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
        }
    }

    @Override
    public void initialize() {
        Room room = (Room) getIntent().getSerializableExtra("room");
        tv_title_title.setText(room.Name);
        Glide.with(mContext).load(ImageUtil.ChangeImgUrlToMoblie(room.ImageUrlApp)).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(ivRoomdetailImg);
        tvRoomName.setText(room.Name);
        tvRoomPlayname.setText(PriceUtil.getRoomTeacherName(room.StaffStatus));
        tvRoomNumber.setText(room.Prestige + "人气");
        tvRoomStyle.setText(room.Style);
    }


}
