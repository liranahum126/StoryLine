package com.storyline.ui.users.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.storyline.InterActiveFriend;
import com.storyline.R;
import com.storyline.User;
import com.storyline.ui.users.viewholder.UserViewHolder;
import com.storyline.utils.Utils;
import java.util.HashMap;

import static com.storyline.InterActiveFriend.END_GAME;
import static com.storyline.InterActiveFriend.FRIEND_TURN;
import static com.storyline.InterActiveFriend.MY_TURN;

public class UsersFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<User, UserViewHolder> {
    FirebaseUser mFirebaseUser;
    String theStory;
    Context context;

    private UserViewHolderListener userViewHolderListener;


    public interface UserViewHolderListener {
        void moveToChatFragment(User friendUser);

        void moveToCategoryFragment(User friendUser);

        void showFullStoryDialogFragment(User friendUser);

    }

    public UsersFirebaseRecyclerAdapter(@NonNull FirebaseRecyclerOptions options, final FirebaseUser mFirebaseUser,
                                        String theStory, UserViewHolderListener userViewHolderListener) {
        super(options);
        this.mFirebaseUser = mFirebaseUser;
        this.theStory = theStory;
        this.userViewHolderListener = userViewHolderListener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.i("222222222222", "2222222222");
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        return new UserViewHolder(inflater.inflate(R.layout.item_user, viewGroup,
                false));
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {

        final UserViewHolder viewHolder = holder;
        User user = model;


        if (!user.getUserId().equals(mFirebaseUser.getUid())) {
            setItemClick(viewHolder.itemView);
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, (int) Utils.dpTopixel(context, 100)));
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
                            viewHolder.messengerImageView.setImageResource(R.drawable.storyline_btn_play);
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

        } else {
//            myInnerTableUser = user;
            viewHolder.itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 0));
        }
    }

    private void changeFriendTurnColor(View view) {
        view.setEnabled(false);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
    }

    private void changeMyTurnColor(View view) {
        view.setEnabled(true);
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.bright_blue));
    }


    private void setItemClick(final View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User friendUser = (User) itemView.getTag();

                HashMap<String, InterActiveFriend> interActiveFriendHashMap = friendUser.getInterActiveFriendList();

                if (interActiveFriendHashMap != null) {
                    InterActiveFriend interActiveFriend = interActiveFriendHashMap.get(mFirebaseUser.getUid());
                    if (interActiveFriend != null) {
                        if (interActiveFriend.getCurrentGameStatus() != END_GAME) {
                            userViewHolderListener.moveToChatFragment(friendUser);
                        } else {
                            userViewHolderListener.showFullStoryDialogFragment(friendUser);
                        }
                    } else {
                        userViewHolderListener.moveToCategoryFragment(friendUser);
                    }
                } else {
                    userViewHolderListener.moveToCategoryFragment(friendUser);
                }
            }
        });
    }
}
