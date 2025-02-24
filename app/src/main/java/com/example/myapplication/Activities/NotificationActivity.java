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

    private static final String VIDEO_MED =
            "https://storage.googleapis.com/bcitdataforclass/Med.mp4";
    private static final String VIDEO_VITAL =
            "https://storage.googleapis.com/bcitdataforclass/Vitals.mp4";
    private static final String VIDEO_HOUSE =
            "https://storage.googleapis.com/bcitdataforclass/House.mp4";
    private static final String VIDEO_PERSONAL =
            "https://storage.googleapis.com/bcitdataforclass/Self.mp4";

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

        if (type.equalsIgnoreCase("Med Reminders"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_MED));
        }
        else if (type.equalsIgnoreCase("Vitals Check"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_VITAL));
        }
        else if (type.equalsIgnoreCase("House Keeping"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_HOUSE));
        }
        else if (type.equalsIgnoreCase("Personal Care"))
        {
            player.setMediaItem(MediaItem.fromUri(VIDEO_PERSONAL));
        }
        player.prepare();
        player.setPlayWhenReady(true);
    }
}
