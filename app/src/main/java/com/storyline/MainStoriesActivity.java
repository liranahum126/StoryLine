package com.storyline;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.storyline.ui.success.SuccessFragment;

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

    public void moveToSuccessFragment(){
        replaceFragmentInActivity(new SuccessFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                FirebaseAuth.getInstance().signOut();
               // Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                //mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivityWithUsernameAndPassword.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
