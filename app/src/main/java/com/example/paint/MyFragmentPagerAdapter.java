package com.example.paint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment>fragments;
    public MyFragmentPagerAdapter(FragmentManager fm,List<Fragment>fragments){
        super(fm);
        this.fragments=fragments;
    }
    @Override
    public int getCount(){
        return  fragments.size();
    }
    @Override
    public Fragment getItem(int position){
        return fragments.get(position);
    }
}
