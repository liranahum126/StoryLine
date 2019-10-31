package com.storyline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.storyline.ui.FullStoryDialogFragment;
import com.storyline.ui.categories.listeners.StartStoryListener;
import com.storyline.ui.categories.model.Category;
import com.storyline.ui.categories.providers.CategoriesProvider;
import com.storyline.ui.categories.views.CategoriesFragment;
import com.storyline.ui.users.adapter.UsersFirebaseRecyclerAdapter;

public class FriendsListFragment extends Fragment {
    private DatabaseReference mFirebaseDatabaseReference;
    private RecyclerView recyclerView;
    private UsersFirebaseRecyclerAdapter
            mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    private String theStory;
    //  private ProgressBar mProgressBar;

    private UsersFirebaseRecyclerAdapter.UserViewHolderListener userViewHolderListener = new UsersFirebaseRecyclerAdapter.UserViewHolderListener() {
        @Override
        public void moveToChatFragment(User friendUser) {
            ChatFragment chatFragment = new ChatFragment();
            if (friendUser.userId.compareTo(mFirebaseUser.getUid()) > 0) {
                chatFragment.setMESSAGES_CHILD(friendUser.userId + mFirebaseUser.getUid());
            } else {
                chatFragment.setMESSAGES_CHILD(mFirebaseUser.getUid() + friendUser.userId);
            }
            ((MainStoriesActivity) getActivity()).replaceFragmentInActivity(chatFragment);
        }

        @Override
        public void moveToCategoryFragment(User friendUser) {
            CategoriesFragment categoriesFragment = CategoriesFragment.newInstance(CategoriesProvider.getCategories());
            categoriesFragment.setFriendId(friendUser.userId);
            categoriesFragment.setOnStartStoryListener(new StartStoryListener() {
                @Override
                public void onStartStoryClicked(Category category, String line) {
                    Log.e(getClass().getSimpleName(), "onStartStoryClicked: category name: " + category.getCategoryName());
                    Log.e(getClass().getSimpleName(), "onStartStoryClicked: category line: " + line);
                }
            });

            ((MainStoriesActivity) getActivity()).replaceFragmentInActivity(categoriesFragment);
        }

        @Override
        public void showFullStoryDialogFragment(User friendUser) {
            String fullStoryId;
            if (friendUser.userId.compareTo(mFirebaseUser.getUid()) > 0) {
                fullStoryId = friendUser.userId + mFirebaseUser.getUid();
            } else {
                fullStoryId = mFirebaseUser.getUid() + friendUser.userId;
            }

            FriendsListFragment.this.theStory = friendUser.getInterActiveFriendList().get(mFirebaseUser.getUid()).getFullStory();
            FullStoryDialogFragment fullStoryDialogFragment = FullStoryDialogFragment.newInstance(FriendsListFragment.this.theStory);
            fullStoryDialogFragment.show(getChildFragmentManager(), null);

            //remove chat and interaction
            FirebaseDatabase.getInstance().getReference().child(fullStoryId).removeValue();
            FirebaseDatabase.getInstance().getReference().child("users").child(mFirebaseUser.getUid()).child("interActiveFriendList").child(friendUser.getUserId()).removeValue();
            FirebaseDatabase.getInstance().getReference().child("users").child(friendUser.getUserId()).child("interActiveFriendList").child(mFirebaseUser.getUid()).removeValue();

        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getUsers();

    }

    private void getUsers() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<User> parser = new SnapshotParser<User>() {
            @Override
            public User parseSnapshot(DataSnapshot dataSnapshot) {
                Log.i("1111111111", "111111111");
                User user = dataSnapshot.getValue(User.class);
//                if (user != null) {
//                    user.setUserId(dataSnapshot.getKey());
//                }
                return user;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child("users");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(messagesRef, parser)
                        .build();

        mFirebaseAdapter = new UsersFirebaseRecyclerAdapter(options, mFirebaseUser, theStory, userViewHolderListener);

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.i("444444444444444", "444444444444444444444444");
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        listenToMyInteractiveList();

        recyclerView.setAdapter(mFirebaseAdapter);
    }

    private void listenToMyInteractiveList() {
        mFirebaseDatabaseReference.child("users").child(mFirebaseUser.getUid()).child("interActiveFriendList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mFirebaseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initViews(View rootView) {
        // mProgressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }
}
