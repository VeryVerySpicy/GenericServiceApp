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
            holder.taskButton = convertView.findViewById(R.id.listTaskBtn);
            holder.checkboxLayout = convertView.findViewById(R.id.listCheckboxLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Person person = personList.get(position);

        // Load image
        String photoPath = person.getPhotoPath();
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap != null) {
            holder.personImageView.setImageBitmap(bitmap);
        } else {
            holder.personImageView.setImageResource(R.drawable.cat);
        }

        holder.personTextView.setText(person.getFullInfo());

        // Set visibility of the checkbox layout
        if (person.isCheckboxLayoutVisible()) {
            holder.checkboxLayout.setVisibility(View.VISIBLE);
            addCheckboxes(holder.checkboxLayout, person, holder.personTextView);
        } else {
            holder.checkboxLayout.setVisibility(View.GONE);
        }

        // Set the status button click listener
        holder.statusButton.setOnClickListener(view -> {
            boolean isVisible = person.isCheckboxLayoutVisible();
            person.setCheckboxLayoutVisible(!isVisible);
            notifyDataSetChanged(); // Refresh the ListView
        });

        // Set the Task button click listener
        holder.taskButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskManagementActivity.class);
            intent.putExtra("clientName", person.getFullInfo());
            intent.putExtra("clientPhoto", person.getPhotoPath());
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
            webAccessAdapter.sendUpdatedPersonListToServer(personList);
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
}
