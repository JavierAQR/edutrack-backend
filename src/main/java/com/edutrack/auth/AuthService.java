package com.edutrack.auth;

import java.time.LocalDate;
import java.util.Optional;


import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;

public interface AuthService {

    String login(String username, String password);
    String signUp(String name, String lastname, String username, String password, String email, LocalDate birthdate, UserType userType);
    String verifyToken(String token);
    Optional<User> findByEmail (String email);

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken (String theToken);

    User getUserByUsername(String username); //Nuevo
}
