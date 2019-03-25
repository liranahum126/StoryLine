package com.storyline.ui.categories.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.storyline.ui.categories.CategoryFragment;
import com.storyline.ui.categories.model.Category;

import java.util.List;

public class CategoryAdapter extends FragmentPagerAdapter {

    private List<Category> categories;

    public CategoryAdapter(FragmentManager fm, List<Category> categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        Category category = categories.get(position);
        return CategoryFragment.newInstance(category);
    }

    @Override
    public int getCount() {
        return categories.size();
    }
}