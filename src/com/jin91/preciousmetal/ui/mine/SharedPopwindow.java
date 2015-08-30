package com.jin91.preciousmetal.ui.mine;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.jin91.preciousmetal.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class SharedPopwindow {
	public static PopupWindow getSharedPopwindow(final Context context, View v) {
		View mView = LayoutInflater.from(context).inflate(
				R.layout.shared_pop_layout, null);
		final PopupWindow menuWindow = new PopupWindow(mView,
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		menuWindow.setOutsideTouchable(true);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new ColorDrawable(-00000));
		menuWindow.setTouchable(true);
		menuWindow.setContentView(mView);
		menuWindow.showAtLocation(v, Gravity.TOP | Gravity.LEFT, 0, 0);
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		// 设置分享内容
		mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(context, 
		                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
		mView.findViewById(R.id.CalenPopwindow_cancle).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						
						menuWindow.dismiss();
					}
				});
		mView.findViewById(R.id.shared_weixinhaoyou).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
					UMImage shareImage =new  UMImage(context,"http://www.umeng.com/images/pic/banner_module_social.png");
					WeiXinShareContent weixinContent=new WeiXinShareContent(shareImage);
					weixinContent.setShareContent("来自友盟社会化组件（SDK）让移");
						weixinContent.setTitle("友盟社会化分享");
						weixinContent.setTargetUrl("http://www.umeng.com/");
						mController.setShareMedia(weixinContent);
						mController.postShare(context, SHARE_MEDIA.WEIXIN_CIRCLE, new SnsPostListener() {
							
							@Override
							public void onStart() {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
								menuWindow.dismiss();
							}
						});	
						
					}
				});
		mView.findViewById(R.id.shared_pengyouquan).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
					}
				});
		mView.findViewById(R.id.shared_xinlangweibo).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
					}
				});
		mView.findViewById(R.id.shared_tengxunweibo).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						menuWindow.dismiss();
					}
				});
		
		return menuWindow;
	}
}
