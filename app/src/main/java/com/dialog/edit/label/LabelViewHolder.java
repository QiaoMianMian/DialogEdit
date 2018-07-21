package com.dialog.edit.label;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dialog.edit.R;

public class LabelViewHolder extends RecyclerView.ViewHolder {
    public LabelLayout editLayout;
    public TextView tvContent;
    public ImageView ivDelete;
    public ImageView ivChoice;
    public ImageView ivEdit;
    public ImageView ivAdd;

    public LabelViewHolder(View itemView, boolean footer) {
        super(itemView);
        if (footer) {
            ivAdd = itemView.findViewById(R.id.iv_label_add);
        } else {
            editLayout = itemView.findViewById(R.id.edt_label_layout);
            tvContent = itemView.findViewById(R.id.tv_label_content);
            ivDelete = itemView.findViewById(R.id.iv_label_delete);
            ivChoice = itemView.findViewById(R.id.iv_label_choice);
            ivEdit = itemView.findViewById(R.id.iv_label_edit);
        }
    }
}
