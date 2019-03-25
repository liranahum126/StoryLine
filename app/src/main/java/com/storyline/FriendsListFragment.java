package com.storyline;

import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListFragment extends Fragment {
    private DatabaseReference mFirebaseDatabaseReference;
    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<User, UserViewHolder>
            mFirebaseAdapter;
    private LinearLayoutManager mLinearLayoutManager;
  //  private ProgressBar mProgressBar;


    public static class UserViewHolder extends RecyclerView.ViewHolder {
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

    private void getUsers(){

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<User> parser = new SnapshotParser<User>() {
            @Override
            public User parseSnapshot(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    user.setUserId(dataSnapshot.getKey());
                }
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
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new UserViewHolder(inflater.inflate(R.layout.item_message, viewGroup,
                        false));
            }

            @Override
            protected void onBindViewHolder(final UserViewHolder viewHolder,
                                            int position,
                                            User user) {
              //  mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                if (user.getName() != null) {
                    viewHolder.messageTextView.setText(user.getName());
                    viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
                    viewHolder.messageImageView.setVisibility(ImageView.GONE);
                }

                viewHolder.messengerTextView.setText(user.getUserEmail());
                if (user.getPhotoUrl() == null) {
                    viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                            R.drawable.ic_account_circle_black_36dp));
                } else {
                    Glide.with(getActivity())
                            .load(user.getPhotoUrl())
                            .into(viewHolder.messengerImageView);
                }

            }
        };

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
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setAdapter(mFirebaseAdapter);
    }


    private void initViews(View rootView) {
       // mProgressBar = rootView.findViewById(R.id.progressBar);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mLinearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayoutManager);
    }

}
