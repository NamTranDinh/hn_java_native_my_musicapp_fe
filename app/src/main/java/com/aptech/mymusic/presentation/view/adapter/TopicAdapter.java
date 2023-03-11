package com.aptech.mymusic.presentation.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.utils.GlideUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private final List<TopicModel> topicModelList;
    private final ICardListener mICardListener;

    public TopicAdapter(List<TopicModel> topicModelList, ICardListener mICardListener) {
        this.topicModelList = topicModelList;
        this.mICardListener = mICardListener;
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
        GlideUtils.load(topic.getImageUrl(), holder.imgThumb.get());
        holder.tvName.setText(topic.getName());
        if (mICardListener != null) {
            holder.itemView.setOnClickListener(view -> mICardListener.onCardClicked(topic, ICardListener.Action.SHOW_MODAL));
        }
    }

    @Override
    public int getItemCount() {
        if (topicModelList != null) {
            return topicModelList.size();
        }
        return 0;
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {
        WeakReference<ImageView> imgThumb;
        TextView tvName;

        public TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = new WeakReference<>(itemView.findViewById(R.id.img_thumb));
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
