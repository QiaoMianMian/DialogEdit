package com.dialog.edit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dialog.edit.dialog.LabelDialog;
import com.dialog.edit.dialog.LogDialog;
import com.dialog.edit.label.LabelEntry;
import com.dialog.edit.units.SPUtils;
import com.dialog.edit.units.StringUtils;
import com.dialog.edit.view.TagView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.badgeview.BGABadgeImageView;

public class MainActivity extends AppCompatActivity {

    SwitchCompat scStyleChoice;
    TextView tvStyleText;
    LinearLayout lltContainer;
    TextView tvAdd;
    BGABadgeImageView bivLabel;
    LinearLayout lltLayout;
    TagView tvLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scStyleChoice = findViewById(R.id.sc_style_choice);
        scStyleChoice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tvStyleText.setText(R.string.sleep_style_2);
            } else {
                tvStyleText.setText(R.string.sleep_style_1);
            }
        });
        tvStyleText = findViewById(R.id.tv_style_text);

        lltContainer = findViewById(R.id.llt_main_container);
        lltContainer.setOnClickListener(v -> {
            if (scStyleChoice.isChecked()) {
                LogDialog logDialog = new LogDialog();
                logDialog.show(getSupportFragmentManager(), "Log");
                logDialog.setLabelInterface(() -> doSleepLog());
            } else {
                LabelDialog labelDialog = new LabelDialog();
                labelDialog.show(getSupportFragmentManager(), "Label");
                labelDialog.setLabelInterface(() -> doSleepLog());
            }
        });

        tvAdd = findViewById(R.id.tv_add);
        bivLabel = findViewById(R.id.bga_iv_tag);
        lltLayout = findViewById(R.id.llt_main_content);
        tvLabel = findViewById(R.id.tv_tag);
    }

    private void doSleepLog() {
        List<LabelEntry> labelEntries = (List<LabelEntry>) StringUtils.str2Object(
                (String) SPUtils.getParam(this, SPUtils.SP_LOCAL_LABEL, ""));
        if (labelEntries != null && labelEntries.size() > 0) {
            List<String> tagList = new ArrayList<>();
            for (LabelEntry entry : labelEntries) {
                if (entry == null) continue;
                if (entry.isChoice()) {
                    tagList.add(entry.getContent());
                }
            }
            tvLabel.setTags(tagList);
            int size = tagList.size();
            if (size > 0) {
                lltLayout.setVisibility(View.VISIBLE);
                tvAdd.setVisibility(View.GONE);
                bivLabel.setVisibility(View.VISIBLE);

                bivLabel.showTextBadge("" + size);
                bivLabel.getBadgeViewHelper().setBadgeTextColorInt(getResources().getColor(R.color.colorGreen));
                bivLabel.getBadgeViewHelper().setBadgeBgColorInt(getResources().getColor(R.color.colorWhite));
                bivLabel.getBadgeViewHelper().setBadgePaddingDp(4);
                bivLabel.getBadgeViewHelper().setBadgeBorderWidthDp(1);
                bivLabel.getBadgeViewHelper().setBadgeBorderColorInt(getResources().getColor(R.color.colorGreen));
            } else {
                lltLayout.setVisibility(View.GONE);
                tvAdd.setVisibility(View.VISIBLE);
                bivLabel.setVisibility(View.GONE);
            }
        }
    }
}
