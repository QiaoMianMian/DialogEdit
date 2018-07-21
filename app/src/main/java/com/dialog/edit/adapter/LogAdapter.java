package com.dialog.edit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dialog.edit.R;
import com.dialog.edit.label.LabelEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter implements Filterable {

    private Context mContext;
    private List<LabelEntry> mLabelEntries;
    private List<LabelEntry> mLabelCaches; //缓存

    private OnItemClickListener mItemClickListener;

    private LogFilter mLogFilter;

    public LogAdapter(Context context, List<LabelEntry> List) {
        this.mContext = context;
        this.mLabelEntries = List;
        mLabelCaches = mLabelEntries;
    }

    @Override
    public Filter getFilter() {
        if (mLogFilter == null) {
            mLogFilter = new LogFilter();
        }
        return mLogFilter;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_log_item, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LogViewHolder viewHolder = (LogViewHolder) holder;
        LabelEntry entry = mLabelEntries.get(position);
        if (entry != null) {
            viewHolder.tvLogContent.setText(entry.getContent());
            viewHolder.tvLogNumber.setText("" + entry.getNumber());
            if (entry.isChoice()) {
                viewHolder.ivLogLeft.setImageResource(R.mipmap.ic_log_selected);
                viewHolder.tvLogContent.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                viewHolder.tvLogNumber.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
            } else {
                viewHolder.ivLogLeft.setImageResource(R.mipmap.ic_log_unselected);
                viewHolder.tvLogContent.setTextColor(mContext.getResources().getColor(R.color.colorBlackDark));
                viewHolder.tvLogNumber.setTextColor(mContext.getResources().getColor(R.color.colorBlackDark));
            }
        }
        viewHolder.lltLogItem.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLabelEntries.size();
    }

    //选中或取消子项
    public void doModifyLog(int position) {
        if (mLabelCaches == null) return;
        LabelEntry entry = mLabelCaches.get(getCachePosition(position));
        if (entry == null) return;
        boolean isChoice = entry.isChoice();
        int number = entry.getNumber();
        if (isChoice) {
            if (number == 1) {
                mLabelCaches.remove(position); //删除睡眠标签
            } else {
                entry.setChoice(!isChoice);
                entry.setNumber(number - 1);
            }
        } else {
            entry.setChoice(!isChoice);
            entry.setNumber(number + 1);
        }
        Collections.sort(mLabelCaches, new LogComparator());
    }

    //获取缓存中位置
    public int getCachePosition(int position) {
        if (mLabelEntries != null) {
            int size = mLabelEntries.size();
            if (size > position) {
                String content = mLabelEntries.get(position).getContent();
                for (int i = 0; i < mLabelCaches.size(); i++) {
                    String _content = mLabelCaches.get(i).getContent();
                    if (TextUtils.equals(content, _content)) {
                        return i;
                    }
                }
            }
        }
        return 0;
    }

    //新增睡眠标签
    public void doAddLog(String content) {
        if (!TextUtils.isEmpty(content)) {
            if (!isRepeat(content)) {
                mLabelCaches.add(new LabelEntry(content, true, 1));
            } else {
                for (LabelEntry entry : mLabelCaches) {
                    if (TextUtils.equals(content.toLowerCase(), entry.getContent().toLowerCase())) {
                        entry.setNumber(entry.getNumber() + 1);
                        entry.setChoice(true);
                    }
                }
            }
        }
        Collections.sort(mLabelCaches, new LogComparator());
    }

    public List<LabelEntry> getLabelCaches() {
        return mLabelCaches;
    }

    // 是否重复
    public boolean isRepeat(String content) {
        for (LabelEntry entry : getLabelCaches()) {
            String _content = entry.getContent();
            if (TextUtils.isEmpty(_content)) continue;
            if (TextUtils.equals(content, _content)) {
                return true;
            }
        }
        return false;
    }

    //Holder类(视图)
    class LogViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout lltLogItem;
        public ImageView ivLogLeft;
        public TextView tvLogContent;
        public TextView tvLogNumber;

        public LogViewHolder(View itemView) {
            super(itemView);
            lltLogItem = itemView.findViewById(R.id.llt_log_item);
            ivLogLeft = itemView.findViewById(R.id.iv_log_left);
            tvLogContent = itemView.findViewById(R.id.tv_log_content);
            tvLogNumber = itemView.findViewById(R.id.tv_log_number);
        }
    }

    //Comparator类(比较)
    class LogComparator implements Comparator<LabelEntry> {

        @Override
        public int compare(LabelEntry o1, LabelEntry o2) {
            if (o1.isChoice() && !o2.isChoice()) {//被选中的往前排
                return -1;
            } else if (o1.isChoice() && o2.isChoice()) {//如果都被选中,则Number大的往前排
                if (o1.getNumber() > o2.getNumber()) {
                    return -1;
                } else if (o1.getNumber() == o2.getNumber()) {
                    return 0;
                }
            } else if (!o1.isChoice() && !o2.isChoice()) {//如果都被未选中,则Number大的往前排
                if (o1.getNumber() > o2.getNumber()) {
                    return -1;
                } else if (o1.getNumber() == o2.getNumber()) {
                    return 0;
                }
            }
            return 1;
        }
    }

    //Filter(检索)
    class LogFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults result = new FilterResults();
            List<LabelEntry> list;
            if (TextUtils.isEmpty(charSequence)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = mLabelCaches;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                String query = charSequence.toString().toLowerCase();
                for (LabelEntry entry : mLabelCaches) {
                    String content = entry.getContent().toLowerCase();
                    if (TextUtils.isEmpty(content)) continue;
                    if (content.contains(query)) {
                        list.add(entry);
                    }
                }
            }
            result.values = list; //将得到的集合保存到FilterResults的value变量中
            result.count = list.size();//将集合的大小保存到FilterResults的count变量中
            return result;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mLabelEntries = (List<LabelEntry>) filterResults.values;
            if (filterResults.count > 0) {
                Collections.sort(mLabelEntries, new LogComparator());
                notifyDataSetChanged();//通知数据发生了改变
            }
        }
    }
}

