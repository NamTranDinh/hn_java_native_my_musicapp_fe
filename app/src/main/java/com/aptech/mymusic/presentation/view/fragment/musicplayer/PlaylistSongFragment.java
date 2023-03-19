package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.adapter.SongSwipeDeleteAdapter;
import com.aptech.mymusic.presentation.view.dialog.ConfirmDialog;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.baseui.BaseOverlayDialog;
import com.mct.components.utils.ToastUtils;

public class PlaylistSongFragment extends BaseFragment implements BaseActivity.OnBackPressed, SongSwipeDeleteAdapter.IOnClickListener {

    private Toolbar toolbar;
    private SongSwipeDeleteAdapter adapter;
    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            if (MusicServiceHelper.getCurrentSong() == null) {
                return;
            }
            initData();
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
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
        toolbar.setNavigationOnClickListener(v -> {
            if (adapter.isSelectMode()) {
                toolbar.setSelected(!toolbar.isSelected());
                adapter.setSelectAll(toolbar.isSelected());
                initToolbar();
            } else {
                popLastFragment();
            }
        });
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_delete) {
                final int count = adapter.getSelectedItemCount();
                if (count == 0) {
                    showToast("Please choose song to delete", ToastUtils.WARNING, true);
                    return false;
                }
                String message = String.format("Are you sure want to delete %s selected song?", count);
                new ConfirmDialog(requireContext(), message, new ConfirmDialog.IOnClickListener() {
                    @Override
                    public void onClickCancel(BaseOverlayDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    @SuppressLint("NotifyDataSetChanged")
                    public void onClickOk(BaseOverlayDialog dialog) {
                        dialog.dismiss();
                        MusicServiceHelper.removeSongs(adapter.getSelectedSongs());
                        if (MusicServiceHelper.getCurrentListSong().isEmpty()) {
                            requireActivity().finish();
                            return;
                        }
                        showToast(String.format("Deleted %s songs success", count), ToastUtils.INFO, true);
                        adapter.setSelectMode(false);
                        initToolbar();
                    }
                }).create(null);
                return true;
            }
            if (item.getItemId() == R.id.menu_cancel) {
                adapter.setSelectMode(false);
                initToolbar();
                return true;
            }
            return false;
        });

        RecyclerView rcvListSong = view.findViewById(R.id.rcv_list_song);

        // set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        rcvListSong.setLayoutManager(layoutManager);

        // create adapter
        adapter = new SongSwipeDeleteAdapter(this);

        // create item touch helper
        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            private boolean isDrawing;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int positionDrag = viewHolder.getAdapterPosition();
                int positionTarget = target.getAdapterPosition();
                MusicServiceHelper.swapSong(positionDrag, positionTarget);
                adapter.notifyItemMoved(positionDrag, positionTarget);
                return true;
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
                    viewHolder.itemView.setBackgroundResource(R.drawable.custom_background_song_item_press);
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

        initData();
        initToolbar();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
    }

    private void initData() {
        adapter.setData(MusicServiceHelper.isPlaying(), MusicServiceHelper.getCurrentSong(), MusicServiceHelper.getCurrentListSong());
    }

    private void initToolbar() {
        if (adapter.isSelectMode()) {
            Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.selector_custom_check_box);
            if (drawable != null) {
                drawable.setTint(Color.WHITE);
            }
            toolbar.setNavigationIcon(drawable);
            toolbar.setNavigationContentDescription(0);
            toolbar.getMenu().findItem(R.id.menu_delete).setVisible(true);
            toolbar.getMenu().findItem(R.id.menu_cancel).setVisible(true);
            toolbar.setTitle(getString(R.string.text_play_music_selected_n, adapter.getSelectedItemCount()));
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_back);
            toolbar.setNavigationContentDescription(R.string.content_desc_back);
            toolbar.getMenu().findItem(R.id.menu_delete).setVisible(false);
            toolbar.getMenu().findItem(R.id.menu_cancel).setVisible(false);
            toolbar.setTitle(getString(R.string.text_play_music_playlist_n, adapter.getItemCount()));
        }
    }


    @Override
    public void onItemClicked(SongModel song, int position) {
        if (adapter.isSelectMode()) {
            song.setSelected(!song.isSelected());
            adapter.notifyItemChanged(position);
            toolbar.setSelected(adapter.getSelectedItemCount() == adapter.getItemCount());
            initToolbar();
        } else {
            MusicServiceHelper.playSong(song);
        }
    }

    @Override
    public void onItemLongClicked(SongModel song, int position) {
        if (!adapter.isSelectMode()) {
            adapter.setSelectMode(true);
        }
        onItemClicked(song, position);
    }

    @Override
    public void onDeleteClicked(SongModel song, int position) {
        MusicServiceHelper.removeSong(song);
        adapter.notifyItemRemoved(position);
        showToast(String.format("Removed %s from playlist", song.getName()), ToastUtils.INFO, true);
        initToolbar();
    }

    @Override
    public boolean onBackPressed() {
        if (adapter.isSelectMode()) {
            adapter.setSelectMode(false);
            initToolbar();
            return true;
        }
        return false;
    }
}
