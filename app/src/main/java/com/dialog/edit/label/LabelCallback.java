package com.dialog.edit.label;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * 退拽排序回调
 */

public class LabelCallback extends ItemTouchHelper.Callback {

    public LabelCallback() {

    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 允许上下拖拽
        //int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 禁止上下拖拽
        int dragFlags = 0;
        // 禁止左右拖拽
        int swipeFlags = 0;
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }
}
