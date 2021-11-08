package com.example.librateapps.function;

import android.content.Context;
import android.view.ViewGroup;

import com.example.librateapps.R;
import com.example.librateapps.callBack.CallbackListener;
import com.example.librateapps.dialog.CustomRateAppDialog;

public class RateUtils {
    public static void showCustomRateDialog(Context context, CallbackListener callbackListener) {
        CustomRateAppDialog dialog = new CustomRateAppDialog(context, callbackListener);
        int w = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        int h = ViewGroup.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setLayout(w, h);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        dialog.show();
    }
}
