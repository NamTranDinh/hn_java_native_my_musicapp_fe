package com.aptech.mymusic.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aptech.mymusic.presentation.view.fragment.musicplayer.DetailSongFragment;
import com.aptech.mymusic.presentation.view.fragment.musicplayer.MainSongFragment;

public class PlayMusicViewPagerAdapter extends FragmentStateAdapter {

    public PlayMusicViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new MainSongFragment();
        }
        return new DetailSongFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
