package com.example.videoservice.controller;

import com.example.videoservice.entity.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/")
public class VideoController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Environment env;

    @RequestMapping("/")
    public String home() {
        // This is useful for debugging
        // When having multiple instance of gallery service running at different ports.
        // We load balance among them, and display which instance received the request.
        return "Hello from Video Service running at port: " + env.getProperty("local.server.port");
    }

//    @RequestMapping("/video")
//    public String getVideo() {
//        // This is useful for debugging
//        // When having multiple instance of gallery service running at different ports.
//        // We load balance among them, and display which instance received the request.
//        String testMsg = restTemplate.getForObject("http://user-service/test", String.class);
//        return testMsg;
//    }

    @RequestMapping("/user")
    public List<Object> getUser() {
        // create video object
        Video video = new Video();
//        video.setId(id);

        // get list of available users
        List<Object> user = restTemplate.getForObject("http://user-service/user", List.class);
        video.setUser(user);

        return video.getUser();
    }

    // -------- Admin Area --------
    // This method should only be accessed by users with role of 'admin'
    // We'll add the logic of role based auth later
    @RequestMapping("/admin")
    public String homeAdmin() {
        return "This is the admin area of Gallery service running at port: " + env.getProperty("local.server.port");
    }
}