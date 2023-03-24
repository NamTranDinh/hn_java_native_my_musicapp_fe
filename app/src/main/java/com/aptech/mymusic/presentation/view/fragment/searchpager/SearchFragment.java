package com.aptech.mymusic.presentation.view.fragment.searchpager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.utils.ToastUtils;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setShowControlLayout(false);
        }
    }

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
    public void onResume() {
        super.onResume();
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
                if (!s.toString().trim().equals("")) {
                    iconLoading.setVisibility(View.VISIBLE);
                    llLayoutEmpty.setVisibility(View.GONE);
                    lnLayoutSuggest.setVisibility(View.GONE);
                    delayAndExecuteFunction(s.toString());
                } else {
                    llLayoutEmpty.setVisibility(View.GONE);
                    llResultLayout.setVisibility(View.GONE);
                    lnLayoutSuggest.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initView(View view) {
        chipGroupRecommend = view.findViewById(R.id.chip_group_recommend);
        chip = view.findViewById(R.id.chip_item_chip);
        rcvSearchSong = view.findViewById(R.id.rcv_search_song);
        chipGroupRecommend = view.findViewById(R.id.chip_group_recommend);
        toolbarSearch = view.findViewById(R.id.toolbar_search);
        edtTbSearch = view.findViewById(R.id.edt_tb_search);
        llResultLayout = view.findViewById(R.id.ll_result_layout);
        llLayoutEmpty = view.findViewById(R.id.ll_layout_empty);
        lnLayoutSuggest = view.findViewById(R.id.ln_layout_suggest);
        iconLoading = view.findViewById(R.id.icon_loading);

        iconLoading.setVisibility(View.GONE);
        llLayoutEmpty.setVisibility(View.GONE);
        llResultLayout.setVisibility(View.GONE);

        setDataChip();
    }

    private void setDataTopic(List<SongModel> data) {
        SongAdapter adapter = new SongAdapter(data, this, SongAdapter.TYPE_SEARCH);
        rcvSearchSong.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSearchSong.setAdapter(adapter);
    }

    private void setDataChip() {
        List<String> chipLabels = Arrays.asList("Say you do", "How you like that", "Stay", "Sai lầm của anh", "Unstoppable", "Ba kể con nghe", "By your side", "Kill this love", "Mood");
        chipLabels.forEach(s -> {
            chip = new Chip(getContext());
            chip.setText(s);
            chipGroupRecommend.addView(chip);
            chip.setOnClickListener(SearchFragment.this);
        });
    }

    private void delayAndExecuteFunction(String s) {
        new Handler().postDelayed(() -> mHomePresenter.getDataSongSearch(s, SearchFragment.this), 1000); // 2000 milliseconds = 2 seconds
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHomePresenter.release();
        mHomePresenter = null;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setShowControlLayout(true);
        }
    }

    @Override
    public void onClick(View v) {
        String text = ((Chip) v).getText().toString();
        edtTbSearch.setText(text);
        edtTbSearch.setSelection(text.length());
    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {
        PlayMusicActivity.start(requireContext(), song);
    }

    @Override
    public void onClickedAdd(SongModel song, int position) {

    }

    @Override
    public void getDataSongSearchSuccess(List<SongModel> data) {
        if (data.size() != 0) {
            iconLoading.setVisibility(View.GONE);
            llLayoutEmpty.setVisibility(View.GONE);
            llResultLayout.setVisibility(View.VISIBLE);
            setDataTopic(data);
        } else {
            iconLoading.setVisibility(View.GONE);
            llLayoutEmpty.setVisibility(View.VISIBLE);
            llResultLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDataSongSearchFailure(String error) {

    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }
}