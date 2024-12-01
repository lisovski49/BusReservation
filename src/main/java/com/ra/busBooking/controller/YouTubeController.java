package com.ra.busBooking.controller;

import com.ra.busBooking.service.YouTubeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YouTubeController {

    private final YouTubeService youTubeService;

    public YouTubeController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }

    @GetMapping("/video-details")
    public String getVideoDetails(@RequestParam String videoId) {
        return youTubeService.getVideoDetails(videoId);
    }
}
