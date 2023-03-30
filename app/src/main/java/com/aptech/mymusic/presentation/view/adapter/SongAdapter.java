package com.aptech.mymusic.presentation.view.adapter;


import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.application.App;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.utils.ToastUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_SUGGEST = 1;
    public static final int TYPE_SEARCH = 2;
    public static final int TYPE_FAVORITE = 3;

    private Toast mToast;
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

    public List<SongModel> getListSong() {
        return mListSong;
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
        if (mType == TYPE_SEARCH || mType == TYPE_FAVORITE) {
            holder.itemView.setBackgroundResource(R.drawable.custom_background_song_item_dark);
            holder.tvSongName.setTextColor(Color.BLACK);
            holder.tvSingerName.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.black_70));
            holder.imgAdd.setColorFilter(Color.BLACK);
            holder.imgAdd.setVisibility(View.GONE);
            holder.imgMenu.setVisibility(View.GONE);
            holder.imgLike.setColorFilter(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
        }

        boolean isSongFavor = MusicServiceHelper.getMusicPreference().isFavorite(song);
        if (isSongFavor) {
            holder.imgLike.setImageResource(R.drawable.ic_heart_fill);
        } else {
            holder.imgLike.setImageResource(R.drawable.ic_heart);
        }

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
                notifyItemRemoved(holder.getAdapterPosition());
            }
        });
        holder.imgLike.setOnClickListener(view -> {
            if (mItemClickedListener != null) {
                MusicServiceHelper.getMusicPreference().toggleFavorSong(song);
                notifyItemChanged(holder.getAdapterPosition());
            }
            boolean isFavor = MusicServiceHelper.getMusicPreference().isFavorite(song);
            if (isFavor) {
                showToast("Added to favorite");
            } else {
                showToast("Removed from favorite");
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
        ImageView imgAdd, imgMenu, imgLike;
        TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            imgThumb = new WeakReference<>(itemView.findViewById(R.id.img_thumb));
            imgMenu = itemView.findViewById(R.id.img_menu_song);
            imgAdd = itemView.findViewById(R.id.img_add_song);
            imgLike = itemView.findViewById(R.id.img_like);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
        }
    }

    void showToast(String mess) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = ToastUtils.makeInfoToast(App.getInstance(), Toast.LENGTH_SHORT, mess, true);
        mToast.show();
    }
}
