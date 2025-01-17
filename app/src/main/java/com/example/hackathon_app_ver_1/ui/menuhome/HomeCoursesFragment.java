package com.example.hackathon_app_ver_1.ui.menuhome;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hackathon_app_ver_1.R;
import com.example.hackathon_app_ver_1.VoiceRecordActivity;
import com.example.hackathon_app_ver_1.data.CourseCardsFake;
import com.example.hackathon_app_ver_1.databinding.FragmentHomeCoursesBinding;
import com.example.hackathon_app_ver_1.ui.model.CourseCard;
import com.example.hackathon_app_ver_1.ui.utils.MyUtilsApp;

import java.util.List;

public class HomeCoursesFragment extends Fragment implements PopularCoursesAdapter.ClickListener {
    FragmentHomeCoursesBinding binding;
    private RecyclerView recyclerView;
    private static final String TAG = "HomeCoursesFragment";

    public HomeCoursesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void setUpUI() {
        String percentage = getResources().getString(R.string.percentage_course, 75);
        //binding.tvPercentage.setText(percentage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvPopularCourses.setLayoutManager(layoutManager);
        binding.rvPopularCourses.hasFixedSize();

        PopularCoursesAdapter popularCoursesAdapter = new PopularCoursesAdapter(this);
        binding.rvPopularCourses.setAdapter(popularCoursesAdapter);
        if (binding == null) {
            Log.e(TAG, "Binding object is null. Check if the layout file is properly set up.");
        }
        binding.dementiastart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), VoiceRecordActivity.class);
                startActivity(intent);
            }
        });
        List<CourseCard> courseCardsList;
        courseCardsList = CourseCardsFake.getInstance().getCourseCards();

        popularCoursesAdapter.setListDataItems(courseCardsList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeCoursesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setUpUI();
        return view;
    }

    @Override
    public void onClick(CourseCard view, int position) {
        MyUtilsApp.showToast(requireContext(), view.getCourseTitle());
    }
}
