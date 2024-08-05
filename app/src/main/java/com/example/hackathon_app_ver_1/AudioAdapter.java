package com.example.hackathon_app_ver_1;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class AudioAdapter {

    //리사이클러뷰에 넣을 데이터 리스트
    ArrayList<Uri> dataModels;
    Context context;

    // 리스너 객체 참조를 저장하는 변수
    private OnIconClickListener listener = null;

    /**
     * 커스텀 이벤트 리스너
     * 클릭이벤트를 Adapter에서 구현하기에 제약이 있기 때문에 Activity 에서 실행시키기 위해 커스텀 이벤트 리스너를 생성함.
     * 절차
     * 1.커스텀 리스너 인터페이스 정의
     * 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
     * 3. 아이템 클릭 이벤트 핸들러 메스드에서 리스너 객체 메서드 호출
     * 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달(MainActivity.java 에서 audioAdapter.setOnItemClickListener() )
     */
    // 1.커스텀 리스너 인터페이스 정의
    public interface OnIconClickListener {
        void onItemClick(View view, int position);
    }

    // 2. 리스너 객체를 전달하는 메서드와 전달된 객체를 저장할변수 추가
    public void setOnItemClickListener(OnIconClickListener listener) {
        this.listener = listener;
    }

    //생성자를 통하여 데이터 리스트 context를 받음
    public AudioAdapter(Context context, ArrayList<Uri> dataModels) {
        this.dataModels = dataModels;
        this.context = context;
    }

}