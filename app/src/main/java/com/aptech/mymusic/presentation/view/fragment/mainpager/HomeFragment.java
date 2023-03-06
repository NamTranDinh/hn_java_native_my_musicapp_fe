package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.model.RowCardModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.adapter.HomePageAdapter;
import com.mct.components.baseui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment implements Callback.GetDataPlayListTop100CallBack, Callback.GetDataNewReleaseMusicCallBack, Callback.GetDataAlbumCallBack, Callback.GetDataCategoryFavorCallBack {

    private View view;
    private RecyclerView rcvRowCard;
    private HomePageAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        adapter = new HomePageAdapter(getContext());
        rcvRowCard.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvRowCard.setLayoutManager(manager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView() {
        rcvRowCard = view.findViewById(R.id.rcv_list_card);
        HomePresenter homePresenter = new HomePresenter(this);
        homePresenter.getDataPlaylistTop100(this);
        homePresenter.getDataNewReleasedMusic(this);
        homePresenter.getDataAlbum(this);
        homePresenter.getDataCategoryFavor(this);
    }

    @Override
    public void getDataAlbumSuccess(List<AlbumModel> data) {
        adapter.addCarModel(new RowCardModel("Có thể bạn muốn nghe", new ArrayList<>(data)));
    }

    @Override
    public void getDataAlbumFailure(String error) {

    }

    @Override
    public void getDataTop100Success(List<PlaylistModel> data) {
        adapter.addCarModel(new RowCardModel("Top 100", new ArrayList<>(data)));
    }

    @Override
    public void getDataTop100Failure(String error) {

    }

    @Override
    public void getDataCateFavorSuccess(List<CategoryModel> data) {
        adapter.addCarModel(new RowCardModel("Thể loại được ưa thích", new ArrayList<>(data)));
    }

    @Override
    public void getDataCateFavorFailure(String error) {

    }

    @Override
    public void getDataReleaseMusicSuccess(List<SongModel> data) {
        adapter.addCarModel(new RowCardModel("Mới phát hành", new ArrayList<>(data)));
    }

    @Override
    public void getDataReleaseMusicFailure(String error) {

    }
}
