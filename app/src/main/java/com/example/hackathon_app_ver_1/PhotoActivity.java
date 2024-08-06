package com.example.hackathon_app_ver_1;



import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri photoURI;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Button takePictureButton = findViewById(R.id.takePhotoButton);
        takePictureButton.setOnClickListener(v -> dispatchTakePictureIntent());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("PhotoActivity", "Error occurred while creating the File", ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "com.example.hackathon_app_ver_1.photo_provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                Log.i("PhotoActivity", "Photo capture intent started");
            }
        } else {
            Log.e("PhotoActivity", "No camera activity available to handle the intent");
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        Log.i("PhotoActivity", "Created image file: " + image.getAbsolutePath());
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.i("PhotoActivity", "Photo capture successful, URI: " + photoURI);
            // Show the thumbnail
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(photoURI);

            // Upload photo in a new thread
            new Thread(() -> {
                if (photoFile != null) {
                    uploadPhotoToServer(photoFile);
                    runOnUiThread(() -> {
                        Log.i("PhotoActivity", "Photo uploaded to server");
                        // Update UI if needed
                    });
                } else {
                    Log.e("PhotoActivity", "Photo file is null, cannot upload");
                }
            }).start();
        } else {
            Log.e("PhotoActivity", "Photo capture failed or canceled");
        }
    }

    private void uploadPhotoToServer(File photoFile) {
        // Implement your upload logic here
        Log.i("PhotoActivity", "Uploading photo to server: " + photoFile.getAbsolutePath());
    }
}
