package io.nology.supportdesk.user.dtos;

import io.nology.supportdesk.user.entity.User;
import io.nology.supportdesk.user.entity.UserRole;

import java.util.List;

public record UserResponseDTO(Long id, String name, String email, UserRole role) {
    public static UserResponseDTO fromEntity(User user){
        return new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getRole());
    }

    public static List<UserResponseDTO> fromEntity(List<User> userList){
        return userList.stream().map(UserResponseDTO::fromEntity).toList();
    }
}
