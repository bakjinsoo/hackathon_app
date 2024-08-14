package com.example.hackathon_app_ver_1.ui.listners;

import android.widget.ImageView;

import com.example.hackathon_app_ver_1.ui.model.CourseCard;

public interface CoursesItemClickListner {
    void onDashboardCourseClick(CourseCard courseCard, ImageView imageView);
}
