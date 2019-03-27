package com.storyline.ui.categories.views;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.storyline.R;
import com.storyline.ui.categories.adapter.CategoryAdapter;
import com.storyline.ui.categories.listeners.StartStoryListener;
import com.storyline.ui.categories.model.Category;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener {

    private static final String CATEGORY_LIST = "CATEGORY_LIST";

    private ViewPager categoriesViewPager;
    private StartStoryListener startStoryListener;
    private Button startStoryButton;

    private CategoryAdapter categoryAdapter;

    public static CategoriesFragment newInstance(ArrayList<Category> categories) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(CATEGORY_LIST, categories);
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoriesViewPager = view.findViewById(R.id.categories_view_pager);
        startStoryButton = view.findViewById(R.id.start_story_button);

        startStoryButton.setOnClickListener(this);

        initPagerSettings(categoriesViewPager);

        categoryAdapter = new CategoryAdapter(getChildFragmentManager(), getCategoriesFromBundle());
        categoriesViewPager.setAdapter(categoryAdapter);
    }

    private void initPagerSettings(final ViewPager viewPager) {
        // clipping should be off on the pager for its children so that they can scale out of bounds.
        viewPager.setClipChildren(false);
        viewPager.setClipToPadding(false);
        // to avoid fade effect at the end of the page
        viewPager.setOverScrollMode(2);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPadding(dp2px(getContext().getResources(), 34), 0, dp2px(getContext().getResources(), 34), 0);
        viewPager.setPageMargin(dp2px(getContext().getResources(), 20));
    }

    @Nullable
    private List<Category> getCategoriesFromBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) return null;

        return bundle.getParcelableArrayList(CATEGORY_LIST);
    }

    private int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }

    public void setOnStartStoryListener(StartStoryListener startStoryListener) {
        this.startStoryListener = startStoryListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_story_button:
                if (startStoryListener != null) {
                    Category category = getCategoriesFromBundle().get(categoriesViewPager.getCurrentItem());
                    Fragment fragment = categoryAdapter.getItem(categoriesViewPager.getCurrentItem());
                    if (fragment instanceof CategoryFragment) {
                        CategoryFragment categoryFragment = (CategoryFragment) fragment;
                        String categoryLine = categoryFragment.getChosenCategoryLine();
                        startStoryListener.onStartStoryClicked(category, categoryLine);
                    }
                }
                break;
        }
    }
}
