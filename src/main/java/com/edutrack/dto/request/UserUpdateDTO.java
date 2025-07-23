package com.edutrack.dto.request;

import com.edutrack.entities.Institution;
import com.edutrack.entities.enums.UserType;
import lombok.Data;

@Data
public class UserUpdateDTO {
    private String username;
    private String name;
    private String lastname;
    private UserType userType;
    private Institution institution;
}
