package com.edutrack.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.edutrack.dto.ApiResponse;
import com.edutrack.dto.request.UserCreateDTO;
import com.edutrack.dto.request.UserPersonalInfoDTO;
import com.edutrack.dto.request.UserUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edutrack.entities.User;
import com.edutrack.repositories.UserRepository;
import com.edutrack.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @GetMapping()
    @Transactional(readOnly = true)
    public List<User> findAllStudents() {
        return this.userService.findAll();
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO userDTO) {
        try {
            // Validar que el email no exista
            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity.badRequest().body("El email ya está registrado");
            }

            // Mapear DTO a entidad User
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setName(userDTO.getName());
            user.setLastname(userDTO.getLastname());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hashear la contraseña
            user.setBirthdate(userDTO.getBirthdate());
            user.setUserType(userDTO.getUserType());
            user.setEnabled(false); // Requerirá activación

            User savedUser = userService.save(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al crear el usuario");
        }
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public User getUserById(@PathVariable Long id) {
        return this.userService.findById(id);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UserUpdateDTO userDTO) {
        Optional<User> userOpt = Optional.ofNullable(this.userService.findById(id));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setUsername(userDTO.getUsername());
            user.setName(userDTO.getName());
            user.setLastname(userDTO.getLastname());
            user.setUserType(userDTO.getUserType());
            return ResponseEntity.ok(this.userService.save(user));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            if (!userService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al eliminar el usuario");
        }
    }

    @PutMapping("/update-personal-info")
    @Transactional
    public ResponseEntity<?> updatePersonalInfo(
            @RequestBody @Valid UserPersonalInfoDTO personalInfoDTO,
            Authentication authentication) {

        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            User updatedUser = userService.updatePersonalInfo(user.getId(), personalInfoDTO);

            Map<String, Object> response = Map.of(
                    "username", updatedUser.getUsername(),
                    "name", updatedUser.getName(),
                    "lastname", updatedUser.getLastname(),
                    "email", updatedUser.getEmail(),
                    "birthdate", updatedUser.getBirthdate());

            return ResponseEntity.ok(new ApiResponse("Información personal actualizada exitosamente", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al actualizar información personal: " + e.getMessage()));
        }
    }
}
