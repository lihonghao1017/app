package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.ServiceAdapter;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.common.api.entity.Room;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.customview.CustomDialog;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.ui.mine.LoginActivity;
import com.jin91.preciousmetal.ui.mine.OpenAccountActivity;
import com.jin91.preciousmetal.util.FaceUtil;
import com.jin91.preciousmetal.util.IntentUtil;
import com.jin91.preciousmetal.util.PriceUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/4/27.
 * 服务
 */
public class ServiceFragment extends BaseFragment implements ServiceView {


    @ViewInject(R.id.ad_gallery)
    Gallery gallery;
    @ViewInject(R.id.tv_service_replycontent)
    TextView tv_service_replycontent;
    @ViewInject(R.id.tv_service_askcontent)
    TextView tv_service_askcontent;
    @ViewInject(R.id.tv_play_status)
    TextView tv_play_status;
    @ViewInject(R.id.tv_play_teachername)
    TextView tv_play_teachername;

    @ViewInject(R.id.tv_play_number)
    TextView tv_play_number;
    @ViewInject(R.id.rl_play_status)
    RelativeLayout rl_play_status;
    @ViewInject(R.id.ll_current_line)
    LinearLayout ll_current_line;


    ServicePresenter presenter;
    ServiceAdapter adapter;
    List<Room> mList;
    CustomDialog customDialog;
    Room currentRoom; // 当前选中的room房间

    public ServiceFragment() {

    }

    public static ServiceFragment newInstance() {
        ServiceFragment fragment = new ServiceFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_fragment, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }


    /**
     * 刷新播间绑定的状态
     */
    public void refreshRoomList() {
        String userId = "";
        if (PreciousMetalAplication.getINSTANCE().user != null) {
            User user = PreciousMetalAplication.getINSTANCE().user;
            userId = user.ID;
        }
        presenter.getRoomList(MainActivity.TAG, userId);
    }


    @Override
    public void initialize() {
        tv_service_replycontent.setVisibility(View.GONE);
        tv_service_askcontent.setVisibility(View.GONE);
        rl_play_status.setVisibility(View.GONE);
        ll_current_line.setVisibility(View.GONE);
        presenter = new ServicePresenterImpl(this,"");
        mList = new ArrayList<Room>();
        adapter = new ServiceAdapter(mContext, mList);
        gallery.setAdapter(adapter);
        gallery.setAnimationDuration(10);
        gallery.setSpacing(1);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectItem(position);
                int newPosition = position % mList.size();
                currentRoom = mList.get(newPosition);
                setRoomStatus(currentRoom);
                presenter.getNewsPlayDy(MainActivity.TAG, currentRoom.ID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                int newPosition = position % mList.size();
                Room room = mList.get(newPosition);
                ServiceDetailActivity.actionLaunch(mContext, room);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mList != null && mList.size() == 0) {
            String userId = "";
            if (PreciousMetalAplication.INSTANCE.user != null) {
                userId = PreciousMetalAplication.INSTANCE.user.ID;
            }
            presenter.getRoomList(MainActivity.TAG, userId);
        }
    }

    private void setRoomStatus(Room room) {
        if ("1".equals(room.isLive)) {
            // 正在直播
            tv_play_status.setText("正在直播");
            tv_play_status.setBackgroundResource(R.drawable.btn_rect_red);
        } else {
            tv_play_status.setText("直播关闭");
            tv_play_status.setBackgroundResource(R.drawable.btn_rect_black);
        }
        tv_play_teachername.setText(PriceUtil.getRoomTeacherName(room.StaffStatus));
        tv_play_number.setText("直播人气: " + room.Prestige);
    }

    @OnClick({R.id.rl_calendar, R.id.rl_account, R.id.rl_alert, R.id.rl_call_phone})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_calendar:
                FinanceCalenActivity.actionLaunch(mContext);
                break;
            case R.id.rl_account:
                OpenAccountActivity.actionLaunch(mContext);
                break;
            case R.id.rl_alert:
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                PriceAlarmActivity.actionLuanch(mContext);
                break;
            case R.id.rl_call_phone:
                if (customDialog == null) {
                    customDialog = new CustomDialog(mContext, new CustomDialog.CustomDialogListener() {
                        @Override
                        public void customButtonListener(int postion) {
                            if (postion == 2) {
                                IntentUtil.callPhone(mContext, getString(R.string.phond_order_tel));
                            }
                        }
                    }, false, true);
                }
                customDialog.setTvDialogTitle(getString(R.string.phond_order_tel_title));
                customDialog.setHideContent();
                customDialog.show();
                break;

        }
    }

    @Override
    public void setTitleNav() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.tv_title_title.setText(getString(R.string.service));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setItem(List<Room> list) {
        mList.clear();
        mList.addAll(list);
        gallery.setGravity(Gravity.CENTER_VERTICAL);
        int current = adapter.getCount() / 2;
        current = list.size() * (current / list.size());
        gallery.setSelection(current);

        adapter.notifyDataSetChanged();
        rl_play_status.setVisibility(View.VISIBLE);
        ll_current_line.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPriceNotice(Price price) {

    }

    @Override
    public void setDirectPlay(AllData<DirectPlay> playAllData) {

        if (playAllData != null && playAllData.Table != null && playAllData.Table.size() > 0) {
            DirectPlay directPlay = playAllData.Table.get(0);
            if ("1".equals(directPlay.Type)) {
                if (tv_service_replycontent.getVisibility() == View.GONE) {
                    tv_service_replycontent.setVisibility(View.VISIBLE);
                }
                if (tv_service_askcontent.getVisibility() == View.GONE) {
                    tv_service_askcontent.setVisibility(View.VISIBLE);
                }
                // 提问
                tv_service_replycontent.setText(FaceUtil.textAddFace(mContext, "答:" + directPlay.Answer));
                tv_service_askcontent.setText(FaceUtil.textAddFace(mContext, "问:" + directPlay.Question));
            } else {
                // 直播
                if (tv_service_replycontent.getVisibility() == View.GONE) {
                    tv_service_replycontent.setVisibility(View.VISIBLE);
                }
                tv_service_replycontent.setText(FaceUtil.textAddFace(mContext, "直播:" + directPlay.Content));
                tv_service_askcontent.setVisibility(View.GONE);
            }
        } else {
            // 没有直播数据的时候显示
            if (tv_service_askcontent.getVisibility() == View.VISIBLE) {
                tv_service_askcontent.setVisibility(View.GONE);
            }

            if (currentRoom != null) {
                if (!"1".equals(currentRoom.isLive)) {
                    // 不是存在直播就不显示
                    if (tv_service_replycontent.getVisibility() == View.VISIBLE) {
                        tv_service_replycontent.setVisibility(View.GONE);
                    }
                } else {
                    // 是在直播，没有数据
                    if (tv_service_replycontent.getVisibility() == View.GONE) {
                        tv_service_replycontent.setVisibility(View.VISIBLE);
                    }
                    // 是正在直播，但是没有数据
                    tv_service_replycontent.setText(currentRoom.Name + ":欢迎大家加入!");
                }
            } else {
                if (tv_service_replycontent.getVisibility() == View.VISIBLE) {
                    tv_service_replycontent.setVisibility(View.GONE);
                }
            }
        }


    }

    @Override
    public void netError() {
        // 加载失败
        if (tv_service_replycontent.getVisibility() == View.GONE) {
            tv_service_replycontent.setVisibility(View.VISIBLE);
        }
        if (tv_service_askcontent.getVisibility() == View.VISIBLE) {
            tv_service_askcontent.setVisibility(View.GONE);
        }
        tv_service_replycontent.setText(getString(R.string.net_error));
    }

    //////////////////////////////////下面这些没有在播间详情里面用到了//////////////////////////////////////////
    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void showProcessDialog() {

    }

    @Override
    public void hideProcessDialog() {

    }

    @Override
    public void sendCommSuccess() {

    }

    @Override
    public void sendDirectSuccess() {

    }

    @Override
    public void hideInputMethod() {

    }
}
