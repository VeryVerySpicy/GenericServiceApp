package com.example.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myapplication.Models.Person;
import com.example.myapplication.R;
import com.example.myapplication.Adapters.OnSwipeTouchListenerAdapter;
import com.example.myapplication.Adapters.WebAccessAdapter;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class DetailActivity extends Activity {

    private WebAccessAdapter webAccessAdapter;
    Intent intent;
    ArrayList<Person> data;
    int position;
    private ImageView picture;
    private TextView personInfoView;
    private LinearLayout checkboxLayout;
    private Button statusButton;
    private final String[] checkboxOptions = {"active", "inactive", "pending"};
    private List<Person> personList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        webAccessAdapter = new WebAccessAdapter(getString(R.string.base_url));
        picture = findViewById(R.id.detailImage);
        personInfoView = findViewById(R.id.detailPersonInfo);
        checkboxLayout = findViewById(R.id.detailCheckboxLayout);
        statusButton = findViewById(R.id.detailStatusBtn);

        intent = getIntent();
        position = getIntent().getExtras().getInt("KEY_POSITION");
        data = Objects.requireNonNull(getIntent().getExtras()).getParcelableArrayList("KEY_PERSON_LIST");

        changePerson(position);

        picture.setOnTouchListener(new OnSwipeTouchListenerAdapter(DetailActivity.this) {
            public void onSwipeRight() {
                if (position < data.size() - 1)
                    position++;
                else
                    position = 0;
                changePerson(position);
            }
            public void onSwipeLeft() {
                if (position > 0)
                    position--;
                else
                    position = data.size() - 1;
                changePerson(position);
            }
        });

        statusButton.setOnClickListener(this::toggleCheckboxes);

    }

    private void changePerson (int pos)
    {
        Person person = data.get(pos);

        // Load the image from the local path
        String photoPath = person.getPhotoPath(); // Assuming this method returns the local file path
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath); // Load the image from the file

        if (bitmap != null) {
            picture.setImageBitmap(bitmap); // Set the bitmap to the ImageView
        } else {
            // Optionally set a placeholder image if the bitmap is null
            picture.setImageResource(R.drawable.cat); // Replace with your placeholder image
        }
        personInfoView = findViewById(R.id.detailPersonInfo);
        personInfoView.setText(person.getFullInfo());
    }


    // Toggle checkboxes visibility and populate them
    private void toggleCheckboxes(View view) {
        if (checkboxLayout.getVisibility() == View.VISIBLE) {
            checkboxLayout.setVisibility(View.GONE);
        } else {
            checkboxLayout.setVisibility(View.VISIBLE);
            addCheckboxes(data.get(position));
        }
    }

    // Add checkboxes based on the status
    private void addCheckboxes(Person person) {
        checkboxLayout.removeAllViews(); // Clear previous checkboxes if any
        boolean[] checkedStates = getCheckedStates(person.getStatuses());
        for (int i = 0; i < checkboxOptions.length; i++) {
            checkboxLayout.addView(createCheckbox(checkboxOptions[i], checkedStates[i], person));
        }
    }

    // Create a checkbox and set its listener
    private CheckBox createCheckbox(String option, boolean isChecked, Person person) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(option);
        checkBox.setChecked(isChecked);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
            String message = isChecked1 ? option + " selected" : option + " deselected";
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            if (isChecked1) {
                person.addStatus(option);
            } else {
                person.removeStatus(option);
            }
            personInfoView.setText(person.getFullInfo()); // Update the displayed person info if necessary

            // Send updated list to the server after checkbox status is changed
            webAccessAdapter.sendUpdatedPersonListToServer(data);
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
