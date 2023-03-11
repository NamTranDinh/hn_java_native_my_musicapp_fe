package com.aptech.mymusic.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aptech.mymusic.presentation.view.fragment.mainpager.AlbumFragment;
import com.aptech.mymusic.presentation.view.fragment.mainpager.HomeFragment;
import com.aptech.mymusic.presentation.view.fragment.mainpager.PlaylistFragment;
import com.aptech.mymusic.presentation.view.fragment.mainpager.TopicFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PlaylistFragment();
            case 2:
                return new AlbumFragment();
            case 3:
                return new TopicFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
