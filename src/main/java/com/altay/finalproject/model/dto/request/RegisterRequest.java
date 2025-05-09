package com.altay.finalproject.model.dto.request;


import com.altay.finalproject.model.entity.AppUser.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private Role role; // LIBRARIAN or PATRON
}
