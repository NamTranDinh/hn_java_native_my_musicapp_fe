package com.aptech.mymusic.presentation.view.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.aptech.mymusic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircularAvatarUser extends RelativeLayout {

    private CircleImageView cvAvatar;
    private int pathImg;
    private int paddingVertical;
    private int paddingHorizontal;
    private int paddingLeft;
    private int paddingRight;

    public CircularAvatarUser(Context context) {
        super(context);
    }

    public CircularAvatarUser(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.custom_circular_avatar_user, this, true);
        initView(view);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CircularAvatarUser);
        try {
            pathImg = typedArray.getResourceId(R.styleable.CircularAvatarUser_backgroundImage, -1);
            paddingVertical = typedArray.getResourceId(R.styleable.CircularAvatarUser_paddingVertical, 0);
            paddingHorizontal = typedArray.getResourceId(R.styleable.CircularAvatarUser_paddingHorizontal, 0);
            paddingLeft = typedArray.getResourceId(R.styleable.CircularAvatarUser_paddingLeft, 0);
            paddingRight = typedArray.getResourceId(R.styleable.CircularAvatarUser_paddingRight, 0);

            Log.d("TAG", "CircularAvatarUser: " + paddingRight);
        } finally {
            typedArray.recycle();
        }
        settAttrsForView();
    }

    private void settAttrsForView() {
        if (pathImg == -1) {
            cvAvatar.setImageResource(R.drawable.test_bg);
        } else {
            cvAvatar.setImageResource(pathImg);
        }
        if (paddingVertical == 0) {
            cvAvatar.setPadding(0, 0, 0, 0);
        } else {
            cvAvatar.setPadding(0, paddingVertical, 0, paddingVertical);
        }
        if (paddingHorizontal == 0) {
            cvAvatar.setPadding(0, 0, 0, 0);
        } else {
            cvAvatar.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);
        }
        if (paddingLeft == 0) {
            cvAvatar.setPadding(0, 0, 0, 0);
        } else {
            cvAvatar.setPadding(paddingLeft, 0, 0, 0);
        }
        if (paddingRight == 0) {
            cvAvatar.setPadding(0, 0, 0, 0);
        } else {
            cvAvatar.setPadding(0, 0, paddingRight, 0);
        }

    }

    private void initView(View view) {
        cvAvatar = view.findViewById(R.id.cv_avatar);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    @Override
    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public CircleImageView getCvAvatar() {
        return cvAvatar;
    }

    public void setCvAvatar(CircleImageView cvAvatar) {
        this.cvAvatar = cvAvatar;
    }

    public int getPathImg() {
        return pathImg;
    }

    public void setPathImg(int pathImg) {
        this.pathImg = pathImg;
    }

    public int getPaddingVertical() {
        return paddingVertical;
    }

    public void setPaddingVertical(int paddingVertical) {
        this.paddingVertical = paddingVertical;
    }

    public int getPaddingHorizontal() {
        return paddingHorizontal;
    }

    public void setPaddingHorizontal(int paddingHorizontal) {
        this.paddingHorizontal = paddingHorizontal;
    }
}
