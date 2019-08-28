package com.storyline.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.storyline.R;

public class FullStoryDialogFragment extends AbsFullScreenDialogFragment {

    public static final String TAG = FullStoryDialogFragment.class.getSimpleName();

    private static final String FULL_STORY_KEY = "full_story_key";


    private TextView fullStoryTv;

    public static FullStoryDialogFragment newInstance(String fullStory) {
        Bundle args = new Bundle();
        args.putString(FULL_STORY_KEY, fullStory);
        FullStoryDialogFragment fragment = new FullStoryDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_full_story, container, false);

        fullStoryTv = (TextView) view.findViewById(R.id.text_view_story_line);
        fullStoryTv.setText(getArguments().getString(FULL_STORY_KEY));

        return view;
    }

}
