package com.moodflix.service;

import com.moodflix.model.Quote;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class QuoteService {

    private static final String API_URL = "https://zenquotes.io/api/random";

    /**
     * ✅ Method used by GUI (fetches a random quote and returns Quote object)
     */
    public Quote fetchQuote(String mood) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(API_URL, String.class);

            JSONArray jsonArray = new JSONArray(response);
            JSONObject quoteObj = jsonArray.getJSONObject(0);

            String text = quoteObj.getString("q");
            String author = quoteObj.getString("a");

            return new Quote(text, author, mood);
        } catch (Exception e) {
            e.printStackTrace();
            return new Quote("Error fetching quote.", "System", mood);
        }
    }

    /**
     * ✅ Method used by other classes (if needed)
     */
    public String[] getRandomQuote() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(API_URL, String.class);

            JSONArray jsonArray = new JSONArray(response);
            JSONObject quoteObj = jsonArray.getJSONObject(0);

            String quote = quoteObj.getString("q");
            String author = quoteObj.getString("a");

            return new String[] { quote, author };
        } catch (Exception e) {
            e.printStackTrace();
            return new String[] { "Error fetching quote.", "System" };
        }
    }
}
