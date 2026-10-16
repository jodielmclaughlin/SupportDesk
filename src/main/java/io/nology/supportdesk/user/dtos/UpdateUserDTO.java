package io.nology.supportdesk.user.dtos;

import io.nology.supportdesk.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;


public class UpdateUserDTO {
    @Pattern(regexp = ".*\\S.*", message = "Name must not be empty")
    private String name;

    @Pattern(regexp = ".*\\S.*", message = "Email must not be empty")
    @Email(message = "Email must be valid")
    private String email;

    private UserRole role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
