package com.storyline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.storyline.ui.chat.ChatFragment;

public class TempCategoryFragment extends Fragment {
    Button button;
    String friendId;
    private FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_temp_categories, container, false);
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
    }

    private void initViews(View view) {
        button = view.findViewById(R.id.button);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChatFragment chatFragment = new ChatFragment();
                chatFragment.setOpeningSentance("bla bla vla bla vla bla bla");
                if(friendId.compareTo(mFirebaseUser.getUid()) > 0){
                    chatFragment.setMESSAGES_CHILD(friendId + mFirebaseUser.getUid());
                }else {
                    chatFragment.setMESSAGES_CHILD(mFirebaseUser.getUid() + friendId);
                }
                ((MainStoriesActivity)getActivity()).replaceFragmentInActivity(chatFragment);
            }
        });
    }
}
