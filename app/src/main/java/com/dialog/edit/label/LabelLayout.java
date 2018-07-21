package com.dialog.edit.label;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 侧滑删除控件
 */
public class LabelLayout extends FrameLayout {

    private int mWidth;         //内容部分宽度
    private int mHeight;        //内容部分高度

    private TextView tvContent;  //内容部分

    private ImageView ivDelete;     //左边圆形删除按键
    private int mDeleteWidth;     //左边圆形删除按键宽度

    private ImageView ivChoice;     //选择按钮
    private ImageView ivEdit;     //编辑

    private ViewDragHelper mDragHelper;
    private boolean isEdit;     //是否为编辑状态

    public LabelLayout(Context context) {
        this(context, null);
    }

    public LabelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return false;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == tvContent) {
                    ivDelete.offsetLeftAndRight(dx);
                }
                invalidate();
                //左侧展开时，修改 tvContent 宽度
                if (left == mDeleteWidth) {
                    tvContent.layout(mDeleteWidth, 0, mWidth, mHeight);
                }
            }
        };

        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvContent = (TextView) getChildAt(0);
        ivDelete = (ImageView) getChildAt(1);
        ivChoice = (ImageView) getChildAt(2);
        ivEdit = (ImageView) getChildAt(3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);
        mWidth = w;
        mHeight = h;
        mDeleteWidth = ivDelete.getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //判断是否为编辑模式,摆放每个子View的位置
        if (isEdit) {
            tvContent.layout(mDeleteWidth, 0, mWidth, mHeight);
            ivDelete.layout(0, 0, mDeleteWidth, mHeight);
        } else {
            tvContent.layout(0, 0, mWidth, mHeight);
            ivDelete.layout(-mDeleteWidth, 0, 0, mHeight);
        }
    }

    @Override
    public void computeScroll() {
        invalidate();
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //设置编辑状态
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            ivChoice.setVisibility(GONE);
            ivEdit.setVisibility(VISIBLE);
        } else {
            ivChoice.setVisibility(VISIBLE);
            ivEdit.setVisibility(GONE);
        }
    }

    //展开左侧
    public void openLeft() {
        //滑动到左侧展开位置
        mDragHelper.smoothSlideViewTo(tvContent, mDeleteWidth, 0);
        invalidate();
    }

    //关闭左侧
    public void close() {
        //将 tvContent 宽度复原
        if (tvContent.getLeft() > 0) {
            //左边展开复原
            tvContent.layout(mDeleteWidth, 0, mWidth + mDeleteWidth, mHeight);
            ivDelete.layout(0, 0, mDeleteWidth, mHeight);
        } else {
            //右边展开复原
            tvContent.layout(0, 0, mWidth, mHeight);
            ivDelete.layout(-mDeleteWidth, 0, 0, mHeight);
        }
        //滑动到关闭位置
        mDragHelper.smoothSlideViewTo(tvContent, 0, 0);
        invalidate();
    }

}
