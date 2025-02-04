package com.example.myapplication.Activities;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.example.myapplication.R;

public class NotificationActivity extends AppCompatActivity {
    TextView textView;
    PlayerView playerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);
        textView = findViewById(R.id.textView);
        playerView = findViewById(R.id.playerView);
        //getting the notification message
        String type = getIntent().getStringExtra("type");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String message = type + "\n" + date + "\n" + time;
        textView.setText(message);
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
    }
}