package com.edutrack.auth;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edutrack.entities.User;
import com.edutrack.entities.enums.UserType;
import com.edutrack.listener.RegistrationCompleteEvent;
import com.edutrack.repositories.UserRepository;
import com.edutrack.token.VerificationToken;
import com.edutrack.token.VerificationTokenRepository;
import com.edutrack.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private AuthenticationManager authenticationManager;// Se encarga de autenticar al usuario con su nombre de usuario y contraseña.
    @Autowired
    private PasswordEncoder passwordEncoder; // Codifica las contraseñas antes de guardarlas en la base de datos.
    @Autowired
    private UserRepository userRepository; // Repositorio que interactúa con la base de datos para las operaciones relacionadas con el usuario.

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Override
    public String login(String username, String password) {

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = optionalUser.get();

        // Verifica si el usuario está habilitado
        if (!user.getEnabled()) {
            throw new RuntimeException("La cuenta no ha sido verificada");
        }

        // Crea un token de autenticación con el nombre de usuario y la contraseña
        var authToken = new UsernamePasswordAuthenticationToken(username, password);

        // Autentica al usuario utilizando el AuthenticationManager
        var authenticate = authenticationManager.authenticate(authToken);

        // Genera y devuelve un token JWT utilizando el nombre de usuario autenticado.
        return JwtUtils.generateToken(((UserDetails) (authenticate.getPrincipal())).getUsername(), user.getUserType());
    }

    @Override
    public String signUp(String name, String lastname, String username, String password, String email, LocalDate birthdate, UserType userType) {

        // Verifica si el nombre de usuario ya existe en la base de datos.
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("El Username ya existe");  // Lanza una excepción si el nombre de usuario ya existe.
        }
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("El correo electrónico ya existe");
        }
        // Crear un nuevo objeto Usuario
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // La contraseña se cifra antes de guardarse
        user.setBirthdate(birthdate);
        user.setName(name);
        user.setLastname(lastname);
        user.setEmail(email); 
        user.setEnabled(false);
        user.setUserType(userType);

        user = userRepository.save(user);

        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, "..."));

        return "verifica tu email";
    }

    @Override
    public Optional<User> findByEmail (String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token){
        // Crea un nuevo token de verificación para el usuario proporcionado.
        // Se utiliza una clase llamada VerificationToken para almacenar el token y el usuario asociado.
        var verificationToken = new VerificationToken(token, theUser);
        // Guarda el token de verificación en el repositorio correspondiente.
        tokenRepository.save(verificationToken);
    }

    @Override
    public String verifyToken(String token) {
        // Obtiene el nombre de usuario del token.
        var usernameOptional = JwtUtils.getUsernameFromToken(token);
        // Si el token es válido, devuelve el nombre de usuario.
        if (usernameOptional.isPresent()) {
            return usernameOptional.get();
        }
        // Si el token no es válido, lanza una excepción.
        throw new RuntimeException("Token invalid");
    }

    @Override
    public String validateToken(String theToken) {
        // Busca el token de verificación en el repositorio utilizando su valor.
        VerificationToken token = tokenRepository.findByToken(theToken);
        if (token == null) {
            // Si el token no se encuentra, imprime un mensaje en la consola y retorna un mensaje de error.
            System.out.println("Token no encontrado en la base de datos.");
            return "Token de verificación no válido";
        }

        // Obtiene el usuario asociado al token.
        User user = token.getUser();
        System.out.println("Estado actual del usuario: " + user.getEnabled());

        // Verifica si el token ha expirado comparando la fecha de expiración con la fecha actual.
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            // Si el token ha expirado, lo elimina del repositorio y retorna un mensaje de expiración.
            tokenRepository.delete(token);
            System.out.println("Token expirado y eliminado.");
            return "expired";
        }

        // Si el token es válido, habilita al usuario cambiando su estado a activo.
        user.setEnabled(true);
        try {
            // Guarda los cambios en el usuario en el repositorio.
            userRepository.save(user);
            System.out.println("Usuario actualizado. Nuevo estado: " + user.getEnabled());
            // Elimina el token usado después de completar la verificación.
            tokenRepository.delete(token);
            return "valido";
        } catch (Exception e) {
            // Si ocurre un error al guardar el usuario, imprime el error en la consola y retorna un mensaje de error.
            System.out.println("Error al guardar usuario: " + e.getMessage());
            e.printStackTrace();
            return "Error al actualizar usuario";
        }
    }

    //Nuevo
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
