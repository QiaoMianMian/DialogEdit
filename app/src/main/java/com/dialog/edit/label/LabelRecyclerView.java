package com.dialog.edit.label;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

/**
 * 自定义 RecyclerView，判断是否可滑动
 */
public class LabelRecyclerView extends RecyclerView {

    private ItemTouchHelper mItemTouchHelper;

    public LabelRecyclerView(Context context) {
        this(context, null);
    }

    public LabelRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setItemAnimator(new DefaultItemAnimator());
        mItemTouchHelper = new ItemTouchHelper(new LabelCallback());
        mItemTouchHelper.attachToRecyclerView(this);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }
}
