package com.jin91.preciousmetal.ui.service.play;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.DirectPlay;
import com.jin91.preciousmetal.common.util.Logger;
import com.jin91.preciousmetal.ui.service.ServiceDetailActivity;
import com.jin91.preciousmetal.util.InputUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/12.
 */
public class DirectPlayFragment extends Fragment implements
		View.OnClickListener {

	@ViewInject(R.id.liveRoom_all)
	TextView all_tv;
	@ViewInject(R.id.liveRoom_baiyin)
	TextView baiyin_tv;
	@ViewInject(R.id.liveRoom_huangjin)
	TextView huangjin_tv;
	@ViewInject(R.id.liveRoom_tonglvge)
	TextView tonglvnue_tv;
	@ViewInject(R.id.liveRoom_shiyou)
	TextView shiyou_tv;
	@ViewInject(R.id.liveRoom_index)
	ImageView index;
	private Fragment room01,room02,room03,room04,room05;

	protected String replyType; // 回复的类型 // 2--回复或者交流 1 提问
	protected String replyToId; // 回复的id 如果是交流传0,回复传回复的id,提问的时候不传

	private int indexWidth;
	private int currentIndex;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private ServiceDetailActivity detailActivity;


	public DirectPlayFragment() {
	}

	public static DirectPlayFragment newInstance() {
		DirectPlayFragment fragment = new DirectPlayFragment();
		return fragment;
	}

	private void initLineWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		indexWidth = dm.widthPixels / 5;
		index.setLayoutParams(new LinearLayout.LayoutParams(indexWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT));
	}

	private void setTabText(int flag) {
		all_tv.setTextColor(flag == 0 ? 0xFFF34F4F : 0xFF333333);
		baiyin_tv.setTextColor(flag == 1 ? 0xFFF34F4F : 0xFF333333);
		huangjin_tv.setTextColor(flag == 2 ? 0xFFF34F4F : 0xFF333333);
		tonglvnue_tv.setTextColor(flag == 3 ? 0xFFF34F4F : 0xFF333333);
		shiyou_tv.setTextColor(flag == 4 ? 0xFFF34F4F : 0xFF333333);
		Animation animation = new TranslateAnimation(indexWidth * currentIndex,
				indexWidth * flag, 0, 0);
		;
		animation.setFillAfter(true);
		animation.setDuration(300);
		index.startAnimation(animation);
		currentIndex = flag;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		detailActivity = (ServiceDetailActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.direct_play_list_back, container,
				false);
		ViewUtils.inject(this, view);
		initLineWidth();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
//		detailActivity = (ServiceDetailActivity) getActivity();
//		isUseNempt=true;
		initialize();
		fm=this.getChildFragmentManager();
		ft=fm.beginTransaction();
		ft.replace(R.id.fl_room_container, room01).commit();
	}

	public void initialize() {
		replyType = "1";
		replyToId = "";
		InputUtil.hideSoftInput(getActivity());
		room01=new AllRomeFragment(0);
		room02=new AllRomeFragment(1);
		room03=new AllRomeFragment(2);
		room04=new AllRomeFragment(3);
		room05=new AllRomeFragment(4);
	}

	@Override
	public void onResume() {
		super.onResume();
//		isVisable = true;
//		if (!threadStart) {
//			Logger.i(ServiceDetailActivity.TAG, "onresume中获取数据");
//			threadStart = true;
//		}
		if (detailActivity != null) {
			detailActivity.setBottomViewIsVisble(View.VISIBLE);
			detailActivity.setReplyToId(replyToId);
			detailActivity.setReplyType(replyType);
			detailActivity.setEtCommentContentText("");
			detailActivity.setEtCommentContentHintText("顾问咨询...");
		}
	}


	@OnClick({ R.id.liveRoom_all, R.id.liveRoom_baiyin, R.id.liveRoom_huangjin,
			R.id.liveRoom_tonglvge, R.id.liveRoom_shiyou })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.liveRoom_all: // 数据为null
			setTabText(0);
			ft=fm.beginTransaction();
			ft.replace(R.id.fl_room_container, room01).commit();
			break;
		case R.id.liveRoom_baiyin: // 数据为null
			setTabText(1);
			ft=fm.beginTransaction();
			ft.replace(R.id.fl_room_container, room02).commit();
			break;
		case R.id.liveRoom_huangjin: // 数据为null
			setTabText(2);
			ft=fm.beginTransaction();
			ft.replace(R.id.fl_room_container, room03).commit();
			break;
		case R.id.liveRoom_tonglvge: // 数据为null
			setTabText(3);
			ft=fm.beginTransaction();
			ft.replace(R.id.fl_room_container, room04).commit();
			break;
		case R.id.liveRoom_shiyou: // 数据为null
			setTabText(4);
			ft=fm.beginTransaction();
			ft.replace(R.id.fl_room_container, room05).commit();
			break;

		}
	}
}
