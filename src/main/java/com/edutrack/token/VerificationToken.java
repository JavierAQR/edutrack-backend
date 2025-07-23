package com.edutrack.token;

import java.util.Calendar;
import java.util.Date;

import com.edutrack.entities.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // El token en sí, que se almacenará en la base de datos.
    private String token;
    
    // La fecha de expiración del token, se asegura de que no sea nula.
    @Column(nullable = false)
    private Date expirationTime;
    
    // Tiempo de expiración del token en minutos (valor de 30 minutos por defecto).
    private static final int EXPIRATION_TIME = 30;
    
    // Relación uno a uno con el usuario, que se obtiene de manera 'eager' (siempre se carga cuando se obtiene el token).
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    // Constructor que recibe un token y un usuario, calcula el tiempo de expiración del token.
    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationTime();
    }

    // Constructor que recibe solo el token y calcula el tiempo de expiración.
    public VerificationToken(String token) {
        this.token = token;
        this.expirationTime = calculateExpirationTime();
    }

    // Método que calcula el tiempo de expiración del token añadiendo minutos al momento actual.
    private Date calculateExpirationTime() {
        Calendar calendar = Calendar.getInstance();  // Obtiene el calendario actual.
        calendar.setTimeInMillis(new Date().getTime());  // Establece el tiempo actual.
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);  // Añade el tiempo de expiración.
        return calendar.getTime();  // Devuelve la nueva fecha de expiración.
    }
}
