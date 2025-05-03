package com.example.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Message;

@RestController
public class MessageController {
    // @RequestMapping("/message")
    // public String getMessage() {
    // return "Hello, this is a message from the server!";
    // }
    @RequestMapping("/message")
    public Message getMessage() {
        return new Message("Hello, this is a message from the server!");
    }

}
