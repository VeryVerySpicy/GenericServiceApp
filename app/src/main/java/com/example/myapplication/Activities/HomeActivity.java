package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.RadioGroup;
import androidx.appcompat.widget.SearchView;

import com.example.myapplication.Models.Person;
import com.example.myapplication.R;
import com.example.myapplication.Adapters.PersonAdapter;
import com.example.myapplication.Adapters.WebAccessAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private List<Person> originalPersonList;
    private List<Person> filteredPersonList;
    private PersonAdapter personAdapter;
    private EditText searchEditText;  // Ensure this corresponds to the XML ID search_view
    private Button searchButton;
    private ListView personListView;
    private WebAccessAdapter webAccessAdapter;
    private RadioGroup sortRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Correct the way you're accessing the SearchView and its EditText
        SearchView searchView = findViewById(R.id.search_view);  // Reference the SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);  // Get the internal EditText

        searchButton = findViewById(R.id.searchButton);  // Reference the search button
        personListView = findViewById(R.id.personListView);
        sortRadioGroup = findViewById(R.id.sortRadioGroup);

        originalPersonList = new ArrayList<>();
        filteredPersonList = new ArrayList<>();

        webAccessAdapter = new WebAccessAdapter(getString(R.string.base_url));



        // Instantiate WebAccess and fetch JSON data
        new Thread(new Runnable() {
            @Override
            public void run() {
                originalPersonList = webAccessAdapter.fetchAndParseJson();
                filteredPersonList.addAll(originalPersonList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        personAdapter = new PersonAdapter(HomeActivity.this, originalPersonList, webAccessAdapter);
                        personListView.setAdapter(personAdapter);
                    }
                });
            }
        }).start();

        // Set up search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList(searchEditText);  // Pass the EditText as a parameter to the filterList method
            }
        });

        sortRadioGroup.setOnCheckedChangeListener((group, checkedId) -> sortPersonList(checkedId));

        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getApplicationContext(), DetailActivity.class);
                in.putExtra("KEY_POSITION", position);
                if (filteredPersonList.size() > 0) {
                    in.putParcelableArrayListExtra("KEY_PERSON_LIST", (ArrayList<? extends Parcelable>) filteredPersonList);
                } else {
                    in.putParcelableArrayListExtra("KEY_PERSON_LIST", (ArrayList<? extends Parcelable>) originalPersonList);
                }
                startActivity(in);
            }
        });
    }

    private void filterList(EditText searchEditText) {
        String query = searchEditText.getText().toString().toLowerCase();
        filteredPersonList.clear();

        if (!query.isEmpty()) {
            for (Person person : originalPersonList) {
                if (person.getFullInfo().toLowerCase().contains(query)) {
                    filteredPersonList.add(person);
                }
            }
        } else {
            filteredPersonList.addAll(originalPersonList);
        }

        // Instead of re-creating the adapter, just update the list
        personAdapter.notifyDataSetChanged();
    }

    private void sortPersonList(int checkedId) {
        Comparator<Person> comparator = null;

        if (checkedId == R.id.sortByFirstName) {
            comparator = Comparator.comparing(Person::getFirstName);
        } else if (checkedId == R.id.sortByLastName) {
            comparator = Comparator.comparing(Person::getLastName);
        } else if (checkedId == R.id.sortByAddress) {
            comparator = Comparator.comparing(person -> getUnitNum(person.getAddress()));
        }

        if (comparator != null) {
            Collections.sort(filteredPersonList, comparator);
            personAdapter = new PersonAdapter(this, filteredPersonList, webAccessAdapter);
            personListView.setAdapter(personAdapter);
        }
    }

    public String getUnitNum(String input) {
        // Split the input string by whitespace
        String[] parts = input.split("\\s+");
        return parts.length > 0 ? parts[0] : "";
    }
}
