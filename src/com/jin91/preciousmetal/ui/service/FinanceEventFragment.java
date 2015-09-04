package com.jin91.preciousmetal.ui.service;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jin91.preciousmetal.R;
import com.jin91.preciousmetal.adapter.FinanceEventAdapter;
import com.jin91.preciousmetal.common.Config;
import com.jin91.preciousmetal.common.api.entity.FinanceCalen;
import com.jin91.preciousmetal.common.api.entity.FinanceEvent;

public class FinanceEventFragment extends Fragment implements OnItemClickListener{
	private ListView lv;
	public FinanceEventAdapter adapter;
	private FinanceCalenActivity activity;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity = (FinanceCalenActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.financeevent_fragment_layout, null);
		lv = (ListView) v.findViewById(R.id.FinanceEventFragment_ListView);
		adapter=new FinanceEventAdapter(activity.Events, activity);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		return v;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FinanceEvent financeCalen = activity.Events.get(position);
		activity.financeCalenPre.shareDialog(activity, Config.JIN91_HOST, getString(R.string.finance_calcenda), financeCalen.Event);
	}
}
