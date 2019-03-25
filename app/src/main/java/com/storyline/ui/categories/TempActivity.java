package com.storyline.ui.categories;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.storyline.R;
import com.storyline.ui.categories.model.Category;

import java.util.ArrayList;
import java.util.List;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        List<String> categoryLines = new ArrayList<>();
        categoryLines.add("adfadfa");
        categoryLines.add("ngndndgndgndgndgn");
        categoryLines.add("aaa");
        categoryLines.add("bbbbb");

        Category category = new Category("some category name", 0, categoryLines);

        CategoryFragment categoryFragment = CategoryFragment.newInstance(category);

        openFragment(categoryFragment);
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
