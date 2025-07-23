package com.edutrack.entities.enums;

public enum UserType {
    STUDENT("ROLE_STUDENT"),
    TEACHER("ROLE_TEACHER"),
    PARENT("ROLE_PARENT"),
    INSTITUTION_ADMIN("ROLE_INSTITUTION_ADMIN"),
    ADMIN("ROLE_ADMIN");
    
    private final String displayName;
    
    UserType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    // Método opcional para buscar por displayName
    public static UserType fromDisplayName(String displayName) {
        for (UserType type : values()) {
            if (type.displayName.equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de usuario no válido: " + displayName);
    }
}