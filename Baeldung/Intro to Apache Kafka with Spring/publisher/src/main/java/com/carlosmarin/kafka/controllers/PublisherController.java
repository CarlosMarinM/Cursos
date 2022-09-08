package com.carlosmarin.kafka.controllers;

import com.carlosmarin.kafka.models.Greeting;
import com.carlosmarin.kafka.services.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PublisherController {

    @Autowired
    private Publisher publisher;

    @PostMapping("/message")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        publisher.send(message);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/greeting")
    public ResponseEntity<?> sendMessage(@RequestBody Greeting greeting) {
        publisher.send(greeting);
        return ResponseEntity.ok(greeting);
    }

    @PostMapping("/object")
    public <T> ResponseEntity<?> sendMessage(@RequestBody T object) {
        publisher.send(object);
        return ResponseEntity.ok(object);
    }
}
