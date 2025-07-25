package com.edutrack.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutrack.dto.request.UserPersonalInfoDTO;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User save(User student) {
        return this.userRepository.save(student);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    public User update(Long id, User user) {
        User u = this.userRepository.findById(id).get();
        u.setName(user.getName());
        u.setLastname(user.getLastname());
        u.setEmail(user.getEmail());
        u.setBirthdate(user.getBirthdate());
        u.setPassword(passwordEncoder.encode(user.getPassword()));

        return this.userRepository.save(u);
    }

    // Método específico para buscar por email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public boolean needsProfileCompletion(String username) {
        User user = findByUsername(username);
        return user.getUserType() == UserType.TEACHER && !user.hasCompleteProfile();
    }

    @Transactional
    public User updatePersonalInfo(Long userId, UserPersonalInfoDTO personalInfoDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Verificar si el nuevo username ya existe (excluyendo el usuario actual)
        if (userRepository.existsByUsernameAndIdNot(personalInfoDTO.getUsername(), userId)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        // Verificar si el nuevo email ya existe (excluyendo el usuario actual)
        if (userRepository.existsByEmailAndIdNot(personalInfoDTO.getEmail(), userId)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Actualizar campos
        user.setUsername(personalInfoDTO.getUsername());
        user.setName(personalInfoDTO.getName());
        user.setLastname(personalInfoDTO.getLastname());
        user.setEmail(personalInfoDTO.getEmail());
        user.setBirthdate(personalInfoDTO.getBirthdate());

        return userRepository.save(user);
    }

}
