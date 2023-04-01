package com.aptech.mymusic.presentation.view.fragment.homepages;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.CardAdapter;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.aptech.mymusic.presentation.view.fragment.mainpager.BaseTabFragment;
import com.aptech.mymusic.utils.BarsUtils;
import com.aptech.mymusic.utils.BitmapUtils;
import com.aptech.mymusic.utils.GlideUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.mct.components.utils.ScreenUtils;
import com.mct.components.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DetailCardFragment extends BaseTabFragment implements SongAdapter.ItemClickedListener, Callback.GetDataAllSongCallBack, Callback.GetDataAllCategoryCallBack {

    private static final String KEY_CARD_MODEL = "KEY_CARD_MODEL";
    private CardModel card;
    private ImageView imgBackground;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RelativeLayout rlDetailHeader;
    private ImageView imgCard;
    private TextView titleCard;
    private TextView subTitleCard;
    private LottieAnimationView imgLoading;
    private TextView tvNoHaveSong;
    private RecyclerView rcvSong;

    private HomePresenter mHomePresenter;

    @NonNull
    public static DetailCardFragment newInstance(CardModel cardModel) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CARD_MODEL, cardModel);
        DetailCardFragment fragment = new DetailCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = requireArguments();
        card = (CardModel) bundle.getSerializable(KEY_CARD_MODEL);
        mHomePresenter = new HomePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi(view);
        initToolBarAnimation();
        initData();
        BarsUtils.offsetStatusBar(mToolbar);

        rcvSong.setVisibility(View.GONE);
        imgLoading.setVisibility(View.VISIBLE);
        imgLoading.playAnimation();

        if (isTopic()) {
            ColorStateList csl = ColorStateList.valueOf(Color.BLACK);
            SimpleColorFilter filter = new SimpleColorFilter(csl.getDefaultColor());
            KeyPath keyPath = new KeyPath("**");
            LottieValueCallback<ColorFilter> callback = new LottieValueCallback<>(filter);
            imgLoading.addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback);
            imgLoading.invalidate();
            mHomePresenter.getDataAllCategory(card.getId(), this);
            return;
        }

        String type = null;
        if (card instanceof PlaylistModel) {
            type = "playlist";
        }
        if (card instanceof AlbumModel) {
            type = "album";
        }
        if (card instanceof CategoryModel) {
            type = "category";
        }
        mHomePresenter.getDataAllSong(type, card.getId(), this);
    }

    @Override
    public void onResume() {
        super.onResume();
        BarsUtils.setAppearanceLightStatusBars(getActivity(), false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHomePresenter.release();
        mHomePresenter = null;
    }

    private void initUi(@NonNull View view) {
        imgBackground = view.findViewById(R.id.img_background);
        mAppBarLayout = view.findViewById(R.id.app_bar_layout);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> popLastFragment());
        if (isTopic()) {
            rlDetailHeader = view.findViewById(R.id.rl_detail_header_with_list_category);
            imgCard = view.findViewById(R.id.img_bg_topic);
        } else {
            rlDetailHeader = view.findViewById(R.id.rl_detail_header_with_list_song);
            imgCard = view.findViewById(R.id.img_thumb);
        }
        rlDetailHeader.setVisibility(View.VISIBLE);
        titleCard = view.findViewById(R.id.tv_title_card);
        subTitleCard = view.findViewById(R.id.tv_sub_title_card);
        imgLoading = view.findViewById(R.id.icon_loading);
        tvNoHaveSong = view.findViewById(R.id.tv_no_have_song);
        rcvSong = view.findViewById(R.id.rcv_song);
        view.findViewById(R.id.btn_play_rand).setOnClickListener(v -> {
            List<SongModel> songs;
            if (rcvSong.getAdapter() instanceof SongAdapter) {
                songs = new ArrayList<>(((SongAdapter) rcvSong.getAdapter()).getListSong());
                Collections.shuffle(songs);
            } else {
                songs = null;
            }
            play(songs, 0);
        });

    }

    private void initToolBarAnimation() {
        AtomicBoolean isExpanded = new AtomicBoolean(false);
        Drawable drawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back);
        mToolbar.setNavigationIcon(drawable);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int maxOffset = appBarLayout.getHeight() - mToolbar.getHeight() - ScreenUtils.getStatusBarHeight(requireContext());
            float alpha = (float) Math.abs(verticalOffset) * 100 / maxOffset;
            if (rlDetailHeader.getAlpha() != alpha) {
                rlDetailHeader.setAlpha(1 - (float) Math.abs(verticalOffset) / maxOffset);
            }
            if (verticalOffset == 0 && !isExpanded.get()) {
                // Expanded
                isExpanded.set(true);
                if (isTopic()) {
                    BarsUtils.setAppearanceLightStatusBars(getActivity(), false);
                    mToolbar.setTitleTextColor(Color.WHITE);
                    Objects.requireNonNull(drawable).setTint(Color.WHITE);
                }
            } else if (Math.abs(verticalOffset) == maxOffset && isExpanded.get()) {
                isExpanded.set(false);
                if (isTopic()) {
                    BarsUtils.setAppearanceLightStatusBars(getActivity(), true);
                    mToolbar.setTitleTextColor(Color.BLACK);
                    Objects.requireNonNull(drawable).setTint(Color.BLACK);
                }
            }
            if (Math.abs(verticalOffset) == maxOffset) {
                mToolbar.setTitle(card.getName());
            } else {
                mToolbar.setTitle("");
            }
        });
    }

    private void initData() {
        if (isTopic()) {
            imgBackground.setForeground(null);
            GlideUtils.load(card.getImageUrl(), imgCard);
            return;
        }
        imgBackground.setForeground(ContextCompat.getDrawable(requireContext(), R.drawable.custom_overlay_card_black));
        GlideUtils.load(card.getImageUrl(), imgCard);
        GlideUtils.load(card.getImageUrl(), image -> {
            if (getActivity() == null) {
                return;
            }
            if (image != null) {
                imgBackground.setImageBitmap(BitmapUtils.blur(getActivity(), image, 25, 1));
            }
        });
        titleCard.setText(card.getName());
        if (card instanceof AlbumModel) {
            subTitleCard.setText(((AlbumModel) card).getSingerName());
            subTitleCard.setVisibility(View.VISIBLE);
        } else {
            subTitleCard.setVisibility(View.GONE);
        }
    }

    private void setDataCategory(List<CategoryModel> data) {
        rcvSong.setVisibility(View.VISIBLE);
        imgLoading.setVisibility(View.GONE);

        ((ViewGroup.MarginLayoutParams) rcvSong.getLayoutParams()).leftMargin = (int) getResources().getDimension(R.dimen.space_view);
        ((ViewGroup.MarginLayoutParams) rcvSong.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.space_view);
        CardAdapter adapter = new CardAdapter(new ArrayList<>(data), false, this);
        rcvSong.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), MainActivity.TWO_ITEM_CARD);
        rcvSong.setLayoutManager(manager);
    }

    private void setDataSong(List<SongModel> data) {
        imgLoading.setVisibility(View.GONE);
        imgLoading.pauseAnimation();

        if (data == null || data.isEmpty()) {
            tvNoHaveSong.setVisibility(View.VISIBLE);
            rcvSong.setVisibility(View.GONE);
        } else {
            tvNoHaveSong.setVisibility(View.GONE);
            rcvSong.setVisibility(View.VISIBLE);
            SongAdapter adapter = new SongAdapter(data, this);
            rcvSong.setAdapter(adapter);
            rcvSong.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private boolean isTopic() {
        return card instanceof TopicModel;
    }

    private void play(List<SongModel> songs, int index) {
        if (songs == null || songs.isEmpty()) {
            String type = card.getClass().getSimpleName().replace("Model", "");
            String msg = type + " is empty, please choose another " + type.toLowerCase() + "!";
            showToast(msg, ToastUtils.WARNING, true);
            return;
        }
        PlayMusicActivity.start(requireContext(), songs, index);
    }

    @NonNull
    @Override
    protected RecyclerView getScrollView() {
        return rcvSong;
    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {
        play(songs, position);
    }

    @Override
    public void onClickedAdd(SongModel song, int position) {

    }

    @Override
    public void getDataSongSuccess(List<SongModel> data) {
        setDataSong(data);
    }

    @Override
    public void getDataSongFailure(String error) {

    }

    @Override
    public void getDataAllCategorySuccess(List<CategoryModel> data) {
        setDataCategory(data);
    }

    @Override
    public void getDataAllCategoryFailure(String error) {

    }
}
