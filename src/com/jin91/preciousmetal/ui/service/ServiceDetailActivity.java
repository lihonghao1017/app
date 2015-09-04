package com.jin91.preciousmetal.ui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.PlayAdapter;
import com.jin91.preciousmetal.common.api.entity.AllData;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.api.entity.Price;
import com.jin91.preciousmetal.common.api.entity.Room;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.customview.DrawableSubscriptNavButton;
import com.jin91.preciousmetal.customview.GlideCircleTransform;
import com.jin91.preciousmetal.customview.LazyViewPager;
import com.jin91.preciousmetal.customview.LoadingDialog;
import com.jin91.preciousmetal.customview.StickyNavLayout;
import com.jin91.preciousmetal.customview.emo.EmoGridView;
import com.jin91.preciousmetal.customview.emo.Emoparser;
import com.jin91.preciousmetal.ui.Constants;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.jin91.preciousmetal.ui.base.BaseFragment;
import com.jin91.preciousmetal.ui.service.play.DirectPlayFragment;
import com.jin91.preciousmetal.ui.service.play.ExchangeFragment;
import com.jin91.preciousmetal.ui.service.play.MarrowsFragment;
import com.jin91.preciousmetal.ui.service.play.MySayFragment;
import com.jin91.preciousmetal.ui.service.play.RoomNoticeActivity;
import com.jin91.preciousmetal.ui.service.play.SuggestFragment;
import com.jin91.preciousmetal.ui.service.play.ThemeFragment;
import com.jin91.preciousmetal.util.BuildApiUtil;
import com.jin91.preciousmetal.util.ImageUtil;
import com.jin91.preciousmetal.util.InputUtil;
import com.jin91.preciousmetal.util.MessageToast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/4.
 * 服务里面的直播大厅里面的房间
 */
@SuppressLint("NewApi")
public class ServiceDetailActivity extends BaseActivity implements ServiceView {

    public static final String TAG = "ServiceDetailActivity";
    @ViewInject(R.id.id_stickynavlayout_viewpager)
    LazyViewPager id_stickynavlayout_viewpager;
    @ViewInject(R.id.iv_room_avatar)
    ImageView ivRoomAvatar;
    @ViewInject(R.id.tv_online_name)
    TextView tvOnlineName;
    @ViewInject(R.id.tv_notice_content)
    TextView tvNoticeContent;
    @ViewInject(R.id.tv_notice_precent)
    TextView tvNoticePrecent;
    @ViewInject(R.id.iv_service_cursor)
    ImageView ivServiceCursor;
    @ViewInject(R.id.rg_room_nav)
    RadioGroup rgRoomNav;
    @ViewInject(R.id.stickyNavLayout)
    StickyNavLayout stickyNavLayout;
    @ViewInject(R.id.rb_net_telecast)
    DrawableSubscriptNavButton rbNetTelecast;
    @ViewInject(R.id.rb_service_exchange)
    DrawableSubscriptNavButton rbServiceExchange;
    @ViewInject(R.id.rb_service_speak)
    DrawableSubscriptNavButton rbServiceSpeak;
    @ViewInject(R.id.rb_service_subject)
    DrawableSubscriptNavButton rbServiceSubject;
    @ViewInject(R.id.rb_service_essence)
    DrawableSubscriptNavButton rbServiceEssence;
    @ViewInject(R.id.rb_service_suggest)
    DrawableSubscriptNavButton rbServiceSuggest;
    @ViewInject(R.id.id_stickynavlayout_bottomview)
    LinearLayout id_stickynavlayout_bottomview;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    private PlayAdapter adapter;
    private List<BaseFragment> fragments;
    private Room room;
    private int screenWidthSix;
    private int currentIndex;

    private boolean threadStart; // 线程是否开始
    private boolean isVisable;// 是否可见
    private ServicePresenter presenter;
    private Timer timer;
    private Price prePrice;// 上一个的价格

    ////////////////////////////////////////////////////////
    @ViewInject(R.id.iv_text_emo)
    ImageView ivTextEmo;
    @ViewInject(R.id.et_comment_content)
    EditText etCommentContent;
    @ViewInject(R.id.emoGrideView)
    EmoGridView emoGrideView;
    private boolean textChanged;

    protected LoadingDialog loadingDialog;

    protected String replyType; // 回复的类型 // 2--回复或者交流 1 提问
    protected String replyToId; // 回复的id  如果是交流传0,回复传回复的id,提问的时候不传
    /////////////////////////////////////////////


    public static void actionLaunch(Context context, Room room) {
        Intent intent = new Intent(context, ServiceDetailActivity.class);
        intent.putExtra("roomId", room.ID);
        intent.putExtra("room", room);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_detail_2);
        ViewUtils.inject(this);
        initialize();
        setOnClickListener();
    }


    @Override
    public void initialize() {
        room = (Room) getIntent().getSerializableExtra("room");
        Glide.with(mContext).load(ImageUtil.ChangeImgUrlToMoblie(room.ImageUrlApp)).transform(new GlideCircleTransform(mContext)).error(R.mipmap.default_error_avatar).placeholder(R.mipmap.default_normal_avatar).into(ivRoomAvatar);
        tv_title_title.setText(room.Name);
        // 取出播主名字
        tvOnlineName.setText("主播:" + getRoomName() + "   人气:" + room.Prestige);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidthSix = dm.widthPixels / 6;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivServiceCursor.getLayoutParams();
        layoutParams.width = screenWidthSix;
        fragments = new ArrayList<>();
        fragments.add(DirectPlayFragment.newInstance());
        fragments.add(ExchangeFragment.newInstance());
        fragments.add(MySayFragment.newInstance());
        fragments.add(ThemeFragment.newInstance());
        fragments.add(MarrowsFragment.newInstance());
        fragments.add(SuggestFragment.newInstance());
        adapter = new PlayAdapter(getSupportFragmentManager(), id_stickynavlayout_viewpager, fragments);
        id_stickynavlayout_viewpager.setCurrentItem(0);
        stickyNavLayout.setmList(fragments);
        rbNetTelecast.setSubscriptDrawable(R.mipmap.new_msg_tip);
        rbServiceExchange.setSubscriptDrawable(R.mipmap.new_msg_tip);
        rbServiceSpeak.setSubscriptDrawable(R.mipmap.new_msg_tip);
        rbServiceSubject.setSubscriptDrawable(R.mipmap.new_msg_tip);
        rbServiceEssence.setSubscriptDrawable(R.mipmap.new_msg_tip);
        rbServiceSuggest.setSubscriptDrawable(R.mipmap.new_msg_tip);
        presenter = new ServicePresenterImpl(this, room.ID);
        threadStart = true;
        setCommentListener();
        presenter.getPriceNotice(TAG);
        contiunePrice();
    }

    public void setOnClickListener() {
        adapter.setOnExtraPageChangeListener(new PlayAdapter.OnExtraPageChangeListener() {
            @Override
            public void onExtraPageScrolled(int i, float v, int i2) {
                super.onExtraPageScrolled(i, v, i2);
            }

            @Override
            public void onExtraPageSelected(int i) {
                checkRadioButton(i);
            }

            @Override
            public void onExtraPageScrollStateChanged(int i) {
                super.onExtraPageScrollStateChanged(i);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisable = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisable = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    @OnClick({R.id.tv_title_back, R.id.iv_room_avatar, R.id.rb_net_telecast, R.id.rb_service_exchange, R.id.rb_service_subject,
            R.id.rb_service_speak, R.id.rb_service_suggest, R.id.rb_service_essence, R.id.ll_roomnotice_content, R.id.btn_send, R.id.iv_text_emo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.ll_roomnotice_content: // 房间的公告
                RoomNoticeActivity.actionLaunch(mContext, room.ID);
                break;
            case R.id.iv_room_avatar: // 房间的图片
                RoomDetailActivity.actionLaunch(mContext, room);
                break;
            case R.id.rb_net_telecast:// 直播
                id_stickynavlayout_viewpager.setCurrentItem(0, false);
                executorAnim(0);
                break;
            case R.id.rb_service_exchange: // 交流
                id_stickynavlayout_viewpager.setCurrentItem(1, false);
                executorAnim(1);
                break;
            case R.id.rb_service_speak: // 发言
                id_stickynavlayout_viewpager.setCurrentItem(2, false);
                executorAnim(2);
                break;
            case R.id.rb_service_subject: // 主题
                id_stickynavlayout_viewpager.setCurrentItem(3, false);
                executorAnim(3);
                break;
            case R.id.rb_service_essence: // 精华
                id_stickynavlayout_viewpager.setCurrentItem(4, false);
                executorAnim(4);
                break;
            case R.id.rb_service_suggest: // 建议
                id_stickynavlayout_viewpager.setCurrentItem(5, false);
                executorAnim(5);
                break;
            case R.id.btn_send: // 发送
                String content = etCommentContent.getText().toString().trim();
                try {
                    presenter.sendComment(ServiceDetailActivity.TAG, replyType, replyToId, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_text_emo: // 表情或者输入框
                if ("emo".equals(ivTextEmo.getTag())) {
                    // 关闭输入法，显示表情
                    ivTextEmo.setTag("keyboard");
                    InputUtil.hideSoftInput(mContext, etCommentContent.getWindowToken());
                    emoGrideView.setVisibility(View.VISIBLE);
                    ivTextEmo.setImageResource(R.drawable.btn_keyboard_selector);
                } else {
                    emoGrideView.setVisibility(View.GONE);
                    ivTextEmo.setTag("emo");
                    ivTextEmo.setImageResource(R.drawable.btn_emo_selector);
                    InputUtil.openKeyboard(mContext, etCommentContent);
                }

        }
    }


    /**
     * 选中radioButton
     *
     * @param i
     */
    private void checkRadioButton(int i) {
        switch (i) {
            case 0:
                rgRoomNav.check(R.id.rb_net_telecast);
                break;
            case 1:
                rgRoomNav.check(R.id.rb_service_exchange);
                break;
            case 2:
                rgRoomNav.check(R.id.rb_service_speak);
                break;
            case 3:
                rgRoomNav.check(R.id.rb_service_subject);
                break;
            case 4:
                rgRoomNav.check(R.id.rb_service_essence);
                break;
            case 5:
                rgRoomNav.check(R.id.rb_service_suggest);
                break;
        }
        executorAnim(i);
    }

    private void executorAnim(int pageSelectedIndex) {
        TranslateAnimation animation = new TranslateAnimation(screenWidthSix * currentIndex, screenWidthSix * pageSelectedIndex, 0, 0);// 显然这个比较简洁，只有一行代码。
        currentIndex = pageSelectedIndex;
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(200);
        ivServiceCursor.startAnimation(animation);
    }

    /**
     * 获取行情
     */
    private void contiunePrice() {
//        if (timer == null) {
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (!threadStart && isVisable) {
//                        presenter.getPriceNotice(TAG);
//                    }
//                }
//            }, 0, Constants.PRICE_INTERVAL);
//        }
    }

    /**
     * 得到播间的播主名字
     *
     * @return
     */
    public String getRoomName() {
        if (!TextUtils.isEmpty(room.StaffStatus)) {
            String[] str = room.StaffStatus.split(",");
            StringBuilder sb = new StringBuilder();
            for (String newStr : str) {
                String regex = "([\u4e00-\u9FFF]*.*?)";//为汉字
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(newStr);
                while (matcher.find()) {
                    sb.append(matcher.group()).append(" ");
                }
            }
            return sb.toString().trim();
        }
        return "";

    }

    /**
     * @param allData
     */
    public void setNewMsgCount(AllData<Object> allData) {
        switch (currentIndex) {
            case 0: // 不设置第一个设置其他五个
                rbNetTelecast.setSubscriptText(0);
                rbServiceExchange.setSubscriptText(allData.Speak);
                rbServiceSpeak.setSubscriptText(allData.Mydata);
                rbServiceSubject.setSubscriptText(allData.Topic);
                rbServiceEssence.setSubscriptText(allData.Marrow);
                rbServiceSuggest.setSubscriptText(allData.Call);
                break;
            case 1:
                rbNetTelecast.setSubscriptText(allData.Live);
                rbServiceExchange.setSubscriptText(0);
                rbServiceSpeak.setSubscriptText(allData.Mydata);
                rbServiceSubject.setSubscriptText(allData.Topic);
                rbServiceEssence.setSubscriptText(allData.Marrow);
                rbServiceSuggest.setSubscriptText(allData.Call);
                break;
            case 2:
                rbNetTelecast.setSubscriptText(allData.Live);
                rbServiceExchange.setSubscriptText(allData.Speak);
                rbServiceSpeak.setSubscriptText(0);
                rbServiceSubject.setSubscriptText(allData.Topic);
                rbServiceEssence.setSubscriptText(allData.Marrow);
                rbServiceSuggest.setSubscriptText(allData.Call);
                break;
            case 3:
                rbNetTelecast.setSubscriptText(allData.Live);
                rbServiceExchange.setSubscriptText(allData.Speak);
                rbServiceSpeak.setSubscriptText(allData.Mydata);
                rbServiceSubject.setSubscriptText(0);
                rbServiceEssence.setSubscriptText(allData.Marrow);
                rbServiceSuggest.setSubscriptText(allData.Call);
                break;
            case 4:
                rbNetTelecast.setSubscriptText(allData.Live);
                rbServiceExchange.setSubscriptText(allData.Speak);
                rbServiceSpeak.setSubscriptText(allData.Mydata);
                rbServiceSubject.setSubscriptText(allData.Topic);
                rbServiceEssence.setSubscriptText(0);
                rbServiceSuggest.setSubscriptText(allData.Call);
                break;
            case 5:
                rbNetTelecast.setSubscriptText(allData.Live);
                rbServiceExchange.setSubscriptText(allData.Speak);
                rbServiceSpeak.setSubscriptText(allData.Mydata);
                rbServiceSubject.setSubscriptText(allData.Topic);
                rbServiceEssence.setSubscriptText(allData.Marrow);
                rbServiceSuggest.setSubscriptText(0);
                break;
        }


    }


    @Override
    public String getRequestTag() {
        return TAG;
    }

    @Override
    public void setItem(List<Room> list) {

    }

    @Override
    public void setPriceNotice(Price price) {
        if (prePrice != null) {
            if ((prePrice.sell + "").equals(price.sell + "")) {
                //当前的价格有上一个的价格是否相等
                price.changed = false;
            } else {
                price.changed = true;
            }
        }
        // 涨幅=(现价-上一个交易日收盘价）/上一个交易日收盘价*100%
        double persent = ((Double.parseDouble(price.last) - Double.parseDouble(price.lastclose)) * 100) / (Double.parseDouble(price.lastclose));
        tvNoticePrecent.setText(String.format("%.2f", persent) + "%");
        tvNoticeContent.setText(String.format("%.2f", Double.parseDouble(price.buy)) + "/" + String.format("%.2f", Double.parseDouble(price.sell)));
        setPriceColorForRoom(persent, price, tvNoticeContent, tvNoticePrecent);
        threadStart = false;
        prePrice = price;
    }

    @Override
    public void setDirectPlay(AllData<DirectPlay> playAllData) {

    }


    @Override
    public void netError() {
        threadStart = false;

    }

    @Override
    public void showToastMessage(String message) {
        MessageToast.showToast(message, 0);
    }

    @Override
    public void showProcessDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, false);
        }
        loadingDialog.show();
    }

    @Override
    public void hideProcessDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void sendCommSuccess() {
        MessageToast.showToast("发送成功", 0);
        etCommentContent.setText("");
        etCommentContent.setHint("鑫友交流...");
    }

    @Override
    public void sendDirectSuccess() {
        MessageToast.showToast("发送成功", 0);
        etCommentContent.setText("");
        etCommentContent.setHint("顾问咨询...");
    }

    @Override
    public void hideInputMethod() {
        InputUtil.hideSoftInput(mContext, etCommentContent.getWindowToken());
    }

    public void setPriceColorForRoom(double persent, Price price, TextView tv_price, TextView tv_increase) {
        int white = getResources().getColor(R.color.white);
        int no_color = getResources().getColor(R.color.transparent);
        int green = getResources().getColor(R.color.green);
        int black = getResources().getColor(R.color.black);
        int persent_zero = getResources().getColor(R.color.persent_zero);
        int red = getResources().getColor(R.color.red);
        if (persent < 0) {
            tv_increase.setTextColor(green);
            tv_increase.setBackgroundResource(R.drawable.btn_rect_green);
            if (price.changed) {
                tv_price.setBackgroundColor(green);
                tv_price.setTextColor(white);
            } else {
                tv_price.setBackgroundColor(no_color);
                tv_price.setTextColor(green);
            }
        } else if (persent > 0) {
            tv_increase.setTextColor(red);
            tv_increase.setBackgroundResource(R.drawable.btn_rect_red);
            if (price.changed) {
                tv_price.setBackgroundColor(red);
                tv_price.setTextColor(white);
            } else {
                tv_price.setBackgroundColor(no_color);
                tv_price.setTextColor(red);
            }
        } else {
            tv_increase.setTextColor(persent_zero);
            tv_increase.setBackgroundColor(getResources().getColor(R.color.transparent));
            if (price.changed) {
                tv_price.setBackgroundColor(persent_zero);
                tv_price.setTextColor(black);
            } else {
                tv_price.setBackgroundColor(no_color);
                tv_price.setTextColor(persent_zero);
            }
        }
        tv_increase.setTextColor(white);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 打开软件盘
     */
    public void setOpenKeyBoarder() {
        etCommentContent.setFocusable(true);
        etCommentContent.setFocusableInTouchMode(true);
        etCommentContent.requestFocus();
        InputUtil.KeyBoard(etCommentContent, "open");
    }

    /**
     * 设置底部的view是否显示
     *
     * @param isVisble
     */
    public void setBottomViewIsVisble(int isVisble) {
        id_stickynavlayout_bottomview.setVisibility(isVisble);
        if (isVisble == View.VISIBLE) {
            stickyNavLayout.setBottomViewHeight(2);
        } else {
            stickyNavLayout.setBottomViewHeight(1);
        }

    }

    /**
     * 隐藏输入法,在直播和交流里面调用
     */
    public void hideSoftInput() {
        if (InputUtil.isShowKeyBoard(etCommentContent)) {
            // 关闭软键盘
            InputUtil.hideSoftInput(mContext, etCommentContent.getWindowToken());
        }
    }

    /**
     * 回复的id  如果是交流传0,回复传回复的id,提问的时候不传
     *
     * @param replyId
     */
    public void setReplyToId(String replyId) {
        // 设置回复的id
        this.replyToId = replyId;

    }

    /**
     * 2--回复或者交流 1 提问
     *
     * @param replyType
     */
    public void setReplyType(String replyType) {
        // 设置replyType的类型
        this.replyType = replyType;

    }


    /**
     * 设置交流里面下面隐藏
     * @param replyType
     * @param replyToId
     */
    public void setExchangeComInfo(String replyType, String replyToId) {
        // 判断软键盘是弹起
        if (InputUtil.isShowKeyBoard(etCommentContent)) {
            setReplyToId(replyToId);
            setReplyType(replyType);
            hideSoftInput();
            setEtCommentContentText("");
            setEtCommentContentHintText("鑫友交流...");
        }
        if (emoGrideView.getVisibility() == View.VISIBLE) {
            emoGrideView.setVisibility(View.GONE);
            ivTextEmo.setTag("emo");
            ivTextEmo.setImageResource(R.drawable.btn_emo_selector);
        }
    }

    /**
     * 设置直播里面
     * @param replyType
     * @param replyToId
     */
    public void setDirectPlayComInfo(String replyType, String replyToId)
    {
        if (InputUtil.isShowKeyBoard(etCommentContent))
        {
            setReplyToId(replyToId);
            setReplyType(replyType);
            hideSoftInput();
        }
        if (emoGrideView.getVisibility() == View.VISIBLE) {
            emoGrideView.setVisibility(View.GONE);
            ivTextEmo.setTag("emo");
            ivTextEmo.setImageResource(R.drawable.btn_emo_selector);
        }
    }   

    public void setEtCommentContentText(String text) {
        etCommentContent.setText(text);
    }

    public void setEtCommentContentHintText(String hintText) {
        etCommentContent.setHint(hintText);
    }

    public void setCommentListener() {
        ivTextEmo.setTag("emo");
        BuildApiUtil.setCursorDraw(etCommentContent);
        emoGrideView.setOnEmoGridViewItemClick(new EmoGridView.OnEmoGridViewItemClick() {
            @Override
            public void onItemClick(int facesPos, int viewIndex) {

                textChanged = false;
                int deleteId = (++viewIndex) * (Constants.pageSize - 1);
                if (deleteId > Emoparser.getInstance(mContext.getApplicationContext()).getResIdList().length) {
                    deleteId = Emoparser.getInstance(mContext.getApplicationContext()).getResIdList().length;
                }
                if (deleteId == facesPos) {
                    String msgContent = etCommentContent.getText().toString();
                    if (TextUtils.isEmpty(msgContent)) {
                        return;
                    }
                    if (msgContent.contains("[")) {
                        msgContent = msgContent.substring(0, msgContent.lastIndexOf("[EMOT]"));
                    }
                    etCommentContent.setText(msgContent);
                } else {
                    int resId = Emoparser.getInstance(mContext.getApplicationContext()).getResIdList()[facesPos];
                    String pharse = Emoparser.getInstance(mContext.getApplicationContext()).getIdPhraseMap().get(resId);
                    int startIndex = etCommentContent.getSelectionStart();
                    Editable edit = etCommentContent.getEditableText();
                    if (startIndex < 0 || startIndex >= edit.length()) {
                        if (null != pharse) {
                            edit.append(pharse);
                        }
                    } else {
                        if (null != pharse) {
                            edit.insert(startIndex, pharse);
                        }
                    }
                }
                Editable edtable = etCommentContent.getText();
                int position = edtable.length();
                Selection.setSelection(edtable, position);

            }
        });
        emoGrideView.setAdapter();
        etCommentContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Logger.i(ServiceDetailActivity.TAG, "onTextChanged");
                if (s.length() > 0) {
                    String strMsg = etCommentContent.getText().toString();
                    CharSequence emoCharSeq = Emoparser.getInstance(mContext.getApplicationContext()).emoCharsequence(strMsg);
                    if (!textChanged) {
                        textChanged = true;
                        etCommentContent.setText(emoCharSeq);
                        Editable edtable = etCommentContent.getText();
                        int position = edtable.length();
                        Selection.setSelection(edtable, position);

                    } else {
                        textChanged = false;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textChanged = true;
            }
        });
        etCommentContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivTextEmo.setImageResource(R.drawable.btn_emo_selector);
            }
        });
    }

}
