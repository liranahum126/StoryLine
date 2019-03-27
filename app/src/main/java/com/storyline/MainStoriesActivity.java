package com.storyline;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainStoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_stories);
        replaceFragmentInActivity(new FriendsListFragment());
    }

    public void replaceFragmentInActivity(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_container, fragment).addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void moveToFriendsListFragment(){
        replaceFragmentInActivity(new FriendsListFragment());
    }
}
