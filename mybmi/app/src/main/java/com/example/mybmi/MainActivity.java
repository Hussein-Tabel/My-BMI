package com.example.mybmi;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    MediaPlayer mp;

    ImageButton btnSound, btnNosound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnSound = findViewById(R.id.ibSound);
        btnNosound = findViewById(R.id.ibNoSound);

        mp = MediaPlayer.create(this, R.raw.music1);
        mp.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Go(View view) {
        Intent i = new Intent(this, TakeData.class);
        startActivity(i);
    }

    public void PauseSound(View view) {
        mp.pause();
        btnSound.setVisibility(View.GONE);
        btnNosound.setVisibility(View.VISIBLE);
    }


    public void PlaySound(View view) {
        mp.start();
        btnSound.setVisibility(View.VISIBLE);
        btnNosound.setVisibility(View.GONE);
    }
}