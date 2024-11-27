package com.example.amqp.json;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private TransactionMessageProducer producer;

    @PostMapping("/transactions")
    public ResponseEntity<String> sendTransaction(@RequestBody Transaction transaction) {
        try {
            producer.sendTransaction(transaction);
            return ResponseEntity.ok("Transaction sent successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send transaction: " + e.getMessage());
        }
    }
}