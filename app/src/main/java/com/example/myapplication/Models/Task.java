package com.example.myapplication.Models;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.Context;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.Activities.NotificationView;

import java.util.Set;
import java.util.UUID;

public class Task {
    private UUID taskId;
    private String taskType;
    private String date;
    private String time;
    private boolean isRepeating;
    private Set<String> selectedDays;

    public Task(UUID taskId, String taskType, String date, String time, boolean isRepeating, Set<String> selectedDays) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.date = date;
        this.time = time;
        this.isRepeating = isRepeating;
        this.selectedDays = selectedDays;
    }

    // Getter methods
    public UUID getTaskId() { return taskId; }
    public String getTaskType() { return taskType; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public boolean isRepeating() { return isRepeating; }
    public Set<String> getSelectedDays() { return selectedDays; }
    @Override
    public String toString() {
        return taskId + " | "  + taskType + " | " + date + " | " + time + " | " + (isRepeating ? "Repeats on: " + String.join(", ", selectedDays) : "No Repeat");
    }

    public void sendNotification(Context context, Task task)
    {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        //.setSmallIcon(R.drawable.messageicon) //set icon for notification
                        .setContentTitle("Notifications Example") //set title of notification
                        .setContentText("This is a notification message")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification


        Intent notificationIntent = new Intent(context, NotificationView.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
}
