package com.example.keping.newsofmine.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesHorizontalDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesHorizontalDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.right = space;
        }
    }

}
