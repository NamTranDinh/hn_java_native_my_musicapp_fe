package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Objects;

import me.relex.circleindicator.CircleIndicator;

public class BannerFragment extends BaseFragment implements Callback.GetDataBannerCallBack {

    private static final int TIME_SLIDE_CHANGE = 3000;
    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private Runnable runnable;
    private List<AdsModel> adsModelList;

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

    private void setAdapter() {
        BannerAdapter mBannerAdapter = new BannerAdapter(getActivity(), adsModelList);
        mViewPager.setAdapter(mBannerAdapter);
        mCircleIndicator.setViewPager(mViewPager);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUi(@NonNull View view) {
        mViewPager = view.findViewById(R.id.banner_view_pager);
        mCircleIndicator = view.findViewById(R.id.circle_indicator);
        View.OnTouchListener touchListener = (v, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                stopRun();
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
                autoRun();
            }
            return false;
        };
        mViewPager.setOnTouchListener(touchListener);
    }

    private void autoRun() {
        if (runnable == null) {
            runnable = () -> {
                int currentItem = mViewPager.getCurrentItem() + 1;
                if (currentItem >= Objects.requireNonNull(mViewPager.getAdapter()).getCount()) {
                    currentItem = 0;
                }
                mViewPager.setCurrentItem(currentItem, true);
                mViewPager.postDelayed(runnable, BannerFragment.TIME_SLIDE_CHANGE);
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
        adsModelList = data;
        setAdapter();
    }

    @Override
    public void getDataBannerFailure(String error) {
        ToastUtils.makeErrorToast(getContext(), Toast.LENGTH_LONG, "404" + error, true).show();
    }

}
