package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Bundle;
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
import com.aptech.mymusic.presentation.view.adapter.ICardListener;

import java.util.ArrayList;
import java.util.List;

public class HomeTabFragment extends BaseTabFragment implements Callback.GetDataPlayListCallBack, Callback.GetDataNewReleaseMusicCallBack, Callback.GetDataAlbumCallBack, Callback.GetDataCategoryFavorCallBack, ICardListener {

    private RecyclerView rcvRowCard;
    private HomePageAdapter adapter;
    private HomePresenter homePresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        adapter = new HomePageAdapter(getContext(), this);
        rcvRowCard.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvRowCard.setLayoutManager(manager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homePresenter.release();
        homePresenter = null;
    }

    private void initView(@NonNull View view) {
        rcvRowCard = view.findViewById(R.id.rcv_list_card);
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
    public void getDataPlaylistSuccess(List<PlaylistModel> data) {
        adapter.addCarModel(new RowCardModel("Top 100", new ArrayList<>(data)));
    }

    @Override
    public void getDataPlaylistFailure(String error) {

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

    @NonNull
    @Override
    protected RecyclerView getScrollView() {
        return rcvRowCard;
    }
}
