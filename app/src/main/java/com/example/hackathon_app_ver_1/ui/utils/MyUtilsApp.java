package com.example.hackathon_app_ver_1.ui.utils;

import android.content.Context;
import android.widget.Toast;

public class MyUtilsApp {
    private MyUtilsApp(){}
    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
