package com.aptech.mymusic.presentation.view.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.utils.GlideUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SUGGEST = 1;

    private final List<SongModel> mListSong;
    private final int mType;
    private final ItemClickedListener mItemClickedListener;

    public SongAdapter(List<SongModel> songs, ItemClickedListener listener) {
        this(songs, listener, TYPE_NORMAL);
    }

    public SongAdapter(List<SongModel> songs, ItemClickedListener listener, int type) {
        this.mListSong = songs;
        this.mItemClickedListener = listener;
        this.mType = type;
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
        GlideUtils.load(song.getImageUrl(), holder.imgThumb.get());
        holder.tvSongName.setText(song.getName());
        holder.tvSingerName.setText(song.getSingerName());

        holder.itemView.setOnClickListener(view -> {
            if (mItemClickedListener != null) {
                mItemClickedListener.onClickedItem(mListSong, song, holder.getAdapterPosition());
            }
        });

        holder.imgAdd.setVisibility(mType == TYPE_SUGGEST ? View.VISIBLE : View.GONE);
        holder.imgAdd.setOnClickListener(view -> {
            if (mItemClickedListener != null) {
                mItemClickedListener.onClickedAdd(song, holder.getAdapterPosition());
                mListSong.remove(song);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListSong != null) {
            return mListSong.size();
        }
        return 0;
    }

    public interface ItemClickedListener {
        void onClickedItem(List<SongModel> songs, SongModel song, int position);

        void onClickedAdd(SongModel song, int position);

    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        WeakReference<ImageView> imgThumb;
        ImageView imgAdd, imgMenu;
        TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = new WeakReference<>(itemView.findViewById(R.id.img_thumb));
            imgMenu = itemView.findViewById(R.id.img_menu_song);
            imgAdd = itemView.findViewById(R.id.img_add_song);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
        }
    }
}
