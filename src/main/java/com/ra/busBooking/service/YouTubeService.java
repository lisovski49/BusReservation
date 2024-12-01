package com.ra.busBooking.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YouTubeService {
    private final String apiKey = "AIzaSyBIJ54Z6iZZzA6xWRcGkYU3bOxVPcQVmo4";
    private final RestTemplate restTemplate;

    public YouTubeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getVideoDetails(String videoId) {
        try {
            String url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&key=" + apiKey;
            String response = restTemplate.getForObject(url, String.class);

            // Парсим JSON-ответ
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode snippet = root.path("items").get(0).path("snippet");

            String title = snippet.path("title").asText();
            String description = snippet.path("description").asText();

            return "Title: " + title + "\nDescription: " + description;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при вызове YouTube API", e);
        }
    }
}
