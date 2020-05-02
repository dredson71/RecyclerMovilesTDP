package com.example.reciclemosdemo.Grafico;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class ViewTrendingAdapter extends FragmentStatePagerAdapter {

    int tabCount;
    private String[] tabTitles = new String[]{"Semana", "Mes", "Record"};

    public ViewTrendingAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount=tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                WeekFragment tab1 = new WeekFragment();
                return tab1;
            case 1:
                YearFragment tab2 = new YearFragment();
                return tab2;
            case 2:
                MonthFragment tab3 = new MonthFragment();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabCount;
    }


}
