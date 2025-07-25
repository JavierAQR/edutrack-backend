package com.edutrack.controllers;

import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/administrators")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllAdmins() {
        return userRepository.findByUserType(UserType.ADMIN);
    }

    @PostMapping
    public User createAdmin(@RequestBody User user) {
        user.setUserType(UserType.ADMIN);
        user.setEnabled(true); // Si tu modelo lo requiere
        // Valida que birthdate y password no sean null
        if (user.getBirthdate() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("birthdate y password son obligatorios");
        }
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateAdmin(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) return ResponseEntity.notFound().build();

        User user = optionalUser.get();
        user.setName(userDetails.getName());
        user.setLastname(userDetails.getLastname());
        user.setEmail(userDetails.getEmail());
        user.setUsername(userDetails.getUsername());
        // Puedes agregar más campos según tu modelo
        return ResponseEntity.ok(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.notFound().build();
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}