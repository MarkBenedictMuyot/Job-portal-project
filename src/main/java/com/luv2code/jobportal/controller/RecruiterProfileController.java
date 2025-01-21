package com.luv2code.jobportal.controller;

import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.UsersRepository;
import com.luv2code.jobportal.services.RecruiterProfileService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/recruiter-profile")
public class RecruiterProfile {
    private final UsersRepository usersRepository;
    private RecruiterProfileService recruiterProfileService;

    public RecruiterProfile(UsersRepository usersRepository,RecruiterProfileService recruiterProfileService) {
        this.usersRepository = usersRepository;
        this.recruiterProfileService = recruiterProfileService;
    }
    public String recruiterProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            Users users=usersRepository.findByEmail(currentUsername).orElseThrow(()-> new UsernameNotFoundException("Could not found user"));
            Optional<com.luv2code.jobportal.entity.RecruiterProfile> recruiterProfile=recruiterProfileService.getOne(users.getUserId());
            if(!recruiterProfile.isEmpty())
                model.addAttribute("profile",recruiterProfile.get());
        }
        return "recruiter-profile";
    }
}
