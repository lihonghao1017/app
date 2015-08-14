package com.jin91.preciousmetal.ui.price;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jin91.preciousmetal.ui.base.BaseFragment;

/**
 * Created by lijinhua on 2015/5/25.
 * 交易的fragment
 */
public class TradeFragment extends BaseFragment {

    public TradeFragment() {
    }

    public static TradeFragment newInstance() {
        return new TradeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        TextView textView = new TextView(mContext);
        return textView;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void setTitleNav() {

    }
}
