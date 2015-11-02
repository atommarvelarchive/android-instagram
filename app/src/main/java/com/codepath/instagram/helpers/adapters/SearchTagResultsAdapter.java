package com.codepath.instagram.helpers.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramSearchTag;

import java.util.List;

/**
 * Created by araiff on 11/1/15.
 */
public class SearchTagResultsAdapter extends RecyclerView.Adapter<SearchTagResultsAdapter.ViewHolder> {

    private List<InstagramSearchTag> instagramSearchTags;

    public SearchTagResultsAdapter(List<InstagramSearchTag> tags) {
        instagramSearchTags = tags;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View postView = inflater.inflate(R.layout.layout_item_tag, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Context context = viewHolder.tvTagName.getContext();
        InstagramSearchTag tag = instagramSearchTags.get(position);

        // Set item views based on the data model
        // Tag Name
        viewHolder.tvTagName.setText(tag.tag);
        // Post Count
        viewHolder.tvTagCount.setText(Utils.formatNumberForDisplay(tag.count) + " posts");
    }

    @Override
    public int getItemCount() {
        return instagramSearchTags.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvTagName;
        public TextView tvTagCount;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTagName = (TextView) itemView.findViewById(R.id.tvTagName);
            tvTagCount = (TextView) itemView.findViewById(R.id.tvTagCount);
        }
    }
}
