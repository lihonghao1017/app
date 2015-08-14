package com.jin91.preciousmetal.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.ShareDialogAdapter;

/**
 * Created by lijinhua on 2015/4/23.
 * 底部弹出的dialog
 */
public class ShareDailog extends Dialog {

    private Context mContext;
    private int[] iconList;
    private int[] nameList;

    private OnClickLinstener linstener;

    public ShareDailog(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param iconList 图标的
     * @param nameList 资源的id
     * @param linstener
     */
    public ShareDailog(Context context, int[] iconList, int[] nameList, OnClickLinstener linstener) {
        super(context, R.style.CustomDialogTheme);
        this.mContext = context;
        this.iconList = iconList;
        this.nameList = nameList;
        this.linstener = linstener;
        initView();
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM); //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.CustomDialogTheme_Anim); //添加动画
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public interface OnClickLinstener {
        void onClick(int position);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.share_dialog, null);
        GridView mGridView = (GridView) view.findViewById(R.id.gv_content);
        RippleView ripp_cancel = (RippleView) view.findViewById(R.id.ripp_cancel);
        ShareDialogAdapter adapter = new ShareDialogAdapter(mContext, iconList, nameList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (linstener != null) {
                    linstener.onClick(position);
                }
            }
        });
        ripp_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);
    }
}
