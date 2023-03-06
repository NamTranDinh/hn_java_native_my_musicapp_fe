package com.aptech.mymusic.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.bumptech.glide.Glide;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private final Context context;
    private final List<TopicModel> topicModelList;

    public TopicAdapter(Context context, List<TopicModel> topicModelList) {
        this.context = context;
        this.topicModelList = topicModelList;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        TopicModel topic = topicModelList.get(position);
        if (topic == null) {
            return;
        }
        Glide.with(context).load(topic.getImageUrl()).placeholder(R.drawable.ic_logo).into(holder.imgThumb);
        Glide.with(context).load(topic.getImageUrl()).into(holder.imgThumb);
        holder.tvName.setText(topic.getName());
    }

    @Override
    public int getItemCount() {
        if (topicModelList != null) {
            return topicModelList.size();
        }
        return 0;
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumb;
        TextView tvName;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_thumb);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
