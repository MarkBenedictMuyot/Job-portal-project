package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.entity.UsersType;
import com.luv2code.jobportal.repository.UsersTypeRepository;
import com.luv2code.jobportal.services.UserService;
import com.luv2code.jobportal.services.UsersTypeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UsersController {
    private final UsersTypeService usersTypeService;
    private final UserService userService;

    @Autowired
    public UsersController(UsersTypeService usersTypeService, UserService userService) {
        this.usersTypeService = usersTypeService;
        this.userService = userService;
    }
    @GetMapping("/register")
    public String register(Model model) {
        List<UsersType> usersType=usersTypeService.getAll();
        model.addAttribute("getAllTypes", usersType);
        model.addAttribute("user", new Users());
        return "register";

    }
    @PostMapping("/register/new")
    public String userRegistration(@Valid Users user, Model model) {
        Optional<Users> optionalUser=userService.getUserByEmail(user.getEmail());
        if(optionalUser.isPresent()) {
            model.addAttribute("error", "Email already used, login in or reg with other email");
            List<UsersType> usersType=usersTypeService.getAll();
            model.addAttribute("getAllTypes", usersType);
            model.addAttribute("user", new Users());
            return "register";


        }
        System.out.println("User:: "+user);
        userService.addNew(user);
        return "redirect:/dashboard/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {
new SecurityContextLogoutHandler().logout(request, response, authentication);

        }

        return "redirect:/";
    }
}
