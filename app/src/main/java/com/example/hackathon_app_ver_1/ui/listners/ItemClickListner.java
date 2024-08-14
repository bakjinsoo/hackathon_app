package com.example.hackathon_app_ver_1.ui.listners;

import android.widget.ImageView;

public interface ItemClickListner<T> {
    void onItemClick(T item, ImageView imageView);
}
