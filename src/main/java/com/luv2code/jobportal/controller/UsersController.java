package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.entity.UsersType;
import com.luv2code.jobportal.repository.UsersTypeRepository;
import com.luv2code.jobportal.services.UsersTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class UsersController {
    private final UsersTypeService usersTypeService;
    @Autowired
    public UsersController(UsersTypeService usersTypeService) {
        this.usersTypeService = usersTypeService;
    }
    public String register(Model model) {
        List<UsersType> usersType=usersTypeService.getAll();
        model.addAttribute("getAllTypes", usersType);
        model.addAttribute("user", new Users());
        return "register";

    }
}
