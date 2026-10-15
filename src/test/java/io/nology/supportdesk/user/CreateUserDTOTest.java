package io.nology.supportdesk.user;

import io.nology.supportdesk.user.dtos.CreateUserDTO;
import io.nology.supportdesk.user.entity.UserRole;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserDTOTest {
    private final Validator validator;

    CreateUserDTOTest(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void createUserDTO_whenEmailIsBlank_failsValidation(){
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Fake User");
        dto.setEmail("");
        dto.setPasswordHash("some-password");
        dto.setRole(UserRole.REQUESTER);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath()
                        .toString().equals("email")));
    }

    @Test
    void createUserDTO_whenEmailFormatIsInvalid_failsValidation() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Fake User");
        dto.setEmail("not-an-email");
        dto.setPasswordHash("some-password");
        dto.setRole(UserRole.REQUESTER);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath()
                        .toString().equals("email")));
    }

    @Test
    void createUserDTO_whenFirstNameIsBlank_failsValidation() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName(" ");
        dto.setEmail("fake@example.com");
        dto.setPasswordHash("some-password");
        dto.setRole(UserRole.REQUESTER);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath()
                        .toString().equals("name")));
    }

    @Test
    void createUserDTO_whenPasswordIsMissing_failsValidation() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Fake");
        dto.setEmail("fake@example.com");
        dto.setPasswordHash(null);
        dto.setRole(UserRole.REQUESTER);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath()
                        .toString().equals("passwordHash")));
    }

    @Test
    void createUserDTO_whenRoleIsMissing_failsValidation() {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setName("Fake");
        dto.setEmail("fake@example.com");
        dto.setPasswordHash("password123");
        dto.setRole(null);

        var violations = validator.validate(dto);

        assertTrue(violations.stream()
                .anyMatch(violation -> violation.getPropertyPath()
                        .toString().equals("role")));
    }
}
