package com.example.hackathon_app_ver_1;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PTDExplainActivity extends AppCompatActivity {
    private CircleProgressBar circleProgressBar;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView timerText;
    private int timeLeft = 30;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_view);
        circleProgressBar = findViewById(R.id.custom_progressBar);
        timerText = findViewById(R.id.timer_text);
        ObjectAnimator animator = ObjectAnimator.ofFloat(circleProgressBar, "progress", 100f, 0f);
        animator.setDuration(30000); // 30초 동안 애니메이션 실행
        animator.start();
        // 1초마다 텍스트뷰를 업데이트하는 핸들러
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (timeLeft > 0) {
                    timeLeft--;
                    timerText.setText(String.valueOf(timeLeft));

                    if (timeLeft <= 10) {
                        // 텍스트뷰 색상 및 크기 애니메이션
                        animateTextView();

                        // CircleProgressBar 두께 및 색상 애니메이션
                        animateCircleProgressBar();
                    }

                    handler.postDelayed(this, 1000); // 1초마다 반복
                }
                else {
                    // 30초가 끝났을 때 PTDDrawActivity로 전환
                    Intent intent = new Intent(PTDExplainActivity.this, PTDDrawActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 1000);
    }
    private void animateTextView() {
        // 텍스트 색상 변경
        timerText.setTextColor(Color.RED);

        // 텍스트 크기 애니메이션 (커졌다가 줄어들기)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(timerText, "scaleX", 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(timerText, "scaleY", 1.5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(500); // 애니메이션 지속 시간
        animatorSet.start();
    }

    private void animateCircleProgressBar() {
        // CircleProgressBar 두께 변경
        ObjectAnimator thicknessAnimator = ObjectAnimator.ofFloat(circleProgressBar, "strokeWidth", 16f, 32f, 16f);
        thicknessAnimator.setDuration(500); // 두께 애니메이션 지속 시간

        // CircleProgressBar 색상 변경
        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(circleProgressBar, "color", circleProgressBar.getColor(), Color.RED);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setDuration(500); // 색상 애니메이션 지속 시간

        // AnimatorSet을 사용해 두께와 색상 애니메이션 동시에 실행
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(thicknessAnimator, colorAnimator);
        animatorSet.start();
    }

}
