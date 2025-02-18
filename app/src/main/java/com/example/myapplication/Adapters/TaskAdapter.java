package com.example.myapplication.Adapters;

import com.example.myapplication.Activities.TaskManagementActivity;
import com.example.myapplication.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.myapplication.Models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private Context context;

    public TaskAdapter(List<Task> taskList, Context context)
    {
        this.taskList = taskList;
        this.context = context;

    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTypeTextView.setText(task.getTaskType());
        holder.dateTextView.setText(task.getDate());
        holder.timeTextView.setText(task.getTime());

        if (task.isRepeating()) {
            holder.repeatTextView.setText("Repeats on: " + String.join(", ", task.getSelectedDays()));
        } else {
            holder.repeatTextView.setText("One-time task");
        }

        // Set up the delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            // Remove the task from the list
            taskList.remove(position);
            // Notify the adapter that the item was removed
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, taskList.size());

            // Call a method in the activity to update the SharedPreferences
            if (holder.itemView.getContext() instanceof TaskManagementActivity) {
                ((TaskManagementActivity) holder.itemView.getContext()).deleteTask(task); // Pass the task to be deleted
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTypeTextView, dateTextView, timeTextView, repeatTextView;
        ImageButton deleteButton;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTypeTextView = itemView.findViewById(R.id.taskTypeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            repeatTextView = itemView.findViewById(R.id.repeatTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Initialize the delete button
        }
    }

}
