package com.dialog.edit.units;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class AppUtils {

    //弹出软键盘
    public static void showKeyboard(Context context, EditText edt) {
        edt.setFocusable(true);
        edt.setFocusableInTouchMode(true);
        edt.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edt, 0);
            }
        }, 200);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
