package com.example.librateapps.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.librateapps.R;
import com.example.librateapps.Toaster;
import com.example.librateapps.callBack.CallbackListener;
import com.ymb.ratingbar_lib.RatingBar;

public class CustomRateAppDialog extends Dialog {
    Context mContext;
    CallbackListener callbackListener;
    private Thread mThread;
    EditText edtFeedback;
    TextView tvSubmit;
    private TextView btnTooAds, btnNotWorking, btnOther;
    private LinearLayout layoutFeedback, layoutActions, layoutMaybeLate;
    float ratting = 1f;
    private String feedback = "";

    public CustomRateAppDialog(Context context, CallbackListener callbackListener) {
        super(context);
        mContext = context;
        this.callbackListener = callbackListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_custom_rating);
        initView();
    }

    private void initView() {
        setCancelable(false);
        RatingBar rating = findViewById(R.id.rating);
        edtFeedback = findViewById(R.id.edtFeedback);
        tvSubmit = findViewById(R.id.tv_submit);
        btnTooAds = findViewById(R.id.btn_too_ads);
        btnNotWorking = findViewById(R.id.btn_not_working);
        btnOther = findViewById(R.id.btn_other);
        layoutFeedback = findViewById(R.id.layout_feedback);
        layoutActions = findViewById(R.id.layout_actions);
        layoutMaybeLate = findViewById(R.id.ln_later);

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toaster.simpleToast(mContext, getContext().getString(R.string.thanks_feedback));
                dismiss();
                if (feedback.trim().length() == 0) {
                    feedback = edtFeedback.getText().toString();
                }
                callbackListener.onRating(ratting, feedback);
            }
        });

        layoutMaybeLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                callbackListener.onMaybeLater();
            }
        });

        rating.setOnRatingChangedListener(new RatingBar.OnRatingChangedListener() {
            @Override
            public void onRatingChange(float v, float v1) {
                if (mThread != null) {
                    mThread.interrupt();
                }
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(300);
                            if (v1 == 0.0f)
                                return;
                            if (v1 <= 3.0) {
                                layoutFeedback.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutMaybeLate.setVisibility(View.GONE);
                                        layoutFeedback.setVisibility(View.VISIBLE);
                                    }
                                });
                                layoutActions.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        layoutActions.setVisibility(v1 == 3 ? View.GONE : View.VISIBLE);
                                    }
                                });
                                if (v1 == 3) {
                                    edtFeedback.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            edtFeedback.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                                ratting = v1;
                                return;
                            }
                            dismiss();
                            callbackListener.onRating(v1, "");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                mThread.start();
            }
        });

        btnTooAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(1);
                edtFeedback.setVisibility(View.GONE);
                feedback = "Too many ads";
            }
        });

        btnNotWorking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(2);
                edtFeedback.setVisibility(View.GONE);
                feedback = "Not working";
            }
        });

        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOption(3);
                edtFeedback.setVisibility(View.GONE);
                feedback = "Other";
            }
        });
    }

    private void selectOption(int option) {
        btnTooAds.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), option == 1 ? R.drawable.bg_option_selected : R.drawable.bg_option, null));
        btnNotWorking.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), option == 2 ? R.drawable.bg_option_selected : R.drawable.bg_option, null));
        btnOther.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), option == 3 ? R.drawable.bg_option_selected : R.drawable.bg_option, null));

        btnTooAds.setTextColor(getContext().getResources().getColor(option == 1 ? R.color.colorWhite : R.color.color_303030));
        btnNotWorking.setTextColor(getContext().getResources().getColor(option == 2 ? R.color.colorWhite : R.color.color_303030));
        btnOther.setTextColor(getContext().getResources().getColor(option == 3 ? R.color.colorWhite : R.color.color_303030));
    }
}
