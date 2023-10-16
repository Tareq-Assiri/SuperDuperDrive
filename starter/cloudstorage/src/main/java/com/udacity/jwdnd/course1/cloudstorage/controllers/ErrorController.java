package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class ErrorController {

    @GetMapping
    public String errorRedirect() {
        return "redirect:/result?error";
    }

    @PostMapping
    public String errorPostRedirect() {
        return "redirect:/result?error";
    }
}
