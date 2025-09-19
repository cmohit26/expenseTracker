package com.mohit.expense_backend.controllers;

import com.mohit.expense_backend.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@CrossOrigin
public class MainController {

    @GetMapping("")
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String displayLoginRegisterPage(Model model) {
        model.addAttribute("user", new User()); // for Thymeleaf form binding
        return "/UserPages/login-register";
    }


}
