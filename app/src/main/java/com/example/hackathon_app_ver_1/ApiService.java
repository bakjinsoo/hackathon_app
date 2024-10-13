package com.example.hackathon_app_ver_1;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadAudio(@Part MultipartBody.Part audio);

    @Multipart
    @POST("/upload/photo")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part photo);

    @Multipart
    @POST("/upload/secondAudio")
    Call<ResponseBody> uploadSecondAudio(@Part MultipartBody.Part audio);
    @GET("score")  // 엔드포인트 URL
    Call<ScoreResponse> getScore();
    @GET("/ai_score")
    Call<AiScoreResponse> getAiScore();
}
