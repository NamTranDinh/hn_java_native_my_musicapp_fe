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
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.activity.ISendDataToDetail;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SUGGEST = 1;

    private final Context context;
    private final List<SongModel> mListSong;
    private final int type;
    private ISendDataToDetail mISendDataToDetail;
    private IOnClickSongItem mIOnClickSongItem;
    private IOnClickAddSong mIOnClickAddSong;

    public SongAdapter(Context context, List<SongModel> mListSong) {
        this(context, mListSong, TYPE_NORMAL);
    }

    public SongAdapter(Context context, List<SongModel> mListSong, int type) {
        this.context = context;
        this.mListSong = mListSong;
        this.type = type;
        if (context instanceof ISendDataToDetail) {
            mISendDataToDetail = (ISendDataToDetail) context;
        }
        if (context instanceof PlayMusicActivity) {
            mIOnClickSongItem = (IOnClickSongItem) context;
            mIOnClickAddSong = (IOnClickAddSong) context;
        }
    }

    public interface IOnClickSongItem {
        void onClickSongItem(SongModel song);
    }

    public interface IOnClickAddSong {
        void onClickAddSong(SongModel song);
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = mListSong.get(position);
        if (song == null) {
            return;
        }
        Glide.with(context).load(song.getImageUrl()).placeholder(R.drawable.ic_logo).into(holder.imgThumb);
        holder.tvSongName.setText(song.getName());
        holder.tvSingerName.setText(song.getSingerName());

        if (mIOnClickSongItem != null) {
            holder.itemView.setOnClickListener(view -> mIOnClickSongItem.onClickSongItem(song));
        }

        if (mISendDataToDetail != null) {
            holder.itemView.setOnClickListener(view -> mISendDataToDetail.sendDataListener(mListSong, position));
        }

        if (type == TYPE_SUGGEST) {
            holder.imgAdd.setVisibility(View.VISIBLE);
            holder.imgAdd.setOnClickListener(view -> {
                mIOnClickAddSong.onClickAddSong(song);
                mListSong.remove(song);
                notifyItemRemoved(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListSong != null) {
            return mListSong.size();
        }
        return 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb, imgAdd, imgMenu;
        TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = itemView.findViewById(R.id.img_thumb);
            imgMenu = itemView.findViewById(R.id.img_menu_song);
            imgAdd = itemView.findViewById(R.id.img_add_song);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
        }
    }
}
