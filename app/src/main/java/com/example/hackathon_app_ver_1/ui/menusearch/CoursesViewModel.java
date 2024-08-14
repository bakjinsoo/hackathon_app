package com.example.hackathon_app_ver_1.ui.menusearch;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.hackathon_app_ver_1.data.CoursesRepository;
import com.example.hackathon_app_ver_1.ui.model.MatchCourse;

import java.util.List;

public class CoursesViewModel extends ViewModel {
    private final MutableLiveData<List<MatchCourse>> mMatchedCourses;
    private final CoursesRepository repository;

    public CoursesViewModel(CoursesRepository repository) {
        this.repository = repository;
        this.mMatchedCourses = new MutableLiveData<>();
    }

    public LiveData<List<MatchCourse>> matchedCourses() {
        return mMatchedCourses;
    }

    public void fetchMatchedCourses() {
        List<MatchCourse> data = repository.matchedCourses();
        mMatchedCourses.postValue(data);
    }

    static class MyCoursesViewModelFactory implements ViewModelProvider.Factory {
        private final CoursesRepository repository;

        public MyCoursesViewModelFactory(CoursesRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CoursesViewModel.class)) {
                return (T) new CoursesViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
