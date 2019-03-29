package com.storyline;

import android.content.Context;
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
import com.storyline.ui.FullStoryDialogFragment;
import com.storyline.ui.categories.listeners.StartStoryListener;
import com.storyline.ui.categories.model.Category;
import com.storyline.ui.categories.providers.CategoriesProvider;
import com.storyline.ui.categories.views.CategoriesFragment;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.storyline.InterActiveFriend.END_GAME;
import static com.storyline.InterActiveFriend.FRIEND_TURN;
import static com.storyline.InterActiveFriend.MY_TURN;

public class FriendsListFragment extends Fragment {
    private DatabaseReference mFirebaseDatabaseReference;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserViewHolder>
            mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseAuth mFirebaseAuth;
    public FirebaseUser mFirebaseUser;
    private String theStory;
  //  private ProgressBar mProgressBar;

    private User myInnerTableUser;

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        ImageView messageImageView;
        TextView messengerTextView;
        ImageView messengerImageView;


        public  void moveToCategoryFragment(User friendUser){
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

        public UserViewHolder(View v) {
            super(v);
            messageTextView = (TextView) itemView.findViewById(R.id.user_name_text_view);
            messageImageView = (ImageView) itemView.findViewById(R.id.user_icon);
            messengerTextView = (TextView) itemView.findViewById(R.id.story_status_text_view);
            messengerImageView = (ImageView) itemView.findViewById(R.id.story_status_icon_image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User friendUser = (User)itemView.getTag();

                    HashMap<String,InterActiveFriend> interActiveFriendHashMap =  friendUser.getInterActiveFriendList();

                    if(interActiveFriendHashMap != null) {
                        InterActiveFriend interActiveFriend = interActiveFriendHashMap.get(mFirebaseUser.getUid());
                        if (interActiveFriend != null) {
                            ChatFragment chatFragment = new ChatFragment();

                            String fullStoryId;
                            if(friendUser.userId.compareTo(mFirebaseUser.getUid()) > 0){
                                chatFragment.setMESSAGES_CHILD(friendUser.userId + mFirebaseUser.getUid());
                                fullStoryId = friendUser.userId + mFirebaseUser.getUid();
                            }else {
                                chatFragment.setMESSAGES_CHILD(mFirebaseUser.getUid() + friendUser.userId);
                                fullStoryId = mFirebaseUser.getUid() + friendUser.userId;
                            }
                            if (interActiveFriend.getCurrentGameStatus() != END_GAME) {
                                ((MainStoriesActivity) getActivity()).replaceFragmentInActivity(chatFragment);
                            }else {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(fullStoryId);
                                ref.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                //Get map of users in datasnapshot
                                                story((HashMap<String,Object>) dataSnapshot.getValue());
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                //handle databaseError
                                            }
                                        });
                                FullStoryDialogFragment fullStoryDialogFragment = FullStoryDialogFragment.newInstance(theStory);
                                fullStoryDialogFragment.show(getChildFragmentManager(), null);
                            }
                        }else{
                            moveToCategoryFragment(friendUser);
                        }
                    }else {
                        moveToCategoryFragment(friendUser);
                    }

                }
            });
        }
    }

    private void story(HashMap<String,Object> sentences) {

        ArrayList<String> story = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (HashMap.Entry<String, Object> entry : sentences.entrySet()){

            //Get user map
            HashMap singleUser = (HashMap) entry.getValue();
            //Get phone field and append to list
            story.add((String) singleUser.get("text"));
        }

        theStory = story.toString();
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
        view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
    }

    private void changeMyTurnColor(View view){
        view.setEnabled(true);
        view.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.bright_blue));
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
                return new UserViewHolder(inflater.inflate(R.layout.item_user, viewGroup,
                        false));
            }

            @Override
            protected void onBindViewHolder(final UserViewHolder viewHolder,
                                            int position,
                                            User user) {
                if (!user.getUserId().equals(mFirebaseUser.getUid())) {
                    viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,(int)dpTopixel(getActivity(),100)));
                    HashMap<String, InterActiveFriend> interActiveFriendHashMap = user.getInterActiveFriendList();

                    if (interActiveFriendHashMap != null) {
                        InterActiveFriend interActiveFriend = interActiveFriendHashMap.get(mFirebaseUser.getUid());
                        if (interActiveFriend != null) {
                            @InterActiveFriend.TurnStatus int turn = interActiveFriend.getCurrentGameStatus();
                            switch (turn) {
                                case MY_TURN:
                                viewHolder.messengerTextView.setText("myTurn");
                                viewHolder.messengerImageView.setImageResource(R.drawable.storyline_btn_edit);
                                changeMyTurnColor(viewHolder.itemView);
                                break;
                                case FRIEND_TURN:
                                viewHolder.messengerTextView.setText("friend turn");
                                viewHolder.messengerImageView.setImageResource(R.drawable.storyline_btn_edit);
                                changeFriendTurnColor(viewHolder.itemView);
                                break;
                                case END_GAME:
                                    viewHolder.messengerTextView.setText("discover story");
                                    viewHolder.messengerImageView.setImageResource(R.drawable.storyline_btn_edit);
                                    break;
                            }
                        } else {
                            viewHolder.messengerTextView.setText("start game");
                            viewHolder.messengerImageView.setImageResource(R.drawable.storyline_btn_play);

                            changeMyTurnColor(viewHolder.itemView);
                        }
                    } else {
                        viewHolder.messengerTextView.setText("start game");
                        viewHolder.messengerImageView.setImageResource(R.drawable.storyline_btn_play);
                        changeMyTurnColor(viewHolder.itemView);
                    }

                    //  mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    viewHolder.itemView.setTag(user);
                    if (user.getName() != null) {
                        viewHolder.messageTextView.setText(user.getUserEmail());
                        viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                        viewHolder.messageImageView.setVisibility(ImageView.VISIBLE);
                    }

                    // viewHolder.messengerTextView.setText(user.getUserEmail());
                    if (user.getPhotoUrl() == null) {
//                        viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),
//                                R.drawable.ic_account_circle_black_36dp));
                    } else {
//                        Glide.with(getActivity())
//                                .load(user.getPhotoUrl())
//                                .into(viewHolder.messengerImageView);
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

    public static float dpTopixel(Context c, float dp) {
        float density = c.getResources().getDisplayMetrics().density;
        float pixel = dp * density;
        return pixel;
    }

}
