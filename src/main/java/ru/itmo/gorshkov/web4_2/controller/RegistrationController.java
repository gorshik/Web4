package ru.itmo.gorshkov.web4_2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.gorshkov.web4_2.data.MyUser;
import ru.itmo.gorshkov.web4_2.data.UserRepository;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    private final UserRepository repository;

    @Autowired
    public RegistrationController(UserRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody MyUser user) {
        MyUser userFromDb = repository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return new ResponseEntity("NOT_OK", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(user.getPassword());
        repository.save(user);
        return new ResponseEntity("OK", HttpStatus.OK);
    }
}
