package com.example.myapplication.Adapters;

import com.example.myapplication.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.example.myapplication.Models.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
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
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTypeTextView, dateTextView, timeTextView, repeatTextView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskTypeTextView = itemView.findViewById(R.id.taskTypeTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            repeatTextView = itemView.findViewById(R.id.repeatTextView);
        }
    }
}
