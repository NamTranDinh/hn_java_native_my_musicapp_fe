package com.aptech.mymusic.presentation.view.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.aptech.mymusic.presentation.view.fragment.mainpager.AlbumTabFragment;
import com.aptech.mymusic.presentation.view.fragment.mainpager.HomeTabFragment;
import com.aptech.mymusic.presentation.view.fragment.mainpager.PlaylistTabFragment;
import com.aptech.mymusic.presentation.view.fragment.mainpager.TopicTabFragment;

public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new PlaylistTabFragment();
            case 2:
                return new AlbumTabFragment();
            case 3:
                return new TopicTabFragment();
            default:
                return new HomeTabFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
