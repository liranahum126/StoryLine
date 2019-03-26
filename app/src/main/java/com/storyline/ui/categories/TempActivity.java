package com.storyline.ui.categories;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.storyline.R;
import com.storyline.ui.categories.model.Category;
import com.storyline.ui.categories.providers.CategoriesProvider;

import java.util.ArrayList;
import java.util.List;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        CategoriesFragment categoriesFragment = CategoriesFragment.newInstance(CategoriesProvider.getCategories());

        openFragment(categoriesFragment);
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
