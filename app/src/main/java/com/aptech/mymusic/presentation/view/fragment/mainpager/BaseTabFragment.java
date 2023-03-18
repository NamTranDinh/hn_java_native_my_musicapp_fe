package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.CategoryModel;
import com.aptech.mymusic.domain.entity.PlaylistModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.presenter.Callback;
import com.aptech.mymusic.presentation.presenter.HomePresenter;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.ICardListener;
import com.aptech.mymusic.presentation.view.dialog.LoadingDialog;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.aptech.mymusic.presentation.view.fragment.detailpager.DetailCardFragment;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.utils.ToastUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseTabFragment extends BaseFragment implements ICardListener {

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
    public void onCardClicked(CardModel model, Action action) {
        if (action == Action.PLAY) {
            if (model instanceof SongModel) {
                startPlayMusicActivity((SongModel) model);
            }
            String type = "";
            if (model instanceof AlbumModel) {
                type = "album";
            }
            if (model instanceof CategoryModel) {
                type = "category";
            }
            if (model instanceof PlaylistModel) {
                type = "playlist";
            }
            if (!type.isEmpty()) {
                long startTime = SystemClock.elapsedRealtime();
                HomePresenter presenter = new HomePresenter(this);
                AtomicBoolean isRelease = new AtomicBoolean(false);
                LoadingDialog dialog = new LoadingDialog(requireContext(), dl -> {
                    isRelease.set(true);
                    presenter.release();
                    dl.dismiss();
                });
                dialog.create(null);
                presenter.getDataAllSong(type, model.getId(), new Callback.GetDataAllSongCallBack() {
                    @Override
                    public void getDataSongSuccess(List<SongModel> songs) {
                        if (isRelease.get()) {
                            return;
                        }
                        final int MIN_TIME_PROCESS = 500;
                        long delay = 0;
                        long timeProcess = SystemClock.elapsedRealtime() - startTime;
                        if (timeProcess < MIN_TIME_PROCESS) {
                            delay = MIN_TIME_PROCESS - timeProcess;
                        }
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (isRelease.get()) {
                                return;
                            }
                            dialog.dismiss();
                            presenter.release();
                            if (songs == null || songs.isEmpty()) {
                                String type = model.getClass().getSimpleName().replace("Model", "");
                                String msg = type + " is empty, please choose another " + type.toLowerCase() + "!";
                                showToast(msg, ToastUtils.WARNING, true);
                                return;
                            }
                            startPlayMusicActivity(songs);
                        }, delay);
                    }

                    @Override
                    public void getDataSongFailure(String error) {
                        dialog.dismiss();
                        presenter.release();
                    }
                });
            }
        }
        if (action == Action.SHOW_MODAL) {
            openDetailCard(model);
        }
    }

    private void startPlayMusicActivity(SongModel song) {
        PlayMusicActivity.start(requireContext(), song);
    }

    private void startPlayMusicActivity(List<SongModel> songs) {
        PlayMusicActivity.start(requireContext(), songs, 0);
    }

    private void openDetailCard(CardModel model) {
        DetailCardFragment fragment = DetailCardFragment.newInstance(model);
        replaceFragment(fragment, true, BaseActivity.Anim.TRANSIT_FADE);
    }

}
