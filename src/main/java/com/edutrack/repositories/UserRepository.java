package com.edutrack.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);
    
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    Optional<User> findByEmail(String email);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType")
    List<User> findByUserType(@Param("userType") UserType userType);

    Optional<User> findByEmailIgnoreCase(String email);

    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.id != :userId")
    boolean existsByUsernameAndIdNot(@Param("username") String username, @Param("userId") Long userId);
    
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.id != :userId")
    boolean existsByEmailAndIdNot(@Param("email") String email, @Param("userId") Long userId);

    List<User> findByInstitutionIdAndUserType(Long institutionId, UserType userType);

    Optional<User> findByInstitutionIdAndIdAndUserType(Long institutionId, Long id, UserType userType);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.studentProfile sp LEFT JOIN FETCH sp.grade g LEFT JOIN FETCH g.academicLevel WHERE u.institution.id = :institutionId AND u.userType = :userType")
    List<User> findByInstitutionIdAndUserTypeWithProfile(@Param("institutionId") Long institutionId,
                                                         @Param("userType") UserType userType);
}
