package com.storyline.ui.categories.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.storyline.ui.categories.CategoryLineFragment;

import java.util.List;

public class CategoryLineAdapter extends FragmentPagerAdapter {

    private List<String> categoryLines;

    public CategoryLineAdapter(FragmentManager fm, List<String> categoryLines) {
        super(fm);
        this.categoryLines = categoryLines;
    }

    @Override
    public Fragment getItem(int position) {
        String categoryLine = categoryLines.get(position);
        return CategoryLineFragment.newInstance(categoryLine);
    }

    @Override
    public int getCount() {
        return categoryLines.size();
    }
}