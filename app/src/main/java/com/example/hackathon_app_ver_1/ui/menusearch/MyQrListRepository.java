package com.example.hackathon_app_ver_1.ui.menusearch;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.hackathon_app_ver_1.data.MatchesCoursesFake;
import com.example.hackathon_app_ver_1.ui.model.MatchCourse;

import java.util.List;

public class MyQrListRepository {
    private static final String TAG = "MyQrListRepository";
    private final MediatorLiveData<MatchCourse> getInfo = new MediatorLiveData<>();


    MutableLiveData<Boolean> isLoadingGetList = new MutableLiveData<>();

    MutableLiveData<Boolean> isLoadingRepo = new MutableLiveData<>();

    MutableLiveData<List<MatchCourse>> liveData;

    public void getData(MutableLiveData<List<MatchCourse>> liveData) {
        isLoadingGetList.setValue(true);
        try {
            isLoadingGetList.setValue(false);
            List<MatchCourse> myQrItem = MatchesCoursesFake.getInstance().matchedCourses();
            liveData.postValue( myQrItem);
        }catch (Exception ex){
            isLoadingGetList.setValue(false);
        }


    }
}
