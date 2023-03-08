package com.aptech.mymusic.presentation.view.fragment.mainpager;

import static com.aptech.mymusic.presentation.view.service.MusicDelegate.Action.PLAY_NEW_SONG;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.activity.ISendDataToDetail;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.aptech.mymusic.presentation.view.service.MusicStarter;
import com.mct.components.baseui.BaseFragment;

import java.util.List;

public abstract class BaseTabFragment extends BaseFragment implements ISendDataToDetail {

    @Override
    public void onResume() {
        super.onResume();
        MainFragment fragment = getMainParentFragment();
        if (fragment != null) {
            fragment.getAppBarLayout().setLiftOnScrollTargetView(getScrollView());
            fragment.getAppBarLayout().setLifted(getScrollView().computeVerticalScrollOffset() != 0);
        }
    }

    @NonNull
    protected abstract RecyclerView getScrollView();

    @Nullable
    private MainFragment getMainParentFragment() {
        Fragment fragment = getParentFragmentManager().findFragmentByTag(MainFragment.class.getName());
        if (fragment instanceof MainFragment) {
            return (MainFragment) fragment;
        }
        return null;
    }

    @Override
    public void sendDataListener(CardModel model, Action action) {
        if (action == Action.PLAY) {
            if (model instanceof SongModel) {
                goToPlayMusicActivity((SongModel) model);
            } else {

            }
        }
        if (action == Action.SHOW_MODAL) {

        }
    }

    @Override
    public void sendDataListener(List<SongModel> songs, int index) {

    }

    private void goToPlayMusicActivity(SongModel song) {
        MusicStarter.startService(PLAY_NEW_SONG, song);
        startActivity(new Intent(getContext(), PlayMusicActivity.class));
    }

}
