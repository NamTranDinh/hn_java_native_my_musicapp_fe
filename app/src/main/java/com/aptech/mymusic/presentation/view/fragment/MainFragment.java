package com.aptech.mymusic.presentation.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.aptech.mymusic.R;
import com.aptech.mymusic.presentation.view.adapter.MainViewPagerAdapter;
import com.aptech.mymusic.presentation.view.common.CircularAvatarUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mct.components.baseui.BaseFragment;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private BottomNavigationView mTopNavView;
    private ViewPager2 mViewPager2;

    Toolbar toolbar;
    private CircularAvatarUser cvAvatarUser;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        initAction();
        MainViewPagerAdapter mHomeViewPagerAdapter = new MainViewPagerAdapter(requireActivity());
        mViewPager2.setAdapter(mHomeViewPagerAdapter);
        mNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void initUi() {
        mDrawerLayout = requireActivity().findViewById(R.id.drawer_layout);
        mNavigationView = requireActivity().findViewById(R.id.navigation_view);
        mTopNavView = view.findViewById(R.id.top_nav);
        toolbar = view.findViewById(R.id.toolbar);
        mViewPager2 = view.findViewById(R.id.view_pager2);
        cvAvatarUser = view.findViewById(R.id.cv_avatar_user);
    }

    private void initAction() {
        cvAvatarUser.setOnClickListener(this);
        mNavigationView.setNavigationItemSelectedListener(this::onItemSelected);
        mViewPager2.registerOnPageChangeCallback(new OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mTopNavView.getMenu().findItem(R.id.top_home).setChecked(true);
                        mNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        mTopNavView.getMenu().findItem(R.id.top_playlist).setChecked(true);
                        mNavigationView.getMenu().findItem(R.id.nav_playlist).setChecked(true);
                        break;
                    case 2:
                        mTopNavView.getMenu().findItem(R.id.top_album).setChecked(true);
                        mNavigationView.getMenu().findItem(R.id.nav_album).setChecked(true);
                        break;
                    case 3:
                        mTopNavView.getMenu().findItem(R.id.top_topic).setChecked(true);
                        mNavigationView.getMenu().findItem(R.id.nav_topic).setChecked(true);
                        break;
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cv_avatar_user) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
            case R.id.top_home:
                mViewPager2.setCurrentItem(0);
                break;
            case R.id.nav_playlist:
            case R.id.top_playlist:
                mViewPager2.setCurrentItem(1);
                break;
            case R.id.nav_album:
            case R.id.top_album:
                mViewPager2.setCurrentItem(2);
                break;
            case R.id.nav_topic:
            case R.id.top_topic:
                mViewPager2.setCurrentItem(3);
                break;
        }
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
