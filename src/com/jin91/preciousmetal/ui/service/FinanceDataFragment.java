package com.jin91.preciousmetal.ui.service;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jin91.preciousmetal.R;

public class FinanceDataFragment extends Fragment {
	private ListView lv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.financedata_fragment_layout, null);
		lv=(ListView) v.findViewById(R.id.FinanceDataFragment_ListView);
		return v;
	}
}
