package com.luv2code.jobportal.services;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import com.luv2code.jobportal.entity.RecruiterProfile;
import com.luv2code.jobportal.entity.Users;
import com.luv2code.jobportal.repository.JobSeekerRepository;
import com.luv2code.jobportal.repository.RecruiterProfileRepository;
import com.luv2code.jobportal.repository.UsersRepository;
import com.luv2code.jobportal.repository.UsersTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final RecruiterProfileRepository recruiterProfileRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService(UsersRepository usersRepository,JobSeekerRepository jobSeekerRepository,RecruiterProfileRepository recruiterProfileRepository,PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.recruiterProfileRepository = recruiterProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public Users addNew(Users users){
        users.setActive(true);
        users.setRegistrationDate(new Date(System.currentTimeMillis()));
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        Users savedUser=usersRepository.save(users);
        int userTypeId=users.getUserTypeId().getUserTypeId();
        if(userTypeId==1){
            recruiterProfileRepository.save(new RecruiterProfile(savedUser));
        }else{
            jobSeekerRepository.save(new JobSeekerProfile(savedUser));
        }

        return savedUser;
    }

    public Optional<Users> getUserByEmail(String email){
        return usersRepository.findByEmail(email);
    }

    public Object getCurrentUserProfile() {

        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
        String username=authentication.getName();
        Users users=usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
        int userId=users.getUserId();
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("Recruiter"))) {
            RecruiterProfile recruiterProfile = recruiterProfileRepository.findById(userId).orElse(new RecruiterProfile());
            return recruiterProfile;
        } else {
            JobSeekerProfile jobSeekerProfile=jobSeekerRepository.findById(userId).orElse(new JobSeekerProfile());
            return jobSeekerProfile;
        }
        }
        return null;
    }

    public Users getCurrentUser() {
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username=authentication.getName();
            Users user= usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));
            return user;
        }
        return null;
    }
}
