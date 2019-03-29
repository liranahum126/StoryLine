package com.storyline.ui.fullstory;


import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storyline.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullStoryFragment extends Fragment {

    private static final String STORY_LINE_ARRAY_BUNDLE = "STORY_LINE_ARRAY_BUNDLE";
    private static final String CATEGORY_NAME_BUNDLE = "CATEGORY_NAME_BUNDLE";

    private RecyclerView storyLinesRecyclerView;
    private StoryLineAdapter storyLineAdapter;

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
        storyLinesRecyclerView = view.findViewById(R.id.story_lines_recycler_view);
        storyLinesRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

//        storyLineAdapter = new StoryLineAdapter(getStoryLinesArray(),
//                getCategoryIconFromCategoryName());
        storyLinesRecyclerView.setAdapter(storyLineAdapter);

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
