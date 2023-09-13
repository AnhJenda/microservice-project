package com.example.userservice.service;

import com.example.userservice.entities.User;
import com.example.userservice.entities.Video;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/*
    @author: Dinh Quang Anh
    Date   : 9/6/2023
    Project: user-service
*/
@Service
public class UserService {

    private final UserRepository repository;
    private final RestTemplate restTemplate;

    @Autowired
    public UserService(UserRepository repository,
                       RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }


    public User save(User user) {
        return this.repository.save(user);
    }

    public User getById(long id) {
        return this.repository.findById(id).orElse(null);
    }

    public User getUserWithVideo(String id) {
        User user = this.getById(Long.valueOf(id));

        Video video = restTemplate.getForObject("http://video" + user.getVideoId(), Video.class);

        return user;
    }

    public Optional<User> getUserByEmail(String email){
        Optional<User> user = repository.getUserByEmail(email);
        if (Objects.isNull(user)){
            System.out.println("user not found");
            return null;
        }
        return user;
    }

    public List<User> findAll(){
        return repository.findAll();
    }
}
