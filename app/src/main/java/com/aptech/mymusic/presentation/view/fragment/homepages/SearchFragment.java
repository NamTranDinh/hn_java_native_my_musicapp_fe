package com.aptech.mymusic.presentation.view.fragment.homepages;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mct.components.baseui.BaseFragment;

import java.util.Arrays;
import java.util.List;

public class SearchFragment extends BaseFragment implements View.OnClickListener, SongAdapter.ItemClickedListener, Callback.GetDataSongSearchCallBack {
    private ChipGroup chipGroupRecommend;
    private Chip chip;
    private RecyclerView rcvSearchSong;
    private EditText edtTbSearch;
    private Toolbar toolbarSearch;
    private HomePresenter mHomePresenter;
    private LinearLayout llResultLayout, llLayoutEmpty, lnLayoutSuggest;
    private LottieAnimationView iconLoading;

    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomePresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAction();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHomePresenter.release();
        mHomePresenter = null;
    }

    private void initAction() {
        toolbarSearch.setNavigationOnClickListener(v -> popLastFragment());
        showSoftInput(edtTbSearch);
        edtTbSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchFragment.this.onTextChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initView(@NonNull View view) {
        chipGroupRecommend = view.findViewById(R.id.chip_group_recommend);
        chip = view.findViewById(R.id.chip_item_chip);
        rcvSearchSong = view.findViewById(R.id.rcv_search_song);
        toolbarSearch = view.findViewById(R.id.toolbar_search);
        edtTbSearch = view.findViewById(R.id.edt_tb_search);
        llResultLayout = view.findViewById(R.id.ll_result_layout);
        llLayoutEmpty = view.findViewById(R.id.ll_layout_empty);
        lnLayoutSuggest = view.findViewById(R.id.ln_layout_suggest);
        iconLoading = view.findViewById(R.id.icon_loading);

        show(lnLayoutSuggest);
        setDataChip();
    }

    private void setDataTopic(List<SongModel> data) {
        SongAdapter adapter = new SongAdapter(data, this, SongAdapter.TYPE_SEARCH);
        rcvSearchSong.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSearchSong.setAdapter(adapter);
    }

    private void setDataChip() {
        List<String> chipLabels = Arrays.asList("Say you do", "How you like that",
                "Stay", "Sai lầm của anh", "Unstoppable", "Ba kể con nghe",
                "By your side", "Kill this love", "Mood");
        chipLabels.forEach(s -> {
            chip = new Chip(getContext());
            chip.setText(s);
            chipGroupRecommend.addView(chip);
            chip.setOnClickListener(SearchFragment.this);
        });
    }

    private void onTextChanged() {
        String search = edtTbSearch.getText().toString().trim();
        if (!search.equals("")) {
            if (searchRunnable == null) {
                searchRunnable = () -> {
                    String s = edtTbSearch.getText().toString().trim();
                    mHomePresenter.getDataSongSearch(s, SearchFragment.this);
                };
            }
            searchHandler.removeCallbacks(searchRunnable);
            searchHandler.postDelayed(searchRunnable, 1000);
            show(iconLoading);
        } else {
            show(lnLayoutSuggest);
        }
    }

    private void show(@NonNull View view) {
        Arrays.asList(iconLoading, llLayoutEmpty, llResultLayout, lnLayoutSuggest)
                .forEach(v -> v.setVisibility(View.GONE));
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        String text = ((Chip) v).getText().toString();
        edtTbSearch.setText(text);
        edtTbSearch.setSelection(text.length());
    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {
        hideSoftInput();
        clearFocus();
        PlayMusicActivity.start(requireContext(), song);
    }

    @Override
    public void onClickedAdd(SongModel song, int position) {
    }

    @Override
    public void getDataSongSearchSuccess(@NonNull List<SongModel> data) {
        String search = edtTbSearch.getText().toString().trim();
        if (TextUtils.isEmpty(search)) {
            show(lnLayoutSuggest);
            return;
        }
        if (data.size() != 0) {
            show(llResultLayout);
            setDataTopic(data);
        } else {
            show(llLayoutEmpty);
        }
    }

    @Override
    public void getDataSongSearchFailure(String error) {
    }


}