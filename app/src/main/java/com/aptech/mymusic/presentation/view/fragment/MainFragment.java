package com.aptech.mymusic.presentation.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import com.aptech.mymusic.R;
import com.aptech.mymusic.presentation.view.adapter.MainViewPagerAdapter;
import com.aptech.mymusic.presentation.view.fragment.homepages.SearchFragment;
import com.aptech.mymusic.utils.BarsUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.mct.components.baseui.BaseFragment;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private AppBarLayout mAppBarLayout;
    private BottomNavigationView mTopNavView;
    private ViewPager2 mViewPager2;
    private EditText mEdtTbSearch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
        initAction();
        mViewPager2.setAdapter(new MainViewPagerAdapter(requireActivity()));
        mNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        BarsUtils.setAppearanceLightStatusBars(getActivity(), true);
    }

    private void initUi(@NonNull View view) {
        mDrawerLayout = view.findViewById(R.id.drawer_layout);
        mAppBarLayout = view.findViewById(R.id.app_bar_layout);
        mNavigationView = view.findViewById(R.id.navigation_view);
        mTopNavView = view.findViewById(R.id.top_nav);
        mViewPager2 = view.findViewById(R.id.view_pager2);
        mEdtTbSearch = view.findViewById(R.id.edt_tb_search);
        view.findViewById(R.id.civ_avatar_user).setOnClickListener(this);

        BarsUtils.offsetStatusBar(view.findViewById(R.id.toolbar));
    }

    private void initAction() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                BarsUtils.setAppearanceLightStatusBars(getActivity(), false);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                BarsUtils.setAppearanceLightStatusBars(getActivity(), true);
            }
        });
        mNavigationView.setNavigationItemSelectedListener(this::onItemSelected);
        mTopNavView.setOnItemSelectedListener(this::onItemSelected);
        mEdtTbSearch.setOnClickListener(this);
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
    public void onClick(@NonNull View v) {
        int id = v.getId();
        if (id == R.id.civ_avatar_user) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        if (id == R.id.edt_tb_search){
            Fragment searchFragment = new SearchFragment();
            replaceFragment(searchFragment, true);
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

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }
}
