package com.dialog.edit.dialog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dialog.edit.R;
import com.dialog.edit.adapter.LabelAdapter;
import com.dialog.edit.label.LabelDivider;
import com.dialog.edit.label.LabelEntry;
import com.dialog.edit.label.LabelEnum;
import com.dialog.edit.label.LabelImp;
import com.dialog.edit.label.LabelInterface;
import com.dialog.edit.label.LabelRecyclerView;
import com.dialog.edit.units.AppUtils;
import com.dialog.edit.units.DensityUtils;
import com.dialog.edit.units.LayoutUtils;
import com.dialog.edit.units.SPUtils;
import com.dialog.edit.units.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LabelDialog extends DialogFragment implements LabelImp, View.OnClickListener {

    private View mParentView;
    private TextView tvLeft;
    private ImageView ivLeft;
    private TextView tvMiddle;
    private TextView tvDate;
    private ImageView ivRight;
    private LabelRecyclerView lrvLabel;
    private EditText edtLabel;
    private TextView tvFinish;

    private boolean isLeftOpened; //左侧抽屉是否打开标记

    private LabelAdapter mAdapter;

    private LabelEntry mModifyEntry;

    private LabelEnum mLabelEnum = LabelEnum.NONE;

    List<LabelEntry> mEntryList = new ArrayList<>();

    private LabelInterface mLabelInterface;

    public void setLabelInterface(LabelInterface mLabelInterface) {
        this.mLabelInterface = mLabelInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        return inflater.inflate(R.layout.dialog_label, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        initViews(view);
        initRecyclerView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    private void initData() {
        mEntryList = (List<LabelEntry>) StringUtils.str2Object((String) SPUtils.getParam(getActivity(),
                SPUtils.SP_LOCAL_LABEL, ""));
        if (mEntryList == null) {
            mEntryList = new ArrayList<>();
            mEntryList.add(new LabelEntry(getString(R.string.sleep_log_water), false, 5));
            mEntryList.add(new LabelEntry(getString(R.string.sleep_log_eat), false, 4));
            mEntryList.add(new LabelEntry(getString(R.string.sleep_log_sport), false, 4));
            mEntryList.add(new LabelEntry(getString(R.string.sleep_log_walk), false, 3));
            mEntryList.add(new LabelEntry(getString(R.string.sleep_log_tv), false, 2));
            mEntryList.add(new LabelEntry(getString(R.string.sleep_log_wine), false, 1));
        }
    }

    private void initViews(View view) {
        mParentView = view.findViewById(R.id.label_parent_llt);
        LayoutUtils.setClipViewCornerRadius(mParentView, DensityUtils.dip2px(getContext(), 10));

        tvLeft = view.findViewById(R.id.tv_label_left);
        tvLeft.setOnClickListener(this);

        ivLeft = view.findViewById(R.id.iv_label_left);
        ivLeft.setOnClickListener(this);

        tvMiddle = view.findViewById(R.id.tv_label_middle);
        tvDate = view.findViewById(R.id.tv_label_date);

        ivRight = view.findViewById(R.id.iv_label_right);
        ivRight.setOnClickListener(this);

        lrvLabel = view.findViewById(R.id.lav_label);
        edtLabel = view.findViewById(R.id.edt_label);
        edtLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    int len = s.length();
                    tvMiddle.setText(len + "/20");
                }
            }
        });

        tvFinish = view.findViewById(R.id.tv_label_finish);
        tvFinish.setOnClickListener(this);
    }

    //编辑状态
    private void doLabelMode(LabelEnum mode) {
        mLabelEnum = mode;
        if (mode == LabelEnum.NONE || mode == LabelEnum.OPEN) {
            tvLeft.setVisibility(View.VISIBLE);
            ivLeft.setVisibility(View.GONE);
            lrvLabel.setVisibility(View.VISIBLE);
            edtLabel.setVisibility(View.GONE);
            tvFinish.setVisibility(View.VISIBLE);
            tvDate.setVisibility(View.VISIBLE);
            tvMiddle.setTextColor(getResources().getColor(R.color.colorBlackDark));
            tvMiddle.setText(getString(R.string.sleep_log));
        } else if (mode == LabelEnum.ADD || mode == LabelEnum.MODIFY) {
            tvLeft.setVisibility(View.GONE);
            ivLeft.setVisibility(View.VISIBLE);
            lrvLabel.setVisibility(View.GONE);
            edtLabel.setVisibility(View.VISIBLE);
            edtLabel.requestFocus();
            tvFinish.setVisibility(View.GONE);
            tvDate.setVisibility(View.GONE);
            tvMiddle.setTextColor(getResources().getColor(R.color.colorDarkLight));
            tvMiddle.setText("0/20");
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lrvLabel.setLayoutManager(layoutManager);
        lrvLabel.addItemDecoration(new LabelDivider(getActivity(), 1));
        mAdapter = new LabelAdapter(getActivity(), mEntryList);
        mAdapter.setLabelImp(this);
        lrvLabel.setAdapter(mAdapter);
        setFooter(lrvLabel);
    }

    private void setFooter(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.label_item_footer, view, false);
        mAdapter.setFooterView(header);
    }

    @Override
    public void onAdd() {
        edtLabel.setText(""); //置空
        edtLabel.setHint("");
        doLabelMode(LabelEnum.ADD);
    }

    @Override
    public void onModify(LabelEntry entry) {
        edtLabel.setText(""); //置空
        doLabelMode(LabelEnum.MODIFY);
        mModifyEntry = entry;
        if (entry != null) {
            edtLabel.setHint(entry.getContent());
        }
    }

    //添加新标签
    private void doAdd() {
        String content = edtLabel.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;
        if (mAdapter.isLabelRepeat(content)) return;
        LabelEntry entry = new LabelEntry(content, false, 0);
        doLabelMode(LabelEnum.NONE);
        mAdapter.addLabel(entry);
    }

    //修改标签
    private void doModify() {
        String content = edtLabel.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;
        if (mAdapter.isLabelRepeat(content)) return;
        doLabelMode(LabelEnum.NONE);
        mAdapter.editLabel(mModifyEntry, content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_label_left:
                if (isLeftOpened) {
                    tvLeft.setText(getString(R.string.sleep_label_edit));
                } else {
                    tvLeft.setText(getString(R.string.sleep_label_done));
                }
                isLeftOpened = !isLeftOpened;
                mAdapter.setEdit(isLeftOpened);
                break;
            case R.id.iv_label_left:
                AppUtils.hideKeyboard(getContext(), edtLabel);
                if (mLabelEnum == LabelEnum.ADD) {
                    doAdd();
                } else if (mLabelEnum == LabelEnum.MODIFY) {
                    doModify();
                }
                break;
            case R.id.iv_label_right:
                AppUtils.hideKeyboard(getContext(), edtLabel);
                if (mLabelEnum == LabelEnum.ADD || mLabelEnum == LabelEnum.MODIFY) {
                    doLabelMode(LabelEnum.NONE);
                } else {
                    dismiss();
                }
                break;
            case R.id.tv_label_finish:
                if (isLeftOpened) {
                    tvLeft.setText(getString(R.string.sleep_label_done));
                    isLeftOpened = !isLeftOpened;
                    mAdapter.setEdit(isLeftOpened);
                } else {
                    mAdapter.saveLabel();
                    dismiss();
                    if (mLabelInterface != null) {
                        mLabelInterface.onClosed();
                    }
                }
                break;
        }
    }
}
