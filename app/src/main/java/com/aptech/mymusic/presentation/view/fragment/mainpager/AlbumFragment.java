package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.presentation.presenter.AlbumPresenter;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.view.activity.ISendDataToDetail;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.adapter.CardAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends BaseTabFragment implements Callback.GetDataAlbumCallBack, ISendDataToDetail {
    private AlbumPresenter albumPresenter;
    private RecyclerView rcvCard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumPresenter = new AlbumPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvCard = view.findViewById(R.id.rcv_card);
        albumPresenter.getDataAlbum(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        albumPresenter.release();
        albumPresenter = null;
    }

    private void setData(List<AlbumModel> list) {
        CardAdapter adapter = new CardAdapter(getContext(), new ArrayList<>(list), false, this);
        rcvCard.setAdapter(adapter);

        GridLayoutManager manager = new GridLayoutManager(getContext(), MainActivity.TWO_ITEM_CARD);
        rcvCard.setLayoutManager(manager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void getDataAlbumSuccess(List<AlbumModel> data) {
        setData(data);
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
