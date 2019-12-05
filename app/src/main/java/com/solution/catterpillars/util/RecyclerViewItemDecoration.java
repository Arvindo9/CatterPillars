package com.solution.catterpillars.util;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private final int spacing;
    private int displayMode;
    private boolean isOuterBound;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;

    public RecyclerViewItemDecoration(int spacing) {
        this(spacing, -1);
    }

    public RecyclerViewItemDecoration(int spacing, int displayMode) {
        this.spacing = spacing;
        this.displayMode = displayMode;
    }

    public RecyclerViewItemDecoration(int spacing, int displayMode, boolean isOuterBound) {
        this.spacing = spacing;
        this.displayMode = displayMode;
        this.isOuterBound = isOuterBound;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }

    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        switch (displayMode) {
            case HORIZONTAL:
                if (isOuterBound) {
                    outRect.left = spacing;
                    outRect.right = position == itemCount - 1 ? spacing : 0;
                }
                else {
                    outRect.left = position == 0 ? 0 : spacing;
                    outRect.right = 0;
                }

                outRect.top = spacing;
                outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.top = spacing;
                outRect.bottom = position == itemCount - 1 ? spacing : 0;

                if(isOuterBound){
                    outRect.left = spacing;
                    outRect.right = spacing;
                }else{
                    outRect.left = 0;
                    outRect.right = 0;
                }
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;


                    if(isOuterBound){
                        outRect.left = position % cols == 0 ? spacing : 0;
                        outRect.right = spacing;
                        outRect.top = spacing;
                        outRect.bottom = position / cols == rows - 1 ? 0 : spacing;
                    }else{
                        outRect.left = position % cols == 0 ? 0 : spacing;
                        outRect.right = 0;
                        outRect.top = spacing;
                        outRect.bottom = position / cols == rows - 1 ? 0 : spacing;
                    }
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}