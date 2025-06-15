package com.confessly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "confessly";
    }

    @GetMapping("/confessly")
    public String confessly() {
        return "confessly";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
} 