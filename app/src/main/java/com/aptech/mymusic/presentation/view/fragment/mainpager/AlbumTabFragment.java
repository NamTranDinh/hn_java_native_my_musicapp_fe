package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.presentation.presenter.AlbumPresenter;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.adapter.CardAdapter;
import com.aptech.mymusic.presentation.view.adapter.ICardListener;

import java.util.ArrayList;
import java.util.List;

public class AlbumTabFragment extends BaseTabFragment implements Callback.GetDataAlbumCallBack, ICardListener {
    protected LottieAnimationView mImgLoading;
    private RecyclerView rcvCard;
    private AlbumPresenter albumPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumPresenter = new AlbumPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImgLoading = view.findViewById(R.id.icon_loading);
        rcvCard = view.findViewById(R.id.rcv_card);

        showLoading(mImgLoading);
        albumPresenter.getDataAlbum(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        albumPresenter.release();
        albumPresenter = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void getDataAlbumSuccess(List<AlbumModel> data) {
        hideLoading(mImgLoading);
        rcvCard.setAdapter(new CardAdapter(new ArrayList<>(data), false, this));
        rcvCard.setLayoutManager(new GridLayoutManager(getContext(), MainActivity.TWO_ITEM_CARD));
    }

    @Override
    public void getDataAlbumFailure(String error) {

    }

    @NonNull
    @Override
    protected RecyclerView getScrollView() {
        return rcvCard;
    }
}
