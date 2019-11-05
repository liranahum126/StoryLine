package com.storyline.ui.users.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.storyline.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public TextView messageTextView;
    public ImageView messageImageView;
    public TextView messengerTextView;
    public ImageView messengerImageView;
    public ImageView discoverStoryImageView;

    public UserViewHolder(View v){
        super(v);
        messageTextView = itemView.findViewById(R.id.user_name_text_view);
        messageImageView = itemView.findViewById(R.id.user_icon);
        messengerTextView = itemView.findViewById(R.id.story_status_text_view);
        messengerImageView = itemView.findViewById(R.id.story_status_icon_image_view);
        discoverStoryImageView = itemView.findViewById(R.id.discover_story_icon_image_view);
    }
}
