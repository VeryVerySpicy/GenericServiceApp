package com.example.myapplication.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.Adapters.TaskAdapter;
import com.example.myapplication.Models.Task;
import com.example.myapplication.R;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskManagementActivity extends AppCompatActivity {

    private RecyclerView taskListRecyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    // Fields remain unchanged
    private TextView clientNameTextView, clientAddressTextView, selectedDateTextView, selectedTimeTextView;
    private Spinner taskTypeSpinner;
    private Button datePickerButton, timePickerButton, saveTaskButton, cancelTaskButton;
    private CheckBox repeatTaskCheckbox;
    private LinearLayout daysOfWeekLayout;

    private String selectedDate, selectedTime;
    private Set<String> selectedDays;
    private Context context;

    private ImageView clientImageView; // Add this to the class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);

        // Initialize Views
        clientNameTextView = findViewById(R.id.clientNameTextView);
        clientAddressTextView = findViewById(R.id.clientAddressTextView);
        clientImageView = findViewById(R.id.clientImageView); // Initialize the ImageView
        taskTypeSpinner = findViewById(R.id.taskTypeSpinner);
        datePickerButton = findViewById(R.id.datePickerButton);
        timePickerButton = findViewById(R.id.timePickerButton);
        saveTaskButton = findViewById(R.id.saveTaskButton);
        cancelTaskButton = findViewById(R.id.cancelTaskButton);
        repeatTaskCheckbox = findViewById(R.id.repeatTaskCheckbox);
        daysOfWeekLayout = findViewById(R.id.daysOfWeekLayout);
        selectedDateTextView = findViewById(R.id.selectedDateTextView);
        selectedTimeTextView = findViewById(R.id.selectedTimeTextView);
        taskListRecyclerView = findViewById(R.id.taskListRecyclerView);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);
        taskListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListRecyclerView.setAdapter(taskAdapter);

        selectedDays = new HashSet<>();
        context = this;

        // Initially disable Save Task button
        saveTaskButton.setEnabled(false);

        // Retrieve client data from Intent
        String clientName = getIntent().getStringExtra("clientName");
        String clientAddress = getIntent().getStringExtra("clientAddress");
        String clientImageUri = getIntent().getStringExtra("clientPhoto"); // Retrieve the image URI

        // Display client details
        if (clientName != null) clientNameTextView.setText(clientName);
        if (clientAddress != null) clientAddressTextView.setText(clientAddress);

        // Load the image into the ImageView
        if (clientImageUri != null) {
            Uri imageUri = Uri.parse(clientImageUri); // Convert the string URI to Uri
            clientImageView.setImageURI(imageUri); // Set the image in the ImageView
        }

        // Set up Task Type Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.task_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskTypeSpinner.setAdapter(adapter);

        // Date Picker
        datePickerButton.setOnClickListener(view -> openDatePicker());

        // Time Picker
        timePickerButton.setOnClickListener(view -> openTimePicker());

        // Repeat Task Checkbox
        repeatTaskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            daysOfWeekLayout.setVisibility(isChecked ? View.VISIBLE : View.GONE);

        });


        // Save Task Button
        saveTaskButton.setOnClickListener(view -> saveTask(clientName, clientAddress));

        // Cancel Task Button
        cancelTaskButton.setOnClickListener(view -> finish());

        // Populate Days of Week Checkboxes
        populateDaysOfWeek();

        loadSavedTasks(clientName);
        /*
        // Load saved task data if any
        if (clientName != null) {
            loadSavedTask(clientName); // Pass clientName to loadSavedTask
        }*/
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    selectedDateTextView.setText(selectedDate);
                    checkFormCompletion(); // Check if form is complete
                }, year, month, day);
        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    selectedTime = hourOfDay + ":" + String.format("%02d", minute1);
                    selectedTimeTextView.setText(selectedTime);
                    checkFormCompletion(); // Check if form is complete
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void checkFormCompletion() {
        // Enable Save Task button only when both date and time are selected
        saveTaskButton.setEnabled(selectedDate != null && selectedTime != null);
    }

    private void populateDaysOfWeek() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        for (String day : days) {
            CheckBox dayCheckbox = new CheckBox(this);
            dayCheckbox.setText(day);
            dayCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedDays.add(day);
                } else {
                    selectedDays.remove(day);
                }
            });
            daysOfWeekLayout.addView(dayCheckbox);
        }
    }

    private void saveTask(String clientName, String clientAddress) {
        String taskType = taskTypeSpinner.getSelectedItem().toString();
        if (selectedDate == null || selectedTime == null) {
            Toast.makeText(context, "Please select both date and time.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isRepeating = repeatTaskCheckbox.isChecked();

        // Create a new Task object
        Task newTask = new Task(taskType, selectedDate, selectedTime, isRepeating, selectedDays);

        // Add the Task object to the task list
        taskList.add(newTask);

        // Notify the adapter to update the RecyclerView
        taskAdapter.notifyDataSetChanged();

        // Optionally scroll to the newly added task
        taskListRecyclerView.scrollToPosition(taskList.size() - 1);

        // Save the task list for the current client to SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Tasks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Convert the taskList to a Set of strings
        Set<String> taskSet = new HashSet<>();
        for (Task task : taskList) {
            taskSet.add(task.toString()); // Convert task to string representation
        }

        // Use the clientName as the key to save tasks specific to this client
        editor.putStringSet(clientName + "_taskList", taskSet); // Use clientName to uniquely identify the task list
        editor.apply();

        // Go back to the previous screen
        finish();
    }


    private void loadSavedTasks(String clientName) {
        SharedPreferences sharedPreferences = getSharedPreferences("Tasks", MODE_PRIVATE);

        // Use the clientName to retrieve the task list for this specific client
        Set<String> taskSet = sharedPreferences.getStringSet(clientName + "_taskList", new HashSet<>()); // Use client-specific key

        taskList.clear(); // Clear any existing tasks

        // Loop through each task string and convert it back to a Task object
        for (String taskString : taskSet) {
            // Split the task string into parts (you may need to adjust this depending on the format)
            String[] parts = taskString.split(" \\| "); // Split by " | " (ensure spacing is correct)

            if (parts.length >= 4) {
                String taskType = parts[0];
                String date = parts[1];
                String time = parts[2];
                boolean isRepeating = parts[3].startsWith("Repeats on:");
                Set<String> selectedDays = new HashSet<>();


                if (isRepeating && parts.length > 3) {
                    // Add selected days from the string (e.g., "Monday, Tuesday")
                    String daysString = parts[3].replace("Repeats on: ", "");
                    String[] days = daysString.split(", ");
                    selectedDays.addAll(Arrays.asList(days));
                }

                // Create a Task object and add it to the task list
                Task task = new Task(taskType, date, time, isRepeating, selectedDays);
                taskList.add(task);
            }
        }

        // Notify the adapter to update the UI
        taskAdapter.notifyDataSetChanged();
    }




/*
    private void loadSavedTask(String clientName) {
        SharedPreferences sharedPreferences = getSharedPreferences("Tasks", MODE_PRIVATE);
        String savedTaskType = sharedPreferences.getString(clientName + "_taskType", null);
        String savedDate = sharedPreferences.getString(clientName + "_date", null);
        String savedTime = sharedPreferences.getString(clientName + "_time", null);
        boolean isRepeating = sharedPreferences.getBoolean(clientName + "_isRepeating", false);
        Set<String> savedDays = sharedPreferences.getStringSet(clientName + "_selectedDays", new HashSet<>());

        if (savedTaskType != null) {
            taskTypeSpinner.setSelection(((ArrayAdapter<String>) taskTypeSpinner.getAdapter()).getPosition(savedTaskType));
            selectedDateTextView.setText(savedDate);
            selectedTimeTextView.setText(savedTime);
            repeatTaskCheckbox.setChecked(isRepeating);
            if (isRepeating) {
                for (int i = 0; i < daysOfWeekLayout.getChildCount(); i++) {
                    View view = daysOfWeekLayout.getChildAt(i);
                    if (view instanceof CheckBox) {
                        CheckBox dayCheckbox = (CheckBox) view;
                        if (savedDays.contains(dayCheckbox.getText().toString())) {
                            dayCheckbox.setChecked(true);
                        }
                    }
                }
            }
        }
    }*/
}
