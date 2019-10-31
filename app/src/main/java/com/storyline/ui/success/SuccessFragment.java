package com.storyline.ui.success;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.storyline.MainStoriesActivity;
import com.storyline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessFragment extends Fragment {

    private final int TIME_TO_EXIT = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_success, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MainStoriesActivity)getActivity()).moveToFriendsListFragment();
            }
        }, 1000 * TIME_TO_EXIT);
    }
}
