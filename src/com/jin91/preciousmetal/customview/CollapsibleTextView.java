package com.jin91.preciousmetal.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.ItemMore;
import com.jin91.preciousmetal.util.FaceUtil;

public class CollapsibleTextView extends LinearLayout implements OnClickListener {

	private static final int MAX_LINE = 2;

	public static final int STATE_NONE = 0;
	public static final int expandN = 1;// 收拢的
	public static final int expand = 2;// 展开的

	private TextView tvContent;
	private TextView tvOp;

	private String shrinkup;
	private String zhankai;
	private boolean flag = true;

	public CollapsibleTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CollapsibleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		shrinkup = context.getString(R.string.desc_shrinkup);
		zhankai = context.getString(R.string.desc_spread);
		View view = inflate(context, R.layout.collapsible_textview, this);
		view.setPadding(0, -1, 0, 0);
		tvContent = (TextView) view.findViewById(R.id.tv_text_content);
		tvOp = (TextView) view.findViewById(R.id.tv_text_more);
		tvOp.setText(zhankai);
		tvOp.setOnClickListener(this);
	}

	ItemMore itemMore;


    public final void setDesc(CharSequence charSequence, ItemMore itemMore) {
        tvContent.setText(FaceUtil.textAddFace(getContext(), charSequence.toString()), TextView.BufferType.NORMAL);
        requestLayout();
        this.isFirst = true;
        this.itemMore = itemMore;
    }





	public static final int TEXT = 1;// 纯文本
	public static final int TEXT_FACE = 2;// 包含表情
	public static final int TEXT_COLOR = 3;// 部分变色
	public static final int TEXT_FACE_COLOR = 4;// 包含表情和部分颜色
	public static final int TEXT_TYPE = TEXT;// 默认纯文本

	@Override
	public void onClick(View v) {
		isFirst = false;
		flag = false;
		requestLayout();
	}

	boolean isFirst = false;

	public void setTextColor(int color) {
		tvContent.setTextColor(color);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (flag) {
			if (itemMore == null) {
				return;
			}
			if (itemMore.getState() == 3) {
				if (tvContent.getLineCount() <= MAX_LINE) {
					tvOp.setVisibility(View.GONE);
					tvContent.setMaxLines(MAX_LINE + 1);
					itemMore.setState(STATE_NONE);
				} else {
					post(new InnerRunnable());
				}
			} else {
				post(new InnerRunnable2());
			}
		} else {
			flag = true;
			post(new InnerRunnable3());
		}
	}

	class InnerRunnable implements Runnable {

		@Override
		public void run() {
			tvContent.setMaxLines(MAX_LINE);
			tvOp.setVisibility(View.VISIBLE);
			tvOp.setText(zhankai);
			itemMore.setState(expandN);
		}
	}

	class InnerRunnable2 implements Runnable {

		@Override
		public void run() {
			if (itemMore.getState() == expand) {
				tvContent.setMaxLines(Integer.MAX_VALUE);
				tvOp.setVisibility(View.VISIBLE);
				tvOp.setText(shrinkup);
				itemMore.setState(expand);
			} else if (itemMore.getState() == expandN) {

				tvContent.setMaxLines(MAX_LINE);
				tvOp.setVisibility(View.VISIBLE);
				tvOp.setText(zhankai);
				itemMore.setState(expandN);

			} else if (itemMore.getState() == STATE_NONE) {
				tvOp.setVisibility(View.GONE);
				tvContent.setMaxLines(MAX_LINE + 1);
				itemMore.setState(STATE_NONE);
			}
		}
	}

	class InnerRunnable3 implements Runnable {

		@Override
		public void run() {
			if (itemMore.getState() == expand) {
				tvContent.setMaxLines(MAX_LINE);
				tvOp.setVisibility(View.VISIBLE);
				tvOp.setText(zhankai);
				itemMore.setState(expandN);
			} else if (itemMore.getState() == expandN) {
				tvContent.setMaxLines(Integer.MAX_VALUE);
				tvOp.setVisibility(View.VISIBLE);
				tvOp.setText(shrinkup);
				itemMore.setState(expand);
			} else if (itemMore.getState() == STATE_NONE) {
				tvOp.setVisibility(View.GONE);
				tvContent.setMaxLines(MAX_LINE + 1);
				itemMore.setState(STATE_NONE);
			}
		}
	}

}
