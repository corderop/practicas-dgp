package com.example.vality;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.videoplayer);

        VideoView video = findViewById(R.id.video_view);
        MediaController mediaController = new MediaController(this, false);

        String url = getIntent().getStringExtra("url");

        System.out.println("Creamos mensaje para pedir video: " + url);

        video.setVideoPath(url);
        video.requestFocus();

        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        video.start();
    }
}
