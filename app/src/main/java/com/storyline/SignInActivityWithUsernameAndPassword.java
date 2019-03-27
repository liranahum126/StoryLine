/**
 * Copyright Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.storyline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.storyline.models.User;

public class SignInActivityWithUsernameAndPassword extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private Button mRegisterButton;
    private Button mLoginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nicknameEditText;

    // Firebase instance variables
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_username_and_password);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        nicknameEditText = findViewById(R.id.nickname_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mRegisterButton = findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        switch (v.getId()) {
            case R.id.register_button:
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    showErrorDialog(getString(R.string.please_fill_all_fields_in_order_to_register));
                    return;
                }

                register(email, password);
                break;

            case R.id.login_button:
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    showErrorDialog(getString(R.string.please_fill_all_fields_in_order_to_login));
                    return;
                }

                login(email, password);
                break;
        }
    }

    private void register(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                addNewUser(new User(user.getUid(), user.getEmail(), nicknameEditText.getText().toString(), ""));
                            }

                            onRegisterSucceed(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            showErrorDialog(task.getException().getMessage());
                        }
                    }
                });
    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onRegisterSucceed(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            showErrorDialog(task.getException().getMessage());
                        }
                    }
                });
    }

    private void onRegisterSucceed(FirebaseUser user) {
        startActivity(new Intent(SignInActivityWithUsernameAndPassword.this, MainStoriesActivity.class));
        finish();
    }

    private void showErrorDialog(@Nullable String errorMessage) {
        if (TextUtils.isEmpty(errorMessage)) return;

        new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setNeutralButton(R.string.ok, null)
                .show();
    }

    private void addNewUser(User user) {
        DatabaseReference mFireBaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messagesRef = mFireBaseDatabaseReference.child("users").child(user.getUserId());
        messagesRef.setValue(user);
    }
}
