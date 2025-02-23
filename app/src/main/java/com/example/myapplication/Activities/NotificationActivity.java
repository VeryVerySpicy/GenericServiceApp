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
            "https://cdn.discordapp.com/attachments/691865206124445778/1343249516937089066/Med.mp4?ex=67bc9619&is=67bb4499&hm=91227197d3a8f68a1a6dfa1833e875240d94d858fa7f06c535c3e09e3bf6dd2f&";
    private static final String VIDEO_VITAL =
            "https://cdn.discordapp.com/attachments/691865206124445778/1343250148154933280/Vitals.mp4?ex=67bc96af&is=67bb452f&hm=963c1792c163be43f114b559f316b5d5b9983d14441f8b7a59dd439aebaea420&";

    private static final String VIDEO_HOUSE =
            "https://cdn.discordapp.com/attachments/691865206124445778/1343250131205619782/House.mp4?ex=67bc96ab&is=67bb452b&hm=5fe6387f00bfddee09e64780da20e0f8acb64ea159071e4ced3788107b11d857&";

    private static final String VIDEO_PERSONAL =
            "https://cdn.discordapp.com/attachments/691865206124445778/1343250158741229691/Self.mp4?ex=67bc96b2&is=67bb4532&hm=63e8398bb388474cf89cff047284ab020c1fe3030ee7659b2e4cdc51308e41cf&";

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
