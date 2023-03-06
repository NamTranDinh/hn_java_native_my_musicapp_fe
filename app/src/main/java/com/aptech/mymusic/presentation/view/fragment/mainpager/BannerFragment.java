package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    private View mView;
    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private BannerAdapter mBannerAdapter;
    private Runnable runnable;
    private Handler handler;
    private List<AdsModel> adsModelList;
    private static final int TIME_SLIDE_CHANGE = 5000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_banner, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUi();
        HomePresenter homePresenter = new HomePresenter(this);
        homePresenter.getDataBanner(this);
    }

    private void setAdapter() {
        mBannerAdapter = new BannerAdapter(getActivity(), adsModelList);
        mViewPager.setAdapter(mBannerAdapter);
        mCircleIndicator.setViewPager(mViewPager);
        autoRun();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUi() {
        mViewPager = mView.findViewById(R.id.banner_view_pager);
        mCircleIndicator = mView.findViewById(R.id.circle_indicator);
        View.OnTouchListener touchListener = (view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        };
        mViewPager.setOnTouchListener(touchListener);
    }

    private void autoRun(){
        handler = new Handler();
        runnable = () -> {
            int currentItem = mViewPager.getCurrentItem() + 1;
            if (currentItem >= Objects.requireNonNull(mViewPager.getAdapter()).getCount()) {
                currentItem = 0;
            }
            mViewPager.setCurrentItem(currentItem, true);
            handler.postDelayed(runnable, BannerFragment.TIME_SLIDE_CHANGE);
        };
        handler.postDelayed(runnable, BannerFragment.TIME_SLIDE_CHANGE);
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

    public View getView() {
        return mView;
    }

    public void setView(View mView) {
        this.mView = mView;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    public CircleIndicator getCircleIndicator() {
        return mCircleIndicator;
    }

    public void setCircleIndicator(CircleIndicator mCircleIndicator) {
        this.mCircleIndicator = mCircleIndicator;
    }

    public BannerAdapter getBannerAdapter() {
        return mBannerAdapter;
    }

    public void setBannerAdapter(BannerAdapter mBannerAdapter) {
        this.mBannerAdapter = mBannerAdapter;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
