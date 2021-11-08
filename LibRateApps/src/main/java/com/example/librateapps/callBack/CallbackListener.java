package com.example.librateapps.callBack;

public interface CallbackListener {
    void onMaybeLater();
    void onRating(float rating, String feedback);
}
