package com.jin91.preciousmetal.adapter;

import java.util.ArrayList;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class FragmentsPagerAdapter extends FragmentPagerAdapter
{
	private ArrayList<Fragment> fragmentList;

	public FragmentsPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragmentList)
	{
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragmentList = fragmentList;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}

	@Override
	public Fragment getItem(int index)
	{
		// TODO Auto-generated method stub
		Fragment fragment = fragmentList.get(index);
		return fragment;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return fragmentList.size();
	}

}
