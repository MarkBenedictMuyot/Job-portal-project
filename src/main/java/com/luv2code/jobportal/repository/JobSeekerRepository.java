package com.luv2code.jobportal.repository;

import com.luv2code.jobportal.entity.JobSeekerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeekerProfile,Integer> {
}
