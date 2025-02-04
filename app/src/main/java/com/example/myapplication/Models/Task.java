package com.example.myapplication.Models;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.myapplication.Activities.NotificationActivity;
import com.example.myapplication.R;

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

    public void sendNotification(Context context)
    {
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.putExtra("type", getTaskType());
        intent.putExtra("date", getDate());
        intent.putExtra("time", getTime());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "test")
                .setSmallIcon(R.drawable.cat) //set icon for notification
                .setContentTitle(getTaskType())
                .setContentText("Task is due on " + getDate() + " at " + getTime())
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = getSystemService(context, NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "name";
            String description = "Service Task Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("test", name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

            notificationManager.notify(10, builder.build());
        }
    }
}