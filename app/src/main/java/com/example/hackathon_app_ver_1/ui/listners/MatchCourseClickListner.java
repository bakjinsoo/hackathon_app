package com.example.hackathon_app_ver_1.ui.listners;

import android.widget.ImageView;

import com.example.hackathon_app_ver_1.ui.model.MatchCourse;

public interface MatchCourseClickListner {
    void onScrollPagerItemClick(MatchCourse courseCard, ImageView imageView);
}
