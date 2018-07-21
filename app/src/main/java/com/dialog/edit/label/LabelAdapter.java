package com.dialog.edit.label;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.edit.R;
import com.dialog.edit.units.SPUtils;
import com.dialog.edit.units.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表适配器
 */
public class LabelAdapter extends RecyclerView.Adapter {

    public final int TYPE_FOOTER = 0;
    public final int TYPE_NORMAL = 1;

    protected Context mContext;
    protected List<LabelEntry> mList;

    private boolean isEdit;  //是否处于编辑状态
    private List<LabelLayout> allItems = new ArrayList<>();

    public View mFooterView;

    private LabelImp mLabelImp;

    public void setFooterView(View footerView) {
        mFooterView = footerView;
    }

    public LabelAdapter(Context context, List<LabelEntry> List) {
        this.mContext = context;
        this.mList = List;
    }

    public void setLabelImp(LabelImp mLabelImp) {
        this.mLabelImp = mLabelImp;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new LabelViewHolder(mFooterView, true);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.label_item_list, parent, false);
        return new LabelViewHolder(view, false);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mList.size()) return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LabelViewHolder viewHolder = (LabelViewHolder) holder;
        if (getItemViewType(position) == TYPE_NORMAL) {
            LabelLayout editLayout = viewHolder.editLayout;

            if (!allItems.contains(editLayout)) {
                allItems.add(editLayout);
            }

            editLayout.setEdit(isEdit);

            LabelEntry entry = mList.get(position);
            if (entry != null) {
                viewHolder.tvContent.setText(entry.getContent());
                viewHolder.ivChoice.setSelected(entry.isChoice());
            }

            viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = viewHolder.getAdapterPosition();
                    if (position >= 0) {
                        mList.remove(position);
                        notifyItemRemoved(position);
                        if (position != mList.size()) {
                            notifyItemRangeChanged(position, mList.size() - position);
                        }
                    }
                }
            });

            viewHolder.ivChoice.setOnClickListener(v -> {
                int position1 = viewHolder.getAdapterPosition();
                if (position1 >= 0) {
                    LabelEntry entry1 = mList.get(position1);
                    if (entry1 != null) {
                        entry1.setChoice(!entry1.isChoice());
                    }
                    notifyDataSetChanged();
                }
            });

            viewHolder.ivEdit.setOnClickListener(v -> {
                int position12 = viewHolder.getAdapterPosition();
                LabelEntry entry12 = mList.get(position12);
                mLabelImp.onModify(entry12);
            });
        } else {
            viewHolder.ivAdd.setOnClickListener(v -> mLabelImp.onAdd());
        }
    }

    @Override
    public int getItemCount() {
        return mFooterView == null ? mList.size() : mList.size() + 1;
    }

    //设置编辑状态
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            openLeftAll();
            mFooterView.setVisibility(View.INVISIBLE);
        } else {
            closeAll();
            mFooterView.setVisibility(View.VISIBLE);
        }
        for (LabelLayout editLayout : allItems) {
            editLayout.setEdit(isEdit);
        }
    }

    //关闭所有 item
    private void closeAll() {
        for (LabelLayout editLayout : allItems) {
            editLayout.close();
        }
    }

    //将所有 item 向左展开
    private void openLeftAll() {
        for (LabelLayout editLayout : allItems) {
            editLayout.openLeft();
        }
    }

    //标签是否重复
    public boolean isLabelRepeat(String content) {
        for (LabelEntry e : mList) {
            if (e == null) continue;
            String _content = e.getContent();
            if (TextUtils.equals(_content, content)) {
                return true;
            }
        }
        return false;
    }

    //添加新标签
    public void addLabel(LabelEntry entry) {
        if (entry == null) return;
        mList.add(entry);
        notifyDataSetChanged();
    }

    //修改标签
    public void editLabel(LabelEntry entry, String label) {
        if (entry == null) return;
        entry.setContent(label);
        notifyDataSetChanged();
    }

    public void saveLabel() {
        SPUtils.setParam(mContext, SPUtils.SP_LOCAL_LABEL, StringUtils.obj2String((mList)));
    }
}
