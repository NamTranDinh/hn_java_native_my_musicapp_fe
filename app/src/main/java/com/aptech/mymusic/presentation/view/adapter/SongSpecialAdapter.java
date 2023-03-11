package com.aptech.mymusic.presentation.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.common.swipe.SwipeRevealLayout;
import com.aptech.mymusic.presentation.view.common.swipe.ViewBinderHelper;
import com.aptech.mymusic.utils.GlideUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class SongSpecialAdapter extends RecyclerView.Adapter<SongSpecialAdapter.SongViewHolder> {

    private final ViewBinderHelper mBinderHelper = new ViewBinderHelper();
    private final IOnClickSongItem mIOnClickSongItem;
    private SongModel mSong;
    private List<SongModel> mListSong;
    private final IOnItemDelete mIOnItemDelete;
    private StartDragListener mDragListener;

    public SongSpecialAdapter(IOnClickSongItem mIOnClickSongItem, IOnItemDelete mIOnItemDelete) {
        this.mIOnClickSongItem = mIOnClickSongItem;
        this.mIOnItemDelete = mIOnItemDelete;
    }

    public interface IOnClickSongItem {
        void onClickSongItem(SongModel song);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(SongModel song, List<SongModel> songs) {
        mSong = song;
        mListSong = songs;
        notifyDataSetChanged();
    }

    public interface StartDragListener {
        void requestDrag(RecyclerView.ViewHolder viewHolder);
    }

    public void setSong(SongModel song) {
        setData(song, mListSong);
    }

    public void setListSong(List<SongModel> mListSong) {
        setData(mSong, mListSong);
    }

    public void setDragListener(StartDragListener dragListener) {
        this.mDragListener = dragListener;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = mListSong.get(position);
        if (song == null) {
            return;
        }
        String id = String.valueOf(song.getId());
        boolean isActive = song.equals(mSong);

        mBinderHelper.bind(holder.swrLayout, id);

        holder.rlItem.setSelected(isActive);
        if (isActive) {
            mBinderHelper.lockSwipe(id);
        } else {
            mBinderHelper.unlockSwipe(id);
        }
        holder.tvSongName.setText(song.getName());
        holder.tvSingerName.setText(song.getSingerName());
        GlideUtils.load(song.getImageUrl(), holder.imgThumb.get());

        if (mIOnClickSongItem != null) {
            holder.rlItem.setOnClickListener(isActive ? null : (view -> mIOnClickSongItem.onClickSongItem(song)));
        }
        if (mIOnItemDelete != null) {
            holder.imgDelete.setOnClickListener(view -> {
                mListSong.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                mIOnItemDelete.onItemDeleted(song);
            });
        }

        holder.imgMenu.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDragListener.requestDrag(holder);
            }
            return false;
        });
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_sw_delete, parent, false);
        return new SongViewHolder(view);
    }

    public interface IOnItemDelete {
        void onItemDeleted(SongModel song);
    }

    @Override
    public int getItemCount() {
        if (mListSong != null) {
            return mListSong.size();
        }
        return 0;
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        SwipeRevealLayout swrLayout;
        RelativeLayout rlItem;
        WeakReference<ImageView> imgThumb;
        ImageView imgMenu, imgDelete;
        TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            swrLayout = itemView.findViewById(R.id.swr_layout);
            rlItem = itemView.findViewById(R.id.rl_item_song);
            imgThumb = new WeakReference<>(itemView.findViewById(R.id.img_thumb));
            imgMenu = itemView.findViewById(R.id.img_menu_song);
            imgDelete = itemView.findViewById(R.id.img_delete);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
        }
    }
}
