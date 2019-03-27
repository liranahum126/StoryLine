package com.storyline.ui.categories.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.storyline.ui.categories.views.CategoryFragment;
import com.storyline.ui.categories.model.Category;

import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter {

    private List<Category> categories;
    private Fragment[] fragments;

    public CategoryAdapter(FragmentManager fm, List<Category> categories) {
        super(fm);
        this.categories = categories;
        fragments = new Fragment[categories.size()];
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            Category category = categories.get(position);
            fragments[position] = CategoryFragment.newInstance(category);
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}