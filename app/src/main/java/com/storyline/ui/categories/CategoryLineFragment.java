package com.storyline.ui.categories;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.storyline.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryLineFragment extends Fragment {

    private static String CATEGORY_LINE_STRING = "CATEGORY_LINE_STRING";

    public static CategoryLineFragment newInstance(String categoryLine) {
        Bundle args = new Bundle();
        args.putString(CATEGORY_LINE_STRING, categoryLine);
        CategoryLineFragment fragment = new CategoryLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_line, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = view.findViewById(R.id.category_line_text_view);
        String categoryLine = getCategoryLineFromBundle();
        if (!TextUtils.isEmpty(categoryLine)) {
            textView.setText(categoryLine);
        }
    }

    private @Nullable String getCategoryLineFromBundle() {
        Bundle bundle = getArguments();
        if (bundle == null) return null;

        return bundle.getString(CATEGORY_LINE_STRING);
    }
}
