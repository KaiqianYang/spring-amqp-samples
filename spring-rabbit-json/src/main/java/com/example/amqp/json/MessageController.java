package com.example.amqp.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class MessageController {

    @Autowired
    private DefaultProducer producer;

    @GetMapping("/sendMessages")
    public ResponseEntity<String> sendMessages(@RequestParam String queue, @RequestParam String message) {
        if (queue.equalsIgnoreCase("foo")) {
            producer.sendMessage(Application.FOO_QUEUE, message);
        } else if (queue.equalsIgnoreCase("bar")) {
            producer.sendMessage(Application.BAR_QUEUE, message);
        } else {
            return ResponseEntity.badRequest().body("Invalid queue name");
        }
        return ResponseEntity.ok("Message sent to " + queue + " queue");
    }
}