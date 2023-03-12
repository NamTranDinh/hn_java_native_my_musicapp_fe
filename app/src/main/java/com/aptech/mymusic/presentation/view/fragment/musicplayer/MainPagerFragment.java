package com.aptech.mymusic.presentation.view.fragment.musicplayer;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.ACTION_UPDATE_VIEW;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.widget.ViewPager2;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.adapter.PlayMusicViewPagerAdapter;
import com.aptech.mymusic.presentation.view.service.MusicServiceHelper;
import com.aptech.mymusic.utils.ZoomOutPageTransformer;
import com.mct.components.baseui.BaseFragment;

import me.relex.circleindicator.CircleIndicator3;

public class MainPagerFragment extends BaseFragment {

    private Toolbar mToolbar;
    BroadcastReceiver receiverFromMusicService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            initData();
        }
    };
    private ViewPager2 mViewPager2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiverFromMusicService, new IntentFilter(ACTION_UPDATE_VIEW));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_music_main_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> requireActivity().finish());

        mViewPager2 = view.findViewById(R.id.view_pager2);
        mViewPager2.setAdapter(new PlayMusicViewPagerAdapter(this));
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
        mViewPager2.setCurrentItem(1, false);

        CircleIndicator3 mCircleIndicator3 = view.findViewById(R.id.circle_indicator);
        mCircleIndicator3.setViewPager(mViewPager2);

        initData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiverFromMusicService);
    }

    public void jumpToPage(int page, boolean smoothScroll) {
        if (mViewPager2 != null) {
            mViewPager2.setCurrentItem(page, smoothScroll);
        }
    }

    private void initData() {
        SongModel song = MusicServiceHelper.getCurrentSong();
        mToolbar.setTitle(song.getName());
        mToolbar.setSubtitle(song.getSingerName());
    }
}
