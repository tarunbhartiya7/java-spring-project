package com.example.demo.controllers;

import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.ChangePasswordRequest;
import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;

    // @RequestMapping("/users")
    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestHeader(value = "X-Auth-Token", required = false) String authToken,
            @RequestParam(required = false, defaultValue = "id") String sort) {
        System.out.println("Auth Token: " + authToken);
        if (!Set.of("id", "name", "email").contains(sort)) {
            throw new IllegalArgumentException("Invalid sort parameter");
        }
        return userRepository.findAll(Sort.by(sort).descending()).stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword()))
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return ResponseEntity.notFound().build();
        }

        // return new ResponseEntity<>(user, HttpStatus.OK);
        var userDto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok(userDto);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
        // var user = new User();
        // user.setName(name);
        // user.setEmail(email);
        // userRepository.save(user);
        // var userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        // return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto data) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.setName(data.getName());
        user.setEmail(data.getEmail());
        userRepository.save(user);
        var userDto = new UserDto(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        if (!user.getPassword().equals(request.getOldPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        user.setPassword(request.getNewPassword()); // Assuming User entity has a setPassword method
        userRepository.save(user);
        return ResponseEntity.noContent().build();
    }

}
