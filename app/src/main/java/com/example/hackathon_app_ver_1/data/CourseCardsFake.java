package com.example.hackathon_app_ver_1.data;
import com.example.hackathon_app_ver_1.R;
import com.example.hackathon_app_ver_1.ui.model.CourseCard;

import java.util.Arrays;
import java.util.List;
public class CourseCardsFake {
    private static CourseCardsFake instance;

    private CourseCardsFake() {
    }

    public static synchronized CourseCardsFake getInstance() {
        if (instance == null) {
            instance = new CourseCardsFake();
        }
        return instance;
    }

    public List<CourseCard> getCourseCards() {
        return Arrays.asList(
                new CourseCard(1, "단어 퀴즈", "언어 능력", R.drawable.read_icon),
                new CourseCard(2, "숫자 맞추기", "주의 집중력", R.drawable.calculation_icon),
                new CourseCard(3, "카드 짝맞추기", "기억력", R.drawable.remembericon),
                new CourseCard(4, "다트 맞추기", "집중력", R.drawable.focuse_icon)
        );
    }

    public List<CourseCard> getSearchCoursesCards() {
        return Arrays.asList(
                new CourseCard(1, "Design Thinking", "19 courses", R.drawable.course_design_thinking),
                new CourseCard(2, "App Development", "14 courses", R.drawable.course_design_coding),
                new CourseCard(3, "Marketing", "24 courses", R.drawable.course_design_marketing),
                new CourseCard(4, "Security Expert", "18 courses", R.drawable.course_design_securityexpert),
                new CourseCard(5, "Pentest App Android", "21 courses", R.drawable.course_design_whatisthisshit),
                new CourseCard(6, "Big Data", "10 courses", R.drawable.course_coding)
        );
    }
}
