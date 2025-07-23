package com.edutrack.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.edutrack.dto.ApiResponse;
import com.edutrack.entities.StudentProfile;
import com.edutrack.entities.TeacherProfile;
import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.edutrack.token.VerificationToken;
import com.edutrack.token.VerificationTokenRepository;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService; // Inyección de dependencia del servicio encargado de la lógica de
                                     // autenticación.

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private AuthServiceImpl userService;

    private final UserRepository userRepository;

    @PostMapping("/login") // Define que este método manejará solicitudes POST a "/api/auth/login".
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDto) {
        try {
            var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());

            var authResponseDTO = new AuthResponseDTO(jwtToken, AuthStatus.LOGIN_SUCCESS, "Inicio de sesión exitoso.");

            // Llama al servicio para autenticar al usuario y generar un token JWT
            return ResponseEntity.status(HttpStatus.OK)
                    .body(authResponseDTO);

        } catch (Exception e) {
            String errorMessage = e.getMessage();

            AuthStatus status = AuthStatus.LOGIN_FAILED;

            if (errorMessage.contains("Usuario no encontrado")) {
                errorMessage = "Usuario no encontrado";
            } else if (errorMessage.contains("La cuenta no ha sido verificada")) {
                errorMessage = "La cuenta no ha sido verificada. Por favor, revise su correo electrónico.";
            } else if (errorMessage.contains("Bad credentials")) {
                errorMessage = "Usuario o contraseña incorrectos";
            }

            var authResponseDTO = new AuthResponseDTO(null, status, errorMessage);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(authResponseDTO);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> signUp(@RequestBody AuthRequestDTO authRequestDTO) {
        try {
            var jwtToken = authService.signUp(authRequestDTO.name(), authRequestDTO.lastname(),
                    authRequestDTO.username(), authRequestDTO.password(), authRequestDTO.email(),
                    authRequestDTO.birthdate(), authRequestDTO.userType(), authRequestDTO.institutionId());

            var authResponseDTO = new AuthResponseDTO(jwtToken, AuthStatus.USER_CREATED_SUCCESSFULLY,
                    "Usuario registrado exitosamente.");

            return ResponseEntity.status(HttpStatus.OK)
                    .body(authResponseDTO);
        } catch (Exception e) {

            String errorMessage = e.getMessage();
            AuthStatus status = AuthStatus.USER_NOT_CREATED;
            e.printStackTrace();

            if (e.getMessage().contains("Username already exists")) {
                errorMessage = "El nombre de usuario ya está en uso.";
            } else if (e.getMessage().contains("Email already exists")) {
                errorMessage = "El correo electrónico ya está registrado.";
            }

            var authResponseDTO = new AuthResponseDTO(null, status, errorMessage);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(authResponseDTO);
        }

    }

    @GetMapping("/verifyEmail")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        // Imprime el token recibido en la consola para verificación
        System.out.println("Recibida solicitud de verificación para token: " + token);

        try {
            // Busca el token de verificación en la base de datos utilizando el repositorio
            VerificationToken theToken = tokenRepository.findByToken(token);

            // Si no se encuentra el token, redirige al usuario a la página de verificación
            // con un mensaje de error
            if (theToken == null) {
                System.out.println("Token no encontrado");
                response.sendRedirect("http://localhost:5173/verification?status=invalid-token");
                return;
            }

            // Valida el token y guarda el resultado
            String result = userService.validateToken(token);

            // Imprime el resultado de la validación en la consola para saber si es válido,
            // expirado, etc.
            System.out.println("Resultado de la validación: " + result);

            // Dependiendo del resultado de la validación, redirige al usuario a diferentes
            // páginas
            switch (result) {
                case "valido":
                    // Si el token es válido, redirige con un mensaje de éxito
                    response.sendRedirect("http://localhost:5173/verification?status=success");
                    break;
                case "expired":
                    // Si el token ya ha expirado, redirige con un mensaje de expiración
                    response.sendRedirect("http://localhost:5173/verification?status=expired");
                    break;
                default:
                    // Si el token no es válido, redirige con un mensaje de error
                    response.sendRedirect("http://localhost:5173/verification?status=invalid-token");
            }
        } catch (Exception e) {
            // Si ocurre algún error durante el proceso de verificación, se captura la
            // excepción
            // Se imprime el mensaje del error en la consola y se redirige al usuario a una
            // página de error
            System.out.println("Error durante la verificación: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect("http://localhost:5173/verification?status=error");
        }
    }

    @GetMapping("/profile-status")
    public ResponseEntity<?> getProfileStatus(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

         boolean needsProfileCompletion = !user.hasCompleteProfile();

            Map<String, Object> status = Map.of(
                    "userType", user.getUserType().toString(),
                    "hasCompleteProfile", user.hasCompleteProfile(),
                    "needsProfileCompletion", needsProfileCompletion);

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al verificar estado del perfil");
        }
    }

    @GetMapping("/my-complete-profile")
    public ResponseEntity<?> getCompleteProfile(Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Map<String, Object> completeProfile = new HashMap<>();

            // Información personal (común para todos los roles)
            Map<String, Object> personalInfo = Map.of(
                    "username", user.getUsername(),
                    "name", user.getName(),
                    "lastname", user.getLastname(),
                    "email", user.getEmail(),
                    "birthdate", user.getBirthdate(),
                    "userType", user.getUserType().toString());
            completeProfile.put("personalInfo", personalInfo);

            // Información específica del rol
            if (user.getUserType() == UserType.TEACHER && user.getTeacherProfile() != null) {
                TeacherProfile teacherProfile = user.getTeacherProfile();
                Map<String, Object> professionalInfo = Map.of(
                        "especialization", teacherProfile.getSpecialization(),
                        "title", teacherProfile.getTitle(),
                        "yearsExperience", teacherProfile.getYearsExperience(),
                        "biography", teacherProfile.getBiography() != null ? teacherProfile.getBiography() : "",
                        "cvUrl", teacherProfile.getCvUrl() != null ? teacherProfile.getCvUrl() : "");
                completeProfile.put("professionalInfo", professionalInfo);
            }

            if (user.getUserType() == UserType.STUDENT && user.getStudentProfile() != null) {
                StudentProfile studentProfile = user.getStudentProfile();
                Map<String, Object> professionalInfo = Map.of(
                        "id", studentProfile.getId(),
                        "academicLevel", studentProfile.getGrade(),
                        "biography", studentProfile.getBiography() != null ? studentProfile.getBiography() : "");
                completeProfile.put("professionalInfo", professionalInfo);
            }

            return ResponseEntity.ok(new ApiResponse("Perfil completo obtenido", completeProfile));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error al obtener el perfil"));
        }
    }
}
