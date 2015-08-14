package com.jin91.preciousmetal.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.AccountMangerApi;
import com.jin91.preciousmetal.common.api.base.ResultCallback;
import com.jin91.preciousmetal.common.api.entity.User;
import com.jin91.preciousmetal.common.api.entity.UserInfo;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.ui.MainActivity;
import com.jin91.preciousmetal.ui.PreciousMetalAplication;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.util.ClientUtil;
import com.jin91.preciousmetal.util.JsonUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.jin91.preciousmetal.util.UserHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import de.greenrobot.event.EventBus;

/**
 * Created by lijinhua on 2015/4/27.
 * 我的
 */
public class MineFragment extends BaseFragment {


    @ViewInject(R.id.rl_advise)
    RelativeLayout rl_advise;
    @ViewInject(R.id.rl_advise_reply)
    RelativeLayout rl_advise_reply;
    @ViewInject(R.id.rl_message)
    RelativeLayout rl_message;
    @ViewInject(R.id.rl_setting)
    RelativeLayout rl_setting;
    @ViewInject(R.id.tv_mine_login)
    TextView tv_mine_login;
    @ViewInject(R.id.tv_nickname)
    TextView tv_nickname;
    @ViewInject(R.id.tv_mine_email)
    TextView tv_mine_email;
    @ViewInject(R.id.iv_user_header)
    ImageView iv_user_header;
    @ViewInject(R.id.iv_advise_newMsg)
    ImageView iv_advise_newMsg;
    @ViewInject(R.id.iv_reply_newMsg)
    ImageView iv_reply_newMsg;
    @ViewInject(R.id.iv_guoxin_newMsg)
    ImageView iv_guoxin_newMsg;
    @ViewInject(R.id.iv_set_newMsg)
    ImageView iv_set_newMsg;


    UserHelper userHelper;

    public MineFragment() {

    }

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        initNewsMsg();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @param i 1---表示登录
     *          2---表示注销
     */
    public void onEventMainThread(String i) {
        MessageToast.showToast("mainefrage===" + i, 0);

        if ("1".equals(i) || "2".equals(i)) {
            // 登录成功
            initUserView();
        }
    }

    @Override
    public void initialize() {
        userHelper = UserHelper.getInstance();
        if (userHelper.getIdeaMsgNumber() > 0) {
            iv_advise_newMsg.setVisibility(View.VISIBLE);
        }
        if (userHelper.getReplyCount() > 0) {
            iv_reply_newMsg.setVisibility(View.VISIBLE);
        }
        if (userHelper.getGuoXinCount() > 0) {
            iv_guoxin_newMsg.setVisibility(View.VISIBLE);
        }

        initUserView();

    }

    public void initUserView() {
        if (PreciousMetalAplication.getINSTANCE().user != null) {
            tv_nickname.setVisibility(View.VISIBLE);
            tv_mine_email.setVisibility(View.VISIBLE);
            // 登录的时候
            tv_mine_login.setVisibility(View.GONE);
            User user = PreciousMetalAplication.getINSTANCE().user;
            tv_nickname.setText(user.NickName);
            getUserInfo(user.ID);
        } else {
            // 没有登录的时候
            tv_mine_login.setVisibility(View.VISIBLE);
            tv_nickname.setVisibility(View.GONE);
            tv_mine_email.setVisibility(View.GONE);
            iv_user_header.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.default_normal_avatar));
        }
    }

    @OnClick({R.id.rl_message, R.id.tv_mine_login, R.id.rl_setting, R.id.iv_user_header, R.id.rl_advise, R.id.rl_advise_reply, R.id.tv_nickname, R.id.tv_mine_email})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_message: // 国鑫消息
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                GuoXinMessageActivity.actionLaunch(mContext);
                break;
            case R.id.tv_mine_login:
                LoginActivity.actionLaunch(mContext);
                break;
            case R.id.rl_setting: // 设置
                SettingActivity.actionLaunch(mContext);
                break;
            case R.id.iv_user_header: // 用户的头像
            case R.id.tv_mine_email: // 用户的email
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    return;
                }
                AccountMangerActivity.actionLaunch(mContext);
                break;
            case R.id.tv_nickname: // 用户有昵称
                AccountMangerActivity.actionLaunch(mContext);
                break;
            case R.id.rl_advise: // 即时建议
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                ImmedSuggestActivity.actionLaunch(mContext);
                break;
            case R.id.rl_advise_reply: // 顾问回复
                if (PreciousMetalAplication.getINSTANCE().user == null) {
                    LoginActivity.actionLaunch(mContext);
                    return;
                }
                CounselorReplyActivity.actionLaunch(mContext);
                break;
        }
    }


    public void initNewsMsg() {
        if (userHelper.getIdeaMsgNumber() > 0) {
            iv_advise_newMsg.setVisibility(View.VISIBLE);
        } else {
            iv_advise_newMsg.setVisibility(View.INVISIBLE);
        }

        if (userHelper.getReplyCount() > 0) {
            iv_reply_newMsg.setVisibility(View.VISIBLE);
        } else {
            iv_reply_newMsg.setVisibility(View.INVISIBLE);
        }

        if (userHelper.getGuoXinCount() > 0) {
            iv_guoxin_newMsg.setVisibility(View.VISIBLE);
        } else {
            iv_guoxin_newMsg.setVisibility(View.INVISIBLE);
        }
        String versionName = userHelper.getVersion();
        if (TextUtils.isEmpty(versionName) || ClientUtil.getVersionName(mContext).equals(versionName)) {
            iv_set_newMsg.setVisibility(View.INVISIBLE);
        } else {
            iv_set_newMsg.setVisibility(View.VISIBLE);
        }
    }

    public void showSetMsg() {
        iv_set_newMsg.setVisibility(View.VISIBLE);
    }

    /**
     * 登录的时候获取一下用户信息
     */
    private void getUserInfo(String uid) {
        AccountMangerApi.getUserinfo(MainActivity.TAG, uid, new ResultCallback() {
            @Override
            public void onEntitySuccess(String json) {
                UserInfo userInfo = JsonUtil.parseUserInfo(json);
                if (userInfo != null) {
                    if (TextUtils.isEmpty(userInfo.Email)) {
                        tv_mine_email.setText(userInfo.Mobile);
                    } else {
                        tv_mine_email.setText(userInfo.Email);
                    }
                    Glide.with(mContext).load(userInfo.UserPicFull).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(iv_user_header);
                }
            }

            @Override
            public void onException(VolleyError e) {

            }
        });
    }

    @Override
    public void setTitleNav() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.tv_title_title.setText(getString(R.string.personal));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
