package com.storyline.ui.categories;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.storyline.R;
import com.storyline.ui.categories.listeners.StartStoryListener;
import com.storyline.ui.categories.model.Category;
import com.storyline.ui.categories.providers.CategoriesProvider;
import com.storyline.ui.categories.views.CategoriesFragment;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        CategoriesFragment categoriesFragment = CategoriesFragment.newInstance(CategoriesProvider.getCategories());
        categoriesFragment.setOnStartStoryListener(new StartStoryListener() {
            @Override
            public void onStartStoryClicked(Category category, String line) {
                Log.e(getClass().getSimpleName(), "onStartStoryClicked: category name: " + category.getCategoryName());
                Log.e(getClass().getSimpleName(), "onStartStoryClicked: category line: " + line);
            }
        });

        openFragment(categoriesFragment);
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
