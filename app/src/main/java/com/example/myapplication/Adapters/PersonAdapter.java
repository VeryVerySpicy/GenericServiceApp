package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;

import com.example.myapplication.Models.Person;
import com.example.myapplication.R;
import com.example.myapplication.Activities.TaskManagementActivity;

import java.util.List;
import java.util.Set;

public class PersonAdapter extends BaseAdapter {
    private List<Person> personList;
    private Context context;
    private WebAccessAdapter webAccessAdapter;
    private final String[] checkboxOptions = {"active", "inactive", "pending"};

    public PersonAdapter(Context context, List<Person> personList, WebAccessAdapter webAccessAdapter) {
        this.context = context;
        this.personList = personList;
        this.webAccessAdapter = webAccessAdapter;
    }

    @Override
    public int getCount() {
        return personList.size();
    }

    @Override
    public Object getItem(int position) {
        return personList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_listview, parent, false);

            holder = new ViewHolder();
            holder.personImageView = convertView.findViewById(R.id.personImageView);
            holder.personTextView = convertView.findViewById(R.id.personTextView);
            holder.statusButton = convertView.findViewById(R.id.listStatusBtn);
            holder.taskButton = convertView.findViewById(R.id.listTaskBtn); // Use the correct ID
            holder.checkboxLayout = convertView.findViewById(R.id.listCheckboxLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Person person = personList.get(position);

        // Load the image from the local path
        String photoPath = person.getPhotoPath();
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);

        if (bitmap != null) {
            holder.personImageView.setImageBitmap(bitmap);
        } else {
            holder.personImageView.setImageResource(R.drawable.cat); // Placeholder image
        }

        holder.personTextView.setText(person.getFullInfo());

        // Set the status button click listener
        holder.statusButton.setOnClickListener(view -> toggleCheckboxes(holder, position));

        // Set the Task button click listener
        holder.taskButton.setOnClickListener(v -> {
            // Create an Intent to start TaskManagementActivity
            Intent intent = new Intent(context, TaskManagementActivity.class);

            // Pass client details via Intent extras
            intent.putExtra("clientName", person.getFullInfo()); // Or pass other relevant details
            intent.putExtra("clientPhoto", person.getPhotoPath()); // If you want to pass the photo too
            context.startActivity(intent);
        });


        return convertView;
    }

    // Update the ViewHolder to include the Task button
    static class ViewHolder {
        ImageView personImageView;
        TextView personTextView;
        Button statusButton;
        Button taskButton;
        LinearLayout checkboxLayout;
    }

    // Method to toggle the visibility of checkboxes
    private void toggleCheckboxes(ViewHolder holder, int position) {
        if (holder.checkboxLayout.getVisibility() == View.VISIBLE) {
            holder.checkboxLayout.setVisibility(View.GONE);
        } else {
            holder.checkboxLayout.setVisibility(View.VISIBLE);
            addCheckboxes(holder.checkboxLayout, personList.get(position), holder.personTextView);
        }
    }

    // Create the checkboxes based on the status
    private void addCheckboxes(LinearLayout layout, Person person, TextView personTextView) {
        layout.removeAllViews(); // Clear previous checkboxes if any

        boolean[] checkedStates = getCheckedStates(person.getStatuses());
        for (int i = 0; i < checkboxOptions.length; i++) {
            layout.addView(createCheckbox(checkboxOptions[i], checkedStates[i], person, personTextView));
        }
    }

    // Create a checkbox and set its listener
    private CheckBox createCheckbox(String option, boolean isChecked, Person person, TextView personTextView) {
        CheckBox checkBox = new CheckBox(context);
        checkBox.setText(option);
        checkBox.setChecked(isChecked);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            String message = isChecked1 ? option + " selected" : option + " deselected";
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            if (isChecked1) {
                person.addStatus(option);
            } else {
                person.removeStatus(option);
            }
            personTextView.setText(person.getFullInfo());

            // Send updated JSON to the server
            sendUpdatedJson();
        });

        return checkBox;
    }

    // Determine which checkboxes should be checked based on the status
    private boolean[] getCheckedStates(Set<String> statuses) {
        boolean[] checkedStates = new boolean[checkboxOptions.length];

        for (int i = 0; i < checkboxOptions.length; i++) {
            checkedStates[i] = statuses.contains(checkboxOptions[i]);
        }

        return checkedStates;
    }

    // Method to send the updated JSON to the server
    private void sendUpdatedJson() {

        // Build the JSON string from the personList
        JSONArray jsonArray = new JSONArray();
        for (Person p : personList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", p.getId());
                jsonObject.put("firstName", p.getFirstName());
                jsonObject.put("lastName", p.getLastName());
                jsonObject.put("photo", p.getPhotoPath());
                jsonObject.put("address", p.getAddress());
                jsonObject.put("statuses", new JSONArray(p.getStatuses())); // Convert Set to JSONArray
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Log.e("PersonAdapter", "Error creating JSON object: " + e.getMessage());
            }
        }

        // Convert JSONArray to String
        String jsonString = jsonArray.toString();
        // Send JSON to the server
        this.webAccessAdapter.sendJsonToServer(jsonString);
    }
}
