package com.jin91.preciousmetal.customview;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.common.api.entity.ListDialogEntity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by lijinhua on 2015/5/19.
 */
@SuppressLint("NewApi")
public class ListViewDialog extends Dialog {


    private Context mContext;
    private ListDialogItemListener listener;
    private int selectPosition;
    private List<ListDialogEntity> mList;
    private ListDialogAdapter adapter;

    public ListViewDialog(Context context) {
        super(context);

    }

    public ListViewDialog(Context context, List<ListDialogEntity> mList, ListDialogItemListener listener, int defaultPosition) {
        super(context, R.style.ListDialogTheme);
        this.mContext = context;
        this.listener = listener;
        this.mList = mList;
        this.selectPosition = defaultPosition;
        initView();
//        Window window = this.getWindow();

//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.gravity = Gravity.CENTER;
//        window.setAttributes(lp);
//        DisplayMetrics metrics = getDisplayMetrics(context);
//        int height = metrics.heightPixels - ScreenUtil.dip2px(mContext, 100);
//        int width = metrics.widthPixels - ScreenUtil.dip2px(mContext, 20);
//        window.setLayout(width, height);
//        window.setGravity(Gravity.CENTER); //此处可以设置dialog显示的位置

//        WindowManager.LayoutParams params = window.getAttributes();
//        params.x = -ScreenUtil.dip2px(mContext, 100);//设置x坐标
//        params.y = -ScreenUtil.dip2px(mContext, 20);//设置y坐标
//        window.setAttributes(params);
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.from(mContext).inflate(R.layout.listview, null);
        ListView mListView = (ListView) view.findViewById(R.id.mListView);
        adapter = new ListDialogAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                if (listener != null) {
                    listener.listItemListener(position);
                }
            }
        });
        setContentView(view);
    }

    public DisplayMetrics getDisplayMetrics(Context context) {
        WindowManager manager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);//可显示区域
        return dm;
    }

    public void setSelectPosition(int position) {
        this.selectPosition = position;
        adapter.notifyDataSetChanged();
    }

    public interface ListDialogItemListener {
        void listItemListener(int position);
    }

    class ListDialogAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_dialog_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == selectPosition) {
                holder.rlListdialog.setBackgroundColor(mContext.getResources().getColor(R.color.nav_title_bg));
                holder.ivListdialogSelected.setVisibility(View.VISIBLE);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.rlListdialog.setBackground(mContext.getResources().getDrawable(R.drawable.listview_item_selector));
                } else {
                    holder.rlListdialog.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.listview_item_selector));
                }
                holder.ivListdialogSelected.setVisibility(View.INVISIBLE);
            }
            holder.tvListdialogItem.setText(mList.get(position).key);
            return convertView;
        }


    }

    static class ViewHolder {
        @ViewInject(R.id.tv_listdialog_item)
        TextView tvListdialogItem;
        @ViewInject(R.id.iv_listdialog_selected)
        ImageView ivListdialogSelected;
        @ViewInject(R.id.rl_listdialog)
        RelativeLayout rlListdialog;

        ViewHolder(View view) {
        	ViewUtils.inject(this, view);
        }
    }
}
