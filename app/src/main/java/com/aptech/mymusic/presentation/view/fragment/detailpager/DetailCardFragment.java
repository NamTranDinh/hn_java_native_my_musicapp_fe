package com.aptech.mymusic.presentation.view.fragment.detailpager;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.presentation.view.adapter.CardAdapter;
import com.aptech.mymusic.presentation.view.adapter.SongAdapter;
import com.aptech.mymusic.presentation.view.fragment.mainpager.BaseTabFragment;
import com.aptech.mymusic.utils.BitmapUtils;
import com.aptech.mymusic.utils.GlideUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.aptech.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class DetailCardFragment extends BaseTabFragment implements SongAdapter.ItemClickedListener, Callback.GetDataAllSongCallBack, Callback.GetDataAllCategoryCallBack {

    private static final String KEY_CARD_MODEL = "KEY_CARD_MODEL";
    private CardModel card;
    private View view;
    private View overlay;
    private ImageView imgBackground;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private RelativeLayout rlDetailHeader;
    private ImageView imgCard;
    private TextView titleCard;
    private TextView subTitleCard;
    private Button btnPlayRand;
    private RecyclerView rcvSong;

    private HomePresenter mHomePresenter;

    public static DetailCardFragment newInstance(CardModel cardModel) {
        Bundle args = new Bundle();
        args.putSerializable(KEY_CARD_MODEL, cardModel);
        DetailCardFragment fragment = new DetailCardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).dispatchShowControlLayout(false);
        }
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
        view = inflater.inflate(R.layout.fragment_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        initToolBarAnimation();

        if (card instanceof TopicModel) {
            mHomePresenter.getDataAllCategory(((TopicModel) card).getId(), this);
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

    private void getDataRemote() {
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
            ((MainActivity) getActivity()).dispatchShowControlLayout(true);
        }
    }

    private void initUi() {
        overlay = view.findViewById(R.id.overlay);
        imgBackground = view.findViewById(R.id.img_background);
        mAppBarLayout = view.findViewById(R.id.app_bar_layout);
        mToolbar = view.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> popLastFragment());
        if (card instanceof TopicModel) {
            rlDetailHeader = view.findViewById(R.id.rl_detail_header_with_list_category);
            imgCard = view.findViewById(R.id.img_bg_topic);
        } else {
            rlDetailHeader = view.findViewById(R.id.rl_detail_header_with_list_song);
            imgCard = view.findViewById(R.id.img_thumb);
        }
        titleCard = view.findViewById(R.id.tv_title_card);
        subTitleCard = view.findViewById(R.id.tv_sub_title_card);
        btnPlayRand = view.findViewById(R.id.btn_play_rand);
        rcvSong = view.findViewById(R.id.rcv_song);
        rcvSong.setHasFixedSize(true);
    }

    private void initToolBarAnimation() {
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int maxOffset = appBarLayout.getHeight() - (int) requireContext().getResources().getDimension(R.dimen.custom_action_bar_size);
            float alpha = (float) Math.abs(verticalOffset) * 100 / maxOffset;
            if (rlDetailHeader.getAlpha() != alpha) {
                rlDetailHeader.setAlpha(1 - (float) Math.abs(verticalOffset) / maxOffset);
                if (Math.abs(verticalOffset) == maxOffset) {
                    mToolbar.setTitle(card.getName());
                } else {
                    mToolbar.setTitle("");

                }
            }
        });
    }

    private void setDataCategory(List<CategoryModel> data) {
        rlDetailHeader.setVisibility(View.VISIBLE);
        GlideUtils.load(card.getImageUrl(), imgCard);
        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary));
        overlay.setBackgroundResource(R.drawable.custom_overlay_card_white);
        ((RelativeLayout.LayoutParams) rcvSong.getLayoutParams()).leftMargin = (int) getResources().getDimension(R.dimen.space_view);
        ((RelativeLayout.LayoutParams) rcvSong.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.space_view);

        CardAdapter adapter = new CardAdapter(new ArrayList<>(data), true, this);
        rcvSong.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), MainActivity.TWO_ITEM_CARD);
        rcvSong.setLayoutManager(manager);
    }


    private void setDataSongAdapter(List<SongModel> data) {
        rlDetailHeader.setVisibility(View.VISIBLE);
        GlideUtils.load(card.getImageUrl(), imgCard);
        GlideUtils.load(card.getImageUrl(), img -> {
            if (img == null) {
                img = BitmapFactory.decodeResource(getResources(), R.drawable.custom_overlay_black);
            }
            imgBackground.setImageBitmap(BitmapUtils.blur(getContext(), img, 25, 1));
        });

        titleCard.setText(card.getName());
        if (card instanceof AlbumModel) {
            subTitleCard.setText(((AlbumModel) card).getSingerName());
            subTitleCard.setVisibility(View.VISIBLE);
        }

        if (data == null || data.isEmpty()) {
            view.findViewById(R.id.tv_no_have_song).setVisibility(View.VISIBLE);
            rcvSong.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        } else {
            SongAdapter adapter = new SongAdapter((List<SongModel>) data, this);
            rcvSong.setAdapter(adapter);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            rcvSong.setLayoutManager(manager);
        }
    }

    @NonNull
    @Override
    protected RecyclerView getScrollView() {
        return rcvSong;
    }

    @Override
    public void onClickedItem(List<SongModel> songs, SongModel song, int position) {

    }

    @Override
    public void onClickedAdd(SongModel song, int position) {

    }

    @Override
    public void getDataSongSuccess(List<SongModel> data) {
        setDataSongAdapter(data);
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
