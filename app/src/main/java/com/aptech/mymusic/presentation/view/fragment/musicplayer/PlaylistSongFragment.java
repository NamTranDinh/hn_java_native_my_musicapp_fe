package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.SongSpecialAdapter;

public class PlaylistSongFragment extends Fragment {

    private View view;
    private ImageView imgClose, imgTimer, imgMenu;
    private TextView tvTitleToolbar;
    private RecyclerView rcvListSong;
    private PlayMusicActivity mPlayMusicActivity;
    private SongSpecialAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mPlayMusicActivity = (PlayMusicActivity) requireActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist_song, container, false);
        initUi();
        // imgClose.setOnClickListener(view -> mPlayMusicActivity.hidePlaylistFragment());

        // set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvListSong.setLayoutManager(layoutManager);

//        // create adapter
//        adapter = new SongSpecialAdapter(mPlayMusicActivity, MusicService.getListSong(), () -> {
//            initTitleToolbar();
//        });
//        // create item touch helper
//        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
//
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                int positionDrag = viewHolder.getAdapterPosition();
//                int positionTarget = target.getAdapterPosition();
//                Collections.swap(MusicService.getListSong(), positionDrag, positionTarget);
//                adapter.notifyItemMoved(positionDrag, positionTarget);
//                return false;
//            }
//
//            // disable onLongPressItem
//            @Override
//            public boolean isLongPressDragEnabled() {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        });
//        // set drag for interface
//        adapter.setDragListener(touchHelper::startDrag);
//        // set touch for recyclerview
//        touchHelper.attachToRecyclerView(rcvListSong);
//        // set adapter
//        rcvListSong.setAdapter(adapter);

        return view;
    }

    private void initTitleToolbar() {
//        String title = "Danh sách phát (" + MusicService.getListSong().size() + ")";
//        tvTitleToolbar.setText(title);
    }

    private void initUi() {
        imgClose = view.findViewById(R.id.img_close);
        imgTimer = view.findViewById(R.id.img_timer);
        imgMenu = view.findViewById(R.id.img_menu);
        tvTitleToolbar = view.findViewById(R.id.tv_title_toolbar);
        rcvListSong = view.findViewById(R.id.rcv_list_song);
        initTitleToolbar();
    }
}
