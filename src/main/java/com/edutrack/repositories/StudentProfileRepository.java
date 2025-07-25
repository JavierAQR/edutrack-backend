package com.edutrack.repositories;

import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUser(User user);
    Optional<StudentProfile> findByUserId(Long userId);
    boolean existsByUser(User user);
    boolean existsByUserId(Long userId);
    void deleteByUser(User user);
}