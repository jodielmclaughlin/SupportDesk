package io.nology.supportdesk.user;

import io.nology.supportdesk.user.dtos.UserResponseDTO;
import io.nology.supportdesk.user.entity.User;
import io.nology.supportdesk.user.entity.UserRole;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserResponseDTOTest {

    @Test
    void fromEntity_doesNotExposePassword() {
        User user = new User();
        user.setId(1L);
        user.setName("Fake");

        user.setEmail("fake@example.com");
        user.setPasswordHash("encodedPassword123");
        user.setRole(UserRole.REQUESTER);

        UserResponseDTO dto = UserResponseDTO.fromEntity(user);

        assertEquals(1L, dto.id());
        assertEquals("Fake", dto.name());
        assertEquals("fake@example.com", dto.email());
        assertEquals(UserRole.REQUESTER, dto.role());

        assertFalse(
                java.util.Arrays.stream(dto.getClass().getRecordComponents())
                        .anyMatch(component ->
                                component.getName().equals("password"))
        );
    }
}
