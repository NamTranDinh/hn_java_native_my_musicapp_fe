package com.aptech.mymusic.presentation.view.fragment.mainpager;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.TopicModel;
import com.aptech.mymusic.presentation.view.activity.PlayMusicActivity;
import com.aptech.mymusic.presentation.view.adapter.ICardListener;
import com.aptech.mymusic.presentation.view.fragment.MainFragment;
import com.aptech.mymusic.presentation.view.fragment.detailpager.DetailCardFragment;
import com.mct.components.baseui.BaseActivity;
import com.mct.components.baseui.BaseFragment;
import com.mct.components.utils.ToastUtils;

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
            goToPlayMusicActivity(model);
        }
        if (action == Action.SHOW_MODAL) {
            goToDetailCard(model);
        }
    }

    private void goToPlayMusicActivity(CardModel model) {
        PlayMusicActivity.start(requireContext(), model);
    }

    private void goToDetailCard(CardModel model) {
        DetailCardFragment fragment = DetailCardFragment.newInstance(model);
        replaceFragment(fragment, true, BaseActivity.Anim.TRANSIT_FADE);
    }

}
