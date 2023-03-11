package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.adapter.BannerAdapter;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.utils.ToastUtils;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class BannerFragment extends BaseFragment implements Callback.GetDataBannerCallBack {

    private static final int TIME_SLIDE_CHANGE = 3000;
    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private Runnable runnable;

    private HomePresenter homePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_banner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUi(view);
        homePresenter.getDataBanner(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        autoRun();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRun();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.release();
        homePresenter = null;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUi(@NonNull View view) {
        mViewPager = view.findViewById(R.id.banner_view_pager);
        mViewPager.setAdapter(new BannerAdapter(getActivity(), null));
        mViewPager.setOnTouchListener((v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                stopRun();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                autoRun();
            }
            return false;
        });
        mCircleIndicator = view.findViewById(R.id.circle_indicator);
    }

    private void autoRun() {
        if (runnable == null) {
            runnable = () -> {
                if (mViewPager.getAdapter() != null && mViewPager.getAdapter().getCount() > 1) {
                    int currentItem = mViewPager.getCurrentItem() + 1;
                    if (currentItem >= mViewPager.getAdapter().getCount()) {
                        currentItem = 0;
                    }
                    mViewPager.setCurrentItem(currentItem, true);
                    mViewPager.postDelayed(runnable, BannerFragment.TIME_SLIDE_CHANGE);
                }
            };
        }
        mViewPager.removeCallbacks(runnable);
        mViewPager.postDelayed(runnable, BannerFragment.TIME_SLIDE_CHANGE);
    }

    private void stopRun() {
        mViewPager.removeCallbacks(runnable);
    }

    @Override
    public void getDataBannerSuccess(List<AdsModel> data) {
        mViewPager.setAdapter(new BannerAdapter(getActivity(), data));
        mCircleIndicator.setViewPager(mViewPager);
        autoRun();
    }

    @Override
    public void getDataBannerFailure(String error) {
        ToastUtils.makeErrorToast(getContext(), Toast.LENGTH_LONG, "404" + error, true).show();
    }

}
