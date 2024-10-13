package com.example.hackathon_app_ver_1;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.color.utilities.Score;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultViewActivity extends AppCompatActivity {

    private LinearProgressIndicator progressIndicator;
    private TextView progressText;
    private ImageView imageView;
    private TextView aiText;
    private TextView resultText;
    private TextView result_Title;
    int aiScore;
    int progress;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);
        progressIndicator = findViewById(R.id.progress_indicator);
        progressText = findViewById(R.id.progress_text);
        imageView = findViewById(R.id.result_icon);
        aiText=findViewById(R.id.ai_score_view);
        resultText=findViewById(R.id.result_explain);
        result_Title=findViewById(R.id.result_title_view);
        fetchLatestScore();
        fetchLatestAiScore();
        //updateIndicatorColor(progress); // 프로그레스 값에 따라 색상 변경
        //animateProgress(progress);
        animateImageViewSize(); // ImageView 애니메이션 시작
    }
    private void fetchLatestAiScore() {
        ApiService apiService = RetrofitClient.getClient("http://172.30.1.34:8080/").create(ApiService.class);
        Call<AiScoreResponse> call = apiService.getAiScore();
        call.enqueue(new Callback<AiScoreResponse>() {
            @Override
            public void onResponse(Call<AiScoreResponse> call, Response<AiScoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AiScoreResponse aiScoreResponse = response.body();
                    aiScore = aiScoreResponse.getAiScore(); // AiScoreResponse 객체에서 aiScore 값 가져오기
                    aiText.setText(String.format("AI 분석결과 치매입니다.")); // TextView에 AI Score 표시
                }
            }

            @Override
            public void onFailure(Call<AiScoreResponse> call, Throwable t) {
                // 에러 처리
                aiText.setText("Error fetching AI score");
            }
        });
    }
    private void fetchLatestScore() {
        ApiService apiService = RetrofitClient.getClient("http://172.30.1.34:8080/").create(ApiService.class);
        Call<ScoreResponse> call = apiService.getScore();
        call.enqueue(new Callback<ScoreResponse>() {
            @Override
            public void onResponse(Call<ScoreResponse> call, Response<ScoreResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ScoreResponse scoreResponse = response.body();
                    progress = scoreResponse.getTotalScore(); //
                    progress=56;// ScoreResponse 객체에서 totalScore 값 가져오기
                    Log.d("ResultViewActivity", "Fetched score: " + progress); // 디버깅 로그
                    result_Title.setText(String.format("%d점", progress)); // progress가 업데이트된 후에 호출
                    updateIndicatorColor(progress); // 프로그레스 값에 따라 색상 변경
                    animateProgress(progress);
                } else {
                    Log.e("ResultViewActivity", "Failed to fetch score. Response code: " + response.code()); // 실패 로그
                }
            }

            @Override
            public void onFailure(Call<ScoreResponse> call, Throwable t) {
                // 에러 처리
                Log.e("ResultViewActivity", "Error fetching score", t); // 에러 로그
                progressText.setText("Error fetching score");
            }
        });
    }

    private void updateIndicatorColor(int progress) {
        int color;
        int drawableResId;
        if (progress >= 80 && progress <= 100) {
            color = ContextCompat.getColor(this, R.color.result_green);
            drawableResId = R.drawable.green_result;
            resultText.setText("검사 결과, 인지 기능이 정상 범위에 속합니다.\n현재 인지 능력에 특별한 문제가 발견되지 않았습니다.\n그러나 인지 건강을 유지하기 위해 규칙적인 건강 관리와 생활 습관을 유지하는 것이 좋습니다. \n앞으로도 건강한 생활을 지속하시기 바랍니다.");

        } else if (progress >= 67 && progress <= 79) {
            color = ContextCompat.getColor(this, R.color.result_yellow);
            resultText.setText("검사 결과, 경도의 인지 손상 징후가 \n관찰되었습니다. \n이는 기억력, 집중력, 또는 일상적인 \n인지 기능에서 약간의 어려움이 있을 수 있음을 나타냅니다. 이 단계에서는 조기 개입과 적절한 관리가 중요합니다.\n 더 정확한 평가와 맞춤형 조언을 받기 위해, \n가까운 병원이나 전문의를 방문하실 것을 \n권장드립니다.");
            drawableResId = R.drawable.yellow_result;
        } else {
            color = ContextCompat.getColor(this, R.color.result_red);
            drawableResId = R.drawable.red_result;
        }
        progressIndicator.setIndicatorColor(color);
        imageView.setImageResource(drawableResId);
    }

    private void animateProgress(int targetProgress) {
        int startProgress = 0;
        int endProgress = targetProgress;

        ValueAnimator animator = ValueAnimator.ofInt(startProgress, endProgress);
        animator.setDuration(1000); // 1초 동안 진행
        animator.addUpdateListener(animation -> {
            int animatedProgress = (int) animation.getAnimatedValue();
            progressIndicator.setProgress(animatedProgress);
            progressText.setText(String.format("%d%%", animatedProgress));

            // 애니메이션 중간에도 색상을 업데이트
            updateIndicatorColor(animatedProgress);
        });
        animator.start();
    }

    private void animateImageViewSize() {
        int originalSize = dpToPx(150);
        int expandedSize = dpToPx(180); // 원하는 확대 크기

        ValueAnimator sizeAnimator = ValueAnimator.ofInt(originalSize, expandedSize);
        sizeAnimator.setDuration(1000); // 한 사이클당 1초
        sizeAnimator.setRepeatCount(4); // 총 5번 반복 (0부터 시작)
        sizeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        sizeAnimator.addUpdateListener(animation -> {
            int animatedValue = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.width = animatedValue;
            layoutParams.height = animatedValue;
            imageView.setLayoutParams(layoutParams);
        });

        sizeAnimator.start();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
