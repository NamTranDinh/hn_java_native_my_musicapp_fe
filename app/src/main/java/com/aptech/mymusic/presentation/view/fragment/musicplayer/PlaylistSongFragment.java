package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_PLAY_LIST;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_LIST_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_CURRENT_SONG;
import static com.aptech.mymusic.presentation.view.service.MusicDelegate.KEY_IS_PLAYING;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.adapter.SongSpecialAdapter;
import com.aptech.mymusic.presentation.view.service.MusicService;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.mct.components.baseui.BaseFragment;

import java.util.List;

public class PlaylistSongFragment extends BaseFragment implements SongSpecialAdapter.IOnClickSongItem {

    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            Bundle bundle = intent.getExtras();
            if (ACTION_UPDATE_VIEW.equals(intent.getAction())) {
                if (bundle != null) {
                    boolean isPlaying = bundle.getBoolean(KEY_IS_PLAYING);
                    adapter.setSong(isPlaying ? (SongModel) bundle.getSerializable(KEY_CURRENT_SONG) : null);
                }
            }
            if (ACTION_UPDATE_PLAY_LIST.equals(intent.getAction())) {
                if (bundle != null) {
                    //noinspection unchecked
                    adapter.setListSong((List<SongModel>) bundle.getSerializable(KEY_CURRENT_LIST_SONG));
                }
            }
        }
    };
    private SongSpecialAdapter adapter;
    private Toolbar toolbar;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_UPDATE_VIEW);
        filter.addAction(ACTION_UPDATE_PLAY_LIST);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiverFromMusicService, filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> popLastFragment());

        RecyclerView rcvListSong = view.findViewById(R.id.rcv_list_song);

        // set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvListSong.setLayoutManager(layoutManager);

        // create adapter
        adapter = new SongSpecialAdapter(this, song -> {
            initToolbarTitle();
            MusicServiceHelper.removeSong(song);
        });

        // create item touch helper
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            private boolean isDrawing;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int positionDrag = viewHolder.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                MusicServiceHelper.swapSong(positionDrag, positionTarget);
                adapter.notifyItemMoved(positionDrag, positionTarget);
                return false;
            }

            // disable onLongPressItem
            @Override
            public boolean isLongPressDragEnabled() {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (!isDrawing && isCurrentlyActive) {
                    isDrawing = true;
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#66000000"));
                }
                if (isDrawing && !isCurrentlyActive) {
                    isDrawing = false;
                    viewHolder.itemView.setBackgroundResource(0);
                }
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        // set drag for interface
        adapter.setDragListener(touchHelper::startDrag);
        // set touch for recyclerview
        touchHelper.attachToRecyclerView(rcvListSong);
        // set adapter
        rcvListSong.setAdapter(adapter);

        adapter.setData(MusicService.isPlaying() ? MusicService.getCurrentSong() : null, MusicService.getCurrentListSong());
        initToolbarTitle();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
    }

    private void initToolbarTitle() {
        toolbar.setTitle(getString(R.string.text_play_music_playlist_n, adapter.getItemCount()));
    }

    @Override
    public void onClickSongItem(SongModel song) {
        MusicServiceHelper.playSong(song);
    }
}
