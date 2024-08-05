package com.example.hackathon_app_ver_1;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TranscriptApi {
    @GET("/api/transcripts/latest")
    Call<Transcript> getLatestTranscript();

    @POST("/api/inputcheck/update")
    @FormUrlEncoded
    Call<Void> updateInputCheckKey(@Field("key") int key);
}
