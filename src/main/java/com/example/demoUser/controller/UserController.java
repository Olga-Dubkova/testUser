package com.example.demoUser.controller;

import com.example.demoUser.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class UserController {
    private Map<String, User> users = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @GetMapping("/getString")
    public ResponseEntity<String> getStaticJson() {
        try {
            String jsonResponse = "{\"message\": \"Static JSON\"}";
            // Возвращаем JSON-ответ
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<String> getAllUsers() {
        try{
            Collection<User> userList = users.values();

            if (userList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            String jsonResponse = objectMapper.writeValueAsString(userList);
            return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
        }catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody String requestBody) {
        try {
            User user = objectMapper.readValue(requestBody, User.class);

            if (users.containsKey(user.getLogin())) {
                return new ResponseEntity<>("Логин уже занят", HttpStatus.BAD_REQUEST);
            }
            user.setRegistrationDate(LocalDateTime.now());
            users.put(user.getLogin(), user);

            String jsonString = objectMapper.writeValueAsString(user);

            return new ResponseEntity<>(jsonString, HttpStatus.CREATED);
        } catch (Exception ex) {
            ex.printStackTrace(); // логи
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
