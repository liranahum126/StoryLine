package com.storyline.ui.chat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.storyline.FriendlyMessage;
import com.storyline.InterActiveFriend;
import com.storyline.MainStoriesActivity;
import com.storyline.R;
import com.storyline.ui.FullStoryDialogFragment;
import com.storyline.ui.categories.model.Category;
import com.storyline.ui.chat.adapter.ChatAdapter;
import com.storyline.utils.FirebaseUtil;

public class ChatFragment extends Fragment {
    private static final String FIRST_LINE_BUNDLE = "FIRST_LINE_BUNDLE";
    private static final String LAST_WORD_BUNDLE = "LAST_WORD_BUNDLE";
    private EditText chatEditText;
    private TextView firstLineTextView;
    private String firstLine;
    private String lastWord;
    private static final String TAG = "ChatFragment";
    public String MESSAGES_CHILD = "";
    private static final int STORY_LENGTH = 7;
    private String mUsername;
    private String mPhotoUrl;
    private ImageButton mSendButton;
    private RecyclerView mMessageRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private EditText mMessageEditText;
    private TextView textViewOpen;
    private FullStoryDialogFragment fullStoryDialogFragment;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private ChatAdapter
            mFirebaseAdapter;
    private String openingSentance;
    private String friendUserId;
    private Category category;
    private int currentCount;

    public static com.storyline.ui.chat.ChatFragment newInstance(String firstLine, String lastWord) {

        Bundle args = new Bundle();
        args.putString(FIRST_LINE_BUNDLE, firstLine);
        args.putString(LAST_WORD_BUNDLE, lastWord);
        com.storyline.ui.chat.ChatFragment fragment = new com.storyline.ui.chat.ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setOpeningSentance(String openingSentance) {
        this.openingSentance = openingSentance;
    }

    public void setMESSAGES_CHILD(String MESSAGES_CHILD) {
        this.MESSAGES_CHILD = MESSAGES_CHILD;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    private void setFriendUserId(){
        friendUserId = MESSAGES_CHILD;
        friendUserId =  friendUserId.replace(mFirebaseUser.getUid(),"");
    }

    @Nullable
    private String getFromBunndle(String key) {
        Bundle bundle = getArguments();
        if (bundle == null) return null;

        return bundle.getString(key);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chatEditText = view.findViewById(R.id.chat_edit_text);
        firstLineTextView = view.findViewById(R.id.first_line_text_view);
        firstLine = getFromBunndle(FIRST_LINE_BUNDLE);
        lastWord = getFromBunndle(LAST_WORD_BUNDLE);
        firstLineTextView.setText(firstLine);
        chatEditText.setHint(lastWord);
        // Initialize ProgressBar and RecyclerView.
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        setFriendUserId();
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mMessageRecyclerView = (RecyclerView) view.findViewById(R.id.messageRecyclerView);
        textViewOpen = view.findViewById(R.id.first_line_text_view);
        textViewOpen.setText(openingSentance);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);

        FirebaseUtil.getUserFromFriendInterActiveFriend(mFirebaseUser,friendUserId,new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                InterActiveFriend interActiveFriend = snapshot.getValue(InterActiveFriend.class);
                if(interActiveFriend != null && !TextUtils.isEmpty(interActiveFriend.lastWord)){
                    textViewOpen.setText("Your line starts with the word " + interActiveFriend.lastWord);
                    currentCount = interActiveFriend.count;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
//
//        // New child entries
//        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//
//        mFirebaseDatabaseReference.child("users").child(friendUserId).child("interActiveFriendList").child(mFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                InterActiveFriend interActiveFriend = snapshot.getValue(InterActiveFriend.class);
//                if(interActiveFriend != null && !TextUtils.isEmpty(interActiveFriend.lastWord)){
//                    textViewOpen.setText("Your line starts with the word " + interActiveFriend.lastWord);
//                     currentCount = interActiveFriend.count;
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        SnapshotParser<FriendlyMessage> parser = new SnapshotParser<FriendlyMessage>() {
            @Override
            public FriendlyMessage parseSnapshot(DataSnapshot dataSnapshot) {
                FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                if (friendlyMessage != null) {
                    friendlyMessage.setId(dataSnapshot.getKey());
                }
                return friendlyMessage;
            }
        };

        DatabaseReference messagesRef = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
        FirebaseRecyclerOptions<FriendlyMessage> options =
                new FirebaseRecyclerOptions.Builder<FriendlyMessage>()
                        .setQuery(messagesRef, parser)
                        .build();

        mFirebaseAdapter = new ChatAdapter(options, getActivity(), new ChatAdapter.ChatAdapterListener() {
            @Override
            public void onBindViewHolderStarted() {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition =
                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    mMessageRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mMessageRecyclerView.setAdapter(mFirebaseAdapter);
        mMessageEditText = (EditText) view.findViewById(R.id.chat_edit_text);
        mMessageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mSendButton = view.findViewById(R.id.send_image_button);

        mUsername = mFirebaseUser.getDisplayName();
        if (mFirebaseUser.getPhotoUrl() != null) {
            mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
        }

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isLastTurn = false;
                    if (mFirebaseAdapter.getItemCount() < STORY_LENGTH - 1) {
                        ((MainStoriesActivity) getActivity()).moveToSuccessFragment();
                    } else {
                        Toast.makeText(getActivity(), "end game", Toast.LENGTH_LONG).show();
                        StringBuilder fullStory = new StringBuilder();
                        for(int i = 0; i< mFirebaseAdapter.getItemCount(); ++i){
                            fullStory.append(mFirebaseAdapter.getItem(i).getText().trim()).append("\n");
                        }
                        fullStory.append(mMessageEditText.getText().toString().trim());
                        Toast.makeText(getActivity(), fullStory.toString(), Toast.LENGTH_LONG).show();
                        fullStoryDialogFragment = FullStoryDialogFragment.newInstance(fullStory.toString());
                        fullStoryDialogFragment.show(getChildFragmentManager(), null);



                       // final DatabaseReference updateTurnsReferance = mFirebaseDatabaseReference.child("users");
                        String lastword = mMessageEditText.getText().toString().trim().substring(mMessageEditText.getText().toString().lastIndexOf(" ") + 1);

                        FirebaseUtil.updateFriendTurn(mFirebaseUser,friendUserId,lastword,currentCount,fullStory.toString());
                        FirebaseUtil.updateMyTurn(mFirebaseUser,friendUserId,lastword,currentCount,fullStory.toString());


//                        //update friends turn
//                        InterActiveFriend interActiveFriendFriendTurn = new InterActiveFriend(mFirebaseUser.getUid(), InterActiveFriend.FRIEND_TURN, lastword, currentCount+1);
//                        interActiveFriendFriendTurn.setFullStory(fullStory.toString());
//                        updateTurnsReferance.child(friendUserId).child("interActiveFriendList").child(mFirebaseUser.getUid()).setValue(interActiveFriendFriendTurn);
//
//                        //update my turn
//                        InterActiveFriend interActiveFriendMyTurn = new InterActiveFriend(friendUserId, InterActiveFriend.END_GAME, lastword,currentCount+1);
//                        interActiveFriendMyTurn.setFullStory(fullStory.toString());
//                        updateTurnsReferance.child(mFirebaseUser.getUid()).child("interActiveFriendList").child(friendUserId).setValue(interActiveFriendMyTurn);

                        isLastTurn = true;
                    }

                    FriendlyMessage friendlyMessage = new
                            FriendlyMessage(mMessageEditText.getText().toString().trim(),
                            "mUsername",
                            mPhotoUrl,
                            null /* no image */,currentCount);
                    friendlyMessage.setId(mFirebaseUser.getUid());

                    if (!TextUtils.isEmpty(openingSentance)) {
                        FriendlyMessage friendlyMessageOpening = new
                                FriendlyMessage(openingSentance,
                                "mUsername",
                                mPhotoUrl,
                                null /* no image */,
                                currentCount);
                        friendlyMessageOpening.setId(friendUserId);
                        mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                                .push().setValue(friendlyMessageOpening);
                        openingSentance = "";
                    }

                    mFirebaseDatabaseReference.child(MESSAGES_CHILD)
                            .push().setValue(friendlyMessage);


                    if (!isLastTurn) {
                        final DatabaseReference updateTurnsReferance = mFirebaseDatabaseReference.child("users");
                        String lastword = mMessageEditText.getText().toString().trim().substring(mMessageEditText.getText().toString().lastIndexOf(" ") + 1);
                        //update friends turn
                        updateTurnsReferance.child(friendUserId).child("interActiveFriendList").child(mFirebaseUser.getUid()).setValue(new InterActiveFriend(mFirebaseUser.getUid(), InterActiveFriend.FRIEND_TURN, lastword,currentCount+1));
                        //update my turn
                        updateTurnsReferance.child(mFirebaseUser.getUid()).child("interActiveFriendList").child(friendUserId).setValue(new InterActiveFriend(friendUserId, InterActiveFriend.MY_TURN, lastword,currentCount+1));
                        mMessageEditText.setText("");
                    }
//                }else{
//                    StringBuilder fullStory = new StringBuilder();
//                    for(int i = 0; i< mFirebaseAdapter.getItemCount() - 1; ++i){
//
//                        fullStory.append(mFirebaseAdapter.getItem(i).getText().trim()).append("\n");
//
//                    }
//                    fullStory.append(mMessageEditText.getText().toString().trim());
//                    Toast.makeText(getActivity(), fullStory.toString(), Toast.LENGTH_LONG).show();
//                }
            }
        });
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
