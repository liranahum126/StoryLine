package com.storyline.ui.fullstory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.storyline.R;

import java.util.List;

public class StoryLineAdapter extends RecyclerView.Adapter<StoryLineAdapter.StoryLineViewHolder> {

    private List<String> storyLinesArray;
    private int iconResourceId;

    public StoryLineAdapter(List<String> storyLinesArray, int iconResoourceId) {
        this.storyLinesArray = storyLinesArray;
        this.iconResourceId = iconResoourceId;
    }

    @NonNull
    @Override
    public StoryLineViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story_line, viewGroup, false);
        return new StoryLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryLineViewHolder storyLineViewHolder, int position) {
        if (position == RecyclerView.NO_POSITION) return;

        String line = storyLinesArray.get(position);

        storyLineViewHolder.storyLineTextView.setText(line);

        if (position == 0) {
            storyLineViewHolder.logoImageView.setImageResource(iconResourceId);
            storyLineViewHolder.logoImageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return storyLinesArray.size();
    }

    class StoryLineViewHolder extends RecyclerView.ViewHolder {

        ImageView logoImageView;
        TextView storyLineTextView;

        public StoryLineViewHolder(@NonNull View itemView) {
            super(itemView);

            logoImageView = itemView.findViewById(R.id.logo_image_view);
            storyLineTextView = itemView.findViewById(R.id.line_text_view);
        }
    }
}
