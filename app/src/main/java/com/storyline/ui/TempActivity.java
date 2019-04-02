package com.storyline.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.storyline.R;
import com.storyline.ui.chat.ChatFragment;
import com.storyline.ui.fullstory.FullStoryFragment;

import java.util.ArrayList;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

       /* CategoriesFragment categoriesFragment = CategoriesFragment.newInstance(CategoriesProvider.getCategories());
        categoriesFragment.setOnStartStoryListener(new StartStoryListener() {
            @Override
            public void onStartStoryClicked(Category category, String line) {
                Log.e(getClass().getSimpleName(), "onStartStoryClicked: category name: " + category.getCategoryName());
                Log.e(getClass().getSimpleName(), "onStartStoryClicked: category line: " + line);
            }
        });*/

//        ChatFragment chatFragment = ChatFragment.newInstance("Some first line", "lastWord");

        ArrayList<String> strings = new ArrayList<>();
        strings.add("advadvdavdv");
        strings.add("advadvdavdv");
        strings.add("advadvdavdv");
        strings.add("advadvdavdv");
        strings.add("advadvdavdv");
        strings.add("advadvdavdv");

        FullStoryFragment fullStoryFragment = FullStoryFragment.newInstance(strings, "some category name");

        openFragment(fullStoryFragment);
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
