package com.confessly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String home() {
        return "confessly";
    }

    @GetMapping("/confessly")
    public String confessly() {
        return "confessly";
    }
} 