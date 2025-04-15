package com.expensetracker.tracker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HuggingFaceService {

    @Value("${huggingface.api.key}")
    private String apiKey;  // Get your Hugging Face API Key from application.properties

    // Using distilbert-base-uncased model from Hugging Face
    private static final String API_URL = "https://api-inference.huggingface.co/models/distilbert-base-uncased";


    /**
     * Get the category for an expense title using Hugging Face API
     * @param title The title of the expense (e.g., "Lunch at McDonald's")
     * @return The category of the expense based on highest score
     */
    public String getCategory(String title) {
        // Prepare the request payload to send to Hugging Face API
        String payload = "{\n" +
                "  \"inputs\": \"" + title + "\",\n" +
                "  \"parameters\": {\"candidate_labels\": [\"Groceries\", \"Entertainment\", \"Bills\", \"Transport\", \"Food\"]}\n" +
                "}";

        // Set up headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Prepare RestTemplate and HttpEntity for making the request
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        try {
            // Send the request to Hugging Face API
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

            // Log the full raw response from Hugging Face API for debugging
            System.out.println("Raw HuggingFace Response: " + response.getBody());

            // Parse the JSON response to extract the labels and scores
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            // Log the parsed labels and scores for debugging
            JsonNode labels = root.path("labels");
            JsonNode scores = root.path("scores");

            System.out.println("Parsed Labels: " + labels);
            System.out.println("Parsed Scores: " + scores);

            // Ensure that labels and scores are arrays and have the same length
            if (labels.isArray() && scores.isArray() && labels.size() == scores.size()) {
                double highestScore = -1;
                String highestLabel = "Other"; // Default fallback label

                // Loop through labels and scores to find the label with the highest score
                for (int i = 0; i < labels.size(); i++) {
                    double score = scores.get(i).asDouble();
                    if (score > highestScore) {
                        highestScore = score;
                        highestLabel = labels.get(i).asText();  // Set the label with the highest score
                    }
                }

                // Return the label with the highest score
                return highestLabel;
            } else {
                // Handle case when the labels and scores don't match
                System.out.println("Error: Labels and scores do not match or are not in an array format.");
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log any exceptions for further debugging
        }

        return "Other"; // Default fallback if parsing or request fails
    }
}
