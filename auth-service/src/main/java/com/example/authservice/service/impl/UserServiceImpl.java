package com.example.authservice.service.impl;

import com.example.authservice.dto.LoginResponseDto;
import com.example.authservice.entity.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.config.UserDetailsImpl;
import com.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl extends A_Service implements UserService {

    private RestTemplate restTemplate = new RestTemplate();


    @Autowired
    UserRepository repository;

    @Autowired
    private UserService userService;

        List<User> users;


    @Async
    public void sendMessage(String message){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public User getUserById(Long userId) {
        return repository.getById(userId);
    }

    @Override
    public User getUserCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = getByUsername(userDetails.getUsername());
        return user;
    }

    @Override
    public List<User> gets() {
        return repository.findAll();
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get(); // Extract the User object from Optional
        } else {
            return null; // Return null if user is not found
        }
    }

    @Override
    public User save(User user) {
        return  repository.save(user);
    }


    @Override
    public List<User> findAll() {
        users = repository.findAll();
        return users;
    }

    @Override
    public User findById(long id) {
//        return users.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
//        for (User user : users) {
//            if (user.getId() == id) {
//                return user;
//            }
//        }
        if (id > 0) {
           return repository.getById(id);
        }

        return null;
    }

    @Override
    public boolean add(User user) {
        if (!Objects.isNull(user) && user.getId() == 0) {
            validateText(user.getUsername(), user.getPassword(), user.getEmail(), user.getEmail());
            Optional<User> existedUser = users.stream().filter(u -> u.getUsername().equals(user.getUsername())).findFirst();
            if (existedUser.isPresent()) return false;
            repository.save(user);
//            users.add(user);
            return true;
        }
        return false;
    }

    @Override
    public User getByName(String username) {
        return null;
    }

    @Override
    public User getByUsername(String username) {
        User user = repository.findByUsername(username);
        return user;
    }

    @Override
    public boolean checkLogin(User user) {
        for (User existedUser : findAll()) {
            if (user.getUsername().equals(existedUser.getUsername()) && user.getPassword().equals(existedUser.getPassword())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LoginResponseDto returnLogin(User user) {
        return null;
    }



//    public static void main(String[] args) {
//        System.err.println(JWTUtils.genToken(users.get(0)));
//
//        JWTUtils jwtUtils = new JWTUtils();
//        String token = JWTUtils.genToken(users.get(0));
//        System.err.println(jwtUtils.getExpireDateFormToken(token));
//    }

}
