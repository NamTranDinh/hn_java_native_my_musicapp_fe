package com.aptech.mymusic.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AdsModel;
import com.aptech.mymusic.utils.GlideUtils;

import java.util.List;

public class BannerAdapter extends PagerAdapter {
    private final Context context;
    private final List<AdsModel> mAdsModelList;

    public BannerAdapter(Context context, List<AdsModel> mAdsModelList) {
        this.context = context;
        this.mAdsModelList = mAdsModelList;
    }

    @Override
    public int getCount() {
        if (mAdsModelList != null) {
            return mAdsModelList.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, container, false);

        ImageView imgBackground = view.findViewById(R.id.img_bg_banner);
        ImageView imgSongIcon = view.findViewById(R.id.img_ic_banner);
        TextView titleBanner = view.findViewById(R.id.tv_title_banner);
        TextView contentBanner = view.findViewById(R.id.tv_content_banner);

        AdsModel ads = mAdsModelList.get(position);

        GlideUtils.load(ads.getImageUrl(), imgBackground);
        GlideUtils.load(ads.getSong().getImageUrl(), imgSongIcon);
        titleBanner.setText(ads.getSong().getName());
        contentBanner.setText(ads.getContent());

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
