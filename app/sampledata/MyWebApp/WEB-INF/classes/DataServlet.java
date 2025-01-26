import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/") // Change the URL pattern to root
public class DataServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Get the path to the data.json file
        String jsonFilePath = getServletContext().getRealPath("/data.json"); // Adjust the path if necessary

        // Read the JSON file and send it as the response
        File jsonFile = new File(jsonFilePath);
        StringBuilder jsonBuilder = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Unable to read data file\"}");
            return;
        }

        System.out.println("Sent JSON array");
        // Write the JSON response
        response.getWriter().write(jsonBuilder.toString());
    }

/*     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String jsonArray = jsonBuilder.toString();
        System.out.println("Received JSON array: " + jsonArray);

        // Respond back with a success message
        String jsonResponse = "{\"status\": \"success\", \"message\": \"JSON array received and printed to server logs.\"}";
        response.getWriter().write(jsonResponse);
    } */

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String jsonArray = jsonBuilder.toString();
        System.out.println("Received JSON array: " + jsonArray);

        // Specify the path to the file you want to replace
        String filePath = getServletContext().getRealPath("/data.json"); // Adjust the path as needed

        // Write the received JSON data to the file, replacing the old file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(jsonArray);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorResponse = "{\"status\": \"error\", \"message\": \"Failed to save JSON file.\"}";
            response.getWriter().write(errorResponse);
            return;
        }

        // Respond back with a success message
        String jsonResponse = "{\"status\": \"success\", \"message\": \"JSON array received and replaced the file.\"}";
        response.getWriter().write(jsonResponse);
    }
}
