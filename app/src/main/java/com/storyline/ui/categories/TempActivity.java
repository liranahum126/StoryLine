package com.storyline.ui.categories;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.storyline.R;
import com.storyline.ui.chat.ChatFragment;

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

        ChatFragment chatFragment = ChatFragment.newInstance();

        openFragment(chatFragment);
    }

    private void openFragment(ChatFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
