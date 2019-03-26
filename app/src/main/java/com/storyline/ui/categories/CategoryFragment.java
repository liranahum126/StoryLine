package com.storyline.ui.categories;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.storyline.R;
import com.storyline.ui.categories.adapter.CategoryLineAdapter;
import com.storyline.ui.categories.customs.WrapContentViewPager;
import com.storyline.ui.categories.model.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment implements View.OnClickListener {

    private static String CATEGORY_OBJECT = "CATEGORY_OBJECT";

    public static CategoryFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putParcelable(CATEGORY_OBJECT, category);
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView categoryPictureImageView;
    private TextView categoryNameTextView;
    private WrapContentViewPager categoryLinesViewPager;
    private ImageButton forwardButton;
    private ImageButton backwardButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forwardButton = view.findViewById(R.id.arrow_forward_image_button);
        backwardButton = view.findViewById(R.id.arrow_backward_image_button);
        categoryPictureImageView = view.findViewById(R.id.category_picture_image_view);
        categoryNameTextView = view.findViewById(R.id.category_name_text_view);
        categoryLinesViewPager = view.findViewById(R.id.category_line_view_pager);

        forwardButton.setOnClickListener(this);
        backwardButton.setOnClickListener(this);

        Category category = getCategoryFromBundle();
        if (category != null) {
            initCategory(category);
        }
    }

    private void initCategory(Category category) {
        // set category image
        if (category.getCategoryImageId() != 0) {
            categoryPictureImageView.setImageDrawable(ContextCompat.getDrawable(getContext(), category.getCategoryImageId()));
        }

        // set category name
        if (!TextUtils.isEmpty(category.getCategoryName())) {
            categoryNameTextView.setText(category.getCategoryName());
        }

        CategoryLineAdapter categoryLineAdapter = new CategoryLineAdapter(getChildFragmentManager() ,category.getCategoryLines());
        categoryLinesViewPager.setAdapter(categoryLineAdapter);
    }

    @Nullable
    private Category getCategoryFromBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) return null;

        return bundle.getParcelable(CATEGORY_OBJECT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arrow_forward_image_button:
                categoryLinesViewPager.setCurrentItem(categoryLinesViewPager.getCurrentItem() - 1, true);
                break;

            case R.id.arrow_backward_image_button:
                categoryLinesViewPager.setCurrentItem(categoryLinesViewPager.getCurrentItem() + 1, true);
                break;
        }
    }
}
