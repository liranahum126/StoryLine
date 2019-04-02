package com.storyline.ui.fullstory;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.storyline.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullStoryFragment extends Fragment {

    private static final String STORY_LINE_ARRAY_BUNDLE = "STORY_LINE_ARRAY_BUNDLE";
    private static final String CATEGORY_NAME_BUNDLE = "CATEGORY_NAME_BUNDLE";

    private LinearLayout storyLinesLinearLayout;

    public static FullStoryFragment newInstance(ArrayList<String> storyLinesArray, String categoryName) {
        Bundle args = new Bundle();
        args.putStringArrayList(STORY_LINE_ARRAY_BUNDLE, storyLinesArray);
        args.putString(CATEGORY_NAME_BUNDLE, categoryName);
        FullStoryFragment fragment = new FullStoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_story, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        storyLinesLinearLayout = view.findViewById(R.id.story_lines_linearlayout);

        int iconResourceId = getCategoryIconFromCategoryName(getCategoryName());
        initLinearLayout(getStoryLinesArray(), iconResourceId);
    }

    private void initLinearLayout(List<String> storyLinesArray, int iconResourceId) {
        for (int position = 0; position < storyLinesArray.size(); position++) {
            String line = storyLinesArray.get(position);

            View storyLineItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_story_line, null, false);

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 100, 0, 0);
            storyLineItemView.setLayoutParams(params);

            TextView storyLineTextView = storyLineItemView.findViewById(R.id.line_text_view);

            storyLineTextView.setText(line);

            if (position == 0) {
                ImageView logoImageView = storyLineItemView.findViewById(R.id.logo_image_view);
                logoImageView.setImageResource(iconResourceId);
                logoImageView.setVisibility(View.VISIBLE);
            }

            storyLinesLinearLayout.addView(storyLineItemView);
        }
    }

    @Nullable
    private ArrayList<String> getStoryLinesArray() {
        Bundle bundle = getArguments();
        if (bundle == null) return null;

        return bundle.getStringArrayList(STORY_LINE_ARRAY_BUNDLE);
    }

    @Nullable
    private String getCategoryName() {
        Bundle bundle = getArguments();
        if (bundle == null) return null;

        return bundle.getString(CATEGORY_NAME_BUNDLE);
    }

    private int getCategoryIconFromCategoryName(String categoryName) {
        return R.drawable.storyline_story_adventure;
    }
}
