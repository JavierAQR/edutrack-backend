package com.edutrack.listener;

import org.springframework.context.ApplicationEvent;

import com.edutrack.entities.User;

public class RegistrationCompleteEvent extends ApplicationEvent{
    
    private User user;
    private String applicationUrl;

    public RegistrationCompleteEvent(User user, String applicationUrl){
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }

     // Método para obtener el usuario asociado al evento.
     public User getUser() {
        return user; // Retorna el usuario asociado.
    }

    // Método para establecer el usuario asociado al evento.
    public void setUser(User user) {
        this.user = user; // Asigna el nuevo usuario.
    }

    // Método para obtener la URL base de la aplicación.
    public String getApplicationUrl() {
        return applicationUrl; // Retorna la URL de la aplicación.
    }

    // Método para establecer la URL base de la aplicación.
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl; // Asigna la nueva URL de la aplicación.
    }
}
