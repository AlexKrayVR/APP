package com.alex.vr_party_app.support;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int offsetEnd = 0;
    private int offsetStart = 0;

    public ItemOffsetDecoration(int offsetEnd) {
        this.offsetEnd = offsetEnd;
    }


    public ItemOffsetDecoration(int offsetEnd, int offsetStart) {
        this.offsetEnd = offsetEnd;
        this.offsetStart = offsetStart;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) == 0){
            outRect.left = offsetStart;
        }

        // Добавление отступов к нулевому элементу
        if (parent.getChildAdapterPosition(view) == (state.getItemCount() - 1)) {
            outRect.right = offsetEnd;
            //outRect.left = offset;
            //outRect.top = offset;
            //outRect.bottom = offset;
        }
    }
}


