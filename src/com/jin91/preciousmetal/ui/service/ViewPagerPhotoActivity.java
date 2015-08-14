package com.jin91.preciousmetal.ui.service;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.Image;
import com.jin91.preciousmetal.customview.photoview.HackyViewPager;
import com.jin91.preciousmetal.customview.photoview.PhotoView;
import com.jin91.preciousmetal.ui.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by lijinhua on 2015/5/18.
 */
public class ViewPagerPhotoActivity extends BaseActivity {


    @ViewInject(R.id.hacky_viewpager)
    HackyViewPager hackyViewpager;
    @ViewInject(R.id.tv_title_back)
    public  TextView tv_title_back;
    @ViewInject(R.id.tv_title_option)
    public TextView tv_title_option;
    @ViewInject(R.id.tv_title_title)
    public TextView tv_title_title;

    private ImagePagerAdapter adapter;
    private List<Image> mList;
    private int currentPosition;// 当前的位置

    public static void actionLaunch(Context context, List<Image> list, int position) {
        Intent intent = new Intent(context, ViewPagerPhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("list", (java.io.Serializable) list);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager_photo_view);
        ViewUtils.inject(this);
        initialize();

    }

    @SuppressWarnings({ "unchecked", "deprecation" })
	@Override
    public void initialize() {
        mList = (List<Image>) getIntent().getExtras().getSerializable("list");
        System.out.println(mList);
        currentPosition = getIntent().getIntExtra("position", 0);
        tv_title_title.setText((currentPosition + 1) + "/" + mList.size());
        adapter = new ImagePagerAdapter();
        hackyViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title_title.setText((position + 1) + "/" + mList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        hackyViewpager.setAdapter(adapter);

    }

    @OnClick({R.id.tv_title_back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
        }
    }

    @Override
    public String getRequestTag() {
        return "ViewPagerPhotoActivity";
    }

    private class ImagePagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public ImagePagerAdapter() {
            inflater = getLayoutInflater();
        }


        @SuppressLint("InflateParams")
		@Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.viewpager_photo_item, null);
            PhotoView photoView = (PhotoView) view.findViewById(R.id.photoView);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
            Glide.with(ViewPagerPhotoActivity.this).load(mList.get(position).Img).error(R.mipmap.default_icon).placeholder(R.mipmap.default_icon).into(new GlideDrawableImageViewTarget(photoView) {
                @SuppressWarnings("unchecked")
				@Override
                public void onResourceReady(GlideDrawable drawable, @SuppressWarnings("rawtypes") GlideAnimation anim) {
                    super.onResourceReady(drawable, anim);
                    progressBar.setVisibility(View.GONE);
                }
            });
            container.addView(view); // 将图片增加到ViewPager
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
