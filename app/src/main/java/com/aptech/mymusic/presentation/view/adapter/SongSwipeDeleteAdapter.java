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

import com.airbnb.lottie.LottieAnimationView;
import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.common.swipe.SwipeRevealLayout;
import com.aptech.mymusic.presentation.view.common.swipe.ViewBinderHelper;
import com.aptech.mymusic.utils.GlideUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class SongSwipeDeleteAdapter extends RecyclerView.Adapter<SongSwipeDeleteAdapter.SongViewHolder> {

    private final ViewBinderHelper mBinderHelper = new ViewBinderHelper();
    private final IOnClickListener mIOnClickListener;
    private boolean mIsPlaying;
    private SongModel mCurrentSong;
    private List<SongModel> mListSong;
    private StartDragListener mDragListener;

    private boolean mIsSelectMode;

    public SongSwipeDeleteAdapter(IOnClickListener mIOnClickListener) {
        this.mIOnClickListener = mIOnClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(boolean isPlaying, SongModel song, List<SongModel> songs) {
        mIsPlaying = isPlaying;
        mCurrentSong = song;
        mListSong = songs;
        mCurrentSong.setSelected(false);
        notifyDataSetChanged();
    }

    public void setDragListener(StartDragListener dragListener) {
        this.mDragListener = dragListener;
    }

    public boolean isSelectMode() {
        return mIsSelectMode;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSelectMode(boolean isSelectMode) {
        if (mIsSelectMode != isSelectMode) {
            mIsSelectMode = isSelectMode;
            notifyDataSetChanged();
        }
        if (!mIsSelectMode) {
            mListSong.forEach(s -> s.setSelected(false));
        }
    }

    public int getSelectedItemCount() {
        return (int) mListSong.stream().filter(SongModel::isSelected).count();
    }

    @SuppressLint("NotifyDataSetChanged")
    public List<SongModel> removeAllSelectedSong() {
        mListSong.removeIf(SongModel::isSelected);
        notifyDataSetChanged();
        return mListSong;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_sw_delete, parent, false);
        return new SongViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        SongModel song = mListSong.get(position);
        if (song == null) {
            return;
        }
        String id = String.valueOf(song.getId());
        boolean isActive = song.equals(mCurrentSong);

        mBinderHelper.bind(holder.swrLayout, id);

        holder.rlItem.setActivated(isActive);
        holder.rlItem.setSelected(song.isSelected());
        if (isActive) {
            mBinderHelper.closeLayout(id);
            mBinderHelper.lockSwipe(id);
            if (mIsPlaying) {
                holder.lottieMusicWave.playAnimation();
            } else {
                holder.lottieMusicWave.pauseAnimation();
            }
            holder.lottieMusicWave.setVisibility(View.VISIBLE);
            if (mIsSelectMode) {
                holder.btnMove.setVisibility(View.GONE);
                holder.btnCheckBox.setVisibility(View.GONE);
            } else {
                holder.btnMove.setVisibility(View.VISIBLE);
                holder.btnCheckBox.setVisibility(View.GONE);
            }
        } else {
            holder.lottieMusicWave.pauseAnimation();
            holder.lottieMusicWave.setVisibility(View.GONE);
            if (mIsSelectMode) {
                mBinderHelper.closeLayout(id);
                mBinderHelper.lockSwipe(id);
                holder.btnMove.setVisibility(View.GONE);
                holder.btnCheckBox.setVisibility(View.VISIBLE);
            } else {
                mBinderHelper.unlockSwipe(id);
                holder.btnMove.setVisibility(View.VISIBLE);
                holder.btnCheckBox.setVisibility(View.GONE);
            }
        }

        holder.tvSongName.setText(song.getName());
        holder.tvSingerName.setText(song.getSingerName());
        GlideUtils.load(song.getImageUrl(), holder.imgThumb.get());

        holder.rlItem.setOnClickListener(view -> {
            if (holder.rlItem.isActivated()) return;
            if (mIOnClickListener != null) {
                mIOnClickListener.onItemClicked(song, holder.getAdapterPosition());
            }
        });
        holder.rlItem.setOnLongClickListener(v -> {
            if (holder.rlItem.isActivated()) return false;
            if (mIOnClickListener != null) {
                mIOnClickListener.onItemLongClicked(song, holder.getAdapterPosition());
            }
            return false;
        });
        holder.btnDelete.setOnClickListener(view -> {
            if (mIOnClickListener != null) {
                mIOnClickListener.onDeleteClicked(song, holder.getAdapterPosition());
            }
        });
        holder.btnCheckBox.setOnClickListener(view -> holder.rlItem.performClick());
        holder.btnMove.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDragListener.requestDrag(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        if (mListSong != null) {
            return mListSong.size();
        }
        return 0;
    }

    public interface IOnClickListener {
        void onItemClicked(SongModel song, int position);

        void onItemLongClicked(SongModel song, int position);

        void onDeleteClicked(SongModel song, int position);

    }


    public interface StartDragListener {
        void requestDrag(RecyclerView.ViewHolder viewHolder);
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        SwipeRevealLayout swrLayout;
        RelativeLayout rlItem;
        WeakReference<ImageView> imgThumb;
        ImageView btnCheckBox, btnMove, btnDelete;
        LottieAnimationView lottieMusicWave;
        TextView tvSongName, tvSingerName;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            swrLayout = itemView.findViewById(R.id.swr_layout);
            rlItem = itemView.findViewById(R.id.rl_item_song);
            imgThumb = new WeakReference<>(itemView.findViewById(R.id.img_thumb));
            lottieMusicWave = itemView.findViewById(R.id.lottie_music_wave);
            btnCheckBox = itemView.findViewById(R.id.btn_check_box);
            btnMove = itemView.findViewById(R.id.btn_move_song);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            tvSongName = itemView.findViewById(R.id.tv_song_name);
            tvSingerName = itemView.findViewById(R.id.tv_singer_name);
        }
    }

}
