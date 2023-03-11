package com.aptech.mymusic.presentation.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aptech.mymusic.R;
import com.aptech.mymusic.domain.entity.AlbumModel;
import com.aptech.mymusic.domain.entity.CardModel;
import com.aptech.mymusic.domain.entity.SongModel;
import com.aptech.mymusic.presentation.view.activity.MainActivity;
import com.aptech.mymusic.utils.GlideUtils;
import com.mct.components.utils.ScreenUtils;

import java.lang.ref.WeakReference;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardItemViewHolder> {

    private final Context context;
    private final List<CardModel> cardModelList;
    private final boolean isLinearLayoutManager;
    private final ICardListener mICardListener;

    public CardAdapter(Context context, List<CardModel> cardModelList, boolean isLinearLayoutManager, ICardListener mICardListener) {
        this.context = context;
        this.cardModelList = cardModelList;
        this.isLinearLayoutManager = isLinearLayoutManager;
        this.mICardListener = mICardListener;
    }

    @NonNull
    @Override
    public CardItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardItemViewHolder holder, int position) {
        CardModel card = cardModelList.get(position);
        if (card == null) {
            return;
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemCard.getLayoutParams();
        int margin = (int) context.getResources().getDimension(R.dimen.space_view);

        if (isLinearLayoutManager) {
            if (position == 0) {
                // margin left cho item đầu tiên
                layoutParams.leftMargin = margin;
            }
        } else {
            // margin top cho item
            layoutParams.topMargin = margin;
            // Độ dài tối đa của 1 cell in grid layout
            int itemLength = (ScreenUtils.getScreenWidth(holder.itemView.getContext()) - (margin * (1 + MainActivity.TWO_ITEM_CARD))) / MainActivity.TWO_ITEM_CARD;
            layoutParams.width = itemLength;
            holder.imgThumb.get().getLayoutParams().height = itemLength;
        }
        // viết api trả data
        GlideUtils.load(card.getImageUrl(), holder.imgThumb.get());
        holder.titleCard.setText(card.getName());

        if (card instanceof AlbumModel) {
            holder.subTitleCard.setText(((AlbumModel) card).getSingerName());
            holder.subTitleCard.setVisibility(View.VISIBLE);
        }
        if (card instanceof SongModel) {
            holder.subTitleCard.setText(((SongModel) card).getSingerName());
            holder.subTitleCard.setVisibility(View.VISIBLE);
        }

        // set view click listener
        holder.itemCard.setOnClickListener(view -> {
            if (card instanceof SongModel) {
                mICardListener.onCardClicked(card, ICardListener.Action.PLAY);
            } else {
                mICardListener.onCardClicked(card, ICardListener.Action.SHOW_MODAL);
            }
        });

        holder.btnPlayCard.setOnClickListener(view -> mICardListener.onCardClicked(card, ICardListener.Action.PLAY));
    }

    @Override
    public int getItemCount() {
        if (cardModelList != null) {
            return cardModelList.size();
        }
        return 0;
    }

    public static class CardItemViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout itemCard;
        ImageView btnPlayCard;
        WeakReference<ImageView> imgThumb;
        TextView titleCard, subTitleCard;

        public CardItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCard = itemView.findViewById(R.id.rl_item_card);
            imgThumb = new WeakReference<>(itemView.findViewById(R.id.img_thumb));
            btnPlayCard = itemView.findViewById(R.id.btn_play_card);
            titleCard = itemView.findViewById(R.id.tv_title_card);
            subTitleCard = itemView.findViewById(R.id.tv_sub_title_card);
        }
    }


}
