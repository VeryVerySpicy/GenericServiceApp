package com.example.myapplication.Adapters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.os.Environment;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.myapplication.Models.Person;
import java.io.OutputStream;


public class WebAccessAdapter {
    String url;

    public WebAccessAdapter(String url) {
        this.url = url;
    }

    public List<Person> fetchAndParseJson() {
        String jsonString = fetchJsonFromUrl(this.url); // Use the instance variable here
        if (jsonString != null) {
            try {
                return parseJson(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>(); // Return an empty list if fetching or parsing fails
    }

    private String fetchJsonFromUrl(String urlString) {
        StringBuilder response = new StringBuilder();
        HttpURLConnection conn = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int status = conn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = conn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Log.d("Web Access:", "Response: " + response.toString()); // Log the response
            } else {
                Log.e("Web Access:", "Error fetching data, response code: " + status);
                InputStream errorStream = conn.getErrorStream();
                if (errorStream != null) {
                    reader = new BufferedReader(new InputStreamReader(errorStream));
                    StringBuilder errorResponse = new StringBuilder();
                    String errorLine;
                    while ((errorLine = reader.readLine()) != null) {
                        errorResponse.append(errorLine);
                    }
                    Log.e("Web Access:", "Error response: " + errorResponse.toString());
                }
            }
        } catch (Exception e) {
            Log.e("Web Access:", "Exception: " + e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e("Web Access:", "Error closing reader", e);
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return response.toString();
    }



    public List<Person> parseJson(String jsonString) throws JSONException {
        List<Person> personList = new ArrayList<>();

        // Parse the JSON object
        Log.d("Web Access:", "Received JSON: " + jsonString);
        jsonString = jsonString.trim();

        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject personObject = jsonArray.getJSONObject(i);

            // Extract data using the correct keys
            int id = personObject.getInt("id");
            String firstName = personObject.getString("firstName");
            String lastName = personObject.getString("lastName");
            String photoPath = personObject.getString("photo");
            String address = personObject.getString("address");

            // Download the image and update the photoPath
            String localPhotoPath = downloadImage(url + "/" + photoPath);

            // Create a set for statuses
            Set<String> statusSet = new HashSet<>();
            JSONArray statusesArray = personObject.getJSONArray("statuses"); // Get the statuses array
            for (int j = 0; j < statusesArray.length(); j++) {
                String status = statusesArray.getString(j);
                statusSet.add(status.trim().toLowerCase()); // Add each status to the set
            }

            // Create a Person object and add it to the list
            Person person = new Person(id, firstName, lastName, localPhotoPath, address, statusSet);
            personList.add(person);
        }

        return personList;
    }

    public String downloadImage(String imageUrl) {
        InputStream input = null;
        FileOutputStream output = null;
        String localFilePath = null; // Initialize local file path
        try {
            // Create the URL object
            URL url = new URL(imageUrl);
            input = new BufferedInputStream(url.openStream());

            // Create a directory to save images
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyAppImages");
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory if it doesn't exist
            }

            // Create the output file
            File imageFile = new File(directory, imageUrl.substring(imageUrl.lastIndexOf('/') + 1));
            output = new FileOutputStream(imageFile);
            localFilePath = imageFile.getAbsolutePath(); // Update local file path

            // Download the image and save it
            byte[] data = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            Log.d("Web Access:", "Image downloaded to: " + localFilePath);
        } catch (Exception e) {
            Log.e("Web Access:", "Error downloading image: " + e.getMessage());
        } finally {
            try {
                if (output != null) output.close();
                if (input != null) input.close();
            } catch (Exception e) {
                Log.e("Web Access:", "Error closing streams: " + e.getMessage());
            }
        }
        return localFilePath; // Return the local file path
    }

    // This method will send the updated person list to the server
    public void sendUpdatedPersonListToServer(List<Person> personList) {
        // Convert the list of Person objects into a JSON string
        String jsonString = convertPersonListToJson(personList);

        // Now send the JSON to the server
        sendJsonToServer(jsonString);
    }

    // Convert the Person list to JSON string
    private String convertPersonListToJson(List<Person> personList) {
        JSONArray jsonArray = new JSONArray();

        // Iterate over the personList and create JSON objects
        for (Person person : personList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", person.getId());
                jsonObject.put("firstName", person.getFirstName());
                jsonObject.put("lastName", person.getLastName());
                jsonObject.put("photo", person.getPhotoPath());
                jsonObject.put("address", person.getAddress());

                // Convert the Set of statuses to a JSONArray
                JSONArray statusesArray = new JSONArray(person.getStatuses());
                jsonObject.put("statuses", statusesArray);

                // Add the JSON object to the JSON array
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Log.e("WebAccessAdapter", "Error creating JSON object for person: " + person.getId(), e);
            }
        }

        // Convert the entire JSON array into a string
        return jsonArray.toString();
    }

    public void sendJsonToServer(String jsonData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                OutputStream os = null;
                try {
                    URL url = new URL(WebAccessAdapter.this.url);
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true); // Set to true to send a request body

                    // Write the JSON data to the output stream
                    os = conn.getOutputStream();
                    os.write(jsonData.getBytes("UTF-8"));
                    os.flush();

                    int responseCode = conn.getResponseCode();
                    Log.d("WebAccess", "Response Code: " + responseCode);
                } catch (Exception e) {
                    Log.e("WebAccess", "Error sending data: " + e.getMessage(), e);
                } finally {
                    try {
                        if (os != null) os.close();
                        if (conn != null) conn.disconnect();
                    } catch (Exception e) {
                        Log.e("WebAccess", "Error closing resources: " + e.getMessage(), e);
                    }
                }
            }
        }).start(); // Start the thread
    }
}
