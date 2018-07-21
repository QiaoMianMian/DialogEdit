package com.dialog.edit.dialog;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dialog.edit.R;
import com.dialog.edit.adapter.LogAdapter;
import com.dialog.edit.label.LabelEntry;
import com.dialog.edit.label.LabelInterface;
import com.dialog.edit.label.LabelRecyclerView;
import com.dialog.edit.units.DensityUtils;
import com.dialog.edit.units.LayoutUtils;
import com.dialog.edit.units.SPUtils;
import com.dialog.edit.units.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class LogDialog extends DialogFragment {

    private LinearLayout lltParent;
    private TextView tvMiddle;
    private TextView tvDate;
    private EditText edtLabel;
    private LabelRecyclerView lrvLabel;
    private TextView tvFinish;

    private LogAdapter mAdapter;

    private List<LabelEntry> mEntryList;

    private LabelInterface mLabelInterface;

    public void setLabelInterface(LabelInterface mLabelInterface) {
        this.mLabelInterface = mLabelInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        return inflater.inflate(R.layout.dialog_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initRecyclerView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);
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
        lltParent = view.findViewById(R.id.log_parent_llt);
        LayoutUtils.setClipViewCornerRadius(lltParent, DensityUtils.dip2px(getContext(), 10));

        tvMiddle = view.findViewById(R.id.tv_log_middle);
        tvDate = view.findViewById(R.id.tv_log_date);

        edtLabel = view.findViewById(R.id.edt_log_label);
        //监听文本变化
        edtLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {

                }
            }
        });
        //监听键盘按键
        edtLabel.setOnKeyListener((v, keyCode, event) -> {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                mAdapter.doAddLog(edtLabel.getText().toString());
                edtLabel.setText("");
                return true;
            }
            return false;
        });
        //监听键盘弹出和隐藏
        edtLabel.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            //获取当前界面可视部分
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
            //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
            int displayHeight = r.bottom - r.top;
            int keyboardHeight = screenHeight - displayHeight;
            ViewGroup.LayoutParams lps = lltParent.getLayoutParams();
            if (keyboardHeight > screenHeight / 3) {
                lps.height = keyboardHeight;
            } else {
                lps.height = DensityUtils.dip2px(getActivity(), 387);
            }
            lltParent.setLayoutParams(lps);
        });

        lrvLabel = view.findViewById(R.id.lav_log_label);

        tvFinish = view.findViewById(R.id.tv_log_finish);
        tvFinish.setOnClickListener(v -> {
            mAdapter.doAddLog(edtLabel.getText().toString());
            edtLabel.setText("");
            if (mLabelInterface != null) {
                mEntryList = mAdapter.getLabelCaches();
                SPUtils.setParam(getContext(), SPUtils.SP_LOCAL_LABEL, StringUtils.obj2String(mEntryList));
                mLabelInterface.onClosed();
                dismiss();
            }
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lrvLabel.setLayoutManager(layoutManager);
        lrvLabel.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mAdapter = new LogAdapter(getActivity(), mEntryList);
        mAdapter.setItemClickListener(position -> {
            mAdapter.doModifyLog(position);
            edtLabel.setText("");
        });
        lrvLabel.setAdapter(mAdapter);
    }
}
