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
    private SongModel mSong;
    private List<SongModel> mListSong;
    private StartDragListener mDragListener;


    public SongSwipeDeleteAdapter(IOnClickListener mIOnClickListener) {
        this.mIOnClickListener = mIOnClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(SongModel song, List<SongModel> songs) {
        mSong = song;
        mListSong = songs;
        notifyDataSetChanged();
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
        int holderPosition = holder.getAdapterPosition();
        String id = String.valueOf(song.getId());
        boolean isActive = song.equals(mSong);

        mBinderHelper.bind(holder.swrLayout, id);

        holder.rlItem.setSelected(isActive);
        if (isActive) {
            mBinderHelper.lockSwipe(id);
            holder.lottieMusicWave.setVisibility(View.VISIBLE);
            holder.lottieMusicWave.playAnimation();
        } else {
            mBinderHelper.unlockSwipe(id);
            holder.lottieMusicWave.setVisibility(View.GONE);
            holder.lottieMusicWave.pauseAnimation();
        }

        holder.tvSongName.setText(song.getName());
        holder.tvSingerName.setText(song.getSingerName());
        GlideUtils.load(song.getImageUrl(), holder.imgThumb.get());

        holder.rlItem.setOnClickListener(view -> {
            if (mIOnClickListener != null) {
                mIOnClickListener.onItemClicked(song, holderPosition);
            }
        });
        holder.btnDelete.setOnClickListener(view -> {
            if (mIOnClickListener != null) {
                mIOnClickListener.onDeleteClicked(song, holderPosition);
            }
        });

        holder.btnMove.setOnTouchListener((v, event) -> {
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

    @Override
    public int getItemCount() {
        if (mListSong != null) {
            return mListSong.size();
        }
        return 0;
    }

    public interface IOnClickListener {
        void onItemClicked(SongModel song, int position);

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
