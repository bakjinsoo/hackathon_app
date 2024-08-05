package com.example.hackathon_app_ver_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/") // 서버 URL을 입력
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService apiService = retrofit.create(ApiService.class);
    private static final String TAG = "MainActivity";
    StorageReference storageReference;
    /**xml 변수*/
    ImageButton audioRecordImageBtn;
    TextView audioRecordText;

    /**오디오 파일 관련 변수*/
    // 오디오 권한
    private String recordPermission = Manifest.permission.RECORD_AUDIO;
    private int PERMISSION_CODE = 21;

    // 오디오 파일 녹음 관련 변수
    private MediaRecorder mediaRecorder;
    private String audioFileName; // 오디오 녹음 생성 파일 이름
    private boolean isRecording = false;    // 현재 녹음 상태를 확인하기 위함.
    private Uri audioUri = null; // 오디오 파일 uri

    // 오디오 파일 재생 관련 변수
    private MediaPlayer mediaPlayer = null;
    private Boolean isPlaying = false;
    ImageView playIcon;

    /** 리사이클러뷰 */
    private AudioAdapter audioAdapter;
    private ArrayList<Uri> audioList;
    private TextView textView;
    private Handler handler;
    private Runnable runnable;
    private TranscriptApi transcriptApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.scriptText);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://202.31.147.131:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        transcriptApi = retrofit.create(TranscriptApi.class);
        handler=new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("쓰레드","쓰레드 돌아가는중");
                transcriptsTextUpdate();
                handler.postDelayed(this,5000);
            }
        };
        storageReference = FirebaseStorage.getInstance().getReference();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable); // Remove callbacks when activity is destroyed
        }
    }

    private void transcriptsTextUpdate() {
        Call<Transcript> call = transcriptApi.getLatestTranscript();
        call.enqueue(new Callback<Transcript>() {
            @Override
            public void onResponse(Call<Transcript> call, Response<Transcript> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String text = response.body().getText();
                    textView.setText(text);
                    updateInputCheckKey(0);
                    Log.d("API Success", "Transcript text updated");
                } else {
                    Log.e("API Error", "Failed to get transcript");
                }
            }

            @Override
            public void onFailure(Call<Transcript> call, Throwable t) {
                Log.e("API Error", "API call failed", t);
            }
        });
    }
    private void updateInputCheckKey(int key) {
        Call<Void> updateCall = transcriptApi.updateInputCheckKey(key);
        updateCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API Success", "InputCheck key updated to " + key);
                } else {
                    Log.e("API Error", "Failed to update InputCheck key. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Error", "API call failed", t);
            }
        });
    }
    // xml 변수 초기화
    // 리사이클러뷰 생성 및 클릭 이벤트
    private void init() {
        audioRecordImageBtn = findViewById(R.id.audioRecordImageBtn);
        audioRecordText = findViewById(R.id.audioRecordText);
        if (handler != null && runnable != null) {
            handler.post(runnable); // Register the Runnable to start
        } else {
            Log.e("MainActivity", "Handler or Runnable is null");
        }
        audioRecordImageBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording) {
                    // 현재 녹음 중 O
                    // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                    isRecording = false; // 녹음 상태 값
                    audioRecordImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_record, null)); // 녹음 상태 아이콘 변경
                    audioRecordText.setText("녹음 시작"); // 녹음 상태 텍스트 변경
                    stopRecording();
                    // 녹화 이미지 버튼 변경 및 리코딩 상태 변수값 변경
                } else {
                    // 현재 녹음 중 X
                    /*절차
                     *       1. Audio 권한 체크
                     *       2. 처음으로 녹음 실행한건지 여부 확인
                     * */
                    if(checkAudioPermission()) {
                        // 녹음 상태에 따른 변수 아이콘 & 텍스트 변경
                        isRecording = true; // 녹음 상태 값
                        audioRecordImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_recording_red, null)); // 녹음 상태 아이콘 변경
                        audioRecordText.setText("녹음 중"); // 녹음 상태 텍스트 변경
                        startRecording();
                    }
                }
            }
        });

        // 리사이클러뷰
        RecyclerView audioRecyclerView = findViewById(R.id.recyclerview);

        audioList = new ArrayList<>();
        audioAdapter = new AudioAdapter(this, audioList);
        audioRecyclerView.setAdapter(audioAdapter);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(mLayoutManager);

        // 커스텀 이벤트 리스너 4. 액티비티에서 커스텀 리스너 객체 생성 및 전달
        audioAdapter.setOnItemClickListener(new AudioAdapter.OnIconClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String uriName = String.valueOf(audioList.get(position));

                /*음성 녹화 파일에 대한 접근 변수 생성;
                     (ImageView)를 붙여줘서 View 객체를 형변환 시켜줌.
                     전역변수로 한 이유는
                    * */

                File file = new File(uriName);

                if(isPlaying){
                    // 음성 녹화 파일이 여러개를 클릭했을 때 재생중인 파일의 Icon을 비활성화(비 재생중)으로 바꾸기 위함.
                    if(playIcon == (ImageView)view){
                        // 같은 파일을 클릭했을 경우
                        stopAudio();
                    } else {
                        // 다른 음성 파일을 클릭했을 경우
                        // 기존의 재생중인 파일 중지
                        stopAudio();

                        // 새로 파일 재생하기
                        playIcon = (ImageView)view;
                        playAudio(file);
                    }
                } else {
                    playIcon = (ImageView)view;
                    playAudio(file);
                }
            }
        });
    }

    // 오디오 파일 권한 체크
    private boolean checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), recordPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{recordPermission}, PERMISSION_CODE);
            return false;
        }
    }

    // 녹음 시작
    private void startRecording() {
        //파일의 외부 경로 확인
        String recordPath = getExternalFilesDir("/").getAbsolutePath();
        // 파일 이름 변수를 현재 날짜가 들어가도록 초기화. 그 이유는 중복된 이름으로 기존에 있던 파일이 덮어 쓰여지는 것을 방지하고자 함.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        audioFileName = recordPath + "/" +"RecordExample_" + timeStamp + "_"+"audio.mp4";

        //Media Recorder 생성 및 설정
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(audioFileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //녹음 시작
        mediaRecorder.start();
    }

    // 녹음 종료
    private void stopRecording() {
        // 녹음 종료 종료
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;

        // 파일 경로(String) 값을 Uri로 변환해서 저장
        //      - Why? : 리사이클러뷰에 들어가는 ArrayList가 Uri를 가지기 때문
        //      - File Path를 알면 File을  인스턴스를 만들어 사용할 수 있기 때문
        String packageName = getApplicationContext().getPackageName();
        audioUri = FileProvider.getUriForFile(MainActivity.this, packageName + ".provider", new File(audioFileName));
        // 데이터 ArrayList에 담기
        audioList.add(audioUri);
        // 데이터 갱신
        audioAdapter.notifyDataSetChanged();
        convertAndUploadAudio(audioFileName);

    }

    // 녹음 파일 재생
    private void playAudio(File file) {
        mediaPlayer = new MediaPlayer();

        try {
            mediaPlayer.setDataSource(file.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_pause, null));
        isPlaying = true;

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopAudio();
            }
        });

    }

    // 녹음 파일 중지
    private void stopAudio() {
        playIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_audio_play, null));
        isPlaying = false;
        mediaPlayer.stop();
    }
    private void convertAndUploadAudio(String inputFilePath) {
        String outputFilePath = inputFilePath.replace(".mp4", ".wav");

        FFmpeg.executeAsync("-i " + inputFilePath + " -acodec pcm_s16le -ar 44100 " + outputFilePath, (executionId, returnCode) -> {
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                Log.i(Config.TAG, "Command execution completed successfully.");
                uploadAudioToServer(outputFilePath);
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                Log.i(Config.TAG, "Command execution cancelled by user.");
            } else {
                Log.i(Config.TAG, String.format("Command execution failed with returnCode=%d.", returnCode));
            }
        });
    }
    private void uploadAudioToServer(String filePath) {
        File audioFile = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/wav"), audioFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("audio", audioFile.getName(), requestFile);

        ApiService apiService = RetrofitClient.getClient("http://202.31.147.131:8080/").create(ApiService.class);
        Call<ResponseBody> call = apiService.uploadAudio(body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 업로드 성공 처리
                    Log.i("Upload", "Success");
                } else {
                    // 업로드 실패 처리
                    Log.i("Upload", "Failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 네트워크 오류 처리
                Log.e("Upload error:", t.getMessage());
            }
        });
    }
//    private void uploadAudioToFirebase(Uri audioUri) {
//        StorageReference reference = storageReference.child("audios/" + UUID.randomUUID().toString() + ".wav");
//        reference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(MainActivity.this, "Audio uploaded successfully!", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(MainActivity.this, "Failed to upload audio", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}