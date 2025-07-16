package com.example.brieflog.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthyCheckController {
    @GetMapping("/")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}