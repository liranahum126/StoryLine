package com.storyline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.storyline.ui.categories.listeners.StartStoryListener;
import com.storyline.ui.categories.model.Category;
import com.storyline.ui.categories.providers.CategoriesProvider;
import com.storyline.ui.categories.views.CategoriesFragment;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListFragment extends Fragment {
    private DatabaseReference mFirebaseDatabaseReference;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserViewHolder>
            mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
  //  private ProgressBar mProgressBar;

    private User myInnerTableUser;


    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        CircleImageView messengerImageView;

        public UserViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
            messageImageView = (ImageView) itemView.findViewById(R.id.messageImageView);
            messengerTextView = (TextView) itemView.findViewById(R.id.messengerTextView);
            messengerImageView = (CircleImageView) itemView.findViewById(R.id.messengerImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User friendUser = (User)itemView.getTag();

                    HashMap<String,InterActiveFriend> interActiveFriendHashMap =  friendUser.getInterActiveFriendList();

                    if(interActiveFriendHashMap != null) {
                        InterActiveFriend interActiveFriend = interActiveFriendHashMap.get(mFirebaseUser.getUid());
                        if (interActiveFriend != null) {
                            ChatFragment chatFragment = new ChatFragment();

                            if(friendUser.userId.compareTo(mFirebaseUser.getUid()) > 0){
                                chatFragment.setMESSAGES_CHILD(friendUser.userId + mFirebaseUser.getUid());
                            }else {
                                chatFragment.setMESSAGES_CHILD(mFirebaseUser.getUid() + friendUser.userId);
                            }
                            ((MainStoriesActivity)getActivity()).replaceFragmentInActivity(chatFragment);
                        }
                    }else {
//                        TempCategoryFragment tempCategoryFragment = new TempCategoryFragment();
////                        tempCategoryFragment.setFriendId(friendUser.userId);

                        CategoriesFragment categoriesFragment = CategoriesFragment.newInstance(CategoriesProvider.getCategories());
                        categoriesFragment.setFriendId(friendUser.userId);
                        categoriesFragment.setOnStartStoryListener(new StartStoryListener() {
                            @Override
                            public void onStartStoryClicked(Category category, String line) {
                                Log.e(getClass().getSimpleName(), "onStartStoryClicked: category name: " + category.getCategoryName());
                                Log.e(getClass().getSimpleName(), "onStartStoryClicked: category line: " + line);
                            }
                        });

                        ((MainStoriesActivity)getActivity()).replaceFragmentInActivity(categoriesFragment);
                    }

                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        getUsers();

    }


    private void changeFriendTurnColor(View view){
        view.setEnabled(false);
        view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorAccent));
    }

    private void changeMyTurnColor(View view){
        view.setEnabled(true);
        view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
    }

    private void getUsers(){

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<User> parser = new SnapshotParser<User>() {
            @Override
            public User parseSnapshot(DataSnapshot dataSnapshot) {
                Log.i("1111111111","111111111");
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

        mFirebaseAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                Log.i("222222222222","2222222222");
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new UserViewHolder(inflater.inflate(R.layout.item_message, viewGroup,
                        false));
            }

            @Override
            protected void onBindViewHolder(final UserViewHolder viewHolder,
                                            int position,
                                            User user) {
                if (!user.getUserId().equals(mFirebaseUser.getUid())) {
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,RecyclerView.LayoutParams.WRAP_CONTENT));
                    HashMap<String, InterActiveFriend> interActiveFriendHashMap = user.getInterActiveFriendList();

                    if (interActiveFriendHashMap != null) {
                        InterActiveFriend interActiveFriend = interActiveFriendHashMap.get(mFirebaseUser.getUid());
                        if (interActiveFriend != null) {
                            boolean isMyTurn = interActiveFriend.isMyTurn();
                            if (!isMyTurn) {
                                viewHolder.messengerTextView.setText("myTurn");
                                changeMyTurnColor(viewHolder.itemView);
                            } else {
                                viewHolder.messengerTextView.setText("friend turn");
                                changeFriendTurnColor(viewHolder.itemView);
                            }
                        } else {
                            viewHolder.messengerTextView.setText("start game");
                            changeMyTurnColor(viewHolder.itemView);
                        }
                    } else {
                        viewHolder.messengerTextView.setText("start game");
                        changeMyTurnColor(viewHolder.itemView);
                    }

                    //  mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    viewHolder.itemView.setTag(user);
                    if (user.getName() != null) {
                        viewHolder.messageTextView.setText(user.getUserEmail());
                        viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                        viewHolder.messageImageView.setVisibility(ImageView.GONE);
                    }

                    // viewHolder.messengerTextView.setText(user.getUserEmail());
                    if (user.getPhotoUrl() == null) {
                        viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                                R.drawable.ic_account_circle_black_36dp));
                    } else {
                        Glide.with(getActivity())
                                .load(user.getPhotoUrl())
                                .into(viewHolder.messengerImageView);
                    }

                }else{
                    myInnerTableUser = user;
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,0));
                }
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.i("444444444444444","444444444444444444444444");
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


    private void listenToMyInteractiveList(){
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


}
