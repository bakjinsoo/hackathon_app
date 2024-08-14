package com.example.hackathon_app_ver_1.data;

import com.example.hackathon_app_ver_1.ui.model.MatchCourse;

import java.util.Collections;
import java.util.List;

public class CoursesRepository {
    public List<MatchCourse> matchedCourses() {
        try {
            return MatchesCoursesFake.getInstance().matchedCourses();
        } catch (Exception ex) {
            // Maneja la excepción aquí
            return Collections.emptyList();
        }
    }
}
