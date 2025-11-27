package com.prou.taskmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Task Management API! <br>" +
               "Use <a href='/api/employees'>/api/employees</a> to manage employees <br>" +
               "Use <a href='/api/tasks'>/api/tasks</a> to manage tasks";
    }
}
