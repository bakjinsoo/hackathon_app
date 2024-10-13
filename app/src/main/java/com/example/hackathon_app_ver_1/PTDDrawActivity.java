package com.example.hackathon_app_ver_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PTDDrawActivity extends AppCompatActivity {
    Button drawButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_view);
        drawButton = findViewById(R.id.draw_load_button);
        drawButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PTDDrawActivity.this, PhotoActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
