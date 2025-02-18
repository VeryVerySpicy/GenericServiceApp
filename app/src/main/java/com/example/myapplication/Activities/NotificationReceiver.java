package com.example.myapplication.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import com.example.myapplication.Activities.NotificationActivity;
import com.example.myapplication.R;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Extract task details from the intent
        String taskId = intent.getStringExtra("taskId");
        String taskType = intent.getStringExtra("taskType");
        String taskDate = intent.getStringExtra("taskDate");
        String taskTime = intent.getStringExtra("taskTime");

        // Create an Intent to open NotificationActivity when tapped
        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        notificationIntent.putExtra("type", taskType);
        notificationIntent.putExtra("date", taskDate);
        notificationIntent.putExtra("time", taskTime);
        notificationIntent.putExtra("taskId", taskId);  // Optional: pass taskId to handle further actions

        // Create a PendingIntent to trigger NotificationActivity when notification is clicked
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Reminder")
                .setSmallIcon(R.drawable.cat) //set icon for notification
                .setContentTitle(taskType)
                .setContentText("Task is due on " + taskDate + " at " + taskTime)
                .setContentIntent(pendingIntent) // Set the PendingIntent to open the activity
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Dismiss the notification once clicked

        // Show the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(taskId.hashCode(), builder.build());
    }
}
