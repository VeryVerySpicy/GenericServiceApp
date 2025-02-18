package com.example.myapplication.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.myapplication.R;

public class NotificationActivity extends AppCompatActivity {
    TextView textView;
    PlayerView playerView;

    private static final String VIDEO_URI =
            "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        textView = findViewById(R.id.textView);
        playerView = findViewById(R.id.playerView);

        // Getting the notification message from the Intent
        String type = getIntent().getStringExtra("type");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String message = type + "\n" + date + "\n" + time;

        // Set the message to the TextView
        textView.setText(message);

        // Setup ExoPlayer to play the video
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        player.setMediaItem(MediaItem.fromUri(VIDEO_URI));
        player.prepare();
        player.setPlayWhenReady(true);
    }
}
