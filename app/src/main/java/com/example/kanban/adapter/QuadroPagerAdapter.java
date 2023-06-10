package com.example.kanban.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.kanban.fragments.FragmentoAFazer;
import com.example.kanban.fragments.FragmentoFazendo;
import com.example.kanban.fragments.FragmentoFeito;

public class QuadroPagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_TABS = 3;
    private Activity mActivity;

    public QuadroPagerAdapter(FragmentManager fm, Activity activity){
        super(fm);
        mActivity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new FragmentoAFazer(mActivity);
            case 1:
                return new FragmentoFazendo(mActivity);
            case 2:
                return new FragmentoFeito(mActivity);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}
