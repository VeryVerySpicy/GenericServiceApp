package com.example.myapplication.Adapters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Socket;
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

    /**
     * This fetches a JSON file from the given URL using a low-level Socket API.
     * This method sends an HTTP GET request to the provided URL and retrieves the response.
     *
     * @param urlString the URL to fetch the JSON data from
     * @return the JSON response as a String, or an empty string if an error occurs
     */
    private String fetchJsonFromUrl(String urlString) {
        StringBuilder response = new StringBuilder();
        Socket socket = null;
        BufferedReader reader = null;
        OutputStream outStream;

        try {
            // url configuration
            URL url = new URL(urlString);
            String host = url.getHost();
            int port = url.getPort() == -1 ? 81 : url.getPort();
            String path = url.getPath().isEmpty() ? "/" : url.getPath();

            // Open a socket connection
            socket = new Socket(host, port);

            // Creates output stream to send the request
            outStream = socket.getOutputStream();
            outStream.write(("GET " + path + " HTTP/1.1\r\n").getBytes());
            outStream.write(("HOST: " + host + "\r\n").getBytes());
            outStream.write(("Connection: close\r\n\r\n").getBytes());
            outStream.flush();

            // Read the response
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            boolean isBody = false;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    isBody = true;
                } else if (isBody) {
                    response.append(line);
                }
            }
            // current has bug that fetch does not grab the one last letter and this will temporary fix for the issue
            response.append("]");

            Log.d("Socket Access:", "Response: " + response);

        } catch (Exception e) {
            Log.e("Socket Access:", "Exception: " + e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e("Socket Access:", "Error closing reader", e);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    Log.e("Socket Access: ", "Error closing socket", e);
                }
            }
        }
        return response.toString();
    }
/*    private String fetchJsonFromUrl(String urlString) {
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
    }*/

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

    /**
     *  This downloads an image from the given URL using the Socket API and saves it to the device's storage.
     *
     * @param imageUrl The URL of the image to download.
     * @return The local file path where the image is saved, or null if an error occurs.
     */
    public String downloadImage(String imageUrl) {
        Socket socket = null;
        FileOutputStream imageOutput = null;
        String localFilePath = null;
        BufferedInputStream bufferedInputStream;
        InputStream imageInput = null;
        OutputStream outStream;

        try {
            // Create the URL object
            URL url = new URL(imageUrl);
            String host = url.getHost();
            int port = url.getPort() == -1 ? 81 : url.getPort();
            String path = url.getPath().isEmpty() ? "/" : url.getPath();

            // Open a socket connection
            socket = new Socket(host, port);

            // Creates output stream to send the request
            outStream = socket.getOutputStream();
            outStream.write(("GET " + path + " HTTP/1.1\r\n").getBytes());
            outStream.write(("HOST: " + host + "\r\n").getBytes());
            outStream.write(("Connection: close\r\n").getBytes());
            outStream.write(("\r\n").getBytes());
            outStream.flush();

            // Read the response
            imageInput = socket.getInputStream();
            bufferedInputStream = new BufferedInputStream(imageInput);

            ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();
            int newLineCount = 0;
            while (newLineCount < 4) {
                int b = bufferedInputStream.read();
                headerBuffer.write(b);
                if (b == '\r' || b == '\n') {
                    newLineCount++;
                } else {
                    newLineCount = 0; // Reset if not continuous newlines
                }
            }
            Log.d("Socket Access:", "Headers: " + headerBuffer.toString("UTF-8"));

            // Create a directory to save images
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyAppImages");
            if (!directory.exists()) {
                directory.mkdirs(); // Create directory if it doesn't exist
            }

            // Create the output file
            File imageFile = new File(directory, path.substring(path.lastIndexOf('/') + 1));
            imageOutput = new FileOutputStream(imageFile);
            localFilePath = imageFile.getAbsolutePath(); // Update local file path

            // Download the image content as binary data
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                imageOutput.write(buffer, 0, bytesRead);
            }

            Log.d("Socket Access:", "Image downloaded to: " + localFilePath);
        } catch (Exception e) {
            Log.e("Socket Access:", "Error downloading image: " + e.getMessage());
        } finally {
            try {
                if (imageOutput != null) imageOutput.close();
                if (imageInput != null) imageInput.close();
                if (socket != null) socket.close();
            } catch (Exception e) {
                Log.e("Socket Access:", "Error closing streams: " + e.getMessage());
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

                // Transform the photo field value
                String photoPath = person.getPhotoPath().replace("/storage/emulated/0/Pictures/MyAppImages/", "images/");
                jsonObject.put("photo", photoPath);

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

        // Convert the JSON array into a string
        return jsonArray.toString().replace("\\/", "/");
    }

    /**
     * Sends JSON data to a server using a socket connection.
     * <p>
     * This method establishes a socket connection to the specified server and
     * sends an HTTP POST request with the given JSON data. The response from the
     * server is logged for debugging purposes. It runs on a separate thread to
     * avoid blocking the main thread.
     *
     * @param jsonData The JSON data to send to the server as a string.
     *                 Must be in valid JSON format.
     */
    public void sendJsonToServer(String jsonData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                OutputStream outStream = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(WebAccessAdapter.this.url);
                    String host = url.getHost();
                    int port = url.getPort() == -1 ? 81 : url.getPort();
                    String path = url.getPath().isEmpty() ? "/" : url.getPath();

                    // Open a socket connection
                    socket = new Socket(host, port);

                    // Send HTTP POST request with JSON data
                    outStream = socket.getOutputStream();
                    outStream.write(("POST " + path + " HTTP/1.1\r\n").getBytes());
                    outStream.write(("HOST: " + host + "\r\n").getBytes());
                    outStream.write(("Content-Type: application/json\r\n").getBytes());
                    outStream.write(("Content-Length: " + jsonData.length() + "\r\n").getBytes());
                    outStream.write(("Connection: close\r\n\r\n" + jsonData).getBytes());
                    outStream.flush();

                    // Read the server response
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line).append("\n");
                    }

                    Log.d("WebAccess", "Response Code: " + response);
                } catch (Exception e) {
                    Log.e("WebAccess", "Error sending data: " + e.getMessage(), e);
                } finally {
                    try {
                        if (outStream != null) outStream.close();
                        if (reader != null) reader.close();
                        if (socket != null) socket.close();
                    } catch (Exception e) {
                        Log.e("WebAccess", "Error closing resources: " + e.getMessage(), e);
                    }
                }
            }
        }).start(); // Start the thread
    }
}
